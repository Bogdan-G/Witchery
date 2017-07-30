package com.emoniph.witchery.blocks;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockBaseBush;
import com.emoniph.witchery.util.MultiItemBlock;
import com.emoniph.witchery.worldgen.WorldGenLargeWitchTree;
import com.emoniph.witchery.worldgen.WorldGenWitchTree;
import cpw.mods.fml.common.IFuelHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.event.terraingen.TerrainGen;

public class BlockWitchSapling extends BlockBaseBush implements IFuelHandler, IGrowable {

   private static final String[] WOOD_TYPES = new String[]{"rowan", "alder", "hawthorn"};
   @SideOnly(Side.CLIENT)
   private IIcon[] saplingIcon;


   public BlockWitchSapling() {
      super(Material.plants, BlockWitchSapling.ClassItemBlock.class);
      this.setHardness(0.0F);
      this.setStepSound(Block.soundTypeGrass);
      float f = 0.4F;
      this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 2.0F, 0.5F + f);
      GameRegistry.registerFuelHandler(this);
   }

   public void updateTick(World world, int x, int y, int z, Random rand) {
      if(!world.isRemote) {
         super.updateTick(world, x, y, z, rand);
         if(world.getBlockLightValue(x, y + 1, z) >= 9 && rand.nextInt(7) == 0) {
            markOrGrowMarked(world, x, y, z, rand);
         }
      }

   }

   @SideOnly(Side.CLIENT)
   public IIcon getIcon(int par1, int metadata) {
      metadata &= 3;
      if(metadata < 0 || metadata >= this.saplingIcon.length) {
         metadata = 0;
      }

      return this.saplingIcon[metadata];
   }

   public static void markOrGrowMarked(World world, int x, int y, int z, Random rand) {
      int l = world.getBlockMetadata(x, y, z);
      if((l & 8) == 0) {
         world.setBlockMetadataWithNotify(x, y, z, l | 8, 4);
      } else {
         growTree(world, x, y, z, rand);
      }

   }

   public static void growTree(World world, int x, int y, int z, Random rand) {
      if(TerrainGen.saplingGrowTree(world, rand, x, y, z)) {
         int l = world.getBlockMetadata(x, y, z) & 3;
         Object object = null;
         byte i1 = 0;
         byte j1 = 0;
         boolean flag = false;
         WorldGenLargeWitchTree tree;
         if(l == 1) {
            tree = new WorldGenLargeWitchTree(true, 1, 1, 0.5D);
            tree.setScale(0.6D, 0.5D, 0.5D);
            object = tree;
         } else if(l == 2) {
            tree = new WorldGenLargeWitchTree(true, 2, 2);
            tree.setScale(0.8D, 1.2D, 1.0D);
            object = tree;
         } else {
            object = new WorldGenWitchTree(true, 5, 0, 0, 1, false);
         }

         if(flag) {
            world.setBlock(x + i1, y, z + j1, Blocks.air, 0, 4);
            world.setBlock(x + i1 + 1, y, z + j1, Blocks.air, 0, 4);
            world.setBlock(x + i1, y, z + j1 + 1, Blocks.air, 0, 4);
            world.setBlock(x + i1 + 1, y, z + j1 + 1, Blocks.air, 0, 4);
         } else {
            world.setBlock(x, y, z, Blocks.air, 0, 4);
         }

         if(!((WorldGenerator)object).generate(world, rand, x + i1, y, z + j1)) {
            if(flag) {
               world.setBlock(x + i1, y, z + j1, Witchery.Blocks.SAPLING, l, 4);
               world.setBlock(x + i1 + 1, y, z + j1, Witchery.Blocks.SAPLING, l, 4);
               world.setBlock(x + i1, y, z + j1 + 1, Witchery.Blocks.SAPLING, l, 4);
               world.setBlock(x + i1 + 1, y, z + j1 + 1, Witchery.Blocks.SAPLING, l, 4);
            } else {
               world.setBlock(x, y, z, Witchery.Blocks.SAPLING, l, 4);
            }
         }

      }
   }

   public boolean isSameSapling(World world, int x, int y, int z, int metadata) {
      return world.getBlock(x, y, z) == this && (world.getBlockMetadata(x, y, z) & 3) == metadata;
   }

   public int damageDropped(int metadata) {
      return metadata & 3;
   }

   @SideOnly(Side.CLIENT)
   public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list) {
      for(int i = 0; i < WOOD_TYPES.length; ++i) {
         list.add(new ItemStack(item, 1, i));
      }

   }

   @SideOnly(Side.CLIENT)
   public void registerBlockIcons(IIconRegister iconRegister) {
      this.saplingIcon = new IIcon[WOOD_TYPES.length];

      for(int i = 0; i < this.saplingIcon.length; ++i) {
         this.saplingIcon[i] = iconRegister.registerIcon(this.getTextureName() + "_" + WOOD_TYPES[i]);
      }

   }

   public int getBurnTime(ItemStack fuel) {
      return Item.getItemFromBlock(this) == fuel.getItem()?100:0;
   }

   public boolean func_149851_a(World world, int rand, int x, int y, boolean z) {
      return true;
   }

   public boolean func_149852_a(World world, Random rand, int x, int y, int z) {
      return (double)world.rand.nextFloat() < 0.75D;
   }

   public void func_149853_b(World world, Random rand, int x, int y, int z) {
      markOrGrowMarked(world, x, y, z, rand);
   }


   public static class ClassItemBlock extends MultiItemBlock {

      public ClassItemBlock(Block block) {
         super(block);
      }

      protected String[] getNames() {
         return BlockWitchSapling.WOOD_TYPES;
      }

      @SideOnly(Side.CLIENT)
      public IIcon getIconFromDamage(int par1) {
         return super.field_150939_a.getIcon(0, par1);
      }
   }
}
