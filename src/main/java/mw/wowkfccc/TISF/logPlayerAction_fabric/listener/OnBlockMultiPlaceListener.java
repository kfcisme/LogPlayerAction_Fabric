//package mw.wowkfccc.TISF.logPlayerAction_fabric.listener;
//
//import net.fabricmc.fabric.api.event.player.UseBlockCallback;
//import net.minecraft.util.ActionResult;
//import net.minecraft.util.Hand;
//import net.minecraft.server.network.ServerPlayerEntity;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//
//public class OnBlockMultiPlaceListener {
//    public static final Map<UUID, Integer> multiPlaceCounts = new HashMap<>();
//
//    public static void register() {
//        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
//            if (!(player instanceof ServerPlayerEntity serverPlayer)) return ActionResult.PASS;
//
//            // 模擬類似多方塊放置邏輯（實際放置仍是逐一呼叫）
//            if (player.getStackInHand(hand).getCount() > 1) {
//                UUID uuid = serverPlayer.getUuid();
//                multiPlaceCounts.put(uuid, multiPlaceCounts.getOrDefault(uuid, 0) + 1);
//                player.sendMessage(
//                        net.minecraft.text.Text.literal("§6[multiPlace] §f你已合成 "
//                                + multiPlaceCounts.get(uuid) + " 次。"),
//                        false
//                );
//            }
//
//            return ActionResult.PASS;
//        });
//    }
//
//    public static int getCount(UUID playerId) {
//        return multiPlaceCounts.getOrDefault(playerId, 0);
//    }
//
//    public static void reset(UUID playerId) {
//        multiPlaceCounts.remove(playerId);
//    }
//}
