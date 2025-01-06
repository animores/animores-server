package animores.serverapi.profile.dao;

import animores.serverapi.profile.domain.Profile;

public record ProfileVo(
    String name,

    String imageUrl
) {

    public static ProfileVo fromProfile(Profile profile) {
        return new ProfileVo(
            profile.getName(),
            profile.getImageUrl());
    }
}
