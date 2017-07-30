package com.emoniph.witchery.item;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockGrassper;
import com.emoniph.witchery.blocks.BlockLeechChest;
import com.emoniph.witchery.entity.EntityMandrake;
import com.emoniph.witchery.entity.EntityOwl;
import com.emoniph.witchery.entity.EntityParasyticLouse;
import com.emoniph.witchery.entity.EntityToad;
import com.emoniph.witchery.entity.EntityWingedMonkey;
import com.emoniph.witchery.item.ItemBase;
import com.emoniph.witchery.item.ItemGeneral;
import com.emoniph.witchery.util.BlockUtil;
import com.emoniph.witchery.util.Dye;
import com.emoniph.witchery.util.Log;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.lang.reflect.Constructor;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCocoa;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class ItemMutator extends ItemBase {

   private static final int MAX_DAMAGE = 15;
   private static final int DAMAGE_PER_USE = 1;


   public ItemMutator() {
      this.setMaxStackSize(1);
      this.setMaxDamage(15);
      this.setFull3D();
   }

   @SideOnly(Side.CLIENT)
   public EnumRarity getRarity(ItemStack itemstack) {
      return EnumRarity.uncommon;
   }

   public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
      Block block = world.getBlock(x, y, z);
      Material materialAbove = world.getBlock(x, y + 1, z).getMaterial();
      if(block == Blocks.grass) {
         if(!world.isRemote) {
            world.setBlock(x, y, z, Blocks.mycelium);
            ParticleEffect.INSTANT_SPELL.send(SoundEffect.MOB_SLIME_BIG, world, 0.5D + (double)x, 1.0D + (double)y, 0.5D + (double)z, 1.0D, 1.0D, 8);
         }

         stack.damageItem(1, player);
      } else if(block == Blocks.mycelium) {
         if(!world.isRemote) {
            world.setBlock(x, y, z, Blocks.grass);
            ParticleEffect.INSTANT_SPELL.send(SoundEffect.MOB_SLIME_BIG, world, 0.5D + (double)x, 1.0D + (double)y, 0.5D + (double)z, 1.0D, 1.0D, 8);
         }

         stack.damageItem(1, player);
      } else if(block == Blocks.dirt && materialAbove == Material.water) {
         if(!world.isRemote) {
            ItemGeneral.setBlockToClay(world, x, y, z);
            ItemGeneral.setBlockToClay(world, x + 1, y, z);
            ItemGeneral.setBlockToClay(world, x - 1, y, z);
            ItemGeneral.setBlockToClay(world, x, y, z + 1);
            ItemGeneral.setBlockToClay(world, x, y, z - 1);
         }

         stack.damageItem(1, player);
      } else if(this.isMutatableTrapChest(world, x, y, z)) {
         if(!world.isRemote) {
            world.setBlockToAir(x, y, z);
            world.setBlockToAir(x + 1, y, z);
            world.setBlockToAir(x - 1, y, z);
            world.setBlockToAir(x, y, z + 1);
            world.setBlockToAir(x, y, z - 1);
            world.setBlock(x, y, z, Witchery.Blocks.LEECH_CHEST);
            ParticleEffect.SLIME.send(SoundEffect.MOB_SLIME_BIG, world, 0.5D + (double)x, (double)y, 0.5D + (double)z, 1.0D, 2.0D, 8);
         }

         stack.damageItem(1, player);
      } else {
         int monkey;
         int adjY;
         if(this.isMutatableReed(world, x, y, z)) {
            if(!world.isRemote) {
               world.setBlockToAir(x + 1, y, z);
               world.setBlockToAir(x - 1, y, z);
               world.setBlockToAir(x, y, z + 1);
               world.setBlockToAir(x, y, z - 1);
               this.clearGrassperAt(world, x + 1, y, z + 1);
               this.clearGrassperAt(world, x + 1, y, z - 1);
               this.clearGrassperAt(world, x - 1, y, z + 1);
               this.clearGrassperAt(world, x - 1, y, z - 1);
               ParticleEffect.SLIME.send(SoundEffect.MOB_SLIME_BIG, world, 0.5D + (double)x, (double)y, 0.5D + (double)z, 3.0D, 2.0D, 8);

               for(monkey = 15; monkey >= 0; --monkey) {
                  adjY = y + monkey;
                  if(world.getBlock(x, adjY, z) == Blocks.reeds) {
                     world.setBlock(x, adjY, z, Witchery.Blocks.BRAMBLE);
                     ParticleEffect.SLIME.send(SoundEffect.NONE, world, 0.5D + (double)x, (double)adjY, 0.5D + (double)z, 1.0D, 1.0D, 16);
                  }
               }
            }

            stack.damageItem(1, player);
         } else if(this.isMutatableWheat(world, x, y, z)) {
            if(!world.isRemote) {
               world.setBlockToAir(x, y, z);
               world.setBlockToAir(x + 1, y, z);
               world.setBlockToAir(x - 1, y, z);
               world.setBlockToAir(x, y, z + 1);
               world.setBlockToAir(x, y, z - 1);
               BlockUtil.setBlock(world, x, y, z, (Block)Witchery.Blocks.CROP_WORMWOOD, 0, 3);
               ParticleEffect.SLIME.send(SoundEffect.MOB_SLIME_BIG, world, 0.5D + (double)x, (double)y, 0.5D + (double)z, 3.0D, 2.0D, 8);
            }

            stack.damageItem(1, player);
         } else if(this.isMutatableCactus(world, x, y, z)) {
            if(!world.isRemote) {
               world.setBlockToAir(x + 1, y, z);
               world.setBlockToAir(x - 1, y, z);
               world.setBlockToAir(x, y, z + 1);
               world.setBlockToAir(x, y, z - 1);
               this.clearGrassperAt(world, x + 1, y, z + 1);
               this.clearGrassperAt(world, x + 1, y, z - 1);
               this.clearGrassperAt(world, x - 1, y, z + 1);
               this.clearGrassperAt(world, x - 1, y, z - 1);
               ParticleEffect.SLIME.send(SoundEffect.MOB_SLIME_BIG, world, 0.5D + (double)x, (double)y, 0.5D + (double)z, 3.0D, 2.0D, 8);

               for(monkey = 4; monkey >= 0; --monkey) {
                  adjY = y + monkey;
                  if(world.getBlock(x, adjY, z) == Blocks.cactus) {
                     world.setBlock(x, adjY, z, Witchery.Blocks.BRAMBLE, 1, 3);
                     ParticleEffect.SLIME.send(SoundEffect.NONE, world, 0.5D + (double)x, (double)adjY, 0.5D + (double)z, 1.0D, 1.0D, 16);
                  }
               }
            }

            stack.damageItem(1, player);
         } else if(this.isMutatableChest(world, x, y, z)) {
            if(!world.isRemote) {
               world.setBlockToAir(x, y, z);
               world.setBlock(x + 1, y, z, Witchery.Blocks.GRASSPER);
               world.setBlock(x - 1, y, z, Witchery.Blocks.GRASSPER);
               world.setBlock(x, y, z + 1, Witchery.Blocks.GRASSPER);
               world.setBlock(x, y, z - 1, Witchery.Blocks.GRASSPER);
               ParticleEffect.SLIME.send(SoundEffect.MOB_SLIME_BIG, world, 0.5D + (double)x, (double)y, 0.5D + (double)z, 3.0D, 2.0D, 8);
            }

            stack.damageItem(1, player);
         } else if(this.isMutatableLeechChest(world, x, y, z)) {
            if(!world.isRemote) {
               world.setBlockToAir(x, y, z);
               world.setBlock(x + 1, y, z, Witchery.Blocks.BLOOD_ROSE);
               world.setBlock(x - 1, y, z, Witchery.Blocks.BLOOD_ROSE);
               world.setBlock(x, y, z + 1, Witchery.Blocks.BLOOD_ROSE);
               world.setBlock(x, y, z - 1, Witchery.Blocks.BLOOD_ROSE);
               ParticleEffect.SLIME.send(SoundEffect.MOB_SLIME_BIG, world, 0.5D + (double)x, (double)y, 0.5D + (double)z, 1.0D, 2.0D, 8);
            }

            stack.damageItem(1, player);
         } else if(this.isMutatableWeb(world, x, y, z)) {
            if(!world.isRemote) {
               world.setBlockToAir(x, y, z);
               world.setBlock(x + 1, y, z, Witchery.Blocks.CRITTER_SNARE);
               world.setBlock(x - 1, y, z, Witchery.Blocks.CRITTER_SNARE);
               world.setBlock(x, y, z + 1, Witchery.Blocks.CRITTER_SNARE);
               world.setBlock(x, y, z - 1, Witchery.Blocks.CRITTER_SNARE);
               ParticleEffect.SLIME.send(SoundEffect.MOB_SLIME_BIG, world, 0.5D + (double)x, (double)y, 0.5D + (double)z, 3.0D, 2.0D, 8);
            }

            stack.damageItem(1, player);
         } else if(this.isMutatableToOwl(world, x, y, z)) {
            if(!world.isRemote) {
               world.setBlockToAir(x, y, z);
               this.convertToEntity(world, x + 1, y, z, Witchery.Blocks.CRITTER_SNARE, 1, EntityOwl.class);
               this.convertToEntity(world, x - 1, y, z, Witchery.Blocks.CRITTER_SNARE, 1, EntityOwl.class);
               this.convertToEntity(world, x, y, z + 1, Witchery.Blocks.CRITTER_SNARE, 1, EntityOwl.class);
               this.convertToEntity(world, x, y, z - 1, Witchery.Blocks.CRITTER_SNARE, 1, EntityOwl.class);
               this.clearGrassperAt(world, x + 1, y, z + 1);
               this.clearGrassperAt(world, x + 1, y, z - 1);
               this.clearGrassperAt(world, x - 1, y, z + 1);
               this.clearGrassperAt(world, x - 1, y, z - 1);
               ParticleEffect.SLIME.send(SoundEffect.MOB_SLIME_BIG, world, 0.5D + (double)x, (double)y, 0.5D + (double)z, 3.0D, 2.0D, 8);
            }

            stack.damageItem(1, player);
         } else if(this.isMutatableToToad(world, x, y, z)) {
            if(!world.isRemote) {
               world.setBlockToAir(x, y, z);
               this.convertToEntity(world, x + 1, y, z, Witchery.Blocks.CRITTER_SNARE, 3, EntityToad.class);
               this.convertToEntity(world, x - 1, y, z, Witchery.Blocks.CRITTER_SNARE, 3, EntityToad.class);
               this.convertToEntity(world, x, y, z + 1, Witchery.Blocks.CRITTER_SNARE, 3, EntityToad.class);
               this.convertToEntity(world, x, y, z - 1, Witchery.Blocks.CRITTER_SNARE, 3, EntityToad.class);
               this.clearGrassperAt(world, x + 1, y, z + 1);
               this.clearGrassperAt(world, x + 1, y, z - 1);
               this.clearGrassperAt(world, x - 1, y, z + 1);
               this.clearGrassperAt(world, x - 1, y, z - 1);
               ParticleEffect.SLIME.send(SoundEffect.MOB_SLIME_BIG, world, 0.5D + (double)x, (double)y, 0.5D + (double)z, 3.0D, 2.0D, 8);
            }

            stack.damageItem(1, player);
         } else if(this.isMutatableToMindrake(world, x, y, z)) {
            if(!world.isRemote) {
               world.setBlockToAir(x, y, z);
               world.setBlock(x + 1, y, z, Witchery.Blocks.CROP_MINDRAKE);
               world.setBlock(x - 1, y, z, Witchery.Blocks.CROP_MINDRAKE);
               world.setBlock(x, y, z + 1, Witchery.Blocks.CROP_MINDRAKE);
               world.setBlock(x, y, z - 1, Witchery.Blocks.CROP_MINDRAKE);
               this.clearGrassperAt(world, x + 1, y, z + 1);
               this.clearGrassperAt(world, x + 1, y, z - 1);
               this.clearGrassperAt(world, x - 1, y, z + 1);
               this.clearGrassperAt(world, x - 1, y, z - 1);
               ParticleEffect.SLIME.send(SoundEffect.MOB_SLIME_BIG, world, 0.5D + (double)x, (double)y, 0.5D + (double)z, 3.0D, 2.0D, 8);
            }

            stack.damageItem(1, player);
         } else if(this.isMutatableToMonkey(world, x, y, z)) {
            if(!world.isRemote) {
               world.setBlockToAir(x, y, z);
               world.setBlockToAir(x + 1, y, z);
               world.setBlockToAir(x - 1, y, z);
               world.setBlockToAir(x, y, z + 1);
               world.setBlockToAir(x, y, z - 1);
               this.clearGrassperAt(world, x + 1, y, z + 1);
               this.clearGrassperAt(world, x + 1, y, z - 1);
               this.clearGrassperAt(world, x - 1, y, z + 1);
               this.clearGrassperAt(world, x - 1, y, z - 1);
               EntityWingedMonkey var15 = new EntityWingedMonkey(world);
               var15.setPositionAndRotation((double)x + 0.5D, (double)y, (double)z + 0.5D, 0.0F, 0.0F);
               var15.func_110163_bv();
               world.spawnEntityInWorld(var15);
               ParticleEffect.SLIME.send(SoundEffect.MOB_SLIME_BIG, world, 0.5D + (double)x, (double)y, 0.5D + (double)z, 3.0D, 2.0D, 8);
            }

            stack.damageItem(1, player);
         } else {
            if(!this.isMutatableToLouse(world, x, y, z)) {
               return super.onItemUseFirst(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
            }

            if(!world.isRemote) {
               world.setBlockToAir(x + 1, y, z);
               world.setBlockToAir(x - 1, y, z);
               world.setBlockToAir(x, y, z + 1);
               world.setBlockToAir(x, y, z - 1);
               this.convertToEntity(world, x, y, z, Witchery.Blocks.CRITTER_SNARE, 2, EntityParasyticLouse.class);
               this.clearGrassperAt(world, x + 1, y, z + 1);
               this.clearGrassperAt(world, x + 1, y, z - 1);
               this.clearGrassperAt(world, x - 1, y, z + 1);
               this.clearGrassperAt(world, x - 1, y, z - 1);
               ParticleEffect.SLIME.send(SoundEffect.MOB_SLIME_BIG, world, 0.5D + (double)x, (double)y, 0.5D + (double)z, 3.0D, 2.0D, 8);
            }

            stack.damageItem(1, player);
         }
      }

      return !world.isRemote;
   }

   private boolean isMutatableToLouse(World world, int x, int y, int z) {
      Block block = world.getBlock(x, y, z);
      int meta = world.getBlockMetadata(x, y, z);
      if(block == Witchery.Blocks.CRITTER_SNARE && meta == 2) {
         byte vineCount = 0;
         int vineCount1 = vineCount + (world.getBlock(x + 1, y, z) == Blocks.waterlily?1:0);
         vineCount1 += world.getBlock(x - 1, y, z) == Blocks.waterlily?1:0;
         vineCount1 += world.getBlock(x, y, z + 1) == Blocks.waterlily?1:0;
         vineCount1 += world.getBlock(x, y, z - 1) == Blocks.waterlily?1:0;
         byte watercount = 0;
         int watercount1 = watercount + (world.getBlock(x + 1, y - 1, z) == Blocks.water?1:0);
         watercount1 += world.getBlock(x - 1, y - 1, z) == Blocks.water?1:0;
         watercount1 += world.getBlock(x, y - 1, z + 1) == Blocks.water?1:0;
         watercount1 += world.getBlock(x, y - 1, z - 1) == Blocks.water?1:0;
         byte grasperAttunedCount = 0;
         ItemStack pearl = Witchery.Items.GENERIC.itemAttunedStoneCharged.createStack();
         int grasperAttunedCount1 = grasperAttunedCount + (this.isGrasperWith(world, x + 1, y, z + 1, pearl)?1:0);
         grasperAttunedCount1 += this.isGrasperWith(world, x + 1, y, z - 1, pearl)?1:0;
         grasperAttunedCount1 += this.isGrasperWith(world, x - 1, y, z + 1, pearl)?1:0;
         grasperAttunedCount1 += this.isGrasperWith(world, x - 1, y, z - 1, pearl)?1:0;
         byte grasperCount = 0;
         pearl = Witchery.Items.GENERIC.itemMutandis.createStack();
         int grasperCount1 = grasperCount + (this.isGrasperWith(world, x + 1, y, z + 1, pearl)?1:0);
         grasperCount1 += this.isGrasperWith(world, x + 1, y, z - 1, pearl)?1:0;
         grasperCount1 += this.isGrasperWith(world, x - 1, y, z + 1, pearl)?1:0;
         grasperCount1 += this.isGrasperWith(world, x - 1, y, z - 1, pearl)?1:0;
         byte grasperTongueCount = 0;
         pearl = Witchery.Items.GENERIC.itemDogTongue.createStack();
         int grasperTongueCount1 = grasperTongueCount + (this.isGrasperWith(world, x + 1, y, z + 1, pearl)?1:0);
         grasperTongueCount1 += this.isGrasperWith(world, x + 1, y, z - 1, pearl)?1:0;
         grasperTongueCount1 += this.isGrasperWith(world, x - 1, y, z + 1, pearl)?1:0;
         grasperTongueCount1 += this.isGrasperWith(world, x - 1, y, z - 1, pearl)?1:0;
         return vineCount1 >= 2 && watercount1 >= 1 && grasperCount1 >= 2 && grasperAttunedCount1 >= 1 && grasperTongueCount1 >= 1;
      } else {
         return false;
      }
   }

   private boolean isMutatableToToad(World world, int x, int y, int z) {
      Block blockID = world.getBlock(x, y, z);
      if(blockID != Blocks.web) {
         return false;
      } else {
         byte vineCount = 0;
         int vineCount1 = vineCount + (world.getBlock(x + 1, y, z) == Witchery.Blocks.CRITTER_SNARE && world.getBlockMetadata(x + 1, y, z) == 3?1:0);
         vineCount1 += world.getBlock(x - 1, y, z) == Witchery.Blocks.CRITTER_SNARE && world.getBlockMetadata(x - 1, y, z) == 3?1:0;
         vineCount1 += world.getBlock(x, y, z + 1) == Witchery.Blocks.CRITTER_SNARE && world.getBlockMetadata(x, y, z + 1) == 3?1:0;
         vineCount1 += world.getBlock(x, y, z - 1) == Witchery.Blocks.CRITTER_SNARE && world.getBlockMetadata(x, y, z - 1) == 3?1:0;
         byte watercount = 0;
         int watercount1 = watercount + (world.getBlock(x, y - 1, z) == Blocks.water?1:0);
         byte grasperAttunedCount = 0;
         ItemStack pearl = Witchery.Items.GENERIC.itemAttunedStoneCharged.createStack();
         int grasperAttunedCount1 = grasperAttunedCount + (this.isGrasperWith(world, x + 1, y, z + 1, pearl)?1:0);
         grasperAttunedCount1 += this.isGrasperWith(world, x + 1, y, z - 1, pearl)?1:0;
         grasperAttunedCount1 += this.isGrasperWith(world, x - 1, y, z + 1, pearl)?1:0;
         grasperAttunedCount1 += this.isGrasperWith(world, x - 1, y, z - 1, pearl)?1:0;
         byte grasperCount = 0;
         pearl = Witchery.Items.GENERIC.itemMutandisExtremis.createStack();
         int grasperCount1 = grasperCount + (this.isGrasperWith(world, x + 1, y, z + 1, pearl)?1:0);
         grasperCount1 += this.isGrasperWith(world, x + 1, y, z - 1, pearl)?1:0;
         grasperCount1 += this.isGrasperWith(world, x - 1, y, z + 1, pearl)?1:0;
         grasperCount1 += this.isGrasperWith(world, x - 1, y, z - 1, pearl)?1:0;
         if(vineCount1 >= 2 && watercount1 >= 1 && grasperCount1 >= 3 && grasperAttunedCount1 >= 1) {
            AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox((double)(x - 1), (double)(y - 1), (double)(z - 1), (double)(x + 1), (double)(y + 2), (double)(z + 1));
            List list = world.getEntitiesWithinAABB(EntityOcelot.class, aabb);
            if(list.size() == 0) {
               return false;
            } else {
               if(!world.isRemote) {
                  EntityOcelot ocelot = (EntityOcelot)list.get(0);
                  ParticleEffect.SLIME.send(SoundEffect.MOB_OCELOT_DEATH, ocelot, 3.0D, 2.0D, 8);
                  ocelot.setDead();
               }

               return true;
            }
         } else {
            return false;
         }
      }
   }

   private boolean isMutatableToMonkey(World world, int x, int y, int z) {
      Block blockID = world.getBlock(x, y, z);
      if(blockID != Blocks.web) {
         return false;
      } else {
         byte vineCount = 0;
         int vineCount1 = vineCount + (world.getBlock(x + 1, y, z) == Blocks.cocoa && BlockCocoa.func_149987_c(world.getBlockMetadata(x + 1, y, z)) == 2?1:0);
         vineCount1 += world.getBlock(x - 1, y, z) == Blocks.cocoa && BlockCocoa.func_149987_c(world.getBlockMetadata(x - 1, y, z)) == 2?1:0;
         vineCount1 += world.getBlock(x, y, z + 1) == Blocks.cocoa && BlockCocoa.func_149987_c(world.getBlockMetadata(x, y, z + 1)) == 2?1:0;
         vineCount1 += world.getBlock(x, y, z - 1) == Blocks.cocoa && BlockCocoa.func_149987_c(world.getBlockMetadata(x, y, z - 1)) == 2?1:0;
         byte watercount = 0;
         int watercount1 = watercount + (world.getBlock(x, y - 1, z) == Blocks.water?1:0);
         byte grasperAttunedCount = 0;
         ItemStack pearl = Witchery.Items.GENERIC.itemAttunedStoneCharged.createStack();
         int grasperAttunedCount1 = grasperAttunedCount + (this.isGrasperWith(world, x + 1, y, z + 1, pearl)?1:0);
         grasperAttunedCount1 += this.isGrasperWith(world, x + 1, y, z - 1, pearl)?1:0;
         grasperAttunedCount1 += this.isGrasperWith(world, x - 1, y, z + 1, pearl)?1:0;
         grasperAttunedCount1 += this.isGrasperWith(world, x - 1, y, z - 1, pearl)?1:0;
         byte grasperCount = 0;
         pearl = Witchery.Items.GENERIC.itemMutandisExtremis.createStack();
         int grasperCount1 = grasperCount + (this.isGrasperWith(world, x + 1, y, z + 1, pearl)?1:0);
         grasperCount1 += this.isGrasperWith(world, x + 1, y, z - 1, pearl)?1:0;
         grasperCount1 += this.isGrasperWith(world, x - 1, y, z + 1, pearl)?1:0;
         grasperCount1 += this.isGrasperWith(world, x - 1, y, z - 1, pearl)?1:0;
         byte grasperTongueCount = 0;
         pearl = new ItemStack(Blocks.red_flower);
         int grasperTongueCount1 = grasperTongueCount + (this.isGrasperWith(world, x + 1, y, z + 1, pearl)?1:0);
         grasperTongueCount1 += this.isGrasperWith(world, x + 1, y, z - 1, pearl)?1:0;
         grasperTongueCount1 += this.isGrasperWith(world, x - 1, y, z + 1, pearl)?1:0;
         grasperTongueCount1 += this.isGrasperWith(world, x - 1, y, z - 1, pearl)?1:0;
         if(vineCount1 >= 4 && watercount1 >= 1 && grasperCount1 >= 2 && grasperAttunedCount1 >= 1 && grasperTongueCount1 >= 1) {
            EntityOwl owl = null;
            AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox((double)(x - 1), (double)(y - 1), (double)(z - 1), (double)(x + 1), (double)(y + 2), (double)(z + 1));
            List owlList = world.getEntitiesWithinAABB(EntityOwl.class, aabb);
            if(owlList.size() == 0) {
               return false;
            } else {
               if(!world.isRemote) {
                  owl = (EntityOwl)owlList.get(0);
               }

               EntityWolf wolf = null;
               aabb = AxisAlignedBB.getBoundingBox((double)(x - 1), (double)(y - 1), (double)(z - 1), (double)(x + 1), (double)(y + 2), (double)(z + 1));
               List wolfList = world.getEntitiesWithinAABB(EntityWolf.class, aabb);
               if(wolfList.size() == 0) {
                  return false;
               } else {
                  if(!world.isRemote) {
                     wolf = (EntityWolf)wolfList.get(0);
                  }

                  if(owl != null && wolf != null) {
                     ParticleEffect.SLIME.send(SoundEffect.MOB_CREEPER_DEATH, owl, 3.0D, 2.0D, 8);
                     owl.setDead();
                     ParticleEffect.SLIME.send(SoundEffect.MOB_GHAST_DEATH, wolf, 3.0D, 2.0D, 8);
                     wolf.setDead();
                     return true;
                  } else {
                     return false;
                  }
               }
            }
         } else {
            return false;
         }
      }
   }

   private boolean isMutatableToOwl(World world, int x, int y, int z) {
      Block blockID = world.getBlock(x, y, z);
      if(blockID != Blocks.web) {
         return false;
      } else {
         byte vineCount = 0;
         int vineCount1 = vineCount + (world.getBlock(x + 1, y, z) == Witchery.Blocks.CRITTER_SNARE && world.getBlockMetadata(x + 1, y, z) == 1?1:0);
         vineCount1 += world.getBlock(x - 1, y, z) == Witchery.Blocks.CRITTER_SNARE && world.getBlockMetadata(x - 1, y, z) == 1?1:0;
         vineCount1 += world.getBlock(x, y, z + 1) == Witchery.Blocks.CRITTER_SNARE && world.getBlockMetadata(x, y, z + 1) == 1?1:0;
         vineCount1 += world.getBlock(x, y, z - 1) == Witchery.Blocks.CRITTER_SNARE && world.getBlockMetadata(x, y, z - 1) == 1?1:0;
         byte watercount = 0;
         int watercount1 = watercount + (world.getBlock(x, y - 1, z) == Blocks.water?1:0);
         byte grasperAttunedCount = 0;
         ItemStack pearl = Witchery.Items.GENERIC.itemAttunedStoneCharged.createStack();
         int grasperAttunedCount1 = grasperAttunedCount + (this.isGrasperWith(world, x + 1, y, z + 1, pearl)?1:0);
         grasperAttunedCount1 += this.isGrasperWith(world, x + 1, y, z - 1, pearl)?1:0;
         grasperAttunedCount1 += this.isGrasperWith(world, x - 1, y, z + 1, pearl)?1:0;
         grasperAttunedCount1 += this.isGrasperWith(world, x - 1, y, z - 1, pearl)?1:0;
         byte grasperCount = 0;
         pearl = Witchery.Items.GENERIC.itemMutandisExtremis.createStack();
         int grasperCount1 = grasperCount + (this.isGrasperWith(world, x + 1, y, z + 1, pearl)?1:0);
         grasperCount1 += this.isGrasperWith(world, x + 1, y, z - 1, pearl)?1:0;
         grasperCount1 += this.isGrasperWith(world, x - 1, y, z + 1, pearl)?1:0;
         grasperCount1 += this.isGrasperWith(world, x - 1, y, z - 1, pearl)?1:0;
         if(vineCount1 >= 2 && watercount1 >= 1 && grasperCount1 >= 3 && grasperAttunedCount1 >= 1) {
            AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox((double)(x - 1), (double)(y - 1), (double)(z - 1), (double)(x + 1), (double)(y + 2), (double)(z + 1));
            List list = world.getEntitiesWithinAABB(EntityWolf.class, aabb);
            if(list.size() == 0) {
               return false;
            } else {
               if(!world.isRemote) {
                  EntityWolf wolf = (EntityWolf)list.get(0);
                  ParticleEffect.SLIME.send(SoundEffect.MOB_WOLF_DEATH, wolf, 3.0D, 2.0D, 8);
                  wolf.setDead();
               }

               return true;
            }
         } else {
            return false;
         }
      }
   }

   private void convertToEntity(World world, int x, int y, int z, Block block, int blockMeta, Class entityClass) {
      Block foundBlock = world.getBlock(x, y, z);
      int foundBlockMeta = world.getBlockMetadata(x, y, z);
      if(foundBlock == block && foundBlockMeta == blockMeta && entityClass != null) {
         world.setBlockToAir(x, y, z);

         try {
            Constructor e = entityClass.getConstructor(new Class[]{World.class});
            EntityLiving entity = (EntityLiving)e.newInstance(new Object[]{world});
            entity.setLocationAndAngles(0.5D + (double)x, 0.001D + (double)y, 0.5D + (double)z, 1.0F, 0.0F);
            world.spawnEntityInWorld(entity);
            Object entitylivingData = null;
            entity.func_110163_bv();
            entity.onSpawnWithEgg((IEntityLivingData)entitylivingData);
         } catch (Throwable var13) {
            Log.instance().warning(var13, "Error occurred while mutating a creature with a sprig");
         }
      }

   }

   private boolean isMutatableWeb(World world, int x, int y, int z) {
      Block blockID = world.getBlock(x, y, z);
      if(blockID != Blocks.web) {
         return false;
      } else {
         byte vineCount = 0;
         int vineCount1 = vineCount + (world.getBlock(x + 1, y, z) == Witchery.Blocks.SAPLING && (world.getBlockMetadata(x + 1, y, z) & 3) == 1?1:0);
         vineCount1 += world.getBlock(x - 1, y, z) == Witchery.Blocks.SAPLING && (world.getBlockMetadata(x - 1, y, z) & 3) == 1?1:0;
         vineCount1 += world.getBlock(x, y, z + 1) == Witchery.Blocks.SAPLING && (world.getBlockMetadata(x, y, z + 1) & 3) == 1?1:0;
         vineCount1 += world.getBlock(x, y, z - 1) == Witchery.Blocks.SAPLING && (world.getBlockMetadata(x, y, z - 1) & 3) == 1?1:0;
         byte watercount = 0;
         int watercount1 = watercount + (world.getBlock(x, y - 1, z) == Blocks.water?1:0);
         if(vineCount1 >= 4 && watercount1 >= 1) {
            AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox((double)(x - 1), (double)(y - 1), (double)(z - 1), (double)(x + 1), (double)(y + 2), (double)(z + 1));
            List list = world.getEntitiesWithinAABB(EntityZombie.class, aabb);
            if(list.size() == 0) {
               return false;
            } else {
               if(!world.isRemote) {
                  EntityZombie zombie = (EntityZombie)list.get(0);
                  ParticleEffect.SLIME.send(SoundEffect.MOB_ZOMBIE_DEATH, zombie, 3.0D, 2.0D, 8);
                  zombie.setDead();
               }

               return true;
            }
         } else {
            return false;
         }
      }
   }

   private boolean isMutatableToMindrake(World world, int x, int y, int z) {
      Block blockID = world.getBlock(x, y, z);
      if(blockID != Blocks.web) {
         return false;
      } else {
         byte vineCount = 0;
         int vineCount1 = vineCount + (world.getBlock(x + 1, y, z) == Witchery.Blocks.CROP_MANDRAKE && world.getBlockMetadata(x + 1, y, z) == Witchery.Blocks.CROP_MANDRAKE.getNumGrowthStages()?1:0);
         vineCount1 += world.getBlock(x - 1, y, z) == Witchery.Blocks.CROP_MANDRAKE && world.getBlockMetadata(x - 1, y, z) == Witchery.Blocks.CROP_MANDRAKE.getNumGrowthStages()?1:0;
         vineCount1 += world.getBlock(x, y, z + 1) == Witchery.Blocks.CROP_MANDRAKE && world.getBlockMetadata(x, y, z + 1) == Witchery.Blocks.CROP_MANDRAKE.getNumGrowthStages()?1:0;
         vineCount1 += world.getBlock(x, y, z - 1) == Witchery.Blocks.CROP_MANDRAKE && world.getBlockMetadata(x, y, z - 1) == Witchery.Blocks.CROP_MANDRAKE.getNumGrowthStages()?1:0;
         byte watercount = 0;
         int watercount1 = watercount + (world.getBlock(x, y - 1, z) == Blocks.water?1:0);
         byte grasperAttunedCount = 0;
         ItemStack pearl = Witchery.Items.GENERIC.itemAttunedStoneCharged.createStack();
         int grasperAttunedCount1 = grasperAttunedCount + (this.isGrasperWith(world, x + 1, y, z + 1, pearl)?1:0);
         grasperAttunedCount1 += this.isGrasperWith(world, x + 1, y, z - 1, pearl)?1:0;
         grasperAttunedCount1 += this.isGrasperWith(world, x - 1, y, z + 1, pearl)?1:0;
         grasperAttunedCount1 += this.isGrasperWith(world, x - 1, y, z - 1, pearl)?1:0;
         byte grasperCount = 0;
         pearl = Witchery.Items.GENERIC.itemMutandisExtremis.createStack();
         int grasperCount1 = grasperCount + (this.isGrasperWith(world, x + 1, y, z + 1, pearl)?1:0);
         grasperCount1 += this.isGrasperWith(world, x + 1, y, z - 1, pearl)?1:0;
         grasperCount1 += this.isGrasperWith(world, x - 1, y, z + 1, pearl)?1:0;
         grasperCount1 += this.isGrasperWith(world, x - 1, y, z - 1, pearl)?1:0;
         byte grasperTongueCount = 0;
         pearl = Witchery.Items.GENERIC.itemFocusedWill.createStack();
         int grasperTongueCount1 = grasperTongueCount + (this.isGrasperWith(world, x + 1, y, z + 1, pearl)?1:0);
         grasperTongueCount1 += this.isGrasperWith(world, x + 1, y, z - 1, pearl)?1:0;
         grasperTongueCount1 += this.isGrasperWith(world, x - 1, y, z + 1, pearl)?1:0;
         grasperTongueCount1 += this.isGrasperWith(world, x - 1, y, z - 1, pearl)?1:0;
         if(vineCount1 >= 4 && watercount1 >= 1 && grasperCount1 >= 2 && grasperAttunedCount1 >= 1 && grasperTongueCount1 >= 1) {
            EntityCreeper creeper = null;
            AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox((double)(x - 1), (double)(y - 1), (double)(z - 1), (double)(x + 1), (double)(y + 2), (double)(z + 1));
            List list = world.getEntitiesWithinAABB(EntityCreeper.class, aabb);
            if(list.size() == 0) {
               return false;
            } else {
               if(!world.isRemote) {
                  creeper = (EntityCreeper)list.get(0);
               }

               EntityMandrake mandrake = null;
               aabb = AxisAlignedBB.getBoundingBox((double)(x - 1), (double)(y - 1), (double)(z - 1), (double)(x + 1), (double)(y + 2), (double)(z + 1));
               list = world.getEntitiesWithinAABB(EntityMandrake.class, aabb);
               if(list.size() == 0) {
                  return false;
               } else {
                  if(!world.isRemote) {
                     mandrake = (EntityMandrake)list.get(0);
                  }

                  if(creeper != null && mandrake != null) {
                     ParticleEffect.SLIME.send(SoundEffect.MOB_CREEPER_DEATH, creeper, 3.0D, 2.0D, 8);
                     creeper.setDead();
                     ParticleEffect.SLIME.send(SoundEffect.MOB_GHAST_DEATH, mandrake, 3.0D, 2.0D, 8);
                     mandrake.setDead();
                     return true;
                  } else {
                     return false;
                  }
               }
            }
         } else {
            return false;
         }
      }
   }

   private boolean isMutatableTrapChest(World world, int x, int y, int z) {
      Block blockID = world.getBlock(x, y, z);
      if(blockID != Blocks.trapped_chest) {
         return false;
      } else {
         byte vineCount = 0;
         int var11 = vineCount + (world.getBlock(x + 1, y, z) == Blocks.vine?1:0);
         var11 += world.getBlock(x - 1, y, z) == Blocks.vine?1:0;
         var11 += world.getBlock(x, y, z + 1) == Blocks.vine?1:0;
         var11 += world.getBlock(x, y, z - 1) == Blocks.vine?1:0;
         byte watercount = 0;
         int var12 = watercount + (world.getBlock(x + 1, y - 1, z) == Blocks.water?1:0);
         var12 += world.getBlock(x - 1, y - 1, z) == Blocks.water?1:0;
         var12 += world.getBlock(x, y - 1, z + 1) == Blocks.water?1:0;
         var12 += world.getBlock(x, y - 1, z - 1) == Blocks.water?1:0;
         if(var11 >= 4 && var12 >= 4) {
            TileEntity tile = world.getTileEntity(x, y, z);
            if(tile != null && tile instanceof TileEntityChest) {
               TileEntityChest chest = (TileEntityChest)tile;

               for(int i = 0; i < chest.getSizeInventory(); ++i) {
                  if(chest.getStackInSlot(i) != null) {
                     return false;
                  }
               }

               return true;
            } else {
               return false;
            }
         } else {
            return false;
         }
      }
   }

   private boolean isMutatableWheat(World world, int x, int y, int z) {
      Block block = BlockUtil.getBlock(world, x, y, z);
      if(block == Blocks.wheat && BlockUtil.getBlockMetadata(world, x, y, z) == 7) {
         byte vineCount = 0;
         int vineCount1 = vineCount + (BlockUtil.getBlock(world, x + 1, y, z) == Witchery.Blocks.WISPY_COTTON?1:0);
         vineCount1 += BlockUtil.getBlock(world, x - 1, y, z) == Witchery.Blocks.WISPY_COTTON?1:0;
         vineCount1 += BlockUtil.getBlock(world, x, y, z + 1) == Witchery.Blocks.WISPY_COTTON?1:0;
         vineCount1 += BlockUtil.getBlock(world, x, y, z - 1) == Witchery.Blocks.WISPY_COTTON?1:0;
         byte watercount = 0;
         int watercount1 = watercount + (BlockUtil.getBlock(world, x + 1, y - 1, z + 1) == Blocks.water?1:0);
         watercount1 += BlockUtil.getBlock(world, x + 1, y - 1, z - 1) == Blocks.water?1:0;
         watercount1 += BlockUtil.getBlock(world, x - 1, y - 1, z + 1) == Blocks.water?1:0;
         watercount1 += BlockUtil.getBlock(world, x - 1, y - 1, z - 1) == Blocks.water?1:0;
         return vineCount1 >= 4 && watercount1 >= 4;
      } else {
         return false;
      }
   }

   private boolean isMutatableLeechChest(World world, int x, int y, int z) {
      Block blockID = world.getBlock(x, y, z);
      if(blockID != Witchery.Blocks.LEECH_CHEST) {
         return false;
      } else {
         byte vineCount = 0;
         int var11 = vineCount + (world.getBlock(x + 1, y, z) == Blocks.red_flower?1:0);
         var11 += world.getBlock(x - 1, y, z) == Blocks.red_flower?1:0;
         var11 += world.getBlock(x, y, z + 1) == Blocks.red_flower?1:0;
         var11 += world.getBlock(x, y, z - 1) == Blocks.red_flower?1:0;
         byte watercount = 0;
         int var12 = watercount + (world.getBlock(x, y - 1, z) == Blocks.water?1:0);
         if(var11 >= 4 && var12 >= 1) {
            TileEntity tile = world.getTileEntity(x, y, z);
            if(tile != null && tile instanceof BlockLeechChest.TileEntityLeechChest) {
               BlockLeechChest.TileEntityLeechChest chest = (BlockLeechChest.TileEntityLeechChest)tile;

               for(int i = 0; i < chest.getSizeInventory(); ++i) {
                  if(chest.getStackInSlot(i) != null) {
                     return false;
                  }
               }

               return true;
            } else {
               return false;
            }
         } else {
            return false;
         }
      }
   }

   private boolean isMutatableChest(World world, int x, int y, int z) {
      Block blockID = world.getBlock(x, y, z);
      if(blockID != Blocks.chest) {
         return false;
      } else {
         byte vineCount = 0;
         int var11 = vineCount + (world.getBlock(x + 1, y, z) == Blocks.tallgrass?1:0);
         var11 += world.getBlock(x - 1, y, z) == Blocks.tallgrass?1:0;
         var11 += world.getBlock(x, y, z + 1) == Blocks.tallgrass?1:0;
         var11 += world.getBlock(x, y, z - 1) == Blocks.tallgrass?1:0;
         byte watercount = 0;
         int var12 = watercount + (world.getBlock(x, y - 1, z) == Blocks.water?1:0);
         if(var11 >= 4 && var12 >= 1) {
            TileEntity tile = world.getTileEntity(x, y, z);
            if(tile != null && tile instanceof TileEntityChest) {
               TileEntityChest chest = (TileEntityChest)tile;

               for(int i = 0; i < chest.getSizeInventory(); ++i) {
                  if(chest.getStackInSlot(i) != null) {
                     return false;
                  }
               }

               return true;
            } else {
               return false;
            }
         } else {
            return false;
         }
      }
   }

   private boolean isMutatableReed(World world, int x, int y, int z) {
      Block blockID = world.getBlock(x, y, z);
      if(blockID != Blocks.reeds) {
         return false;
      } else {
         byte vineCount = 0;
         int vineCount1 = vineCount + (world.getBlock(x + 1, y, z) == Witchery.Blocks.SPANISH_MOSS?1:0);
         vineCount1 += world.getBlock(x - 1, y, z) == Witchery.Blocks.SPANISH_MOSS?1:0;
         vineCount1 += world.getBlock(x, y, z + 1) == Witchery.Blocks.SPANISH_MOSS?1:0;
         vineCount1 += world.getBlock(x, y, z - 1) == Witchery.Blocks.SPANISH_MOSS?1:0;
         byte watercount = 0;
         int watercount1 = watercount + (world.getBlock(x + 1, y - 1, z) == Blocks.water?1:0);
         watercount1 += world.getBlock(x - 1, y - 1, z) == Blocks.water?1:0;
         watercount1 += world.getBlock(x, y - 1, z + 1) == Blocks.water?1:0;
         watercount1 += world.getBlock(x, y - 1, z - 1) == Blocks.water?1:0;
         byte grasperCount = 0;
         ItemStack pearl = new ItemStack(Items.ender_pearl);
         int grasperCount1 = grasperCount + (this.isGrasperWith(world, x + 1, y, z + 1, pearl)?1:0);
         grasperCount1 += this.isGrasperWith(world, x + 1, y, z - 1, pearl)?1:0;
         grasperCount1 += this.isGrasperWith(world, x - 1, y, z + 1, pearl)?1:0;
         grasperCount1 += this.isGrasperWith(world, x - 1, y, z - 1, pearl)?1:0;
         return vineCount1 >= 4 && watercount1 >= 4 && grasperCount1 >= 4;
      }
   }

   private boolean isMutatableCactus(World world, int x, int y, int z) {
      Block blockID = world.getBlock(x, y, z);
      if(blockID != Blocks.cactus) {
         return false;
      } else {
         byte vineCount = 0;
         int vineCount1 = vineCount + (world.getBlock(x + 1, y, z) == Witchery.Blocks.SPANISH_MOSS?1:0);
         vineCount1 += world.getBlock(x - 1, y, z) == Witchery.Blocks.SPANISH_MOSS?1:0;
         vineCount1 += world.getBlock(x, y, z + 1) == Witchery.Blocks.SPANISH_MOSS?1:0;
         vineCount1 += world.getBlock(x, y, z - 1) == Witchery.Blocks.SPANISH_MOSS?1:0;
         byte watercount = 0;
         int watercount1 = watercount + (world.getBlock(x + 1, y - 1, z) == Blocks.water?1:0);
         watercount1 += world.getBlock(x - 1, y - 1, z) == Blocks.water?1:0;
         watercount1 += world.getBlock(x, y - 1, z + 1) == Blocks.water?1:0;
         watercount1 += world.getBlock(x, y - 1, z - 1) == Blocks.water?1:0;
         byte boneMeal = 0;
         ItemStack bone = Dye.BONE_MEAL.createStack();
         int boneMeal1 = boneMeal + (this.isGrasperWith(world, x + 1, y, z + 1, bone)?1:0);
         boneMeal1 += this.isGrasperWith(world, x + 1, y, z - 1, bone)?1:0;
         boneMeal1 += this.isGrasperWith(world, x - 1, y, z + 1, bone)?1:0;
         boneMeal1 += this.isGrasperWith(world, x - 1, y, z - 1, bone)?1:0;
         byte blazePowder = 0;
         ItemStack blaze = new ItemStack(Items.blaze_powder);
         int blazePowder1 = blazePowder + (this.isGrasperWith(world, x + 1, y, z + 1, blaze)?1:0);
         blazePowder1 += this.isGrasperWith(world, x + 1, y, z - 1, blaze)?1:0;
         blazePowder1 += this.isGrasperWith(world, x - 1, y, z + 1, blaze)?1:0;
         blazePowder1 += this.isGrasperWith(world, x - 1, y, z - 1, blaze)?1:0;
         return vineCount1 >= 4 && watercount1 >= 4 && boneMeal1 >= 2 && blazePowder1 >= 2;
      }
   }

   private boolean isGrasperWith(World world, int x, int y, int z, ItemStack stack) {
      Block blockID = world.getBlock(x, y, z);
      if(blockID == Witchery.Blocks.GRASSPER) {
         TileEntity tile = world.getTileEntity(x, y, z);
         if(tile != null && tile instanceof BlockGrassper.TileEntityGrassper) {
            BlockGrassper.TileEntityGrassper grassperTile = (BlockGrassper.TileEntityGrassper)tile;
            ItemStack foundStack = grassperTile.getStackInSlot(0);
            return foundStack != null && foundStack.isItemEqual(stack);
         }
      }

      return false;
   }

   private void clearGrassperAt(World world, int x, int y, int z) {
      Block blockID = world.getBlock(x, y, z);
      if(blockID == Witchery.Blocks.GRASSPER) {
         TileEntity tile = world.getTileEntity(x, y, z);
         if(tile != null && tile instanceof BlockGrassper.TileEntityGrassper) {
            BlockGrassper.TileEntityGrassper grassperTile = (BlockGrassper.TileEntityGrassper)tile;
            grassperTile.setInventorySlotContents(0, (ItemStack)null);
            ParticleEffect.SLIME.send(SoundEffect.NONE, world, 0.5D + (double)x, (double)y, 0.5D + (double)z, 1.0D, 2.0D, 8);
         }
      }

   }
}
