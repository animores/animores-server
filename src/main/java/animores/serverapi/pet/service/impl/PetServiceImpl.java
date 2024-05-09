package animores.serverapi.pet.service.impl;

import animores.serverapi.account.domain.Account;
import animores.serverapi.common.exception.CustomException;
import animores.serverapi.common.exception.ExceptionCode;
import animores.serverapi.pet.dao.PetDao;
import animores.serverapi.pet.dto.response.GetPetDetailResponse;
import animores.serverapi.pet.entity.Breed;
import animores.serverapi.pet.entity.Pet;
import animores.serverapi.pet.entity.Species;
import animores.serverapi.pet.dto.PetDto;
import animores.serverapi.pet.dto.request.PetCreateRequest;
import animores.serverapi.pet.dto.response.BreedResponse;
import animores.serverapi.pet.dto.response.PetCreateResponse;
import animores.serverapi.pet.dto.response.SpeciesResponse;
import animores.serverapi.pet.repository.BreedRepository;
import animores.serverapi.pet.repository.PetRepository;
import animores.serverapi.pet.repository.SpeciesRepository;
import animores.serverapi.pet.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class PetServiceImpl implements PetService {
    private final SpeciesRepository speciesRepository;
    private final BreedRepository breedRepository;
    private final PetRepository petRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Pet> checkAccountPets(Long accountId, List<Long> petIds) {
        List<Pet> pets = petRepository.findAllByAccount_id(accountId);

        if(petIds.isEmpty()){
            return pets;
        } else {
            Set<Long> petIdsSet = new HashSet<>(petIds);
            pets.forEach(pet -> {
                if(!petIdsSet.contains(pet.getId())){
                    throw new CustomException(ExceptionCode.ILLEGAL_PET_IDS);
                }
            });

            return pets.stream().filter(pet -> petIdsSet.contains(pet.getId())).toList();
        }
    }

    @Override
    public List<SpeciesResponse> getSpecies() {
        List<Species> species = speciesRepository.findAll();
        return species.stream().map(SpeciesResponse::fromEntity).toList();
    }

    @Override
    public List<BreedResponse> getBreedsOfSpecies(Long speciesId) {
        List<Breed> breeds = breedRepository.findAllBySpecies_Id(speciesId);
        return breeds.stream().map(BreedResponse::fromEntity).toList();
    }

    @Transactional
    @Override
    public PetCreateResponse createPet(Account account, PetCreateRequest request) {
        Breed breed = breedRepository.findById(request.breedId())
                                     .orElseThrow(() -> new IllegalArgumentException("Breed not found"));

        Pet pet = Pet.createFromRequest(account, request, breed);
        pet = petRepository.save(pet);

        return new PetCreateResponse(pet.getId(),pet.getName());
    }

    @Transactional(readOnly = true)
    @Override
    public List<PetDto> getPets(Account account) {
        List<PetDao> pets = petRepository.findAllByAccount_IdWithImages(account.getId());
        return pets.stream().map(PetDto::fromDao).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public GetPetDetailResponse getPet(Long petId) {

        return GetPetDetailResponse.fromEntity(petRepository.findById(petId)
                .orElseThrow(() -> new CustomException(ExceptionCode.ILLEGAL_PET_IDS)));
    }

    @Transactional
    @Override
    public PetCreateResponse updatePet(Long petId, PetCreateRequest request) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new CustomException(ExceptionCode.ILLEGAL_PET_IDS));

        Breed breed = breedRepository.getReferenceById(request.breedId());
        pet.update(request, breed);
        return new PetCreateResponse(pet.getId(),pet.getName());
    }

    @Transactional
    @Override
    public void deletePet(Long petId) {
        petRepository.deleteById(petId);
    }
}
