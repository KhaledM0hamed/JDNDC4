package com.example.demo.controller;

import com.example.demo.TestUtils;
import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;
import static org.junit.Assert.assertEquals;


import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class ItemControllerTest {

    private ItemController itemController;
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void init() {
        itemController = new ItemController();
        TestUtils.injectObject(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void get_all_items () {
        ResponseEntity<List<Item>> response = itemController.getItems();
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void get_item_by_name () {
        ResponseEntity<List<Item>> response = itemController.getItemsByName("item");
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void get_item_by_id() {
        ResponseEntity<Item> response = itemController.getItemById(1L);
        assertEquals(404, response.getStatusCodeValue());
    }
}
