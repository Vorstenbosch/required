package nl.vorstenbosch.required.util;

import com.github.difflib.text.DiffRow;
import com.github.difflib.text.DiffRowGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        diff = sanitizeMarkDown(diff);
        log.info("diff created: {}", diff);
        return diff;
    }

    /**
     * Method to sanitize MarkDown tags in the newLines
     * @param diffRows
     * @return sanitized DiffRows
     */
    private static List<DiffRow> sanitizeMarkDown(List<DiffRow> diffRows) {
        // This pattern matches if there is invalid markdown in the line
        //   invalid due to strikethrough or bold sections beginning with ' ', '.', '!' or ','
        Pattern pattern = Pattern.compile("(.* )([a-zA-Z-.,!]+)(~~ |\\*\\* |\\*\\*\\.|~~\\.|\\*\\*,|~~,|\\*\\*!|~~!,)(.*)$");

        List<DiffRow> safeDiffRows = new ArrayList(diffRows);

        for(int i=0; i < diffRows.size(); i++) {
            DiffRow diffRow = diffRows.get(i);
            Matcher matcher = pattern.matcher(diffRow.getNewLine());
            boolean matchFound = matcher.find();
            if (matchFound) {
                StringBuilder newLineBuilder = new StringBuilder();
                String partUpToWordBeforeTag = matcher.group(1);
                String wordBeforeTag = matcher.group(2);
                String taggedPartUntilLineEnd = matcher.group(3) + matcher.group(4);
                newLineBuilder.append(partUpToWordBeforeTag);
                String tag = taggedPartUntilLineEnd.substring(0, 2);
                newLineBuilder.append(tag);
                newLineBuilder.append(wordBeforeTag);
                String taggedPartWithoutStartingTag = taggedPartUntilLineEnd.substring(2);
                newLineBuilder.append(taggedPartWithoutStartingTag);
                safeDiffRows.set(i, new DiffRow(diffRow.getTag(), diffRow.getOldLine(), newLineBuilder.toString()));
            }
        }

        return safeDiffRows;
    }
}
