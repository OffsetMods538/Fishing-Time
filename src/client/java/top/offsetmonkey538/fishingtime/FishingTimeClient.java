package top.offsetmonkey538.fishingtime;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE_NEW;
import static top.offsetmonkey538.fishingtime.FishingTime.LOGGER;

public class FishingTimeClient implements ClientModInitializer {
    public static final Path FISHING_TIME_FILE_PATH = FabricLoader.getInstance().getGameDir().resolve("fishing_time.txt");

    private static int fishingTimeTicks = 0;
    private static float fishingTimeSeconds = 0;
    private static float averageFishingTimeSeconds = 0;
    private static float averageFishingTimeSumSeconds = 0;
    private static int averageFishingTimeCount = 0;

    @Override
    public void onInitializeClient() {
        try {
            Files.deleteIfExists(FISHING_TIME_FILE_PATH);
            Files.writeString(
                    FISHING_TIME_FILE_PATH,
                    "seconds / ticks   |   average time sec / average count\n",
                    CREATE_NEW
            );
        } catch (IOException e) {
            LOGGER.error("Could not write to file '" + FISHING_TIME_FILE_PATH + "'!", e);
        }
    }

    public static void fishingFinishedWithTime(int newFishingTimeTicks) {
        fishingTimeTicks = newFishingTimeTicks;
        averageFishingTimeCount++;

        fishingTimeSeconds = fishingTimeTicks / 20f;

        averageFishingTimeSumSeconds += fishingTimeSeconds;
        averageFishingTimeSeconds = averageFishingTimeSumSeconds / averageFishingTimeCount;

        FishingTimeClient.writeFishingTimeToFile();
        FishingTimeClient.writeFishingTimeToHud();
    }

    public static void writeFishingTimeToFile() {
        try {
            Files.writeString(
                    FISHING_TIME_FILE_PATH,
                    "\n" +
                            StringUtils.center(String.valueOf(fishingTimeSeconds), "seconds".length()) +
                            " / " +
                            StringUtils.center(String.valueOf(fishingTimeTicks), "ticks".length()) +
                            "   |   " +
                            StringUtils.center(String.valueOf(averageFishingTimeSeconds), "average time sec".length()) +
                            " / " +
                            StringUtils.center(String.valueOf(averageFishingTimeCount), "average count".length()),
                    APPEND
            );
        } catch (IOException e) {
            LOGGER.error("Could not write to file '" + FISHING_TIME_FILE_PATH + "'!", e);
        }
    }

    public static void writeFishingTimeToHud() {
        MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of("Fishing finished in " + fishingTimeSeconds + " seconds. Average per " + averageFishingTimeCount + " throws is " + averageFishingTimeSeconds + " seconds."));
    }
}
