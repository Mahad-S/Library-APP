package se.yrgo.libraryapp.validators;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class UsernameTest {
  
    @Test
    void testCorrectUsername() {
        assertThat(Username.validate("Boose123")).isTrue();
    }

    @Test
    void testIncorrectUsername() {
        assertThat(Username.validate("Test with space")).isFalse();
    }
}
