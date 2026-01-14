package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.Direction;

public class AutoAnchor extends Module {

    // Settings
    public Setting<Long> delay = register(new Setting<>("Delay", 150L, 50L, 1000L));
    public Setting<Boolean> autoSwitch = register(new Setting<>("AutoSwitchBack", true));
    public Setting<Boolean> healthCheck = register(new Setting<>("HealthCheck", false));
    public Setting<Float> minHealth = register(new Setting<>("MinHealth", 10f, 1f, 36f));
    public Setting<String> placeMode = register(new Setting<>("PlaceMode", "Above")); // "Above" or "Front"

    private long lastTime = 0;
    private int lastHotbar = 0;

    public AutoAnchor() {
        super("AutoAnchor", "Automatically places glowstone when holding anchor", Category.COMBAT);
    }

    public void onUpdate() {
        if (mc.player == null || mc.level == null) return;

        // Delay
        if (System.currentTimeMillis() - lastTime < delay.getValue()) return;
        lastTime = System.currentTimeMillis();

        // Health check
        if (healthCheck.getValue() && mc.player.getHealth() > minHealth.getValue()) return;

        // Check if holding anchor
        ItemStack hand = mc.player.getMainHandItem();
        if (hand.getItem() != Items.RESPAWN_ANCHOR) return;

        // Determine placement position
        BlockPos targetPos;
        if (placeMode.getValue().equalsIgnoreCase("Above")) {
            targetPos = mc.player.blockPosition().above();
        } else { // Front
            int x = mc.player.blockPosition().getX() + (int) mc.player.getLookAngle().x;
            int y = mc.player.blockPosition().getY();
            int z = mc.player.blockPosition().getZ() + (int) mc.player.getLookAngle().z;
            targetPos = new BlockPos(x, y, z);
        }

        // Find glowstone
        int glowSlot = findItem(Items.GLOWSTONE);
        if (glowSlot == -1) return;

        int windowSlot = glowSlot < 9 ? glowSlot + 36 : glowSlot;

        // Save current hotbar if autoSwitch enabled
        if (autoSwitch.getValue()) lastHotbar = mc.player.getInventory().selected;

        // Swap to glowstone
        mc.gameMode.handleInventoryMouseClick(0, windowSlot, 0, net.minecraft.world.inventory.ClickType.PICKUP, mc.player);

        // Place glowstone
        mc.player.swing(InteractionHand.MAIN_HAND);
        mc.gameMode.useItemOn(mc.player, mc.level, mc.player.getMainHandItem(),
                new BlockHitResult(new Vec3(targetPos.getX() + 0.5, targetPos.getY() + 0.5, targetPos.getZ() + 0.5),
                        Direction.UP, targetPos, false));

        // Swap back to anchor or previous item
        int anchorSlot = mc.player.getInventory().selected;
        int anchorWindow = anchorSlot < 9 ? anchorSlot + 36 : anchorSlot;
        mc.gameMode.handleInventoryMouseClick(0, anchorWindow, 0, net.minecraft.world.inventory.ClickType.PICKUP, mc.player);

        if (autoSwitch.getValue()) {
            int lastWindow = lastHotbar < 9 ? lastHotbar + 36 : lastHotbar;
            mc.gameMode.handleInventoryMouseClick(0, lastWindow, 0, net.minecraft.world.inventory.ClickType.PICKUP, mc.player);
        }
    }

    // Find item in inventory or hotbar
    private int findItem(Item target) {
        for (int i = 0; i < 36; i++) {
            ItemStack stack = mc.player.getInventory().getItem(i);
            if (stack.getItem() == target) return i;
        }
        return -1;
    }
}


