package animores.serverapi.diary.repository;

import animores.serverapi.diary.entity.DiaryLike;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiaryLikeRepository extends JpaRepository<DiaryLike, Long> {

    Optional<DiaryLike> findByDiaryIdAndProfileId(Long diaryId, Long profileId);
}
