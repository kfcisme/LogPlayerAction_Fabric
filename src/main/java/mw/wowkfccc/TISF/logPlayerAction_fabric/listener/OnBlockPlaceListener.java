//package mw.wowkfccc.TISF.logPlayerAction_fabric.listener;
//
//import net.fabricmc.fabric.api.event.player.UseBlockCallback;
//import net.minecraft.util.ActionResult;
//import net.minecraft.util.Hand;
//import net.minecraft.util.hit.BlockHitResult;
//import net.minecraft.server.network.ServerPlayerEntity;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//
//public class OnBlockPlaceListener {
//    public static final Map<UUID, Integer> playerBlockPlaceCount = new HashMap<>();
//
//    public static void register() {
//        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
//            if (!(player instanceof ServerPlayerEntity serverPlayer)) return ActionResult.PASS;
//
//            // 簡易判斷（實際放置需配合 block、物品與 world 檢查，可視需求擴充）
//            if (!world.isClient && hand == Hand.MAIN_HAND) {
//                UUID uuid = serverPlayer.getUuid();
//                playerBlockPlaceCount.put(uuid, playerBlockPlaceCount.getOrDefault(uuid, 0) + 1);
//                player.sendMessage(
//                        net.minecraft.text.Text.literal("§6[BlockPlace] §f你已合成 "
//                                + playerBlockPlaceCount.get(uuid) + " 次。"),
//                        false
//                );
//            }
//
//            return ActionResult.PASS;
//        });
//    }
//
//    public static int getCount(UUID playerId) {
//        return playerBlockPlaceCount.getOrDefault(playerId, 0);
//    }
//
//    public static void reset(UUID playerId) {
//        playerBlockPlaceCount.remove(playerId);
//    }
//}
