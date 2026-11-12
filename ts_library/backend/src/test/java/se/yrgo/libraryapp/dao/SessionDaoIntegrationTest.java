package se.yrgo.libraryapp.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.UUID;
import javax.sql.DataSource;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import com.radcortez.flyway.test.annotation.H2;
import org.pac4j.core.exception.CredentialsException;
import se.yrgo.libraryapp.entities.UserId;

@Tag("integration")
@H2
public class SessionDaoIntegrationTest {

    private static DataSource ds;

    @BeforeAll
    static void initDataSource() {
        final JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:test");
        SessionDaoIntegrationTest.ds = ds;
    }

    @Test
    void createAndValidateSession() {
        SessionDao sessionDao = new SessionDao(ds);
        UserId userId = UserId.of(1);

        UUID sessionId = sessionDao.create(userId);
        assertThat(sessionId).isNotNull();

        UserId validatedUserId = sessionDao.validate(sessionId);
        assertThat(validatedUserId).isEqualTo(userId);

        sessionDao.delete(sessionId);
    }

    @Test
    void validateNonExistentSessionThrowsException() {
        SessionDao sessionDao = new SessionDao(ds);
        UUID randomSessionId = UUID.randomUUID();

        assertThatThrownBy(() -> sessionDao.validate(randomSessionId))
                .isInstanceOf(CredentialsException.class);
    }
}