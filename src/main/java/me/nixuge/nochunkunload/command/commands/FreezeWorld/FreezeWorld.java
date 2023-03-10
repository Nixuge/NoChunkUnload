package me.nixuge.nochunkunload.command.commands.FreezeWorld;


import me.nixuge.nochunkunload.MessageBuilder;
import me.nixuge.nochunkunload.command.AbstractCommand;
import me.nixuge.nochunkunload.config.Cache;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.List;

public class FreezeWorld extends AbstractCommand {
    private final Cache cache;

    public FreezeWorld(final Cache cache) {
        super("freezeworld");

        this.cache = cache;
    }

    @Override
    public List<String> getCommandAliases() {
        ArrayList<String> al = new ArrayList<>();
        al.add("fw");
        al.add("freeze");
        return al;
    }

    @Override
    public void onCommand(final ICommandSender sender, final String[] args) {
        if (this.cache.isWorldFrozen()) {
            this.tell(new MessageBuilder("World already frozen", EnumChatFormatting.GRAY));
        } else {
            cache.setWorldFrozen(true);
            this.tell(new MessageBuilder("World now frozen.", EnumChatFormatting.GRAY));
        }
    }
}
