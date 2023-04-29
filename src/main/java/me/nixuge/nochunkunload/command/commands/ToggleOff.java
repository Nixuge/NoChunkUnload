package me.nixuge.nochunkunload.command.commands;


import me.nixuge.nochunkunload.MessageBuilder;
import me.nixuge.nochunkunload.command.AbstractCommand;
import me.nixuge.nochunkunload.config.Cache;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;

public class ToggleOff extends AbstractCommand {
    private final Cache cache;

    public ToggleOff(final Cache cache) {
        super("toggleoff");

        this.cache = cache;
    }

    @Override
    public List<String> getCommandAliases() {
        ArrayList<String> al = new ArrayList<>();
        al.add("toff");
        return al;
    }

    @Override
    public void onCommand(final ICommandSender sender, final String[] args) {
        this.cache.setChunksUnloadable(true);
        this.cache.setWorldFrozen(false);
        this.tell(new MessageBuilder("World now unfrozen & chunks unloadable", TextFormatting.GRAY));

    }
}
