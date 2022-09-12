package spring.week7.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.week7.Dto.Request.CommentRequestDto;
import spring.week7.Errorhandler.BusinessException;
import spring.week7.Repository.CommentRepository;
import spring.week7.Repository.PostRepository;
import spring.week7.domain.Comment;
import spring.week7.domain.Member;
import spring.week7.domain.Post;

import static spring.week7.Errorhandler.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public void CommentCreate(CommentRequestDto commentRequestDto, Member member) {
        Post post = postRepository.findById(commentRequestDto.getPostId()).orElseThrow(
                () -> new BusinessException("포스트가 존재하지 않습니다.", POST_NOT_EXIST)
        );
        Comment comment = new Comment(commentRequestDto, member, post);
        commentRepository.save(comment);
    }

    @Transactional
    public void CommentDelete(Long commentId, Member member) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new BusinessException("코맨트가 존재하지 않습니다.", COMMENT_NOT_EXIST)
        );
        if (!comment.getAuthor().equals(member.getEmail())) {
            throw new BusinessException("아이디가 일치하지 않습니다.", MEMBER_NOT_EXIST);
        }

        commentRepository.deleteById(commentId);
    }

    @Transactional
    public void CommentUpdate(Long commentId, CommentRequestDto commentRequestDto, Member member) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new BusinessException("코맨트가 존재하지 않습니다.", COMMENT_NOT_EXIST)
        );
        if (!comment.getAuthor().equals(member.getEmail())) {
            throw new BusinessException("아이디가 일치하지 않습니다.", MEMBER_NOT_EXIST);
        }
        comment.update(commentRequestDto);
    }
}
