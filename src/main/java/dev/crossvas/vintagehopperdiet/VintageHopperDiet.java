package dev.crossvas.vintagehopperdiet;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHopper;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;

@Mod(modid = "vintagehopperdiet", name = "Vintage Hopper Diet", version = "1.5.2-1.0.0-forge", acceptedMinecraftVersions = "[1.5.2]")
public class VintageHopperDiet {

    @Mod.PostInit
    public void postInit(FMLPostInitializationEvent e) {
        Block hopper = new BlockDietHopper().setUnlocalizedName("hopper");
        GameRegistry.registerBlock(hopper, "diet_hopper");
        GameRegistry.addShapelessRecipe(new ItemStack(hopper), Block.hopperBlock);
    }

    public static class BlockDietHopper extends BlockHopper {

        public BlockDietHopper() {
            super(Block.hopperBlock.blockID + 256);
        }

        @Override
        public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
            this.setBlockBounds(0 / 16F, 10 / 16F, 0 / 16F, 16 / 16F, 16 / 16F, 16 / 16F);
        }
    }
}
