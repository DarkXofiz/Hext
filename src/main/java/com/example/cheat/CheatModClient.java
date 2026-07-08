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

    public static boolean killaura = false;
    public static boolean esp = false;
    public static boolean hitbox = false;
    public static boolean speed = false;
    public static boolean speedmine = false;
    public static boolean xray = false;

    @Override
    public void onInitializeClient() {
        menuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.hext.menu", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_M, "category.hext"
        ));

        HudOverlay.register();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (menuKey.wasPressed()) {
                client.setScreen(new ModMenuScreen());
            }

            // Killaura
            if (killaura && client.player != null && client.world != null) {
                for (Entity e : client.world.getEntities()) {
                    if (e instanceof PlayerEntity p && p != client.player && client.player.distanceTo(p) < 5) {
                        client.interactionManager.attackEntity(client.player, p);
                    }
                }
            }

            // Speed
            if (speed && client.player != null) {
                client.player.setVelocity(client.player.getVelocity().multiply(1.8));
            }
        });
    }
}
