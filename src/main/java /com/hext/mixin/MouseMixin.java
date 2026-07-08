package com.darkclient.mixin;

import com.darkclient.client.DarkClient;
import com.darkclient.client.module.impl.movement.FreeLook;
import net.minecraft.client.Mouse;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseMixin {

    @Inject(method = "updateMouse", at = @At("HEAD"), cancellable = true)
    private void onUpdateMouse(CallbackInfo ci) {
        FreeLook freelook = DarkClient.getInstance().getModuleManager()
                .getModule(FreeLook.class).orElse(null);
        if (freelook == null || !freelook.isEnabled() || !freelook.isActive()) return;
        // Cancel normal mouse rotation; rotation handled via dedicated logic
        ci.cancel();
    }
}
