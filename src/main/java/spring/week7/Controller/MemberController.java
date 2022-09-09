package spring.week7.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import spring.week7.Dto.Request.LoginRequestDto;
import spring.week7.Dto.Request.MemberRequestDto;
import spring.week7.Dto.Response.MemberResponseDto;
import spring.week7.Service.MemberService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    @PostMapping(value = "/api/member/signup")
    public ResponseEntity<MemberResponseDto> signup(@RequestBody @Valid MemberRequestDto requestDto){
        memberService.createMember(requestDto);
        return ResponseEntity.ok(new MemberResponseDto(true,"로그인 성공"));
    }

    @PostMapping(value = "/api/member/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDto requestDto, HttpServletResponse response){
        return memberService.login(requestDto, response);
    }

    @PostMapping(value ="/api/auth/member/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        return memberService.logout(request);
    }



}