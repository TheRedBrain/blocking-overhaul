package com.github.theredbrain.blockingoverhaul.compatibility;

import com.github.theredbrain.blockingoverhaul.entity.DuckLivingEntityMixin;
import com.github.theredbrain.staminaattributes.StaminaAttributes;
import com.github.theredbrain.staminaattributes.entity.StaminaUsingEntity;
import net.minecraft.world.entity.LivingEntity;

public class StaminaAttributesIntegration {

    public static float getCurrentStamina(LivingEntity livingEntity) {
        return ((StaminaUsingEntity) livingEntity).staminaattributes$getStamina();
    }

    public static void addStamina(LivingEntity livingEntity, float amount) {
        ((StaminaUsingEntity) livingEntity).staminaattributes$addStamina(amount);
    }

    public static boolean blockingRequiresStamina() {
		return StaminaAttributes.SERVER_CONFIG.enable_attack_blocking_stamina_cost;
    }

    public static void applyBlockAttackStaminaCost(LivingEntity livingEntity, boolean parried) {
        if (parried) {
            addStamina(livingEntity, -((DuckLivingEntityMixin) livingEntity).blockingoverhaul$getParryStaminaCost());
        } else {
        	addStamina(livingEntity, -((StaminaUsingEntity) livingEntity).staminaattributes$getAttackBlockingActionStaminaCost());
        }
    }

}
