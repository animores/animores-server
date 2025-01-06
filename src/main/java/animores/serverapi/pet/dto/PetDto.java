package animores.serverapi.pet.dto;

import animores.serverapi.pet.dao.PetDao;

public record PetDto(
    Long id,
    String name,
    String imageUrl
) {

    public static PetDto fromDao(PetDao pet) {
        return new PetDto(pet.id(), pet.name(), pet.imageUrl());
    }
}