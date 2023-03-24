package org.hse.bse.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.FileReader;
import java.nio.file.Path;

public class DataProvider {
  private static Path resources = Path.of("src", "main", "resources");

  public static JsonObject read(Data data) {
    JsonParser parser = new JsonParser();
    try {
      return (JsonObject)
          parser.parse(new FileReader(resources.resolve(data.getFilename()).toString()));
    } catch (java.io.FileNotFoundException exception) {
      exception.printStackTrace();
      return null;
    }
  }
}
