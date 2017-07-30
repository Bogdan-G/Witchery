package com.emoniph.witchery.blocks;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.WitcheryCreativeTab;
import com.emoniph.witchery.util.BlockUtil;
import com.emoniph.witchery.util.MultiItemBlock;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeavesBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;

public class BlockWitchLeaves extends BlockLeavesBase implements IShearable {

   private static final String[] LEAF_TYPES = new String[]{"rowan", "alder", "hawthorn"};
   private static final String[][] field_94396_b = new String[][]{{"_rowan", "_alder", "_hawthorn"}, {"_rowan_opaque", "_alder_opaque", "_hawthorn_opaque"}};
   private int field_94394_cP;
   private int[] adjacentTreeBlocks;
   private IIcon[][] iconsForModes = new IIcon[2][];
   private int[] decayMatrix;


   public BlockWitchLeaves() {
      super(Material.leaves, false);
      this.setHardness(0.2F);
      this.setLightOpacity(1);
      this.setStepSound(Block.soundTypeGrass);
      this.setTickRandomly(true);
      this.setCreativeTab(WitcheryCreativeTab.INSTANCE);
   }

   public Block setBlockName(String blockName) {
      BlockUtil.registerBlock(this, BlockWitchLeaves.ClassItemBlock.class, blockName);
      super.setBlockName(blockName);
      Blocks.fire.setFireInfo(this, 30, 60);
      return this;
   }

   @SideOnly(Side.CLIENT)
   public int getBlockColor() {
      double d0 = 0.5D;
      double d1 = 1.0D;
      return ColorizerFoliage.getFoliageColor(d0, d1);
   }

   @SideOnly(Side.CLIENT)
   public int getRenderColor(int par1) {
      return (par1 & 3) == 1?getFoliageColorAlder():((par1 & 3) == 2?getFoliageColorHawthorn():getFoliageColorBasic());
   }

   @SideOnly(Side.CLIENT)
   public static int getFoliageColorAlder() {
      return 3774771;
   }

   @SideOnly(Side.CLIENT)
   public static int getFoliageColorHawthorn() {
      return 6728294;
   }

   @SideOnly(Side.CLIENT)
   public static int getFoliageColorBasic() {
      return 4764952;
   }

   @SideOnly(Side.CLIENT)
   public int colorMultiplier(IBlockAccess world, int x, int y, int z) {
      int l = world.getBlockMetadata(x, y, z);
      if((l & 3) == 1) {
         return getFoliageColorAlder();
      } else if((l & 3) == 2) {
         return getFoliageColorHawthorn();
      } else {
         int i1 = 0;
         int j1 = 0;
         int k1 = 0;

         for(int l1 = -1; l1 <= 1; ++l1) {
            for(int i2 = -1; i2 <= 1; ++i2) {
               int j2 = world.getBiomeGenForCoords(x + i2, z + l1).getBiomeFoliageColor(x + l1, y, z + k1);
               i1 += (j2 & 16711680) >> 16;
               j1 += (j2 & '\uff00') >> 8;
               k1 += j2 & 255;
            }
         }

         return (i1 / 9 & 255) << 16 | (j1 / 9 & 255) << 8 | k1 / 9 & 255;
      }
   }

