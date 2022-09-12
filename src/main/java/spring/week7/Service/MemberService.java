package spring.week7.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import spring.week7.Dto.Request.LoginRequestDto;
import spring.week7.Dto.Request.MemberImageRequestDto;
import spring.week7.Dto.Request.MemberRequestDto;
import spring.week7.Dto.Response.MemberResponseDto;
import spring.week7.Dto.TokenDto;
import spring.week7.Errorhandler.BusinessException;
import spring.week7.Jwt.TokenProvider;
import spring.week7.Repository.FollowRepository;
import spring.week7.Repository.MemberRepository;
import spring.week7.Util.S3Uploader;
import spring.week7.domain.Follow;
import spring.week7.domain.Member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static spring.week7.Errorhandler.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final S3Uploader s3Uploader;
    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;

    public void createMember(MemberRequestDto requestDto) {
        //만들때는 bool값을 true로
        isPresentMember(requestDto.getEmail(), true);
        Member member = new Member(requestDto, passwordEncoder);
        memberRepository.save(member);

    }

    public ResponseEntity<MemberResponseDto> login(LoginRequestDto requestDto, HttpServletResponse response) {
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
    @Transactional
    public ResponseEntity<?> mypage(String memberid, Member userDetails) {
        return null ;
    }



    @Transactional
    public void myImageUpload(MemberImageRequestDto memberImageRequestDto, MultipartFile image, Member member) throws IOException {

        if (!memberImageRequestDto.getMember().equals(member.getEmail())) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }
        String imageUrl = member.getImage();
        //이미지 존재시 먼저 삭제후 다시 업로드.
        if (imageUrl != null) {
            String deleteUrl = imageUrl.substring(imageUrl.indexOf("MemberImage"));
            s3Uploader.deleteImage(deleteUrl);
            imageUrl = s3Uploader.upload(image, "MemberImage");

        }

        member.update(imageUrl);
    }
    //팔로우 설정 해제.

    public void myfollow(String email, Member member) {
        if (email.equals(member.getEmail())) {
            throw new BusinessException("자신을 팔로우 할수 없습니다.",EMAIL_DUPLICATION);
        }
        Optional<Follow> follow1=followRepository.findByfollower(email);
        if(follow1.isEmpty()){
            Follow follow =new Follow(email,member.getEmail());
            followRepository.save(follow);
        }else{
            followRepository.deleteById(follow1.get().getId());
        }

    }
}
