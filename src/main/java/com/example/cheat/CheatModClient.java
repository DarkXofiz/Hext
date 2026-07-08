package com.example.cheat;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class CheatModClient implements ClientModInitializer {
    private static KeyBinding menuKey;

    public static boolean killaura = false;
    public static boolean xray = false;
    public static boolean esp = false;
    public static boolean hitbox = false;
    public static boolean speed = false;
    public static double speedMultiplier = 1.5; // Ayarlanabilir
    public static boolean speedmine = false;
    public static float speedmineSpeed = 2.0f; // Ayarlanabilir

    @Override
    public void onInitializeClient() {
        menuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.hext.menu", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT, "category.hext"
        ));

        HudOverlay.register();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (menuKey.wasPressed()) {
                if (client.currentScreen == null) {
                    client.setScreen(new ModMenuScreen()); // Ayarlar burada
                }
            }

            // Killaura
            if (killaura && client.player != null && client.world != null) {
                for (var e : client.world.getEntities()) {
                    if (e instanceof net.minecraft.entity.player.PlayerEntity p && p != client.player && client.player.distanceTo(p) < 5) {
                        client.interactionManager.attackEntity(client.player, p);
                    }
                }
            }

            // Speed (ayarlı)
            if (speed && client.player != null) {
                var vel = client.player.getVelocity();
                client.player.setVelocity(vel.multiply(speedMultiplier));
            }

            // HUD her tick güncellenir
        });
    }
}
