package de.relluem94.minecraft.server.spigot.essentials.models;

import lombok.Getter;

/**
 * Represents an action that can be triggered by interacting with a sign.
 * Each action has a unique name and may require additional custom input.
 *
 * @author rellu
 */
@SuppressWarnings("ClassCanBeRecord")
public class SignAction {

  @Getter
  private final String name;
  private final boolean requiresCustomInput;

  /**
   * Creates a new sign action definition.
   *
   * @param name the unique name of the sign action
   * @param requiresCustomInput {@code true} if the action requires additional custom input, {@code false} otherwise
   */
  public SignAction(String name, boolean requiresCustomInput) {
    this.name = name;
    this.requiresCustomInput = requiresCustomInput;
  }

  /**
   * Indicates whether this sign action requires additional custom input.
   *
   * @return {@code true} if additional input is required; {@code false} otherwise
   */
  public boolean requiresCustomInput() {
    return requiresCustomInput;
  }

  /**
   * Returns the display name of this sign action.
   *
   * @return the display name of this sign action
   */
  public String getDisplayName() {
    return name;
  }

  /**
   * Returns the uppercase, bracketed shorthand representation of this sign action name.
   *
   * @return the shorthand representation in the form {@code [NAME]}
   */
  public String getShorthandBracket() {
    return "[" + name.toUpperCase() + "]";
  }

  /**
   * Returns the uppercase, bracketed representation of this sign action name.
   * This is functionally identical to {@link #getShorthandBracket()}.
   *
   * @return the name representation in the form {@code [NAME]}
   */
  public String getNameBracket() {
    return "[" + name.toUpperCase() + "]";
  }
}