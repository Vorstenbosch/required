package nl.vorstenbosch.required.util;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserInterfaceUtilsTest {

    @Test
    void parseDescription() {
        // Given
        String description = "# Header\r\nTesting the parsing of descriptions\r\n\r\nDoes it work?";
        List<String> expectedResult = Arrays.asList("# Header", "Testing the parsing of descriptions", "", "Does it work?");

        // When
        List<String> parsedDescription = UserInterfaceUtils.parseDescription(description);

        // Then
        assertEquals(expectedResult, parsedDescription);
    }

    @Test
    void ListToStringWithNewLines() {
        // Given
        List<String> descriptionAsList = Arrays.asList("# Header", "Testing the parsing of descriptions", "", "Does it work?");
        String expectedResult = "# Header\r\nTesting the parsing of descriptions\r\n\r\nDoes it work?";

        // When
        String parsedDescription = UserInterfaceUtils.ListToStringWithNewLines(descriptionAsList);

        // Then
        assertEquals(expectedResult, parsedDescription);
    }

}