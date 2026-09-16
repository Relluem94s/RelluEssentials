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
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
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
  void findAnnotatedClassesReturnsEmptyListForUnknownProtocol() {
    ClassLoader fakeProtocolClassLoader = new ClassLoader(ClassDiscoveryHelperTest.class.getClassLoader()) {
      @Override
      public URL getResource(String name) {
        try {
          return URI.create("ftp://localhost/fake/path").toURL();
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
    assertTrue(temporaryJarFile.delete());

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
  void findAnnotatedClassesFromJarReturnsAnnotatedClassesMatchingTargetType() throws Exception {
    File temporaryJarFile = buildTemporaryJarContainingTestFixtures();

    URLClassLoader jarClassLoader = new URLClassLoader(
        new URL[]{temporaryJarFile.toURI().toURL()},
        ClassDiscoveryHelperTest.class.getClassLoader()
    ) {
      @Override
      public URL getResource(String name) {
        return findResource(name);
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

  @Test
  void findAnnotatedClassesReturnsEmptyListWhenDirectoryListingReturnsNull() throws Exception {
    File nonDirectoryFile = Files.createTempFile("notadirectory", ".tmp").toFile();
    nonDirectoryFile.deleteOnExit();

    ClassLoader classLoaderPointingToFile = new ClassLoader(ClassDiscoveryHelperTest.class.getClassLoader()) {
      @Override
      public URL getResource(String name) {
        try {
          return nonDirectoryFile.toURI().toURL();
        } catch (Exception e) {
          return null;
        }
      }
    };

    List<Class<? extends Runnable>> result = ClassDiscoveryHelper.findAnnotatedClasses(
        "de.relluem94.minecraft.server.spigot.essentials.discovery.testfixtures",
        TestAnnotation.class,
        Runnable.class,
        classLoaderPointingToFile
    );

    assertTrue(result.isEmpty());
  }

  @Test
  void findAnnotatedClassesReturnsEmptyListWhenJarStreamCannotBeOpened() throws Exception {
    File temporaryJarFile = buildTemporaryJarContainingTestFixtures();
    URL validJarUrl = temporaryJarFile.toURI().toURL();

    ClassLoader classLoaderWithUnreadableJarUrl = new ClassLoader(ClassDiscoveryHelperTest.class.getClassLoader()) {
      @Override
      public URL getResource(String name) {
        try {
          return URI.create("jar:" + validJarUrl + "!/de/relluem94/minecraft/server/spigot/essentials/discovery/testfixtures").toURL();
        } catch (Exception e) {
          return null;
        }
      }
    };

    assertTrue(temporaryJarFile.delete());

    List<Class<? extends Runnable>> result = ClassDiscoveryHelper.findAnnotatedClasses(
        "de.relluem94.minecraft.server.spigot.essentials.discovery.testfixtures",
        TestAnnotation.class,
        Runnable.class,
        classLoaderWithUnreadableJarUrl
    );

    assertTrue(result.isEmpty());
  }

  @Test
  void findAnnotatedClassesReturnsEmptyListWhenFileUrlContainsInvalidUriSyntax() throws Exception {
    URL mockedUrl = Mockito.mock(URL.class);
    Mockito.when(mockedUrl.getProtocol()).thenReturn("file");
    Mockito.when(mockedUrl.toURI()).thenThrow(new URISyntaxException("invalid", "mocked"));

    ClassLoader classLoaderWithMockedUrl = new ClassLoader(ClassDiscoveryHelperTest.class.getClassLoader()) {
      @Override
      public URL getResource(String name) {
        return mockedUrl;
      }
    };

    List<Class<? extends Runnable>> result = ClassDiscoveryHelper.findAnnotatedClasses(
        "de.relluem94.minecraft.server.spigot.essentials.discovery.testfixtures",
        TestAnnotation.class,
        Runnable.class,
        classLoaderWithMockedUrl
    );

    assertTrue(result.isEmpty());
  }

  private File buildTemporaryJarContainingTestFixtures() throws Exception {
    File temporaryJarFile = Files.createTempFile("testfixtures", ".jar").toFile();

    try (FileOutputStream fileOutputStream = new FileOutputStream(temporaryJarFile);
        JarOutputStream jarOutputStream = new JarOutputStream(fileOutputStream)) {

      writeDirectoryEntryToJar(jarOutputStream);
      writeClassToJar(jarOutputStream, AnnotatedTestFixture.class);
      writeClassToJar(jarOutputStream, TestAnnotation.class);
    }

    return temporaryJarFile;
  }

  private void writeDirectoryEntryToJar(JarOutputStream jarOutputStream) throws Exception {
    jarOutputStream.putNextEntry(new JarEntry(
        "de/relluem94/minecraft/server/spigot/essentials/discovery/testfixtures/"));
    jarOutputStream.closeEntry();
  }

  private void writeClassToJar(JarOutputStream jarOutputStream, Class<?> clazz) throws Exception {
    String classResourcePath = clazz.getName().replace('.', '/') + ".class";

    try (InputStream classInputStream = clazz.getClassLoader().getResourceAsStream(classResourcePath)) {
      assert classInputStream != null;
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