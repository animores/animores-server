package animores.serverapi.diary.repository;

import animores.serverapi.diary.entity.DiaryComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiaryCommentRepository extends JpaRepository<DiaryComment, Long> {

}
