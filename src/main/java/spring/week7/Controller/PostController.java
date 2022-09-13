package spring.week7.Controller;



import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
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


    private final PostService postService;

    // 게시물 상세내용 가져오기
    @GetMapping("api/post/{postId}")
    public PostResponseDto detailPost(@PathVariable(name = "postId") Long id) {
        return postService.findPostByID(id);
    }

    //게시물 생성
    @PostMapping("api/auth/post")
    public Post createPost(PostRequestDto postRequestDto,
                           @RequestPart (value="image") MultipartFile image,
                           @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return postService.postCreate( postRequestDto,image, userDetails.getMember());
    }

    //게시물 좋아요
    @PostMapping("api/auth/post/like")
    public Post likePost(PostRequestDto postRequestDto,
                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.postlike( postRequestDto, userDetails.getMember());
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
    public ResponseEntity<String> deletePost(@PathVariable(name = "postId") Long id,
                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {
        postService.postDelete(id, userDetails.getMember());
        return ResponseEntity.ok("게시물 삭제 성공");
    }


    // 게시물 검색
    @GetMapping("/api/post/search")
    public List<PostResponseDto> searchPost(@RequestParam("query") String keyword) {
        return postService.postSearch(keyword);
    }

    //게시물 전체조회
    @GetMapping("/api/post")
    public Page<Post> listAllPost(Model model,
                                    @RequestParam(value = "category", required = false) String category,
                                  Pageable pageable) {

        return postService.postAllList(model,category, pageable);
    }

    //게시물을 보드에 추가
    @PostMapping("/api/auth/post/board/{id}")
    public Post addPost(@PathVariable Long id,
                      @RequestParam("boardId") Long boardId,
                      @AuthenticationPrincipal UserDetailsImpl userDetails){
        return postService.postAdd(id, boardId, userDetails.getMember());
    }
}

