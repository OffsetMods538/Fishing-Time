package top.offsetmonkey538.fishingtime.mixin.client;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FishingBobberEntity.class)
public abstract class FishingBobberEntityMixin extends Entity {
    public FishingBobberEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }
    
    @Unique
    private long fishingTime$fishingStartTime;
    
    @Inject(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/math/Vec3d;multiply(DDD)Lnet/minecraft/util/math/Vec3d;"
            )
    )
    private void storeFishingStartTime(CallbackInfo ci) {
        fishingTime$fishingStartTime = System.nanoTime();
    }
}
