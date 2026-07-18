# Documentation

## `public class AnnotationHelper`

## `public static <T extends CommandConstruct> String getCommandName(Class<T> clazz)`

Returns the command name defined by the `@CommandName` annotation on the given class

* **Parameters:**
   * `clazz` — Class extending CommandConstruct
* **Returns:** String — the command name, or null if the annotation is not present or an error occurs
```

```markdown docs/helpers/AttributeHelper.md
# Documentation

## `public class AttributeHelper`

## `public static Multimap<Attribute, AttributeModifier> addAttribute()`

Returns an empty attribute map

* **Returns:** Multimap of Attribute and AttributeModifier

## `public static Multimap<Attribute, AttributeModifier> addAttribute(Attribute attribute, Operation operation, EquipmentSlotGroup slot, String name, double multiplier)`

Returns a new attribute map containing a single attribute modifier

* **Parameters:**
   * `attribute` — Attribute
   * `operation` — Operation
   * `slot` — EquipmentSlotGroup
   * `name` — String
   * `multiplier` — double
* **Returns:** Multimap of Attribute and AttributeModifier

## `public static Multimap<Attribute, AttributeModifier> addAttribute(Multimap<Attribute, AttributeModifier> attributes, Attribute attribute, Operation operation, EquipmentSlotGroup slot, String name, double multiplier)`

Adds an attribute modifier to an existing attribute map and returns it

* **Parameters:**
   * `attributes` — Multimap of Attribute and AttributeModifier
   * `attribute` — Attribute
   * `operation` — Operation
   * `slot` — EquipmentSlotGroup
   * `name` — String
   * `multiplier` — double
* **Returns:** Multimap of Attribute and AttributeModifier — the same map passed as `attributes`, with the new entry added