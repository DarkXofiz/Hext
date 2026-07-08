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

    // Hile toggle'ları
    private boolean killauraEnabled = false;
    private boolean xrayEnabled = false;
    private boolean espEnabled = false;
    private boolean speedEnabled = false;
    private boolean speedmineEnabled = false;
    private boolean hitboxEnabled = false;

    @Override
    public void onInitializeClient() {
        // M tuşu yerine RIGHT_SHIFT (istediğin gibi), istersen değiştir
        menuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.hext.menu",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_RIGHT_SHIFT,
            "category.hext"
        ));

        HudOverlay.register();  // ESP/Hitbox için

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (menuKey.wasPressed()) {
                if (client.currentScreen == null) {
                    client.setScreen(new ModMenuScreen());  // Mod menü aç
                } else {
                    // Toggle hileler
                    killauraEnabled = !killauraEnabled;
                    xrayEnabled = !xrayEnabled;
                    espEnabled = !espEnabled;
                    speedEnabled = !speedEnabled;
                    speedmineEnabled = !speedmineEnabled;
                    hitboxEnabled = !hitboxEnabled;

                    if (client.player != null) {
                        client.player.getAbilities().flying = !client.player.getAbilities().flying;
                    }
                    System.out.println("ExClient Menu: Aura=" + killauraEnabled + " XRay=" + xrayEnabled + " ESP=" + espEnabled);
                }
            }

            // === HİLELER ===
            // Killaura (Aura target)
            if (killauraEnabled && client.world != null && client.player != null) {
                for (Entity entity : client.world.getEntities()) {
                    if (entity instanceof PlayerEntity && entity != client.player && client.player.distanceTo(entity) < 6.0) {
                        client.interactionManager.attackEntity(client.player, entity);
                    }
                }
            }

            // Speed hack
            if (speedEnabled && client.player != null) {
                client.player.setVelocity(client.player.getVelocity().multiply(1.8)); // Hızlı koş
            }

            // SpeedMine (block break hızı - mixin ile tam olur)
            if (speedmineEnabled && client.player != null) {
                // TODO: Block breaking mixin ile hızlandır
            }

            // XRay, ESP, Hitbox: HudOverlay + mixin ile render'da çalışır
            // HudOverlay'de espEnabled ve hitboxEnabled kontrol et
        });
    }
}
