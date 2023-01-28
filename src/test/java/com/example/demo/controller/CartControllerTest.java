package com.example.demo.controller;

import com.example.demo.TestUtils;
import com.example.demo.controllers.CartController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {
    private CartController cartController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void init(){
        cartController = new CartController();
        TestUtils.injectObject(cartController, "userRepository", userRepository);
        TestUtils.injectObject(cartController, "cartRepository", cartRepository);
        TestUtils.injectObject(cartController, "itemRepository", itemRepository);
    }

    @Test
    public void add_to_cart_happy_path(){
        User user = createUser();
        when(userRepository.findByUsername("cartUser")).thenReturn(user);

        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername(user.getUsername());
        request.setItemId(0);
        request.setQuantity(3);

        when(itemRepository.findById(request.getItemId())).thenReturn(Optional.of(getItem()));

        ResponseEntity<Cart> response = cartController.addTocart(request);
        Cart savedCart = response.getBody();
        assertEquals(3, savedCart.getItems().size());
        assertNotNull(response);
    }

    @Test
    public void remove_from_cart_happy_path(){
        User user = createUser();
        when(userRepository.findByUsername("cartUser")).thenReturn(user);

        user.setCart(getUserCart((long)0, 5));

        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername(user.getUsername());
        request.setItemId(0);
        request.setQuantity(2);

        when(itemRepository.findById(request.getItemId())).thenReturn(Optional.of(getItem()));

        ResponseEntity<Cart> response = cartController.removeFromcart(request);

        Cart savedCart = response.getBody();
        assertNotNull(response);
        assertEquals(3, savedCart.getItems().size());

    }

    private User createUser(){
        User user = new User();
        user.setUsername("cartUser");
        user.setPassword("Password");
        user.setCart(new Cart());
        return user;
    }

    private Item getItem(){
        Item item = new Item();
        item.setId((long)0);
        item.setName("item test");
        item.setDescription("item test desc");
        item.setPrice(new BigDecimal(10.00));
        return item;
    }

    private Cart getUserCart(long itemId, int quantity){
        Cart cart = new Cart();
        IntStream.range(0, quantity)
                .forEach(i ->  cart.addItem(getItem()));
        return cart;
    }

}
