package spring.week7.Dto.Response;


import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.week7.domain.Member;
import spring.week7.domain.Post;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private String postCategory;
    private String image;
    private LocalDateTime createdAt;
    private Member member;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
//        this.postCategory = String.valueOf(post.getPostCategory());
        this.image = post.getImage();
        this.createdAt = post.getCreatedAt();
        this.member = post.getMember();
    }
}
