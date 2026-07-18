# Documentation

## `public class AttributeHelper`

* **Author:** rellu

## `public static Multimap<Attribute, AttributeModifier> addAttribute()`

Creates and returns an empty Attribute Multimap

* **Returns:** Multimap of Attribute and AttributeModifier

## `public static Multimap<Attribute, AttributeModifier> addAttribute(Attribute attribute, Operation operation, EquipmentSlotGroup slot, String name, double multiplier)`

Creates a new Attribute Multimap and adds the given Attribute with the given Modifier to it

* **Parameters:**
   * `attribute` — Attribute
   * `operation` — Operation
   * `slot` — EquipmentSlotGroup
   * `name` — String
   * `multiplier` — double
* **Returns:** Multimap of Attribute and AttributeModifier

## `public static Multimap<Attribute, AttributeModifier> addAttribute(Multimap<Attribute, AttributeModifier> attributes, Attribute attribute, Operation operation, EquipmentSlotGroup slot, String name, double multiplier)`

Adds the given Attribute with the given Modifier to an existing Attribute Multimap

* **Parameters:**
   * `attributes` — Multimap of Attribute and AttributeModifier
   * `attribute` — Attribute
   * `operation` — Operation
   * `slot` — EquipmentSlotGroup
   * `name` — String
   * `multiplier` — double
* **Returns:** Multimap of Attribute and AttributeModifier