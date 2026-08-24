package de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.loader;

import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

public class ClasspathSqlResourceLoader implements SqlResourceLoader {

  @Override
  public String load(String fileName) throws FileNotFoundException {
    try (InputStream is = getClass().getResourceAsStream("/" + fileName);
        InputStreamReader isr = new InputStreamReader(Objects.requireNonNull(is));
        BufferedReader br = new BufferedReader(isr)) {

      StringBuilder sb = new StringBuilder();
      String line;
      while ((line = br.readLine()) != null) {
        sb.append(line).append(Constants.PLUGIN_EOL);
      }
      return sb.toString();

    } catch (IOException | NullPointerException e) {
      throw new FileNotFoundException(String.format("File %s was not found!", fileName));
    }
  }
}