package com.hext.client;

import com.hext.client.gui.theme.ThemeManager;
import com.hext.client.module.ModuleManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class Hext implements ClientModInitializer {

    public static final String NAME = "Hext";
    public static final String VERSION = "1.21x";

    private static Hext instance;

    private ThemeManager themeManager;
    private ModuleManager moduleManager;

    private KeyBinding openGuiKeybind;

    @Override
    public void onInitializeClient() {
        instance = this;

        themeManager = new ThemeManager();
        moduleManager = new ModuleManager();

        openGuiKeybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.hext.open_gui",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                "category.hext"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            moduleManager.onTick(client);

            while (openGuiKeybind.wasPressed()) {
                com.hext.client.gui.clickgui.ClickGUI gui = new com.hext.client.gui.clickgui.ClickGUI();
                gui.open();
            }
        });
    }

    public static Hext getInstance() {
        return instance;
    }

    public ThemeManager getThemeManager() {
        return themeManager;
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }
}
