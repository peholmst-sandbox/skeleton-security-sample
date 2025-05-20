package com.example.skeletonsecurity.security.oidc;

import com.example.skeletonsecurity.base.domain.Email;
import com.example.skeletonsecurity.security.AppUserInfo;
import com.example.skeletonsecurity.security.domain.UserId;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;

import java.time.DateTimeException;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Locale;
import java.util.Optional;

/**
 * Extended version of {@link DefaultOidcUser} that implements {@link AppUserInfo}. It could also have been implemented
 * as a wrapper around {@link DefaultOidcUser}, but this would have required implementing more interfaces.
 */
final class OidcAppUser extends DefaultOidcUser implements AppUserInfo {

    private final UserId userId;
    private final Email email;
    private final ZoneId zoneId;
    private final Locale locale;

    OidcAppUser(Collection<? extends GrantedAuthority> authorities, OidcIdToken idToken, OidcUserInfo userInfo) {
        this(authorities, idToken, userInfo, IdTokenClaimNames.SUB);
    }

    public OidcAppUser(Collection<? extends GrantedAuthority> authorities, OidcIdToken idToken, OidcUserInfo userInfo, String nameAttributeKey) {
        super(authorities, idToken, userInfo, nameAttributeKey);
        userId = UserId.of(getSubject());
        email = Email.of(getEmail());
        zoneId = parseZoneInfo(getZoneInfo());
        locale = parseLocale(getLocale());
    }

    private static ZoneId parseZoneInfo(@Nullable String zoneInfo) {
        if (zoneInfo == null) {
            return ZoneId.systemDefault();
        }
        try {
            return ZoneId.of(zoneInfo);
        } catch (DateTimeException e) {
            return ZoneId.systemDefault();
        }
    }

    private static Locale parseLocale(@Nullable String locale) {
        if (locale == null) {
            return Locale.getDefault();
        }
        return Locale.forLanguageTag(locale);
    }

    @Override
    public UserId userId() {
        return userId;
    }

    @Override
    public String fullName() {
        return getFullName();
    }

    @Override
    public Optional<String> profileUrl() {
        return Optional.ofNullable(getProfile());
    }

    @Override
    public Optional<String> pictureUrl() {
        return Optional.ofNullable(getPicture());
    }

    @Override
    public Email email() {
        return email;
    }

    @Override
    public ZoneId zoneId() {
        return zoneId;
    }

    @Override
    public Locale locale() {
        return locale;
    }
}
