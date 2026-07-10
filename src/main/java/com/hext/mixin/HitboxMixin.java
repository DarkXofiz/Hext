package com.hext.mixin;

import com.hext.client.Hext;
import com.hext.client.module.impl.combat.Hitbox;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityRendererMixin {

    @Inject(method = "getDimensions", at = @At("RETURN"), cancellable = true)
    private void expandDimensions(EntityPose pose, CallbackInfoReturnable<EntityDimensions> cir) {
        Hitbox mod = Hext.getInstance().getModuleManager()
                .getModule(Hitbox.class).orElse(null);
        if (mod == null || !mod.isEnabled()) return;

        MinecraftClient mc = MinecraftClient.getInstance();
        Entity self = (Entity)(Object)this;
        if (mc.player != null && self == mc.player) return;

        EntityDimensions orig = cir.getReturnValue();
        cir.setReturnValue(EntityDimensions.changing(
                (float)(orig.width()  * (1.0 + mod.getSize())),
                (float)(orig.height() * (1.0 + mod.getHeight()))
        ));
    }
}
