package animores.serverapi.pet.service;

import animores.serverapi.pet.domain.Pet;

import java.util.List;

public interface PetService {
    List<Pet> checkAccountPets(Long accountId, List<Long> petIds);
}
