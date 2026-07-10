package com.hext.mixin;

import com.hext.client.Hext;
import com.hext.client.module.impl.combat.Hitbox;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Entity.class)
public class EntityRendererMixin {

    @ModifyVariable(method = "getBoundingBox", at = @At("RETURN"), ordinal = 0)
    private Box expandHitbox(Box box) {
        Hitbox mod = Hext.getInstance().getModuleManager()
                .getModule(Hitbox.class).orElse(null);
        if (mod == null || !mod.isEnabled()) return box;
        Entity self = (Entity)(Object)this;
        // Don't expand self
        net.minecraft.client.MinecraftClient mc = net.minecraft.client.MinecraftClient.getInstance();
        if (mc.player != null && mc.player.equals(self)) return box;
        double s = mod.getSize();
        double h = mod.getHeight();
        return box.expand(s, h, s);
    }
}
