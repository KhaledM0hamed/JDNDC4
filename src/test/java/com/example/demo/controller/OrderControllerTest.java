package com.example.demo.controller;

import com.example.demo.TestUtils;
import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import static org.junit.Assert.assertEquals;

import java.util.List;

import static org.mockito.Mockito.mock;

public class OrderControllerTest {

    private OrderController orderController;
    private OrderRepository orderRepository = mock(OrderRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);

    @Before
    public void init() {
        orderController = new OrderController();
        TestUtils.injectObject(orderController, "orderRepository", orderRepository);
        TestUtils.injectObject(orderController, "userRepository", userRepository);
    }

    @Test
    public void verify_user_order_if_user_is_not_found () {
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("khaled");
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void submit_user_order_if_user_is_not_found () {
        ResponseEntity<UserOrder> response = orderController.submit("khaled");
        assertEquals(404, response.getStatusCodeValue());
    }
}
