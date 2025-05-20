package com.example.skeletonsecurity.security.dev;

import com.example.skeletonsecurity.base.domain.Email;
import com.example.skeletonsecurity.security.AppUserInfo;
import com.example.skeletonsecurity.security.AppUserInfoLookup;
import com.example.skeletonsecurity.security.domain.UserId;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Implementation of {@link UserDetailsService} and {@link AppUserInfoLookup} that uses a fixed set of users. Intended
 * for development and testing.
 */
class DevUserDetailsService implements UserDetailsService, AppUserInfoLookup {

    private final Map<Email, DevUser> userByEmail;
    private final Map<UserId, DevUser> userById;

    DevUserDetailsService(DevUser... users) {
        userByEmail = new HashMap<>();
        userById = new HashMap<>();
        Stream.of(users).forEach(user -> {
            userByEmail.put(user.email(), user);
            userById.put(user.userId(), user);
        });
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return Optional
                .ofNullable(userByEmail.get(Email.of(username)))
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Override
    public Optional<AppUserInfo> getUserInfo(UserId userId) {
        return Optional.ofNullable(userById.get(userId));
    }
}
