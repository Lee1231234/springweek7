package spring.week7.Controller;



import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import spring.week7.Dto.Request.BoardRequestDto;
import spring.week7.Dto.Response.BoardResponseDto;
import spring.week7.Service.BoardService;
import spring.week7.domain.UserDetailsImpl;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    // 보드 생성
    @PostMapping("/api/auth/board")
    public BoardResponseDto createBoard(@RequestBody BoardRequestDto boardRequestDto,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails){
        return boardService.boardCreate(boardRequestDto, userDetails.getMember());
    }

    // 보드 가져오기
    @GetMapping("/api/auth/board")
    public List<BoardResponseDto> getBoard(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return boardService.boardGet(userDetails.getMember());
    }




}