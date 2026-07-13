package com.hext.modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;

public class ElytraSwap extends BaseModule {
    private static final int CHEST_CONTAINER_SLOT = 6;
    private boolean wearingElytra = false;

    public ElytraSwap() { super("ElytraSwap"); }

    @Override
    public void onTick() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.interactionManager == null) return;

        // Inventory slot 6 = chest armor
        ItemStack chest = mc.player.getInventory().getStack(6);
        boolean hasElytra = chest.getItem() == Items.ELYTRA;
        int syncId = mc.player.currentScreenHandler.syncId;

        if (hasElytra && wearingElytra) {
            // Elytra giyili, zırh ile değiştir
            for (int i = 0; i < 9; i++) {
                ItemStack stack = mc.player.getInventory().getStack(i);
                if (stack.getItem() == Items.DIAMOND_CHESTPLATE || stack.getItem() == Items.NETHERITE_CHESTPLATE) {
                    mc.interactionManager.clickSlot(syncId, i + 36, 0, SlotActionType.PICKUP, mc.player);
                    mc.interactionManager.clickSlot(syncId, CHEST_CONTAINER_SLOT, 0, SlotActionType.PICKUP, mc.player);
                    mc.interactionManager.clickSlot(syncId, i + 36, 0, SlotActionType.PICKUP, mc.player);
                    wearingElytra = false;
                    break;
                }
            }
        } else if (!hasElytra && !wearingElytra) {
            // Zırh giyili, elytra ile değiştir
            for (int i = 0; i < 9; i++) {
                if (mc.player.getInventory().getStack(i).getItem() == Items.ELYTRA) {
                    mc.interactionManager.clickSlot(syncId, i + 36, 0, SlotActionType.PICKUP, mc.player);
                    mc.interactionManager.clickSlot(syncId, CHEST_CONTAINER_SLOT, 0, SlotActionType.PICKUP, mc.player);
                    mc.interactionManager.clickSlot(syncId, i + 36, 0, SlotActionType.PICKUP, mc.player);
                    wearingElytra = true;
                    break;
                }
            }
        }
    }
}
