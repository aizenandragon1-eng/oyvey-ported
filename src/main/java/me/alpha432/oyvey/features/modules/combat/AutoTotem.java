package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.inventory.ClickType;

public class AutoTotem extends Module {

    public AutoTotem() {
        super("AutoTotem", "Automatically equips a totem in offhand", Category.COMBAT);
    }

    @Override
    public void onUpdate() {

        // 1. Safety checks
        if (mc.player == null || mc.level == null) return;

        // 2. If offhand already has a totem, do nothing
        ItemStack offhand = mc.player.getOffhandItem();
        if (offhand.getItem() == Items.TOTEM_OF_UNDYING) return;

        // 3. Find a totem in inventory
        int totemSlot = -1;

        for (int i = 0; i < 36; i++) {
            ItemStack stack = mc.player.getInventory().getItem(i);
            if (stack.getItem() == Items.TOTEM_OF_UNDYING) {
                totemSlot = i;
                break;
            }
        }

        // 4. If no totem found, stop
        if (totemSlot == -1) return;

        // 5. Convert inventory slot to window slot
        int windowSlot = totemSlot < 9 ? totemSlot + 36 : totemSlot;

        // 6. Swap totem into offhand
        mc.gameMode.handleInventoryMouseClick(0, windowSlot, 0, ClickType.PICKUP, mc.player);
        mc.gameMode.handleInventoryMouseClick(0, 45, 0, ClickType.PICKUP, mc.player);
        mc.gameMode.handleInventoryMouseClick(0, windowSlot, 0, ClickType.PICKUP, mc.player);
    }
}
