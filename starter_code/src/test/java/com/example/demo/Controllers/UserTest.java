package com.example.demo.Controllers;

import com.example.demo.TestUtils;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTest {
    private UserController userController;

    private final UserRepository userRepository = mock(UserRepository.class);

    private final CartRepository cartRepository = mock(CartRepository.class);

    private final BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setup(){
        userController = new UserController();
        TestUtils.injectObjects(userController,"userRepository",userRepository);
        TestUtils.injectObjects(userController,"cartRepository",cartRepository);
        TestUtils.injectObjects(userController,"bCryptPasswordEncoder",encoder);

        User user = new User();
        Cart cart = new Cart();
        user.setId(1L);
        user.setUsername("test");
        user.setPassword("testpass");
        user.setCart(cart);
        when(userRepository.findByUsername("test")).thenReturn(user);
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));
    }

    @Test
    public void createUserHappyPath() throws Exception{
        when(encoder.encode("testpass")).thenReturn("This is hashed");
        CreateUserRequest req = new CreateUserRequest();
        req.setUsername("test");
        req.setPassword("testpass");
        req.setConfirmPassword("testpass");

        final ResponseEntity<User> response = userController.createUser(req);
        Assert.assertNotNull(response);
        Assert.assertEquals(200,response.getStatusCodeValue());

        User u = response.getBody();
        Assert.assertNotNull(u);
        Assert.assertEquals(0,u.getId());
        Assert.assertEquals("test",u.getUsername());
        Assert.assertEquals("This is hashed",u.getPassword());

    }

    @Test
    public void Find_user_by_username(){
        ResponseEntity<User> response = userController.findByUserName("test");
        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());
        User generatedUser = response.getBody();
        Assert.assertNotNull(generatedUser);
        Assert.assertEquals("test", generatedUser.getUsername());
    }

    @Test
    public void Find_user_by_Id(){
        ResponseEntity<User> response = userController.findById(1L);
        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());
        User generatedUser = response.getBody();
        Assert.assertNotNull(generatedUser);
        Assert.assertEquals(1L, generatedUser.getId());
    }

    @Test
    public void Find_user_by_invalid_username(){
        ResponseEntity<User> response = userController.findByUserName("Zeyad");
        Assert.assertNotNull(response);
        Assert.assertEquals(404, response.getStatusCodeValue());

    }

    @Test
    public void Find_user_by_invalid_Id(){
        ResponseEntity<User> response = userController.findById(10L);
        Assert.assertNotNull(response);
        Assert.assertEquals(404, response.getStatusCodeValue());

    }
}
