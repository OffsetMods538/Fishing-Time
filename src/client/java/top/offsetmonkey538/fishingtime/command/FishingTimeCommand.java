package top.offsetmonkey538.fishingtime.command;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.command.CommandRegistryAccess;
import top.offsetmonkey538.fishingtime.FishingTimeClient;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;
import static top.offsetmonkey538.fishingtime.FishingTime.MOD_ID;

public final class FishingTimeCommand {
    private FishingTimeCommand() {}

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        dispatcher.register(
                literal(MOD_ID).then(literal("reset").executes(context -> {
                    FishingTimeClient.resetAverage();
                    return 1;
                })));
    }
}
