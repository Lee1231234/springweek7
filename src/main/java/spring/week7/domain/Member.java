package spring.week7.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import spring.week7.Dto.Request.MemberRequestDto;
import spring.week7.Errorhandler.BusinessException;

import javax.persistence.*;

import static spring.week7.Errorhandler.ErrorCode.LOGIN_INPUT_INVALID;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String email;


    @Column(nullable = false)
    @JsonIgnore
    private String password;


    public Member(MemberRequestDto requestDto, PasswordEncoder passwordEncoder) {
        this.email = requestDto.getEmail();
        this.password = passwordEncoder.encode(requestDto.getPassword());
    }

    public void validatePassword(PasswordEncoder passwordEncoder, String password) {
        if (!passwordEncoder.matches(password, this.password)) {
            throw new BusinessException("로그인 실패", LOGIN_INPUT_INVALID);
        }

    }
}
