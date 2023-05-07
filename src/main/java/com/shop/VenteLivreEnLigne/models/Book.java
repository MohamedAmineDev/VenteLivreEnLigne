package com.shop.VenteLivreEnLigne.models;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Book implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String title;
    private String imageLink;
    private Long quantity;
    private Date writtenDate;
    private Double price;
    @ManyToOne(fetch = FetchType.EAGER)
    private Category category;
    @ManyToOne(fetch = FetchType.EAGER)
    private Writer writer;

    public Book(UUID id, String title, String imageLink, Long quantity, Date writtenDate, Double price, UUID categorieId, UUID writerId) {
        this.id = id;
        this.title = title;
        this.imageLink = imageLink;
        this.quantity = quantity;
        this.writtenDate = writtenDate;
        this.price = price;
        category = null;
        writer = new Writer(writerId, null, null, null, null);
    }
}
