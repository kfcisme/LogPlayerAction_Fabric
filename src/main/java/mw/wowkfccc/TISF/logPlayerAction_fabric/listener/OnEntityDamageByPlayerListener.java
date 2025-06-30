package mw.wowkfccc.TISF.logPlayerAction_fabric.listener;

import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.util.ActionResult;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.entity.Entity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OnEntityDamageByPlayerListener {
    public static final Map<UUID, Integer> playerEntityDamageByEntityCount = new HashMap<>();

    public static void register() {
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (!(player instanceof ServerPlayerEntity serverPlayer)) return ActionResult.PASS;

            UUID playerId = serverPlayer.getUuid();
            playerEntityDamageByEntityCount.put(playerId,
                    playerEntityDamageByEntityCount.getOrDefault(playerId, 0) + 1);

            return ActionResult.PASS;
        });
    }

    public static int getCount(UUID uuid) {
        return playerEntityDamageByEntityCount.getOrDefault(uuid, 0);
    }

    public static void reset(UUID uuid) {
        playerEntityDamageByEntityCount.remove(uuid);
    }
}
