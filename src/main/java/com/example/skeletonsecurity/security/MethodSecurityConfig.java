package com.example.skeletonsecurity.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

/**
 * Configuration class that enables method-level security annotations in Spring Security.
 * <p>
 * All authenticated principals in the application implement {@link AppUserPrincipal},
 * providing consistent ways to access user information in security expressions:
 * <ul>
 *   <li>{@code authentication.principal.appUser.userId} - Access the user ID object</li>
 *   <li>{@code authentication.name} - Access the user ID as a string</li>
 * </ul>
 * </p>
 * <p>
 * Example usage in security expressions:
 * <pre>
 * {@code
 * // Using the user ID object
 * @PreAuthorize("authentication.principal.appUser.userId == #document.ownerId")
 * public void updateDocument(Document document) { ... }
 *
 * // Using the user ID as a string
 * @PreAuthorize("authentication.name == #documentOwnerId")
 * public void deleteDocument(String documentOwnerId) { ... }
 * }
 * </pre>
 * </p>
 */
@EnableMethodSecurity
@Configuration
class MethodSecurityConfig {
}
