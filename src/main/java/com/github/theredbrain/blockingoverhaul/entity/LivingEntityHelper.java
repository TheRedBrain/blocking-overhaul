package com.github.theredbrain.blockingoverhaul.entity;

import com.github.theredbrain.blockingoverhaul.BlockingOverhaul;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class LivingEntityHelper {

    public static boolean canParry(LivingEntity livingEntity, DamageSource damageSource, ItemStack shieldItemStack) {
        return livingEntity.getType().is(BlockingOverhaul.CAN_PARRY) && ((DuckLivingEntityMixin) livingEntity).blockingoverhaul$getBlockingTime() <= ((DuckLivingEntityMixin) livingEntity).blockingoverhaul$getParryWindow() && damageSource.getEntity() != null && damageSource.getEntity() instanceof LivingEntity && shieldItemStack.has(BlockingOverhaul.PARRIES_ATTACKS);
    }

}
