package animores.serverapi.pet.service;

import animores.serverapi.account.entity.Account;
import animores.serverapi.pet.dto.PetDto;
import animores.serverapi.pet.dto.request.PetCreateRequest;
import animores.serverapi.pet.dto.request.PetUpdateRequest;
import animores.serverapi.pet.dto.response.BreedResponse;
import animores.serverapi.pet.dto.response.GetPetDetailResponse;
import animores.serverapi.pet.dto.response.PetCreateResponse;
import animores.serverapi.pet.dto.response.PetImageResponse;
import animores.serverapi.pet.dto.response.SpeciesResponse;
import animores.serverapi.pet.entity.Pet;
import java.util.List;

public interface PetService {

    List<Pet> checkAccountPets(Long accountId, List<Long> petIds);

    List<SpeciesResponse> getSpecies();

    List<BreedResponse> getBreedsOfSpecies(Long speciesId);

    List<PetImageResponse> getPetImages(Long speciesId);

    PetCreateResponse createPet(Account account, PetCreateRequest request);

    List<PetDto> getPets(Account account);

    GetPetDetailResponse getPet(Long petId);

    PetCreateResponse updatePet(Long petId, PetUpdateRequest request);

    void deletePet(Long petId);
}
