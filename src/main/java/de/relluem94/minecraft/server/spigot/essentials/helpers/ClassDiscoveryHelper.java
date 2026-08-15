package de.relluem94.minecraft.server.spigot.essentials.helpers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class ClassDiscoveryHelper {

  public static <T> List<Class<? extends T>> findAnnotatedClasses(
      String packageName,
      Class<? extends Annotation> annotation,
      Class<T> targetType,
      ClassLoader classLoader
  ) {
    String packagePath = packageName.replace('.', '/');
    URL resource = classLoader.getResource(packagePath);

    if (resource == null) {
      return List.of();
    }

    return switch (resource.getProtocol()) {
      case "jar" -> scanJar(resource, packagePath, annotation, targetType, classLoader);
      case "file" -> scanDirectory(resource, packageName, annotation, targetType, classLoader);
      default -> List.of();
    };
  }

  private static <T> List<Class<? extends T>> scanJar(
      URL jarResource,
      String packagePath,
      Class<? extends Annotation> annotation,
      Class<T> targetType,
      ClassLoader classLoader
  ) {
    String jarUrlString = jarResource.toString();
    String jarFilePath = jarUrlString.substring("jar:".length(), jarUrlString.indexOf("!/"));

    try (InputStream inputStream = URI.create(jarFilePath).toURL().openStream();
        JarInputStream jarInputStream = new JarInputStream(inputStream)) {

      return collectAnnotatedClassesFromJar(jarInputStream, packagePath, annotation, targetType, classLoader);
    } catch (IOException e) {
      return List.of();
    }
  }

  private static <T> List<Class<? extends T>> collectAnnotatedClassesFromJar(
      JarInputStream jarInputStream,
      String packagePath,
      Class<? extends Annotation> annotation,
      Class<T> targetType,
      ClassLoader classLoader
  ) throws IOException {
    List<Class<? extends T>> result = new ArrayList<>();
    JarEntry entry;

    while ((entry = jarInputStream.getNextJarEntry()) != null) {
      String entryName = entry.getName();
      if (entryName.startsWith(packagePath) && entryName.endsWith(".class") && !entry.isDirectory()) {
        String className = entryName.replace('/', '.').replace(".class", "");
        resolveAnnotatedClass(className, annotation, targetType, classLoader).ifPresent(result::add);
      }
    }

    return result;
  }

  private static <T> List<Class<? extends T>> scanDirectory(
      URL resource,
      String packageName,
      Class<? extends Annotation> annotation,
      Class<T> targetType,
      ClassLoader classLoader
  ) {
    try {
      return scanDirectoryRecursive(new File(resource.toURI()), packageName, annotation, targetType, classLoader);
    } catch (URISyntaxException e) {
      return List.of();
    }
  }

  private static <T> List<Class<? extends T>> scanDirectoryRecursive(
      File directory,
      String packageName,
      Class<? extends Annotation> annotation,
      Class<T> targetType,
      ClassLoader classLoader
  ) {
    File[] files = directory.listFiles();
    if (files == null) {
      return List.of();
    }

    List<Class<? extends T>> result = new ArrayList<>();
    for (File file : files) {
      if (file.isDirectory()) {
        result.addAll(scanDirectoryRecursive(file, packageName + "." + file.getName(), annotation, targetType, classLoader));
      } else if (file.getName().endsWith(".class")) {
        String className = packageName + "." + file.getName().replace(".class", "");
        resolveAnnotatedClass(className, annotation, targetType, classLoader).ifPresent(result::add);
      }
    }

    return result;
  }

  private static <T> Optional<Class<? extends T>> resolveAnnotatedClass(
      String className,
      Class<? extends Annotation> annotation,
      Class<T> targetType,
      ClassLoader classLoader
  ) {
    try {
      Class<?> clazz = classLoader.loadClass(className);
      if (clazz.isAnnotationPresent(annotation) && targetType.isAssignableFrom(clazz)) {
        return Optional.of(clazz.asSubclass(targetType));
      }
    } catch (ClassNotFoundException ignored) {
    }
    return Optional.empty();
  }
}