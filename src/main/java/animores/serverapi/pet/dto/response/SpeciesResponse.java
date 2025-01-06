package animores.serverapi.pet.dto.response;

import animores.serverapi.pet.entity.Species;

public record SpeciesResponse(
    Long id,
    String name
) {

    public static SpeciesResponse fromEntity(Species species) {
        return new SpeciesResponse(species.getId(), species.getName());
    }
}
