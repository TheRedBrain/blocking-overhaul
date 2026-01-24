package com.github.theredbrain.blockingoverhaul.mixin.entity.attribute;

import com.github.theredbrain.blockingoverhaul.BlockingOverhaul;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Attributes.class)
public class EntityAttributesMixin {
    static {
        BlockingOverhaul.BLOCK_FORCE = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, BlockingOverhaul.identifier("block_force"), new RangedAttribute("attribute.name.block_force", 0.0, 0.0, 1024.0).setSyncable(true));
        BlockingOverhaul.PARRY_MULTIPLIER = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, BlockingOverhaul.identifier("parry_multiplier"), new RangedAttribute("attribute.name.parry_bonus", 1.0, 0.0, 1024.0).setSyncable(true));
        BlockingOverhaul.PARRY_WINDOW = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, BlockingOverhaul.identifier("parry_window"), new RangedAttribute("attribute.name.parry_window", 0.0, 0.0, 1024.0).setSyncable(true));

        BlockingOverhaul.PARRY_STAMINA_COST = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, BlockingOverhaul.identifier("parry_stamina_cost"), new RangedAttribute("attribute.name.parry_stamina_cost", 0.0, 0.0, 1024.0).setSyncable(true));
    }
}
