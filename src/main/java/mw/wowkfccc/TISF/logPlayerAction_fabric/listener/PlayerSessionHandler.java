package mw.wowkfccc.TISF.logPlayerAction_fabric.listener;

import mw.wowkfccc.TISF.logPlayerAction_fabric.LogPlayerAction_fabric;
import mw.wowkfccc.TISF.logPlayerAction_fabric.mySQLInsertData;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerSessionHandler {
    private final LogPlayerAction_fabric plugin;
    private final PlayerActionManager actionTracker;
    private final mySQLInsertData mySQLInsert;
    private final Map<UUID, Integer> tickCounters = new HashMap<>();
    private final int sessionSeconds;

    public PlayerSessionHandler(LogPlayerAction_fabric plugin, PlayerActionManager actionTracker, mySQLInsertData mySQLInsert) {
        this.plugin = plugin;
        this.actionTracker = actionTracker;
        this.mySQLInsert = mySQLInsert;
        this.sessionSeconds =  3600;

        setupListeners();
    }

    public void resetTimer(UUID uuid) {
        tickCounters.put(uuid, 0);
        actionTracker.resetCounters(uuid);
    }
//    public void onPlayerJoin(ServerPlayerEntity uuid) {
//        tickCounters.put(uuid, 0);
//        String table = "player_" + uuid.toString().replace("-", "");
//        mySQLInsert.createPlayerTable(table);
//    }
//
//    public void onPlayerQuit(ServerPlayerEntity uuid) {
//        tickCounters.remove(uuid);
//    }
    private void setupListeners() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayerEntity player = handler.getPlayer();
            UUID uuid = player.getUuid();
            tickCounters.put(uuid, 0);
            String table = "player_" + uuid.toString().replace("-", "");
            mySQLInsert.createPlayerTable(table);
        });

        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            UUID uuid = handler.getPlayer().getUuid();
            tickCounters.remove(uuid);
            actionTracker.getAndResetCounts(uuid);
        });

        ServerTickEvents.START_SERVER_TICK.register(server -> {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                UUID uuid = player.getUuid();
                int ticks = tickCounters.getOrDefault(uuid, 0) + 1;
                if (ticks >= sessionSeconds * 20) {
                    tickCounters.put(uuid, 0);

                    PlayerActionManager.EventCounts counts = actionTracker.getAndResetCounts(uuid);
                    if (plugin.isDatabaseEnable() && counts.hasAnyActivity()) {
                        mySQLInsert.insertEventCounts(uuid, counts);
                    }
                } else {
                    tickCounters.put(uuid, ticks);
                }
            }
        });


    }
}