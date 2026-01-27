package com.github.theredbrain.blockingoverhaul;

import com.github.theredbrain.blockingoverhaul.compatibility.OverhauledDamageIntegration;
import com.github.theredbrain.blockingoverhaul.compatibility.StaminaAttributesIntegration;
import com.github.theredbrain.blockingoverhaul.component.type.ParriesAttacksDataComponent;
import com.github.theredbrain.blockingoverhaul.config.ServerConfig;
import com.github.theredbrain.blockingoverhaul.registry.DataComponentTypeRegistry;
import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.api.RegisterType;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BlocksAttacks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlockingOverhaul implements ModInitializer {
	public static final String MOD_ID = "blockingoverhaul";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static ServerConfig SERVER_CONFIG;

	public static final TagKey<EntityType<?>> CAN_PARRY = TagKey.create(Registries.ENTITY_TYPE, identifier("can_parry"));

	public static Holder<Attribute> BLOCK_FORCE;
	public static Holder<Attribute> PARRY_MULTIPLIER;
	public static Holder<Attribute> PARRY_WINDOW;

	public static Holder<Attribute> PARRY_STAMINA_COST;

	public static DataComponentType<ParriesAttacksDataComponent> PARRIES_ATTACKS;

	public static final boolean isStaminaAttributesLoaded = FabricLoader.getInstance().isModLoaded("staminaattributes");
	public static final boolean isOverhauledDamageLoaded = FabricLoader.getInstance().isModLoaded("overhauleddamage");

	public static float getCurrentStamina(LivingEntity livingEntity) {
		float currentStamina = 0.0F;
		if (isStaminaAttributesLoaded) {
			currentStamina = StaminaAttributesIntegration.getCurrentStamina(livingEntity);
		}
		return currentStamina;
	}

	public static void addStamina(LivingEntity livingEntity, float amount) {
		if (isStaminaAttributesLoaded) {
			StaminaAttributesIntegration.addStamina(livingEntity, amount);
		}
	}

	public static void playBlockingSoundEvent(ServerLevel serverLevel, LivingEntity livingEntity, ItemStack itemStack, boolean parried) {
		if (parried) {
			itemStack.getOrDefault(BlockingOverhaul.PARRIES_ATTACKS, ParriesAttacksDataComponent.DEFAULT).onParried(serverLevel, livingEntity);
		} else {
			BlocksAttacks blocksAttacks = itemStack.get(DataComponents.BLOCKS_ATTACKS);
			if (blocksAttacks != null) {
				blocksAttacks.onBlocked(serverLevel, livingEntity);
			}
		}
	}

	public static boolean currentStaminaAllowsBlocking(LivingEntity livingEntity) {
		if (isStaminaAttributesLoaded) {
			return getCurrentStamina(livingEntity) > 0 || !StaminaAttributesIntegration.blockingRequiresStamina();
		}
		return true;
	}

	public static void applyBlockAttackStaminaCost(LivingEntity livingEntity, boolean parried) {
		if (isStaminaAttributesLoaded) {
			StaminaAttributesIntegration.applyBlockAttackStaminaCost(livingEntity, parried);
		}
	}

	public static boolean isOverhauledDamageOverrideActive() {
		if (isOverhauledDamageLoaded) {
			return OverhauledDamageIntegration.isBlockingOverhaulActive();
		}
		return false;
	}

	@Override
	public void onInitialize() {
		LOGGER.info("Blocking attacks was overhauled!");
		SERVER_CONFIG = ConfigApiJava.registerAndLoadConfig(ServerConfig::new, RegisterType.BOTH);

		DataComponentTypeRegistry.init();
	}

	public static Identifier identifier(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}

	public static void info(String message) {
		LOGGER.info("[" + MOD_ID + "] [info]: {}", message);
	}

}