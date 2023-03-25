package org.hse.bse.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DataProvider {
  private static Path resources = Path.of("src", "main", "resources");

  public static String read(Data data) {
    try {
      return Files.readString(resources.resolve(data.getFilename()));
    } catch (IOException ex) {
      ex.printStackTrace();
      return "{}";
    }
  }

  public static JsonObject parse(String content) {
    return (JsonObject) new JsonParser().parse(content);
  }
}
