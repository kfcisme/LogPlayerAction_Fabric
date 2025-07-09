package mw.wowkfccc.TISF.logPlayerAction_fabric;


import mw.wowkfccc.TISF.logPlayerAction_fabric.listener.PlayerActionManager;
import mw.wowkfccc.TISF.logPlayerAction_fabric.listener.PlayerSessionHandler;
import net.fabricmc.api.DedicatedServerModInitializer;

public class ServerEntry implements DedicatedServerModInitializer {
    // 單例，若有其他 class 需要也可用 ServerEntry.INSTANCE 拿到
    public static final ServerEntry INSTANCE = new ServerEntry();

    // 事件統計器與檔案寫入器
    private final PlayerActionManager actionTracker = new PlayerActionManager(LogPlayerAction_fabric.INSTANCE);
    private final FileLogger fileLogger = new FileLogger(LogPlayerAction_fabric.INSTANCE,actionTracker);

    @Override
    public void onInitializeServer() {
        // 只要 new 了 PlayerSessionHandler，就會自動註冊：
        // - 玩家登入時呼叫 fileLogger.createLog(uuid)
        // - 玩家登出時不寫檔（你之前的需求）
        new PlayerSessionHandler(LogPlayerAction_fabric.INSTANCE, actionTracker, fileLogger);
    }
}