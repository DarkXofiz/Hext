package com.hext.mixin;

import com.hext.HextClient;
import com.hext.modules.Hitbox;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class MixinEntity {

    @Inject(method = "getBoundingBox", at = @At("RETURN"), cancellable = true)
    private void onGetBoundingBox(CallbackInfoReturnable<Box> cir) {
        if (HextClient.mc == null || HextClient.mc.player == null) return;

        boolean hitboxEnabled = HextClient.modules.stream()
                .anyMatch(m -> m instanceof Hitbox && m.enabled);

        if (hitboxEnabled && (Object) this != HextClient.mc.player) {
            Box original = cir.getReturnValue();
            double expand = 0.8;
            cir.setReturnValue(original.expand(expand, expand, expand));
        }
    }
}
