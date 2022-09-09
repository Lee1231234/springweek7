package spring.week7.Controller;



import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import spring.week7.Dto.Request.PostRequestDto;
import spring.week7.Dto.Response.PostResponseDto;
import spring.week7.Service.PostService;
import spring.week7.domain.Post;
import spring.week7.domain.UserDetailsImpl;

import java.io.IOException;
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
    public Post createPost(PostRequestDto postRequestDto,
                           @RequestPart (value="image") MultipartFile image,
                           @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return postService.postCreate( postRequestDto,image, userDetails.getMember());
    }


    // 게시물 수정
    @PutMapping("/api/auth/post/{postId}")
    public Post editPost(@PathVariable(name = "postId") Long id,
                         PostRequestDto postRequestDto,
                         @RequestPart (value="image") MultipartFile image,
                         @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return postService.postEdit(id, postRequestDto,image, userDetails.getMember());
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
}

