package com.hextclient.client.util;

public class MathUtil {

    public static double clamp(double val, double min, double max) {
        return Math.max(min, Math.min(max, val));
    }

    public static float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(max, val));
    }

    public static int clamp(int val, int min, int max) {
        return Math.max(min, Math.min(max, val));
    }

    public static double lerp(double a, double b, double t) {
        return a + (b - a) * t;
    }

    public static float easeOut(float t) {
        return 1f - (1f - t) * (1f - t);
    }

    public static float easeIn(float t) {
        return t * t;
    }

    public static float easeInOut(float t) {
        return t < 0.5f ? 2 * t * t : 1 - (float)Math.pow(-2 * t + 2, 2) / 2;
    }
}
