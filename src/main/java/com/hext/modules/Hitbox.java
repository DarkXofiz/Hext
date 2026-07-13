package com.hext.modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Box;

public class Hitbox extends BaseModule {
    private static final float EXPAND = 0.8f;
    public Hitbox() { super("Hitbox"); }

    public void onRender(net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext context) {
        // Visual expansion is handled by mixin, this just draws a larger box
        // We'll let the Mixin do the actual damage scaling, but render feedback
    }

    // Mixin handles the actual hitbox increase, this module is just a toggle.
}
