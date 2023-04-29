package me.nixuge.nochunkunload.mixins.client.multiplayer;

import me.nixuge.nochunkunload.McMod;
import me.nixuge.nochunkunload.config.Cache;
import net.minecraft.client.multiplayer.ChunkProviderClient;


import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChunkProviderClient.class)
public class ChunkProviderClientMixin {
    private final Cache cache = McMod.getInstance().getCache();

    @Inject(method="unloadChunk", at = @At("HEAD"), cancellable = true)
    public void unloadChunk(int int1, int int2, CallbackInfo ci) {
        if (!cache.areChunksUnloadable()) {
            ci.cancel();
        }
    }

    @Inject(method="unloadQueuedChunks", at = @At("HEAD"), cancellable = true)
    public void unloadChunk(CallbackInfoReturnable<Boolean> cir) {
        if (!cache.areChunksUnloadable()) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}
