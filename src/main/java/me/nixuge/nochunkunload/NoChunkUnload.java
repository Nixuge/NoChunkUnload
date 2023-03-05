package me.nixuge.nochunkunload;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = NoChunkUnload.MOD_ID)
public class NoChunkUnload {
    // TODO:
    // Figure out how to get the actual mod loading in runClient

    public static final String MOD_ID = "nochunkunload";

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        System.out.println("Successfully loaded NoChunkUnload");
    }
}
