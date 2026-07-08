package com.example.cheat;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.lwjgl.glfw.GLFW;

public class CheatModClient implements ClientModInitializer {
    private static KeyBinding menuKey;

    // Hile ayarları (önceki kodlardan)
    public static boolean killaura = false;
    public static double killauraRange = 5.0;
    public static boolean esp = false;
    public static boolean hitbox = false;
    public static double hitboxScale = 1.0;
    public static boolean speed = false;
    public static double speedMultiplier = 1.5;
    public static boolean speedmine = false;
    public static float speedmineSpeed = 2.0f;
    public static boolean xray = false;

    @Override
    public void onInitializeClient() {
        // M tuşu
        menuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.hext.menu", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_M, "category.hext"
        ));

        HudOverlay.register();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (menuKey.wasPressed()) {
                if (client.currentScreen == null) {
                    client.setScreen(new ModMenuScreen()); // M bas → Tek menü açılır
                }
            }

            // Killaura (M menüsünden ayarlı)
            if (killaura && client.player != null && client.world != null) {
                for (Entity e : client.world.getEntities()) {
                    if (e instanceof PlayerEntity p && p != client.player && client.player.distanceTo(p) < killauraRange) {
                        client.interactionManager.attackEntity(client.player, p);
                    }
                }
            }

            // Speed
            if (speed && client.player != null) {
                var vel = client.player.getVelocity();
                client.player.setVelocity(vel.multiply(speedMultiplier));
            }
        });
    }
}
