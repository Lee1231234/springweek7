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
import spring.week7.Dto.Response.MyPageResponseDto;
import spring.week7.Dto.TokenDto;
import spring.week7.Errorhandler.BusinessException;
import spring.week7.Jwt.TokenProvider;
import spring.week7.Repository.FollowRepository;
import spring.week7.Repository.MemberPostRestoreRepository;
import spring.week7.Repository.MemberRepository;
import spring.week7.Repository.PostRepository;
import spring.week7.Util.S3Uploader;
import spring.week7.domain.Follow;
import spring.week7.domain.Member;
import spring.week7.domain.MemberPostRestore;
import spring.week7.domain.Post;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
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
    private final PostRepository postRepository;
    private final MemberPostRestoreRepository memberPostRestoreRepository;
    public void createMember(MemberRequestDto requestDto) {
        //만들때는 bool값을 true로
        if(isPresentMember(requestDto.getEmail(), true)!=null){
            throw  new BusinessException("회원가입 실패",EMAIL_INPUT_INVALID);
        }
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
        if (!tokenProvider.validateToken(request.getHeader("RefreshToken"))) {
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
                    () -> new BusinessException("로그인 실패.", LOGIN_INPUT_INVALID)

            );
        } else {
            return optionalMember.orElse(null);
        }

    }

    public void tokenToHeaders(TokenDto tokenDto, HttpServletResponse response) {
        response.addHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
        response.addHeader("RefreshToken", tokenDto.getRefreshToken());
        response.addHeader("Access-Token-Expire-Time", tokenDto.getAccessTokenExpiresIn().toString());
    }

    //자신이 생성한 글 + 저장한 글 + 팔로워 수 모두를 Dto로 출력
    @Transactional
    public ResponseEntity<?> mypage(String memberid, Member userDetails) {
        Optional<Member> member =memberRepository.findByEmail(memberid);
        Optional<List<Post>> post;
        Optional<List<MemberPostRestore>> memberPostRestores;
        int follower = followRepository.countAllByFollower(memberid);
        int following = followRepository.countAllByFollowing(memberid);
        if(member.isPresent()) {
            post = postRepository.findAllByMember(member.get());
            memberPostRestores = memberPostRestoreRepository.findAllByMember(member.get());
        }else{
            throw new BusinessException("멤버가 존재하지 않습니다.",MEMBER_NOT_EXIST);
        }
        MyPageResponseDto myPageResponseDto=null;

        if(memberPostRestores.isPresent()&&post.isPresent()){
            myPageResponseDto = new MyPageResponseDto(post.get(),memberPostRestores.get(),follower,following);
        }else if(memberPostRestores.isPresent()){
            myPageResponseDto = new MyPageResponseDto(null,memberPostRestores.get(),follower,following);
        }else if(post.isPresent()){
            myPageResponseDto = new MyPageResponseDto(post.get(),null,follower,following);
        }


        return ResponseEntity.ok(myPageResponseDto) ;
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
        Optional<Member> member1 =memberRepository.findByEmail(email);
        member1.orElseThrow(()->new BusinessException("존재하지않는 이메일입니다",EMAIL_NOT_EXIST));
        Optional<Follow> follow1=followRepository.findByfollower(email);
        if(follow1.isEmpty()){
            Follow follow =new Follow(email,member.getEmail());
            followRepository.save(follow);
        }else{
            followRepository.deleteById(follow1.get().getId());
        }

    }
}
