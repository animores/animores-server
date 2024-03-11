package animores.serverapi.diary.dto;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class GetAllDiary {

    private Long diaryId;
    private String content;
    private LocalDateTime createdAt;

    private Long profileId;
    private String name;
    private String imageUrl;

}
