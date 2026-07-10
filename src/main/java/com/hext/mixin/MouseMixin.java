package com.hext.mixin;

import com.hext.client.Hext;
import com.hext.client.module.impl.movement.FreeLook;
import com.hext.client.module.impl.render.Keystrokes;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseMixin {

    @Inject(method = "updateMouse", at = @At("HEAD"), cancellable = true)
    private void onUpdateMouse(CallbackInfo ci) {
        FreeLook freelook = Hext.getInstance().getModuleManager()
                .getModule(FreeLook.class).orElse(null);
        if (freelook != null && freelook.isEnabled() && freelook.isActive()) {
            ci.cancel();
        }
    }

    @Inject(method = "onMouseButton", at = @At("HEAD"))
    private void onMouseButton(long window, int button, int action, int mods, CallbackInfo ci) {
        // action 1 = press
        if (action == 1 && (button == 0 || button == 1)) {
            Keystrokes ks = Hext.getInstance().getModuleManager()
                    .getModule(Keystrokes.class).orElse(null);
            if (ks != null && ks.isEnabled()) {
                ks.registerClick();
            }
        }
    }
}
