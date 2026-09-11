package de.relluem94.minecraft.server.spigot.essentials.models;

import lombok.Getter;

public class SignAction {

  @Getter
  private final String name;
  private final boolean requiresCustomInput;

  public SignAction(String name, boolean requiresCustomInput) {
    this.name = name;
    this.requiresCustomInput = requiresCustomInput;
  }

  public boolean requiresCustomInput() {
    return requiresCustomInput;
  }

  public String getDisplayName() {
    return name;
  }

  public String getShorthandBracket() {
    return "[" + name.toUpperCase() + "]";
  }

  public String getNameBracket() {
    return "[" + name.toUpperCase() + "]";
  }
}