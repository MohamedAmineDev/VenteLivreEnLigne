package com.shop.VenteLivreEnLigne.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class ShoppingBasket implements Serializable {
    private List<ShoppingItem> shoppingItems;

    public ShoppingBasket() {
        shoppingItems = new ArrayList<>();
    }

    public boolean addBook(Book book, Long quantity) {
        if (shoppingItems == null) {
            shoppingItems = new ArrayList<>();
        }
        return shoppingItems.add(new ShoppingItem(book, quantity));
    }

    public boolean addBook(ShoppingItem shoppingItem) {
        if (shoppingItems == null) {
            shoppingItems = new ArrayList<>();
        }
        return shoppingItems.add(shoppingItem);
    }

    public boolean removeBook(int index) {
        try {
            shoppingItems.remove(index);
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }
}
