package se.yrgo.libraryapp.validators;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class RealNameTest {

    @Test
    void testValidateWithValidName() {
        assertThat(RealName.validate("John Doe")).isTrue();
    }

    @Test
    void testValidateWithBadWords() {
        assertThat(RealName.validate("admin Doe")).isFalse();
    }
}