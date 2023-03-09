package me.nixuge.nochunkunload.command.commands;

import me.nixuge.nochunkunload.MessageBuilder;
import me.nixuge.nochunkunload.command.AbstractCommand;
import me.nixuge.nochunkunload.config.Cache;
import net.minecraft.command.ICommandSender;

public class ResetChunkLoaded extends AbstractCommand {
    private final Cache cache;
    public ResetChunkLoaded(final Cache cache) {
        super("resetchunkloaded");

        this.cache = cache;
    }

    @Override
    public void onCommand(final ICommandSender sender, final String[] args) {
        this.tell(new MessageBuilder("Saved chunks before: " + this.cache.savedChunks.size()));
        this.cache.resetSavedChunks();
        this.tell(new MessageBuilder("Saved chunks now: " + this.cache.savedChunks.size()));
    }
}
