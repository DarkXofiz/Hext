package com.hext.mixin;

import com.hext.client.HextClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {

    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    private void onSendChatMessage(String message, net.minecraft.network.message.LastSeenMessageList.Acknowledgment acknowledgment, CallbackInfo ci) {
        if (Hext.getInstance().getCommandManager().handleMessage(message)) {
            ci.cancel();
        }
    }
}
