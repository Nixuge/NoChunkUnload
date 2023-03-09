package me.nixuge.nochunkunload.mixins.client.network;

import me.nixuge.nochunkunload.mixins.server.S26PacketMapChunkBulkGetters;
import net.minecraft.network.play.server.S21PacketChunkData;
import net.minecraft.network.play.server.S26PacketMapChunkBulk;

public class Owo {
    final S26PacketMapChunkBulk packet;
    public Owo(S26PacketMapChunkBulk p_handleMapChunkBulk_1_) {
        this.packet = p_handleMapChunkBulk_1_;
    }

    public S21PacketChunkData.Extracted[] getOwo() {
        return ((S26PacketMapChunkBulkGetters) this.packet).getChunksData();
    }
}
