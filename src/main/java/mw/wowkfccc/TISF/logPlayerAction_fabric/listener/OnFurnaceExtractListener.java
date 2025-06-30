package mw.wowkfccc.TISF.logPlayerAction_fabric.listener;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.screen.AbstractFurnaceScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OnFurnaceExtractListener {
    private static final Map<UUID, Integer> furnaceExtractCounts = new HashMap<>();
    // UUID -> 上一次看到的 output stack
    private static final Map<UUID, ItemStack> lastOutput = new HashMap<>();

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            // 每一伺服器 tick（約 1/20 秒）末尾呼叫
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                if (!(player.currentScreenHandler instanceof AbstractFurnaceScreenHandler)) continue;
                AbstractFurnaceScreenHandler furnace =
                        (AbstractFurnaceScreenHandler) player.currentScreenHandler;

                UUID uuid = player.getUuid();
                ItemStack now = furnace.getSlot(2).getStack();  // slot 2 = output

                if (!now.isEmpty()) {
                    ItemStack prev = lastOutput.getOrDefault(uuid, ItemStack.EMPTY);
                    if (!ItemStack.areEqual(now, prev)) {
                        int cnt = furnaceExtractCounts.getOrDefault(uuid, 0) + 1;
                        furnaceExtractCounts.put(uuid, cnt);
                        lastOutput.put(uuid, now.copy());
                        player.sendMessage(
                                Text.literal("§6[爐子] §f你已提取 " + cnt + " 次"),
                                false
                        );
                    }
                } else {
                    lastOutput.remove(uuid);
                }
            }
        });
    }

    public static int getCount(UUID uuid) {
        return furnaceExtractCounts.getOrDefault(uuid, 0);
    }

    public static void reset(UUID uuid) {
        furnaceExtractCounts.remove(uuid);
        lastOutput.remove(uuid);
    }
}
