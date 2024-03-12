package animores.serverapi.diary.dao;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class GetCalendarDiary {

    private Long totalCount;

    private Long diaryId;
    private String content;
    private String mainImageUrl = "url"; // 수정 예정
    private LocalDateTime createdAt;

    private Long profileId;
    private String name;
    private String imageUrl;

    private String test;

}
