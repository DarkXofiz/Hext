package com.hext.mixin;

import com.hext.HextClient;
import com.hext.modules.Fly;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class MixinClientPlayerEntity {
    @Inject(method = "tickMovement", at = @At("HEAD"), cancellable = true)
    private void onTickMovement(CallbackInfo ci) {
        if (HextClient.mc.player == null) return;
        boolean flyEnabled = HextClient.modules.stream().anyMatch(m -> m instanceof Fly && m.enabled);
        if (flyEnabled) {
            HextClient.mc.player.getAbilities().flying = true;
            HextClient.mc.player.fallDistance = 0;
        }
    }
}
