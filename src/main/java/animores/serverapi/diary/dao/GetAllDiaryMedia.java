package animores.serverapi.diary.dao;

import animores.serverapi.diary.entity.DiaryMediaType;
import lombok.Getter;

@Getter
public class GetAllDiaryMedia {

    private Long id;
    private String url;
    private int order;
    private DiaryMediaType type;

}
