package mw.wowkfccc.TISF.logPlayerAction_fabric.listener;

import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OnBlockDamageListener {
    public static final Map<UUID, Integer> playerBlockDamageCount = new HashMap<>();

    public static void register() {
        AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) -> {
            if (player instanceof ServerPlayerEntity) {
                UUID uuid = player.getUuid();
                playerBlockDamageCount.put(uuid, playerBlockDamageCount.getOrDefault(uuid, 0) + 1);
            }
            return ActionResult.PASS;
        });
    }

    public static int getCount(UUID uuid) {
        return playerBlockDamageCount.getOrDefault(uuid, 0);
    }

    public static void reset(UUID uuid) {
        playerBlockDamageCount.remove(uuid);
    }
}
