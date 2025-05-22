package com.example.skeletonsecurity.security.dev;

import com.example.skeletonsecurity.base.domain.Email;
import com.example.skeletonsecurity.security.AppUserInfo;
import com.example.skeletonsecurity.security.AppUserInfoLookup;
import com.example.skeletonsecurity.security.domain.UserId;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Implementation of {@link UserDetailsService} and {@link AppUserInfoLookup} for development environments.
 * <p>
 * This class provides a simple in-memory implementation of both Spring Security's
 * {@link UserDetailsService} for authentication and the application's {@link AppUserInfoLookup}
 * for user information retrieval. It stores a collection of {@link DevUser} instances and
 * allows looking them up by either email address (for authentication) or user ID
 * (for information retrieval).
 * </p>
 * <p>
 * This implementation is specifically designed for development and testing purposes
 * and should not be used in production environments. It allows the application to
 * function with predefined test users without needing external authentication services
 * or databases.
 * </p>
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * DevUserDetailsService userService = new DevUserDetailsService(List.of(
 *     DevUser.builder("Admin User", "admin@example.com")
 *         .password("password")
 *         .roles("ADMIN")
 *         .build(),
 *     DevUser.builder("Regular User", "user@example.com")
 *         .password("password")
 *         .roles("USER")
 *         .build()
 * ));
 * }
 * </pre>
 * </p>
 *
 * @see DevUser The development user class stored in this service
 * @see UserDetailsService Spring Security's interface for loading user authentication details
 * @see AppUserInfoLookup The application's interface for looking up user information
 */
final class DevUserDetailsService implements UserDetailsService, AppUserInfoLookup {

    private final Map<Email, UserDetails> userByEmail;
    private final Map<UserId, AppUserInfo> userInfoById;

    /**
     * Creates a new service with the specified development users.
     * <p>
     * This constructor stores the provided users in memory, indexing them by
     * both email address and user ID for efficient lookups.
     * </p>
     *
     * @param users the development users to include in this service
     */
    DevUserDetailsService(Collection<DevUser> users) {
        userByEmail = new HashMap<>();
        userInfoById = new HashMap<>();
        users.forEach(user -> {
            userByEmail.put(user.getAppUser().email(), user);
            userInfoById.put(user.getAppUser().userId(), user.getAppUser());
        });
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Email email;
        try {
            email = Email.of(username);
        } catch (IllegalArgumentException e) {
            throw new UsernameNotFoundException(username);
        }
        return Optional
                .ofNullable(userByEmail.get(email))
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Override
    public Optional<AppUserInfo> findUserInfo(UserId userId) {
        return Optional.ofNullable(userInfoById.get(userId));
    }
}
