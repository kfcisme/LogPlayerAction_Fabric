//package mw.wowkfccc.TISF.logPlayerAction_fabric.listener;
//
//import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
//import net.minecraft.server.network.ServerPlayerEntity;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//
//public class OnBlockBreakListener {
//    public static final Map<UUID, Integer> playerBlockBreakCount = new HashMap<>();
//
//    public static void register() {
//        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
//            if (!(player instanceof ServerPlayerEntity)) return;
//            UUID uuid = player.getUuid();
//            playerBlockBreakCount.put(uuid, playerBlockBreakCount.getOrDefault(uuid, 0) + 1);
//            player.sendMessage(
//                    net.minecraft.text.Text.literal("§6[方塊破壞] §f你已合成 "
//                            + playerBlockBreakCount.get(uuid) + " 次。"),
//                    false
//            );
//        });
//    }
//
//    public static int getCount(UUID uuid) {
//        return playerBlockBreakCount.getOrDefault(uuid, 0);
//    }
//
//    public static void reset(UUID uuid) {
//        playerBlockBreakCount.remove(uuid);
//    }
//}
