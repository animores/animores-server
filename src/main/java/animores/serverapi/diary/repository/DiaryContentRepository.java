package animores.serverapi.diary.repository;

import animores.serverapi.diary.entity.DiaryMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiaryContentRepository extends JpaRepository<DiaryMedia, Long> {


}
