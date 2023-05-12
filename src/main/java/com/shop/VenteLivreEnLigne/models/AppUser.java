package com.shop.VenteLivreEnLigne.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
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
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Command> commands;
    @NotNull
    private Integer phoneNumber;
    @Transient
    private String role;
    private Boolean locked;
    private Boolean isMale;
    @Column(length = 3000)
    @NotNull
    private String profileImage;

    public AppUser(UUID id, String username, String password, String email, String confirmPassword, List<AppRole> roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.confirmPassword = confirmPassword;
        this.roles = roles;
        commands = new ArrayList<>();
    }
}
