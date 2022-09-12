package spring.week7.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.week7.Dto.Request.PostRequestDto;

import javax.persistence.*;
import javax.validation.constraints.Min;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Post extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;


    @Column(length = 500)
    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postCategory",insertable = false, updatable = false)
    @Min(value = 1)
    private PostCategory postCategory;




    @ManyToOne
    @JoinColumn(name = "Member_id", nullable = false)
    private Member member;

    public void update(PostRequestDto postRequestDto) {
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
//        this.postCategory = postRequestDto.getPostCategory();
        this.image =  postRequestDto.getImage();
    }

    public Post(PostRequestDto postRequestDto, Member member) {
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
//        this.postCategory = postRequestDto.getPostCategory();
        this.image = postRequestDto.getImage();

//        this.createdAt = postRequestDto.getCreatedAt();
//        this.modifiedAt = postRequestDto.getModifiedAt();
        this.member = member;
    }

//    public void PostResponseDto(Post post) {
//        this.id = post.getId();
//        this.title = post.getTitle();
//        this.content = post.getContent();
//        this.postCategory = post.getPostCategory();
//        this.image = post.getImage();
//        this.createdAt = post.getCreatedAt();
//        this.member = post.getMember();
//    }


    public void mappingCategory(PostCategory postCategory){
        this.postCategory = postCategory;
        postCategory.mappingPost(this);
    }

}
