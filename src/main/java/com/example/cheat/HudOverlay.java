package com.example.cheat;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

public class HudOverlay {
    public static void register() {
        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            TextRenderer renderer = client.textRenderer;
            int x = 5;
            int y = 5;

            // Hext Başlığı
            renderer.draw(drawContext, "§b§lHext Client", x, y, 0xFFFFFF);
            y += 12;

            // FPS
            renderer.draw(drawContext, "FPS: §a" + client.getFpsDebugString().split(" ")[0], x, y, 0x00FF00);
            y += 12;

            // Aktif Hileler
            if (CheatModClient.killaura) {
                renderer.draw(drawContext, "§cKillaura", x, y, 0xFF0000);
                y += 10;
            }
            if (CheatModClient.speed) {
                renderer.draw(drawContext, "§aSpeed: " + CheatModClient.speedMultiplier + "x", x, y, 0x00FF00);
                y += 10;
            }
            if (CheatModClient.xray) {
                renderer.draw(drawContext, "§eX-Ray", x, y, 0xFFFF00);
                y += 10;
            }
            if (CheatModClient.esp) {
                renderer.draw(drawContext, "§dESP", x, y, 0xFF00FF);
                y += 10;
            }
            if (CheatModClient.hitbox) {
                renderer.draw(drawContext, "§6Hitbox", x, y, 0xFFAA00);
                y += 10;
            }
            if (CheatModClient.speedmine) {
                renderer.draw(drawContext, "§bSpeedMine: " + CheatModClient.speedmineSpeed + "x", x, y, 0x00AAFF);
            }

            // ESP/Hitbox render (box'lar için ekstra kod lazım)
        });
    }
}
