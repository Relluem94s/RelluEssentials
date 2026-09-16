package de.relluem94.minecraft.server.spigot.essentials.discovery.listenerfixtures;

import de.relluem94.minecraft.server.spigot.essentials.annotations.ListenerName;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;

@ListenerName("UninstantiableListenerFixture")
public class UninstantiableListenerFixture implements ListenerConstruct {

  private UninstantiableListenerFixture() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void injectContext(ServiceContext context) {}
}