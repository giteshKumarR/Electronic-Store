package com.lcwd.electronic.store.ElectronicStore.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "cart")
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id // here we will not use any generation type as we will generate the
    // Ids ourselves...
    private String userId;

    @Column(name = "user_status", nullable = false, length = 8)
    private String UserStatus;

    @Column(name = "user_name")
    private String name;

    @Column(name = "user_email", unique = true)
    private String userEmail;

    @Column(name = "user_password", length=100)
    private String userPassword;

    @Column(name = "user_gender", length = 1)
    private String userGender;

    @Column(name = "user_about", length = 1000)
    private String userAbout;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Role> roles = new ArrayList<>();

    @Column(name = "user_profile_image")
    private String userProfileImage;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("user") // this will prevent infinite recursion in JSON serialisation, and will prevent user details to be included in JSON.
    private Cart cart;

    @OneToMany(mappedBy = "user",
            fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE // means ki agar user remove hua to orders bhi remove ho jayenge
    )
    private List<Order> orderList;

    @Column(name = "user_created_on")
    private LocalDateTime createdOn;

    @Column(name = "user_updated_on")
    private LocalDateTime updatedOn;

    @PrePersist
    public void onPrePersist() {
        // This will run only when the entity will be first created
        this.createdOn = LocalDateTime.now();
    }

    @PreUpdate
    public void onPreUpdate() {
        this.updatedOn = LocalDateTime.now();
    }


    // User Details methods for applying Security in the project
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // IMPORTANT
        Set<SimpleGrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toSet());
        return authorities;
    }


    // These getPassword, getUsername methods are important as Spring Security will be used
    // to fetch the username and password for authentication..

    // This was already created by Lombok but we need to override this in order
    // to sue it with USerDetails in Spring Security..
    @Override
    public String getPassword() {
        return userPassword;
    }

    @Override
    public String getUsername() {
        // We are using Email as the username for our project..
        return this.getUserEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        // In future we will implement this method using DB.
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
