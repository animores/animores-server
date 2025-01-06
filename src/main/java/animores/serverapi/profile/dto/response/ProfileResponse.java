package animores.serverapi.profile.dto.response;

import animores.serverapi.profile.domain.Profile;

public record ProfileResponse(
    Long id,

    String name,

    String imageUrl
) {

    public static ProfileResponse fromEntity(Profile profile) {
        return new ProfileResponse(
            profile.getId(),
            profile.getName(),
            profile.getImageUrl());
    }
}
