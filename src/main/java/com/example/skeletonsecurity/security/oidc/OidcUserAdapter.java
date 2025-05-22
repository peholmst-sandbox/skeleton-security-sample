package com.example.skeletonsecurity.security.oidc;

import com.example.skeletonsecurity.base.domain.Email;
import com.example.skeletonsecurity.security.AppUserInfo;
import com.example.skeletonsecurity.security.AppUserPrincipal;
import com.example.skeletonsecurity.security.domain.UserId;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.time.DateTimeException;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;

/**
 * Adapter implementation that bridges Spring Security's OIDC user representation
 * with the application's {@link AppUserInfo} interface.
 * <p>
 * This class wraps an {@link OidcUser} and implements {@link AppUserPrincipal} to provide
 * consistent access to user information throughout the application without method conflicts.
 * </p>
 */
public final class OidcUserAdapter implements OidcUser, AppUserPrincipal {

    private final OidcUser delegate;
    private final AppUserInfo appUserInfo;

    /**
     * Creates a new adapter for the specified OIDC user.
     *
     * @param oidcUser the OIDC user to adapt
     */
    public OidcUserAdapter(OidcUser oidcUser) {
        this.delegate = oidcUser;
        this.appUserInfo = createAppUserInfo(oidcUser);
    }

    private static AppUserInfo createAppUserInfo(OidcUser oidcUser) {
        return new AppUserInfo() {
            private final UserId userId = UserId.of(oidcUser.getSubject());
            private final Email email = Email.of(oidcUser.getEmail());
            private final ZoneId zoneId = parseZoneInfo(oidcUser.getZoneInfo());
            private final Locale locale = parseLocale(oidcUser.getLocale());

            @Override
            public UserId userId() {
                return userId;
            }

            @Override
            public String fullName() {
                return oidcUser.getFullName();
            }

            @Override
            public @Nullable String profileUrl() {
                return oidcUser.getProfile();
            }

            @Override
            public @Nullable String pictureUrl() {
                return oidcUser.getPicture();
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
        };
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
    public AppUserInfo getAppUser() {
        return appUserInfo;
    }

    @Override
    public Map<String, Object> getClaims() {
        return delegate.getClaims();
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return delegate.getUserInfo();
    }

    @Override
    public OidcIdToken getIdToken() {
        return delegate.getIdToken();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return delegate.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return delegate.getAuthorities();
    }

    @Override
    public String getName() {
        return delegate.getName();
    }
}
