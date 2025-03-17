package io.github.furromaid;

import io.github.furromaid.blocks.SirenPlayerBlockEntity;
import io.github.furromaid.gui.SoundSelectorScreen;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class ModPackets {
    public static final Identifier SOUND_SELECT_PACKET_ID = new Identifier("soundmod", "sound_select");
    public static final Identifier OPEN_GUI_PACKET_ID = new Identifier("soundmod", "open_gui");

    public static void register() {
        // Server-side reception of sound selection
        ServerPlayNetworking.registerGlobalReceiver(SOUND_SELECT_PACKET_ID,
                (server, player, handler, buf, responseSender) -> {
                    BlockPos pos = buf.readBlockPos();
                    Identifier soundId = buf.readIdentifier();

                    server.execute(() -> {
                        if (player.getWorld().getBlockEntity(pos) instanceof SirenPlayerBlockEntity be) {
                            be.setSelectedSound(soundId);
                        }
                    });
                });

        // Client-side GUI opening
        ClientPlayNetworking.registerGlobalReceiver(OPEN_GUI_PACKET_ID,
                (client, handler, buf, responseSender) -> {
                    BlockPos pos = buf.readBlockPos();
                    client.execute(() -> {
                        client.setScreen(new SoundSelectorScreen(pos));
                    });
                });
    }
}
