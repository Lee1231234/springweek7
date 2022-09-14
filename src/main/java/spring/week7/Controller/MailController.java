package spring.week7.Controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import spring.week7.Dto.Request.LoginRequestDto;
import spring.week7.Dto.Request.MailRequestDto;
import spring.week7.Service.EmailService;
import spring.week7.domain.UserDetailsImpl;

@Controller
public class MailController {

    private final EmailService emailService;


    public MailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/mail/send")
    public String main() {
        return "SendMail.html";
    }

    @PostMapping("/api/mail/send")
    public String sendMail(@RequestBody MailRequestDto mailDto,
                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        emailService.sendPasswordMessage(mailDto,userDetails.getMember());
        System.out.println("메일 전송 완료");
        return "AfterMail.html";
    }
    @PostMapping("/api/mail/confirm/{validNum}")
    public String confirmMail(@PathVariable int validNum,
                              @RequestBody LoginRequestDto loginRequestDto,
                              @AuthenticationPrincipal UserDetailsImpl userDetails) {
        emailService.confirmPasswordMessage(validNum,loginRequestDto,userDetails.getMember());
        System.out.println("비밀번호 변경완료");
        return "AfterMail.html";
    }
}