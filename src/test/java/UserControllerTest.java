import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.homeTheatre.controller.UserController;
import ru.yandex.practicum.homeTheatre.model.User;

import java.time.LocalDate;

class UserControllerTest {

    @Test
    void testValidateUser_EmptyEmail() {
        User user = new User();
        user.setLogin("testLogin");
        user.setBirthday(LocalDate.now());
        assertFalse(new UserController().validateUser(user));
    }

    @Test
    void testValidateUser_InvalidEmail() {
        User user = new User();
        user.setEmail("testemail");
        user.setLogin("testLogin");
        user.setBirthday(LocalDate.now());
        assertFalse(new UserController().validateUser(user));
    }

    @Test
    void testValidateUser_EmptyLogin() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setBirthday(LocalDate.now());
        assertFalse(new UserController().validateUser(user));
    }

    @Test
    void testValidateUser_LoginWithSpaces() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("test Login");
        user.setBirthday(LocalDate.now());
        assertFalse(new UserController().validateUser(user));
    }

    @Test
    void testValidateUser_FutureBirthday() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("testLogin");
        user.setBirthday(LocalDate.of(2099, 1, 1));
        assertFalse(new UserController().validateUser(user));
    }

    @Test
    void testValidateUser_ValidUser() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("testLogin");
        user.setBirthday(LocalDate.now().minusYears(18));
        assertTrue(new UserController().validateUser(user));
    }
}