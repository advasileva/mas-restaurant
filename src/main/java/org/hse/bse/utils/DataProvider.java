package org.hse.bse.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DataProvider {
    private static Path resources = Path.of("src", "main", "resources");

    public static JsonObject readAsJson(Data data) {
        return parse(readAsString(data));
    }

    public static String readAsString(Data data) {
        try {
            String content =
                    Files.readString(resources.resolve("input").resolve(data.getFilename()));
            DataChecker.check(data, content);
            return content;
        } catch (IOException ex) {
            ex.printStackTrace();
            return "{}";
        }
    }

    public static JsonObject parse(String content) {
        return JsonParser.parseString(content).getAsJsonObject();
    }

    public static void write(Data data, String content) {
        try {
            Files.write(
                    resources.resolve("output").resolve(data.getFilename()), content.getBytes());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
