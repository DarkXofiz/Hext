// 1. Aura.java - Thunderhack Style
package com.hext.modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;

import java.util.Comparator;

public class Aura extends BaseModule {
    private int tickCounter = 0;

    public enum TargetMode {
        CLOSEST, HIGHEST, LOWEST
    }

    public Aura() {
        super("Aura");
        addSlider("Range", 6.0, 1.0, 20.0);
        addSlider("Speed", 10.0, 1.0, 20.0);
        addBoolean("Teamcheck", false);
        addBoolean("Players", true);
        addBoolean("Animals", false);
        addBoolean("Mobs", true);
    }

    @Override
    public void onTick() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.world == null) return;

        // Ayarları oku
        Double range = getSetting("Range");
        Double speed = getSetting("Speed");
        Boolean teamCheck = getSetting("Teamcheck");
        Boolean players = getSetting("Players");
        Boolean animals = getSetting("Animals");
        Boolean mobs = getSetting("Mobs");

        if (range == null) range = 6.0;
        if (speed == null) speed = 10.0;
        if (teamCheck == null) teamCheck = false;
        if (players == null) players = true;
        if (animals == null) animals = false;
        if (mobs == null) mobs = true;

        // Final değerler
        final double finalRange = range;
        final boolean finalTeamCheck = teamCheck;
        final boolean finalPlayers = players;
        final boolean finalAnimals = animals;
        final boolean finalMobs = mobs;

        // Speed kontrolü
        tickCounter++;
        int speedDelay = (int) (20.0 / speed);
        if (tickCounter % speedDelay != 0) return;

        Box box = mc.player.getBoundingBox().expand(finalRange);
        
        LivingEntity target = mc.world.getEntitiesByClass(
                LivingEntity.class, box,
                e -> {
                    if (e == mc.player || !e.isAlive()) return false;
                    if (finalTeamCheck && isTeammate(e)) return false;
                    
                    String entityType = e.getClass().getSimpleName();
                    
                    if (finalPlayers && entityType.contains("PlayerEntity")) return true;
                    if (finalMobs && (entityType.contains("Monster") || entityType.contains("Spider"))) return true;
                    if (finalAnimals && (entityType.contains("Cow") || entityType.contains("Pig"))) return true;
                    
                    return false;
                }
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
