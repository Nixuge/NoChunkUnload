package me.nixuge.nochunkunload.mixins.client.world;

import me.nixuge.nochunkunload.McMod;
import me.nixuge.nochunkunload.config.Cache;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Chunk.class)
public class ChunkMixin {
    private final Cache cache = McMod.getInstance().getCache();

    @Shadow @Final
    public byte[] blockBiomeArray;

    @Shadow @Final
    public int xPosition;
    @Shadow @Final
    public int zPosition;

//    @Inject(method = "fillChunk", at = @At("HEAD"), cancellable = true)
//    public void fillChunk(byte[] p_fillChunk_1_, int p_fillChunk_2_, boolean p_fillChunk_3_, CallbackInfo ci) {
////        if (cache.isSavedChunk2(xPosition, zPosition)) {
////            if (cache.isWorldFrozen()) {
////                System.out.println("Returned for chunk " + xPosition + " " + zPosition);
////                ci.cancel();
////            }
////            return;
////        }
//        //cache.addSavedChunk2(xPosition, zPosition);
//    }

    @ModifyArg(method = "fillChunk", at = @At("HEAD"), index = 0)
    public byte[] fillChunk(byte[] p_fillChunk_1_) {
        return this.blockBiomeArray;
//        if (cache.isSavedChunk2(xPosition, zPosition)) {
//            if (cache.isWorldFrozen()) {
//                System.out.println("Returned for chunk " + xPosition + " " + zPosition);
//                ci.cancel();
//            }
//            return;
//        }
        //cache.addSavedChunk2(xPosition, zPosition);
    }
}
