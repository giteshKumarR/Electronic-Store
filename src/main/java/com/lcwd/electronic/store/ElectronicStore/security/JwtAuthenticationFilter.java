package com.lcwd.electronic.store.ElectronicStore.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // This will run before accessing the API for verifying JWT token
        // in the request header.

        // Get the token from request (we can use JAVA stream Api too (Do it))
        String requestHeader = request.getHeader("Authorization");
        logger.info("Header : {}", requestHeader);

        String username = null;
        String token = null;
        if(requestHeader != null && requestHeader.startsWith("Bearer")) {
            // Menas Token Bearer se start hora hai to matlab Token mila hai
            token = requestHeader.substring(7);

            try {
                username = jwtHelper.getUsernameFromToken(token);
                logger.info("Username from Token : {}", username);
                } catch (ExpiredJwtException e) {
                    throw new JwtException("JWT token has expired : " + e.getMessage());
                } catch (UnsupportedJwtException e) {
                    throw new JwtException("Unsupported JWT token : " + e.getMessage());
                } catch (MalformedJwtException e) {
                    throw new JwtException("Invalid JWT token : " + e.getMessage());
                } catch (IllegalArgumentException e) {
                    throw new JwtException("JWT claims string is empty : "+e.getMessage());
                } catch( Exception ex) {
                    ex.printStackTrace();
                }

        } else {
            logger.info("Invalid Header !! Header is not starting with Bearer!!");
        }

        // Now we check the username
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // If user's Authentication is not present in the Security Context then we will add it in the
            // context so that when API is called Authentication will already be present in the request and
            // context and data can we retrieved.
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Validate the Token
            if(jwtHelper.validateToken(token, userDetails)) {
                // We will set the authentication in the security Context
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,  // For now, we are putting credentials as null.
                        userDetails.getAuthorities()
                );

                // We need to set the details of the request coming in the Authentication object
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // We will now set the authentication in the context
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }
}
