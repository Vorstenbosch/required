package nl.vorstenbosch.required.util;

import com.github.difflib.text.DiffRow;
import com.github.difflib.text.DiffRowGenerator;

import java.util.List;

public class DiffUtils {

    public static List<DiffRow> generateDiff(List<String> versionA, List<String> versionB) {
        DiffRowGenerator generator = DiffRowGenerator.create()
                .showInlineDiffs(true)
                .inlineDiffByWord(true)
                .oldTag(f -> "~~")
                .newTag(f -> "**")
                .reportLinesUnchanged(true)
                .build();
        return generator.generateDiffRows(versionB, versionA);
    }
}
