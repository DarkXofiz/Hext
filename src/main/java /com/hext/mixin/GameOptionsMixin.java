package com.darkclient.mixin;

import com.darkclient.client.DarkClient;
import com.darkclient.client.gui.clickgui.ClickGUI;
import net.minecraft.client.option.GameOptions;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameOptions.class)
public class GameOptionsMixin {

    @Inject(method = "processOptions", at = @At("HEAD"))
    private void onProcessOptions(CallbackInfo ci) {
        // Hook for future option processing if needed
    }
}
