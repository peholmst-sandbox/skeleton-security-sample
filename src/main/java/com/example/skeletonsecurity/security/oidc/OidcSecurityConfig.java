package com.example.skeletonsecurity.security.oidc;

import com.example.skeletonsecurity.security.AppUserInfoLookup;
import com.vaadin.flow.spring.security.UidlRedirectStrategy;
import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OidcLogoutConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.util.StringUtils;

import java.util.LinkedHashSet;

/**
 * OIDC security configuration. This class should not be needed if the application is using Control Center. However,
 * that would require a change to Control Center so that you can plug in a custom user mapper that returns
 * {@link OidcAppUser} instead of {@link org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser}.
 */
@EnableWebSecurity
@Configuration
@Profile("oidc")
class OidcSecurityConfig extends VaadinWebSecurity {

    private final ClientRegistrationRepository clientRegistrationRepository;

    OidcSecurityConfig(ClientRegistrationRepository clientRegistrationRepository) {
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http.oauth2Login(this::configureOidcLogin);
        setOAuth2LoginPage(http, "/oauth2/authorization/keycloak");
        http.oidcLogout(this::configureOidcLogout);
        http.logout(this::configureLogout);
    }

    private void configureOidcLogin(OAuth2LoginConfigurer<HttpSecurity> login) {
        login.defaultSuccessUrl("/", true);
        login.userInfoEndpoint(userInfoEndpoint -> {
            var userService = new OidcUserService();
            userService.setOidcUserMapper(this::userMapper);
            userInfoEndpoint.oidcUserService(userService);
        });
    }

    private void configureOidcLogout(OidcLogoutConfigurer<HttpSecurity> logout) {
        logout.backChannel(backChannel -> backChannel.logoutUri("{baseScheme}://{baseHost}{basePort}/logout"));
    }

    private void configureLogout(LogoutConfigurer<HttpSecurity> logout) {
        var logoutSuccessHandler = new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository);
        logoutSuccessHandler.setPostLogoutRedirectUri("{baseUrl}");
        logoutSuccessHandler.setRedirectStrategy(new UidlRedirectStrategy());
        logout.logoutSuccessHandler(logoutSuccessHandler);
    }

    private OidcAppUser userMapper(OidcUserRequest userRequest, OidcUserInfo userInfo) {
        var authorities = new LinkedHashSet<GrantedAuthority>();
        authorities
                .add(new OidcUserAuthority(userRequest.getIdToken(), userInfo));

        var accessToken = userRequest.getAccessToken();
        for (var scope : accessToken.getScopes()) {
            authorities.add(new SimpleGrantedAuthority("SCOPE_" + scope));
        }

        // TODO Extract roles from the token and add them as authorities

        var providerDetails = userRequest.getClientRegistration()
                .getProviderDetails();
        var userNameAttributeName = providerDetails.getUserInfoEndpoint()
                .getUserNameAttributeName();
        if (StringUtils.hasText(userNameAttributeName)) {
            return new OidcAppUser(authorities, userRequest.getIdToken(),
                    userInfo, userNameAttributeName);
        }
        return new OidcAppUser(authorities, userRequest.getIdToken(),
                userInfo);
    }

    @Bean
    public AppUserInfoLookup appUserInfoLookup() {
        return new OidcAppUserInfoLookup();
    }
}
