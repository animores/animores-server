package animores.serverapi.diary.service;

import animores.serverapi.diary.dto.AddDiaryWishRequest;

public interface DiaryWishService {

    void addDiaryWish(AddDiaryWishRequest request);

    void removeDiaryWish(Long diaryWishId);

}
