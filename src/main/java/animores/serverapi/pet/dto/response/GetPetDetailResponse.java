package animores.serverapi.pet.dto.response;

import animores.serverapi.pet.entity.Pet;

import java.time.LocalDate;

public record GetPetDetailResponse(
        Long id,
        String name,
        BreedResponse breed,
        LocalDate birthday,
        int gender) {

    public static GetPetDetailResponse fromEntity(Pet pet) {
        return new GetPetDetailResponse(
                pet.getId(),
                pet.getName(),
                BreedResponse.fromEntity(pet.getBreed()),
                pet.getBirthday(),
                pet.getGender());
    }
}
