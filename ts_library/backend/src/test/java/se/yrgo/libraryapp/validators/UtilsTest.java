package se.yrgo.libraryapp.validators;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
    
public class UtilsTest {


    @Test
    void testOnlyLettersAndWhitespaceWithNull() {
        assertThat(Utils.onlyLettersAndWhitespace(null)).isNull();
    }

    @Test
    void testOnlyLettersAndWhitespaceEmptyString() {
        assertThat(Utils.onlyLettersAndWhitespace("")).isEqualTo("");
    }

    @Test
    void testCleanAndUnLeetWithNull() {
        assertThat(Utils.cleanAndUnLeet(null)).isNull();
    }

    @Test
    void testCleanAndUnLeet() {
        assertThat(Utils.cleanAndUnLeet("H4ck3r")).isEqualTo("hacker");
    }

    @Test
    void testCleanAndUnLeetComplex() {
        assertThat(Utils.cleanAndUnLeet("H3LL0! 1122")).isEqualTo("hello ll");
    }
}
