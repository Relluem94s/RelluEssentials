package de.relluem94.minecraft.server.spigot.essentials.discovery;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.relluem94.minecraft.server.spigot.essentials.discovery.testfixtures.AnnotatedTestFixture;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ClassDiscoveryHelperTest {

  @Test
  void privateConstructorThrowsIllegalStateException() throws Exception {
    Constructor<ClassDiscoveryHelper> constructor = ClassDiscoveryHelper.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, constructor::newInstance);
    assertInstanceOf(IllegalStateException.class, thrown.getCause());
  }

  @Test
  void findAnnotatedClassesReturnsEmptyListWhenResourceNotFound() {
    ClassLoader classLoader = new URLClassLoader(new URL[0], null);
    List<Class<? extends Runnable>> result = ClassDiscoveryHelper.findAnnotatedClasses(
        "de.nonexistent.package",
        Deprecated.class,
        Runnable.class,
        classLoader
    );
    assertTrue(result.isEmpty());
  }

  @Test
  void findAnnotatedClassesReturnsEmptyListForUnknownProtocol() throws Exception {
    ClassLoader fakeProtocolClassLoader = new ClassLoader(ClassDiscoveryHelperTest.class.getClassLoader()) {
      @Override
      public URL getResource(String name) {
        try {
          return new URL("ftp://localhost/fake/path");
        } catch (Exception e) {
          return null;
        }
      }
    };

    List<Class<? extends Runnable>> result = ClassDiscoveryHelper.findAnnotatedClasses(
        "de.relluem94.minecraft.server.spigot.essentials.discovery",
        Deprecated.class,
        Runnable.class,
        fakeProtocolClassLoader
    );

    assertTrue(result.isEmpty());
  }

  @Test
  void findAnnotatedClassesFindsAnnotatedClassesFromFileSystemDirectory() {
    ClassLoader classLoader = ClassDiscoveryHelperTest.class.getClassLoader();

    List<Class<? extends Annotation>> result = ClassDiscoveryHelper.findAnnotatedClasses(
        "de.relluem94.minecraft.server.spigot.essentials.discovery.testfixtures",
        TestAnnotation.class,
        Annotation.class,
        classLoader
    );

    assertFalse(result.isEmpty());
    assertTrue(result.stream().allMatch(c -> c.isAnnotationPresent(TestAnnotation.class)));
  }

  @Test
  void findAnnotatedClassesExcludesClassesWithoutAnnotation() {
    ClassLoader classLoader = ClassDiscoveryHelperTest.class.getClassLoader();

    List<Class<? extends Runnable>> result = ClassDiscoveryHelper.findAnnotatedClasses(
        "de.relluem94.minecraft.server.spigot.essentials.discovery.testfixtures",
        TestAnnotation.class,
        Runnable.class,
        classLoader
    );

    assertTrue(result.isEmpty());
  }

  @Test
  void findAnnotatedClassesExcludesClassesNotAssignableToTargetType() {
    ClassLoader classLoader = ClassDiscoveryHelperTest.class.getClassLoader();

    List<Class<? extends Number>> result = ClassDiscoveryHelper.findAnnotatedClasses(
        "de.relluem94.minecraft.server.spigot.essentials.discovery.testfixtures",
        TestAnnotation.class,
        Number.class,
        classLoader
    );

    assertTrue(result.isEmpty());
  }

  @Test
  void findAnnotatedClassesReturnsEmptyListWhenPackageNameIsEmpty() {
    ClassLoader classLoader = ClassDiscoveryHelperTest.class.getClassLoader();

    List<Class<? extends Runnable>> result = ClassDiscoveryHelper.findAnnotatedClasses(
        "",
        Deprecated.class,
        Runnable.class,
        classLoader
    );

    assertTrue(result.isEmpty());
  }

  @Test
  void findAnnotatedClassesFromJarReturnsEmptyListWhenNoAnnotatedClassesPresent() throws Exception {
    File temporaryJarFile = buildTemporaryJarContainingTestFixtures();

    URLClassLoader jarClassLoader = new URLClassLoader(
        new URL[]{temporaryJarFile.toURI().toURL()},
        null
    );

    List<Class<? extends Runnable>> result = ClassDiscoveryHelper.findAnnotatedClasses(
        "de.relluem94.minecraft.server.spigot.essentials.discovery.testfixtures",
        TestAnnotation.class,
        Runnable.class,
        jarClassLoader
    );

    jarClassLoader.close();
    temporaryJarFile.delete();

    assertTrue(result.isEmpty());
  }

  @Test
  void findAnnotatedClassesFromJarExcludesClassesWithoutAnnotation() throws Exception {
    File temporaryJarFile = buildTemporaryJarContainingTestFixtures();

    URLClassLoader jarClassLoader = new URLClassLoader(
        new URL[]{temporaryJarFile.toURI().toURL()},
        null
    );

    List<Class<? extends Annotation>> result = ClassDiscoveryHelper.findAnnotatedClasses(
        "de.relluem94.minecraft.server.spigot.essentials.discovery.testfixtures",
        Deprecated.class,
        Annotation.class,
        jarClassLoader
    );

    jarClassLoader.close();

    assertTrue(temporaryJarFile.delete());
    assertTrue(result.isEmpty());
  }

  @Test
  void debugJarResourceProtocol() throws Exception {
    File temporaryJarFile = buildTemporaryJarContainingTestFixtures();

    URLClassLoader jarClassLoader = new URLClassLoader(
        new URL[]{temporaryJarFile.toURI().toURL()},
        Annotation.class.getClassLoader()
    );

    String packagePath = "de/relluem94/minecraft/server/spigot/essentials/discovery/testfixtures";
    URL resource = jarClassLoader.getResource(packagePath);

    System.out.println("Resource: " + resource);
    System.out.println("Protocol: " + (resource != null ? resource.getProtocol() : "null"));

    jarClassLoader.close();
    assertTrue(temporaryJarFile.delete());
  }

  @Test
  void findAnnotatedClassesFromJarReturnsAnnotatedClassesMatchingTargetType() throws Exception {
    File temporaryJarFile = buildTemporaryJarContainingTestFixtures();

    URLClassLoader jarClassLoader = new URLClassLoader(
        new URL[]{temporaryJarFile.toURI().toURL()},
        ClassDiscoveryHelperTest.class.getClassLoader()
    ) {
      @Override
      public URL getResource(String name) {
        URL jarResource = findResource(name);
        if (jarResource != null) {
          return jarResource;
        }
        return null;
      }
    };

    List<Class<? extends Annotation>> result = ClassDiscoveryHelper.findAnnotatedClasses(
        "de.relluem94.minecraft.server.spigot.essentials.discovery.testfixtures",
        TestAnnotation.class,
        Annotation.class,
        jarClassLoader
    );

    jarClassLoader.close();
    assertTrue(temporaryJarFile.delete());

    assertFalse(result.isEmpty());
    assertTrue(result.stream().anyMatch(c -> c.getSimpleName().equals("AnnotatedTestFixture")));
  }

  private File buildTemporaryJarContainingTestFixtures() throws Exception {
    File temporaryJarFile = Files.createTempFile("testfixtures", ".jar").toFile();

    try (FileOutputStream fileOutputStream = new FileOutputStream(temporaryJarFile);
        JarOutputStream jarOutputStream = new JarOutputStream(fileOutputStream)) {

      writeDirectoryEntryToJar(jarOutputStream, "de/relluem94/minecraft/server/spigot/essentials/discovery/testfixtures/");
      writeClassToJar(jarOutputStream, AnnotatedTestFixture.class);
      writeClassToJar(jarOutputStream, TestAnnotation.class);
    }

    return temporaryJarFile;
  }

  private void writeDirectoryEntryToJar(JarOutputStream jarOutputStream, String directoryPath) throws Exception {
    jarOutputStream.putNextEntry(new JarEntry(directoryPath));
    jarOutputStream.closeEntry();
  }

  private void writeClassToJar(JarOutputStream jarOutputStream, Class<?> clazz) throws Exception {
    String classResourcePath = clazz.getName().replace('.', '/') + ".class";

    try (InputStream classInputStream = clazz.getClassLoader().getResourceAsStream(classResourcePath)) {
      byte[] classBytes = readAllBytes(classInputStream);
      jarOutputStream.putNextEntry(new JarEntry(classResourcePath));
      jarOutputStream.write(classBytes);
      jarOutputStream.closeEntry();
    }
  }

  private byte[] readAllBytes(InputStream inputStream) throws Exception {
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    byte[] chunk = new byte[4096];
    int bytesRead;
    while ((bytesRead = inputStream.read(chunk)) != -1) {
      buffer.write(chunk, 0, bytesRead);
    }
    return buffer.toByteArray();
  }
}