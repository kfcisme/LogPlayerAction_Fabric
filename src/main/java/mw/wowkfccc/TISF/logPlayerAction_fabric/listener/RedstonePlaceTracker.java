package mw.wowkfccc.TISF.logPlayerAction_fabric.listener;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;

public class RedstonePlaceTracker {
    public static final Map<UUID, Integer> playerRedstoneCount = new HashMap<>();

    private static final Set<Block> REDSTONE_BLOCKS = Set.of(
            Blocks.REDSTONE_WIRE,
            Blocks.REDSTONE_TORCH,
            Blocks.REPEATER,
            Blocks.COMPARATOR,
            Blocks.REDSTONE_BLOCK,
            Blocks.RAIL,
            Blocks.DETECTOR_RAIL,
            Blocks.ACTIVATOR_RAIL,
            Blocks.POWERED_RAIL,
            Blocks.HOPPER,
            Blocks.DROPPER,
            Blocks.DISPENSER,
            Blocks.PISTON,
            Blocks.STICKY_PISTON,
            Blocks.TARGET,
            Blocks.LEVER,
            Blocks.OAK_PRESSURE_PLATE,
            Blocks.SPRUCE_PRESSURE_PLATE,
            Blocks.BIRCH_PRESSURE_PLATE,
            Blocks.JUNGLE_PRESSURE_PLATE,
            Blocks.ACACIA_PRESSURE_PLATE,
            Blocks.DARK_OAK_PRESSURE_PLATE,
            Blocks.CRIMSON_PRESSURE_PLATE,
            Blocks.WARPED_PRESSURE_PLATE,
            Blocks.STONE_PRESSURE_PLATE,
            Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE,
            Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE,
            Blocks.REDSTONE_LAMP,
            Blocks.OAK_DOOR,
            Blocks.IRON_DOOR,
            Blocks.OBSERVER,
            Blocks.DAYLIGHT_DETECTOR,
            Blocks.BELL,
            Blocks.LECTERN,
            Blocks.TRAPPED_CHEST,
            Blocks.CHEST,
            Blocks.BARREL,
            Blocks.TRIPWIRE_HOOK,
            Blocks.TRIPWIRE,
            Blocks.SLIME_BLOCK,
            Blocks.OAK_FENCE_GATE,
            Blocks.NOTE_BLOCK,
            Blocks.OAK_TRAPDOOR,
            Blocks.IRON_TRAPDOOR,
            Blocks.BAMBOO_TRAPDOOR,
            Blocks.POLISHED_BLACKSTONE_BUTTON,
            Blocks.OAK_BUTTON,
            Blocks.SPRUCE_BUTTON,
            Blocks.BIRCH_BUTTON,
            Blocks.JUNGLE_BUTTON,
            Blocks.ACACIA_BUTTON,
            Blocks.DARK_OAK_BUTTON,
            Blocks.CRIMSON_BUTTON,
            Blocks.WARPED_BUTTON,
            Blocks.STONE_BUTTON
    );

    public static void register() {
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (!(player instanceof ServerPlayerEntity serverPlayer)) return ActionResult.PASS;

            if (hand != Hand.MAIN_HAND) return ActionResult.PASS;

            BlockPos pos = hitResult.getBlockPos().offset(hitResult.getSide());
            if (!world.getBlockState(pos).isAir()) return ActionResult.PASS;

            Block heldBlock = Block.getBlockFromItem(player.getStackInHand(hand).getItem());
            if (REDSTONE_BLOCKS.contains(heldBlock)) {
                UUID playerId = serverPlayer.getUuid();
                playerRedstoneCount.put(playerId,
                        playerRedstoneCount.getOrDefault(playerId, 0) + 1);

                // Debug log (optional)
                player.sendMessage(
                        net.minecraft.text.Text.literal("§6[playerRedstoneCount] §f你已合成 "
                                + playerRedstoneCount.get(playerId) + " 次。"),
                        false
                );

            }

            return ActionResult.PASS;
        });
    }

    public static int sendInsertData(UUID playerId) {
        return playerRedstoneCount.getOrDefault(playerId, 0);
    }

    public static void resetCounters(UUID playerId) {
        playerRedstoneCount.remove(playerId);
    }
}