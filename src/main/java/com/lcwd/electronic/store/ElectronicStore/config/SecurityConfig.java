package com.lcwd.electronic.store.ElectronicStore.config;

import com.lcwd.electronic.store.ElectronicStore.helper.AppConstants;
import com.lcwd.electronic.store.ElectronicStore.security.JwtAuthenticationEntryPoint;
import com.lcwd.electronic.store.ElectronicStore.security.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletRequest;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

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

        // To Disable CORS in our Application..
//        security.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.disable());

        //  Globally Configuring CORS in our Project
        security.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(
                new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration corsConfiguration = new CorsConfiguration();
                        // This will allow CORS for single course
                        // corsConfiguration.addAllowedOrigin("http://localhost:5173");

                        // For multiple Sources
                        corsConfiguration.setAllowedOrigins(List.of("http://localhost:5173","http://localhost:5234"));

                        // This will allow all (CAUTION : Use this only in DEV env not PROD)
                        // corsConfiguration.setAllowedOrigins(List.of("*"));

                        // We can allow methods too that are allowed
                        corsConfiguration.setAllowedMethods(List.of("*"));

                        // We can allow Credentials too
                        // CAUTION:
                        //----------
                        // If humne allo credential ko true kardiya to hum setAllowedOrignins me "*"  anhi laga sakte
                        // ek ek karke btao ki kis kisko allow kiya hai...
                        corsConfiguration.setAllowCredentials(true);

                        // We can allow what Headers we want to allow or we can allow all
                        // ie. Headers like Authorisation etc will be allowed...
                        corsConfiguration.setAllowedHeaders(List.of("*"));

                        // We can also set the time for with the response data that came from the Pre-flight req
                        // to be in the Cache....
                        corsConfiguration.setMaxAge(3000L); // value is in seconds..

                        return corsConfiguration;
                    }
                }
        ));

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
