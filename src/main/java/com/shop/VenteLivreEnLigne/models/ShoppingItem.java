package com.shop.VenteLivreEnLigne.models;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingItem implements Serializable {
    private Book book;
    @Min(1)
    @Max(99)
    private Long quantity;
}
