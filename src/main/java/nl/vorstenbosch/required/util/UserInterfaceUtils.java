package nl.vorstenbosch.required.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserInterfaceUtils {

    public static List<String> parseDescription(String description) {
        List<String> descriptionAsList = new ArrayList<>();
        Arrays.stream(description.split("\n")).forEach(line -> {
           descriptionAsList.add(line);
        });

        return descriptionAsList;
    }

    public static String ListToStringWithNewLines(List<String> description) {
        StringBuilder stringBuilder = new StringBuilder();
        description.forEach(line -> {
            stringBuilder.append(line + "\n");
        });

        stringBuilder.delete(stringBuilder.length() -2, stringBuilder.length());

        return stringBuilder.toString();
    }
}
