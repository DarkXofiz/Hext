// 2. Hitbox.java - FIXED (Render only, no collision changes)
package com.hext.modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Matrix4f;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;

public class Hitbox extends BaseModule {
    private static final float LINE_WIDTH = 1.5f;

    public Hitbox() {
        super("Hitbox");
        addSlider("Red", 0.0, 0.0, 255.0);
        addSlider("Green", 255.0, 0.0, 255.0);
        addSlider("Blue", 0.0, 0.0, 255.0);
        addSlider("Alpha", 100.0, 0.0, 255.0);
    }

    @Override
    public void onTick() {
        // Nothing to do on tick
    }

    public void onRender(WorldRenderEvents.After context) {
        try {
            MinecraftClient mc = MinecraftClient.getInstance();
            if (mc.world == null || mc.player == null || mc.camera == null) return;

            // Renkler
            Double red = getSetting("Red");
            Double green = getSetting("Green");
            Double blue = getSetting("Blue");
            Double alpha = getSetting("Alpha");

            if (red == null) red = 0.0;
            if (green == null) green = 255.0;
            if (blue == null) blue = 0.0;
            if (alpha == null) alpha = 100.0;

            float r = (float) (red / 255.0);
            float g = (float) (green / 255.0);
            float b = (float) (blue / 255.0);
            float a = (float) (alpha / 255.0);

            // Tüm entityleri render et
            for (Entity entity : mc.world.getEntities()) {
                if (entity == mc.player || entity == null) continue;
                if (!(entity instanceof LivingEntity)) continue;
                if (!entity.isAlive()) continue;

                Box box = entity.getBoundingBox();
                drawBox(context, box, r, g, b, a);
            }

        } catch (Exception e) {
            System.err.println("Hitbox render error: " + e.getMessage());
        }
    }

    private void drawBox(WorldRenderEvents.After context, Box box, float r, float g, float b, float a) {
        double camX = context.camera().getPos().x;
        double camY = context.camera().getPos().y;
        double camZ = context.camera().getPos().z;

        double x1 = box.minX - camX;
        double y1 = box.minY - camY;
        double z1 = box.minZ - camZ;
        double x2 = box.maxX - camX;
        double y2 = box.maxY - camY;
        double z2 = box.maxZ - camZ;

        // Render lines
        var matrices = context.matrixStack();
        matrices.push();

        VertexConsumer consumer = context.consumers().getBuffer(RenderLayer.getLines());

        // Bottom face
        drawLine(consumer, matrices, x1, y1, z1, x2, y1, z1, r, g, b, a);
        drawLine(consumer, matrices, x2, y1, z1, x2, y1, z2, r, g, b, a);
        drawLine(consumer, matrices, x2, y1, z2, x1, y1, z2, r, g, b, a);
        drawLine(consumer, matrices, x1, y1, z2, x1, y1, z1, r, g, b, a);

        // Top face
        drawLine(consumer, matrices, x1, y2, z1, x2, y2, z1, r, g, b, a);
        drawLine(consumer, matrices, x2, y2, z1, x2, y2, z2, r, g, b, a);
        drawLine(consumer, matrices, x2, y2, z2, x1, y2, z2, r, g, b, a);
        drawLine(consumer, matrices, x1, y2, z2, x1, y2, z1, r, g, b, a);

        // Vertical lines
        drawLine(consumer, matrices, x1, y1, z1, x1, y2, z1, r, g, b, a);
        drawLine(consumer, matrices, x2, y1, z1, x2, y2, z1, r, g, b, a);
        drawLine(consumer, matrices, x2, y1, z2, x2, y2, z2, r, g, b, a);
        drawLine(consumer, matrices, x1, y1, z2, x1, y2, z2, r, g, b, a);

        matrices.pop();
    }

    private void drawLine(VertexConsumer consumer, MatrixStack matrices, 
                         double x1, double y1, double z1, 
                         double x2, double y2, double z2, 
                         float r, float g, float b, float a) {
        
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        
        consumer.vertex(matrix, (float) x1, (float) y1, (float) z1)
                .color(r, g, b, a)
                .next();
        
        consumer.vertex(matrix, (float) x2, (float) y2, (float) z2)
                .color(r, g, b, a)
                .next();
    }
}
