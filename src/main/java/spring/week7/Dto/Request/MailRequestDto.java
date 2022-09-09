package spring.week7.Dto.Request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MailRequestDto {
    private String address;
    private String title;
    private String content;
}