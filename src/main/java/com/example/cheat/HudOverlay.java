package com.example.cheat;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.BlockPos;

public class HudOverlay {

    public static void register() {
        HudRenderCallback.EVENT.register(HudOverlay::onHudRender);
    }

    private static void onHudRender(DrawContext context, net.minecraft.client.render.RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        int x = 6;
        int y = 6;
        int lineHeight = 10;

        if (HudState.showCoords) {
            BlockPos pos = client.player.getBlockPos();
            String coordText = String.format("XYZ: %d / %d / %d", pos.getX(), pos.getY(), pos.getZ());
            context.drawTextWithShadow(client.textRenderer, coordText, x, y, 0xFFFFFF);
            y += lineHeight;
        }

        if (HudState.zoomEnabled) {
            context.drawTextWithShadow(client.textRenderer, "Zoom: ON", x, y, 0x55FFFF);
        }
    }
}
