package spring.week7.domain;


import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.week7.Dto.Request.BoardRequestDto;

import javax.persistence.*;


@Entity
@NoArgsConstructor
@Getter
public class Board extends Timestamped {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @Column(nullable = false)
    private String boardTitle;

    @ManyToOne
    private Member member;

    public Board(BoardRequestDto boardRequestDto, Member member){
        this.boardTitle = boardRequestDto.getBoardTitle();
        this.member = member;
    }

}
