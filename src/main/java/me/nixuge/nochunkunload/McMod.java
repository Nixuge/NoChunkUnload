package me.nixuge.nochunkunload;

import lombok.Getter;
import lombok.Setter;
// import lombok.extern.log4j.Log4j2;
import me.nixuge.nochunkunload.command.commands.FreezeWorld.FreezeWorld;
import me.nixuge.nochunkunload.command.commands.FreezeWorld.UnfreezeWorld;
import me.nixuge.nochunkunload.command.commands.ToggleOff;
import me.nixuge.nochunkunload.command.commands.ToggleOn;
import me.nixuge.nochunkunload.command.commands.UnloadChunks.NoUnloadChunks;
import me.nixuge.nochunkunload.command.commands.UnloadChunks.UnloadChunks;
import me.nixuge.nochunkunload.config.Cache;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

@Mod(
        modid = McMod.MOD_ID,
        name = McMod.NAME,
        version = McMod.VERSION,
        guiFactory = "me.nixuge.nochunkunload.gui.GuiFactory",
        clientSideOnly = true
)
// @Log4j2
@Getter
@Setter
public class McMod {
    public static final String MOD_ID = "nochunkunload";
    public static final String NAME = "No Chunk Unload";
    public static final String VERSION = "1.2.1";


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
    }


    // 2do maybe?: handle c07packetplayerdigging, c08packetplayerblockplacement packets
    // TODO: get config working
    // Last commit with forge config: d3b7dc2f9cabafd80945261022f9c3c35ce3977c
    // Files in config/ gui/ & McMod.java

    // TODO: fix
    //Caused by: java.lang.IllegalArgumentException: Cannot get property PropertyDirection{name=facing, clazz=class net.minecraft.util.EnumFacing, values=[north, south, west, east]} as it does not exist in BlockState{block=minecraft:air, properties=[]}
}
