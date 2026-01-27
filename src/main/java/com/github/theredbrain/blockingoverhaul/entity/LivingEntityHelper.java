package com.github.theredbrain.blockingoverhaul.entity;

import com.github.theredbrain.blockingoverhaul.BlockingOverhaul;
import com.github.theredbrain.blockingoverhaul.component.type.ParriesAttacksDataComponent;
import com.github.theredbrain.blockingoverhaul.config.ServerConfig;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class LivingEntityHelper {

	public static boolean canParry(LivingEntity livingEntity, DamageSource damageSource, ItemStack shieldItemStack) {
		return livingEntity.getType().is(BlockingOverhaul.CAN_PARRY) && ((DuckLivingEntityMixin) livingEntity).blockingoverhaul$getBlockingTime() <= ((DuckLivingEntityMixin) livingEntity).blockingoverhaul$getParryWindow() && damageSource.getEntity() != null && damageSource.getEntity() instanceof LivingEntity && shieldItemStack.has(BlockingOverhaul.PARRIES_ATTACKS);
	}

	public static void applyOverhauledItemBlocking(LivingEntity defender, LivingEntity attacker, DamageSource damageSource, ItemStack blockingItemStack) {
		Vec3 vec3 = defender.getDeltaMovement();
		boolean parried = canParry(defender, damageSource, blockingItemStack);

		// stamina cost
		BlockingOverhaul.applyBlockAttackStaminaCost(defender, parried);

		// knockback
		double applied_knock_back = getAppliedBlockingKnockback(defender, attacker, blockingItemStack, parried, 0.0);
		if (applied_knock_back != 0.0) {
			if (applied_knock_back > 0.0) {
				attacker.knockback(Math.abs(applied_knock_back), defender.getX() - attacker.getX(), defender.getZ() - attacker.getZ());

				if (attacker instanceof ServerPlayer) {
					((ServerPlayer) attacker).connection.send(new ClientboundSetEntityMotionPacket(attacker));
					attacker.setDeltaMovement(vec3);
				}
			} else {
				defender.knockback(Math.abs(applied_knock_back), attacker.getX() - defender.getX(), attacker.getZ() - defender.getZ());

				if (defender instanceof ServerPlayer) {
					((ServerPlayer) defender).connection.send(new ClientboundSetEntityMotionPacket(defender));
					defender.setDeltaMovement(vec3);
				}
			}
		}
	}

	public static double getAppliedBlockingKnockback(LivingEntity defender, LivingEntity attacker, ItemStack blockingItemStack, boolean parried, double additionalAttackKnockback) {
		ServerConfig serverConfig = BlockingOverhaul.SERVER_CONFIG;
		double parryBlockForceMultiplier = parried && blockingItemStack.getOrDefault(BlockingOverhaul.PARRIES_ATTACKS, ParriesAttacksDataComponent.DEFAULT).multiplier_applies_to_knockback() && serverConfig.parrying_multiplies_knockback.get() ? ((DuckLivingEntityMixin) defender).blockingoverhaul$getParryMultiplier() : 1.0;
		return ((((DuckLivingEntityMixin) defender).blockingoverhaul$getBlockForce() * parryBlockForceMultiplier) - (attacker.getAttributeValue(Attributes.ATTACK_KNOCKBACK) + additionalAttackKnockback)) * serverConfig.total_block_force_multiplier.get();
	}
}
