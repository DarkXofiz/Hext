package com.hext;

import com.hext.modules.*;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.util.Optional;

public class ModuleManager {
    private static final ModuleManager INSTANCE = new ModuleManager();

    private ModuleManager() {}

    public static ModuleManager getInstance() { return INSTANCE; }

    public void init() {
        HextClient.modules.add(new Fly());
        HextClient.modules.add(new Aura());
        HextClient.modules.add(new Trigger());
        HextClient.modules.add(new Esp());
        HextClient.modules.add(new Hitbox());
        HextClient.modules.add(new ElytraTarget());
        HextClient.modules.add(new ElytraReplace());
        HextClient.modules.add(new ElytraSwap());

        // Her module için keybinding
        HextClient.modules.forEach(m -> {
            if (m != null) {
                m.keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                        "key.hext." + m.name.toLowerCase(),
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_UNKNOWN,
                        "category.hext"
                ));
            }
        });

        // Tick event
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            try {
                if (client == null || client.player == null || client.world == null) return;
                HextClient.mc = client;

                HextClient.modules.forEach(m -> {
                    if (m == null) return;
                    if (m.keyBinding != null && m.keyBinding.wasPressed()) {
                        m.toggle();
                        com.hext.config.Config.save();
                    }
                    if (m.enabled) m.onTick();
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Render event
        WorldRenderEvents.AFTER_ENTITIES.register(context -> {
            try {
                HextClient.modules.forEach(m -> {
                    if (m == null) return;
                    if (m.enabled) {
                        if (m instanceof Esp) {
                            ((Esp) m).onRender(context);
                        }
                        if (m instanceof Hitbox) {
                            ((Hitbox) m).onRender(context);
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // HUD
        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            try {
                if (HextClient.mc == null || HextClient.mc.player == null) return;
                
                int y = 10;
                for (BaseModule m : HextClient.modules) {
                    if (m != null && m.enabled && m.name != null) {
                        drawContext.drawText(
                                HextClient.mc.textRenderer, 
                                "§a" + m.name, 
                                10, 
                                y,
