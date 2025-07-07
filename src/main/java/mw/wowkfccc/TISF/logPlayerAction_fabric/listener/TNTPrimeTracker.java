package mw.wowkfccc.TISF.logPlayerAction_fabric.listener;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.TntEntity;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TNTPrimeTracker {
    public static final Map<UUID, Integer> playerTNTPrimeCount = new HashMap<>();

    public static void register() {
        ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            if (!(entity instanceof TntEntity tnt)) return;

            Entity igniter = tnt.getOwner();
            if (igniter instanceof ServerPlayerEntity player) {
                UUID playerId = player.getUuid();
                playerTNTPrimeCount.put(playerId,
                        playerTNTPrimeCount.getOrDefault(playerId, 0) + 1);
                player.sendMessage(
                        net.minecraft.text.Text.literal("§6[playerTNTPrimeCount] §f你點燃 "
                                + playerTNTPrimeCount.get(playerId) + " 次。"),
                        false
                );

            }
        });
    }

    public static int sendInsertData(UUID playerId) {
        return playerTNTPrimeCount.getOrDefault(playerId, 0);
    }

    public static void resetCounters(UUID playerId) {
        playerTNTPrimeCount.remove(playerId);
    }
}