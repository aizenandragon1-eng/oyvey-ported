package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.Direction;

public class AutoAnchor extends Module {

    private long lastTime = 0;
    private final long delay = 150; // milliseconds
    private int lastHotbar = 0;

    public AutoAnchor() {
        super("AutoAnchor", "Places glowstone automatically when holding anchor", Category.COMBAT);
    }

    public void onUpdate() {
        if (mc.player == null || mc.level == null) return;

        if (System.currentTimeMillis() - lastTime < delay) return;
        lastTime = System.currentTimeMillis();

        // Check if holding anchor
        ItemStack hand = mc.player.getMainHandItem();
        if (hand.getItem() != Items.RESPAWN_ANCHOR) return;

        // Place position: above player
        BlockPos targetPos = mc.player.blockPosition().above();

        // Find glowstone in inventory
        int glowSlot = findItem(Items.GLOWSTONE);
        if (glowSlot == -1) return;

        int windowSlot = glowSlot < 9 ? glowSlot + 36 : glowSlot;

        // Swap to glowstone
        mc.gameMode.handleInventoryMouseClick(0, windowSlot, 0, net.minecraft.world.inventory.ClickType.PICKUP, mc.player);

        // Place glowstone
        mc.player.swing(InteractionHand.MAIN_HAND);
        mc.gameMode.useItemOn(mc.player, InteractionHand.MAIN_HAND,
                new BlockHitResult(new Vec3(targetPos.getX() + 0.5, targetPos.getY() + 0.5, targetPos.getZ() + 0.5),
                        Direction.UP, targetPos, false));

        // Swap back to anchor
        int anchorSlot = mc.player.getInventory().selectedSlot; // works in your fork
        int anchorWindow = anchorSlot < 9 ? anchorSlot + 36 : anchorSlot;
        mc.gameMode.handleInventoryMouseClick(0, anchorWindow, 0, net.minecraft.world.inventory.ClickType.PICKUP, mc.player);
    }

    private int findItem(Items target) {
        for (int i = 0; i < 36; i++) {
            ItemStack stack = mc.player.getInventory().getItem(i);
            if (stack.getItem() == target) return i;
        }
        return -1;
    }
}
