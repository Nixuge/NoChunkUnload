package me.nixuge.nochunkunload.command.commands;


import me.nixuge.nochunkunload.config.Cache;
import me.nixuge.nochunkunload.MessageBuilder;
import me.nixuge.nochunkunload.command.AbstractCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.List;

public class NoUnloadChunks extends AbstractCommand {
    private final Cache cache;

    public NoUnloadChunks(final Cache cache) {
        super("nounloadchunk");

        this.cache = cache;
    }

    @Override
    public List<String> getCommandAliases() {
        ArrayList<String> al = new ArrayList<>();
        al.add("nounloadchunks");
        al.add("nuc");
        al.add("nounloadc");
        return al;
    }

    @Override
    public void onCommand(final ICommandSender sender, final String[] args) {
        if (this.cache.areChunksUnloadable()) {
            this.cache.setChunksUnloadable(false);
            this.tell(new MessageBuilder("Chunks are now not unloadable.", EnumChatFormatting.GRAY));
        } else {
            this.tell(new MessageBuilder("Chunks are already not unloadable.", EnumChatFormatting.GRAY));

        }
    }
}
