package userTest;

import org.junit.jupiter.api.Test;
import ulb.infof307.g9.model.User;

import static org.junit.jupiter.api.Assertions.*;

class TestUser {

    @Test
    void getUsername() {
        User user = new User("test");
        assertEquals("test", user.getUsername());
    }

    @Test
    void getPassword() {
        User user = new User("test", "123");
        assertEquals("123", user.getPassword());
    }

    @Test
    void setPassword() {
        User user = new User("test");
        user.setPassword("1234");
        assertEquals("1234", user.getPassword());
    }

    @Test
    void equals_sameUser() {
        User user1 = new User("test", "123");
        User user2 = new User("test", "123");
        assertEquals(user1, user2);
    }

    @Test
    void equals_differentUser() {
        User user1 = new User("test", "123");
        User user2 = new User("test2", "123");
        assertNotEquals(user1, user2);
    }

    @Test
    void equals_differentObject() {
        User user = new User("test", "123");
        String otherObject = "test";
        assertNotEquals(user, otherObject);
    }

    @Test
    void equals_sameObject() {
        User user = new User("test", "123");
        assertEquals(user, user);
    }

    @Test
    void equals_nullObject() {
        User user = new User("test", "123");
        assertNotEquals(null, user);
    }
}