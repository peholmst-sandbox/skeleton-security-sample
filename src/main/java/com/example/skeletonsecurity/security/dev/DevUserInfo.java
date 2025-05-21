package com.example.skeletonsecurity.security.dev;

import com.example.skeletonsecurity.base.domain.Email;
import com.example.skeletonsecurity.security.AppUserInfo;
import com.example.skeletonsecurity.security.domain.UserId;
import org.jspecify.annotations.Nullable;

import java.time.ZoneId;
import java.util.Locale;

import static java.util.Objects.requireNonNull;

/**
 * Implementation of {@link AppUserInfo} used by {@link DevUser} for development environments.
 * <p>
 * This record provides a simple immutable implementation of the {@link AppUserInfo} interface,
 * storing user information for development and testing purposes. It contains all the essential
 * user profile data needed by the application, with appropriate null checks for required fields.
 * </p>
 * <p>
 * This implementation is specifically designed for development and test environments and should
 * not be used in production. It's primarily used by the {@link DevUser} class to represent
 * test user information.
 * </p>
 *
 * @param userId     the unique identifier for the user (never {@code null})
 * @param fullName   the user's full name (never {@code null})
 * @param profileUrl the URL to the user's profile page, or {@code null} if not available
 * @param pictureUrl the URL to the user's profile picture, or {@code null} if not available
 * @param email      the user's email address (never {@code null})
 * @param zoneId     the user's time zone (never {@code null})
 * @param locale     the user's locale (never {@code null})
 * @see DevUser The development user class that uses this record
 * @see AppUserInfo The interface this record implements
 */
record DevUserInfo(
        UserId userId,
        String fullName,
        @Nullable String profileUrl,
        @Nullable String pictureUrl,
        Email email,
        ZoneId zoneId,
        Locale locale
) implements AppUserInfo {

    DevUserInfo {
        requireNonNull(userId);
        requireNonNull(fullName);
        requireNonNull(email);
        requireNonNull(zoneId);
        requireNonNull(locale);
    }
}
