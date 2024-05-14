package animores.serverapi.pet.dto.response;

import animores.serverapi.pet.entity.Breed;

public record BreedResponse(Long id, String name) {

    public static BreedResponse fromEntity(Breed breed) {
        return new BreedResponse(breed.getId(), breed.getName());
    }
}
