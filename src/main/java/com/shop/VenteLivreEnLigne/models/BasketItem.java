package com.shop.VenteLivreEnLigne.models;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class BasketItem implements Serializable {
    @EqualsAndHashCode.Include
    private UUID bookId;
    @NotNull
    private Long quantity;
    private String bookTitle;
    private String bookImage;
    private Double price;

    public BasketItem(UUID bookId, Long quantity) {
        this.bookId = bookId;
        this.quantity = quantity;
    }

}
