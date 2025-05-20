package com.example.skeletonsecurity.security.dev;

import com.example.skeletonsecurity.base.domain.Email;
import com.example.skeletonsecurity.security.AppUserInfo;
import com.example.skeletonsecurity.security.domain.UserId;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.ZoneId;
import java.util.*;

import static java.util.Objects.requireNonNull;

/**
 * Implementation of {@link AppUserInfo} for use with {@link DevUserDetailsService}.
 */
final class DevUser implements AppUserInfo, UserDetails {

    private final UserId userId;
    private final String fullName;
    private final @Nullable String profileUrl;
    private final @Nullable String pictureUrl;
    private final Email email;
    private final ZoneId zoneInfo;
    private final Locale locale;
    private final Set<GrantedAuthority> authorities;
    private final String password;

    DevUser(UserId userId, String fullName, @Nullable String profileUrl, @Nullable String pictureUrl,
            Email email, ZoneId zoneInfo, Locale locale, Collection<GrantedAuthority> authorities,
            String password) {
        this.userId = userId;
        this.fullName = fullName;
        this.profileUrl = profileUrl;
        this.pictureUrl = pictureUrl;
        this.email = email;
        this.zoneInfo = zoneInfo;
        this.locale = locale;
        this.authorities = Set.copyOf(authorities);
        this.password = password;
    }

    @Override
    public UserId userId() {
        return userId;
    }

    @Override
    public String fullName() {
        return fullName;
    }

    @Override
    public Optional<String> profileUrl() {
        return Optional.ofNullable(profileUrl);
    }

    @Override
    public Optional<String> pictureUrl() {
        return Optional.ofNullable(pictureUrl);
    }

    @Override
    public Email email() {
        return email;
    }

    @Override
    public ZoneId zoneId() {
        return zoneInfo;
    }

    @Override
    public Locale locale() {
        return locale;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userId.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DevUser user) {
            return this.userId.equals(user.userId);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.userId.hashCode();
    }

    public static DevUserBuilder builder(String fullName, String email) {
        return new DevUserBuilder(fullName, email);
    }

    static final class DevUserBuilder {

        private static final PasswordEncoder PASSWORD_ENCODER = PasswordEncoderFactories.createDelegatingPasswordEncoder();

        private final UserId userId = UserId.of(UUID.randomUUID().toString());
        private final String fullName;
        private final Email email;
        private @Nullable String profileUrl;
        private @Nullable String pictureUrl;
        private ZoneId zoneInfo = ZoneId.systemDefault();
        private Locale locale = Locale.getDefault();
        private List<GrantedAuthority> authorities = Collections.emptyList();
        private @Nullable String password;

        DevUserBuilder(String fullName, String email) {
            this.fullName = requireNonNull(fullName);
            this.email = Email.of(email);
        }

        public DevUserBuilder profileUrl(String profileUrl) {
            this.profileUrl = profileUrl;
            return this;
        }

        public DevUserBuilder pictureUrl(String pictureUrl) {
            this.pictureUrl = pictureUrl;
            return this;
        }

        public DevUserBuilder zoneInfo(ZoneId zoneInfo) {
            this.zoneInfo = requireNonNull(zoneInfo);
            return this;
        }

        public DevUserBuilder locale(Locale locale) {
            this.locale = requireNonNull(locale);
            return this;
        }

        public DevUserBuilder roles(String... roles) {
            this.authorities = new ArrayList<>(roles.length);
            for (var role : roles) {
                this.authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
            }
            return this;
        }

        public DevUserBuilder authorities(String... authorities) {
            return authorities(AuthorityUtils.createAuthorityList(authorities));
        }

        public DevUserBuilder authorities(Collection<? extends GrantedAuthority> authorities) {
            this.authorities = new ArrayList<>(authorities);
            return this;
        }

        public DevUserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public DevUser build() {
            if (password == null) {
                throw new IllegalStateException("Password must be set before building the user");
            }
            var encodedPassword = PASSWORD_ENCODER.encode(password);
            return new DevUser(
                    userId,
                    fullName,
                    profileUrl,
                    pictureUrl,
                    email,
                    zoneInfo,
                    locale,
                    authorities,
                    encodedPassword);
        }
    }
}
