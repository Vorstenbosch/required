package nl.vorstenbosch.required.util;

import com.github.difflib.text.DiffRow;
import com.github.difflib.text.DiffRowGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DiffUtils {

    private static final Logger log = LoggerFactory.getLogger(DiffUtils.class);

    public static List<DiffRow> generateDiff(List<String> versionA, List<String> versionB) {
        DiffRowGenerator generator = DiffRowGenerator.create()
                .showInlineDiffs(true)
                .inlineDiffByWord(true)
                .oldTag(f -> "~~")
                .newTag(f -> "**")
                .reportLinesUnchanged(true)
                .build();
        List<DiffRow> diff = generator.generateDiffRows(versionB, versionA);
        log.info("diff created: {}", diff);
        return diff;
    }
}
