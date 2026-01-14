package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class AutoTotem extends Module {

    private long lastTime = 0;
    private final long delay = 100; // milliseconds between swaps

    public AutoTotem() {
        super("AutoTotem", "Automatically switches to Totem when needed", Category.COMBAT);
    }

    public void onUpdate() {
        if (mc.player == null || mc.level == null) return;

        if (System.currentTimeMillis() - lastTime < delay) return;
        lastTime = System.currentTimeMillis();

        // Check if player already holds a totem
        ItemStack mainHand = mc.player.getMainHandItem();
        ItemStack offHand = mc.player.getOffhandItem();

        boolean holdingTotem = mainHand.getItem() == Items.TOTEM_OF_UNDYING
                || offHand.getItem() == Items.TOTEM_OF_UNDYING;

        if (holdingTotem) return;

        // Find totem in inventory
        int totemSlot = findTotem();
        if (totemSlot == -1) return; // no totem found

        int windowSlot = totemSlot < 9 ? totemSlot + 36 : totemSlot;

        // Swap to totem in hotbar
        int currentSlot = mc.player.getInventory().selectedSlot;
        mc.gameMode.handleInventoryMouseClick(0, windowSlot, 0,
                net.minecraft.world.inventory.ClickType.PICKUP, mc.player);

        // Put totem in main hand
        int currentWindow = currentSlot < 9 ? currentSlot + 36 : currentSlot;
        mc.gameMode.handleInventoryMouseClick(0, currentWindow, 0,
                net.minecraft.world.inventory.ClickType.PICKUP, mc.player);
    }

    private int findTotem() {
        Inventory inv = mc.player.getInventory();
        for (int i = 0; i < 36; i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.getItem() == Items.TOTEM_OF_UNDYING) return i;
        }
        return -1;
    }
}



