package com.example.demo.controller;

import com.example.demo.TestUtils;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObject(userController, "userRepository", userRepository);
        TestUtils.injectObject(userController, "cartRepository", cartRepository);
        TestUtils.injectObject(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);
    }

    @Test
    public void create_user_happy_path() throws Exception {
        when(bCryptPasswordEncoder.encode("123456789"))
                .thenReturn("this is hashed");
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("test");
        createUserRequest.setPassword("123456789");
        createUserRequest.setConfirmPassword("123456789");

        final ResponseEntity<User> response = userController.createUser(createUserRequest);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("test", user.getUsername());
        assertEquals("this is hashed", user.getPassword());
    }

    @Test
    public void create_user_error_path(){
        when(bCryptPasswordEncoder.encode("123456789")).thenReturn("thisIsHashed");
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("testUser");
        createUserRequest.setPassword("123456789");

        ResponseEntity<User> response =  userController.createUser(createUserRequest);
        assertEquals(400, response.getStatusCodeValue());
        assertNotNull(response);

    }

    @Test
    public void get_username_happy_path(){
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("testUser");
        createUserRequest.setPassword("123456789");
        createUserRequest.setConfirmPassword("123456789");

        ResponseEntity<User> response1 =  userController.createUser(createUserRequest);

        User testUser = response1.getBody();

        when(userRepository.findByUsername("testUser")).thenReturn(testUser);

        String username = "testUser";
        ResponseEntity<User> response2 = userController.findByUserName(username);
        assertNotNull(response2);
        User user = response2.getBody();
        assertNotNull(user);
        assertEquals(testUser, user);

        ResponseEntity<User> response3 = userController.findById(2L);
        assertNotNull(response3);
        user = response3.getBody();
        assertNull(user);
    }
}
