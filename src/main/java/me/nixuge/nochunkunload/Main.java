package me.nixuge.nochunkunload;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@net.minecraftforge.fml.common.Mod(modid = Main.MOD_ID)
public class Main {
    public static final String MOD_ID = "nochunkunload";

    @net.minecraftforge.fml.common.Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        System.out.println("Successfully loaded NoChunkUnload");
    }
}
