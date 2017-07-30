package com.emoniph.witchery.blocks;

import com.emoniph.witchery.blocks.BlockBase;
import com.emoniph.witchery.item.ItemGeneral;
import com.emoniph.witchery.util.MultiItemBlock;
import com.emoniph.witchery.util.ParticleEffect;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockBramble extends BlockBase {

   private static final String[] BRAMBLE_TYPES = new String[]{"ender", "wild"};
   @SideOnly(Side.CLIENT)
   private IIcon[] iconArray;


   public BlockBramble() {
      super(Material.plants, BlockBramble.ClassItemBlock.class);
      this.setHardness(20.0F);
      this.setResistance(1000.0F);
      float f = 0.45F;
      this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 1.0F, 0.5F + f);
   }

   public int getRenderType() {
      return 6;
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public boolean renderAsNormalBlock() {
      return false;
   }

   public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
      return null;
   }

   public void onEntityCollidedWithBlock(World world, int posX, int posY, int posZ, Entity entity) {
      int meta = world.getBlockMetadata(posX, posY, posZ);
      switch(meta) {
      case 0:
         if(entity instanceof EntityLivingBase) {
            EntityLivingBase living = (EntityLivingBase)entity;
            this.teleportAway(world, posX, posY, posZ, living);
         }
         break;
      case 1:
         entity.attackEntityFrom(DamageSource.cactus, 1.0F);
      }

   }

   public void teleportAway(World world, int posX, int posY, int posZ, EntityLivingBase entity) {
      if(!world.isRemote) {
         boolean distance = true;
         boolean doubleDistance = true;
         posX += world.rand.nextInt(1000) - 500;
         posZ += world.rand.nextInt(1000) - 500;

         int maxY;
         for(maxY = Math.min(posY + 64, 250); !world.getBlock(posX, posY, posZ).getMaterial().isSolid() && posY >= 0; --posY) {
            ;
         }

         while((!world.getBlock(posX, posY, posZ).getMaterial().isSolid() || world.getBlock(posX, posY, posZ) == Blocks.bedrock || !world.isAirBlock(posX, posY + 1, posZ) || !world.isAirBlock(posX, posY + 2, posZ) || !world.isAirBlock(posX, posY + 3, posZ)) && posY < maxY) {
            ++posY;
         }

         if(posY > 0 && posY < maxY) {
            ItemGeneral.teleportToLocation(world, 0.5D + (double)posX, 1.0D + (double)posY, 0.5D + (double)posZ, world.provider.dimensionId, entity, true);
         }
      }

   }

   @SideOnly(Side.CLIENT)
   public IIcon getIcon(int par1, int par2) {
      if(par2 < 0 || par2 >= this.iconArray.length) {
         par2 = 0;
      }

      return this.iconArray[par2];
   }

   public int damageDropped(int par1) {
      return par1;
   }

   @SideOnly(Side.CLIENT)
   public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
      for(int j = 0; j < BRAMBLE_TYPES.length; ++j) {
         par3List.add(new ItemStack(par1, 1, j));
      }

   }

   @SideOnly(Side.CLIENT)
   public void registerBlockIcons(IIconRegister par1IconRegister) {
      this.iconArray = new IIcon[BRAMBLE_TYPES.length];

      for(int i = 0; i < this.iconArray.length; ++i) {
         this.iconArray[i] = par1IconRegister.registerIcon(this.getTextureName() + "_" + BRAMBLE_TYPES[i]);
      }

   }

   public void harvestBlock(World world, EntityPlayer player, int x, int y, int z, int meta) {
      super.harvestBlock(world, player, x, y, z, meta);
      if(!world.isRemote && meta == 1 && (player.getHeldItem() == null || player.getHeldItem().getItem() != Items.golden_axe)) {
         spreadToIfEmpty(world, x + 1, y, z, this, meta);
         spreadToIfEmpty(world, x, y, z + 1, this, meta);
         spreadToIfEmpty(world, x - 1, y, z, this, meta);
         spreadToIfEmpty(world, x, y, z - 1, this, meta);
         spreadToIfEmpty(world, x + 1, y, z + 1, this, meta);
         spreadToIfEmpty(world, x - 1, y, z - 1, this, meta);
         spreadToIfEmpty(world, x - 1, y, z + 1, this, meta);
         spreadToIfEmpty(world, x + 1, y, z - 1, this, meta);
      }

   }

   private static void spreadToIfEmpty(World world, int x, int y0, int z, Block newBlock, int newBlockMeta) {
      if(!world.isRemote) {
         for(int y = y0 - 1; y <= y0 + 1; ++y) {
            Block block = world.getBlock(x, y, z);
            if(block == Blocks.snow || block == Blocks.tallgrass || block == Blocks.air) {
               Block belowBlock = world.getBlock(x, y - 1, z);
               if(belowBlock != Blocks.air && world.rand.nextInt(2) == 0) {
                  world.setBlock(x, y, z, newBlock, newBlockMeta, 3);
                  if(world.rand.nextInt(3) != 0) {
                     break;
                  }
               }
            }
         }
      }

   }

   @SideOnly(Side.CLIENT)
   public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
      int meta = world.getBlockMetadata(x, y, z);
      if(meta == 0 && rand.nextInt(2) == 0) {
         double d0 = (double)((float)x + rand.nextFloat());
         double d1 = (double)((float)y + 0.15F + rand.nextFloat() * 0.3F) + 0.5D;
         double d2 = (double)((float)z + rand.nextFloat());
         world.spawnParticle(ParticleEffect.PORTAL.toString(), d0, d1, d2, 0.0D, -1.2D, 0.0D);
      }

   }

   public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4) {
      return !par1World.isAirBlock(par2, par3 - 1, par4);
   }

   public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, Block par5) {
      this.checkBlockCoordValid(par1World, par2, par3, par4);
   }

   protected final void checkBlockCoordValid(World par1World, int par2, int par3, int par4) {
      if(!this.canBlockStay(par1World, par2, par3, par4)) {
         this.dropBlockAsItem(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), 0);
         par1World.setBlockToAir(par2, par3, par4);
      }

   }

   public boolean canBlockStay(World par1World, int par2, int par3, int par4) {
      return this.canPlaceBlockAt(par1World, par2, par3, par4);
   }


   public static class ClassItemBlock extends MultiItemBlock {

      public ClassItemBlock(Block block) {
         super(block);
      }

      protected String[] getNames() {
         return BlockBramble.BRAMBLE_TYPES;
      }

      @SideOnly(Side.CLIENT)
      public IIcon getIconFromDamage(int meta) {
         return super.field_150939_a.getIcon(0, meta);
      }
   }
}
