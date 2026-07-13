package com.hext.modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;

import java.util.Comparator;

public class Aura extends BaseModule {
    private int tickCounter = 0;

    public Aura() {
        super("Aura");
        addSlider("Menzil", 6.0, 1.0, 12.0);
        addSlider("Gecikme", 2.0, 1.0, 10.0);
        addBoolean("Takım", false);
    }

    @Override
    public void onTick() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.world == null) return;

        // Ayarları oku (final yapısına al)
        Double range = getSetting("Menzil");
        Double delay = getSetting("Gecikme");
        Boolean teamCheck = getSetting("Takım");

        if (range == null) range = 6.0;
        if (delay == null) delay = 2.0;
        if (teamCheck == null) teamCheck = false;

        // Final kopyaları oluştur (lambda için)
        final double finalRange = range;
        final boolean finalTeamCheck = teamCheck;

        tickCounter = (tickCounter + 1) % 20;
        if (tickCounter % (int) Math.max(1, delay) != 0) return;

        Box box = mc.player.getBoundingBox().expand(finalRange);
        Entity target = mc.world.getEntitiesByClass(
                LivingEntity.class, box,
                e -> e != mc.player 
                    && e.isAlive() 
                    && (finalTeamCheck ? !isTeammate(e) : true)
        ).stream()
                .min(Comparator.comparingDouble(e -> mc.player.distanceTo(e)))
                .orElse(null);

        if (target != null && mc.player.distanceTo(target) <= finalRange) {
            if (mc.interactionManager != null) {
                mc.interactionManager.attackEntity(mc.player, target);
                mc.player.swingHand(Hand.MAIN_HAND);
            }
        }
    }

    private boolean isTeammate(Entity entity) {
        if (!(entity instanceof LivingEntity)) return false;
        
        LivingEntity livingEntity = (LivingEntity) entity;
        MinecraftClient mc = MinecraftClient.getInstance();
        
        if (mc.player == null) return false;
        
        if (mc.player.getScoreboardTeam() != null && livingEntity.getScoreboardTeam() != null) {
            return mc.player.getScoreboardTeam().equals(livingEntity.getScoreboardTeam());
        }
        
        return false;
    }
}
