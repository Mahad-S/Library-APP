package se.yrgo.libraryapp.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import io.jooby.Context;
import io.jooby.Cookie;
import se.yrgo.libraryapp.dao.SessionDao;

class LogoutControllerTest {

    private SessionDao sessionDao;
    private LogoutController logoutController;
    private Context context;

    @BeforeEach
    void setUp() {
        sessionDao = mock(SessionDao.class);
        context = mock(Context.class);
        logoutController = new LogoutController(sessionDao);
    }

    @Test
    void testLogoutWithNullSession() {
        logoutController.logout(context, null);

        verify(sessionDao, never()).delete(any());
        verify(context, never()).setResponseCookie(any(Cookie.class));
    }

    @Test
    void testLogoutWithValidSession() {
        UUID sessionUuid = UUID.randomUUID();
        String sessionCookie = sessionUuid.toString();

        logoutController.logout(context, sessionCookie);

        verify(sessionDao).delete(sessionUuid);
        verify(context).setResponseCookie(any(Cookie.class));
    }

    @Test
    void testLogoutWithInvalidSessionFormat() {
        String invalidCookie = "not-a-valid-uuid";

        logoutController.logout(context, invalidCookie);

        verify(sessionDao, never()).delete(any());
        verify(context).setResponseCookie(any(Cookie.class));
    }
}