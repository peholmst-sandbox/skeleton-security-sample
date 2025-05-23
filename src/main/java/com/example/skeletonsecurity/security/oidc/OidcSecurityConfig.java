package com.example.skeletonsecurity.security.oidc;

import com.example.skeletonsecurity.security.AppUserInfoLookup;
import com.example.skeletonsecurity.security.CachingAppUserInfoLookup;
import com.vaadin.flow.spring.security.VaadinSecurityConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
@Profile("!dev")
class OidcSecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .with(VaadinSecurityConfigurer.vaadin(), configurer ->
                        configurer.oauth2LoginPage("/oauth2/authorization/oidc")
                )
                .oauth2Login(oauth2 ->
                        oauth2.userInfoEndpoint(userInfo ->
                                userInfo.oidcUserService(new OidcUserAdapterService(new OidcUserService()))
                        )
                )
                .build();
    }

    @Bean
    AppUserInfoLookup appUserInfoLookup(ClientRegistrationRepository clientRegistrationRepository) {
        var registration = clientRegistrationRepository.findByRegistrationId("oidc");
        var keycloakLookup = new KeycloakAppUserInfoLookup(KeycloakAppUserInfoLookup.createCredentials(
                registration.getProviderDetails().getIssuerUri(),
                registration.getClientId(),
                registration.getClientSecret()
        ));
        return CachingAppUserInfoLookup.builder()
                .delegate(keycloakLookup)
                .build();
    }
}
