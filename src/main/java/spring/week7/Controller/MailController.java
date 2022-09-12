package spring.week7.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import spring.week7.Dto.Request.MailRequestDto;
import spring.week7.Service.EmailService;

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

    @PostMapping("/mail/send")
    public String sendMail(@RequestBody MailRequestDto mailDto) {
        emailService.sendSimpleMessage(mailDto);
        System.out.println("메일 전송 완료");
        return "AfterMail.html";
    }
}