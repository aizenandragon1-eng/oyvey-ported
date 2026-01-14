package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class AutoAnchor extends Module {
    public Setting<Long> delay = register(new Setting<>("Delay", 150L, 50L, 1000L));

    public AutoAnchor() {
        super("AutoAnchor", "Automatically places crystals on target blocks", Category.COMBAT, true, false, false);
    }

    @Override
    public void onTick() {
        if (mc.player == null) return;

        int anchorSlot = mc.player.getInventory().getSelected();
        int glowSlot = findItem(Items.GLOWSTONE);

        if (glowSlot == -1) return;

        mc.player.getInventory().setSelected(glowSlot);
        // TODO: Place glowstone or anchor block logic here
        mc.player.getInventory().setSelected(anchorSlot); // switch back
    }

    private int findItem(Items target) {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.getInventory().getItem(i);
            if (stack.getItem() == target) return i;
        }
        return -1;
    }
}
