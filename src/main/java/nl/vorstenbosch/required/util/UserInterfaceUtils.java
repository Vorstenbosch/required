package nl.vorstenbosch.required.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserInterfaceUtils {

    private static final Logger log = LoggerFactory.getLogger(UserInterfaceUtils.class);

    public static List<String> parseDescription(String description) {
        log.info("description received: {}", description);
        List<String> descriptionAsList = new ArrayList<>();
        Arrays.stream(description.split("\\R")).forEach(line -> {
           descriptionAsList.add(line);
        });

        log.info("list created: {}", descriptionAsList);
        return descriptionAsList;
    }

    public static String ListToStringWithNewLines(List<String> description) {
        log.info("description as list received: {}", description);
        StringBuilder stringBuilder = new StringBuilder();
        description.forEach(line -> stringBuilder.append(line + "\r\n"));

        if(stringBuilder.substring(stringBuilder.length() -2, stringBuilder.length()).equals("\r\n")) {
            stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
        }

        log.info("list as string created: {}", stringBuilder);
        return stringBuilder.toString();
    }
}
