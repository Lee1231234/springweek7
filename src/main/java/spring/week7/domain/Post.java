package spring.week7.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.week7.Dto.Request.PostRequestDto;

import javax.persistence.*;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Post extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String category;

    @Column(length = 500)
    private String image;

    @ManyToOne
    @JoinColumn(name = "Member_id", nullable = false)
    private Member member;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "Comment_id")
    @JsonManagedReference
    private List<Comment> comments;

    public void update(PostRequestDto postRequestDto, String image) {
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
        this.category = postRequestDto.getCategory();
        this.image = image;
    }

    public Post(PostRequestDto postRequestDto, String image, Member member) {
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
        this.category = postRequestDto.getCategory();
        this.image = image;
        this.member = member;
    }

    public void setComment(Comment comment) {
        this.comments.add(comment);
        comment.setPost(this);
    }
}
