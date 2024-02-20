package animores.serverapi.diary.service.impl;

import animores.serverapi.diary.dto.AddDiaryRequest;
import animores.serverapi.diary.entity.Diary;
import animores.serverapi.diary.repository.DiaryRepository;
import animores.serverapi.diary.service.DiaryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DiaryServiceImpl implements DiaryService {

    private final DiaryRepository diaryRepository;


    @Override
    public List<Diary> getAllDiary() {
        List<Diary> diaries = diaryRepository.findAll();

        return diaries;
    }

    @Override
    @Transactional
    public ResponseEntity<Void> addDiary(AddDiaryRequest addDiaryRequest) {
        diaryRepository.save(Diary.create(addDiaryRequest));

        return ResponseEntity.status(HttpStatus.SC_OK).build();
    }
}
