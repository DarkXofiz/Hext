package com.hext.client.util;

import com.hext.client.HextClient;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

public class KeyInputHandler {

    private boolean xWasPressed = false;

    public KeyInputHandler() {
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    private void onTick(MinecraftClient client) {
        if (client.currentScreen != null && !(client.currentScreen instanceof com.hext.client.gui.clickgui.ClickGUI)) {
            xWasPressed = false;
            return;
        }

        long handle = client.getWindow().getHandle();
        boolean xPressed = GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_X) == GLFW.GLFW_PRESS;

        if (xPressed && !xWasPressed && client.currentScreen == null) {
            Hext.getInstance().getClickGUI().open();
        }
        xWasPressed = xPressed;

        // Module keybinds
        if (client.currentScreen == null) {
            Hext.getInstance().getModuleManager().onKeyPress(getCurrentKey(handle));
        }
    }

    private int getCurrentKey(long handle) {
        int[] keys = {
            GLFW.GLFW_KEY_F1, GLFW.GLFW_KEY_F2, GLFW.GLFW_KEY_F3, GLFW.GLFW_KEY_F4,
            GLFW.GLFW_KEY_F5, GLFW.GLFW_KEY_F6, GLFW.GLFW_KEY_F7, GLFW.GLFW_KEY_F8,
            GLFW.GLFW_KEY_F9, GLFW.GLFW_KEY_F10, GLFW.GLFW_KEY_F11, GLFW.GLFW_KEY_F12,
            GLFW.GLFW_KEY_R, GLFW.GLFW_KEY_Y, GLFW.GLFW_KEY_U, GLFW.GLFW_KEY_I,
            GLFW.GLFW_KEY_O, GLFW.GLFW_KEY_P, GLFW.GLFW_KEY_H, GLFW.GLFW_KEY_J,
            GLFW.GLFW_KEY_K, GLFW.GLFW_KEY_L, GLFW.GLFW_KEY_Z, GLFW.GLFW_KEY_V,
            GLFW.GLFW_KEY_B, GLFW.GLFW_KEY_N, GLFW.GLFW_KEY_M
        };
        for (int key : keys) {
            if (GLFW.glfwGetKey(handle, key) == GLFW.GLFW_PRESS) {
                return key;
            }
        }
        return GLFW.GLFW_KEY_UNKNOWN;
    }
}
