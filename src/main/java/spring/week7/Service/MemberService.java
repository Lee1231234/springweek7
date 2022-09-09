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
import spring.week7.Jwt.TokenProvider;
import spring.week7.Repository.MemberRepository;
import spring.week7.domain.Member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    private final MemberRepository memberRepository;

    public ResponseEntity<?> createMember(MemberRequestDto requestDto) {
        if (null != isPresentMember(requestDto.getEmail())) {
            return  Request("회원가입 실패",false);
        }



        Member member = Member.builder()
                .email(requestDto.getEmail())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .build();
        memberRepository.save(member);
        return Request("회원가입 성공",true);
    }

    public ResponseEntity<?> login(LoginRequestDto requestDto, HttpServletResponse response) {
        Member member = isPresentMember(requestDto.getEmail());
        if (null == member) {
            return Request("로그인 실패",false);
        }

        if (!member.validatePassword(passwordEncoder, requestDto.getPassword())) {
            return Request("로그인 실패",false);
        }


        TokenDto tokenDto = tokenProvider.generateTokenDto(member);
        tokenToHeaders(tokenDto, response);

        return ResponseEntity.ok(
                MemberResponseDto.builder()
                        .ok(true)
                        .message("로그인 성공")
                        .Authorization("Bearer " + tokenDto.getAccessToken())
                        .RefreshToken(tokenDto.getRefreshToken())
                        .build()
        );
    }

    public ResponseEntity<?> logout(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return ResponseEntity.badRequest().body("Token이 유효하지 않습니다.");
        }
        Member member = tokenProvider.getMemberFromAuthentication();
        if (null == member) {
            return ResponseEntity.badRequest().body("사용자를 찾을 수 없습니다.");
        }

        return tokenProvider.deleteRefreshToken(member);
    }

    @Transactional(readOnly = true)
    public Member isPresentMember(String email) {
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        return optionalMember.orElse(null);
    }

    public void tokenToHeaders(TokenDto tokenDto, HttpServletResponse response) {
        response.addHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
        response.addHeader("Refresh-Token", tokenDto.getRefreshToken());
        response.addHeader("Access-Token-Expire-Time", tokenDto.getAccessTokenExpiresIn().toString());
    }
    public ResponseEntity<?> Request(String message,boolean bool){
        if(bool){
            return ResponseEntity.ok(
                    MemberResponseDto.builder()
                            .ok(bool)
                            .message(message)
                            .build()
            );
        }else{
            return ResponseEntity.badRequest().body(
                    MemberResponseDto.builder()
                            .ok(bool)
                            .message(message)
                            .build()
            );
        }

    }
}
