package io.github.furromaid.blocks;

import io.github.furromaid.ModPackets;
import io.github.furromaid.SoundMod;
import io.github.furromaid.gui.SoundSelectorScreenHandler;
import io.github.furromaid.handlers.BlockEntitiesHandler;
import io.github.furromaid.handlers.BlockHandler;
import io.github.furromaid.handlers.SoundHandler;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.impl.networking.v0.OldNetworkingHooks;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.sound.Sound;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class SirenPlayer extends BlockWithEntity {
    public static final BooleanProperty POWERED = BooleanProperty.of("powered");

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
    }

    public SirenPlayer(Settings settings) {
        super(settings);
        this.setDefaultState(getStateManager().getDefaultState().with(POWERED, false));
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
            boolean isPowered = world.isReceivingRedstonePower(pos);

            if (isPowered != state.get(POWERED)) {
                world.setBlockState(pos, state.with(POWERED, isPowered), Block.NOTIFY_ALL);
            }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            ServerPlayNetworking.send(
                    (ServerPlayerEntity) player,
                    ModPackets.OPEN_GUI_PACKET_ID,
                    PacketByteBufs.create().writeBlockPos(pos)
            );
            player.sendMessage(Text.of("Opened GUI"), false);
        }
        return ActionResult.success(world.isClient);
    }


    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new SirenPlayerBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return type == BlockHandler.SIREN_PLAYER_ENTITY ? (BlockEntityTicker<T>) BlockHandler.TICKER : null;
    }
}
