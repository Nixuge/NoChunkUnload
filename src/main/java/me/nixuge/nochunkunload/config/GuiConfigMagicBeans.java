package me.nixuge.nochunkunload.config;

import me.nixuge.nochunkunload.core.CoreMod;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.List;

// http://jabelarminecraft.blogspot.com/p/minecraft-modding-configuration-guis.html

public class GuiConfigMagicBeans extends GuiConfig {
    public GuiConfigMagicBeans(GuiScreen parentScreen, List<IConfigElement> configElements, String title) {
        super(parentScreen, configElements, "nochunkunload", false, false, title);
    }


//    public GuiConfigMagicBeans(GuiScreen parent)
//    {
//        super(parent,
//                new ConfigElement(
//
//                        CoreMod.config.getCategory(Configuration.CATEGORY_GENERAL))
//
//                        .getChildElements(),
//                CoreMod.MODID,
//                false,
//                false,
//                "Play Magic Beans Any Way You Want");
//        titleLine2 = CoreMod.configFile.getAbsolutePath();
//    }

    @Override
    public void initGui() {
        // You can add buttons and initialize fields here
        super.initGui();
    }


    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // You can do things like create animations, draw additional elements, etc. here
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        // You can process any additional buttons you may have added here
        super.actionPerformed(button);
    }
}

