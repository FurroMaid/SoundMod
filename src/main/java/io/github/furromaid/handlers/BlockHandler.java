package io.github.furromaid.handlers;

import io.github.furromaid.blocks.SirenPlayer;
import io.github.furromaid.blocks.SirenPlayerBlockEntity;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class BlockHandler {
    public static final String MOD_ID = "soundmod";

    public static final Block SIREN_PLAYER = new SirenPlayer(FabricBlockSettings.create().mapColor(MapColor.GRAY).strength(0.5F, 0.05F).sounds(BlockSoundGroup.METAL).luminance(state -> state.get(SirenPlayer.POWERED) ? 3 : 0));
    public static BlockEntityType<SirenPlayerBlockEntity> SIREN_PLAYER_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(MOD_ID, "siren"), FabricBlockEntityTypeBuilder.create(SirenPlayerBlockEntity::new, SIREN_PLAYER).build());

    public static void registerBlocks() {
        Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "siren"), SIREN_PLAYER);
    }

    public static void registerBlockItems() {
        Registry.register(Registries.ITEM, new Identifier(MOD_ID, "siren"), new BlockItem(SIREN_PLAYER, new FabricItemSettings()));
    }

    public static final BlockEntityTicker<SirenPlayerBlockEntity> TICKER = (world, pos, state, blockEntity) -> {
        if (!world.isClient) {
            blockEntity.tick(world, pos, state, blockEntity);
        }
    };
}
