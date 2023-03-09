package me.nixuge.nochunkunload.config;

import lombok.Getter;

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

    public boolean isSavedChunk2(int[] newChunk) {
        for (int[] chunk : this.savedChunks2) {
            if (Arrays.equals(chunk, newChunk))
                return true;
        }
        return false;
    }
    public boolean isSavedChunk2(int chunkX, int chunkZ) {
        return isSavedChunk(new int[] { chunkX, chunkZ });
    }
    public void addSavedChunk2(int chunkX, int chunkZ) {
        this.savedChunks.add(new int[] { chunkX, chunkZ });
    }
    public void resetSavedChunks2() {
        this.savedChunks = new ArrayList<>();
    }
    public List<int[]> savedChunks2 = new ArrayList<>();
}
