package animores.serverapi.pet.service.impl;

import animores.serverapi.account.domain.Account;
import animores.serverapi.pet.domain.Breed;
import animores.serverapi.pet.domain.Pet;
import animores.serverapi.pet.dto.request.PetCreateRequest;
import animores.serverapi.pet.dto.response.PetCreateResponse;
import animores.serverapi.pet.repository.BreedRepository;
import animores.serverapi.pet.repository.PetRepository;
import animores.serverapi.pet.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PetServiceImpl implements PetService {

    private final BreedRepository breedRepository;
    private final PetRepository petRepository;

    @Transactional
    @Override
    public PetCreateResponse createPet(Account account, PetCreateRequest request) {
        Breed breed = breedRepository.findById(request.breedId())
                                     .orElseThrow(() -> new IllegalArgumentException("Breed not found"));

        Pet pet = Pet.createFromRequest(account, request, breed);
        pet = petRepository.save(pet);

        return new PetCreateResponse(pet.getId(),pet.getName());
    }
}
