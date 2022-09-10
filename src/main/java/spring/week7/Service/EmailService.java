package spring.week7.Service;

import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import spring.week7.Dto.Request.MailRequestDto;

@Service
@AllArgsConstructor
public class EmailService {

    private JavaMailSender emailSender;

    public void sendSimpleMessage(MailRequestDto mailDto) {
        SimpleMailMessage message = new SimpleMailMessage();
        System.out.println(mailDto.getAddress() + " " + mailDto.getTitle() + " " + mailDto.getContent());
        message.setFrom("lee9710250@gmail.com");
        message.setTo(mailDto.getAddress());
        message.setSubject(mailDto.getTitle());
        message.setText(mailDto.getContent());
        emailSender.send(message);
    }
}