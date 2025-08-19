package org.example.bookstoreapp.service;

import org.example.bookstoreapp.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartService extends JpaRepository<ShoppingCart, Long> {

    Object getShoppingCartById(Long id);
}
