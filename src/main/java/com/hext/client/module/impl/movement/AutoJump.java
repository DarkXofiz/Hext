package com.hext.client.module.impl.movement;

import com.hext.client.module.Module;
import com.hext.client.module.category.Category;
import net.minecraft.client.MinecraftClient;

public class AutoJump extends Module {

    public AutoJump() {
        super("AutoJump", "Otomatik zıplar", Category.MOVEMENT);
    }

    @Override
    public void onEnable() {
        if (MinecraftClient.getInstance().options != null)
            MinecraftClient.getInstance().options.getAutoJump().setValue(true);
    }

    @Override
    public void onDisable() {
        if (MinecraftClient.getInstance().options != null)
            MinecraftClient.getInstance().options.getAutoJump().setValue(false);
    }
}
