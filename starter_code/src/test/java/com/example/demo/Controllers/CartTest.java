package com.example.demo.Controllers;

import com.example.demo.TestUtils;
import com.example.demo.controllers.CartController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CartTest {
    private CartController cartController;

    private final UserRepository userRepository = mock(UserRepository.class);

    private final CartRepository cartRepository = mock(CartRepository.class);

    private final ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void Setup(){
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);

        User user = new User();
        Cart cart = new Cart();
        user.setId(1);
        user.setUsername("test");
        user.setPassword("testpass");
        user.setCart(cart);
        when(userRepository.findByUsername("test")).thenReturn(user);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(CreateItem()));
    }

    @Test
    public void add_to_cart(){
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("test");
        request.setItemId(1L);
        request.setQuantity(2);

        final ResponseEntity<Cart> response = cartController.addTocart(request);

        Assert.assertEquals(200,response.getStatusCodeValue());
        Assert.assertNotNull(response);
        Cart c = response.getBody();
        Assert.assertNotNull(c);

    }

    @Test
    public void remove_from_cart(){
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("test");
        request.setItemId(1L);
        request.setQuantity(3);

        ResponseEntity<Cart> response = cartController.addTocart(request);

        Assert.assertNotNull(response);

        response = cartController.removeFromcart(request);
/*Cart c = response.getBody();
        Assertions.assertNull(c);*/


        request = new ModifyCartRequest();
        request.setItemId(1L);
        request.setQuantity(2);
        request.setUsername("test");
        response = cartController.removeFromcart(request);

        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());
        Cart c = response.getBody();
        Assert.assertNotNull(c);
        Assert.assertEquals(BigDecimal.valueOf(31.98), c.getTotal().abs());


    }
    @Test
    public void add_to_cart_invalid_username(){
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("ZeyadMohamed");
        request.setItemId(1L);
        request.setQuantity(2);

        final ResponseEntity<Cart> response = cartController.addTocart(request);

        Assert.assertEquals(404,response.getStatusCodeValue());
    }

    public Item CreateItem(){
        Item item = new Item();
        item.setId(1L);
        item.setName("Speakers");
        BigDecimal price = BigDecimal.valueOf(15.99);
        item.setPrice(price);
        item.setDescription("A logitech Speaker");
        return item;
    }
}
