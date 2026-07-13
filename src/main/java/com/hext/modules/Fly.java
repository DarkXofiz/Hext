package com.hext.modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;

public class Fly extends BaseModule {
    private static final float SPEED_DIVISOR = 10.0f;

    public Fly() {
        super("Fly");
        // Ayarları ekle
        addBoolean("Müzik", true);
        addSlider("Hız", 1.0, 0.1, 5.0);
        addText("Komut", "fly");
    }

    @Override
    public void onEnable() {
        PlayerEntity p = getPlayer();
        if (p != null && p.getAbilities() != null) {
            p.getAbilities().flying = true;
            p.getAbilities().allowFlying = true;
        }
    }

    @Override
    public void onDisable() {
        PlayerEntity p = getPlayer();
        if (p != null && p.getAbilities() != null && !p.isCreative()) {
            p.getAbilities().flying = false;
            p.getAbilities().allowFlying = false;
        }
    }

    @Override
    public void onTick() {
        PlayerEntity p = getPlayer();
        if (p == null || p.getAbilities() == null) return;

        p.getAbilities().flying = true;
        p.getAbilities().allowFlying = true;
        p.fallDistance = 0;

        // Hız ayarını oku
        try {
            Double speed = getSetting("Hız");
            if (speed != null && speed > 0) {
                p.getAbilities().setFlySpeed((float) (speed / SPEED_DIVISOR));
            }
        } catch (Exception e) {
            System.err.println("Fly module speed setting error: " + e.getMessage());
        }

        // Müzik ayarını kontrol et
        Boolean music = getSetting("Müzik");
        if (music != null && music) {
            // Müzik logic buraya gelecek
        }
    }

    /**
     * Null-safe player getter
     */
    private PlayerEntity getPlayer() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) return null;
        return client.player;
    }
}
