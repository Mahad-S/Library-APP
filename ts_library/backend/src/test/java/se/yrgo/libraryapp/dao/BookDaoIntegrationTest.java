package se.yrgo.libraryapp.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.sql.DataSource;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import com.radcortez.flyway.test.annotation.H2;
import se.yrgo.libraryapp.entities.Book;
import se.yrgo.libraryapp.entities.BookEdition;
import se.yrgo.libraryapp.entities.BookId;
import se.yrgo.libraryapp.entities.BookLoan;
import se.yrgo.libraryapp.entities.UserId;

@Tag("integration")
@H2
public class BookDaoIntegrationTest {

    private static DataSource ds;

    @BeforeAll
    static void initDataSource() {
        final JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:test");
        BookDaoIntegrationTest.ds = ds;
    }

    @Test
    void getBookById() {
        BookDao bookDao = new BookDao(ds);
        
        Optional<Book> maybeBook = bookDao.get(BookId.of(1));

        assertThat(maybeBook).isPresent();
    }

    @Test
    void findBooksByTitle() {
        BookDao bookDao = new BookDao(ds);

        Set<BookEdition> results = bookDao.find(null, "Java", null);

        assertThat(results).isNotNull();
    }

    @Test
    void getLoansForUser() {
        BookDao bookDao = new BookDao(ds);

        List<BookLoan> loans = bookDao.loans(UserId.of(1));

        assertThat(loans).isNotNull();
    }

    @Test
    void returnBook() {
        BookDao bookDao = new BookDao(ds);

        boolean success = bookDao.returnBook(BookId.of(1));

        assertThat(success).isIn(true, false);
    }
}