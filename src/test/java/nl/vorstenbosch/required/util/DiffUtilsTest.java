package nl.vorstenbosch.required.util;

import com.github.difflib.text.DiffRow;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DiffUtilsTest {

    @Test
    void safeMarkDownTagsAppendEndOfLine() {
        // Given
        List<String> versionB = Arrays.asList("This is a line of text.");
        List<String> versionA = Arrays.asList("This is a line of text. Now it contains two sentences");

        List<DiffRow> expectedResult = Arrays.asList(new DiffRow(DiffRow.Tag.CHANGE, "This is a line of text.", "This is a line of **text. Now it contains two sentences**"));

        // When
        List<DiffRow> diffRows = DiffUtils.generateDiff(versionA, versionB);

        // Then
        assertEquals(expectedResult, diffRows);
    }

    @Test
    void safeMarkDownTagsAddDotAtEndOfLine() {
        // Given
        List<String> versionB = Arrays.asList("This is a line of text");
        List<String> versionA = Arrays.asList("This is a line of text.");

        List<DiffRow> expectedResult = Arrays.asList(new DiffRow(DiffRow.Tag.CHANGE, "This is a line of text", "This is a line of **text.**"));

        // When
        List<DiffRow> diffRows = DiffUtils.generateDiff(versionA, versionB);

        // Then
        assertEquals(expectedResult, diffRows);
    }

}