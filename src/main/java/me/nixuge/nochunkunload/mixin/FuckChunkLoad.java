package me.nixuge.nochunkunload.mixin;

import net.minecraft.client.multiplayer.ChunkProviderClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ChunkProviderClient.class)
public class FuckChunkLoad {
    //@Inject(method = "unloadChunk", at = @At("HEAD"))
    //private void init(CallbackInfo ci) {
    //    System.out.println("FUCK MC!!");
    //}

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void unloadChunk(int p_73234_1_, int p_73234_2_) {
        System.out.println("LMAO NO UNLOAD ");
    }
    /**
     * @author
     * @reason
     */
    @Overwrite
    public boolean unloadQueuedChunks() {
        System.out.println("QUEUED NO UNLOAD ");
        return false;
    }
}
