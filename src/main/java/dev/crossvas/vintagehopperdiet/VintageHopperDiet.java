package dev.crossvas.vintagehopperdiet;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;

import java.util.logging.Logger;

@Mod(useMetadata = true, modid = VintageHopperDiet.ID,
        name = VintageHopperDiet.NAME,
        acceptedMinecraftVersions = "[1.5.2]")
public class VintageHopperDiet {

    public static final String ID = "vintagehopperdiet";
    public static final String NAME = "Vintage Hopper Diet";

    public static final Logger LOGGER = Logger.getLogger(NAME);

    public VintageHopperDiet() {
        LOGGER.setParent(FMLLog.getLogger());
    }
}
