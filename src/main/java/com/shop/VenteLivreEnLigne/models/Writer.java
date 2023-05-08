package com.shop.VenteLivreEnLigne.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Writer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String penName;
    @Column(length = 3000)
    @NotEmpty
    private String imageLink;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "writer")
    private List<Book> books;

    public Writer(UUID id, String name, String penName, String imageLink) {
        this.id = id;
        this.name = name;
        this.penName = penName;
        this.imageLink = imageLink;
        books = new ArrayList<>();
    }

    public Writer(UUID id) {
        this.id = id;
    }
}
