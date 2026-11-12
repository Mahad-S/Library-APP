package se.yrgo.libraryapp.validators;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.assertj.core.api.Assertions.assertThat;

class RealNameTest {

    @ParameterizedTest
    @ValueSource(strings = { "Anna Andersson", "karl-gÖrAn", "Maria", "O'Connor" })
    void testValidRealNames(String input) {
        assertThat(RealName.validate(input)).isTrue();
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    @ValueSource(strings = { "   ", "darn", "DuMb", "karl£johan", "Anna123", "Sven Svensson!", "Anna_Andersson", "A A A", })
    void testInvalidRealNames(String input) {
        assertThat(RealName.validate(input)).isFalse();
    }