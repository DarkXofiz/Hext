package com.hext.client.module.impl.render;

import com.hext.client.module.Module;
import com.hext.client.module.category.Category;
import com.hext.client.module.setting.BooleanSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayDeque;

public class Keystrokes extends Module {

    private final BooleanSetting showCPS = addSetting(new BooleanSetting(
            "Show CPS", "CPS sayacını göster", true
    ));

    private final ArrayDeque<Long> clicks = new ArrayDeque<>();

    public Keystrokes() {
        super("Keystrokes", "Basılan tuşları ve CPS'i ekranda gösterir", Category.RENDER);
    }

    public void registerClick() {
        long now = System.currentTimeMillis();
        clicks.addLast(now);
        while (!clicks.isEmpty() && now - clicks.peekFirst() > 1000) {
            clicks.pollFirst();
        }
    }

    public int getCPS() {
        long now = System.currentTimeMillis();
        while (!clicks.isEmpty() && now - clicks.peekFirst() > 1000) {
            clicks.pollFirst();
        }
        return clicks.size();
    }

    public boolean shouldShowCPS() { return showCPS.getValue(); }

    public void render(DrawContext ctx, MinecraftClient mc) {
        if (!isEnabled() || mc.player == null) return;

        int screenW = mc.getWindow().getScaledWidth();
        int screenH = mc.getWindow().getScaledHeight();

        // Pozisyon: sağ alt köşe
        int baseX = screenW - 70;
        int baseY = screenH - 90;

        long handle = mc.getWindow().getHandle();
        boolean w = GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_W) == GLFW.GLFW_PRESS;
        boolean a = GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_A) == GLFW.GLFW_PRESS;
        boolean s = GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_S) == GLFW.GLFW_PRESS;
        boolean d = GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_D) == GLFW.GLFW_PRESS;
        boolean space = GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_SPACE) == GLFW.GLFW_PRESS;
        boolean lmb = GLFW.glfwGetMouseButton(handle, GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS;
        boolean rmb = GLFW.glfwGetMouseButton(handle, GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS;

        int activeColor   = 0xCC4FC3F7;
        int inactiveColor = 0x99111111;
        int textActive    = 0xFFFFFFFF;
        int textInactive  = 0xFFAAAAAA;
        int keyW = 20, keyH = 16;
        int gap = 2;

        // W
        drawKey(ctx, mc, "W", baseX + keyW + gap, baseY, keyW, keyH, w, activeColor, inactiveColor, textActive, textInactive);
        // A S D
        drawKey(ctx, mc, "A", baseX,                      baseY + keyH + gap, keyW, keyH, a, activeColor, inactiveColor, textActive, textInactive);
        drawKey(ctx, mc, "S", baseX + keyW + gap,          baseY + keyH + gap, keyW, keyH, s, activeColor, inactiveColor, textActive, textInactive);
        drawKey(ctx, mc, "D", baseX + (keyW + gap) * 2,   baseY + keyH + gap, keyW, keyH, d, activeColor, inactiveColor, textActive, textInactive);
        // Space
        drawKey(ctx, mc, "___", baseX, baseY + (keyH + gap) * 2, keyW * 3 + gap * 2, keyH, space, activeColor, inactiveColor, textActive, textInactive);
        // LMB RMB
        drawKey(ctx, mc, "LMB", baseX,            baseY + (keyH + gap) * 3, keyW + 8, keyH, lmb, activeColor, inactiveColor, textActive, textInactive);
        drawKey(ctx, mc, "RMB", baseX + keyW + 10, baseY + (keyH + gap) * 3, keyW + 8, keyH, rmb, activeColor, inactiveColor, textActive, textInactive);

        // CPS
        if (shouldShowCPS()) {
            String cpsText = "CPS: " + getCPS();
            ctx.drawTextWithShadow(mc.textRenderer, cpsText, baseX, baseY + (keyH + gap) * 4 + 2, 0xFFFFFFFF);
        }
    }

    private void drawKey(DrawContext ctx, MinecraftClient mc,
                         String label, int x, int y, int w, int h,
                         boolean pressed,
                         int activeColor, int inactiveColor,
                         int textActive, int textInactive) {
        int bg = pressed ? activeColor : inactiveColor;
        int fg = pressed ? textActive : textInactive;
        ctx.fill(x, y, x + w, y + h, bg);
        // border
        ctx.fill(x, y, x + w, y + 1, 0x55FFFFFF);
        ctx.fill(x, y + h - 1, x + w, y + h, 0x33000000);
        int tx = x + (w - mc.textRenderer.getWidth(label)) / 2;
        int ty = y + (h - mc.textRenderer.fontHeight) / 2;
        ctx.drawTextWithShadow(mc.textRenderer, label, tx, ty, fg);
    }
}
