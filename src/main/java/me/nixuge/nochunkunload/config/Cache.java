package me.nixuge.nochunkunload.config;

import lombok.Data;

@Data()
public class Cache {
    // Doesn't seem to be a way to change ONLY
    // the getter name w lombok, so manual getter
    public boolean areChunksUnloadable() {
        return this.chunksUnloadable;
    }
    private boolean chunksUnloadable = false;
    private boolean worldFrozen = false;
}
