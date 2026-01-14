package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class AutoAnchor extends Module {
    public long delay = 150; // default delay in ms

    public AutoAnchor() {
        super("AutoAnchor", "Automatically places crystals on target blocks", Category.COMBAT);
    }

    @Override
    public void onTick() {
        if (mc.player == null) return;

        int anchorSlot = mc.player.getInventory().selected;
        int glowSlot = findItem(Items.GLOWSTONE);

        if (glowSlot == -1) return;

        mc.player.getInventory().selected = glowSlot;
        // TODO: Add crystal placement logic here
        mc.player.getInventory().selected = anchorSlot; // switch back
    }

    private int findItem(Items target) {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.getInventory().getItem(i);
            if (stack.getItem() == target) return i;
        }
        return -1;
    }
}

