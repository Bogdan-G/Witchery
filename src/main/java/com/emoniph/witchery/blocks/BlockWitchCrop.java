package com.emoniph.witchery.blocks;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockBaseBush;
import com.emoniph.witchery.entity.EntityMandrake;
import com.emoniph.witchery.network.PacketParticles;
import com.emoniph.witchery.util.BlockUtil;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.emoniph.witchery.util.TargetPointUtil;
import com.emoniph.witchery.util.TimeUtil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockWitchCrop extends BlockBaseBush implements IGrowable {

   @SideOnly(Side.CLIENT)
   private IIcon[] iconArray;
   private ItemStack seedItemPrototype;
   private ItemStack cropItemPrototype;
   private final int growthStages;
   private final boolean waterPlant;
   private final boolean canFertilize;
   private static final int MIN_LIGHT_LEVEL = 9;
   private static final double NIGHT_MANDRAKE_SPAWN_CHANCE = 0.1D;
   private static final double DAY_MANDRAKE_SPAWN_CHANCE = 0.9D;


   public BlockWitchCrop(boolean waterPlant) {
      this(waterPlant, 4, true);
   }

   public BlockWitchCrop(boolean waterPlant, int growthStages, boolean canFertilize) {
      super(Material.plants);
      super.registerWithCreateTab = false;
      this.growthStages = growthStages;
      this.waterPlant = waterPlant;
      this.canFertilize = canFertilize;
      this.setTickRandomly(true);
      float f = 0.5F;
      this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.25F, 0.5F + f);
      this.setHardness(0.0F);
      this.setStepSound(Block.soundTypeGrass);
      this.disableStats();
   }

   public BlockWitchCrop setSeedItem(ItemStack stack) {
      this.seedItemPrototype = stack;
      return this;
   }

   public BlockWitchCrop setCropItem(ItemStack stack) {
      this.cropItemPrototype = stack != null?stack:this.seedItemPrototype.copy();
      return this;
   }

   @SideOnly(Side.CLIENT)
   public void registerBlockIcons(IIconRegister iconRegister) {
      this.iconArray = new IIcon[this.getNumGrowthStages() + 1];

      for(int i = 0; i < this.iconArray.length; ++i) {
         this.iconArray[i] = iconRegister.registerIcon(this.getTextureName() + "_stage_" + i);
      }

   }

   protected boolean canPlaceBlockOn(Block block) {
      return this.waterPlant?block == Blocks.water:block == Blocks.grass || block == Blocks.dirt || block == Blocks.farmland || block == this || block == Witchery.Blocks.CROP_WORMWOOD;
   }

   public int getNumGrowthStages() {
      return this.growthStages;
   }

   public void updateTick(World world, int posX, int posY, int posZ, Random rand) {
      super.updateTick(world, posX, posY, posZ, rand);
      if(world.getBlockLightValue(posX, posY + 1, posZ) >= 9) {
         int l = world.getBlockMetadata(posX, posY, posZ);
         if(l < this.getNumGrowthStages()) {
            float blockBelow = this.getGrowthRate(world, posX, posY, posZ);
            if(rand.nextInt((int)(25.0F / blockBelow) + 1) == 0) {
               ++l;
               world.setBlockMetadataWithNotify(posX, posY, posZ, l, 2);
            }
         } else if(this == Witchery.Blocks.CROP_WORMWOOD) {
            Block var8 = BlockUtil.getBlock(world, posX, posY - 1, posZ);
            if(var8 != this && world.isAirBlock(posX, posY + 1, posZ)) {
               BlockUtil.setBlock(world, posX, posY + 1, posZ, (Block)this, 0, 3);
            }
         }
      }

   }

   public boolean fertilize(World world, int posX, int posY, int posZ) {
      if(!world.isRemote) {
         int stages = this.getNumGrowthStages();
         int current = world.getBlockMetadata(posX, posY, posZ);
         if(current == stages) {
            return false;
         } else {
            int l;
            if(!this.canFertilize) {
               l = current + 1;
            } else {
               l = current + MathHelper.getRandomIntegerInRange(world.rand, 2, stages);
            }

            if(l > stages) {
               l = stages;
            }

            world.setBlockMetadataWithNotify(posX, posY, posZ, l, 2);
            return true;
         }
      } else {
         return false;
      }
   }

   public boolean func_149851_a(World world, int x, int y, int z, boolean flag) {
      return world.getBlockMetadata(x, y, z) != this.getNumGrowthStages();
   }

   public boolean func_149852_a(World world, Random rand, int x, int y, int z) {
      return true;
   }

   public void func_149853_b(World world, Random rand, int x, int y, int z) {
      this.fertilize(world, x, y, z);
   }

   private float getGrowthRate(World world, int posX, int posY, int posZ) {
      float f = 1.0F;
      Block l = world.getBlock(posX, posY, posZ - 1);
      Block i1 = world.getBlock(posX, posY, posZ + 1);
      Block j1 = world.getBlock(posX - 1, posY, posZ);
      Block k1 = world.getBlock(posX + 1, posY, posZ);
      Block l1 = world.getBlock(posX - 1, posY, posZ - 1);
      Block i2 = world.getBlock(posX + 1, posY, posZ - 1);
      Block j2 = world.getBlock(posX + 1, posY, posZ + 1);
      Block k2 = world.getBlock(posX - 1, posY, posZ + 1);
      boolean flag = j1 == this || k1 == this;
      boolean flag1 = l == this || i1 == this;
      boolean flag2 = l1 == this || i2 == this || j2 == this || k2 == this;

      for(int l2 = posX - 1; l2 <= posX + 1; ++l2) {
         for(int i3 = posZ - 1; i3 <= posZ + 1; ++i3) {
            Block j3 = world.getBlock(l2, posY - 1, i3);
            float f1 = 0.0F;
            if(j3 != null && j3.canSustainPlant(world, l2, posY - 1, i3, ForgeDirection.UP, this)) {
               f1 = 1.0F;
               if(j3.isFertile(world, l2, posY - 1, i3)) {
                  f1 = 3.0F;
               }
            }

            if(l2 != posX || i3 != posZ) {
               f1 /= 4.0F;
            }

            f += f1;
         }
      }

      if(flag2 || flag && flag1) {
         f /= 2.0F;
      }

      if(this.cropItemPrototype.getItem() == Witchery.Items.SEEDS_MINDRAKE) {
         f /= 1.5F;
      }

      return f;
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIcon(int par1, int par2) {
      int stages = this.getNumGrowthStages();
      if(par2 < 0 || par2 > stages) {
         par2 = stages;
      }

      return this.iconArray != null?this.iconArray[par2]:null;
   }

   public int getRenderType() {
      return this != Witchery.Blocks.CROP_SNOWBELL && this != Witchery.Blocks.CROP_WOLFSBANE && this != Witchery.Blocks.CROP_WORMWOOD?6:1;
   }

   protected ItemStack getSeedItemStack() {
      return this.seedItemPrototype.copy();
   }

   protected ItemStack getCropItemStack() {
      return this.cropItemPrototype.copy();
   }

   public void dropBlockAsItemWithChance(World par1World, int par2, int par3, int par4, int par5, float par6, int par7) {
      super.dropBlockAsItemWithChance(par1World, par2, par3, par4, par5, par6, 0);
   }

   public ArrayList getDrops(World world, int x, int y, int z, int metadata, int fortune) {
      ArrayList ret = new ArrayList();
      world.getBlock(x, y, z);
      int i;
      if(metadata >= this.getNumGrowthStages()) {
         if(Witchery.Items.GENERIC.itemMandrakeRoot.isMatch(this.cropItemPrototype) && world.difficultySetting != EnumDifficulty.PEACEFUL && (!world.provider.isDaytime() || world.rand.nextDouble() <= 0.9D) && (world.provider.isDaytime() || world.rand.nextDouble() <= 0.1D)) {
            if(!world.isRemote) {
               EntityMandrake var10 = new EntityMandrake(world);
               var10.setLocationAndAngles(0.5D + (double)x, 0.05D + (double)y, 0.5D + (double)z, 0.0F, 0.0F);
               world.spawnEntityInWorld(var10);
               Witchery.packetPipeline.sendToAllAround(new PacketParticles(ParticleEffect.EXPLODE, SoundEffect.NONE, var10, 0.5D, 1.0D), TargetPointUtil.from(var10, 16.0D));
            }
         } else if(this.cropItemPrototype.getItem() == Witchery.Items.SEEDS_MINDRAKE) {
            ret.add(this.getSeedItemStack());
            if(world.rand.nextInt(4) == 0) {
               ret.add(this.getCropItemStack());
            }
         } else {
            for(i = 0; i < 3 + fortune; ++i) {
               if(world.rand.nextInt(15) <= 7) {
                  ret.add(this.getSeedItemStack());
               }
            }

            for(i = 0; i < this.quantityDropped(world.rand); ++i) {
               ret.add(this.getCropItemStack());
            }

            if(this.seedItemPrototype.getItem() == Witchery.Items.SEEDS_SNOWBELL && world.rand.nextDouble() <= 0.2D) {
               ret.add(Witchery.Items.GENERIC.itemIcyNeedle.createStack());
            }
         }
      } else {
         for(i = 0; i < this.quantityDropped(world.rand); ++i) {
            ret.add(this.getSeedItemStack());
         }
      }

      return ret;
   }

   protected void dropBlockAsItem(World p_149642_1_, int p_149642_2_, int p_149642_3_, int p_149642_4_, ItemStack p_149642_5_) {
      if(!p_149642_1_.isRemote && p_149642_1_.getGameRules().getGameRuleBooleanValue("doTileDrops")) {
         float f = 0.7F;
         double d0 = (double)(p_149642_1_.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
         double d1 = (double)(p_149642_1_.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
         double d2 = (double)(p_149642_1_.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
         EntityItem entityitem = new EntityItem(p_149642_1_, (double)p_149642_2_ + d0, (double)p_149642_3_ + d1, (double)p_149642_4_ + d2, p_149642_5_);
         entityitem.delayBeforeCanPickup = 10;
         if(p_149642_5_ != null && p_149642_5_.getItem() == Witchery.Items.SEEDS_MINDRAKE) {
            entityitem.lifespan = TimeUtil.secsToTicks(3);
         }

         p_149642_1_.spawnEntityInWorld(entityitem);
      }

   }

   public Item getItemDropped(int par1, Random rand, int par3) {
      return par1 == this.getNumGrowthStages()?this.cropItemPrototype.getItem():this.seedItemPrototype.getItem();
   }

   public int damageDropped(int par1) {
      return par1 == this.getNumGrowthStages()?this.cropItemPrototype.getItemDamage():this.seedItemPrototype.getItemDamage();
   }

   public int quantityDropped(Random rand) {
      return 1;
   }

   public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
      return this.seedItemPrototype;
   }

   public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z) {
      return this.waterPlant?EnumPlantType.Water:super.getPlantType(world, x, y, z);
   }
}
