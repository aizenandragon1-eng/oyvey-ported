package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class AutoTotem extends Module {
    public Setting<Integer> health = register(new Setting<>("Health", 16, 1, 36));

    public AutoTotem() {
        super("AutoTotem", "Automatically switches to totem", Category.COMBAT, true, false, false);
    }

    @Override
    public void onTick() {
        if (mc.player == null) return;

        int totemSlot = findTotemSlot();
        if (totemSlot != -1 && mc.player.getInventory().getSelected() != totemSlot) {
            mc.player.getInventory().setSelected(totemSlot);
        }
    }

    private int findTotemSlot() {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.getInventory().getItem(i);
            if (stack.getItem() == Items.TOTEM_OF_UNDYING) return i;
        }
        return -1;
    }
}
