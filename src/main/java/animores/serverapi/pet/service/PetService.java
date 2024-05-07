package animores.serverapi.pet.service;

import animores.serverapi.account.domain.Account;
import animores.serverapi.pet.dto.PetDto;
import animores.serverapi.pet.dto.request.PetCreateRequest;
import animores.serverapi.pet.dto.response.BreedResponse;
import animores.serverapi.pet.dto.response.PetCreateResponse;
import animores.serverapi.pet.dto.response.SpeciesResponse;

import java.util.List;

import animores.serverapi.pet.entity.Pet;

import java.util.List;

public interface PetService {
    List<Pet> checkAccountPets(Long accountId, List<Long> petIds);
    List<SpeciesResponse> getSpecies();
    List<BreedResponse> getBreedsOfSpecies(Long speciesId);
    PetCreateResponse createPet(Account account, PetCreateRequest request);
    List<PetDto> getPets(Account account);
}
