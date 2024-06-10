package animores.serverapi.account.repository.auth_mail;

import animores.serverapi.account.entity.auth_mail.ValidMail;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ValidMailRepository extends CrudRepository<ValidMail, String> {
}
