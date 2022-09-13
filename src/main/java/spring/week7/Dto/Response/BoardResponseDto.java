package spring.week7.Dto.Response;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BoardResponseDto {

    private Long id;
    private String boardTitle;
    private Long postNum;
    private List<String> postImage;


    public BoardResponseDto(Long id, String boardTitle) {
        this.id= id;
        this.boardTitle = boardTitle;


    }
}