package me.nixuge.nochunkunload.config;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Cache {
    // Doesn't seem to be a way to change ONLY
    // the getter name w lombok, so manual getter
    public boolean areChunksUnloadable() {
        return this.chunksUnloadable;
    }
    @Setter
    private boolean chunksUnloadable = false;

    @Getter
    @Setter
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
    private List<int[]> savedChunks = new ArrayList<>();
}
