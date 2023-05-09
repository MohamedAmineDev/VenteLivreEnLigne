package com.shop.VenteLivreEnLigne.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(unique = true)
    @NotEmpty
    @NotNull
    private String username;
    @NotEmpty
    @NotNull
    private String password;
    @Column(unique = true)
    private String email;
    @Transient
    @NotEmpty
    @NotNull
    private String confirmPassword;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<AppRole> roles;
}
