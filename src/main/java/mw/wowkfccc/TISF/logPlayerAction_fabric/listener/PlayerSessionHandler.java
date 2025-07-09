
package mw.wowkfccc.TISF.logPlayerAction_fabric.listener;

import mw.wowkfccc.TISF.logPlayerAction_fabric.FileLogger;
import mw.wowkfccc.TISF.logPlayerAction_fabric.LogPlayerAction_fabric;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;

import java.util.UUID;

public class PlayerSessionHandler {
    private final LogPlayerAction_fabric plugin;
    private final PlayerActionManager actionTracker;
    private final FileLogger fileLogger;

    public PlayerSessionHandler(LogPlayerAction_fabric plugin, PlayerActionManager actionTracker, FileLogger fileLogger) {
        this.plugin        = plugin;
        this.actionTracker = actionTracker;
        this.fileLogger    = new FileLogger(plugin,actionTracker);


        ServerPlayConnectionEvents.JOIN.register(
                (ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server) -> {
                    var player = handler.player;
                    UUID uuid = player.getUuid();
                    actionTracker.initializePlayer(uuid);
                    fileLogger.createLog(uuid);
                    fileLogger.schedulePlayerFlush(uuid, 5, 30);
                }
        );

        ServerPlayConnectionEvents.DISCONNECT.register(
                (ServerPlayNetworkHandler handler, MinecraftServer server) -> {
                    UUID uuid = handler.player.getUuid();
                    fileLogger.cancelPlayerFlush(uuid);
                    actionTracker.removePlayer(uuid);
                }
        );

    }
}

