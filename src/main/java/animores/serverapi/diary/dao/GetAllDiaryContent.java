package animores.serverapi.diary.dao;

import animores.serverapi.diary.entity.DiaryContentType;
import lombok.Getter;

@Getter
public class GetAllDiaryContent {

    private Long contentId;
    private String contentUrl;
    private int contentOrder;
    private DiaryContentType type;

}
