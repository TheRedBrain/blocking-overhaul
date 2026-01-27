package com.github.theredbrain.blockingoverhaul.mixin.entity;

import com.github.theredbrain.blockingoverhaul.BlockingOverhaul;
import com.github.theredbrain.blockingoverhaul.component.type.ParriesAttacksDataComponent;
import com.github.theredbrain.blockingoverhaul.config.ServerConfig;
import com.github.theredbrain.blockingoverhaul.entity.DuckLivingEntityMixin;
import com.github.theredbrain.blockingoverhaul.entity.LivingEntityHelper;
import com.google.common.collect.HashMultimap;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BlocksAttacks;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements DuckLivingEntityMixin {

    @Shadow
    public abstract double getAttributeValue(Holder<Attribute> attribute);

    @Shadow
    public abstract AttributeMap getAttributes();

    @Shadow
    public abstract boolean isBlocking();

    @Unique
    private int blockingTime = 0;

    public LivingEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Inject(method = "createLivingAttributes", at = @At("RETURN"))
    private static void blockingoverhaul$createLivingAttributes(CallbackInfoReturnable<AttributeSupplier.Builder> cir) {
        cir.getReturnValue()
                .add(BlockingOverhaul.BLOCK_FORCE)
                .add(BlockingOverhaul.PARRY_MULTIPLIER)
                .add(BlockingOverhaul.PARRY_WINDOW)

                .add(BlockingOverhaul.PARRY_STAMINA_COST)
        ;
    }

    @WrapOperation(method = "applyItemBlocking", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;blockUsingItem(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;)V"))
    protected void blockingoverhaul$takeShieldHit(LivingEntity instance, ServerLevel world, LivingEntity attacker, Operation<Void> original, @Local(argsOnly = true) DamageSource source, @Local ItemStack itemStack) {
        if (!BlockingOverhaul.isOverhauledDamageOverrideActive()) {
            if (BlockingOverhaul.SERVER_CONFIG.enable_blocking_overhaul.get()) {
                LivingEntityHelper.applyOverhauledItemBlocking(instance, attacker, source, itemStack);
            } else {
                original.call(instance, world, attacker);
            }
        }
    }

    @WrapOperation(
            method = "hurtServer",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;applyItemBlocking(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)F")
    )
    public float blockingoverhaul$wrap_applyItemBlocking(LivingEntity instance, ServerLevel world, DamageSource source, float amount, Operation<Float> original, @Local ItemStack itemStack) {

		float parryMultiplier = 1.0F;

        if (BlockingOverhaul.SERVER_CONFIG.enable_blocking_overhaul.get()) {
			if (BlockingOverhaul.isOverhauledDamageOverrideActive() || !BlockingOverhaul.currentStaminaAllowsBlocking(instance)) {
				return 0.0F;
			} else {
				if (BlockingOverhaul.SERVER_CONFIG.parrying_multiplies_blocked_damage.get() && LivingEntityHelper.canParry(instance, source, itemStack) && itemStack.getOrDefault(BlockingOverhaul.PARRIES_ATTACKS, ParriesAttacksDataComponent.DEFAULT).multiplier_applies_to_damage()) {
					parryMultiplier = ((DuckLivingEntityMixin)instance).blockingoverhaul$getParryMultiplier();
				}
			}
        }
		return original.call(instance, world, source, amount) * parryMultiplier;
	}

    @WrapOperation(
            method = "hurtServer",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/component/BlocksAttacks;onBlocked(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;)V")
    )
    public void blockingoverhaul$wrap_onBlocked(BlocksAttacks instance, ServerLevel serverLevel, LivingEntity livingEntity, Operation<Void> original, @Local(argsOnly = true) DamageSource damageSource, @Local ItemStack itemStack) {
        if (BlockingOverhaul.SERVER_CONFIG.enable_blocking_overhaul.get()) {
            BlockingOverhaul.playBlockingSoundEvent(serverLevel, livingEntity, itemStack, LivingEntityHelper.canParry(livingEntity, damageSource, itemStack));
        } else {
            original.call(instance, serverLevel, livingEntity);
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void blockingoverhaul$tick(CallbackInfo ci) {
        if (!this.level().isClientSide()) {
            // apply natural attribute modifiers
            this.getAttributes().addTransientAttributeModifiers(getNaturalAttributeModifiers());
            // update blocking time
            if (this.isBlocking()) {
                this.blockingoverhaul$setBlockingTime(this.blockingoverhaul$getBlockingTime() + 1);
            } else if (this.blockingoverhaul$getBlockingTime() > 0) {
                this.blockingoverhaul$setBlockingTime(0);
            }
        }
    }

    @Override
    public float blockingoverhaul$getBlockForce() {
        return (float) this.getAttributeValue(BlockingOverhaul.BLOCK_FORCE);
    }

    @Override
    public float blockingoverhaul$getParryMultiplier() {
        return (float) this.getAttributeValue(BlockingOverhaul.PARRY_MULTIPLIER);
    }

    @Override
    public float blockingoverhaul$getParryWindow() {
        return (float) this.getAttributeValue(BlockingOverhaul.PARRY_WINDOW);
    }

    @Override
    public float blockingoverhaul$getParryStaminaCost() {
        return (float) this.getAttributeValue(BlockingOverhaul.PARRY_STAMINA_COST);
    }

    @Override
    public int blockingoverhaul$getBlockingTime() {
        return this.blockingTime;
    }

    @Override
    public void blockingoverhaul$setBlockingTime(int blockingTime) {
        this.blockingTime = blockingTime;
    }

    @Unique
    private HashMultimap<Holder<Attribute>, AttributeModifier> getNaturalAttributeModifiers() {
        ServerConfig serverConfig = BlockingOverhaul.SERVER_CONFIG;
        HashMultimap<Holder<Attribute>, AttributeModifier> hashMultimap = HashMultimap.create();
        hashMultimap.put(BlockingOverhaul.BLOCK_FORCE, new AttributeModifier(BlockingOverhaul.identifier("natural_block_force_modifier"), serverConfig.natural_block_force.get(), AttributeModifier.Operation.ADD_VALUE));
        hashMultimap.put(BlockingOverhaul.PARRY_MULTIPLIER, new AttributeModifier(BlockingOverhaul.identifier("natural_parry_multiplier_modifier"), serverConfig.natural_parry_multiplier.get(), AttributeModifier.Operation.ADD_VALUE));
        hashMultimap.put(BlockingOverhaul.PARRY_WINDOW, new AttributeModifier(BlockingOverhaul.identifier("natural_parry_window_modifier"), serverConfig.natural_parry_window.get(), AttributeModifier.Operation.ADD_VALUE));
        hashMultimap.put(BlockingOverhaul.PARRY_STAMINA_COST, new AttributeModifier(BlockingOverhaul.identifier("natural_action_stamina_cost_attack_parrying_modifier"), serverConfig.natural_action_stamina_cost_attack_parrying.get(), AttributeModifier.Operation.ADD_VALUE));
        return hashMultimap;
    }

}
