package spring.week7.Controller;



import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import spring.week7.Dto.Request.PostRequestDto;
import spring.week7.Dto.Response.PostResponseDto;
import spring.week7.Service.PostService;
import spring.week7.domain.Post;
import spring.week7.domain.PostCategory;
import spring.week7.domain.UserDetailsImpl;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class PostController {

    @Autowired
    private final PostService postService;

    // 게시물 상세내용 가져오기
    @GetMapping("api/post/{postId}")
    public Post detailPost(@PathVariable(name = "postId") Long id) {
        return postService.findPostByID(id);
    }

    //게시물 생성
    @PostMapping("api/auth/post")
    public Post createPost(@RequestBody PostRequestDto postRequestDto,
                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.postCreate(postRequestDto, userDetails.getMember());
    }


    // 게시물 수정
    @PutMapping("/api/auth/post/{postId}")
    public Post editPost(@PathVariable(name = "postId") Long id,
                         @RequestBody PostRequestDto postRequestDto,
                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.postEdit(id, postRequestDto, userDetails.getMember());
    }

    // 게시물 삭제
    @DeleteMapping("/api/auth/post/{postId}")
    public Long deletePost(@PathVariable(name = "postId") Long id,
                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.postDelete(id, userDetails.getMember());
    }


    // 게시물 검색
    @GetMapping("/api/post/search")
    public List<PostResponseDto> searchPost(@RequestParam("query") String keyword) {
        return postService.postSearch(keyword);
    }

    // 카테고리 추가
    @PostMapping("/api/post/category/add")
    public PostCategory addCategory(
            @RequestParam(value = "categoryName") String categoryName) {
        return postService.categoryAdd(categoryName);
    }

    //게시물 전체조회
    @GetMapping("/api/post")
    public Page<Post> listAllPost(Model model,
                                  @RequestParam(value = "category", required = false, defaultValue = "0") Long postCategoryId,
                                  Pageable pageable) {

        return postService.postAllList(model, postCategoryId, pageable);
    }

}//class


