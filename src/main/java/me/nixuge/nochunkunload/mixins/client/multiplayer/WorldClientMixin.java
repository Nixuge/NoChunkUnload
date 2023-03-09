package me.nixuge.nochunkunload.mixins.client.multiplayer;

import me.nixuge.nochunkunload.McMod;
import me.nixuge.nochunkunload.config.Cache;
import net.minecraft.client.multiplayer.WorldClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldClient.class)
public class WorldClientMixin {
    private final Cache cache = McMod.getInstance().getCache();

    @Inject(method = "invalidateBlockReceiveRegion", at = @At("HEAD"), cancellable = true)
    public void invalidateBlockReceiveRegion(int blockX1, int _blockY1, int blockZ1, int _blockX2, int _blockY2, int _blockZ2, CallbackInfo ci) {
        // x << 4 = x*16 (chunk to block)
        // x >> 4 = x/16 (block to chunk if block % 16 == 0)
        if (cache.isWorldFrozen() &&
            cache.isSavedChunk(blockX1 >> 4, blockZ1 >> 4)) {
            ci.cancel();
        }
    }

    // doPreChunk doesn't matter
    // as unloadChunks is already handled elsewhere
    // and loadChunk is needed anyways

}
