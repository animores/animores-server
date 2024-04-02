package animores.serverapi.diary.dao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class GetAllDiary {

    private Long diaryId;
    private String content;
    private List<GetAllDiaryMedia> media = new ArrayList<>();
    private boolean wishYn = true; // 수정 예정
    private int commentCount = 10; // 수정 예정
    private LocalDateTime createdAt;

    private Long profileId;
    private String name;
    private String imageUrl;

    public void setDiaryMedia(List<GetAllDiaryMedia> media) {
        this.media = media;
    }

}