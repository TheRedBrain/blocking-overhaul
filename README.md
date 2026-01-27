# Blocking Overhaul

Overhauls blocking attacks by adding a parry mechanic and changing several mechanics to be entity attribute - driven.

When blocking is started just before a hit, the attack is parried. The length (in ticks) of that time window is determined by the "blockingoverhaul:parry_window" entity attribute.

## Changes to knockback on blocking/parrying

Blocking/parrying an attack no longer applies a hardcoded amount of knockback to the attacker.

Instead, the knockback is more dynamic, where knockback amount and affected entity (attacker or defender) are determined by several factors.

- "blockingoverhaul:block_force" entity attribute of the defender
- "blockingoverhaul:parry_multiplier" entity attribute of the defender. This multiplier can be disabled by the server config and/or the item used for blocking.
- "minecraft:attack_knockback" entity attribute of the defender
- an "additional_attack_knockback", which is 0 by default. Other mods that use Blocking Overhaul (like Overhauled Damage) can use this value to modify the calculation
- a "total_applied_blocking_knockback_multiplier", which is a server config option

> ((defender_block_force * defender_parry_multiplier) - (attacker_attack_knockback + additional_attack_knockback)) * total_applied_blocking_knockback_multiplier

If the result of the calculation is negative, the knockback is applied to the defender. If it's positive, the knockback is applied to the attacker.

## Changes to blocked damage amount

Parrying can also apply the multiplier determined by the "blockingoverhaul:parry_multiplier" entity attribute to the amount of blocked damage. This can be disabled by the server config and/or the item used for blocking.

## Technical details

This data component needs to be present on an item stack for parrying to work.

Parrying has several conditions:

- the item used for blocking has to have the "blockingoverhaul:parries_attacks" data component
  - there is currently no easy way to apply this component to existing items (like the vanilla shield) by default
  - using third-party mods like "Default Components" is recommended
- the entity (type) that tries to parry has to be in the "blockingoverhaul:can_parry" entity type tag

## Stamina Attributes integration

Blocking Overhaul has integration for Stamina Attributes, parrying an attack has a different stamina cost than regular blocking, determined by the "blockingoverhaul:parry_stamina_cost" entity attribute.