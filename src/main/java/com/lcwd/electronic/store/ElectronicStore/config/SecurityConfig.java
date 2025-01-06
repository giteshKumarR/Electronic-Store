package com.lcwd.electronic.store.ElectronicStore.config;

import com.lcwd.electronic.store.ElectronicStore.helper.AppConstants;
import com.lcwd.electronic.store.ElectronicStore.security.JwtAuthenticationEntryPoint;
import com.lcwd.electronic.store.ElectronicStore.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity(debug = true) // debug = true for Dev purposes only.
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private JwtAuthenticationEntryPoint entryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
        // Configure the urls
        security.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());
        security.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.disable());

        security.authorizeHttpRequests( request -> request

                // User Management APIs

                // JWT endpoint
                .requestMatchers(HttpMethod.POST, "/v1/auth-api/generate-token").permitAll()

                // Anyone can create a User (PUBLIC)
                .requestMatchers(HttpMethod.POST,"/v1/user-api/create-user").permitAll()
                .requestMatchers(HttpMethod.GET, "/v1/user-api/get-by-id/**").hasAnyRole(AppConstants.ROLE_ADMIN,AppConstants.ROLE_NORMAL)
                .requestMatchers("/v1/user-api/get-all-users/**").permitAll() // test
                .requestMatchers("/v1/user-api/**").hasRole(AppConstants.ROLE_ADMIN)


                // Product APIs
                .requestMatchers(HttpMethod.GET, "/v1/product-api/**").permitAll() // Kind of PUBLIC API
                .requestMatchers("/v1/product-api/**").hasRole(AppConstants.ROLE_ADMIN)

                // Category APIs
                .requestMatchers(HttpMethod.GET, "/v1/category-api/**").permitAll()
                .requestMatchers("/v1/category-api/**").hasRole(AppConstants.ROLE_ADMIN)

                // Cart APIs
                .requestMatchers("/v1/cart-api/**").hasRole("NORMAL")

                // Order APIs
                .requestMatchers("/v1/order-api/create-order/**").hasRole(AppConstants.ROLE_NORMAL)
                .requestMatchers("/v1/order-api/get-user-orders/**").hasRole(AppConstants.ROLE_NORMAL)
                .requestMatchers(HttpMethod.PUT, "/v1/order-api/update-order/*/user").hasAnyRole(AppConstants.ROLE_NORMAL,AppConstants.ROLE_ADMIN)
                .requestMatchers("/v1/order-api/**").hasRole(AppConstants.ROLE_ADMIN)

                // Any other request
                .anyRequest().authenticated()
        );

        // Type of the security used
        // security.httpBasic(Customizer.withDefaults());

        // CONFIGURING JWT
        // ========================

        // Configuring the entry point when Exception is occurred..
        security.exceptionHandling(ex -> ex.authenticationEntryPoint(entryPoint));


        // We will go the session as STATELESS policy so that our JWT logic works Stateless
        security.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));


        // We will now configure the Filter...
        // Means ki hamara filter jo humne banaya for JWT vo UsernamePasswordAuthFilter k
        // execute hone se pehele he execute ho jayga..
        security.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return security.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    // We made a BEAN for getting the Authentication manager at runtime..

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
        return builder.getAuthenticationManager();
    }
}
