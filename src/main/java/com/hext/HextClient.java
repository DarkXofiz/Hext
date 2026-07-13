package com.hext;

import com.hext.config.Config;
import com.hext.modules.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class HextClient implements ModInitializer, ClientModInitializer {
    public static final String MOD_ID = "hext";
    public static MinecraftClient mc;
    public static List<BaseModule> modules = new ArrayList<>();
    public static KeyBinding openGuiKey;

    @Override
    public void onInitialize() {
        // Fabric API init
    }

    @Override
    public void onInitializeClient() {
        mc = MinecraftClient.getInstance();
        Config.load();

        // Modules
        modules.add(new Fly());
        modules.add(new Aura());
        modules.add(new Trigger());
        modules.add(new ESP());
        modules.add(new Hitbox());
        modules.add(new ElytraTarget());
        modules.add(new ElytraReplace());
        modules.add(new ElytraSwap());

        modules.forEach(m -> m.keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.hext." + m.name.toLowerCase(),
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                "category.hext"
        )));

        // Açma/kapama tuşu K yapıldı
        openGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.hext.gui",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_K,
                "category.hext"
        ));

        // Tick handler
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.world == null) return;
            mc = client;
            // K tuşuna basınca tüm modülleri toggle etmek istersen aşağıdaki satırı aktif et:
            // while (openGuiKey.wasPressed()) { modules.forEach(BaseModule::toggle); }
            // Ama sen sadece menü tuşu olarak K istediğin için şimdilik boş bırakıyorum, modüller kendi tuşlarıyla açılıp kapanır.
            modules.forEach(m -> {
                if (m.keyBinding.wasPressed()) m.toggle();
                if (m.enabled) m.onTick();
            });
        });

        // Render handler for ESP/Hitbox visuals
        WorldRenderEvents.AFTER_ENTITIES.register(context -> {
            modules.forEach(m -> {
                if (m.enabled && m instanceof ESP) ((ESP) m).onRender(context);
                if (m.enabled && m instanceof Hitbox) ((Hitbox) m).onRender(context);
            });
        });

        // HUD status
        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            if (mc.player == null) return;
            int y = 10;
            for (BaseModule m : modules) {
                if (m.enabled) {
                    drawContext.drawText(mc.textRenderer, "§a" + m.name, 10, y, 0xFFFFFF, true);
                    y += 12;
                }
            }
        });
    }
}
