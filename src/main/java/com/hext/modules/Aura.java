package com.hext.modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;

import java.util.Comparator;

public class Aura extends BaseModule {
    private int tickCounter = 0;
    private static final double RANGE = 6.0;

    public Aura() { super("Aura"); }

    @Override
    public void onTick() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.world == null) return;

        tickCounter = (tickCounter + 1) % 20;
        if (tickCounter % 2 != 0) return;

        Box box = mc.player.getBoundingBox().expand(RANGE);
        Entity target = mc.world.getEntitiesByClass(
                LivingEntity.class, box,
                e -> e != mc.player && e.isAlive()
        ).stream()
                .min(Comparator.comparingDouble(e -> mc.player.distanceTo(e)))
                .orElse(null);

        if (target != null && mc.player.distanceTo(target) <= RANGE) {
            mc.interactionManager.attackEntity(mc.player, target);
            mc.player.swingHand(Hand.MAIN_HAND);
        }
    }
}
