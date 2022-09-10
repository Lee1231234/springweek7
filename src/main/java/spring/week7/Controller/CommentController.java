package spring.week7.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import spring.week7.Dto.Request.CommentRequestDto;


import spring.week7.Service.CommentService;

import spring.week7.domain.UserDetailsImpl;




@RequiredArgsConstructor
@RestController
public class CommentController {
    private final CommentService commentService;


    //코맨트 생성
    @PostMapping("api/auth/post/comment")
    public ResponseEntity<String> createComment(@RequestBody CommentRequestDto commentRequestDto,
                                              @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.CommentCreate( commentRequestDto, userDetails.getMember());
        return ResponseEntity.ok("코맨트 생성 성공");
    }
    //코맨트 삭제
    @DeleteMapping("api/auth/post/comment/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId,
                                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.CommentDelete(commentId, userDetails.getMember());

        return ResponseEntity.ok("코맨트 삭제 성공");
    }
    //코멘트 수정
    @PutMapping("api/auth/post/comment/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId,
                                                @RequestBody CommentRequestDto commentRequestDto,
                                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.CommentUpdate(commentId,commentRequestDto, userDetails.getMember());

        return ResponseEntity.ok("코맨트 변경 성공");
    }
}
