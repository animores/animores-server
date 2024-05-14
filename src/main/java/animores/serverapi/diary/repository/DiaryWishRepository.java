package animores.serverapi.diary.repository;

import animores.serverapi.diary.entity.DiaryWish;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiaryWishRepository extends JpaRepository<DiaryWish, Long> {

    Optional<DiaryWish> findByDiaryIdAndProfileId(Long diaryId, Long profileId);
}
