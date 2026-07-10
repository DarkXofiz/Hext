package com.hext.mixin;

import com.hext.client.Hext;
import com.hext.client.module.impl.misc.NameTags;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    @Inject(method = "getNameForScoreboard", at = @At("RETURN"), cancellable = true)
    private void onGetName(CallbackInfoReturnable<Text> cir) {
        NameTags mod = Hext.getInstance().getModuleManager()
                .getModule(NameTags.class).orElse(null);
        if (mod == null || !mod.isEnabled()) return;

        PlayerEntity self = (PlayerEntity)(Object)this;
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player != null && mc.player.equals(self)) return;

        String name = self.getName().getString();
        float hp = self.getHealth();
        String display = name;
        if (mod.shouldShowHealth()) display += " §c" + (int)hp + "❤";
        if (mod.shouldShowDistance() && mc.player != null) {
            int dist = (int)mc.player.distanceTo(self);
            display += " §7" + dist + "m";
        }
        cir.setReturnValue(Text.literal(display));
    }
}
