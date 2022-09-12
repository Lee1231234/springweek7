package spring.week7.Dto.Response;


import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import spring.week7.domain.Comment;
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

    private List<Comment> comment;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.category = post.getImage();
        this.image = post.getImage();
        this.createdAt = post.getCreatedAt();
        this.comment = post.getComments();
        this.modifiedAt = post.getModifiedAt();
        this.member = post.getMember();
    }
}
