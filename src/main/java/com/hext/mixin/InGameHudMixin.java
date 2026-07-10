package com.hext.mixin;

import com.hext.client.Hext;
import com.hext.client.module.impl.misc.NameTags;
import com.hext.client.module.impl.player.ArmorStatus;
import com.hext.client.module.impl.render.Keystrokes;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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

        // Keystrokes
        Keystrokes ks = Hext.getInstance().getModuleManager().getModule(Keystrokes.class).orElse(null);
        if (ks != null) ks.render(ctx, mc);

        // ArmorStatus
        ArmorStatus armor = Hext.getInstance().getModuleManager().getModule(ArmorStatus.class).orElse(null);
        if (armor != null && armor.isEnabled()) renderArmorStatus(ctx, mc, armor);
    }

    private void renderArmorStatus(DrawContext ctx, MinecraftClient mc, ArmorStatus armor) {
        if (mc.player == null) return;
        int screenW = mc.getWindow().getScaledWidth();
        int screenH = mc.getWindow().getScaledHeight();

        int x, y;
        String pos = armor.getPosition();
        switch (pos) {
            case "Top-Left"     -> { x = 5;           y = 5; }
            case "Top-Right"    -> { x = screenW - 90; y = 5; }
            case "Bottom-Left"  -> { x = 5;           y = screenH - 80; }
            default             -> { x = screenW - 90; y = screenH - 80; }
        }

        net.minecraft.entity.EquipmentSlot[] slots = {
            net.minecraft.entity.EquipmentSlot.HEAD,
            net.minecraft.entity.EquipmentSlot.CHEST,
            net.minecraft.entity.EquipmentSlot.LEGS,
            net.minecraft.entity.EquipmentSlot.FEET
        };

        int i = 0;
        for (net.minecraft.entity.EquipmentSlot slot : slots) {
            ItemStack stack = mc.player.getEquippedStack(slot);
            if (stack.isEmpty()) { i++; continue; }
            ctx.drawItem(stack, x, y + i * 18);
            if (armor.shouldShowDurability() && stack.isDamageable()) {
                int dur = stack.getMaxDamage() - stack.getDamage();
                int color = dur < stack.getMaxDamage() * 0.25 ? 0xFFFF4444 :
                            dur < stack.getMaxDamage() * 0.5  ? 0xFFFFAA00 : 0xFF55FF55;
                ctx.drawTextWithShadow(mc.textRenderer, String.valueOf(dur), x + 18, y + i * 18 + 4, color);
            }
            i++;
        }
    }
}
