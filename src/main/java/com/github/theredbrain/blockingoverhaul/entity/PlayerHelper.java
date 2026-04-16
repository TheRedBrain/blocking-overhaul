package com.github.theredbrain.blockingoverhaul.entity;

import com.github.theredbrain.blockingoverhaul.BlockingOverhaul;
import com.github.theredbrain.blockingoverhaul.config.ServerConfig;
import com.google.common.collect.HashMultimap;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class PlayerHelper {

	public static HashMultimap<Holder<Attribute>, AttributeModifier> getNaturalAttributeModifiers() {
		ServerConfig serverConfig = BlockingOverhaul.SERVER_CONFIG;
		HashMultimap<Holder<Attribute>, AttributeModifier> hashMultimap = HashMultimap.create();
		hashMultimap.put(BlockingOverhaul.BLOCK_FORCE, new AttributeModifier(BlockingOverhaul.identifier("natural_block_force_modifier"), serverConfig.naturalPlayerAttributeValues.natural_block_force.get(), AttributeModifier.Operation.ADD_VALUE));
		hashMultimap.put(BlockingOverhaul.PARRY_MULTIPLIER, new AttributeModifier(BlockingOverhaul.identifier("natural_parry_multiplier_modifier"), serverConfig.naturalPlayerAttributeValues.natural_parry_multiplier.get(), AttributeModifier.Operation.ADD_VALUE));
		hashMultimap.put(BlockingOverhaul.PARRY_WINDOW, new AttributeModifier(BlockingOverhaul.identifier("natural_parry_window_modifier"), serverConfig.naturalPlayerAttributeValues.natural_parry_window.get(), AttributeModifier.Operation.ADD_VALUE));
		hashMultimap.put(BlockingOverhaul.PARRY_STAMINA_COST, new AttributeModifier(BlockingOverhaul.identifier("natural_action_stamina_cost_attack_parrying_modifier"), serverConfig.naturalPlayerAttributeValues.natural_action_stamina_cost_attack_parrying.get(), AttributeModifier.Operation.ADD_VALUE));
		return hashMultimap;
	}
}
