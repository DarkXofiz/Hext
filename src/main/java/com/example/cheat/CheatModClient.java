package com.example.cheat;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class CheatModClient implements ClientModInitializer {
    private static KeyBinding menuKey;

    @Override
    public void onInitializeClient() {
        menuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.Hext.menu", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_M, "category.mc-cheat-mod"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (menuKey.wasPressed()) {
                // TODO: Open GUI menu here (use ImGui or simple screen)
                System.out.println("M tuşu basıldı! Menu açılıyor... Aura, Fly, XRay toggle et.");
                // Example: Toggle fly
                if (client.player != null) {
                    client.player.getAbilities().flying = !client.player.getAbilities().flying;
                }
            }
        });
    }
}
