package com.shop.VenteLivreEnLigne.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

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
    @NotEmpty
    @Size(min = 4)
    private String title;
    @NotEmpty
    @Size(min = 4)
    private String imageLink;
    @NotNull
    @Min(value = 1)
    @Max(value = 1000000000)
    private Long quantity;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private Date writtenDate;
    @NotNull
    @Min(value = 1)
    private Double price;
    @ManyToOne(fetch = FetchType.EAGER)
    private Category category;
    @ManyToOne(fetch = FetchType.EAGER)
    private Writer writer;
    @Transient
    private UUID categoryId;
    @Transient
    private UUID writerId;

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

    public Book(UUID id, String title, String imageLink, Long quantity, Date writtenDate, Double price, Category category, Writer writer) {
        this.id = id;
        this.title = title;
        this.imageLink = imageLink;
        this.quantity = quantity;
        this.writtenDate = writtenDate;
        this.price = price;
        this.category = category;
        this.writer = writer;
    }
}
