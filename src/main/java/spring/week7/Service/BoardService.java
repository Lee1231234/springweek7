package spring.week7.Service;



import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.week7.Dto.Request.BoardRequestDto;
import spring.week7.Dto.Response.BoardResponseDto;
import spring.week7.Repository.BoardRepository;
import spring.week7.Repository.PostRepository;
import spring.week7.domain.Board;
import spring.week7.domain.Member;
import spring.week7.domain.Post;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final PostRepository postRepository;

    // 보드 생성
    public BoardResponseDto boardCreate(BoardRequestDto boardRequestDto, Member member){
        Board board = new Board(boardRequestDto, member);
        board = boardRepository.save(board);
        return new BoardResponseDto(
                board.getId(),
                board.getBoardTitle()
        );
    }

    // 보드 가져오기
    public List<BoardResponseDto> boardGet(Member member) {

        List<Board> boards = boardRepository.findAllByMemberId(member.getId());
        List<BoardResponseDto> boardResponseDtos = new ArrayList<>();

        for (Board board : boards) {

            BoardResponseDto responseDto = new BoardResponseDto();
            Long postNum = postRepository.countAllByBoardId(board.getId());
            List<String> img = new ArrayList<>();
            List<Post> posts = postRepository.findTop5ByBoardId(board.getId());
            for (Post post : posts) {
                img.add(post.getImage());
            }
            responseDto.setId(board.getId());
            responseDto.setPostNum(postNum);
            responseDto.setPostImage(img);
            responseDto.setBoardTitle(board.getBoardTitle());


            boardResponseDtos.add(responseDto);
        }

        return boardResponseDtos;
    }

}