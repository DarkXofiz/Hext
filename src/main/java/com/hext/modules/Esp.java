package com.hext.modules;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

public class Esp extends BaseModule {
    public Esp() { super("ESP"); }

    public void onRender(WorldRenderContext context) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.world == null) return;

        MatrixStack matrices = context.matrixStack();
        VertexConsumerProvider.Immediate consumers = mc.getBufferBuilders().getEntityVertexConsumers();

        for (Entity e : mc.world.getEntities()) {
            if (e == mc.player || !(e instanceof LivingEntity) || !e.isAlive()) continue;
            drawBox(matrices, consumers, e.getBoundingBox(), context.camera().getPos(), 1f, 0f, 0f, 0.6f);
        }

        consumers.draw(RenderLayer.getLines());
    }

    private void drawBox(MatrixStack matrices, VertexConsumerProvider consumers,
                         Box box, Vec3d camPos, float r, float g, float b, float a) {
        VertexConsumer consumer = consumers.getBuffer(RenderLayer.getLines());
        Matrix4f mat = matrices.peek().getPositionMatrix();

        float minX = (float)(box.minX - camPos.x);
        float minY = (float)(box.minY - camPos.y);
        float minZ = (float)(box.minZ - camPos.z);
        float maxX = (float)(box.maxX - camPos.x);
        float maxY = (float)(box.maxY - camPos.y);
        float maxZ = (float)(box.maxZ - camPos.z);

        // 12 kenar - her kenar 2 vertex
        float[][] edges = {
            {minX,minY,minZ, maxX,minY,minZ},
            {minX,minY,minZ, minX,maxY,minZ},
            {minX,minY,minZ, minX,minY,maxZ},
            {maxX,minY,minZ, maxX,maxY,minZ},
            {maxX,minY,minZ, maxX,minY,maxZ},
            {minX,maxY,minZ, maxX,maxY,minZ},
            {minX,maxY,minZ, minX,maxY,maxZ},
            {minX,minY,maxZ, maxX,minY,maxZ},
            {minX,minY,maxZ, minX,maxY,maxZ},
            {maxX,maxY,minZ, maxX,maxY,maxZ},
            {maxX,minY,maxZ, maxX,maxY,maxZ},
            {minX,maxY,maxZ, maxX,maxY,maxZ}
        };

        for (float[] edge : edges) {
            consumer.vertex(mat, edge[0], edge[1], edge[2]).color(r, g, b, a).normal(matrices.peek(), 1f, 0f, 0f);
            consumer.vertex(mat, edge[3], edge[4], edge[5]).color(r, g, b, a).normal(matrices.peek(), 1f, 0f, 0f);
        }
    }
}
