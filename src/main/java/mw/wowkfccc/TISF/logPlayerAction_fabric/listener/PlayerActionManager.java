package mw.wowkfccc.TISF.logPlayerAction_fabric.listener;

import mw.wowkfccc.TISF.logPlayerAction_fabric.LogPlayerAction_fabric;
import mw.wowkfccc.TISF.logPlayerAction_fabric.listener.*;
import java.util.UUID;

public class PlayerActionManager {
    private final LogPlayerAction_fabric plugin;
    public int chatCount = 0;
    public int pickupCounts = 0;
    public int blockBreakCounts = 0;
    public int tntPrimeCounts = 0;
    public int multiPlaceCounts = 0;
    public int blockDamageCounts = 0;
    public int blockPlaceCounts = 0;
    public int craftCounts = 0;
    public int dmgByEntityCounts = 0;
    public int deathCounts = 0;
    public int explosionCounts = 0;
    public int furnaceExtractCounts = 0;
    public int invCloseCounts= 0;
    public int invOpenCounts = 0;
    public int bucketEmptyCounts = 0;
    public int bucketFillCounts = 0;
    public int cmdPreCounts = 0;
    public int cmdSendCounts = 0;
    public int playerDeathCounts = 0;
    public int itemDropCounts = 0;
    public int expChangeCounts = 0;
    public int interactCounts = 0;
    public int quitCounts = 0;
    public int levelChangeCounts = 0;
    public int respawnCounts = 0;
    public int teleportCounts = 0;
    public int chunkLoadCounts = 0;
    public int redstoneCounts = 0;
    public int afktime = 0;

    public PlayerActionManager(LogPlayerAction_fabric plugin) {
        this.plugin = plugin;
    }

    /** 回傳所有事件次數並歸零 */
    public EventCounts getAndResetCounts(UUID playerId) {
        EventCounts c = new EventCounts(
                pickupCounts = OnPickupItemListener.getCount(playerId),
                blockBreakCounts = OnBlockBreakListener.getCount(playerId),
                tntPrimeCounts = TNTPrimeTracker.sendInsertData(playerId),
                multiPlaceCounts = OnBlockMultiPlaceListener.getCount(playerId),
                chatCount = onPlayerChat.getCount(playerId),
                blockDamageCounts = OnBlockDamageListener.getCount(playerId),
                blockPlaceCounts = OnBlockPlaceListener.getCount(playerId),
                craftCounts = OnCraftItemListener.getCount(playerId),
                dmgByEntityCounts = OnEntityDamageByPlayerListener.getCount(playerId),
                deathCounts = OnEntityDeathListener.getCount(playerId),
                furnaceExtractCounts = OnFurnaceExtractListener.getCount(playerId),
                invCloseCounts = OnInventoryCloseListener.getCount(playerId),
                invOpenCounts = OnInventoryOpenListener.getCount(playerId),
                bucketEmptyCounts = PlayerBucketEmptyTracker.getInsertData(playerId),
                bucketFillCounts = BucketFillListener.get(playerId),
                cmdPreCounts = PlayerCommandPreprocessTracker.getCount(playerId),
                playerDeathCounts = PlayerDeathTracker.sendInsertData(playerId),
                itemDropCounts = PlayerDropItemTracker.getCount(playerId),
                expChangeCounts = PlayerExpChangeTracker.getCount(playerId),
                interactCounts = PlayerInteractTracker.sendInsertData(playerId),
                levelChangeCounts = PlayerLevelChangeTracker.sendInsertData(playerId),
                quitCounts = PlayerQuitTracker.sendInsertData(playerId),
                respawnCounts = PlayerRespawnTracker.getCount(playerId),
                teleportCounts = PlayerTeleportTracker.getCount(playerId),
                chunkLoadCounts = PlayerChunkLoadListener.getCount(playerId),
                redstoneCounts = RedstonePlaceTracker.sendInsertData(playerId)
        );
        return c;
    }

