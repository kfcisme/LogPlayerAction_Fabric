package mw.wowkfccc.TISF.logPlayerAction_fabric;

import mw.wowkfccc.TISF.logPlayerAction_fabric.listener.EntityDeathListener;
import mw.wowkfccc.TISF.logPlayerAction_fabric.listener.TNTPrimeTracker;
import mw.wowkfccc.TISF.logPlayerAction_fabric.listener.AFKActivityListener;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

import mw.wowkfccc.TISF.logPlayerAction_fabric.listener.*;

public class LogPlayerAction_fabric implements ModInitializer {

    public static LogPlayerAction_fabric INSTANCE;
    public static MinecraftServer SERVER;

    private boolean databaseEnable = true;

    private PlayerActionManager actionListener;
    private PlayerSessionHandler sessionListener;
    private FileLogger FileLogger;

    @Override
    public void onInitialize() {
        INSTANCE = this;
        actionListener = new PlayerActionManager(this);
        FileLogger     = new FileLogger( this,actionListener);
        sessionListener = new PlayerSessionHandler(this, actionListener, FileLogger);

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            LogPlayerActionCommand.register(dispatcher, sessionListener); // 你需要實作這個
        });

        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            SERVER = server;
            System.out.println("✅ LogPlayerAction 已啟動");
        });

        // 註冊 /commandcount
//        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
//            dispatcher.register(
//                    CommandManager.literal("commandcount")
//                            .executes(ctx -> {
//                                var player = ctx.getSource().getPlayer();
//                                int count = CommandCountManager.getCount(player.getUuid());
//                                player.sendMessage(Text.literal("您已執行指令 " + count + " 次。"), false);
//                                return 1;
//                            })
//            );
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
        AFKActivityListener.register();
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
