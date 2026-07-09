package com.hextclient.mixin;

import com.hextclient.client.HextClient;
import com.hextclient.client.gui.clickgui.ClickGUI;
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
