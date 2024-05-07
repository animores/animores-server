package animores.serverapi.pet.service.impl;

import animores.serverapi.account.domain.Account;
import animores.serverapi.pet.dao.PetDao;
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

import java.util.List;

@RequiredArgsConstructor
@Service
public class PetServiceImpl implements PetService {
    private final SpeciesRepository speciesRepository;
    private final BreedRepository breedRepository;
    private final PetRepository petRepository;

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
}
