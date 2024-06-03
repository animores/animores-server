package animores.serverapi.pet.service.impl;

import animores.serverapi.account.domain.Account;
import animores.serverapi.common.exception.CustomException;
import animores.serverapi.pet.dao.PetDao;
import animores.serverapi.pet.dto.PetDto;
import animores.serverapi.pet.dto.request.PetCreateRequest;
import animores.serverapi.pet.dto.request.PetUpdateRequest;
import animores.serverapi.pet.dto.response.BreedResponse;
import animores.serverapi.pet.dto.response.GetPetDetailResponse;
import animores.serverapi.pet.dto.response.PetCreateResponse;
import animores.serverapi.pet.dto.response.SpeciesResponse;
import animores.serverapi.pet.entity.Breed;
import animores.serverapi.pet.entity.Pet;
import animores.serverapi.pet.entity.PetImage;
import animores.serverapi.pet.entity.Species;
import animores.serverapi.pet.repository.BreedRepository;
import animores.serverapi.pet.repository.PetImageRepository;
import animores.serverapi.pet.repository.PetRepository;
import animores.serverapi.pet.repository.SpeciesRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PetServiceImplTest {

    @InjectMocks
    private PetServiceImpl petService;

    @Mock
    private SpeciesRepository speciesRepository;

    @Mock
    private BreedRepository breedRepository;

    @Mock
    private PetRepository petRepository;

    @Mock
    private PetImageRepository petImageRepository;

    private static final Long PET_ID = 1L;
    private static final Long BREED_ID = 2L;
    private static final Long SPECIES_ID = 3L;
    private static final Long ACCOUNT_ID = 4L;
    private static final Long PET_IMAGE_ID = 5L;
    private static final String BREED_NAME = "breed";
    private static final String PET_NAME = "pet";


    @Test
    void checkAccountPetsReturnsAllPetsWhenPetIdsIsEmpty() {
        when(petRepository.findAllByAccount_id(ACCOUNT_ID)).thenReturn(Collections.singletonList(new TestPet(PET_ID)));
        List<Pet> result = petService.checkAccountPets(ACCOUNT_ID, Collections.emptyList());
        assertFalse(result.isEmpty());
    }

    @Test
    void checkAccountPetsReturnsFilteredPetsWhenPetIdsIsNotEmpty() {
        Pet pet = new TestPet(PET_ID);
        when(petRepository.findAllByAccount_id(ACCOUNT_ID)).thenReturn(Collections.singletonList(pet));

        List<Pet> result = petService.checkAccountPets(ACCOUNT_ID, Collections.singletonList(PET_ID));

        assertFalse(result.isEmpty());
        assertEquals(1L, result.get(0).getId());
    }

    @Test
    void checkAccountPetsThrowsExceptionWhenPetIdsAreInvalid() {
        Pet pet = new TestPet(PET_ID);
        when(petRepository.findAllByAccount_id(ACCOUNT_ID)).thenReturn(Collections.singletonList(pet));

        try {
            petService.checkAccountPets(ACCOUNT_ID, Collections.singletonList(2L));
            throw new AssertionError();
        } catch (Exception e) {
            assertTrue(e instanceof CustomException);
            assertEquals("ILLEGAL_PET_IDS", ((CustomException) e).getCode().name());
        }

    }

    @Test
    void getSpeciesReturnsAllSpecies() {
        when(speciesRepository.findAll()).thenReturn(Collections.singletonList(new Species()));

        List<SpeciesResponse> result = petService.getSpecies();

        assertFalse(result.isEmpty());
    }

    @Test
    void getBreedsOfSpeciesReturnsAllBreeds() {
        when(breedRepository.findAllBySpecies_Id(SPECIES_ID)).thenReturn(Collections.singletonList(new TestBreed(BREED_ID, BREED_NAME)));

        List<BreedResponse> result = petService.getBreedsOfSpecies(SPECIES_ID);

        assertFalse(result.isEmpty());
    }

    @Test
    void createPetReturnsCreatedPet() {
        Account account = new Account();
        PetCreateRequest request = new PetCreateRequest(
                BREED_ID,
                PET_IMAGE_ID,
                "name",
                0,
                LocalDate.of(2021, 1, 1),
                1.5f
        );

        when(breedRepository.findById(request.breedId())).thenReturn(Optional.of(new TestBreed(BREED_ID, BREED_NAME)));
        when(petImageRepository.getReferenceById(request.imageId())).thenReturn(new TestPetImage(PET_IMAGE_ID));
        when(petRepository.save(any())).thenReturn(new TestPet(PET_ID,PET_NAME));

        PetCreateResponse result = petService.createPet(account, request);

        assertNotNull(result);
        assertEquals(PET_ID, result.id());
        assertEquals(PET_NAME, result.name());
    }

    @Test
    void getPetsReturnsAllPetsOfAccount() {
        Account account = new TestAccount(ACCOUNT_ID);
        when(petRepository.findAllByAccount_IdWithImages(account.getId()))
                .thenReturn(Collections.singletonList(new PetDao(PET_ID, "뽀삐", "image.jpg")));

        List<PetDto> result = petService.getPets(account);

        assertEquals(1, result.size());
        assertEquals(PET_ID, result.get(0).id());
        assertEquals("뽀삐", result.get(0).name());
        assertEquals("image.jpg", result.get(0).imageUrl());
    }

    @Test
    void getPetReturnsPetDetail() {
        when(petRepository.findById(PET_ID)).thenReturn(Optional.of(new TestPet(PET_ID, new TestBreed(BREED_ID, BREED_NAME), new TestPetImage(PET_IMAGE_ID))));
        GetPetDetailResponse result = petService.getPet(PET_ID);
        assertNotNull(result);
    }

    @Test
    void updatePetReturnsUpdatedPet() {
        PetUpdateRequest request = new PetUpdateRequest(
                BREED_ID,
                PET_IMAGE_ID,
                "name",
                0,
                LocalDate.of(2021, 1, 1),
                1.5f
        );

        when(petRepository.findById(PET_ID)).thenReturn(Optional.of(new TestPet(PET_ID)));
        when(breedRepository.findById(BREED_ID)).thenReturn(Optional.of(new TestBreed(BREED_ID,BREED_NAME)));
        when(petImageRepository.findById(PET_IMAGE_ID)).thenReturn(Optional.of(new TestPetImage(PET_IMAGE_ID)));
        PetCreateResponse result = petService.updatePet(PET_ID, request);

        assertNotNull(result);
    }

    @Test
    void deletePetDeletesPet() {
        petService.deletePet(PET_ID);
        verify(petRepository, times(1)).deleteById(PET_ID);
    }

    private static class TestAccount extends Account {
        public TestAccount(Long id) {
            super(id, null, null, null, null, false);
        }
    }

    private static class TestBreed extends Breed {
        public TestBreed(Long id, String name) {
            super(id, null, name);
        }
    }

    private static class TestPet extends Pet {
        public TestPet(Long id) {
            super(id, null, null, null, null,null, 0, null);
        }

        public TestPet (Long id, Breed breed, PetImage image) {
            super(id, null, breed, image, null, null, 0, null);
        }

        public TestPet(Long id, String name) {
            super(id, null, null, null, name, null, 0, null);
        }
    }

    private static class TestPetImage extends PetImage {
        public TestPetImage(Long id) {
            super(id, null, null);
        }
    }
}