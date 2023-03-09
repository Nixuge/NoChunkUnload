package me.nixuge.nochunkunload.mixins.server;

import net.minecraft.network.play.server.S21PacketChunkData;
import net.minecraft.network.play.server.S26PacketMapChunkBulk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(S26PacketMapChunkBulk.class)
public interface S26PacketMapChunkBulkSetters {
    @Accessor("chunksData")
    public void setChunks(S21PacketChunkData.Extracted[] chunksData);

    @Accessor("xPositions")
    public void setxPositions(int[] xPositions);

    @Accessor("zPositions")
    public void setzPositions(int[] zPositions);

    @Accessor("isOverworld")
    public void setIsOverworld(boolean isOverworld);
}
