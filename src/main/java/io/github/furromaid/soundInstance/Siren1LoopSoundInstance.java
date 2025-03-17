package io.github.furromaid.soundInstance;

import net.minecraft.client.sound.AbstractSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.tick.Tick;

public class Siren1LoopSoundInstance extends AbstractSoundInstance {
    public Siren1LoopSoundInstance(SoundEvent soundEvent, BlockPos pos) {
        super(soundEvent, SoundCategory.BLOCKS, SoundInstance.createRandom());
        this.x = pos.getX() + 0.5;
        this.y = pos.getY() + 0.5;
        this.z = pos.getZ() + 0.5;
        this.repeat = false;
        this.repeatDelay = 0;
    }

    @Override
    public boolean canPlay() {
        return true;
    }
}
