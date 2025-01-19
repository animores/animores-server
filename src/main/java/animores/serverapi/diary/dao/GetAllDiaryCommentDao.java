package animores.serverapi.diary.dao;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class GetAllDiaryCommentDao {

    private Long commentId;
    private String content;
    private LocalDateTime createdAt;

    private Long profileId;
    private String name;
    private String imageUrl;

    private Long replyCount;

}
