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

        // === HEXT BAŞLIK + FPS (Thunder tarzı) ===
        context.drawTextWithShadow(client.textRenderer, "§b§lHext Client", x, y, 0xFFFFFF);
        y += lineHeight;

        // FPS
        String fps = "FPS: §a" + client.getFpsDebugString().split(" ")[0];
        context.drawTextWithShadow(client.textRenderer, fps, x, y, 0x00FF00);
        y += lineHeight;

        // Koordinat (zaten var)
        if (HudState.showCoords) {
            BlockPos pos = client.player.getBlockPos();
            String coordText = String.format("XYZ: %d / %d / %d", pos.getX(), pos.getY(), pos.getZ());
            context.drawTextWithShadow(client.textRenderer, coordText, x, y, 0xFFFFFF);
            y += lineHeight;
        }

        // === HİLE DURUMLARI ===
        if (CheatModClient.killaura) {
            context.drawTextWithShadow(client.textRenderer, "§cKillaura ON", x, y, 0xFF0000);
            y += lineHeight;
        }
        if (CheatModClient.speed) {
            context.drawTextWithShadow(client.textRenderer, "§aSpeed: " + CheatModClient.speedMultiplier + "x ON", x, y, 0x00FF00);
            y += lineHeight;
        }
        if (CheatModClient.xray) {
            context.drawTextWithShadow(client.textRenderer, "§eX-Ray ON", x, y, 0xFFFF00);
            y += lineHeight;
        }
        if (CheatModClient.esp) {
            context.drawTextWithShadow(client.textRenderer, "§dESP ON", x, y, 0xFF00FF);
            y += lineHeight;
        }
        if (CheatModClient.hitbox) {
            context.drawTextWithShadow(client.textRenderer, "§6Hitbox ON", x, y, 0xFFAA00);
            y += lineHeight;
        }
        if (CheatModClient.speedmine) {
            context.drawTextWithShadow(client.textRenderer, "§bSpeedMine: " + CheatModClient.speedmineSpeed + "x ON", x, y, 0x00AAFF);
            y += lineHeight;
        }

        if (HudState.flyEnabled) {
            context.drawTextWithShadow(client.textRenderer, "Fly: ON", x, y, 0x55FF55);
            y += lineHeight;
        }

        if (HudState.zoomEnabled) {
            context.drawTextWithShadow(client.textRenderer, "Zoom: ON", x, y, 0x55FFFF);
        }
    }
}
