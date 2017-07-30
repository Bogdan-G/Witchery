package com.emoniph.witchery.blocks;

import com.emoniph.witchery.blocks.BlockBaseRotatedPillar;
import com.emoniph.witchery.entity.EntityEnt;
import com.emoniph.witchery.util.Log;
import com.emoniph.witchery.util.MultiItemBlock;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import cpw.mods.fml.common.IFuelHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockWitchLog extends BlockBaseRotatedPillar implements IFuelHandler {

   public static final String[] WOOD_TYPES = new String[]{"rowan", "alder", "hawthorn"};
   @SideOnly(Side.CLIENT)
   private IIcon[] field_111052_c;
   @SideOnly(Side.CLIENT)
   private IIcon[] tree_top;


   public BlockWitchLog() {
      super(Material.wood, BlockWitchLog.ClassItemBlock.class);
      this.setHardness(2.0F);
      this.setStepSound(Block.soundTypeWood);
      GameRegistry.registerFuelHandler(this);
   }

   public Block setBlockName(String blockName) {
      super.setBlockName(blockName);
      Blocks.fire.setFireInfo(this, 5, 5);
      return this;
   }

   public void dropBlockAsItemWithChance(World world, int x, int y, int z, int par5, float par6, int par7) {
      if(!world.isRemote) {
         double chance = 0.01D;
         chance += world.getBlock(x, y + 1, z) == this?0.01D:0.0D;
         chance += world.getBlock(x, y - 1, z) == this?0.01D:0.0D;
         chance += world.getBlock(x + 1, y, z) == this?0.01D:0.0D;
         chance += world.getBlock(x - 1, y, z) == this?0.01D:0.0D;
         chance += world.getBlock(x, y, z + 1) == this?0.01D:0.0D;
         chance += world.getBlock(x, y, z - 1) == this?0.01D:0.0D;
         chance = Math.min(chance, 0.05D);
         double roll = world.rand.nextDouble();
         Log.instance().debug("Ents: Chance: " + chance + ", roll: " + roll);
         if(roll < chance) {
            boolean MAX_DISTANCE = true;
            boolean MIN_DISTANCE = true;
            byte activeRadius = 8;
            int ax = world.rand.nextInt(activeRadius * 2 + 1);
            if(ax > activeRadius) {
               ax += 16;
            }

            int nx = x - 16 + ax;
            int az = world.rand.nextInt(activeRadius * 2 + 1);
            if(az > activeRadius) {
               az += 16;
            }

            int nz = z - 16 + az;

            int ny;
            for(ny = y; !world.isAirBlock(nx, ny, nz) && ny < y + 8; ++ny) {
               ;
            }

            while(world.isAirBlock(nx, ny, nz) && ny > 0) {
               --ny;
            }

            int hy;
            for(hy = 0; world.isAirBlock(nx, ny + hy + 1, nz) && hy < 6; ++hy) {
               ;
            }

            Log.instance().debug("Ents: hy: " + hy + " (" + nx + "," + ny + "," + nz + ")");
            if(hy >= 3) {
               EntityEnt ent = new EntityEnt(world);
               ent.setLocationAndAngles(0.5D + (double)nx, 0.05D + (double)ny + 1.0D, 0.5D + (double)nz, 0.0F, 0.0F);
               world.spawnEntityInWorld(ent);
               ParticleEffect.INSTANT_SPELL.send(SoundEffect.NOTE_HARP, world, (double)x, (double)y, (double)z, 1.0D, 1.0D, 8);
               ParticleEffect.LARGE_SMOKE.send(SoundEffect.MOB_HORSE_SKELETON_DEATH, ent, 2.0D, 4.0D, 16);
            }
         }
      }

      super.dropBlockAsItemWithChance(world, x, y, z, par5, par6, par7);
   }

   public int quantityDropped(Random par1Random) {
      return 1;
   }

   public Item getItemDropped(int metadata, Random rand, int fortune) {
      return Item.getItemFromBlock(this);
   }

   public void breakBlock(World world, int x, int y, int z, Block origBlock, int par6) {
      byte b0 = 4;
      int i1 = b0 + 1;
      if(world.checkChunksExist(x - i1, y - i1, z - i1, x + i1, y + i1, z + i1)) {
         for(int j1 = -b0; j1 <= b0; ++j1) {
            for(int k1 = -b0; k1 <= b0; ++k1) {
               for(int l1 = -b0; l1 <= b0; ++l1) {
                  Block block = world.getBlock(x + j1, y + k1, z + l1);
                  if(block.isLeaves(world, x + j1, y + k1, z + l1)) {
                     block.beginLeavesDecay(world, x + j1, y + k1, z + l1);
                  }
               }
            }
         }
      }

   }

   @SideOnly(Side.CLIENT)
   protected IIcon getSideIcon(int par1) {
      if(par1 < 0 || par1 >= WOOD_TYPES.length) {
         par1 = 0;
      }

      return this.field_111052_c[par1];
   }

   @SideOnly(Side.CLIENT)
   protected IIcon getTopIcon(int par1) {
      if(par1 < 0 || par1 >= WOOD_TYPES.length) {
         par1 = 0;
      }

      return this.tree_top[par1];
   }

   public static int limitToValidMetadata(int par0) {
      return par0 & WOOD_TYPES.length - 1;
   }

   @SideOnly(Side.CLIENT)
   public void getSubBlocks(Item block, CreativeTabs creativeTabs, List list) {
      for(int i = 0; i < WOOD_TYPES.length; ++i) {
         list.add(new ItemStack(this, 1, i));
      }

   }

   @SideOnly(Side.CLIENT)
   public void registerBlockIcons(IIconRegister iconRegister) {
      this.field_111052_c = new IIcon[WOOD_TYPES.length];
      this.tree_top = new IIcon[WOOD_TYPES.length];

      for(int i = 0; i < this.field_111052_c.length; ++i) {
         this.field_111052_c[i] = iconRegister.registerIcon(this.getTextureName() + "_" + WOOD_TYPES[i]);
         this.tree_top[i] = iconRegister.registerIcon(this.getTextureName() + "_" + WOOD_TYPES[i] + "_top");
      }

   }

   public boolean canSustainLeaves(IBlockAccess world, int x, int y, int z) {
      return true;
   }

   public boolean isWood(IBlockAccess world, int x, int y, int z) {
      return true;
   }

   public int getBurnTime(ItemStack fuel) {
      return Item.getItemFromBlock(this) == fuel.getItem()?300:0;
   }

   public int getFlammability(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
      return world.getBlockMetadata(x, y, z) == 2?1:super.getFlammability(world, x, y, z, face);
   }

   public int getFireSpreadSpeed(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
      return world.getBlockMetadata(x, y, z) == 2?1:super.getFireSpreadSpeed(world, x, y, z, face);
   }


   public static class ClassItemBlock extends MultiItemBlock {

      public ClassItemBlock(Block block) {
         super(block);
      }

      protected String[] getNames() {
         return BlockWitchLog.WOOD_TYPES;
      }
   }
}
