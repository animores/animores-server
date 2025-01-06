package animores.serverapi.account.repository.auth_mail;

import animores.serverapi.account.entity.auth_mail.AuthMail;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthMailRepository extends CrudRepository<AuthMail, String> {

}