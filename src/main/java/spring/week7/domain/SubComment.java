package spring.week7.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.week7.Dto.Request.SubCommentRequestDto;

import javax.persistence.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class SubComment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private String author;


    @JoinColumn(name = "comment_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private Comment comment;

    @Column(nullable = false)
    private String content;

    public SubComment(SubCommentRequestDto subCommentRequestDto, Member member, Comment comment) {
        super();
        this.author = member.getEmail();
        this.comment = comment;
        this.content = subCommentRequestDto.getContent();
    }

    public void update(SubCommentRequestDto subCommentRequestDto) {
        this.content = subCommentRequestDto.getContent();
    }
}
