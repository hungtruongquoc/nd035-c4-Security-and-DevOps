package com.example.demo.Controllers;

import com.example.demo.TestUtils;
import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderTest {
    private OrderController orderController;

    private final UserRepository userRepository = mock(UserRepository.class);

    private final OrderRepository orderRepository = mock(OrderRepository.class);

    @Before
    public void Setup(){
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);

        User user = new User();
        user.setId(1);
        user.setUsername("test");
        user.setPassword("testpass");

        Cart cart = new Cart();
        cart.setItems(CreateItems());
        cart.setId(1L);
        cart.setUser(user);
        cart.setTotal(BigDecimal.valueOf(15.99));

        user.setCart(cart);
        when(userRepository.findByUsername("test")).thenReturn(user);

    }

    @Test
    public void submit_order_valid(){
        ResponseEntity<UserOrder> response = orderController.submit("test");
        Assert.assertNotNull(response);
        Assert.assertEquals(200,response.getStatusCodeValue());
        UserOrder userOrder = response.getBody();
        Assert.assertNotNull(userOrder);
    }

    @Test
    public void submit_order_invalid(){
        ResponseEntity<UserOrder> response = orderController.submit("hung");
        Assert.assertNotNull(response);
        Assert.assertEquals(404,response.getStatusCodeValue());
    }

    @Test
    public void get_order_history_valid(){
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("test");
        Assert.assertNotNull(response);
        Assert.assertEquals(200,response.getStatusCodeValue());
        List<UserOrder> userOrders = response.getBody();
        Assert.assertNotNull(userOrders);
    }

    @Test
    public void get_order_history_invalid(){
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("hung");
        Assert.assertNotNull(response);
        Assert.assertEquals(404,response.getStatusCodeValue());
    }

    public List<Item> CreateItems(){
        Item item = new Item();
        item.setId(1L);
        item.setName("Speakers");
        BigDecimal price = BigDecimal.valueOf(15.99);
        item.setPrice(price);
        item.setDescription("A logitech Speaker");
        List<Item> items = new ArrayList<>();
        items.add(item);
        return items;
    }
}
