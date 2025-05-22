package com.example.skeletonsecurity.security;

import com.example.skeletonsecurity.security.domain.UserId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import java.time.Clock;
import java.util.Optional;

/**
 * Common security configuration that enables method-level security and JPA auditing.
 * <p>
 * This configuration provides foundational security and auditing capabilities that are
 * shared across all authentication mechanisms in the application. It enables:
 * <ul>
 *   <li>Spring Security's method-level security annotations</li>
 *   <li>JPA auditing with automatic tracking of who and when entities are modified</li>
 * </ul>
 * </p>
 * <p>
 * Method security allows the use of the following annotations throughout the application:
 * <ul>
 *   <li>{@link org.springframework.security.access.prepost.PreAuthorize @PreAuthorize} - Controls method access based on expressions evaluated before method execution</li>
 *   <li>{@link org.springframework.security.access.prepost.PostAuthorize @PostAuthorize} - Controls method access based on expressions evaluated after method execution</li>
 *   <li>{@link org.springframework.security.access.prepost.PreFilter @PreFilter} - Filters method arguments before method execution</li>
 *   <li>{@link org.springframework.security.access.prepost.PostFilter @PostFilter} - Filters method return values after method execution</li>
 * </ul>
 * </p>
 * <p>
 * JPA auditing automatically populates audit fields in entities annotated with
 * {@link org.springframework.data.annotation.CreatedBy @CreatedBy},
 * {@link org.springframework.data.annotation.LastModifiedBy @LastModifiedBy},
 * {@link org.springframework.data.annotation.CreatedDate @CreatedDate}, and
 * {@link org.springframework.data.annotation.LastModifiedDate @LastModifiedDate}.
 * <strong>Important:</strong> Entities must also be annotated with
 * {@link org.springframework.data.jpa.domain.support.AuditingEntityListener @EntityListeners(AuditingEntityListener.class)}
 * (or have a superclass with this annotation) for the auditing annotations to work.
 * </p>
 * <p>
 * All authenticated principals must implement {@link AppUserPrincipal}, allowing
 * security expressions to access user information with the pattern:
 * {@code authentication.principal.appUser.userId}
 * </p>
 * <p>
 * Example usage in entities and methods:
 * <pre>
 * {@code
 * // JPA Entity with auditing
 * @Entity
 * public class Document {
 *     @CreatedBy
 *     private UserId createdBy;
 *
 *     @LastModifiedBy
 *     private UserId lastModifiedBy;
 *
 *     @CreatedDate
 *     private Instant createdDate;
 *
 *     @LastModifiedDate
 *     private Instant lastModifiedDate;
 * }
 *
 * // Method security
 * @PreAuthorize("authentication.principal.appUser.userId == #document.ownerId")
 * public void updateDocument(Document document) { ... }
 *
 * @PostAuthorize("returnObject.createdBy == authentication.principal.appUser.userId")
 * public Document getDocument(long id) { ... }
 * }
 * </pre>
 * </p>
 *
 * @see AppUserPrincipal The principal interface that all authenticated users implement
 * @see CurrentUser Utility for accessing the current user information
 * @see org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
 * @see org.springframework.data.jpa.repository.config.EnableJpaAuditing
 * @see org.springframework.data.jpa.domain.support.AuditingEntityListener
 */
@EnableMethodSecurity
@EnableJpaAuditing
@Configuration
class CommonSecurityConfig {

    /**
     * Provides the current user for JPA auditing purposes.
     * <p>
     * This bean is used by Spring Data JPA to automatically populate
     * {@link org.springframework.data.annotation.CreatedBy @CreatedBy} and
     * {@link org.springframework.data.annotation.LastModifiedBy @LastModifiedBy}
     * fields in audited entities with the current user's ID.
     * </p>
     * <p>
     * If no user is currently authenticated, the auditor will be empty,
     * and the audit fields will remain null.
     * </p>
     * <p>
     * <strong>Note:</strong> Entities must be annotated with
     * {@code @EntityListeners(AuditingEntityListener.class)} for this auditor
     * to be used.
     * </p>
     *
     * @return an {@link AuditorAware} that provides the current user's ID
     */
    @Bean
    public AuditorAware<UserId> auditorAware() {
        return () -> CurrentUser.get().map(AppUserInfo::userId);
    }

    /**
     * Provides the current date and time for JPA auditing purposes.
     * <p>
     * This bean is used by Spring Data JPA to automatically populate
     * {@link org.springframework.data.annotation.CreatedDate @CreatedDate} and
     * {@link org.springframework.data.annotation.LastModifiedDate @LastModifiedDate}
     * fields in audited entities with the current timestamp.
     * </p>
     * <p>
     * The date and time are provided by the injected {@link Clock}, allowing
     * for consistent time handling and easier testing with fixed clocks.
     * </p>
     * <p>
     * <strong>Note:</strong> Entities must be annotated with
     * {@code @EntityListeners(AuditingEntityListener.class)} for this date time
     * provider to be used.
     * </p>
     *
     * @param clock the clock to use for generating timestamps
     * @return a {@link DateTimeProvider} that provides the current instant
     */
    @Bean
    public DateTimeProvider dateTimeProvider(Clock clock) {
        return () -> Optional.of(clock.instant());
    }
}
