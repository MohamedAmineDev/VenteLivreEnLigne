package com.shop.VenteLivreEnLigne.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Genre implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @NotNull
    private String label;
    @NotNull
    @Column(length = 8000)
    private String imageLink;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "genre")
    private List<Book> books;

    public Genre(UUID id) {
        this.id = id;
    }
}
