package org.example.bookstoreapp.repository;

import org.example.bookstoreapp.model.ShoppingCart;
import org.example.bookstoreapp.repository.shoppingcart.ShoppingCartRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ShoppingCartRepositoryTest {

    @Autowired
    ShoppingCartRepository shoppingCartRepository;

    @Test
    @DisplayName("""
            Should find shopping cart by user id
            """)
    @Sql(
            scripts = "classpath:database/add-shopping-cart-and-user-relation.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/remove-shopping-cart-ad-user-relation.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void findByUserId_userAndShoppingCartExist_returnShoppingCart() {
        Long userId = 1L;

        Optional<ShoppingCart> result = shoppingCartRepository.findByUserId(userId);

        Assertions.assertTrue(result.isPresent());

        ShoppingCart cart = result.get();
        Assertions.assertEquals("user@user.com", cart.getUser().getEmail());
        Assertions.assertEquals("user", cart.getUser().getFirstName());
        Assertions.assertEquals("user", cart.getUser().getLastName());
        Assertions.assertEquals("password123", cart.getUser().getPassword()); }
}
