package org.example.bookstoreapp.repository.shoppingCart;

import jakarta.validation.constraints.NotNull;
import org.example.bookstoreapp.model.ShoppingCart;
import org.example.bookstoreapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {

    Optional<ShoppingCart> findByUser(@NotNull User user);
}
