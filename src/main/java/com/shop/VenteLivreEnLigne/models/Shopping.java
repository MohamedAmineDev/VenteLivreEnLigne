package com.shop.VenteLivreEnLigne.models;

import java.util.HashMap;
import java.util.UUID;

public class Shopping {
    public static HashMap<UUID, ShoppingBasket> shoppingBasketHashMap = new HashMap<>();

    public Shopping() {
        shoppingBasketHashMap = new HashMap<>();
    }
}
