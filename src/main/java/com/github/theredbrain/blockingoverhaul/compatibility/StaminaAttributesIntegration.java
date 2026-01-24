package com.github.theredbrain.blockingoverhaul.compatibility;

import com.github.theredbrain.blockingoverhaul.entity.DuckLivingEntityMixin;
import net.minecraft.world.entity.LivingEntity;

public class StaminaAttributesIntegration {

    public static float getCurrentStamina(LivingEntity livingEntity) {
        return 0.0F;//((StaminaUsingEntity) livingEntity).staminaattributes$getStamina();
    }

    public static void addStamina(LivingEntity livingEntity, float amount) {
//        ((StaminaUsingEntity) livingEntity).staminaattributes$addStamina(amount);
    }

    public static void applyBlockAttackStaminaCost(LivingEntity livingEntity, boolean parried) {
        if (parried) {
            addStamina(livingEntity, ((DuckLivingEntityMixin) livingEntity).blockingoverhaul$getParryStaminaCost());
        } else {
//        addStamina(livingEntity, ((StaminaUsingEntity) livingEntity).staminaattributes$getAttackBlockingActionStaminaCost);
        }
    }

}
