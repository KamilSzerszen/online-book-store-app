package org.example.bookstoreapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.bookstoreapp.dto.shoppingcart.ShoppingCartDto;
import org.example.bookstoreapp.dto.cartitem.CartItemDto;
import org.example.bookstoreapp.dto.cartitem.CartItemRequestDto;
import org.example.bookstoreapp.dto.cartitem.CartItemUpdateRequestDto;
import org.example.bookstoreapp.model.ShoppingCart;
import org.example.bookstoreapp.model.User;
import org.example.bookstoreapp.service.ShoppingCartService;
import org.example.bookstoreapp.service.UserService;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.sql.DataSource;
import java.sql.Connection;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ShoppingCartControllerTest {

    protected static MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private ShoppingCartService shoppingCartService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    void beforeAll(@Autowired WebApplicationContext applicationContext,
                   @Autowired DataSource dataSource) throws Exception {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection, new ClassPathResource("database/clear-all.sql"));
            ScriptUtils.executeSqlScript(connection, new ClassPathResource("database/add-cart-items.sql"));
        }
    }

    @AfterAll
    void afterAll(@Autowired DataSource dataSource) throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection, new ClassPathResource("database/clear-all.sql"));
        }
    }

    @Test
    @DisplayName("""
            Should return shopping cart with existing items
            """)
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    void getShoppingCart_existingItems_returnsShoppingCartDto() throws Exception {
        ShoppingCartDto shoppingCartDto = new ShoppingCartDto();
        shoppingCartDto.setId(1L);
        shoppingCartDto.setUserId(1L);

        CartItemDto cartItemDto = new CartItemDto(1L, 1L, "Book Title", 2);
        shoppingCartDto.getCartItems().add(cartItemDto);

        Mockito.when(shoppingCartService.getShoppingCartByUserId())
                .thenReturn(shoppingCartDto);

        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.get("/cart")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        ShoppingCartDto resultDto = objectMapper
                .readValue(result.getResponse().getContentAsString(), ShoppingCartDto.class);

        Assertions.assertNotNull(resultDto);
        Assertions.assertEquals(shoppingCartDto.getUserId(), resultDto.getUserId());
        Assertions.assertFalse(resultDto.getCartItems().isEmpty());
        Assertions.assertEquals(cartItemDto.bookId(), resultDto.getCartItems().iterator().next().bookId());
    }

    @Test
    @DisplayName("""
            Should add item to shopping cart
            """)
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    void addItemToCart_validRequest_returnsCartItemDto() throws Exception {
        CartItemRequestDto requestDto = new CartItemRequestDto(1L, 3);

        User currentUser = new User();
        currentUser.setId(1L);
        currentUser.setEmail("user@example.com");
        currentUser.setFirstName("User");
        currentUser.setLastName("Test");
        currentUser.setPassword("password");

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(1L);
        shoppingCart.setUser(currentUser);

        CartItemDto expected = new CartItemDto(1L, 1L, "Book Title", 3);

        Mockito.when(userService.getCurrentUser()).thenReturn(currentUser);
        Mockito.when(shoppingCartService.addItemToCart(requestDto)).thenReturn(expected);

        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.post("/cart")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        CartItemDto result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CartItemDto.class);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(expected.id(), result.id());
        Assertions.assertEquals(expected.bookId(), result.bookId());
        Assertions.assertEquals(expected.bookTitle(), result.bookTitle());
        Assertions.assertEquals(expected.quantity(), result.quantity());
    }

    @Test
    @DisplayName("""
            Should update item quantity in shopping cart
            """)
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    void updateItemQuantity_itemExists_returnsCartItemDto() throws Exception {
        Long cartItemId = 1L;
        CartItemUpdateRequestDto request = new CartItemUpdateRequestDto(5);

        CartItemDto cartItemDto = new CartItemDto(cartItemId, 1L, "Book Title", request.quantity());
        Mockito.when(shoppingCartService.updateItemQuantity(cartItemId, request))
                .thenReturn(cartItemDto);

        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.put("/cart/cart-items/{id}", cartItemId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        CartItemDto resultDto = objectMapper.readValue(result.getResponse().getContentAsString(), CartItemDto.class);

        Assertions.assertNotNull(resultDto);
        Assertions.assertEquals(cartItemDto.id(), resultDto.id());
        Assertions.assertEquals(cartItemDto.quantity(), resultDto.quantity());
    }

    @Test
    @DisplayName("""
            Should delete item from shopping cart
            """)
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    void deleteItemFromCart_validId_noContent() throws Exception {
        Long cartItemId = 1L;

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/cart/cart-items/{id}", cartItemId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
