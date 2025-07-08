package mw.wowkfccc.TISF.logPlayerAction_fabric.listener;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.TntEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TNTPrimeTracker {
    private static final Map<UUID, Integer> playerTNTPrimeCount = new HashMap<>();

    /**
     * 在主 ModInitializer 的 onInitialize() 中呼叫以註冊事件。
     */
    public static void register() {
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            // 僅在伺服器端
            if (world.isClient() || !(player instanceof ServerPlayerEntity serverPlayer)) {
                return ActionResult.PASS;
            }
            // 手持打火石或火焰彈
            if (player.getStackInHand(hand).getItem() == Items.FLINT_AND_STEEL
                    || player.getStackInHand(hand).getItem() == Items.FIRE_CHARGE) {
                BlockPos pos = ((BlockHitResult) hitResult).getBlockPos();
                // 點擊的是 TNT 方塊
                if (world.getBlockState(pos).isOf(Blocks.TNT)) {
                    UUID id = serverPlayer.getUuid();
                    int count = playerTNTPrimeCount.merge(id, 1, Integer::sum);
                    serverPlayer.sendMessage(
                            Text.literal("[TNT 點燃] 你已點燃 TNT " + count + " 次。"),
                            false
                    );
                }
            }
            return ActionResult.PASS;
        });
    }

    /**
     * 累加並發送提示。
     */
    public static void recordPrime(ServerPlayerEntity player) {
        UUID id = player.getUuid();
        int count = playerTNTPrimeCount.merge(id, 1, Integer::sum);
        player.sendMessage(
                Text.literal("[TNT 點燃] 你已點燃 TNT " + count + " 次。"),
                false
        );
    }


    public static int sendInsertData(UUID playerId) {
        return playerTNTPrimeCount.getOrDefault(playerId, 0);
    }

    public static void resetCounters(UUID playerId) {
        playerTNTPrimeCount.remove(playerId);
    }
}