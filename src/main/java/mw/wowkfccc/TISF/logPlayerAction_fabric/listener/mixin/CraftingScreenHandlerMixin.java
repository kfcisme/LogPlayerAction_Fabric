//package mw.wowkfccc.TISF.logPlayerAction_fabric.listener.mixin;
//
//import mw.wowkfccc.TISF.logPlayerAction_fabric.listener.OnCraftItemListener;
//import net.minecraft.screen.CraftingScreenHandler;
//import net.minecraft.screen.slot.SlotActionType;
//import net.minecraft.server.network.ServerPlayNetworkHandler;
//import net.minecraft.item.ItemStack;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//
//@Mixin(CraftingScreenHandler.class)
//public abstract class CraftingScreenHandlerMixin {
//    /**
//     * 攔截玩家在工作台點擊任何格子的方法 onSlotClick，
//     * signature: (int, SlotActionType, ServerPlayNetworkHandler, int) -> ItemStack
//     */
//    @Inject(
//            method = "onSlotClick(ILnet/minecraft/screen/slot/SlotActionType;Lnet/minecraft/server/network/ServerPlayNetworkHandler;I)Lnet/minecraft/item/ItemStack;",
//            at     = @At("HEAD"),
//            remap  = true
//    )
//    private void onSlotClickHead(int slotIndex,
//                                 SlotActionType actionType,
//                                 ServerPlayNetworkHandler netHandler,
//                                 int button,
//                                 CallbackInfoReturnable<ItemStack> cir) {
//        // slotIndex 0 永遠是「合成結果格」
//        if (slotIndex == 0) {
//            // 從網路 handler 拿到真正的玩家
//            OnCraftItemListener.increment(netHandler.getPlayer());
//        }
//    }
//}
//
