package mw.wowkfccc.TISF.logPlayerAction_fabric.listener;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.Blocks;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BucketFillListener {
    private static final Map<UUID, Integer> playerBucketFillCount = new HashMap<>();

    public static void register() {
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (world.isClient || !(player instanceof ServerPlayerEntity serverPlayer)) return ActionResult.PASS;

            // 玩家是否拿著空桶
            if (player.getStackInHand(hand).getItem() == Items.BUCKET) {
                // 是否點擊到水方塊
                BlockPos pos = ((BlockHitResult) hitResult).getBlockPos();
                if (world.getBlockState(pos).getBlock() == Blocks.WATER) {
                    UUID uuid = player.getUuid();
                    playerBucketFillCount.put(uuid, playerBucketFillCount.getOrDefault(uuid, 0) + 1);
//                    player.sendMessage(
//                            net.minecraft.text.Text.literal("§6[水桶填滿] §f你已填滿水桶 "
//                                    + playerBucketFillCount.get(uuid) + " 次。"),
//                            false
//                    );
                }
            }
            return ActionResult.PASS;
        });
    }

    public static int get(UUID playerId) {
        return playerBucketFillCount.getOrDefault(playerId, 0);
    }

    public static void reset(UUID playerId) {
        playerBucketFillCount.remove(playerId);
    }
}
