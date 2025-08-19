package animores.serverapi.account.repository;

import animores.serverapi.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {

    boolean existsByNickname(String nickname);

}