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
            drawBox(matrices, consumers, e.getBoundingBox(), e.getPos(), 1f, 0f, 0f, 0.6f);
        }
    }

    private void drawBox(MatrixStack matrices, VertexConsumerProvider consumers, Box box, Vec3d pos, float r, float g, float b, float a) {
        VertexConsumer consumer = consumers.getBuffer(RenderLayer.getLines());
        Matrix4f mat = matrices.peek().getPositionMatrix();
        Vec3d offset = new Vec3d(box.minX - pos.x, box.minY - pos.y, box.minZ - pos.z);
        Vec3d min = pos.add(offset);
        Vec3d max = pos.add(new Vec3d(box.maxX - pos.x, box.maxY - pos.y, box.maxZ - pos.z));
        float[][] edges = {
                {0,0,0,1,0,0},{0,0,0,0,1,0},{0,0,0,0,0,1},
                {1,0,0,1,1,0},{1,0,0,1,0,1},{0,1,0,1,1,0},
                {0,1,0,0,1,1},{0,0,1,1,0,1},{1,1,0,1,1,1},
                {1,0,1,1,1,1},{0,1,1,1,1,1},{0,0,1,0,1,1}
        };
        for (float[] edge : edges) {
            Vec3d p1 = new Vec3d(min.x + (max.x - min.x) * edge[0], min.y + (max.y - min.y) * edge[1], min.z + (max.z - min.z) * edge[2]);
            Vec3d p2 = new Vec3d(min.x + (max.x - min.x) * edge[3], min.y + (max.y - min.y) * edge[4], min.z + (max.z - min.z) * edge[5]);
            consumer.vertex(mat, (float)p1.x, (float)p1.y, (float)p1.z).color(r,g,b,a).next();
            consumer.vertex(mat, (float)p2.x, (float)p2.y, (float)p2.z).color(r,g,b,a).next();
        }
        consumers.draw();
    }
}
