package com.hextclient.client.module.impl.movement;

import com.hextclient.client.module.Module;
import com.hextclient.client.module.category.Category;
import net.minecraft.client.MinecraftClient;

public class AutoSprint extends Module {

    public AutoSprint() {
        super("AutoSprint", "Her zaman sprint yapar", Category.MOVEMENT);
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null) return;
        if (!client.player.isSprinting()
                && client.player.getHungerManager().getFoodLevel() > 6
                && !client.player.isSubmergedInWater()
                && !client.player.isSneaking()) {
            client.player.setSprinting(true);
        }
    }
}
