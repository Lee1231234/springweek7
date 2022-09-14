package spring.week7.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import spring.week7.Dto.Request.CommentRequestDto;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Comment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String author;

    @Column
    @Min(0)
    private int likeNum;

    @JoinColumn(name = "post_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private Post post;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    @JoinColumn(name = "Comment_Id")
    private List<SubComment> subComments;
    @Column(nullable = false)
    private String content;


    public Comment(CommentRequestDto commentRequestDto, Member member, Post post) {
        super();
        this.content = commentRequestDto.getContent();
        this.author = member.getEmail();
        this.post = post;
    }

    public void update(CommentRequestDto commentRequestDto) {
        this.content = commentRequestDto.getContent();
    }

    public void addLike() {
        this.likeNum += 1;
    }

    public void deleteLike() {
        this.likeNum -= 1;
    }

}

