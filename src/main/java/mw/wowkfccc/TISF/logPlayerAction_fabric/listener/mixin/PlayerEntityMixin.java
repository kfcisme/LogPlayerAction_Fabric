package mw.wowkfccc.TISF.logPlayerAction_fabric.listener.mixin;

//
//@Mixin(PlayerEntity.class)
//public abstract class PlayerEntityMixin {
//    /**
//     * 攔截 PlayerEntity.dropItem(ItemStack, boolean)
//     * Yarn descriptor: dropItem(Lnet/minecraft/item/ItemStack;Z)Lnet/minecraft/entity/ItemEntity;
//     */
//    @Inject(
//            method = "dropItem(Lnet/minecraft/item/ItemStack;Z)Lnet/minecraft/entity/ItemEntity;",
//            at     = @At("HEAD")
//    )
//    private void onDropItem(
//            ItemStack stack,
//            boolean throwRandomly,
//            CallbackInfoReturnable<ItemEntity> cir
//    ) {
//        // mixin class 本身在 runtime 就是 PlayerEntity 的 instance，
//        // 但 compile time 它是個獨立類，因此先 cast：
//        PlayerEntity self = (PlayerEntity)(Object)this;
//        if (self instanceof ServerPlayerEntity server) {
//            // 累計丟物品次數
//            PlayerDropItemTracker.increment(server.getUuid());
//            int total = PlayerDropItemTracker.getCount(server.getUuid());
//            // （可選）回饋玩家
//            server.sendMessage(
//                    net.minecraft.text.Text.literal(
//                            "§6[丟物品追蹤] §f你已丟出 " + total + " 件物品"
//                    ),
//                    false
//            );
//        }
//    }
//}


//@Mixin(ServerPlayerEntity.class)
//public class PlayerEntityMixin {
//    /**
//     * 按 Q 丟棄手上選中物品（整堆）
//     * Yarn: dropSelectedItem(Z)Lnet/minecraft/entity/ItemEntity;
//     */
//    @Inject(method = "dropSelectedItem", at = @At("HEAD"))
//    private void onDropSelectedItem(boolean wholeStack, CallbackInfoReturnable<ItemEntity> cir) {
//        PlayerDropItemTracker.increment(((ServerPlayerEntity)(Object)this).getUuid());
//    }
//
//    /**
//     * 透過滑鼠右鍵拖出來丟棄或是 /give 等指令生成的掉落
//     * Yarn: dropItem(Lnet/minecraft/item/ItemStack;Z)Lnet/minecraft/entity/ItemEntity;
//     */
//    @Inject(method = "dropItem", at = @At("HEAD"))
//    private void onDropItem(ItemStack stack, boolean throwRandomly, boolean retainOwnership, CallbackInfoReturnable<ItemEntity> cir) {
//        PlayerDropItemTracker.increment(((ServerPlayerEntity)(Object)this).getUuid());
//    }
//}

