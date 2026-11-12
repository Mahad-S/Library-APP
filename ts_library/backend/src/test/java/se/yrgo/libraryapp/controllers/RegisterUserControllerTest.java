package se.yrgo.libraryapp.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.yrgo.libraryapp.dao.UserDao;
import se.yrgo.libraryapp.entities.forms.RegisterUserData;

class RegisterUserControllerTest {

    private UserDao userDao;
    private RegisterUserController registerUserController;
    private RegisterUserData registerUserData;

    @BeforeEach
    void setUp() {
        userDao = mock(UserDao.class);
        registerUserData = mock(RegisterUserData.class);
        registerUserController = new RegisterUserController(userDao);
    }

    @Test
    void testRegisterSuccess() {
        when(registerUserData.getName()).thenReturn("validuser");
        when(registerUserData.getRealName()).thenReturn("Valid Name");
        when(registerUserData.getPassword()).thenReturn("secret123");

        when(userDao.register("validuser", "Valid Name", "secret123")).thenReturn(true);

        boolean result = registerUserController.register(registerUserData);

        assertThat(result).isTrue();
        verify(userDao).register("validuser", "Valid Name", "secret123");
    }

    @Test
    void testRegisterValidationFailure() {
        
        when(registerUserData.getName()).thenReturn("");
        when(registerUserData.getRealName()).thenReturn("Valid Name");

        boolean result = registerUserController.register(registerUserData);

        assertThat(result).isFalse();
        verify(userDao, never()).register(any(), any(), any());
    }

    @Test
    void testIsNameAvailable() {
        String name = "newuser";
        when(userDao.isNameAvailable(name)).thenReturn(true);

        boolean result = registerUserController.isNameAvailable(name);

        assertThat(result).isTrue();
        verify(userDao).isNameAvailable(name);
    }
}