package com.indicaAI.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.indicaAI.model.roles.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nickname;
    private String email;
    @JsonIgnore
    @Column(name = "password")
    private String senha;
    @Enumerated(EnumType.STRING)
    private UserRole role;
    @Column(name = "created_at")
    private LocalDateTime created_at;
    @Override
    public String toString() {
        return "Usuario{id=" + id + ", nome='" + nickname + "', email='" + email + "'}";
    }
    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role == UserRole.ADMIN) {
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }
    public String getNickName(){
        return nickname;
    }
    public UserRole getAuthority() {
        return role;
    }
    @JsonIgnore
    @Override
    public String getPassword() {
        return this.senha;
    }
    @JsonIgnore
    @Override
    public String getUsername() {
        return email;
    }
    @Override
    public boolean isAccountNonExpired() {
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