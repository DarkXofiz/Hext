package com.hext.modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;

public class ElytraSwap extends BaseModule {
    public ElytraSwap() { super("ElytraSwap"); }
    private boolean state = false;

    @Override
    public void onTick() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.interactionManager == null) return;
        ItemStack chest = mc.player.getInventory().getArmorStack(EquipmentSlot.CHEST.getEntitySlotId());
        if (chest.getItem() == Items.ELYTRA) {
            if (state) {
                for (int i = 0; i < 9; i++) {
                    ItemStack stack = mc.player.getInventory().getStack(i);
                    if (stack.getItem() == Items.DIAMOND_CHESTPLATE || stack.getItem() == Items.NETHERITE_CHESTPLATE) {
                        mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, i, 0, SlotActionType.PICKUP, mc.player);
                        mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, 6, 0, SlotActionType.PICKUP, mc.player);
                        state = false;
                        break;
                    }
                }
            }
        } else {
            if (!state) {
                for (int i = 0; i < 9; i++) {
                    if (mc.player.getInventory().getStack(i).getItem() == Items.ELYTRA) {
                        mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, i, 0, SlotActionType.PICKUP, mc.player);
                        mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, 6, 0, SlotActionType.PICKUP, mc.player);
                        state = true;
                        break;
                    }
                }
            }
        }
    }
}
