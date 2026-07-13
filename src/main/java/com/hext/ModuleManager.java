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

    public static ModuleManager getInstance() { 
        return INSTANCE; 
    }

    public void init() {
        HextClient.modules.add(new Fly());
        HextClient.modules.add(new Aura());
        HextClient.modules.add(new Trigger());
        HextClient.modules.add(new Esp());
        HextClient.modules.add(new Hitbox());
        HextClient.modules.add(new ElytraTarget());
        HextClient.modules.add(new ElytraReplace());
        HextClient.modules.add(new ElytraSwap());

        // Keybindings
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
        });

        // Render event - World
        WorldRenderEvents.AFTER_TRANSLUCENT_RENDER.register(context -> {
            HextClient.modules.forEach(m -> {
                if (m == null) return;
                if (m.enabled) {
                    if (m instanceof Esp) ((Esp) m).onRender(context);
                    if (m instanceof Hitbox) ((Hitbox) m).onRender(context);
                }
            });
        });

        // HUD
        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            if (HextClient.mc == null || HextClient.mc.player == null) return;
            
            int y = 10;
            for (BaseModule m : HextClient.modules) {
                if (m != null && m.enabled && m.name != null) {
                    drawContext.drawText(HextClient.mc.textRenderer, "§a" + m.name, 10, y, 0xFFFFFF, true);
                    y += 12;
                }
            }
        });
    }

    public Optional<BaseModule> getModuleByName(String name) {
        if (name == null || name.isEmpty()) {
            return Optional.empty();
        }
        return HextClient.modules.stream()
                .filter(m -> m != null && m.name != null && m.name.equalsIgnoreCase(name))
                .findFirst();
    }

    @SuppressWarnings("unchecked")
    public <T extends BaseModule> T getModule(Class<T> clazz) {
        if (clazz == null) {
            return null;
        }
        return (T) HextClient.modules.stream()
                .filter(m -> m != null && clazz.isInstance(m))
                .findFirst()
                .orElse(null);
    }

    public void enableAll() {
        HextClient.modules.forEach(m -> {
            if (m != null) m.enabled = true;
        });
        com.hext.config.Config.save();
    }

    public void disableAll() {
        HextClient.modules.forEach(m -> {
            if (m != null) m.enabled = false;
        });
        com.hext.config.Config.save();
    }
}
