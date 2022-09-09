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
    private String category;
    private String image;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Member member;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.category = post.getImage();
        this.image = post.getImage();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
        this.member = post.getMember();
    }
}
