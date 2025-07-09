package mw.wowkfccc.TISF.logPlayerAction_fabric.listener;

import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.item.BlockItem;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

import java.util.UUID;

/**
 * 同時監聽「放置方塊」和「破壞方塊」，並即時回饋玩家訊息。
 */
public class BlockActionListener {
    public static void register() {
        // 放置方塊：右鍵手持 BlockItem
        UseBlockCallback.EVENT.register((player, world, hand, hit) -> {
            if (!world.isClient && player instanceof ServerPlayerEntity) {
                if (player.getStackInHand(hand).getItem() instanceof BlockItem) {
                    UUID uuid = player.getUuid();
                    int count = BlockPlaceTracker.increment(uuid);
//                    player.sendMessage(Text.literal("§e[方塊放置] §f第 " + count + " 次"), false);
                }
            }
            return ActionResult.PASS;
        });

        // 破壞方塊：左鍵打到方塊
        AttackBlockCallback.EVENT.register((player, world, hand, pos, dir) -> {
            if (!world.isClient && player instanceof ServerPlayerEntity) {
                UUID uuid = player.getUuid();
                int count = BlockBreakTracker.increment(uuid);
//                player.sendMessage(Text.literal("§c[方塊破壞] §f第 " + count + " 次"), false);
            }
            return ActionResult.PASS;
        });
    }
}
