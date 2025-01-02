package com.lcwd.electronic.store.ElectronicStore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity(debug = true) // debug = true for Dev purposes only.
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
        // Configure the urls
        security.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());
        security.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.disable());

        security.authorizeHttpRequests( request -> request

                // User Management APIs
                // Anyone can create a User (PUBLIC)
                .requestMatchers(HttpMethod.POST,"/v1/user-api/create-user").permitAll()
                .requestMatchers(HttpMethod.GET, "/v1/user-api/get-by-id/**").hasAnyRole("ADMIN","NORMAL")
//                .requestMatchers("/v1/user-api/get-all-users/**").permitAll() // test
                .requestMatchers("/v1/user-api/**").hasRole("ADMIN")


                // Product APIs
                .requestMatchers(HttpMethod.GET, "/v1/product-api/**").permitAll() // Kind of PUBLIC API
                .requestMatchers("/v1/product-api/**").hasRole("ADMIN")

                // Category APIs
                .requestMatchers(HttpMethod.GET, "/v1/category-api/**").permitAll()
                .requestMatchers("/v1/category-api/**").hasRole("ADMIN")

                // Cart APIs
                .requestMatchers("/v1/cart-api/**").hasRole("NORMAL")

                // Order APIs
                .requestMatchers("/v1/order-api/create-order/**").hasRole("NORMAL")
                .requestMatchers("/v1/order-api/get-user-orders/**").hasRole("NORMAL")
                .requestMatchers(HttpMethod.PUT, "/v1/order-api/update-order/*/user").hasAnyRole("NORMAL","ADMIN")
                .requestMatchers("/v1/order-api/**").hasRole("ADMIN")

                // Any other request
                .anyRequest().authenticated()
        );

        // Type of the security used
        security.httpBasic(Customizer.withDefaults());
        return security.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
