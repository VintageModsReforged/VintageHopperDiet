package dev.crossvas.vintagehopperdiet;

import mods.vintage.core.platform.config.ItemBlockID;
import net.minecraftforge.common.Configuration;

import java.io.File;

public class VintageHopperConfig extends Configuration {

    public static ItemBlockID HOPPER = ItemBlockID.ofBlock("diet_hopper", 1);

    public VintageHopperConfig(File file) {
        super(file);
    }
}
