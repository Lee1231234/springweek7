package spring.week7.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.week7.Errorhandler.BusinessException;
import spring.week7.Repository.CommentRepository;
import spring.week7.Repository.PostRepository;
import spring.week7.domain.Comment;
import spring.week7.domain.Post;


import static spring.week7.Errorhandler.ErrorCode.COMMENT_NOT_EXIST;
import static spring.week7.Errorhandler.ErrorCode.POST_NOT_EXIST;

@Service
@RequiredArgsConstructor
public class LikesService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;


    // 게시글 좋아요
    public void addPostLike(Long id) {

        Post post = postRepository.findById(id).orElseThrow(
                () -> new BusinessException("포스트가 존재하지 않습니다.", POST_NOT_EXIST)
        );
        post.addLike();
        postRepository.save(post);
    }
    //게시글 좋아요 취소
    public void deletePostLike(Long id) {

        Post post = postRepository.findById(id).orElseThrow(
                () -> new BusinessException("포스트가 존재하지 않습니다.", POST_NOT_EXIST)
        );
        post.deleteLike();
        postRepository.save(post);
    }

    // 댓글 좋아요
    public void addCommentLike(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() ->
                new BusinessException("코맨트가 존재하지 않습니다.", COMMENT_NOT_EXIST)
        );
        comment.addLike();
        commentRepository.save(comment);
    }

    // 댓글 좋아요 취소
    public void deleteCommentLike(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() ->
                new BusinessException("코맨트가 존재하지 않습니다.", COMMENT_NOT_EXIST)
        );
        comment.deleteLike();
        commentRepository.save(comment);
    }

}
