package com.github.theredbrain.blockingoverhaul;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlockingOverhaul implements ModInitializer {
	public static final String MOD_ID = "blockingoverhaul";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Blocking attacks was overhauled!");
	}
}