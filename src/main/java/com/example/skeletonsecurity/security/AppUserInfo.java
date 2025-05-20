package com.example.skeletonsecurity.security;

import com.example.skeletonsecurity.base.domain.Email;
import com.example.skeletonsecurity.security.domain.UserId;

import java.time.ZoneId;
import java.util.Locale;
import java.util.Optional;

/**
 * Interface for accessing information about an application user. The interface can be used to access information
 * about both the current user and any user (for example, to when showing who has last modified a record). Because of
 * this, the interface does not extend {@link org.springframework.security.core.userdetails.UserDetails} or
 * {@link org.springframework.security.core.AuthenticatedPrincipal}.
 * <p>
 * The methods use domain primitives where appropriate and return {@code Optional}s instead of {@code null} for
 * optional values. This is IMO a more modern and safer approach of handling data in Java.
 * </p>
 * <p>
 * However, because of naming conflicts with {@link org.springframework.security.oauth2.core.oidc.IdTokenClaimAccessor},
 * the `get` prefix has been removed, as this interface uses a more traditional approach with raw strings and
 * {@code null} for optional values.
 * </p>
 */
public interface AppUserInfo {

    UserId userId();

    String fullName();

    Optional<String> profileUrl();

    Optional<String> pictureUrl();

    Email email();

    ZoneId zoneId();

    Locale locale();
}
