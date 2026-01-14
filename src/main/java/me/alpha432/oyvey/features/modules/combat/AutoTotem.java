package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;

public class AutoTotem extends Module {

    public AutoTotem() {
        super("AutoTotem", "Automatically switches to totem", Category.COMBAT);
    }

    @Override
    public void onTick() {
        if (mc.player == null) return;

        int totemSlot = findTotemSlot();
        if (totemSlot != -1 && mc.player.getInventory().selectedSlot != totemSlot) {
            mc.player.getInventory().selectedSlot = totemSlot;
        }
    }

    private int findTotemSlot() {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.getInventory().getItem(i);
            if (stack != null && stack.getItem() == Items.TOTEM_OF_UNDYING) return i;
        }
        return -1;
    }
}
