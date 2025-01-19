package animores.serverapi.pet.repository;

import animores.serverapi.pet.dao.PetDao;
import java.util.List;

public interface PetCustomRepository {

    List<PetDao> findAllByAccount_IdWithImages(Long id);
}