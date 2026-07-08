package com.darkclient.mixin;

import com.darkclient.client.DarkClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {

    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    private void onSendChatMessage(String message, net.minecraft.network.message.LastSeenMessageList.Acknowledgment acknowledgment, CallbackInfo ci) {
        if (DarkClient.getInstance().getCommandManager().handleMessage(message)) {
            ci.cancel();
        }
    }
}
