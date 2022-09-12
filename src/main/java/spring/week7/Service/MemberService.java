package spring.week7.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.week7.Dto.Request.LoginRequestDto;
import spring.week7.Dto.Request.MemberRequestDto;
import spring.week7.Dto.Response.MemberResponseDto;
import spring.week7.Dto.TokenDto;
import spring.week7.Errorhandler.BusinessException;
import spring.week7.Jwt.TokenProvider;
import spring.week7.Repository.MemberRepository;
import spring.week7.domain.Member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static spring.week7.Errorhandler.ErrorCode.EMAIL_DUPLICATION;
import static spring.week7.Errorhandler.ErrorCode.JWT_NOT_PERMIT;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    private final MemberRepository memberRepository;

    public void createMember(MemberRequestDto requestDto) {
        //만들때는 bool값을 true로
        isPresentMember(requestDto.getEmail(), true);
        Member member = new Member(requestDto, passwordEncoder);
        memberRepository.save(member);

    }

    public ResponseEntity<?> login(LoginRequestDto requestDto, HttpServletResponse response) {
        //체크할때는 bool를 false로
        Member member = isPresentMember(requestDto.getEmail(), false);
        member.validatePassword(passwordEncoder, requestDto.getPassword());
        TokenDto tokenDto = tokenProvider.generateTokenDto(member);
        tokenToHeaders(tokenDto, response);
        return ResponseEntity.ok(new MemberResponseDto(tokenDto));
    }

    public ResponseEntity<?> logout(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            throw new BusinessException("잘못된 JWT 토큰입니다", JWT_NOT_PERMIT);
        }
        Member member = tokenProvider.getMemberFromAuthentication();

        return tokenProvider.deleteRefreshToken(member);
    }

    @Transactional(readOnly = true)
    public Member isPresentMember(String email, boolean bool) {
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        if (!bool) {
            return optionalMember.orElseThrow(
                    () -> new BusinessException("로그인 실패.", EMAIL_DUPLICATION)

            );
        } else {
            return optionalMember.orElse(null);
        }

    }

    public void tokenToHeaders(TokenDto tokenDto, HttpServletResponse response) {
        response.addHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
        response.addHeader("Refresh-Token", tokenDto.getRefreshToken());
        response.addHeader("Access-Token-Expire-Time", tokenDto.getAccessTokenExpiresIn().toString());
    }

}
