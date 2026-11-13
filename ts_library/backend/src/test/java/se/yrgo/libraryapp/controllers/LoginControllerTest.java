package se.yrgo.libraryapp.controllers;

import io.jooby.Context;
import io.jooby.Cookie;
import io.jooby.StatusCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.Arrays;
import se.yrgo.libraryapp.dao.RoleDao;
import se.yrgo.libraryapp.dao.SessionDao;
import se.yrgo.libraryapp.entities.Role;
import se.yrgo.libraryapp.entities.UserId;
import se.yrgo.libraryapp.entities.forms.LoginData;
import se.yrgo.libraryapp.services.UserService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LoginControllerTest {

    private UserService userService;
    private RoleDao roleDao;
    private SessionDao sessionDao;
    private LoginController loginController;
    private Context context;
    private LoginData loginData;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        roleDao = mock(RoleDao.class);
        sessionDao = mock(SessionDao.class);
        context = mock(Context.class);
        loginData = mock(LoginData.class);

        loginController = new LoginController(userService, roleDao, sessionDao);
    }

    @Test
    void testSuccessfulLogin() {
        String sessionCookie = null;
        UserId dummyUserId = new UserId(1);
        UUID dummySessionUuid = UUID.randomUUID();
        List<Role> expectedRoles = Arrays.asList(Role.USER);

        when(loginData.getUsername()).thenReturn("testuser");
        when(loginData.getPassword()).thenReturn("password123");
        when(userService.validate("testuser", "password123")).thenReturn(Optional.of(dummyUserId));
        when(sessionDao.create(dummyUserId)).thenReturn(dummySessionUuid);
        when(roleDao.get(dummyUserId)).thenReturn(expectedRoles);

        List<Role> result = loginController.login(context, sessionCookie, loginData);

        assertThat(result).isEqualTo(expectedRoles);
        verify(context).setResponseCookie(any(Cookie.class));
        verify(sessionDao).create(dummyUserId);
        verify(roleDao).get(dummyUserId);
    }

    @Test
    void testLoginWithInvalidCredentials() {
        String sessionCookie = null;

        when(loginData.getUsername()).thenReturn("wronguser");
        when(loginData.getPassword()).thenReturn("wrongpass");

        when(userService.validate("wronguser", "wrongpass")).thenReturn(Optional.empty());

        List<Role> result = loginController.login(context, sessionCookie, loginData);

        assertThat(result).isEmpty();
        verify(context).setResponseCode(StatusCode.UNAUTHORIZED);
        verify(sessionDao, never()).create(any());
    }

    @Test
    void testIsLoggedInWithValidSession() {
        String validSessionCookie = UUID.randomUUID().toString();
        UserId dummyUserId = new UserId(1);
        List<Role> expectedRoles = Arrays.asList(Role.USER);

        when(sessionDao.validate(UUID.fromString(validSessionCookie))).thenReturn(dummyUserId);
        when(roleDao.get(dummyUserId)).thenReturn(expectedRoles);

        List<Role> result = loginController.isLoggedIn(validSessionCookie);

        assertThat(result).isEqualTo(expectedRoles);
        verify(sessionDao).validate(any(UUID.class));
        verify(roleDao).get(dummyUserId);
    }

    @Test
    void testIsLoggedInWithNullOrInvalidSession() {
        
        String invalidSessionCookie = null;

        List<Role> result = loginController.isLoggedIn(invalidSessionCookie);

        assertThat(result).isEmpty();
    }
}