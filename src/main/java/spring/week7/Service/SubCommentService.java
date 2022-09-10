package spring.week7.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.week7.Dto.Request.SubCommentRequestDto;
import spring.week7.Errorhandler.BusinessException;
import spring.week7.Repository.CommentRepository;
import spring.week7.Repository.SubCommentRepository;
import spring.week7.domain.Comment;
import spring.week7.domain.Member;

import spring.week7.domain.SubComment;

import java.util.List;

import static spring.week7.Errorhandler.ErrorCode.*;


@Service
@RequiredArgsConstructor
public class SubCommentService {
    private final CommentRepository commentRepository;
    private final SubCommentRepository subCommentRepository;

    @Transactional
    public void SubCommentCreate(SubCommentRequestDto subCommentRequestDto, Member member) {
        Comment comment = commentRepository.findById(subCommentRequestDto.getCommentId()).orElseThrow(
                () -> new BusinessException("코맨트가 존재하지 않습니다.", COMMENT_NOT_EXIST)
        );
        SubComment subComment = new SubComment(subCommentRequestDto, member, comment);
        List<SubComment> subComments = comment.getSubComments();
        subComments.add(subComment);
        comment.setSubComments(subComments);
        subCommentRepository.save(subComment);
    }

    @Transactional
    public void SubCommentDelete(Long subcommentId, Member member) {
        SubComment subComment = subCommentRepository.findById(subcommentId).orElseThrow(() ->
                new BusinessException("서브 코맨트가 존재하지 않습니다. ", SUBCOMMENT_NOT_EXIST));
        if (!subComment.getAuthor().equals(member.getEmail())) {
            throw new BusinessException("아이디가 일치하지 않습니다.", MEMBER_NOT_EXIST);
        }

        subCommentRepository.deleteById(subcommentId);
    }

    @Transactional
    public void SubCommentUpdate(SubCommentRequestDto subCommentRequestDto, Long subcommentId, Member member) {
        SubComment subComment = subCommentRepository.findById(subcommentId).orElseThrow(() ->
                new BusinessException("서브 코맨트가 존재하지 않습니다. ", SUBCOMMENT_NOT_EXIST));
        if (!subComment.getAuthor().equals(member.getEmail())) {
            throw new BusinessException("아이디가 일치하지 않습니다.", MEMBER_NOT_EXIST);
        }
        subComment.update(subCommentRequestDto);
    }
}
