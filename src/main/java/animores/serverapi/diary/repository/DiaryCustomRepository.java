package animores.serverapi.diary.repository;

import animores.serverapi.diary.dto.GetAllDiary;
import java.util.List;

public interface DiaryCustomRepository {

    List<GetAllDiary> getAllDiary(Long accountId);

}
