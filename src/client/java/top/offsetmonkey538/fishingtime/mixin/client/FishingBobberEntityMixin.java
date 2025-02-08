package top.offsetmonkey538.fishingtime.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.offsetmonkey538.fishingtime.FishingTimeClient;

@Mixin(FishingBobberEntity.class)
public abstract class FishingBobberEntityMixin extends Entity {

    @Shadow private boolean caughtFish;

    public FishingBobberEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Unique
    private int fishingTime$fishingTimeTicks;

    @Inject(
            method = "tick",
            at = @At("HEAD")
    )
    private void fishingTime$incrementFishingTime(CallbackInfo ci) {
        if (!caughtFish) fishingTime$fishingTimeTicks++;
    }

    @WrapOperation(
            method = "onTrackedDataSet",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/data/DataTracker;get(Lnet/minecraft/entity/data/TrackedData;)Ljava/lang/Object;"
            ),
            slice = @Slice(
                    from = @At(
                            value = "FIELD",
                            target = "Lnet/minecraft/entity/projectile/FishingBobberEntity;CAUGHT_FISH:Lnet/minecraft/entity/data/TrackedData;"
                    )
            )
    )
    private <T> T fishingTime$detectFishCaught(DataTracker instance, TrackedData<T> data, Operation<T> original) {
        final T value = original.call(instance, data);
        if (!getWorld().isClient) return value;
        if (value instanceof Boolean boolValue && !boolValue) return value;

        FishingTimeClient.fishingFinishedWithTime(fishingTime$fishingTimeTicks);
        fishingTime$fishingTimeTicks = 0;

        return value;
    }
}
