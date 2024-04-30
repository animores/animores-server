package animores.serverapi.profile.domain.vo;

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
