package animores.serverapi.pet.service;

import java.util.List;

public interface PetService {
    void checkAccountPets(Long accountId, List<Long> petIds);
}
