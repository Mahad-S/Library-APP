package se.yrgo.libraryapp.dao;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;
import se.yrgo.libraryapp.entities.*;

class BookDaoTest {

    private BookDao bookDao;
    private JdbcDataSource ds;

    @BeforeEach
    void setUp() {
        // Skapa H2 in-memory databas
        ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        ds.setUser("sa");
        ds.setPassword("");

        // Nollställ och kör Flyway-migrationer före varje test
        Flyway flyway = Flyway.configure().dataSource(ds).load();
        flyway.clean();
        flyway.migrate();

        bookDao = new BookDao(ds);
    }

    @Test
    void testGetExistingBook() {
        // Förutsätter att Flyway har lagt in testböcker via migrationerna (t.ex. V1__init.sql)
        Optional<Book> book = bookDao.get(BookId.of(1));

        assertThat(book).isPresent();
        assertThat(book.get().getBookEdition().getTitle()).isNotBlank();
    }

    @Test
    void testFindBooksByTitle() {
        // Sök efter en bok baserat på titel (beroende på vad som finns i din testdatabas)
        Set<BookEdition> results = bookDao.find(null, "Java", null);

        assertThat(results).isNotEmpty();
    }

    @Test
    void testReturnBook() {
        // Testa att återlämna en bok (eller ta bort ett lån)
        boolean success = bookDao.returnBook(BookId.of(1));
        
        // Beroende på om bok 1 var utlånad i testdatabasen eller inte
        // Kan verifieras mot förväntat resultat
    }
}