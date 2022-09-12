package spring.week7.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import spring.week7.Dto.Request.LoginRequestDto;

import spring.week7.Dto.Request.MemberImageRequestDto;
import spring.week7.Dto.Request.MemberRequestDto;
import spring.week7.Dto.Response.MemberResponseDto;

import spring.week7.Service.MemberService;
import spring.week7.domain.UserDetailsImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;


@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    //회원가입
    @PostMapping(value = "/api/member/signup")
    public ResponseEntity<MemberResponseDto> signup(@RequestBody @Valid MemberRequestDto requestDto) {
        memberService.createMember(requestDto);
        return ResponseEntity.ok(new MemberResponseDto(true, "로그인 성공"));
    }

    //로그인
    @PostMapping(value = "/api/member/login")
    public ResponseEntity<MemberResponseDto> login(@RequestBody @Valid LoginRequestDto requestDto, HttpServletResponse response) {
        return memberService.login(requestDto, response);
    }

    //로그아웃
    @GetMapping(value = "/api/auth/member/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        return memberService.logout(request);
    }

    //마이페이지
    @GetMapping(value = "/api/auth/member/mypage/{memberid}")
    public ResponseEntity<?> mypage(@PathVariable String memberid, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return memberService.mypage(memberid, userDetails.getMember());
    }

    //멤버 이미지 업로드
    @PostMapping(value = "/api/auth/member/mypage/imageupload")
    public ResponseEntity<?> myImageUpload(@RequestPart(value = "image") MultipartFile image,
                                           MemberImageRequestDto memberImageRequestDto,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        memberService.myImageUpload(memberImageRequestDto, image, userDetails.getMember());
        return ResponseEntity.ok("업로드 성공");
    }

    @PostMapping(value = "/api/auth/member/mypage/follow")
    public ResponseEntity<?> myfollow(@RequestBody String email,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        memberService.myfollow(email,userDetails.getMember());
        return ResponseEntity.ok("팔로우 성공");
    }
}