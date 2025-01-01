package com.lcwd.electronic.store.ElectronicStore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity(debug = true) // debug = true for Dev purposes only.
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
        // Configure the urls
        security.authorizeHttpRequests( request -> {
            // Means product API URLs are Authenticated only
            request.requestMatchers("/v1/product-api/**").authenticated();
            // All other URLs are accessible without Authentication
            request.anyRequest().permitAll();
        });

        // Type of the security used
        security.httpBasic(Customizer.withDefaults());
        return security.build();
    }
}
