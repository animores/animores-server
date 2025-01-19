package animores.serverapi.pet.dto.response;

import animores.serverapi.pet.entity.PetImage;

public record PetImageResponse(
    Long id,
    String imageUrl
) {

    public static PetImageResponse fromEntity(PetImage image) {
        return new PetImageResponse(image.getId(), image.getUrl());
    }
}
