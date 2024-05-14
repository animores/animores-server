package animores.serverapi.diary.service;

import animores.serverapi.diary.dto.AddDiaryWishRequest;
import animores.serverapi.diary.dto.CancelDiaryWishRequest;

public interface DiaryWishService {

    void addDiaryWish(AddDiaryWishRequest request);

    void cancelDiaryWish(CancelDiaryWishRequest request);

}
