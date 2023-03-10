package me.nixuge.nochunkunload.config;

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
    int[] xPositions;
    int[] zPositions;
    S21PacketChunkData.Extracted[] chunksData;

    public S26PacketMaker(S26PacketMapChunkBulk packet, List<Integer> validIndexes) {
        this.packet = packet;
        this.validIndexes = validIndexes;
        this.newSize = validIndexes.size();
        if (newSize == 0)
            setValuesToEmptyPacket();
        else
            extractFromPacket();
    }

    private void setValuesToEmptyPacket() {
        S21PacketChunkData.Extracted arr = new S21PacketChunkData.Extracted();
        arr.data = S26PacketMaker.emptyMinus;
        this.isOverworld = true;
        this.xPositions = this.zPositions = new int[]{0};
        this.chunksData = new S21PacketChunkData.Extracted[]{arr};
    }

    private void extractFromPacket() {
        PacketBuffer buf = new PacketBuffer(Unpooled.buffer());
        try {
            this.packet.writePacketData(buf);
        } catch (IOException e) {
            throw new RuntimeException("FAILED TO EXTRACT DATA FROM PACKET");
        }
//
//        System.out.println("part 2.");
        this.isOverworld = buf.readBoolean();
        int bufSize = buf.readVarIntFromBuffer();
        this.xPositions = new int[newSize];
        this.zPositions = new int[newSize];
        this.chunksData = new S21PacketChunkData.Extracted[newSize];
//        System.out.println("part 3.");
        int actualI = 0;
        for(int i = 0; i < bufSize; ++i) {
//            if (!this.validIndexes.contains(i)) {
//                buf.readInt(); buf.readInt(); buf.readShort();
//                continue;
//            }
            this.xPositions[actualI] = buf.readInt();
            this.zPositions[actualI] = buf.readInt();
            this.chunksData[actualI] = new S21PacketChunkData.Extracted();
            this.chunksData[actualI].dataSize = buf.readShort() & 65535;
            this.chunksData[actualI].data = new byte[this.func_180737_a(Integer.bitCount(chunksData[actualI].dataSize), isOverworld, true)];
            actualI++;
        }

        byte[] ok = new byte[0];
        actualI = 0;
        for (int k = 0; k < bufSize; ++k) {
//            if (!this.validIndexes.contains(k)) {
//                buf.readBytes(ok);
//                continue;
//            }
            buf.readBytes(this.chunksData[actualI].data);
            actualI++;
        }
    }


    public S26PacketMapChunkBulk genNewPacket() {
        //        System.out.println("part 5.");
        PacketBuffer buf = new PacketBuffer(Unpooled.buffer());
        buf.writeBoolean(this.isOverworld);
        buf.writeVarIntToBuffer(this.chunksData.length);

        int i;
        for(i = 0; i < this.xPositions.length; ++i) {
            buf.writeInt(this.xPositions[i]);
            buf.writeInt(this.zPositions[i]);
            buf.writeShort((short)(this.chunksData[i].dataSize & 65535));
        }
//        System.out.println("part 6.");
        for(i = 0; i < this.xPositions.length; ++i) {
            buf.writeBytes(this.chunksData[i].data);
        }
//        System.out.println("part 7.");
        S26PacketMapChunkBulk newPacket = new S26PacketMapChunkBulk();
        try {
//            System.out.println("part 7.5.");
            newPacket.readPacketData(buf);
        } catch (IOException e) {
            System.out.println("part EXCEPTION.");
            throw new RuntimeException(e);
        }
//        System.out.println("part 8.");
        return newPacket;
    }



    private int func_180737_a(int p_180737_0_, boolean p_180737_1_, boolean p_180737_2_) {
        int lvt_3_1_ = p_180737_0_ * 2 * 16 * 16 * 16;
        int lvt_4_1_ = p_180737_0_ * 16 * 16 * 16 / 2;
        int lvt_5_1_ = p_180737_1_ ? p_180737_0_ * 16 * 16 * 16 / 2 : 0;
        int lvt_6_1_ = p_180737_2_ ? 256 : 0;
        return lvt_3_1_ + lvt_4_1_ + lvt_5_1_ + lvt_6_1_;
    }

}
