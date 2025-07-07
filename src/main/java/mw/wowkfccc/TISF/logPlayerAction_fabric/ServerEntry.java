package mw.wowkfccc.TISF.logPlayerAction_fabric;

import mw.wowkfccc.TISF.logPlayerAction_fabric.listener.PlayerActionManager;
import net.fabricmc.api.DedicatedServerModInitializer;
import mw.wowkfccc.TISF.logPlayerAction_fabric.listener.PlayerSessionHandler;

public class ServerEntry implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        // 在這裡建立並註冊 PlayerSessionHandler
        var actionTracker = new PlayerActionManager(LogPlayerAction_fabric.INSTANCE);
        var fileLogger    = new FileLogger(LogPlayerAction_fabric.INSTANCE, actionTracker);
        new PlayerSessionHandler(LogPlayerAction_fabric.INSTANCE, actionTracker, fileLogger);
    }
}
