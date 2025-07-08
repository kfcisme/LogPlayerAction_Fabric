package mw.wowkfccc.TISF.logPlayerAction_fabric.listener.mixin;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mixin(ServerPlayerEntity.class)
public class PlayerLevelChangeMixin {
    @Unique private static final Map<UUID, Integer> prevLevel = new HashMap<>();
    @Unique private static final Map<UUID, Integer> changeCount = new HashMap<>();

    @Inject(method = "<init>*", at = @At("RETURN"))
    private void onCtor(CallbackInfo ci) {
        ServerPlayerEntity self = (ServerPlayerEntity) (Object) this;
        prevLevel.put(self.getUuid(), self.experienceLevel);
    }

    @Inject(method = "addExperienceLevels", at = @At("TAIL"))
    private void afterAddLevels(int levels, CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        UUID id = player.getUuid();
        int oldLv = prevLevel.getOrDefault(id, player.experienceLevel - levels);
        int newLv = player.experienceLevel;

        if (oldLv != newLv) {
            int cnt = changeCount.merge(id, 1, Integer::sum);
            player.sendMessage(
                    Text.literal("[等級改變] 你已改變等級 " + cnt + " 次。"),
                    false
            );
        }
        prevLevel.put(id, newLv);
    }
}
