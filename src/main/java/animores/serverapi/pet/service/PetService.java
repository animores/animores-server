package animores.serverapi.pet.service;

import animores.serverapi.account.domain.Account;
import animores.serverapi.pet.dto.request.PetCreateRequest;
import animores.serverapi.pet.dto.response.PetCreateResponse;

public interface PetService {
    PetCreateResponse createPet(Account account, PetCreateRequest request);
}
