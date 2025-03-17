package io.github.furromaid.handlers;

import io.github.furromaid.SoundMod;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class SoundHandler {
    public static final Identifier SIREN_1 = new Identifier("soundmod", "siren_1");
    public static SoundEvent SIREN_1_EVENT = SoundEvent.of(SIREN_1);
    public static final Identifier SIREN_2 = new Identifier("soundmod", "siren_2");
    public static SoundEvent SIREN_2_EVENT = SoundEvent.of(SIREN_2);

    public static void registerSounds() {
        Registry.register(Registries.SOUND_EVENT, SIREN_1, SIREN_1_EVENT);
        Registry.register(Registries.SOUND_EVENT, SIREN_2, SIREN_2_EVENT);
        SoundMod.LOGGER.info("//!// Registered Sounds //!//");
    }
}
