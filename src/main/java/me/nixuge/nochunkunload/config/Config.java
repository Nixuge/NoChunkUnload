package me.nixuge.nochunkunload.config;

import lombok.Data;
import me.nixuge.nochunkunload.McMod;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Data
public class Config {
    private final Cache cache;
    private final Configuration configuration;

    private boolean chunksUnloadable;
    private boolean worldFrozen;

    public Config(final Cache cache, final Configuration configuration) {
        this.cache = cache;
        this.configuration = configuration;

        this.loadConfiguration();
    }

    public void loadConfiguration() {
        this.chunksUnloadable = this.configuration.getBoolean(
                "Chunks Unloadable",
                Configuration.CATEGORY_GENERAL,
                false,
                "Allow chunks to be unloaded"
        );
        this.worldFrozen = this.configuration.getBoolean(
                "World Frozen",
                Configuration.CATEGORY_GENERAL,
                false,
                "Allow block change packets to be processed"
        );

        this.cache.setChunksUnloadable(this.chunksUnloadable);
        this.cache.setWorldFrozen(this.worldFrozen);
        if (this.configuration.hasChanged()) {
            this.configuration.save();
        }

    }

    @SubscribeEvent
    public void onConfigurationChangeEvent(final ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.modID.equalsIgnoreCase(McMod.MOD_ID)) {
            this.loadConfiguration();
        }
    }

    // TODO: make this actually work
    // as it doesn't rn
}
