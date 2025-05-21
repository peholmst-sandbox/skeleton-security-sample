package com.example.skeletonsecurity.security;

/**
 * Interface for objects that can provide access to {@link AppUserInfo} details.
 * <p>
 * This interface serves as an adapter pattern implementation, allowing objects that cannot
 * directly implement {@link AppUserInfo} (often due to inheritance constraints or naming conflicts)
 * to still provide user information to the authentication system.
 * </p>
 * <p>
 * It is commonly implemented by authentication principals from external identity providers
 * or legacy systems that already have their own user representation but need to be
 * compatible with the application's user information model.
 * </p>
 *
 * @see CurrentUser
 */
public interface HasAppUserInfo {

    /**
     * Returns the {@link AppUserInfo} representation of the authenticated user.
     * <p>
     * This method provides a bridge between external or incompatible user representations
     * and the application's standardized user information interface. Authentication systems
     * can use this method to obtain consistent user information regardless of the actual
     * principal implementation.
     * </p>
     *
     * @return the {@link AppUserInfo} representation of the user (never {@code null})
     */
    AppUserInfo getAppUserInfo();
}