    /** 歸零所有事件 */
    public static void resetCounters(UUID playerId) {
        BucketFillListener.reset(playerId);
                OnBlockBreakListener.reset(playerId);
                OnBlockDamageListener.reset(playerId);
                OnBlockMultiPlaceListener.reset(playerId);
                OnBlockPlaceListener.reset(playerId);
                OnCraftItemListener.reset(playerId);
                OnEntityDamageByPlayerListener.reset(playerId);
                OnEntityDeathListener.reset(playerId);
                OnFurnaceExtractListener.reset(playerId);
                OnInventoryCloseListener.reset(playerId);
                OnInventoryOpenListener.reset(playerId);
                OnPickupItemListener.reset(playerId);
                onPlayerChat.reset(playerId);
                PlayerBucketEmptyTracker.resetCounters(playerId);
                PlayerCommandPreprocessTracker.reset(playerId);
                PlayerDeathTracker.resetCounters(playerId);
                PlayerDropItemTracker.reset(playerId);
                PlayerExpChangeTracker.reset(playerId);
                PlayerInteractTracker.resetCounters(playerId);
                PlayerLevelChangeTracker.resetCounters(playerId);
                PlayerQuitTracker.resetCounters(playerId);
                PlayerRespawnTracker.resetCounters(playerId);
                PlayerTeleportTracker.reset(playerId);
                RedstonePlaceTracker.resetCounters(playerId);
                TNTPrimeTracker.resetCounters(playerId);
    }

    public static class EventCounts {
        public final int pickup, blockBreak, tntPrime, multiPlace, chat, blockDamage, blockPlace,
                craft, dmgByEntity, death,
//                explosion,
                furnaceExtract, invClose, invOpen,
                bucketEmpty, bucketFill, cmdPre,
//                cmdSend,
                playerDeath, itemDrop,
                expChange, interact, levelChange, quit, respawn, teleport,
                chunkLoadCounts, redstoneCounts;

        public EventCounts(
                int pickup, int blockBreak, int tntPrime, int multiPlace, int chat, int blockDamage, int blockPlace,
                int craft, int dmgByEntity, int death,
//                int explosion,
                int furnaceExtract, int invClose, int invOpen,
                int bucketEmpty, int bucketFill, int cmdPre,
//                int cmdSend,
                int playerDeath, int itemDrop,
                int expChange, int interact, int levelChange, int quit, int respawn, int teleport,
                int chunkLoadCounts, int redstoneCounts
        ) {
            this.pickup = pickup;
            this.blockBreak = blockBreak;
            this.tntPrime = tntPrime;
            this.multiPlace = multiPlace;
            this.chat = chat;
            this.blockDamage = blockDamage;
            this.blockPlace = blockPlace;
            this.craft = craft;
            this.dmgByEntity = dmgByEntity;
            this.death = death;
//            this.explosion = explosion;
            this.furnaceExtract = furnaceExtract;
            this.invClose = invClose;
            this.invOpen = invOpen;
            this.bucketEmpty = bucketEmpty;
            this.bucketFill = bucketFill;
            this.cmdPre = cmdPre;
//            this.cmdSend = cmdSend;
            this.playerDeath = playerDeath;
            this.itemDrop = itemDrop;
            this.expChange = expChange;
            this.interact = interact;
            this.levelChange = levelChange;
            this.quit = quit;
            this.respawn = respawn;
            this.teleport = teleport;
            this.chunkLoadCounts = chunkLoadCounts;
            this.redstoneCounts = redstoneCounts;
        }

        public boolean hasAnyActivity() {
            return pickup + blockBreak + tntPrime + multiPlace + chat + blockDamage + blockPlace +
                    craft + dmgByEntity + death + /**explosion**/ + furnaceExtract + invClose + invOpen +
                    bucketEmpty + bucketFill + cmdPre + /**cmdSend**/ + playerDeath + itemDrop +
                    expChange + interact + levelChange + quit + respawn + teleport +
                    chunkLoadCounts + redstoneCounts  > 0;
        }
    }
}
