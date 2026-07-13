package com.hext.modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

public class Trigger extends BaseModule {
    public Trigger() { super("Trigger"); }

    @Override
    public void onTick() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.world == null) return;
        HitResult hit = mc.crosshairTarget;
        if (hit instanceof EntityHitResult eHit) {
            Entity e = eHit.getEntity();
            if (e instanceof LivingEntity && e.isAlive() && e != mc.player) {
                mc.interactionManager.attackEntity(mc.player, e);
                mc.player.swingHand(mc.player.getActiveHand());
            }
        }
    }
}
