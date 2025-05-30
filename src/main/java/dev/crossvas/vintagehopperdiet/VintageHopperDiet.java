package dev.crossvas.vintagehopperdiet;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import mods.vintage.core.platform.config.ConfigHandler;
import mods.vintage.core.platform.config.IItemBlockIDProvider;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHopper;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

@Mod(modid = VintageHopperDiet.ID,
        name = "Vintage Hopper Diet",
        dependencies = "required-after:VintageCore",
        acceptedMinecraftVersions = "[1.5.2]",
        useMetadata = true)
public class VintageHopperDiet {

    public static final String ID = "vintagehopperdiet";
    VintageHopperConfig CONFIG;
    ConfigHandler CONFIG_HANDLER = new ConfigHandler(ID);

    public static Block HOPPER;

    @Mod.PreInit
    public void preInit(FMLPreInitializationEvent e) {
        this.CONFIG = new VintageHopperConfig(e.getSuggestedConfigurationFile());
        CONFIG_HANDLER.initIDs(CONFIG);
    }

    @Mod.Init
    public void init(FMLInitializationEvent e) {
        CONFIG_HANDLER.confirmIDs(CONFIG);
        HOPPER = new BlockDietHopper();
        GameRegistry.registerBlock(HOPPER, "diet_hopper");
    }

    @Mod.PostInit
    public void postInit(FMLPostInitializationEvent e) {
        CONFIG_HANDLER.confirmOwnership(CONFIG);
        GameRegistry.addShapelessRecipe(new ItemStack(HOPPER), Block.hopperBlock);
    }

    public static class BlockDietHopper extends BlockHopper implements IItemBlockIDProvider {

        public BlockDietHopper() {
            super(VintageHopperConfig.HOPPER.get());
            this.setUnlocalizedName("hopper");
        }

        @Override
        public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
            return AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1);
        }

        @Override
        public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 start, Vec3 end) {
            int meta = world.getBlockMetadata(x, y, z);
            List<AxisAlignedBB> boxes = getBoundingBoxes(meta, x, y, z);

            MovingObjectPosition closest = null;
            double closestDist = Double.MAX_VALUE;

            for (AxisAlignedBB box : boxes) {
                MovingObjectPosition mop = box.calculateIntercept(start, end);
                if (mop != null) {
                    double dist = start.distanceTo(mop.hitVec);
                    if (dist < closestDist) {
                        closest = new MovingObjectPosition(x, y, z, mop.sideHit, mop.hitVec);
                        closestDist = dist;
                    }
                }
            }

            return closest;
        }

        @Override
        public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB mask, List list, Entity entity) {
            int meta = world.getBlockMetadata(x, y, z);
            List<AxisAlignedBB> boxes = getBoundingBoxes(meta, x, y, z);

            for (AxisAlignedBB box : boxes) {
                if (box != null && mask.intersectsWith(box)) {
                    list.add(box);
                }
            }
        }

        private static List<AxisAlignedBB> getBoundingBoxes(int meta, int x, int y, int z) {
            List<AxisAlignedBB> boxes = new ArrayList<AxisAlignedBB>();

            AxisAlignedBB top = AxisAlignedBB.getBoundingBox(x + 0.0, y + 0.625, z + 0.0, x + 1.0, y + 1.0, z + 1.0);
            AxisAlignedBB center = AxisAlignedBB.getBoundingBox(x + 0.25, y + 0.25, z + 0.25, x + 0.75, y + 0.625, z + 0.75);
            boxes.add(top);
            boxes.add(center);

            switch (meta) {
                case 0: // Down
                    boxes.add(AxisAlignedBB.getBoundingBox(x + 0.375, y + 0.0, z + 0.375, x + 0.625, y + 0.25, z + 0.625));
                    break;
                case 1:
                    break;
                case 2: // North
                    boxes.add(AxisAlignedBB.getBoundingBox(x + 0.375, y + 0.25, z + 0.0, x + 0.625, y + 0.5, z + 0.25));
                    break;
                case 3: // South
                    boxes.add(AxisAlignedBB.getBoundingBox(x + 0.375, y + 0.25, z + 0.75, x + 0.625, y + 0.5, z + 1.0));
                    break;
                case 4: // West
                    boxes.add(AxisAlignedBB.getBoundingBox(x + 0.0, y + 0.25, z + 0.375, x + 0.25, y + 0.5, z + 0.625));
                    break;
                case 5: // East
                    boxes.add(AxisAlignedBB.getBoundingBox(x + 0.75, y + 0.25, z + 0.375, x + 1.0, y + 0.5, z + 0.625));
                    break;
            }

            return boxes;
        }
    }
}
