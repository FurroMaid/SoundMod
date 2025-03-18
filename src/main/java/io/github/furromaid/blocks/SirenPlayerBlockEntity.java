package io.github.furromaid.blocks;

import io.github.furromaid.handlers.BlockHandler;
import io.github.furromaid.handlers.SoundHandler;
import io.github.furromaid.soundInstance.Siren1LoopSoundInstance;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Set;

public class SirenPlayerBlockEntity extends BlockEntity {
    private int tickCount = 899;
    private Identifier selectedSound = new Identifier("soundmod:moon_siren");
    private float volume = 1.0f;
    private float pitch = 1.0f;
    private boolean loopable = false;
    private int radius = 16;
    private Siren1LoopSoundInstance activeSound;
    private final Set<PlayerEntity> playersInRange = new HashSet<>();

    public SirenPlayerBlockEntity(BlockPos pos, BlockState state) {
        super(BlockHandler.SIREN_PLAYER_ENTITY, pos, state);
    }

    public void setSelectedSound(Identifier soundId) {
        this.selectedSound = soundId;
        markDirty();
    }

    public Identifier getSelectedSound() {
        return selectedSound;
    }

    public void setVolume(float volume) {
        this.volume = volume;
        markDirty();
    }

    public float getVolume() {
        return volume;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
        markDirty();
    }

    public float getPitch() {
        return pitch;
    }

    public void setLoopable(boolean loopable) {
        this.loopable = loopable;
        markDirty();
    }

    public boolean isLoopable() {
        return loopable;
    }

    public void setRadius(int radius) {
        this.radius = radius;
        markDirty();
    }

    public int getRadius() {
        return radius;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        selectedSound = new Identifier(nbt.getString("selectedSound"));
        volume = nbt.getFloat("volume");
        pitch = nbt.getFloat("pitch");
        loopable = nbt.getBoolean("loopable");
        radius = nbt.getInt("radius");
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putString("selectedSound", selectedSound.toString());
        nbt.putFloat("volume", volume);
        nbt.putFloat("pitch", pitch);
        nbt.putBoolean("loopable", loopable);
        nbt.putInt("radius", radius);
    }

    public void tick(World world, BlockPos pos, BlockState state, SirenPlayerBlockEntity be) {
        if (world.isClient) return;

        boolean isPowered = state.get(SirenPlayer.POWERED);

        tickCount++;

        if (tickCount >= 900 && isPowered) {
            tickCount = 0;
            System.out.println("Starting sound at " + pos);
            SoundEvent soundEvent = getSoundEvent(be.getSelectedSound());

            if (soundEvent != null) {
                be.activeSound = new Siren1LoopSoundInstance(soundEvent, pos, volume, pitch, loopable);
                playSoundForPlayersInRadius(world, pos, be.activeSound, radius);
            } else {
                System.out.println("No sound found for " + be.getSelectedSound());
            }
        }

        updatePlayersInRange(world, pos, radius);
    }

    private SoundEvent getSoundEvent(Identifier soundId) {
        if (soundId.equals(new Identifier("soundmod:moon_siren"))) {
            return SoundHandler.SIREN_1_EVENT;
        } else if (soundId.equals(new Identifier("soundmod:wawa"))) {
            return SoundHandler.SIREN_2_EVENT;
        }
        return null;
    }

    private void playSoundForPlayersInRadius(World world, BlockPos pos, Siren1LoopSoundInstance sound, int radius) {
        for (PlayerEntity player : world.getPlayers()) {
            if (player.getBlockPos().isWithinDistance(pos, radius)) {
                if (!playersInRange.contains(player)) {
                    MinecraftClient.getInstance().getSoundManager().play(sound);
                    playersInRange.add(player);
                }
            }
        }
    }

    private void updatePlayersInRange(World world, BlockPos pos, int radius) {
        Set<PlayerEntity> currentPlayersInRange = new HashSet<>();
        for (PlayerEntity player : world.getPlayers()) {
            if (player.getBlockPos().isWithinDistance(pos, radius)) {
                currentPlayersInRange.add(player);
                if (!playersInRange.contains(player)) {
                    if (activeSound != null) {
                        MinecraftClient.getInstance().getSoundManager().play(activeSound);
                    }
                    playersInRange.add(player);
                }
            } else if (playersInRange.contains(player)) {
                playersInRange.remove(player);
            }
        }
        if (playersInRange.isEmpty() && activeSound != null) {
            MinecraftClient.getInstance().getSoundManager().stop(activeSound);
        }
        playersInRange.retainAll(currentPlayersInRange);
    }

    @Override
    public void markRemoved() {
        super.markRemoved();
        if (activeSound != null) {
            MinecraftClient.getInstance().getSoundManager().stop(activeSound);
        }
    }
}