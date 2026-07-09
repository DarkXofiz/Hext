package com.hextclient.client.util;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.util.math.MathHelper;
import org.joml.Matrix4f;

public class RenderUtil {

    /**
     * Lerp an animation value toward a target.
     */
    public static float lerp(float current, float target, float speed) {
        return current + (target - current) * MathHelper.clamp(speed, 0f, 1f);
    }

    /**
     * Blend two ARGB colors by t ∈ [0,1].
     */
    public static int lerpColor(int c1, int c2, float t) {
        int a1 = (c1 >> 24) & 0xFF, r1 = (c1 >> 16) & 0xFF,
            g1 = (c1 >>  8) & 0xFF, b1 =  c1        & 0xFF;
        int a2 = (c2 >> 24) & 0xFF, r2 = (c2 >> 16) & 0xFF,
            g2 = (c2 >>  8) & 0xFF, b2 =  c2        & 0xFF;
        int a = (int)(a1 + (a2 - a1) * t);
        int r = (int)(r1 + (r2 - r1) * t);
        int g = (int)(g1 + (g2 - g1) * t);
        int b = (int)(b1 + (b2 - b1) * t);
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    /**
     * Draw a filled rectangle with rounded corners using scissored quads.
     */
    public static void drawRoundedRect(DrawContext ctx, int x, int y, int w, int h, int radius, int color) {
        // Fill center
        ctx.fill(x + radius, y, x + w - radius, y + h, color);
        ctx.fill(x, y + radius, x + w, y + h - radius, color);
        // Corners (simple approximation)
        for (int i = 0; i < radius; i++) {
            for (int j = 0; j < radius; j++) {
                double dist = Math.sqrt((i - radius + 0.5) * (i - radius + 0.5)
                        + (j - radius + 0.5) * (j - radius + 0.5));
                if (dist <= radius) {
                    ctx.fill(x + i, y + j, x + i + 1, y + j + 1, color);
                    ctx.fill(x + w - radius + i, y + j, x + w - radius + i + 1, y + j + 1, color);
                    ctx.fill(x + i, y + h - radius + j, x + i + 1, y + h - radius + j + 1, color);
                    ctx.fill(x + w - radius + i, y + h - radius + j, x + w - radius + i + 1, y + h - radius + j + 1, color);
                }
            }
        }
    }

    /**
     * Draw a gradient rectangle (top-bottom).
     */
    public static void drawGradientRect(DrawContext ctx, int x, int y, int w, int h, int colorTop, int colorBottom) {
        ctx.fillGradient(x, y, x + w, y + h, colorTop, colorBottom);
    }

    /**
     * Interpolate alpha of a color.
     */
    public static int withAlpha(int color, int alpha) {
        return (color & 0x00FFFFFF) | (MathHelper.clamp(alpha, 0, 255) << 24);
    }

    /**
     * Clamp value between min and max.
     */
    public static float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(max, val));
    }
}
