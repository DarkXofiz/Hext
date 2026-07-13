package com.hext.modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.Comparator;

public class ElytraTarget extends BaseModule {
    public ElytraTarget() { super("ElytraTarget"); }

    @Override
    public void onTick() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.world == null) return;
        if (!mc.player.isFallFlying()) return;

        LivingEntity target = mc.world.getEntitiesByClass(
                LivingEntity.class,
                mc.player.getBoundingBox().expand(30),
                e -> e != mc.player && e.isAlive()
        ).stream().min(Comparator.comparingDouble(e -> mc.player.distanceTo(e))).orElse(null);

        if (target == null) return;

        Vec3d diff = target.getPos()
                .subtract(mc.player.getPos())
                .add(0, target.getBoundingBox().getLengthY() / 2.0, 0);

        double horizontalDist = diff.horizontalLength();
        float yaw = (float) (-MathHelper.atan2(diff.x, diff.z) * 180.0 / Math.PI);
        float pitch = (float) (-MathHelper.atan2(diff.y, horizontalDist) * 180.0 / Math.PI);

        mc.player.setYaw(yaw);
        mc.player.setPitch(MathHelper.clamp(pitch, -90f, 90f));
    }
}
