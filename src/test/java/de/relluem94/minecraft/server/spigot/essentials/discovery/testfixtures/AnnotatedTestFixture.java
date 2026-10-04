package de.relluem94.minecraft.server.spigot.essentials.discovery.testfixtures;

import de.relluem94.minecraft.server.spigot.essentials.discovery.TestAnnotation;
import java.lang.annotation.Annotation;

/**
 * Test fixture representing an annotated class used to verify annotation-based discovery logic.
 *
 * @author rellu
 */
@TestAnnotation
public class AnnotatedTestFixture implements Annotation {

  @Override
  public Class<? extends Annotation> annotationType() {
    return TestAnnotation.class;
  }
}