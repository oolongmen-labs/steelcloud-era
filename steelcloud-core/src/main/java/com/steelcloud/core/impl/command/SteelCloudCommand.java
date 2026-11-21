package com.steelcloud.core.impl.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public final class SteelCloudCommand {
    private SteelCloudCommand() {}

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                dispatcher.register(build()));
    }

    private static LiteralArgumentBuilder<ServerCommandSource> build() {
        return CommandManager.literal("steelcloud")
                .then(CommandManager.literal("debug").requires(SteelCloudCommand::isOp)
                        .then(CommandManager.literal("economy")
                                .then(CommandManager.argument("player", StringArgumentType.word())
                                        .executes(ctx -> notImplemented(ctx.getSource(), "debug economy"))))
                        .then(CommandManager.literal("train")
                                .then(CommandManager.literal("list")
                                        .executes(ctx -> notImplemented(ctx.getSource(), "debug train list"))))
                        .then(CommandManager.literal("health")
                                .then(CommandManager.argument("player", StringArgumentType.word())
                                        .executes(ctx -> notImplemented(ctx.getSource(), "debug health"))))
                        .then(CommandManager.literal("currency")
                                .then(CommandManager.argument("player", StringArgumentType.word())
                                        .then(CommandManager.argument("currencyId", StringArgumentType.word())
                                                .executes(ctx -> notImplemented(ctx.getSource(), "debug currency"))))))
                .then(CommandManager.literal("reload").requires(SteelCloudCommand::isOp)
                        .then(CommandManager.literal("tables")
                                .executes(ctx -> notImplemented(ctx.getSource(), "reload tables"))))
                .then(CommandManager.literal("compat").requires(SteelCloudCommand::isOp)
                        .executes(ctx -> notImplemented(ctx.getSource(), "compat")));
    }

    private static boolean isOp(ServerCommandSource source) {
        return source.hasPermissionLevel(2);
    }

    private static int notImplemented(ServerCommandSource source, String what) {
        source.sendError(Text.literal("[core] " + what + " 尚未实现"));
        return 0;
    }
}