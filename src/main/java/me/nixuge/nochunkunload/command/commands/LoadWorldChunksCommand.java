package me.nixuge.nochunkunload.command.commands;


import me.nixuge.nochunkunload.MessageBuilder;
import me.nixuge.nochunkunload.command.AbstractCommand;
import me.nixuge.nochunkunload.config.Cache;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraft.world.chunk.storage.AnvilSaveHandler;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.world.gen.ChunkProviderServer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoadWorldChunksCommand extends AbstractCommand {
    private final Cache cache;

    public LoadWorldChunksCommand(final Cache cache) {
        super("loadworldchunks");

        this.cache = cache;
    }

    @Override
    public List<String> getCommandAliases() {
        ArrayList<String> al = new ArrayList<>();
        al.add("loadworldchunks");
        al.add("lwc");
        return al;
    }

    @Override
    public void onCommand(final ICommandSender sender, final String[] args) {
        this.tell(new MessageBuilder("Trying to load world chunks", EnumChatFormatting.GRAY));
        // Minecraft
        // NetHandlerPlayClient
        // Minecraft.getMinecraft().save
        AnvilChunkLoader chunkLoader = new AnvilChunkLoader(new File("saves/owo"));
        // chunkLoader.loadChunk(null, getRequiredPermissionLevel(), getRequiredPermissionLevel())
        // chunkLoader.sa
        WorldClient worldClient = Minecraft.getMinecraft().theWorld;
        // worldClient.loadD
        try {
            Chunk chunk = chunkLoader.loadChunk(worldClient, 0, 0);
            for (ExtendedBlockStorage storage : chunk.getBlockStorageArray()) {
                if (storage == null) {
                    System.out.println("null");
                } else {
                    System.out.println("y" + storage.getYLocation());
                }
                
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        // ChunkProviderServer
        // AnvilChunkLoader
    }
}
