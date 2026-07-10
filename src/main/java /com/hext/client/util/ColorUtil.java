package com.hext.client.util;

import java.awt.Color;

public class ColorUtil {

    public static int rgba(int r, int g, int b, int a) {
        return ((a & 0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF);
    }

    public static int withAlpha(int color, float alpha) {
        int a = (int)(MathUtil.clamp(alpha, 0f, 1f) * 255);
        return (color & 0x00FFFFFF) | (a << 24);
    }

    public static int rainbow(long offset) {
        float hue = (float)((System.currentTimeMillis() + offset) % 2000L) / 2000f;
        return Color.HSBtoRGB(hue, 0.8f, 1.0f);
    }

    public static int blend(int c1, int c2, float t) {
        return RenderUtil.lerpColor(c1, c2, t);
    }

    public static int brighter(int color, float factor) {
        int r = Math.min(255, (int)(((color >> 16) & 0xFF) * (1f + factor)));
        int g = Math.min(255, (int)(((color >>  8) & 0xFF) * (1f + factor)));
        int b = Math.min(255, (int)((color & 0xFF) * (1f + factor)));
        int a = (color >> 24) & 0xFF;
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    public static int darker(int color, float factor) {
        int r = (int)(((color >> 16) & 0xFF) * (1f - factor));
        int g = (int)(((color >>  8) & 0xFF) * (1f - factor));
        int b = (int)((color & 0xFF) * (1f - factor));
        int a = (color >> 24) & 0xFF;
        return (a << 24) | (r << 16) | (g << 8) | b;
    }
}
