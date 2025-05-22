package com.example.skeletonsecurity.security.dev;

import com.example.skeletonsecurity.security.domain.UserId;

import java.util.UUID;

/**
 * Provides predefined sample users for development and testing environments.
 * <p>
 * This utility class contains constants and user objects that can be used consistently
 * across development configurations, integration tests, and other testing scenarios.
 * It ensures that the same test users are available in all contexts where they are needed,
 * promoting consistency and reducing duplication.
 * </p>
 * <p>
 * The class provides both the complete {@link DevUser} objects (for use in user details services)
 * and individual constants for user IDs and email addresses (for use in tests and assertions).
 * This allows tests to reference specific user properties without needing to extract them
 * from the user objects.
 * </p>
 * <p>
 * Usage in development configuration:
 * <pre>
 * {@code
 * @Bean
 * UserDetailsService userDetailsService() {
 *     return new DevUserDetailsService(SampleUsers.ADMIN, SampleUsers.USER);
 * }
 * }
 * </pre>
 * </p>
 * <p>
 * Usage in integration tests:
 * <pre>
 * {@code
 * @Test
 * @WithUserDetails(SampleUsers.USER_EMAIL)
 * public void testUserFunctionality() {
 *     // Test logic here
 *     assertThat(result.getCreatedBy()).isEqualTo(SampleUsers.USER_ID);
 * }
 * }
 * </pre>
 * </p>
 * <p>
 * <strong>Important:</strong> This class is intended only for development and testing purposes.
 * The sample users have fixed, well-known credentials and should never be used in production
 * environments.
 * </p>
 *
 * @see DevUser The development user implementation
 * @see DevUserDetailsService The service that uses these sample users
 * @see org.springframework.security.test.context.support.WithUserDetails
 */
public final class SampleUsers {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private SampleUsers() {
    }

    /**
     * The user ID for the admin sample user.
     * <p>
     * This constant can be used in tests and assertions to verify that operations
     * were performed by the admin user.
     * </p>
     */
    public static final UserId ADMIN_ID = UserId.of(UUID.randomUUID().toString());

    /**
     * The email address for the admin sample user.
     * <p>
     * This constant can be used with {@link org.springframework.security.test.context.support.WithUserDetails @WithUserDetails}
     * in tests to authenticate as the admin user.
     * </p>
     */
    public static final String ADMIN_EMAIL = "admin@example.com";

    /**
     * The admin sample user with administrative privileges.
     * <p>
     * This user has the "ADMIN" role and can be used in development configurations
     * and tests that require administrative access.
     * </p>
     */
    static DevUser ADMIN = DevUser.builder("Alice Administrator", ADMIN_EMAIL)
            .userId(ADMIN_ID)
            .password("tops3cr3t")
            .roles("ADMIN")
            .build();

    /**
     * The user ID for the regular sample user.
     * <p>
     * This constant can be used in tests and assertions to verify that operations
     * were performed by the regular user.
     * </p>
     */
    public static final UserId USER_ID = UserId.of(UUID.randomUUID().toString());

    /**
     * The email address for the regular sample user.
     * <p>
     * This constant can be used with {@link org.springframework.security.test.context.support.WithUserDetails @WithUserDetails}
     * in tests to authenticate as the regular user.
     * </p>
     */
    public static final String USER_EMAIL = "user@example.com";

    /**
     * The regular sample user with standard privileges.
     * <p>
     * This user has the "USER" role and can be used in development configurations
     * and tests that require standard user access.
     * </p>
     */
    static final DevUser USER = DevUser.builder("Ursula User", USER_EMAIL)
            .userId(USER_ID)
            .password("tops3cr3t")
            .roles("USER")
            .build();
}
