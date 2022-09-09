package spring.week7.Dto.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostRequestDto {
    private String title;
    private String content;
    private String category;
    private String image;
}
