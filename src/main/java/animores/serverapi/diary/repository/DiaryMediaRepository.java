package animores.serverapi.diary.repository;

import animores.serverapi.diary.entity.DiaryMedia;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiaryMediaRepository extends JpaRepository<DiaryMedia, Long> {

    List<DiaryMedia> findByIdIn(List<Long> ids);

}
