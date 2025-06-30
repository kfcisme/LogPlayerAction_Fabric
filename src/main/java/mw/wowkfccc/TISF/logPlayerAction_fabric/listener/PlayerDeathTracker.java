package mw.wowkfccc.TISF.logPlayerAction_fabric.listener;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDeathTracker {
    private static final Map<UUID, Integer> deathCounts = new HashMap<>();

    public static void register() {
        ServerLivingEntityEvents.AFTER_DEATH.register((entity, damageSource) -> {
            if (entity instanceof ServerPlayerEntity player) {
                UUID id = player.getUuid();
                deathCounts.put(id, deathCounts.getOrDefault(id, 0) + 1);

                // Optional: debug log
                // System.out.println("[Fabric] " + player.getName().getString() + " died. Total: " + deathCounts.get(id));
            }
        });
    }

    public static int sendInsertData(UUID playerId) {
        return deathCounts.getOrDefault(playerId, 0);
    }

    public static void resetCounters(UUID playerId) {
        deathCounts.remove(playerId);
    }
}