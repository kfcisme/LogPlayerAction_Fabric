package mw.wowkfccc.TISF.logPlayerAction_fabric.listener;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Items;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerBucketEmptyTracker {
    public static final Map<UUID, Integer> playerPlayerBucketEmptyCount = new HashMap<>();

    public static void register() {
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (!world.isClient && hand == Hand.MAIN_HAND && player instanceof ServerPlayerEntity serverPlayer) {
                if (player.getStackInHand(hand).getItem() instanceof BucketItem bucket && bucket != Items.BUCKET) {
                    UUID id = serverPlayer.getUuid();
                    playerPlayerBucketEmptyCount.put(id, playerPlayerBucketEmptyCount.getOrDefault(id, 0) + 1);
                    player.sendMessage(
                            net.minecraft.text.Text.literal("§6[playerPlayerBucketEmptyCount] §f你已合成 "
                                    + playerPlayerBucketEmptyCount.get(id) + " 次。"),
                            false
                    );}
            }
            return ActionResult.PASS;
        });
    }

    public static int getInsertData(UUID playerId) {
        return playerPlayerBucketEmptyCount.getOrDefault(playerId, 0);
    }

    public static void resetCounters(UUID playerId) {
        playerPlayerBucketEmptyCount.remove(playerId);
    }
}

