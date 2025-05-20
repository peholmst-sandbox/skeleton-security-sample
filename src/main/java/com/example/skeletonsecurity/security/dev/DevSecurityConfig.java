package com.example.skeletonsecurity.security.dev;

import com.example.skeletonsecurity.security.dev.ui.view.LoginView;
import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Security configuration for development.
 */
@EnableWebSecurity
@Configuration
@Profile("dev")
class DevSecurityConfig extends VaadinWebSecurity {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        setLoginView(http, LoginView.class);
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new DevUserDetailsService(
                DevUser.builder("Alice Administrator", "admin@example.com")
                        .password("tops3cr3t")
                        .roles("ADMIN")
                        .build(),
                DevUser.builder("Ursula User", "user@example.com")
                        .password("tops3cr3t")
                        .roles("USER")
                        .build()
        );
    }
}
