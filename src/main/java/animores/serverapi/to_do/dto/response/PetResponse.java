package animores.serverapi.to_do.dto.response;

import animores.serverapi.to_do.entity.PetToDoRelationship;
import com.querydsl.core.annotations.QueryProjection;

public record PetResponse(
    Long id,
    String name
) {

    public static PetResponse fromPetToDoRelationship(PetToDoRelationship petToDoRelationship) {
        return new PetResponse(petToDoRelationship.getPet().getId(),
            petToDoRelationship.getPet().getName());
    }

    @QueryProjection
    public PetResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
