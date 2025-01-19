package animores.serverapi.to_do.dto.response;

import animores.serverapi.to_do.entity.PetToDoRelationship;

public record PetResponse(
    Long id,
    String name
) {

    public static PetResponse fromPetToDoRelationship(PetToDoRelationship petToDoRelationship) {
        return new PetResponse(petToDoRelationship.getPet().getId(),
            petToDoRelationship.getPet().getName());
    }
}
