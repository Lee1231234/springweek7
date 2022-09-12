package spring.week7.Service;

import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.week7.Dto.Request.LoginRequestDto;
import spring.week7.Dto.Request.MailRequestDto;
import spring.week7.Errorhandler.BusinessException;
import spring.week7.Errorhandler.ErrorCode;
import spring.week7.domain.Member;

import java.util.Random;

import static spring.week7.Errorhandler.ErrorCode.MEMBER_NOT_EXIST;

@Service
@AllArgsConstructor
public class EmailService {

    private JavaMailSender emailSender;
    private PasswordEncoder passwordEncoder;
    @Transactional
    public void sendPasswordMessage(MailRequestDto mailDto, Member member) {
        if (!mailDto.getAddress().equals(member.getEmail())) {
            throw new BusinessException("아이디가 일치하지 않습니다.", MEMBER_NOT_EXIST);
        }
        SimpleMailMessage message = new SimpleMailMessage();
        Random random = new Random();
        member.setValidNumber(random.nextInt(1000000));
        message.setFrom("lee9710250@gmail.com");
        message.setTo(mailDto.getAddress());
        message.setSubject("핀터레스트 비밀번호 변경");
        message.setText("입력해야할 번호는 "+member.getValidNumber() +"입니다");
        emailSender.send(message);
    }
    @Transactional
    public void confirmPasswordMessage(int validNum, LoginRequestDto loginRequestDto, Member member) {
        if(member.getValidNumber()!=validNum){
            throw new BusinessException("잘못된 입력입니다", ErrorCode.NUMBER_NOT_PERMIT);
        }
        if (!loginRequestDto.getEmail().equals(member.getEmail())) {
            throw new BusinessException("아이디가 일치하지 않습니다.", MEMBER_NOT_EXIST);
        }
        member.setPassword(passwordEncoder.encode(loginRequestDto.getPassword()))
        ;
    }
}