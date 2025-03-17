package io.github.furromaid.gui;

import io.github.furromaid.ModPackets;
import io.github.furromaid.handlers.SoundHandler;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundEntry;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class SoundSelectorScreen extends Screen {
    private final BlockPos blockPos;
    private SoundListWidget soundList;

    public SoundSelectorScreen(BlockPos pos) {
        super(Text.translatable("gui.soundmod.title"));
        this.blockPos = pos;
    }

    @Override
    protected void init() {
        super.init();
        this.soundList = new SoundListWidget(
                client,
                width,
                height,
                16, // Top padding
                height - 16, // Bottom padding
                20, // Item height
                blockPos);
        this.addSelectableChild(soundList);
        this.setInitialFocus(soundList);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        renderBackground(context);
        soundList.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        super.close();
        // Optional: Play close sound
        client.getSoundManager().play(
                PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0f)
        );
    }

    static class SoundListWidget extends ElementListWidget<SoundListWidget.SoundEntry> {
        private final BlockPos blockPos;

        @Override
        public void appendNarrations(NarrationMessageBuilder builder) {
            super.appendNarrations(builder);
            builder.put(NarrationPart.TITLE, Text.translatable("gui.soundmod.select_sound"));
        }

        public SoundListWidget(MinecraftClient client, int width, int height, int top,
                               int bottom, int itemHeight, BlockPos pos) {
            super(client, width, height, top, bottom, itemHeight);
            this.blockPos = pos;

            addEntry(new SoundEntry(new Identifier("soundmod:moon_siren"), pos));
            addEntry(new SoundEntry(new Identifier("soundmod:wawa"), pos));
        }

        public class SoundEntry extends ElementListWidget.Entry<SoundEntry> {
            private final Identifier soundId;
            private final BlockPos pos;

            public SoundEntry(Identifier soundId, BlockPos pos) {
                this.soundId = soundId;
                this.pos = pos;
            }

            @Override
            public void render(DrawContext context, int index, int y, int x,
                               int entryWidth, int entryHeight, int mouseX,
                               int mouseY, boolean hovered, float tickDelta) {
                // Fixed color value (6 digits) and added proper text rendering
                context.drawText(client.textRenderer,
                        soundId.toString(),
                        x + 4,
                        y + 2,
                        0xFFFFFF, // Correct white color
                        false
                );
            }

            @Override
            public List<? extends Element> children() {
                return List.of();
            }

            @Override
            public boolean mouseClicked(double mouseX, double mouseY, int button) {
                // Get reference to parent screen
                if (client.currentScreen instanceof SoundSelectorScreen screen) {
                    PacketByteBuf buf = PacketByteBufs.create();
                    buf.writeBlockPos(pos);
                    buf.writeIdentifier(soundId);

                    ClientPlayNetworking.send(ModPackets.SOUND_SELECT_PACKET_ID, buf);
                    screen.close();

                    return true;
                }
                return false;
            }

            @Override
            public List<? extends Selectable> selectableChildren() {
                return List.of();
            }
        }
    }

}
