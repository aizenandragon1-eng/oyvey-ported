package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.util.Timer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public class AutoAnchor extends Module {

    private final Timer timer = new Timer();
    private long delay = 150; // delay in milliseconds

    public AutoAnchor() {
        super("AutoAnchor", "Automatically places glowstone when holding anchor", Category.COMBAT);
    }

    @Override
    public void onUpdate() {
        if (mc.player == null || mc.level == null) return;
        if (!timer.passedMs(delay)) return;

        // 1️⃣ Check if player is holding a respawn anchor
        ItemStack hand = mc.player.getMainHandItem();
        if (hand.getItem() != Items.RESPAWN_ANCHOR) return;

        // 2️⃣ Find nearest anchor block (optional, or just below player)
        BlockPos targetPos = mc.player.blockPosition(); // place at your feet
        BlockPos placePos = targetPos.above();

        // 3️⃣ Find glowstone in hotbar or inventory
        int glowSlot = findItem(Items.GLOWSTONE);
        if (glowSlot == -1) return;

        // 4️⃣ Swap to glowstone
        int windowSlot = glowSlot < 9 ? glowSlot + 36 : glowSlot;
        mc.gameMode.handleInventoryMouseClick(0, windowSlot, 0, net.minecraft.world.inventory.ClickType.PICKUP, mc.player);

        // 5️⃣ Place glowstone above anchor
        mc.playerController.processRightClickBlock(mc.player, mc.level, placePos, null, new Vec3(0.5,0.5,0.5), net.minecraft.core.Direction.UP);

        // 6️⃣ Swap back to anchor
        int anchorSlot = mc.player.getInventory().selected;
        mc.gameMode.handleInventoryMouseClick(0, anchorSlot < 9 ? anchorSlot + 36 : anchorSlot, 0, net.minecraft.world.inventory.ClickType.PICKUP, mc.player);

        // 7️⃣ Optional: left click the anchor
        mc.playerController.attack(mc.level.getBlockState(targetPos).getBlock(), targetPos);

        // Reset timer
        timer.reset();
    }

    // Find item in inventory or hotbar
    private int findItem(Items item) {
        for (int i = 0; i < 36; i++) {
            ItemStack stack = mc.player.getInventory().getItem(i);
            if (stack.getItem() == item) return i;
        }
        return -1;
    }
}
