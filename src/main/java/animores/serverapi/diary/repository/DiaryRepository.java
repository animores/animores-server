package animores.serverapi.diary.repository;

import animores.serverapi.account.domain.Account;
import animores.serverapi.diary.entity.Diary;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {

    @EntityGraph(attributePaths = {"profile", "account"})
    List<Diary> findByAccountId(Long accountId);
}
