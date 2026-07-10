package com.hext.mixin;

import com.hext.client.Hext;
import com.hext.client.module.impl.combat.Reach;
import com.hext.client.module.impl.misc.NoHurtCam;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(method = "bobViewWhenHurt", at = @At("HEAD"), cancellable = true)
    private void onBobViewWhenHurt(net.minecraft.client.util.math.MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        NoHurtCam mod = Hext.getInstance().getModuleManager()
                .getModule(NoHurtCam.class).orElse(null);
        if (mod != null && mod.isEnabled()) {
            ci.cancel();
        }
    }

    // findCrosshairTarget'a geçilen reach distance'ı override et
    @ModifyArg(
        method = "findCrosshairTarget",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;raycast(DFZ)Lnet/minecraft/util/hit/HitResult;"),
        index = 0
    )
    private double modifyRaycastDistance(double original) {
        Reach mod = Hext.getInstance().getModuleManager()
                .getModule(Reach.class).orElse(null);
        if (mod != null && mod.isEnabled()) {
            return mod.getDistance();
        }
        return original;
    }
}
