package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.util.Timer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.inventory.ClickType;

public class AutoTotem extends Module {

    private final Timer timer = new Timer();
    private final long delay = 150; // delay in milliseconds

    public AutoTotem() {
        super("AutoTotem", "Automatically equips a totem in offhand", Category.COMBAT);
    }

    @Override
    public void onUpdate() {

        // 1️⃣ Safety checks
        if (mc.player == null || mc.level == null) return;

        // 2️⃣ Only run if timer passed
        if (!timer.passedMs(delay)) return;
        timer.reset();

        // 3️⃣ Check if offhand already has a Totem
        ItemStack offhand = mc.player.getOffhandItem();
        if (offhand.getItem() == Items.TOTEM_OF_UNDYING) return;

        // 4️⃣ Search entire inventory (0-35)
        int totemSlot = -1;
        for (int i = 0; i < 36; i++) {
            ItemStack stack = mc.player.getInventory().getItem(i);
            if (stack.getItem() == Items.TOTEM_OF_UNDYING) {
                totemSlot = i;
                break;
            }
        }

        // 5️⃣ No totem found → stop
        if (totemSlot == -1) return;

        // 6️⃣ Convert slot for window click
        int windowSlot = totemSlot < 9 ? totemSlot + 36 : totemSlot;

        // 7️⃣ Swap totem into offhand
        mc.gameMode.handleInventoryMouseClick(0, windowSlot, 0, ClickType.PICKUP, mc.player);
        mc.gameMode.handleInventoryMouseClick(0, 45, 0, ClickType.PICKUP, mc.player);
        mc.gameMode.handleInventoryMouseClick(0, windowSlot, 0, ClickType.PICKUP, mc.player);
    }
}

