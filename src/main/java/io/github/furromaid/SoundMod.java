package io.github.furromaid;

import io.github.furromaid.blocks.SirenPlayerBlockEntity;
import io.github.furromaid.handlers.BlockHandler;
import io.github.furromaid.handlers.SoundHandler;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SoundMod implements ModInitializer {
	public static final String MOD_ID = "soundmod";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world!");

		BlockHandler.registerBlocks();
		BlockHandler.registerBlockItems();
		LOGGER.info("//!// Registering Sounds //!//");
		SoundHandler.registerSounds();
		ModPackets.register();
	}
}