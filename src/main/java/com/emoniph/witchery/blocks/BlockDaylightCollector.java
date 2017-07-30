package com.emoniph.witchery.blocks;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockDaylightCollector extends BlockBase {

   @SideOnly(Side.CLIENT)
   private IIcon iconGlobe;
   @SideOnly(Side.CLIENT)
   private IIcon iconGlobeCharged;


   public BlockDaylightCollector() {
      super(Material.iron);
      this.setBlockBounds(0.2F, 0.0F, 0.2F, 0.8F, 0.8F, 0.8F);
      this.setHardness(3.5F);
      this.setStepSound(Block.soundTypeMetal);
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public boolean renderAsNormalBlock() {
      return false;
   }

   public int getRenderType() {
      return 1;
   }

   public void onNeighborChange(IBlockAccess world, int x, int y, int z, int tileX, int tileY, int tileZ) {
      if(y == tileY && (x == tileX || z == tileZ) && world.getBlock(tileX, tileY, tileZ) == Blocks.daylight_detector) {
         ForgeDirection direction = ForgeDirection.UNKNOWN;
         if(x - tileX < 0) {
            direction = ForgeDirection.WEST;
         } else if(x - tileX > 0) {
            direction = ForgeDirection.EAST;
         } else if(z - tileZ < 0) {
            direction = ForgeDirection.NORTH;
         } else if(z - tileZ > 0) {
            direction = ForgeDirection.SOUTH;
         }

         Blocks.daylight_detector.isProvidingStrongPower(world, tileX, tileY, tileZ, direction.ordinal());
      }

   }

   public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
      if(!world.isRemote) {
         ForgeDirection[] directions = new ForgeDirection[]{ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.EAST, ForgeDirection.WEST};
         int meta = world.getBlockMetadata(x, y, z);
         if(meta > 0 && meta < 15) {
            ForgeDirection[] arr$ = directions;
            int len$ = directions.length;

            for(int i$ = 0; i$ < len$; ++i$) {
               ForgeDirection direction = arr$[i$];
               int otherX = x + direction.offsetX;
               int otherZ = z + direction.offsetZ;
               Block otherBlock = world.getBlock(otherX, y, otherZ);
               if(otherBlock == Blocks.daylight_detector) {
                  int power = Blocks.daylight_detector.isProvidingWeakPower(world, otherX, y, otherZ, direction.ordinal());
                  if(power == meta + 1) {
                     world.setBlockMetadataWithNotify(x, y, z, meta + 1, 3);
                  }
                  break;
               }
            }
         }
      }

   }

   public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
      if(!world.isRemote) {
         int meta = world.getBlockMetadata(x, y, z);
         if(meta == 0) {
            ItemStack stack = player.getHeldItem();
            if(Witchery.Items.GENERIC.itemQuartzSphere.isMatch(stack)) {
               --stack.stackSize;
               world.setBlockMetadataWithNotify(x, y, z, 1, 3);
            }
         } else if(meta < 15) {
            world.spawnEntityInWorld(new EntityItem(world, player.posX, player.posY + 1.0D, player.posZ, Witchery.Items.GENERIC.itemQuartzSphere.createStack()));
            world.setBlockMetadataWithNotify(x, y, z, 0, 3);
         } else if(meta == 15) {
            world.spawnEntityInWorld(new EntityItem(world, player.posX, player.posY + 1.0D, player.posZ, new ItemStack(Witchery.Items.SUN_GRENADE)));
            world.setBlockMetadataWithNotify(x, y, z, 0, 3);
         }

         return true;
      } else {
         return true;
      }
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIcon(int side, int meta) {
      switch(meta) {
      case 0:
         return super.getIcon(side, meta);
      case 15:
         return this.iconGlobeCharged;
      default:
         return this.iconGlobe;
      }
   }

   @SideOnly(Side.CLIENT)
   public void registerBlockIcons(IIconRegister iconRegister) {
      super.registerBlockIcons(iconRegister);
      this.iconGlobe = iconRegister.registerIcon(this.getTextureName() + "1");
      this.iconGlobeCharged = iconRegister.registerIcon(this.getTextureName() + "2");
   }
}
