package animores.serverapi.diary.controller;

import animores.serverapi.common.Response;
import animores.serverapi.diary.dto.AddDiaryWishRequest;
import animores.serverapi.diary.dto.CancelDiaryWishRequest;
import animores.serverapi.diary.service.DiaryWishService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/diary-wishes")
public class DiaryWishController {

    private final DiaryWishService diaryWishService;

    @PostMapping("")
    public Response<Void> addDiaryWish(@RequestBody AddDiaryWishRequest request) {
        diaryWishService.addDiaryWish(request);
        return Response.success(null);
    }

    @DeleteMapping("")
    public Response<Void> cancelDiaryWish(@RequestBody CancelDiaryWishRequest request) {
        diaryWishService.cancelDiaryWish(request);
        return Response.success(null);
    }

}
