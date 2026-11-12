package se.yrgo.libraryapp.validators;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.assertj.core.api.Assertions.assertThat;

class UsernameTest {

    @ParameterizedTest
    @ValueSource(strings = {"Boose123", "user1", "abcd", "A_B_C", "test_user"})
    void testValidUsernames(String input) {
        assertThat(Username.validate(input)).isTrue();
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    @ValueSource(strings = {"   ", "abc", "a-_","test!user", "user name"})
    void testInvalidUsernames(String input) {
        assertThat(Username.validate(input)).isFalse();
    }
}
