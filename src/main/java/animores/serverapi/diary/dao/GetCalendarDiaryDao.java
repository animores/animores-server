package animores.serverapi.diary.dao;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class GetCalendarDiaryDao {

    private Long diaryId;
    private String content;
    private LocalDateTime createdAt;

    private Long profileId;
    private String name;
    private String imageUrl;
}
