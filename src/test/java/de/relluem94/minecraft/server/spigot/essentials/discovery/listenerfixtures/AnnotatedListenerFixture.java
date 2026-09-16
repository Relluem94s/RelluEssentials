package de.relluem94.minecraft.server.spigot.essentials.discovery.listenerfixtures;

import de.relluem94.minecraft.server.spigot.essentials.annotations.ListenerName;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;

@ListenerName("AnnotatedListenerFixture")
public class AnnotatedListenerFixture implements ListenerConstruct {

  @Override
  public void injectContext(ServiceContext context) {}
}