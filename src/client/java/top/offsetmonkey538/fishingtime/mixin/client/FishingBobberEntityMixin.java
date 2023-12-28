package top.offsetmonkey538.fishingtime.mixin.client;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.nio.file.Files;

import static java.nio.file.StandardOpenOption.APPEND;
import static top.offsetmonkey538.fishingtime.FishingTime.LOGGER;
import static top.offsetmonkey538.fishingtime.FishingTimeClient.FISHING_TIME_FILE_PATH;

@Mixin(FishingBobberEntity.class)
public abstract class FishingBobberEntityMixin extends Entity {
    public FishingBobberEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow
    private boolean caughtFish;

    @Unique
    private boolean fishingTime$isFishing;
    @Unique
    private long fishingTime$fishingTimeTicks;

    @Inject(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/math/Vec3d;multiply(DDD)Lnet/minecraft/util/math/Vec3d;"
            )
    )
    private void storeFishingStartTime(CallbackInfo ci) {
        if (!getWorld().isClient) return;
        fishingTime$isFishing = true;
        fishingTime$fishingTimeTicks = 0;
    }

    @Inject(
            method = "tick",
            at = @At("HEAD")
    )
    private void incrementFishingTime(CallbackInfo ci) {
        if (fishingTime$isFishing) fishingTime$fishingTimeTicks++;
    }

    @Inject(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/lang/Math;max(II)I"
            )
    )
    private void finishMeasuringFishingTime(CallbackInfo ci) {
        if (!caughtFish || !getWorld().isClient || !fishingTime$isFishing) return;

        final double fishingTimeSeconds = (double) fishingTime$fishingTimeTicks / 20;

        try {
            Files.writeString(
                    FISHING_TIME_FILE_PATH,
                    "\n" + fishingTimeSeconds + " / " + fishingTime$fishingTimeTicks,
                    APPEND
            );
        } catch (IOException e) {
            LOGGER.error("Could not write to file '" + FISHING_TIME_FILE_PATH + "'!", e);
        }

        fishingTime$fishingTimeTicks = 0;
        fishingTime$isFishing = false;
    }
}
