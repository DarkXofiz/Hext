package com.hext;

import com.hext.config.Config;
import com.hext.gui.HextScreen;
import com.hext.modules.BaseModule;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
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
        // Sunucu tarafı - boş bırakılabilir
    }

    @Override
    public void onInitializeClient() {
        try {
            // Minecraft instance
            mc = MinecraftClient.getInstance();
            if (mc == null) {
                System.err.println("[HEXT] MinecraftClient instance alınamadı!");
                return;
            }

            // Config yükle
            try {
                Config.load();
            } catch (Exception e) {
                System.err.println("[HEXT] Config yükleme hatası: " + e.getMessage());
                e.printStackTrace();
            }

            // Modülleri başlat
            try {
                ModuleManager moduleManager = ModuleManager.getInstance();
                if (moduleManager != null) {
                    moduleManager.init();
                } else {
                    System.err.println("[HEXT] ModuleManager başlatılamadı!");
                }
            } catch (Exception e) {
                System.err.println("[HEXT] Module başlatma hatası: " + e.getMessage());
                e.printStackTrace();
            }

            // K tuşu - ClickGUI'yi aç
            openGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                    "key.hext.gui",
                    InputUtil.Type.KEYSYM,
                    GLFW.GLFW_KEY_K,
                    "category.hext"
            ));

            if (openGuiKey == null) {
                System.err.println("[HEXT] KeyBinding kaydı başarısız!");
                return;
            }

            // GUI açma event
            ClientTickEvents.END_CLIENT_TICK.register(client -> {
                if (client == null || client.player == null) return;

                try {
                    if (openGuiKey != null && openGuiKey.wasPressed()) {
                        // Null kontrol ve screen check
                        if (!(client.currentScreen instanceof HextScreen)) {
                            HextScreen screen = new HextScreen();
                            if (screen != null) {
                                client.setScreen(screen);
                            }
                        }
                    }
                } catch (Exception e) {
                    System.err.println("[HEXT] GUI açma hatası: " + e.getMessage());
                    e.printStackTrace();
                }
            });

        } catch (Exception e) {
            System.err.println("[HEXT] Client iniciyalizasyon hatası: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
