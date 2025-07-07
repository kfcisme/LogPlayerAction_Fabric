//package mw.wowkfccc.TISF.logPlayerAction_fabric.listener;
//
//import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerSlotEvents;
//import net.minecraft.screen.CraftingScreenHandler;
//import net.minecraft.server.network.ServerPlayerEntity;
//
//public class CraftingListener {
//    /**
//     * 在伺服器啟動後呼叫一次，註冊「點擊任何格子」事件
//     * 只在 handler 是 CraftingScreenHandler 且 slotIndex==0 時才 +1
//     */
//    public static void register() {
//        ScreenHandlerSlotEvents.AFTER_SLOT_CLICK.register((handler, player, slotIndex, button, action, stack) -> {
//            if (handler instanceof CraftingScreenHandler && player instanceof ServerPlayerEntity) {
//                if (slotIndex == 0) {
//                    OnCraftItemListener.increment((ServerPlayerEntity) player);
//                }
//            }
//        });
//    }
//}
