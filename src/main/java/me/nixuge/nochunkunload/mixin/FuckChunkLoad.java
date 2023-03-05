package me.nixuge.nochunkunload.mixin;

import net.minecraft.client.multiplayer.ChunkProviderClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ChunkProviderClient.class)
public class FuckChunkLoad {
    /**
     * @author Nixuge
     * @reason Overwrite vanilla unloadChunk function
     */
    @Overwrite
    public void unloadChunk(int p_73234_1_, int p_73234_2_) {}

    /**
     * @author Nixuge
     * @reason Overwrite vanilla unloadQueuedChunks function
     */
    @Overwrite
    public boolean unloadQueuedChunks() {
        return false;
    }
}
