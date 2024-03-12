package animores.serverapi.diary.dao;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;

@Getter
public class GetAllDiary {

    private Long diaryId;
    private String content;
    private List<String> mediaUrls = Arrays.asList("image url", "image url", "video"); // 수정 예정
    private boolean wishYn = true; // 수정 예정
    private int commentCount = 10; // 수정 예정
    private LocalDateTime createdAt;

    private Long profileId;
    private String name;
    private String imageUrl;

}
