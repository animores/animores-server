package animores.serverapi.diary.repository;

import animores.serverapi.diary.entity.DiaryMedia;
import animores.serverapi.diary.entity.DiaryMediaType;
import java.util.List;

public interface DiaryMediaCustomRepository {

    List<DiaryMedia> getAllDiaryMediaToReorder(Long diaryId, DiaryMediaType type);

}