   public void breakBlock(World world, int x, int y, int z, Block block0, int meta0) {
      byte b0 = 1;
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

   public void updateTick(World world, int x, int y, int z, Random rand) {
      if(!world.isRemote) {
         int meta = world.getBlockMetadata(x, y, z);
         if((meta & 8) != 0 && (meta & 4) == 0) {
            byte b0 = 4;
            int i1 = b0 + 1;
            byte b1 = 32;
            int j1 = b1 * b1;
            int k1 = b1 / 2;
            if(this.decayMatrix == null) {
               this.decayMatrix = new int[b1 * b1 * b1];
            }

            int l1;
            if(world.checkChunksExist(x - i1, y - i1, z - i1, x + i1, y + i1, z + i1)) {
               int i2;
               int j2;
               for(l1 = -b0; l1 <= b0; ++l1) {
                  for(i2 = -b0; i2 <= b0; ++i2) {
                     for(j2 = -b0; j2 <= b0; ++j2) {
                        Block k2 = world.getBlock(x + l1, y + i2, z + j2);
                        if(!k2.canSustainLeaves(world, x + l1, y + i2, z + j2)) {
                           if(k2.isLeaves(world, x + l1, y + i2, z + j2)) {
                              this.decayMatrix[(l1 + k1) * j1 + (i2 + k1) * b1 + j2 + k1] = -2;
                           } else {
                              this.decayMatrix[(l1 + k1) * j1 + (i2 + k1) * b1 + j2 + k1] = -1;
                           }
                        } else {
                           this.decayMatrix[(l1 + k1) * j1 + (i2 + k1) * b1 + j2 + k1] = 0;
                        }
                     }
                  }
               }

               for(l1 = 1; l1 <= 4; ++l1) {
                  for(i2 = -b0; i2 <= b0; ++i2) {
                     for(j2 = -b0; j2 <= b0; ++j2) {
                        for(int var16 = -b0; var16 <= b0; ++var16) {
                           if(this.decayMatrix[(i2 + k1) * j1 + (j2 + k1) * b1 + var16 + k1] == l1 - 1) {
                              if(this.decayMatrix[(i2 + k1 - 1) * j1 + (j2 + k1) * b1 + var16 + k1] == -2) {
                                 this.decayMatrix[(i2 + k1 - 1) * j1 + (j2 + k1) * b1 + var16 + k1] = l1;
                              }

                              if(this.decayMatrix[(i2 + k1 + 1) * j1 + (j2 + k1) * b1 + var16 + k1] == -2) {
                                 this.decayMatrix[(i2 + k1 + 1) * j1 + (j2 + k1) * b1 + var16 + k1] = l1;
                              }

                              if(this.decayMatrix[(i2 + k1) * j1 + (j2 + k1 - 1) * b1 + var16 + k1] == -2) {
                                 this.decayMatrix[(i2 + k1) * j1 + (j2 + k1 - 1) * b1 + var16 + k1] = l1;
                              }

                              if(this.decayMatrix[(i2 + k1) * j1 + (j2 + k1 + 1) * b1 + var16 + k1] == -2) {
                                 this.decayMatrix[(i2 + k1) * j1 + (j2 + k1 + 1) * b1 + var16 + k1] = l1;
                              }

                              if(this.decayMatrix[(i2 + k1) * j1 + (j2 + k1) * b1 + (var16 + k1 - 1)] == -2) {
                                 this.decayMatrix[(i2 + k1) * j1 + (j2 + k1) * b1 + (var16 + k1 - 1)] = l1;
                              }

                              if(this.decayMatrix[(i2 + k1) * j1 + (j2 + k1) * b1 + var16 + k1 + 1] == -2) {
                                 this.decayMatrix[(i2 + k1) * j1 + (j2 + k1) * b1 + var16 + k1 + 1] = l1;
                              }
                           }
                        }
                     }
                  }
               }
            }

            l1 = this.decayMatrix[k1 * j1 + k1 * b1 + k1];
            if(l1 >= 0) {
               world.setBlockMetadataWithNotify(x, y, z, meta & -9, 4);
            } else {
               this.removeLeaves(world, x, y, z);
            }
         }
      }

   }

   @SideOnly(Side.CLIENT)
   public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
      if(world.canLightningStrikeAt(x, y + 1, z) && !World.doesBlockHaveSolidTopSurface(world, x, y - 1, z) && rand.nextInt(15) == 1) {
         double d0 = (double)((float)x + rand.nextFloat());
         double d1 = (double)y - 0.05D;
         double d2 = (double)((float)z + rand.nextFloat());
         world.spawnParticle("dripWater", d0, d1, d2, 0.0D, 0.0D, 0.0D);
      }

   }

