package com.hext;

import com.hext.gui.HextScreen;
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
    private KeyBinding openGuiKey;

    private ModuleManager() {}

    public static ModuleManager getInstance() { return INSTANCE; }

    public void init() {
        // Modülleri ekle
        HextClient.modules.add(new Fly());
        HextClient.modules.add(new Aura());
        HextClient.modules.add(new Trigger());
        HextClient.modules.add(new Esp());
        HextClient.modules.add(new Hitbox());
        HextClient.modules.add(new ElytraTarget());
        HextClient.modules.add(new ElytraReplace());
        HextClient.modules.add(new ElytraSwap());

        // Her modül için keybinding kaydet
        HextClient.modules.forEach(m -> m.keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.hext." + m.name.toLowerCase(),
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                "category.hext"
        )));

        // K tuşu - GUI aç
        openGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.hext.open_gui",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_K,
                "category.hext"
        ));

        // Tick event
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.world == null) return;
            HextClient.mc = client;

            // K tuşu - menüyü aç
            while (openGuiKey.wasPressed()) {
                client.setScreen(new HextScreen());
            }

            HextClient.modules.forEach(m -> {
                while (m.keyBinding.wasPressed()) {
                    m.toggle();
                    com.hext.config.Config.save();
                }
                if (m.enabled) m.onTick();
            });
        });

        // Dünya render eventi
        WorldRenderEvents.AFTER_ENTITIES.register(context -> {
            HextClient.modules.forEach(m -> {
                if (m.enabled && m instanceof Esp esp) esp.onRender(context);
                if (m.enabled && m instanceof Hitbox hitbox) hitbox.onRender(context);
            });
        });

        // HUD - aktif modülleri sol üstte göster
        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            if (HextClient.mc.player == null) return;
            if (HextClient.mc.currentScreen instanceof HextScreen) return; // menü açıkken gösterme
            int y = 10;
            for (BaseModule m : HextClient.modules) {
                if (m.enabled) {
                    drawContext.drawText(HextClient.mc.textRenderer, "§5§l" + m.name, 10, y, 0xFFFFFF, true);
                    y += 12;
                }
            }
        });
    }

    public Optional<BaseModule> getModuleByName(String name) {
        return HextClient.modules.stream()
                .filter(m -> m.name.equalsIgnoreCase(name))
                .findFirst();
    }

    @SuppressWarnings("unchecked")
    public <T extends BaseModule> T getModule(Class<T> clazz) {
        return (T) HextClient.modules.stream()
                .filter(clazz::isInstance)
                .findFirst()
                .orElse(null);
    }

    public void enableAll() {
        HextClient.modules.forEach(m -> { if (!m.enabled) m.toggle(); });
        com.hext.config.Config.save();
    }

    public void disableAll() {
        HextClient.modules.forEach(m -> { if (m.enabled) m.toggle(); });
        com.hext.config.Config.save();
    }
}
