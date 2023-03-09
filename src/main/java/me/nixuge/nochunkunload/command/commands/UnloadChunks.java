package me.nixuge.nochunkunload.command.commands;


import me.nixuge.nochunkunload.config.Cache;
import me.nixuge.nochunkunload.MessageBuilder;
import me.nixuge.nochunkunload.command.AbstractCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.List;

public class UnloadChunks extends AbstractCommand {
    private final Cache cache;

    public UnloadChunks(final Cache cache) {
        super("unloadchunk");

        this.cache = cache;
    }

    @Override
    public List<String> getCommandAliases() {
        ArrayList<String> al = new ArrayList<>();
        al.add("unloadchunks");
        al.add("uc");
        al.add("unloadc");
        return al;
    }

    @Override
    public void onCommand(final ICommandSender sender, final String[] args) {
        if (this.cache.areChunksUnloadable()) {
            this.tell(new MessageBuilder("Chunks are already unloadable.", EnumChatFormatting.GRAY));
        } else {
            this.cache.setChunksUnloadable(true);

            this.tell(new MessageBuilder("Chunks are now unloadable again.", EnumChatFormatting.GRAY));
        }
    }
}