   private void removeLeaves(World world, int x, int y, int z) {
      this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
      world.setBlockToAir(x, y, z);
   }

   public int quantityDropped(Random rand) {
      return rand.nextInt(20) == 0?1:0;
   }

   public Item getItemDropped(int metadata, Random rand, int fortune) {
      return Item.getItemFromBlock(Witchery.Blocks.SAPLING);
   }

   public void dropBlockAsItemWithChance(World world, int x, int y, int z, int par5, float par6, int par7) {
      if(!world.isRemote) {
         int j1 = 20;
         if((par5 & 3) == 3) {
            j1 = 40;
         }

         if(par7 > 0) {
            j1 -= 2 << par7;
            if(j1 < 10) {
               j1 = 10;
            }
         }

         if(world.rand.nextInt(j1) == 0) {
            Item k1 = this.getItemDropped(par5, world.rand, par7);
            this.dropBlockAsItem(world, x, y, z, new ItemStack(k1, 1, this.damageDropped(par5)));
         }

         j1 = 200;
         if(par7 > 0) {
            j1 -= 10 << par7;
            if(j1 < 40) {
               j1 = 40;
            }
         }

         if((par5 & 3) == 0 && world.rand.nextInt(j1) == 0) {
            this.dropBlockAsItem(world, x, y, z, Witchery.Items.GENERIC.itemRowanBerries.createStack());
         }
      }

   }

   public void harvestBlock(World world, EntityPlayer player, int par3, int par4, int par5, int par6) {
      super.harvestBlock(world, player, par3, par4, par5, par6);
   }

   public int damageDropped(int par1) {
      return par1 & 3;
   }

   public boolean isOpaqueCube() {
      this.setGraphicsLevel(Witchery.proxy.getGraphicsLevel());
      return !super.field_150121_P;
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIcon(int par1, int par2) {
      return (par2 & 3) == 1?this.iconsForModes[this.field_94394_cP][1]:((par2 & 3) == 2?this.iconsForModes[this.field_94394_cP][2]:this.iconsForModes[this.field_94394_cP][0]);
   }

   public void setGraphicsLevel(boolean par1) {
      super.field_150121_P = par1;
      this.field_94394_cP = par1?0:1;
   }

   @SideOnly(Side.CLIENT)
   public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list) {
      for(int i = 0; i < LEAF_TYPES.length; ++i) {
         list.add(new ItemStack(item, 1, i));
      }

   }

   protected ItemStack createStackedBlock(int par1) {
      return new ItemStack(this, 1, par1 & 3);
   }

   @SideOnly(Side.CLIENT)
   public void registerBlockIcons(IIconRegister par1IconRegister) {
      for(int i = 0; i < field_94396_b.length; ++i) {
         this.iconsForModes[i] = new IIcon[field_94396_b[i].length];

         for(int j = 0; j < field_94396_b[i].length; ++j) {
            this.iconsForModes[i][j] = par1IconRegister.registerIcon(this.getTextureName() + field_94396_b[i][j]);
         }
      }

   }

   public boolean isShearable(ItemStack item, IBlockAccess world, int x, int y, int z) {
      return true;
   }

   public ArrayList onSheared(ItemStack item, IBlockAccess world, int x, int y, int z, int fortune) {
      ArrayList ret = new ArrayList();
      ret.add(new ItemStack(this, 1, world.getBlockMetadata(x, y, z) & 3));
      return ret;
   }

   public void beginLeavesDecay(World world, int x, int y, int z) {
      world.setBlockMetadataWithNotify(x, y, z, world.getBlockMetadata(x, y, z) | 8, 4);
   }

   public boolean isLeaves(IBlockAccess world, int x, int y, int z) {
      return true;
   }


   public static class ClassItemBlock extends MultiItemBlock {

      public ClassItemBlock(Block block) {
         super(block);
      }

      protected String[] getNames() {
         return BlockWitchLeaves.LEAF_TYPES;
      }
   }
}
