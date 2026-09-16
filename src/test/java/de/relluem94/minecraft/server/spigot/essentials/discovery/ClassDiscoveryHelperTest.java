package de.relluem94.minecraft.server.spigot.essentials.discovery;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
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
  void findAnnotatedClassesFromJarReturnsAnnotatedClasses() throws Exception {
    URL jarUrl = ClassDiscoveryHelperTest.class.getResource("/testfixtures.jar");
    assumeJarResourceExists(jarUrl);

    URLClassLoader jarClassLoader = new URLClassLoader(new URL[]{jarUrl}, ClassDiscoveryHelperTest.class.getClassLoader());

    List<Class<? extends Annotation>> result = ClassDiscoveryHelper.findAnnotatedClasses(
        "de.relluem94.minecraft.server.spigot.essentials.discovery.testfixtures",
        TestAnnotation.class,
        Annotation.class,
        jarClassLoader
    );

    assertFalse(result.isEmpty());
    assertTrue(result.stream().allMatch(c -> c.isAnnotationPresent(TestAnnotation.class)));
  }

  private void assumeJarResourceExists(URL resource) {
    org.junit.jupiter.api.Assumptions.assumeTrue(resource != null, "Skipping JAR test: testfixtures.jar not found.");
  }
}