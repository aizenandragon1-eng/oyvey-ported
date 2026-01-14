package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class AutoTotem extends Module {

    private long lastTime = 0;
    private final long delay = 150; // milliseconds

    public AutoTotem() {
        super("AutoTotem", "Automatically equips a totem in offhand", Category.COMBAT);
    }

    public void onUpdate() {
        if (mc.player == null || mc.level == null) return;

        // Delay
        if (System.currentTimeMillis() - lastTime < delay) return;
        lastTime = System.currentTimeMillis();

        // Offhand already has totem
        ItemStack offhand = mc.player.getOffhandItem();
        if (offhand.getItem() == Items.TOTEM_OF_UNDYING) return;

        // Find totem in inventory
        int totemSlot = -1;
        for (int i = 0; i < 36; i++) {
            ItemStack stack = mc.player.getInventory().getItem(i);
            if (stack.getItem() == Items.TOTEM_OF_UNDYING) {
                totemSlot = i;
                break;
            }
        }

        if (totemSlot == -1) return;

        // Convert inventory slot to window slot
        int windowSlot = totemSlot < 9 ? totemSlot + 36 : totemSlot;

        // Swap totem into offhand
        mc.gameMode.handleInventoryMouseClick(0, windowSlot, 0, net.minecraft.world.inventory.ClickType.PICKUP, mc.player);
        mc.gameMode.handleInventoryMouseClick(0, 45, 0, net.minecraft.world.inventory.ClickType.PICKUP, mc.player);
        mc.gameMode.handleInventoryMouseClick(0, windowSlot, 0, net.minecraft.world.inventory.ClickType.PICKUP, mc.player);
    }
}


