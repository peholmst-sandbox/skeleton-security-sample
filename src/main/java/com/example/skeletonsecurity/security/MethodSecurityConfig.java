package com.example.skeletonsecurity.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

/**
 * Configuration class for method security. This should be the same regardless of the authenetication mechanism.
 */
@EnableMethodSecurity
@Configuration
class MethodSecurityConfig {
}
