package com.hext.mixin;

import com.hext.client.Hext;
import com.hext.client.module.impl.render.Keystrokes;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    @Inject(method = "render", at = @At("TAIL"))
    private void onRender(DrawContext ctx, net.minecraft.client.render.RenderTickCounter tickCounter, CallbackInfo ci) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) return;

        Keystrokes ks = Hext.getInstance().getModuleManager()
                .getModule(Keystrokes.class).orElse(null);
        if (ks != null) {
            ks.render(ctx, mc);
        }
    }
}
