package animores.serverapi.diary.dao;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class GetAllDiaryDao {

    private Long diaryId;
    private String content;
    private List<GetAllDiaryMediaDao> media;
    private boolean LikeYn;
    private Integer commentCount;
    private LocalDateTime createdAt;

    private Long profileId;
    private String name;
    private String imageUrl;

    @QueryProjection
    public GetAllDiaryDao(Long diaryId, String content, List<GetAllDiaryMediaDao> media,
        boolean likeYn, Integer commentCount, LocalDateTime createdAt, Long profileId, String name, String imageUrl) {
        this.diaryId = diaryId;
        this.content = content;
        this.media = media;
        this.LikeYn = likeYn;
        this.commentCount = commentCount;
        this.createdAt = createdAt;
        this.profileId = profileId;
        this.name = name;
        this.imageUrl = imageUrl;
    }
}
