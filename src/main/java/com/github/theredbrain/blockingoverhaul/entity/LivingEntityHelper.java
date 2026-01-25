package com.github.theredbrain.blockingoverhaul.entity;

import com.github.theredbrain.blockingoverhaul.BlockingOverhaul;
import com.github.theredbrain.blockingoverhaul.component.type.ParriesAttacksDataComponent;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
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

	public static void applyBlockingKnockback(ServerLevel world, LivingEntity defender, LivingEntity attacker, DamageSource damageSource, ItemStack blockingItemStack) {
		Vec3 vec3 = defender.getDeltaMovement();
		boolean parried = LivingEntityHelper.canParry(defender, damageSource, blockingItemStack);
		double parryBlockForceMultiplier = parried && blockingItemStack.getOrDefault(BlockingOverhaul.PARRIES_ATTACKS, ParriesAttacksDataComponent.DEFAULT).multiplier_applies_to_knockback() ? ((DuckLivingEntityMixin) defender).blockingoverhaul$getParryMultiplier() : 1.0;
		BlockingOverhaul.applyBlockAttackStaminaCost(defender, parried);
		double applied_knock_back = ((((DuckLivingEntityMixin) defender).blockingoverhaul$getBlockForce() * parryBlockForceMultiplier) - attacker.getAttributeValue(Attributes.ATTACK_KNOCKBACK)) * BlockingOverhaul.SERVER_CONFIG.total_block_force_multiplier.get();
		if (applied_knock_back != 0.0) {
			if (BlockingOverhaul.SERVER_CONFIG.knockback_from_blocking_always_targets_attacker.get() || applied_knock_back > 0.0) {
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
}
