package com.emoniph.witchery.blocks;

import com.emoniph.witchery.blocks.BlockBaseBush;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;

public class BlockEmberMoss extends BlockBaseBush implements IShearable {

   public BlockEmberMoss() {
      super(Material.plants);
      this.setTickRandomly(true);
      this.setHardness(0.0F);
      this.setLightLevel(0.4F);
      this.setStepSound(Block.soundTypeGrass);
      float f = 0.4F;
      this.setBlockBounds(0.099999994F, 0.0F, 0.099999994F, 0.9F, 0.4F, 0.9F);
   }

   public void onEntityCollidedWithBlock(World world, int posX, int posY, int posZ, Entity entity) {
      if(!world.isRemote && entity instanceof EntityLivingBase && !entity.isBurning() && !entity.isImmuneToFire() && (!(entity instanceof EntityPlayer) || !((EntityPlayer)entity).capabilities.isCreativeMode)) {
         entity.setFire(3);
         ParticleEffect.FLAME.send(SoundEffect.MOB_GHAST_FIREBALL, world, 0.5D + (double)posX, 0.05D + (double)posY, 0.5D + (double)posZ, 0.5D, 1.0D, 16);
      }

   }

   public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random) {
      if(!par1World.isRemote && par1World.rand.nextInt(6) == 0) {
         byte b0 = 4;
         int l = 5;

         int i1;
         int j1;
         int k1;
         for(i1 = par2 - b0; i1 <= par2 + b0; ++i1) {
            for(j1 = par4 - b0; j1 <= par4 + b0; ++j1) {
               for(k1 = par3 - 1; k1 <= par3 + 1; ++k1) {
                  if(par1World.getBlock(i1, k1, j1) == this) {
                     --l;
                     if(l <= 0) {
                        return;
                     }
                  }
               }
            }
         }

         i1 = par2 + par5Random.nextInt(3) - 1;
         j1 = par3 + par5Random.nextInt(2) - par5Random.nextInt(2);
         k1 = par4 + par5Random.nextInt(3) - 1;

         for(int l1 = 0; l1 < 4; ++l1) {
            if(par1World.isAirBlock(i1, j1, k1) && this.canBlockSpread(par1World, i1, j1, k1)) {
               par2 = i1;
               par3 = j1;
               par4 = k1;
            }

            i1 = par2 + par5Random.nextInt(3) - 1;
            j1 = par3 + par5Random.nextInt(2) - par5Random.nextInt(2);
            k1 = par4 + par5Random.nextInt(3) - 1;
         }

         if(par1World.isAirBlock(i1, j1, k1) && this.canBlockSpread(par1World, i1, j1, k1)) {
            par1World.setBlock(i1, j1, k1, this, 0, 2);
         }
      }

   }

   @SideOnly(Side.CLIENT)
   public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
      if(rand.nextInt(100) == 0) {
         double d0 = (double)((float)x + 0.2F + rand.nextFloat() * 0.8F);
         double d1 = (double)((float)y + 0.15F + rand.nextFloat() * 0.3F);
         double d2 = (double)((float)z + 0.2F + rand.nextFloat() * 0.8F);
         world.spawnParticle(ParticleEffect.FLAME.toString(), d0, d1, d2, 0.0D, 0.0D, 0.0D);
      }

   }

   public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4) {
      return super.canPlaceBlockAt(par1World, par2, par3, par4) && this.canBlockStay(par1World, par2, par3, par4);
   }

   protected boolean canPlaceBlockOn(Block block) {
      return block != null && block.isOpaqueCube();
   }

   public boolean canBlockSpread(World world, int posX, int posY, int posZ) {
      Block block = world.getBlock(posX, posY - 1, posZ);
      return this.canBlockStay(world, posX, posY, posZ) && (block == Blocks.dirt || block == Blocks.grass || block == Blocks.mycelium || block == Blocks.sand || block == Blocks.farmland);
   }

   public boolean canBlockStay(World world, int posX, int posY, int posZ) {
      Material material = world.getBlock(posX, posY - 1, posZ).getMaterial();
      return material != null && material.isSolid();
   }

   public Item getItemDropped(int par1, Random rand, int fortune) {
      return null;
   }

   public int quantityDropped(Random par1Random) {
      return 0;
   }

   public void harvestBlock(World par3World, EntityPlayer player, int par4, int par5, int par6, int damageValue) {
      super.harvestBlock(par3World, player, par4, par5, par6, damageValue);
   }

   public boolean isShearable(ItemStack item, IBlockAccess world, int x, int y, int z) {
      return true;
   }

   public ArrayList onSheared(ItemStack item, IBlockAccess world, int x, int y, int z, int fortune) {
      ArrayList ret = new ArrayList();
      ret.add(new ItemStack(this, 1, 0));
      return ret;
   }
}
