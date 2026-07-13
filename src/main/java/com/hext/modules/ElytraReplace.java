package com.hext.modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;

public class ElytraReplace extends BaseModule {
    // Chest armor slot = container slot 6 (0=feet,1=legs,2=chest,3=head ama container'da ters)
    private static final int CHEST_CONTAINER_SLOT = 6;

    public ElytraReplace() { super("ElytraReplace"); }

    @Override
    public void onTick() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.interactionManager == null) return;

        // Chest slot'ta zaten elytra varsa bir şey yapma
        ItemStack chest = mc.player.getInventory().getStack(6);
        if (chest.getItem() == Items.ELYTRA) return;

        int syncId = mc.player.currentScreenHandler.syncId;

        // Hotbarda elytra ara
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (stack.getItem() == Items.ELYTRA) {
                mc.interactionManager.clickSlot(syncId, i + 36, 0, SlotActionType.PICKUP, mc.player);
                mc.interactionManager.clickSlot(syncId, CHEST_CONTAINER_SLOT, 0, SlotActionType.PICKUP, mc.player);
                mc.interactionManager.clickSlot(syncId, i + 36, 0, SlotActionType.PICKUP, mc.player);
                break;
            }
        }
    }
}
