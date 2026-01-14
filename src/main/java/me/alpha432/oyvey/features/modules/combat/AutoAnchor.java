package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;

public class AutoAnchor extends Module {

    public AutoAnchor() {
        super("AutoAnchor", "Automatically places glowstone before placing an end crystal", Category.COMBAT);
    }

    @Override
    public void onTick() {
        if (mc.player == null) return;

        int glowSlot = findGlowstoneSlot();
        int anchorSlot = mc.player.getInventory().selectedSlot;

        if (glowSlot != -1) {
            mc.player.getInventory().selectedSlot = glowSlot;
            BlockPos placePos = mc.player.blockPosition().above();
            mc.gameMode.useItemOn(mc.player, InteractionHand.MAIN_HAND, new BlockHitResult(Vec3.atCenterOf(placePos), null, placePos, false));
        }

        // Switch back to original slot
        mc.player.getInventory().selectedSlot = anchorSlot;
    }

    private int findGlowstoneSlot() {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.getInventory().getItem(i);
            if (stack != null && stack.getItem() == Items.GLOWSTONE) return i;
        }
        return -1;
    }
}
