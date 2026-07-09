package com.hextclient.mixin;

import com.hextclient.client.HextClient;
import com.hextclient.client.module.impl.misc.NoHurtCam;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(method = "bobViewWhenHurt", at = @At("HEAD"), cancellable = true)
    private void onBobViewWhenHurt(net.minecraft.client.util.math.MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        NoHurtCam mod = HextClient.getInstance().getModuleManager()
                .getModule(NoHurtCam.class).orElse(null);
        if (mod != null && mod.isEnabled()) {
            ci.cancel();
        }
    }
}
