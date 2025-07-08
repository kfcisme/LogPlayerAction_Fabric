package mw.wowkfccc.TISF.logPlayerAction_fabric;

import mw.wowkfccc.TISF.logPlayerAction_fabric.listener.EntityDeathListener;
import mw.wowkfccc.TISF.logPlayerAction_fabric.listener.TNTPrimeTracker;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

import mw.wowkfccc.TISF.logPlayerAction_fabric.listener.*;

public class LogPlayerAction_fabric implements ModInitializer {

    public static LogPlayerAction_fabric INSTANCE;
    public static MinecraftServer SERVER;

    private boolean databaseEnable = true;
//    private MySQLConnect mySQL;
//    private mySQLInsertData mySQLInsert;
    private PlayerActionManager actionListener;
    private PlayerSessionHandler sessionListener;
    private FileLogger FileLogger;

    @Override
    public void onInitialize() {
        INSTANCE = this;
//        mySQLInsert = new mySQLInsertData(mySQL);
        actionListener = new PlayerActionManager(this);
        sessionListener = new PlayerSessionHandler(this, actionListener, FileLogger);

//        // 玩家登入與登出事件
//        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
//            actionListener.onPlayerJoin(handler.getPlayer());
//            sessionListener.onPlayerJoin(handler.getPlayer());
//        });
//
//        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
//            sessionListener.onPlayerQuit(handler.getPlayer());
//        });

        // 指令註冊（如 /logplayeraction）
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            LogPlayerActionCommand.register(dispatcher, sessionListener); // 你需要實作這個
        });

        // 伺服器生命周期
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            SERVER = server;
            System.out.println("✅ LogPlayerAction 已啟動");
        });

//        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
//            if (databaseEnable && mySQL.isConnected()) {
//                mySQL.disconnect();
//            }
//            System.out.println("🛑 資料庫已關閉");
//        });

        // AFK 與 Essentials 模組暫略（Fabric 無內建）✅
//        onPlayerChat.register();
//        OnBlockBreakListener.register();
        OnBlockDamageListener.register();
        OnBlockMultiPlaceListener.register();
//        OnBlockBreakListener.register();
        BucketFillListener.register();
//        OnBlockPlaceListener.register();
//        OnCraftItemListener.register();
        BlockActionListener.register();
        OnEntityDamageByPlayerListener.register();
//        OnEntityDeathListener.register();
        OnFurnaceExtractListener.register();
        InventoryOpenCloseListener.register();
//        OnPickupItemListener.register();
//        onPlayerChat.register();
        TNTPrimeTracker.register();
        PlayerBucketEmptyTracker.register();
//        PlayerCommandPreprocessTracker.register();
        PlayerDeathTracker.register();
//        PlayerDropItemTracker.register();
//        PlayerExpChangeTracker.register();
        PlayerInteractTracker.register();
        PlayerQuitTracker.register();
        PlayerRespawnTracker.register();
//        PlayerTeleportTracker.register();
        RedstonePlaceTracker.register();
//        TNTPrimeTracker.register();
        BucketFillListener.register();
        EntityDeathListener.register();
        PlayerChunkLoadListener.register();
    }

    public boolean isDatabaseEnable() {
        return databaseEnable;
    }

//    public mySQLInsertData getMySQLInsert() {
//        return mySQLInsert;
//    }

    public PlayerSessionHandler getSessionListener() {
        return sessionListener;
    }

    public static LogPlayerAction_fabric getInstance() {
        return INSTANCE;
    }
}
