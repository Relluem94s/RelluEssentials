package de.relluem94.minecraft.server.spigot.essentials.discovery.testfixtures;

import de.relluem94.minecraft.server.spigot.essentials.discovery.TestAnnotation;
import java.lang.annotation.Annotation;

@TestAnnotation
public class AnnotatedTestFixture implements Annotation {

  @Override
  public Class<? extends Annotation> annotationType() {
    return TestAnnotation.class;
  }
}