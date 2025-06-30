//package mw.wowkfccc.TISF.logPlayerAction_fabric.listener;
//
//import net.minecraft.entity.Entity;
//import net.minecraft.entity.TntEntity;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.server.network.ServerPlayerEntity;
//import net.minecraft.server.world.ServerWorld;
//import net.minecraft.world.World;
//import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
//
//import java.util.*;
//
//public class OnTNTPrimeListener {
//    private static final Map<Integer, UUID> tntSourceMap = new HashMap<>();
//    private static final Map<UUID, Integer> tntPrimeCounts = new HashMap<>();
//
//    public static void register() {
//        ServerTickEvents.END_WORLD_TICK.register(world -> {
//            if (!(world instanceof ServerWorld serverWorld)) return;
//
//            for (Entity entity : serverWorld.iterateEntities()) {
//                if (entity instanceof TntEntity tnt) {
//                    int id = tnt.getId();
//                    if (!tntSourceMap.containsKey(id) && tnt.getCausingEntity() instanceof ServerPlayerEntity player) {
//                        UUID uuid = player.getUuid();
//                        tntSourceMap.put(id, uuid);  // 紀錄來源
//                        tntPrimeCounts.put(uuid, tntPrimeCounts.getOrDefault(uuid, 0) + 1);
//                        // System.out.println("✅ TNT primed by " + player.getName().getString());
//                    }
//                }
//            }
//        });
//    }
//
//    public static int getCount(UUID uuid) {
//        return tntPrimeCounts.getOrDefault(uuid, 0);
//    }
//
//    public static void reset(UUID uuid) {
//        tntPrimeCounts.remove(uuid);
//    }
//}
