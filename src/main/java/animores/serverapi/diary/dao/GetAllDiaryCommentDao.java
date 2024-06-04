package animores.serverapi.diary.dao;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class GetAllDiaryCommentDao {

    private Long commentId;
    private String content;
    private LocalDateTime createAt;

    private Long profileId;
    private String name;
    private String imageUrl;

    private Integer replyCount = 0;

}
