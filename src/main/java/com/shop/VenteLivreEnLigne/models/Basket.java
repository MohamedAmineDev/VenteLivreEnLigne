package com.shop.VenteLivreEnLigne.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Basket {
    public static HashMap<UUID, List<BasketItem>> storages = new HashMap<>();

    public static void addItemToTheBasket(UUID userId, BasketItem basketItem) {
        int index = storages.get(userId).indexOf(basketItem);
        if (index > -1) {
            storages.get(userId).get(index).setQuantity(storages.get(userId).get(index).getQuantity() + basketItem.getQuantity());

        } else {
            storages.get(userId).add(basketItem);
        }

    }

    public static void addUser(UUID userid) {
        storages.put(userid, new ArrayList<>());
    }

    public static void deleteBook(UUID userId, UUID bookId) {
        int index = storages.get(userId).indexOf(new BasketItem(bookId, 0l));
        if (index > -1) {
            storages.get(userId).remove(index);
        }
    }
}
