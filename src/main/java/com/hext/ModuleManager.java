package com.hext;

import com.hext.config.Config;
import com.hext.modules.*;
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
import java.util.Optional;

public class ModuleManager {
    private static final ModuleManager INSTANCE = new ModuleManager();
    private final List<BaseModule> modules = new ArrayList<>();
    private MinecraftClient mc;
    private KeyBinding openGuiKey;

    private ModuleManager() {}

    public static ModuleManager getInstance() {
        return INSTANCE;
    }

    public void init() {
        mc = MinecraftClient.getInstance();
        Config.load();

        // Modülleri oluştur
        modules.add(new Fly());
        modules.add(new Aura());
        modules.add(new Trigger());
        modules.add(new ESP());
        modules.add(new Hitbox());
        modules.add(new ElytraTarget());
        modules.add(new ElytraReplace());
        modules.add(new ElytraSwap());

        // Her module için keybinding (varsayılan boş, oyunda ata)
        modules.forEach(m -> m.keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.hext." + m.name.toLowerCase(),
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                "category.hext"
        )));

        // K tuşu – tüm modülleri toggle etmek için (istersen açık bırak)
        openGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.hext.toggle_all",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_K,
                "category.hext"
        ));

        // Tick event
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.world == null) return;
            mc = client;

            // K tuşu: tüm modülleri aç/kapa
            while (openGuiKey.wasPressed()) {
                modules.forEach(BaseModule::toggle);
                Config.save();
            }

            // Modül özel tick'leri
            modules.forEach(m -> {
                if (m.keyBinding.wasPressed()) {
                    m.toggle();
                    Config.save();
                }
                if (m.enabled) m.onTick();
            });
        });

        // Render event (ESP, Hitbox çizimleri)
        WorldRenderEvents.AFTER_ENTITIES.register(context -> {
            modules.forEach(m -> {
                if (m.enabled && m instanceof ESP) ((ESP) m).onRender(context);
                if (m.enabled && m instanceof Hitbox) ((Hitbox) m).onRender(context);
            });
        });

        // HUD – aktif modülleri göster
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

    public List<BaseModule> getModules() {
        return modules;
    }

    public Optional<BaseModule> getModuleByName(String name) {
        return modules.stream().filter(m -> m.name.equalsIgnoreCase(name)).findFirst();
    }

    @SuppressWarnings("unchecked")
    public <T extends BaseModule> T getModule(Class<T> clazz) {
        return (T) modules.stream().filter(clazz::isInstance).findFirst().orElse(null);
    }

    public void enableAll() {
        modules.forEach(m -> m.enabled = true);
        Config.save();
    }

    public void disableAll() {
        modules.forEach(m -> m.enabled = false);
        Config.save();
    }
}
