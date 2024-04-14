package animores.serverapi.config.security;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlacklistTokenRepository extends CrudRepository<BlackListToken, String> {
}

