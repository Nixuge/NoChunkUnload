package me.nixuge.nochunkunload.config;

import lombok.Getter;
import net.minecraft.network.play.server.S26PacketMapChunkBulk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Cache {
    // Doesn't seem to be a way to change ONLY
    // the getter name w lombok, so manual getter
    public boolean areChunksUnloadable() {
        return this.chunksUnloadable;
    }
    public void setChunksUnloadable(boolean chunksUnloadable) {
        this.chunksUnloadable = chunksUnloadable;
        this.resetSavedChunks();
    }
    private boolean chunksUnloadable = false;


    public void setWorldFrozen(boolean worldFrozen) {
        this.worldFrozen = worldFrozen;
        this.resetSavedChunks();
    }
    @Getter
    private boolean worldFrozen = false;


    public boolean isSavedChunk(int[] newChunk) {
        for (int[] chunk : this.savedChunks) {
            if (Arrays.equals(chunk, newChunk))
                return true;
        }
        return false;
    }
    public boolean isSavedChunk(int chunkX, int chunkZ) {
        return isSavedChunk(new int[] { chunkX, chunkZ });
    }
    public void addSavedChunk(int chunkX, int chunkZ) {
        this.savedChunks.add(new int[] { chunkX, chunkZ });
    }
    public void resetSavedChunks() {
        this.savedChunks = new ArrayList<>();
    }
    public List<int[]> savedChunks = new ArrayList<>();

    public S26PacketMapChunkBulk getPacket(S26PacketMapChunkBulk pack, List<Integer> v) {
        return new S26PacketMaker(pack, v).genNewPacket();
    }
}
