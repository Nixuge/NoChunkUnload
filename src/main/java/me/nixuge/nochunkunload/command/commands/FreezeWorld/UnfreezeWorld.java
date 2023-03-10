package me.nixuge.nochunkunload.command.commands.FreezeWorld;


import me.nixuge.nochunkunload.MessageBuilder;
import me.nixuge.nochunkunload.command.AbstractCommand;
import me.nixuge.nochunkunload.config.Cache;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.List;

public class UnfreezeWorld extends AbstractCommand {
    private final Cache cache;

    public UnfreezeWorld(final Cache cache) {
        super("unfreezeworld");

        this.cache = cache;
    }

    @Override
    public List<String> getCommandAliases() {
        ArrayList<String> al = new ArrayList<>();
        al.add("unfreeze");
        al.add("ufreeze");
        al.add("ufw");
        return al;
    }

    @Override
    public void onCommand(final ICommandSender sender, final String[] args) {
        if (this.cache.isWorldFrozen()) {
            cache.setWorldFrozen(false);
            this.tell(new MessageBuilder("World unfrozen", EnumChatFormatting.GRAY));
        } else {
            this.tell(new MessageBuilder("World already unfrozen.", EnumChatFormatting.GRAY));
        }
    }
}
