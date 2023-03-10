package me.nixuge.nochunkunload.config;

import lombok.Getter;

public class Cache {
    // Doesn't seem to be a way to change ONLY
    // the getter name w lombok, so manual getter
    public boolean areChunksUnloadable() {
        return this.chunksUnloadable;
    }
    public void setChunksUnloadable(boolean chunksUnloadable) {
        this.chunksUnloadable = chunksUnloadable;
    }
    private boolean chunksUnloadable = false;


    public void setWorldFrozen(boolean worldFrozen) {
        this.worldFrozen = worldFrozen;
    }
    @Getter
    private boolean worldFrozen = false;
}
