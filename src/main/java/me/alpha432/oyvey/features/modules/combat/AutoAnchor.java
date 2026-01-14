package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public class AutoAnchor extends Module {

    private long lastTime = 0;
    private final long delay = 150; // milliseconds

    public AutoAnchor() {
        super("AutoAnchor", "Places glowstone automatically when holding anchor", Category.COMBAT);
    }

    public void onUpdate() {
        if (mc.player == null || mc.level == null) return;

        // Delay
        if (System.currentTimeMillis() - lastTime < delay) return;
        lastTime = System.currentTimeMillis();

        // Check if holding anchor
        ItemStack hand = mc.player.getMainHandItem();
        if (hand.getItem() != Items.RESPAWN_ANCHOR) return;

        // Target position (above player)
        BlockPos placePos = mc.player.blockPosition().above();

        // Find glowstone in inventory
        int glowSlot = findItem(Items.GLOWSTONE);
        if (glowSlot == -1) return;

        int windowSlot = glowSlot < 9 ? glowSlot + 36 : glowSlot;

        // Swap to glowstone
        mc.gameMode.handleInventoryMouseClick(0, windowSlot, 0, net.minecraft.world.inventory.ClickType.PICKUP, mc.player);

        // Place glowstone above anchor
        mc.player.swing(net.minecraft.world.InteractionHand.MAIN_HAND); // swing hand
        mc.gameMode.useItemOn(mc.player, mc.level, mc.player.getMainHandItem(),
                new net.minecraft.world.phys.BlockHitResult(
                        new Vec3(placePos.getX() + 0.5, placePos.getY() + 0.5, placePos.getZ() + 0.5),
                        net.minecraft.core.Direction.UP,
                        placePos,
                        false
                )
        );

        // Swap back to anchor
        int anchorSlot = mc.player.getInventory().selected;
        int anchorWindow = anchorSlot < 9 ? anchorSlot + 36 : anchorSlot;
        mc.gameMode.handleInventoryMouseClick(0, anchorWindow, 0, net.minecraft.world.inventory.ClickType.PICKUP, mc.player);
    }

    // Find item in inventory
    private int findItem(Item target) {
        for (int i = 0; i < 36; i++) {
            ItemStack stack = mc.player.getInventory().getItem(i);
            if (stack.getItem() == target) return i;
        }
        return -1;
    }
}

