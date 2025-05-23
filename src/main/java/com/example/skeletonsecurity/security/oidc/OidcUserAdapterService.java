package com.example.skeletonsecurity.security.oidc;

import com.example.skeletonsecurity.security.AppUserPrincipal;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import static java.util.Objects.requireNonNull;

/**
 * Adapter service that wraps OIDC users to implement the application's {@link AppUserPrincipal} interface.
 * <p>
 * This service acts as a decorator around another {@link OAuth2UserService}, automatically wrapping
 * the loaded {@link OidcUser} objects with {@link OidcUserAdapter} instances. This ensures that
 * all OIDC users in the application implement {@link AppUserPrincipal}, providing consistent
 * access to user information regardless of the authentication mechanism.
 * </p>
 * <p>
 * The service delegates the actual user loading to another {@link OAuth2UserService} (typically
 * the default {@link org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService})
 * and then wraps the result to make it compatible with the application's user model.
 * </p>
 * <p>
 * This adapter pattern allows the application to:
 * <ul>
 *   <li>Use consistent security expressions like {@code authentication.principal.appUser.userId}</li>
 *   <li>Avoid method name conflicts between OIDC interfaces and the application's user model</li>
 *   <li>Provide a uniform interface for all authentication mechanisms</li>
 * </ul>
 * </p>
 * <p>
 * Example configuration:
 * <pre>
 * {@code
 * @Bean
 * public OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
 *     return new AdaptingOidcUserService(new OidcUserService());
 * }
 * }
 * </pre>
 * </p>
 *
 * @see OidcUserAdapter The wrapper class that implements AppUserPrincipal
 * @see AppUserPrincipal The application's principal interface
 * @see org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService The default OIDC user service
 */
final class OidcUserAdapterService implements OAuth2UserService<OidcUserRequest, OidcUser> {

    private final OAuth2UserService<OidcUserRequest, OidcUser> delegate;

    /**
     * Creates a new adapting OIDC user service with the specified delegate.
     * <p>
     * The delegate service is responsible for the actual loading of user information
     * from the OIDC provider. This adapting service wraps the results to ensure
     * compatibility with the application's user model.
     * </p>
     *
     * @param delegate the underlying OIDC user service to delegate user loading to (never {@code null})
     */
    OidcUserAdapterService(OAuth2UserService<OidcUserRequest, OidcUser> delegate) {
        this.delegate = requireNonNull(delegate);
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        return new OidcUserAdapter(delegate.loadUser(userRequest));
    }
}
