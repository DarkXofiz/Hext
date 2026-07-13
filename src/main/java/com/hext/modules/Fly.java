package com.hext.modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;

public class Fly extends BaseModule {
    public Fly() { super("Fly"); }

    @Override
    public void onEnable() {
        PlayerEntity p = MinecraftClient.getInstance().player;
        if (p != null) {
            p.getAbilities().flying = true;
            p.getAbilities().allowFlying = true;
        }
    }

    @Override
    public void onDisable() {
        PlayerEntity p = MinecraftClient.getInstance().player;
        if (p != null && !p.isCreative() && !p.isSpectator()) {
            p.getAbilities().flying = false;
            p.getAbilities().allowFlying = false;
        }
    }

    @Override
    public void onTick() {
        PlayerEntity p = MinecraftClient.getInstance().player;
        if (p == null) return;
        p.getAbilities().flying = true;
        p.getAbilities().allowFlying = true;
        p.fallDistance = 0;
    }
}
