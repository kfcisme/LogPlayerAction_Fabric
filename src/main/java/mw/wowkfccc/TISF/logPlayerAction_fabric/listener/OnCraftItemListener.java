package mw.wowkfccc.TISF.logPlayerAction_fabric.listener;

//import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerSlotEvents;

import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class OnCraftItemListener {

    private static final Map<UUID, Integer> COUNTS = new ConcurrentHashMap<>();

    public static void increment(ServerPlayerEntity player) {
        UUID uuid = player.getUuid();
        COUNTS.merge(uuid, 1, Integer::sum);
        player.sendMessage(
                net.minecraft.text.Text.literal("§6[合成追蹤] §f你已合成 "
                        + COUNTS.get(uuid) + " 次。"),
                false
        );
    }

    public static int getCount(UUID uuid) {
        return COUNTS.getOrDefault(uuid, 0);
    }

    public static void reset(UUID uuid) {
        COUNTS.remove(uuid);
    }
}
