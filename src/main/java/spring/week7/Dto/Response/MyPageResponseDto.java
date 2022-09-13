package spring.week7.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.week7.domain.MemberPostRestore;
import spring.week7.domain.Post;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MyPageResponseDto {
    private int follower;
    private int following;
    private List<Mypage> myPage;
    private List<Mypage> storePage;

    @Builder
    public static class Mypage{
        private Long id;
        private String category;
        private String image;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }

    public MyPageResponseDto(List<Post> mypage,List<MemberPostRestore>storePage,int follower,int following){
        for(Post post:mypage){
            this.myPage.add(Mypage.builder()
                    .id(post.getId())
                    .category(post.getCategory())
                    .createdAt(post.getCreatedAt())
                    .modifiedAt(post.getModifiedAt())
                    .build());
        }
        for(MemberPostRestore post:storePage){
            this.storePage.add(Mypage.builder()
                    .id(post.getPost().getId())
                    .category(post.getPost().getCategory())
                    .createdAt(post.getPost().getCreatedAt())
                    .modifiedAt(post.getPost().getModifiedAt())
                    .build());
        }
        this.follower = follower;
        this.following = following;

    }

}
