package mw.wowkfccc.TISF.logPlayerAction_fabric.listener.mixin;

import mw.wowkfccc.TISF.logPlayerAction_fabric.listener.CraftingListener;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

import net.minecraft.screen.slot.CraftingResultSlot;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(CraftingResultSlot.class)
public abstract class CraftingScreenHandlerMixin extends Slot {
    /**
     * 必須與 Slot 類別構造器簽名匹配
     */
    protected CraftingScreenHandlerMixin(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    /**
     * 在玩家從結果槽取出合成物品時累計次數
     */
    @Inject(method = "onTakeItem", at = @At("HEAD"))
    private void onTakeItemInject(PlayerEntity player, ItemStack stack, CallbackInfo ci) {
        CraftingListener.increment(player.getUuid());
//        player.sendMessage(
//                Text.literal("§6[合成追蹤]"),
//                false
//        );
    }
}

