package com.example.demo.Controllers;

import com.example.demo.TestUtils;
import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ItemTest {

    private ItemController itemController;

    private final ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void Setup(){
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);

        Item item = new Item();
        item.setId(1L);
        item.setName("Speakers");
        BigDecimal price = BigDecimal.valueOf(15.99);
        item.setPrice(price);
        item.setDescription("A logitech Speaker");
        when(itemRepository.findAll()).thenReturn(Collections.singletonList(item));
        when(itemRepository.findById(1L)).thenReturn(java.util.Optional.of(item));
        when(itemRepository.findByName("Speakers")).thenReturn(Collections.singletonList(item));

    }

    @Test
    public void get_all_items(){
        ResponseEntity<List<Item>> response = itemController.getItems();
        Assert.assertNotNull(response);
        Assert.assertEquals(200,response.getStatusCodeValue());

    }

    @Test
    public void get_item_by_id(){
        ResponseEntity<Item> response = itemController.getItemById(1L);
        Assert.assertNotNull(response);
        Assert.assertEquals(200,response.getStatusCodeValue());
        Item i = response.getBody();
        Assert.assertNotNull(i);
    }

    @Test
    public void get_item_by_name(){
        ResponseEntity<List<Item>> response = itemController.getItemsByName("Speakers");
        Assert.assertNotNull(response);
        Assert.assertEquals(200,response.getStatusCodeValue());
    }

    @Test
    public void get_invalid_item_id(){
        ResponseEntity<Item> response = itemController.getItemById(20L);
        Assert.assertNotNull(response);
        Assert.assertEquals(404,response.getStatusCodeValue());
    }

    @Test
    public void get_invalid_item_name(){
        ResponseEntity<List<Item>> response = itemController.getItemsByName("Mouse");
        Assert.assertNotNull(response);
        Assert.assertEquals(404,response.getStatusCodeValue());
    }
}
