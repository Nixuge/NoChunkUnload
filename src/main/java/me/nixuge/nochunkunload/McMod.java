package me.nixuge.nochunkunload;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import me.nixuge.nochunkunload.command.commands.FreezeWorld.FreezeWorld;
import me.nixuge.nochunkunload.command.commands.FreezeWorld.UnfreezeWorld;
import me.nixuge.nochunkunload.command.commands.ToggleOff;
import me.nixuge.nochunkunload.command.commands.ToggleOn;
import me.nixuge.nochunkunload.command.commands.UnloadChunks.NoUnloadChunks;
import me.nixuge.nochunkunload.command.commands.UnloadChunks.UnloadChunks;
import me.nixuge.nochunkunload.config.Cache;
import me.nixuge.nochunkunload.config.Config;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.io.File;

@Mod(
        modid = McMod.MOD_ID,
        name = McMod.NAME,
        version = McMod.VERSION,
        guiFactory = "me.nixuge.nochunkunload.gui.GuiFactory",
        clientSideOnly = true
)
@Log4j2
@Getter
@Setter
public class McMod {
    public static final String MOD_ID = "nochunkunload";
    public static final String NAME = "No Chunk Unload";
    public static final String VERSION = "1.1.1";


    @Getter
    @Mod.Instance(value = McMod.MOD_ID)
    private static McMod instance;

    private final Cache cache = new Cache();
    private Configuration configuration;
    private String configDirectory;

    @Mod.EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
        this.configDirectory = event.getModConfigurationDirectory().toString();
        final File path = new File(this.configDirectory + File.separator + McMod.MOD_ID + ".cfg");
        this.configuration = new Configuration(path);
    }

    @Mod.EventHandler
    public void init(final FMLInitializationEvent event) {
        ClientCommandHandler.instance.registerCommand(new NoUnloadChunks(this.cache));
        ClientCommandHandler.instance.registerCommand(new UnloadChunks(this.cache));
        ClientCommandHandler.instance.registerCommand(new FreezeWorld(this.cache));
        ClientCommandHandler.instance.registerCommand(new UnfreezeWorld(this.cache));
        ClientCommandHandler.instance.registerCommand(new ToggleOn(this.cache));
        ClientCommandHandler.instance.registerCommand(new ToggleOff(this.cache));

        MinecraftForge.EVENT_BUS.register(
                new Config(this.cache, this.configuration)
        );
    }

    @SubscribeEvent
    public void worldChange(PlayerEvent.PlayerChangedDimensionEvent event) {
        this.cache.resetSavedChunks();
    }
}
