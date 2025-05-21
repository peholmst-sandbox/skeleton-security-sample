package com.example.skeletonsecurity.security;

import com.example.skeletonsecurity.base.domain.Email;
import com.example.skeletonsecurity.security.domain.UserId;
import org.jspecify.annotations.Nullable;

import java.time.ZoneId;
import java.util.Locale;

/**
 * Interface for accessing information about an application user.
 * <p>
 * This interface provides standard methods to access user identity and profile information
 * throughout the application. It can be used in various contexts, both for retrieving the
 * current authenticated user and for accessing information about any user in the system
 * (e.g., when displaying audit information about who last modified a record).
 * </p>
 * <p>
 * Note: This interface intentionally does not extend {@link org.springframework.security.core.userdetails.UserDetails UserDetails}
 * or {@link org.springframework.security.core.AuthenticatedPrincipal AuthenticatedPrincipal} to maintain separation
 * between authentication concerns and general user information access. For the same reason it does not contain
 * information about the user's roles or authorities.
 * </p>
 */
public interface AppUserInfo {

    /**
     * Returns the user's unique identifier within the application.
     * <p>
     * For OIDC authenticated users, this typically corresponds to the "subject" claim.
     * This identifier remains consistent across sessions and is used as the primary key
     * for user-related data.
     * </p>
     *
     * @return the unique user identifier (never {@code null})
     */
    UserId userId();

    /**
     * Returns the user's full display name.
     * <p>
     * This typically combines the user's first and last name in a format appropriate
     * for display in the user interface.
     * </p>
     *
     * @return the user's full name (never {@code null})
     */
    String fullName();

    /**
     * Returns a URL to the user's profile page in the application or external system.
     * <p>
     * Implementations may return {@code null} if no profile page is available or
     * if the current context doesn't have permission to access this information.
     * </p>
     *
     * @return URL to the user's profile, or {@code null} if not available
     */
    default @Nullable String profileUrl() {
        return null;
    }

    /**
     * Returns a URL to the user's profile picture or avatar.
     * <p>
     * Implementations may return {@code null} if no picture is available or
     * if the current context doesn't have permission to access this information.
     * </p>
     *
     * @return URL to the user's picture, or {@code null} if not available
     */
    default @Nullable String pictureUrl() {
        return null;
    }

    /**
     * Returns the user's email address.
     * <p>
     * This email address is considered the primary contact method for the user
     * and may be used for notifications and communications.
     * </p>
     *
     * @return the user's email address (never {@code null})
     */
    Email email();

    /**
     * Returns the user's preferred time zone.
     * <p>
     * This time zone is used for displaying dates and times in the user interface.
     * If the user has not explicitly set a time zone preference, the system default
     * time zone is returned as a fallback.
     * </p>
     *
     * @return the user's time zone (never {@code null})
     */
    default ZoneId zoneId() {
        return ZoneId.systemDefault();
    }

    /**
     * Returns the user's preferred locale for internationalization.
     * <p>
     * This locale is used for language selection and formatting of numbers, dates,
     * and currencies in the user interface. If the user has not explicitly set a locale
     * preference, the system default locale is returned as a fallback.
     * </p>
     *
     * @return the user's locale (never {@code null})
     */
    default Locale locale() {
        return Locale.getDefault();
    }
}
