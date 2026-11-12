package se.yrgo.libraryapp.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Set;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.yrgo.libraryapp.dao.BookDao;
import se.yrgo.libraryapp.entities.BookEdition;
import se.yrgo.libraryapp.entities.DdsClassification;

class BookControllerTest {

    private BookDao bookDao;
    private BookController bookController;

    @BeforeEach
    void setUp() {
        bookDao = mock(BookDao.class);
        bookController = new BookController(bookDao);
    }

    @Test
    void testSearchByClassification() {
        Set<BookEdition> expectedBooks = Collections.emptySet();
        when(bookDao.withClass(any(DdsClassification.class))).thenReturn(expectedBooks);

        Set<BookEdition> result = bookController.search(null, null, null, "300");

        assertThat(result).isEqualTo(expectedBooks);
        verify(bookDao).withClass(any(DdsClassification.class));
    }

    @Test
    void testSearchByParameters() {
        Set<BookEdition> expectedBooks = Collections.emptySet();
        String isbn = "9781234567890";
        String title = "Java Programming";
        String author = "Jane Doe";

        when(bookDao.find(isbn, title, author)).thenReturn(expectedBooks);

        Set<BookEdition> result = bookController.search(isbn, title, author, null);

        assertThat(result).isEqualTo(expectedBooks);
        verify(bookDao).find(isbn, title, author);
    }
}