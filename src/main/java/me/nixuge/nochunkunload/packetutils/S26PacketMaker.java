package me.nixuge.nochunkunload.packetutils;

import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.S21PacketChunkData;
import net.minecraft.network.play.server.S26PacketMapChunkBulk;

import java.io.IOException;
import java.util.List;

public class S26PacketMaker {
    private static final byte[] emptyMinus = new byte[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };

    private final S26PacketMapChunkBulk packet;

    private final List<Integer> validIndexes;
    int newSize;

    boolean isOverworld;
    int[] oldxPositions;
    int[] oldzPositions;
    S21PacketChunkData.Extracted[] oldchunksData;

    int[] xPositions;
    int[] zPositions;
    S21PacketChunkData.Extracted[] chunksData;

    public S26PacketMaker(S26PacketMapChunkBulk packet, List<Integer> validIndexes) {
        this.packet = packet;
        this.validIndexes = validIndexes;
        this.newSize = validIndexes.size();
        if (newSize == 0)
            setValuesToEmptyPacket();
        else {
            extractFromPacket();
            removeUnneccessaryChunks();
        }
    }

    /**
     * Sets isOverworld, xPositions, zPositions & chunksData
     * for an empty (0 chunks) packet
     */
    private void setValuesToEmptyPacket() {
        S21PacketChunkData.Extracted arr = new S21PacketChunkData.Extracted();
        arr.data = S26PacketMaker.emptyMinus;
        this.isOverworld = true;
        this.xPositions = this.zPositions = new int[]{0};
        this.chunksData = new S21PacketChunkData.Extracted[]{arr};
    }

    /**
     * Sets isOverworld, oldxPositions, oldzPositions & oldchunksData
     * from the packet given in the constructor.
     * Needs to be used before removeUnneccessaryChunks()
     */
    private void extractFromPacket() {
        PacketBuffer buf = new PacketBuffer(Unpooled.buffer());
        try {
            this.packet.writePacketData(buf);
        } catch (IOException e) {
            throw new RuntimeException("FAILED TO EXTRACT DATA FROM PACKET");
        }
//
        this.isOverworld = buf.readBoolean();
        int bufSize = buf.readVarIntFromBuffer();
        this.oldxPositions = new int[bufSize];
        this.oldzPositions = new int[bufSize];
        this.oldchunksData = new S21PacketChunkData.Extracted[bufSize];

        for(int i = 0; i < bufSize; i++) {
            this.oldxPositions[i] = buf.readInt();
            this.oldzPositions[i] = buf.readInt();
            this.oldchunksData[i] = new S21PacketChunkData.Extracted();
            this.oldchunksData[i].dataSize = buf.readShort() & 65535;
            this.oldchunksData[i].data = new byte[this.func_180737_a(Integer.bitCount(this.oldchunksData[i].dataSize), isOverworld)];
        }

        for (int k = 0; k < bufSize; k++) {
            buf.readBytes(this.oldchunksData[k].data);
        }
    }

    /**
     * Sets xPositions, zPositions , chunksData & newSize
     * from the "old"s arrays present after running extractFromPacket()
     * without any processing (bypasses removeUnneccessaryChunks())
     */
    @SuppressWarnings("unused")
    private void shortCircuitOldNew() {
        this.newSize = this.oldxPositions.length;
        this.xPositions = this.oldxPositions;
        this.zPositions = this.oldzPositions;
        this.chunksData = this.oldchunksData;
    }

    /**
     * Sets xPositions, zPositions & chunksData
     * from the "old"s arrays present after running extractFromPacket()
     * by removing the unneeded values (already saved chunks) from the old arrays
     */
    private void removeUnneccessaryChunks() {
        this.xPositions = new int[this.newSize];
        this.zPositions = new int[this.newSize];
        this.chunksData = new S21PacketChunkData.Extracted[this.newSize];

        int actualI = 0;
        for(int i = 0; i < this.oldxPositions.length; i++) {
            if (!this.validIndexes.contains(i)) {
                continue;
            }
            this.xPositions[actualI] = this.oldxPositions[i];
            this.zPositions[actualI] = this.oldzPositions[i];
            this.chunksData[actualI] = this.oldchunksData[i];
            actualI++;
        }
    }

    /**
     * Generates the packet using the "old"s arrays present in the class
     * Use either after removeUnneccessaryChunks() or after setValuesToEmptyPacket()
     */
    public S26PacketMapChunkBulk genNewPacket() {
        PacketBuffer buf = new PacketBuffer(Unpooled.buffer());
        buf.writeBoolean(this.isOverworld);
        buf.writeVarIntToBuffer(this.chunksData.length);

        for(int i = 0; i < this.xPositions.length; ++i) {
            buf.writeInt(this.xPositions[i]);
            buf.writeInt(this.zPositions[i]);
            buf.writeShort((short)(this.chunksData[i].dataSize & 65535));
        }

        for(int i = 0; i < this.xPositions.length; ++i) {
            buf.writeBytes(this.chunksData[i].data);
        }

        S26PacketMapChunkBulk newPacket = new S26PacketMapChunkBulk();
        try {
            newPacket.readPacketData(buf);
        } catch (IOException e) {
            System.out.println("part EXCEPTION.");
            throw new RuntimeException(e);
        }

        return newPacket;
    }


    /**
     * Vanilla function from the S21PacketChunkData class
     * Needed to extract data from the packet
     * (shouldn't reuse mc code, but this is adapted &
     * is so short anyways it really doesn't matter)
     */
    private int func_180737_a(int p_180737_0_, boolean p_180737_1_) {
        int lvt_3_1_ = p_180737_0_ * 2 * 16 * 16 * 16;
        int lvt_4_1_ = p_180737_0_ * 16 * 16 * 16 / 2;
        int lvt_5_1_ = p_180737_1_ ? p_180737_0_ * 16 * 16 * 16 / 2 : 0;
        return lvt_3_1_ + lvt_4_1_ + lvt_5_1_ + 256;
    }

}
