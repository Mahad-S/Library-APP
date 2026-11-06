package se.yrgo.libraryapp.validators;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class UtilsTest {

    @Test
    void testOnlyLettersAndWhitespace() {

        assertThat(Utils.onlyLettersAndWhitespace("Test 123!@#")).isEqualTo("test ");
        
    }

    
}
