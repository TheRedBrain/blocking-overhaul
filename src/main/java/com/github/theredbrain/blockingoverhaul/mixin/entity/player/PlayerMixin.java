package com.github.theredbrain.blockingoverhaul.mixin.entity.player;

import com.github.theredbrain.blockingoverhaul.entity.PlayerHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {

	protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level world) {
		super(entityType, world);
	}

	@Inject(method = "tick", at = @At("TAIL"))
	public void staminaattributes$tick(CallbackInfo ci) {
		if (!this.level().isClientSide()) {
			this.getAttributes().addTransientAttributeModifiers(PlayerHelper.getNaturalAttributeModifiers());
		}
	}

}
