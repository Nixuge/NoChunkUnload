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
                ""
        );

        this.cache.setChunksUnloadable(this.chunksUnloadable);
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
}
