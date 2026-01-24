package com.github.theredbrain.blockingoverhaul.registry;

import com.github.theredbrain.blockingoverhaul.BlockingOverhaul;
import com.github.theredbrain.blockingoverhaul.component.type.ParriesAttacksDataComponent;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;

public class DataComponentTypeRegistry {
	static {
		BlockingOverhaul.PARRIES_ATTACKS = Registry.register(
				BuiltInRegistries.DATA_COMPONENT_TYPE,
				BlockingOverhaul.identifier("parries_attacks"),
				DataComponentType.<ParriesAttacksDataComponent>builder().persistent(ParriesAttacksDataComponent.CODEC).networkSynchronized(ParriesAttacksDataComponent.STREAM_CODEC).build()
		);
	}

	public static void init() {
	}
}
