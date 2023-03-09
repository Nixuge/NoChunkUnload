package me.nixuge.nochunkunload.mixins.server;

import net.minecraft.network.play.server.S21PacketChunkData;
import net.minecraft.network.play.server.S26PacketMapChunkBulk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(S26PacketMapChunkBulk.class)
public interface S26PacketMapChunkBulkGetters {
    @Accessor("chunksData")
    S21PacketChunkData.Extracted[] getChunksData();

    @Accessor("xPositions")
    int[] getxPositions();

    @Accessor("zPositions")
    int[] getzPositions();

    @Accessor("isOverworld")
    boolean isOverworld();
}
