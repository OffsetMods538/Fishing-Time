package top.offsetmonkey538.fishingtime;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE_NEW;
import static top.offsetmonkey538.fishingtime.FishingTime.LOGGER;

public class FishingTimeClient implements ClientModInitializer {
    public static final Path FISHING_TIME_FILE_PATH = FabricLoader.getInstance().getGameDir().resolve("fishing_time.txt");

    @Override
    public void onInitializeClient() {
        try {
            Files.deleteIfExists(FISHING_TIME_FILE_PATH);
            Files.writeString(
                    FISHING_TIME_FILE_PATH,
                    "seconds / ticks\n",
                    CREATE_NEW
            );
        } catch (IOException e) {
            LOGGER.error("Could not write to file '" + FISHING_TIME_FILE_PATH + "'!", e);
        }
    }

    public static void writeFishingTimeToFile(double fishingTimeSeconds, long fishingTimeTicks) {
        try {
            Files.writeString(
                    FISHING_TIME_FILE_PATH,
                    "\n" + fishingTimeSeconds + " / " + fishingTimeTicks,
                    APPEND
            );
        } catch (IOException e) {
            LOGGER.error("Could not write to file '" + FISHING_TIME_FILE_PATH + "'!", e);
        }
    }

    public static void writeFishingTimeToHud(double fishingTimeSeconds) {
        MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of("Fishing finished in " + fishingTimeSeconds + " seconds."));
    }
}
