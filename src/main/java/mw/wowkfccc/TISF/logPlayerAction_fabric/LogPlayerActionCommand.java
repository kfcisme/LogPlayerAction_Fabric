package mw.wowkfccc.TISF.logPlayerAction_fabric;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.server.network.ServerPlayerEntity;

import mw.wowkfccc.TISF.logPlayerAction_fabric.LogPlayerAction_fabric;
import mw.wowkfccc.TISF.logPlayerAction_fabric.listener.PlayerSessionHandler;

import java.util.function.Supplier;

import static net.minecraft.server.command.CommandManager.*;

public class LogPlayerActionCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, PlayerSessionHandler sessionListener) {
        dispatcher.register(
                literal("logplayeraction")
                        .executes(ctx -> {
                            ctx.getSource().sendFeedback(
                                    (Supplier<Text>) Text.literal("LogPlayerAction Fabric 版 by Wowkfccc"), false);
                            return 1;
                        })

                        .then(literal("reload")
                                .requires(src -> src.hasPermissionLevel(4)) // OP level 4
                                .executes(ctx -> {
                                    // TODO: config 重載邏輯（視 config 類別實作）
                                    ctx.getSource().sendFeedback((Supplier<Text>) Text.literal("Config reloaded (stub)."), false);
                                    return 1;
                                })
                        )

                        .then(literal("time_reset")
                                .requires(src -> src.hasPermissionLevel(2))
                                .executes(ctx -> {
                                    // 重置自己
                                    ServerPlayerEntity player = ctx.getSource().getPlayer();
                                    sessionListener.resetTimer(player.getUuid());
                                    ctx.getSource().sendFeedback((Supplier<Text>) Text.literal("✅ 已重置自己計時器！"), false);
                                    return 1;
                                })

                                .then(argument("target", net.minecraft.command.argument.EntityArgumentType.player())
                                        .executes(ctx -> {
                                            ServerPlayerEntity target = net.minecraft.command.argument.EntityArgumentType.getPlayer(ctx, "target");
                                            sessionListener.resetTimer(target.getUuid());
                                            ctx.getSource().sendFeedback((Supplier<Text>) Text.literal("✅ 已重置 " + target.getName().getString() + " 的計時器！"), false);
                                            return 1;
                                        })
                                )

                                .then(literal("all")
                                        .executes(ctx -> {
                                            for (ServerPlayerEntity p : LogPlayerAction_fabric.SERVER.getPlayerManager().getPlayerList()) {
                                                sessionListener.resetTimer(p.getUuid());
                                            }
                                            ctx.getSource().sendFeedback((Supplier<Text>) Text.literal("✅ 已重置所有在線玩家計時器！"), false);
                                            return 1;
                                        })
                                )
                        )
        );
    }
}
