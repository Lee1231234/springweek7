package spring.week7.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import spring.week7.Service.LikesService;



@Validated
@RequiredArgsConstructor
@RestController
public class LikesController {

    private final LikesService likesService;

    // 게시글 좋아요
    @PutMapping("/api/auth/post/likes/{id}")
    public ResponseEntity<String> addPostLike(@PathVariable Long id) {
        likesService.addPostLike(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    //게시글 좋아요 취소
    @PutMapping("/api/auth/post/dislikes/{id}")
    public ResponseEntity<String> deletePostLike(@PathVariable Long id) {
        likesService.deletePostLike(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    // 댓글 좋아요
    @PutMapping("/api/auth/comments/like/{id}")
    public ResponseEntity<String> addCommentLike(@PathVariable Long id) {
        likesService.addCommentLike(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 댓글 좋아요 취소
    @PutMapping("/api/auth/comments/dislike/{id}")
    public ResponseEntity<String> deleteCommentLike(@PathVariable Long id) {
        likesService.deleteCommentLike(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
