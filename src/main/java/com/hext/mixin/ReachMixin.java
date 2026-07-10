package com.hext.mixin;

import com.hext.client.Hext;
import com.hext.client.module.impl.combat.Reach;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public class ReachMixin {

    @Inject(method = "getBlockInteractionRange", at = @At("RETURN"), cancellable = true)
    private void onGetBlockRange(CallbackInfoReturnable<Double> cir) {
        Reach mod = Hext.getInstance().getModuleManager()
                .getModule(Reach.class).orElse(null);
        if (mod != null && mod.isEnabled()) {
            cir.setReturnValue(mod.getDistance());
        }
    }

    @Inject(method = "getEntityInteractionRange", at = @At("RETURN"), cancellable = true)
    private void onGetEntityRange(CallbackInfoReturnable<Double> cir) {
        Reach mod = Hext.getInstance().getModuleManager()
                .getModule(Reach.class).orElse(null);
        if (mod != null && mod.isEnabled()) {
            cir.setReturnValue(mod.getDistance());
        }
    }
}
