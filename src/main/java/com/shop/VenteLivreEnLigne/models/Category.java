package com.shop.VenteLivreEnLigne.models;

import jakarta.persistence.*;
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
public class Category implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String label;
    private String imageLink;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "category")
    private List<Book> books;

    public Category(UUID id) {
        this.id = id;
    }
}
