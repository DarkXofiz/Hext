package com.example.cheat;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class ModMenuScreen extends Screen {

    public ModMenuScreen() {
        super(Text.literal("Hext Menu"));
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int startY = this.height / 2 - 60;
        int spacing = 24;

        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("Coordinate HUD: " + (HudState.showCoords ? "ON" : "OFF")),
                button -> {
                    HudState.showCoords = !HudState.showCoords;
                    button.setMessage(Text.literal("Coordinate HUD: " + (HudState.showCoords ? "ON" : "OFF")));
                })
                .dimensions(centerX - 100, startY, 200, 20)
                .build());

        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("Fly (Creative-like): " + (HudState.flyEnabled ? "ON" : "OFF")),
                button -> {
                    HudState.flyEnabled = !HudState.flyEnabled;
                    if (this.client != null && this.client.player != null) {
                        this.client.player.getAbilities().flying = HudState.flyEnabled;
                        this.client.player.getAbilities().allowFlying = HudState.flyEnabled;
                        this.client.player.sendAbilitiesUpdate();
                    }
                    button.setMessage(Text.literal("Fly (Creative-like): " + (HudState.flyEnabled ? "ON" : "OFF")));
                })
                .dimensions(centerX - 100, startY + spacing, 200, 20)
                .build());

        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("Zoom: " + (HudState.zoomEnabled ? "ON" : "OFF")),
                button -> {
                    HudState.zoomEnabled = !HudState.zoomEnabled;
                    button.setMessage(Text.literal("Zoom: " + (HudState.zoomEnabled ? "ON" : "OFF")));
                })
                .dimensions(centerX - 100, startY + spacing * 2, 200, 20)
                .build());

        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("Kapat"),
                button -> this.close())
                .dimensions(centerX - 100, startY + spacing * 4, 200, 20)
                .build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, "Hext Menu", this.width / 2, this.height / 2 - 90, 0xFFFFFF);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
