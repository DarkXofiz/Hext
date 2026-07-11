package com.hext.mixin;

import com.hext.client.Hext;
import com.hext.client.module.impl.misc.NoHurtCam;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(method = "bobViewWhenHurt", at = @At("HEAD"), cancellable = true)
    private void onBobViewWhenHurt(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        NoHurtCam mod = Hext.getInstance().getModuleManager()
                .getModule(NoHurtCam.class).orElse(null);
        if (mod != null && mod.isEnabled()) ci.cancel();
    }

    // Not: Reach için burada ayrı bir hack yok artık.
    // Reach mesafesi ReachMixin uzerinden ClientPlayerInteractionManager'in
    // getBlockInteractionRange()/getEntityInteractionRange() metodlariyla dogru
    // sekilde uygulaniyor (1.20.5+/1.21.1'de reach artik attribute tabanli).
    // Buradaki eski findCrosshairTarget local-variable hack'i guncel mappinglerle
    // eslesmiyordu ve yanlis bir degiskeni hedefleyip crosshair/reach davranisini
    // bozuyor olabilirdi, bu yuzden kaldirildi.
}
