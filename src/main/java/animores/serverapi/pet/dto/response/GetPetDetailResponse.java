package animores.serverapi.pet.dto.response;

import animores.serverapi.pet.entity.Pet;
import java.time.LocalDate;

public record GetPetDetailResponse(
    Long id,
    String name,
    String imageUrl,
    SpeciesResponse species,
    BreedResponse breed,
    LocalDate birthday,
    int gender,
    Float weight) {

    public static GetPetDetailResponse fromEntity(Pet pet) {
        return new GetPetDetailResponse(
            pet.getId(),
            pet.getName(),
            pet.getImage().getUrl(),
            SpeciesResponse.fromEntity(pet.getBreed().getSpecies()),
            BreedResponse.fromEntity(pet.getBreed()),
            pet.getBirthday(),
            pet.getGender(),
            pet.getWeight());
    }
}
