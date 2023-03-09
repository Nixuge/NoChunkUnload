package me.nixuge.nochunkunload.command.commands;

import me.nixuge.nochunkunload.MessageBuilder;
import me.nixuge.nochunkunload.command.AbstractCommand;
import me.nixuge.nochunkunload.config.Cache;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.EnumChatFormatting;

public class CurrentChunkLoaded extends AbstractCommand {
    private final Cache cache;
    public CurrentChunkLoaded(final Cache cache) {
        super("currentchunkloaded");

        this.cache = cache;
    }

    @Override
    public void onCommand(final ICommandSender sender, final String[] args) {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;

        this.tell(new MessageBuilder("x: " + player.chunkCoordX + ", y: " + player.chunkCoordY + ", z: " + player.chunkCoordZ, EnumChatFormatting.GRAY));

        this.tell(new MessageBuilder("Is loaded: " + this.cache.isSavedChunk(player.chunkCoordX, player.chunkCoordZ), EnumChatFormatting.GRAY));
        this.tell(new MessageBuilder("Saved chunks total: " + this.cache.savedChunks.size()));
    }
}
