package me.nixuge.nochunkunload.packetutils;

import net.minecraft.network.play.server.S26PacketMapChunkBulk;

import java.util.List;

public class PacketUtils {
    public S26PacketMapChunkBulk getPacket(S26PacketMapChunkBulk pack, List<Integer> v) {
        return new S26PacketMaker(pack, v).genNewPacket();
    }
}
