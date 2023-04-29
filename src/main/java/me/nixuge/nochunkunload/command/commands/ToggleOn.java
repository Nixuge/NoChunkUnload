package me.nixuge.nochunkunload.command.commands;


import me.nixuge.nochunkunload.config.Cache;
import me.nixuge.nochunkunload.MessageBuilder;
import me.nixuge.nochunkunload.command.AbstractCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;

public class ToggleOn extends AbstractCommand {
    private final Cache cache;

    public ToggleOn(final Cache cache) {
        super("toggleon");

        this.cache = cache;
    }

    @Override
    public List<String> getCommandAliases() {
        ArrayList<String> al = new ArrayList<>();
        al.add("ton");
        return al;
    }

    @Override
    public void onCommand(final ICommandSender sender, final String[] args) {
        this.cache.setChunksUnloadable(false);
        this.cache.setWorldFrozen(true);
        this.tell(new MessageBuilder("World now frozen & chunks not unloadable", TextFormatting.GRAY));

    }
}
