package com.example.skeletonsecurity.security.oidc;

import com.example.skeletonsecurity.security.AppUserInfo;
import com.example.skeletonsecurity.security.AppUserInfoLookup;
import com.example.skeletonsecurity.security.domain.UserId;

import java.util.Optional;

/**
 * When using Keycloak, this class would have to use Keycloak's client to fetch information about the given user.
 * For improved performance, the information should be cached in memory for some time so that every query doesn't
 * result in a network round trip.
 */
class OidcAppUserInfoLookup implements AppUserInfoLookup {

    @Override
    public Optional<AppUserInfo> getUserInfo(UserId userId) {
        // TODO Implement me!
        return Optional.empty();
    }
}
