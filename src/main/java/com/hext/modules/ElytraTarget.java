package com.hext.modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.Comparator;

public class ElytraTarget extends BaseModule {
    private static final double DEFAULT_RANGE = 20.0;
    private static final double DEFAULT_AUTO = true;
    private static final double VERTICAL_OFFSET_DIVISOR = 2.0;
    private static final double DEGREE_MULTIPLIER = 180.0 / Math.PI;
    private static final float PITCH_MIN = -90f;
    private static final float PITCH_MAX = 90f;

    public ElytraTarget() {
        super("ElytraTarget");
        addSlider("Menzil", 20.0, 5.0, 50.0);
        addBoolean("Otomatik", true);
    }

    @Override
    public void onTick() {
        try {
            MinecraftClient mc = MinecraftClient.getInstance();
            if (mc == null || mc.player == null || mc.world == null) return;
            if (!mc.player.isFallFlying()) return;

            // Ayarları oku
            Double range = getSetting("Menzil");
            Boolean autoMode = getSetting("Otomatik");

            if (range == null) range = DEFAULT_RANGE;
            if (autoMode == null) autoMode = DEFAULT_AUTO;

            // Hedef bul
            LivingEntity target = findTarget(mc, range);
            if (target == null) return;

            // Hedefi kilitle
            lockOnTarget(mc, target);

        } catch (Exception e) {
            System.err.println("ElytraTarget module error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Menzil içinde en yakın hedefi bulur
     */
    private LivingEntity findTarget(MinecraftClient mc, double range) {
        if (mc.world == null || mc.player == null) return null;

        return mc.world.getEntitiesByClass(
                LivingEntity.class,
                mc.player.getBoundingBox().expand(range),
                e -> e != null 
                    && e != mc.player 
                    && e.isAlive()
        ).stream()
                .min(Comparator.comparingDouble(e -> mc.player.distanceTo(e)))
                .orElse(null);
    }

    /**
     * Hedefi kilit altına alır ve Yaw/Pitch ayarlar
     */
    private void lockOnTarget(MinecraftClient mc, LivingEntity target) {
        if (mc.player == null) return;

        try {
            // Hedef konumunu hesapla
            Vec3d playerPos = mc.player.getPos();
            Vec3d targetPos = target.getPos();
            
            if (playerPos == null || targetPos == null) return;

            // Dikey offset ile hedefi merkeze al
            double verticalOffset = target.getBoundingBox().getLengthY() / VERTICAL_OFFSET_DIVISOR;
            Vec3d diff = targetPos
                    .add(0, verticalOffset, 0)
                    .subtract(playerPos);

            // Yaw ve Pitch hesapla
            double horizontalDist = diff.horizontalLength();
            if (horizontalDist <= 0) return;

            float yaw = (float) (-MathHelper.atan2(diff.x, diff.z) * DEGREE_MULTIPLIER);
            float pitch = (float) (-MathHelper.atan2(diff.y, horizontalDist) * DEGREE_MULTIPLIER);

            // Açıları uygula
            if (mc.player.getYaw() != yaw) {
                mc.player.setYaw(yaw);
            }
            
            float clampedPitch = MathHelper.clamp(pitch, PITCH_MIN, PITCH_MAX);
            if (mc.player.getPitch() != clampedPitch) {
                mc.player.setPitch(clampedPitch);
            }

        } catch (Exception e) {
            System.err.println("Error locking on target: " + e.getMessage());
        }
    }
}
