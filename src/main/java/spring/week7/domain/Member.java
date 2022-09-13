package spring.week7.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import spring.week7.Dto.Request.MemberRequestDto;
import spring.week7.Errorhandler.BusinessException;

import javax.persistence.*;


import java.util.ArrayList;
import java.util.List;

import static spring.week7.Errorhandler.ErrorCode.LOGIN_INPUT_INVALID;

@Builder
@Getter
@Setter
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

//    포스트 저장용

   @OneToMany(mappedBy = "member")
   @JsonIgnore
   private List<MemberPostRestore> member_post_restores = new ArrayList<>();

//    사진을 위한 자리
    @Column
    private String image;
    //랜덤한 값을 위한 자리
    @JsonIgnore
    private int validNumber;

    @JsonIgnore
    private int age;
//    @JoinColumn(name = "follower_id")
//    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Followr> follower;
//
//    @JoinColumn(name = "followed_id")
//    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Followr> followed;
    public Member(MemberRequestDto requestDto, PasswordEncoder passwordEncoder) {
        this.email = requestDto.getEmail();
        this.password = passwordEncoder.encode(requestDto.getPassword());
    }

    public void validatePassword(PasswordEncoder passwordEncoder, String password) {
        if (!passwordEncoder.matches(password, this.password)) {
            throw new BusinessException("로그인 실패", LOGIN_INPUT_INVALID);
        }

    }

    public void update(String imageUrl) {
        this.image = imageUrl;
    }
}
