package spring.week7.domain;

import lombok.*;

import javax.persistence.*;

//멤버와 포스트의 저장관계 (N:M 을 위한 엔티티)
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class MemberPostRestore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    public MemberPostRestore(Post post, Member member) {
        this.post = post;
        this.member = member;
    }
}
