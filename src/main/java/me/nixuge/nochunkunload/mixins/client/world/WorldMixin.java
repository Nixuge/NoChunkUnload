package me.nixuge.nochunkunload.mixins.client.world;

import me.nixuge.nochunkunload.McMod;
import me.nixuge.nochunkunload.config.Cache;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(World.class)
public class WorldMixin {
    private final Cache cache = McMod.getInstance().getCache();

    @Inject(method = "markBlockRangeForRenderUpdate(IIIIII)V", at = @At("HEAD"), cancellable = true)
    public void markBlockRangeForRenderUpdate(int blockX1, int _blockY1, int blockZ1, int _blockX2, int _blockY2, int _blockZ2, CallbackInfo ci) {
        // UNNEEDED !!!
        // x << 4 = x*16 (chunk to block)
        // x >> 4 = x/16 (block to chunk if block % 16 == 0)
        if (cache.isWorldFrozen() &&
            cache.isSavedChunk(blockX1 >> 4, blockZ1 >> 4)) {
            //ci.cancel();
        }
        if (!cache.isSavedChunk(blockX1 >> 4, blockX1 >> 4)) {
            this.cache.addSavedChunk(blockX1 >> 4, blockX1 >> 4);
        }
    }
}
