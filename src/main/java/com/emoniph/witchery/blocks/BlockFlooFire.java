package com.emoniph.witchery.blocks;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.WitcheryCreativeTab;
import com.emoniph.witchery.item.ItemGeneral;
import com.emoniph.witchery.util.BlockUtil;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFire;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockFlooFire extends BlockFire {

   public BlockFlooFire() {
      super();
      this.setTickRandomly(true);
      this.setLightLevel(1.0F);
      this.setHardness(0.0F);
      this.setStepSound(Block.soundTypeCloth);
      this.setCreativeTab(WitcheryCreativeTab.INSTANCE);
   }

   public Block setBlockName(String blockName) {
      BlockUtil.registerBlock(this, blockName);
      return super.setBlockName(blockName);
   }

   public boolean canPlaceBlockAt(World world, int x, int y, int z) {
      return World.doesBlockHaveSolidTopSurface(world, x, y - 1, z) || world.getBlock(x, y - 1, z) == Blocks.netherrack;
   }

   public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
      if(!World.doesBlockHaveSolidTopSurface(world, x, y - 1, z) && world.getBlock(x, y - 1, z) != Blocks.netherrack) {
         world.setBlockToAir(x, y, z);
      }

   }

   public void updateTick(World world, int x, int y, int z, Random rand) {
      // The verdant Floo flame does NOT spread like normal fire; it simply dies out over time.
      if(!world.isRemote) {
         if(!World.doesBlockHaveSolidTopSurface(world, x, y - 1, z) && world.getBlock(x, y - 1, z) != Blocks.netherrack) {
            world.setBlockToAir(x, y, z);
         } else if(rand.nextInt(4) == 0) {
            world.setBlockToAir(x, y, z);
         } else {
            world.scheduleBlockUpdate(x, y, z, this, this.tickRate(world));
         }
      }

   }

   public int tickRate(World world) {
      return 40;
   }

   public void onBlockAdded(World world, int x, int y, int z) {
      world.scheduleBlockUpdate(x, y, z, this, this.tickRate(world));
   }

   public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
      if(!world.isRemote && entity instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer)entity;
         ItemStack held = player.getHeldItem();
         if(held != null && (Witchery.Items.GENERIC.itemWaystone.isMatch(held) || Witchery.Items.GENERIC.itemWaystoneBound.isMatch(held)) && ItemGeneral.isWaystoneBound(held)) {
            if(Witchery.Items.GENERIC.teleportToLocation(world, held, player, 0, true)) {
               // The Waystone is the destination key, never consumed.
               world.setBlockToAir(x, y, z);
               ParticleEffect.PORTAL.send(SoundEffect.MOB_ENDERMEN_PORTAL, player, 0.5D, 1.0D, 32);
            }
         }
      }

   }

   public int colorMultiplier(IBlockAccess world, int x, int y, int z) {
      return 5308749;
   }

   @SideOnly(Side.CLIENT)
   public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
      if(rand.nextInt(24) == 0) {
         world.playSound((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), "fire.fire", 1.0F + rand.nextFloat(), rand.nextFloat() * 0.7F + 0.3F, false);
      }

      for(int i = 0; i < 4; ++i) {
         double d0 = (double)((float)x + rand.nextFloat());
         double d1 = (double)((float)y + rand.nextFloat() * 0.5F + 0.2F);
         double d2 = (double)((float)z + rand.nextFloat());
         world.spawnParticle("happyVillager", d0, d1, d2, 0.0D, 0.0D, 0.0D);
      }

   }
}
