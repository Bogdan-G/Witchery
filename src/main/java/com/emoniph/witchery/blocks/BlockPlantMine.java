package com.emoniph.witchery.blocks;

import com.emoniph.witchery.blocks.BlockBase;
import com.emoniph.witchery.entity.EntityWitchProjectile;
import com.emoniph.witchery.util.MultiItemBlock;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockPlantMine extends BlockBase implements IPlantable {

   private static final String[] woodType = new String[]{"rose_webs", "rose_ink", "rose_thorns", "rose_sprouting", "dandelion_webs", "dandelion_ink", "dandelion_thorns", "dandelion_sprouting", "grass_webs", "grass_ink", "grass_thorns", "grass_sprouting"};
   private final float RADIUS = 0.2F;
   public static final int MINE_ROSE_WEBS = 0;
   public static final int MINE_ROSE_INK = 1;
   public static final int MINE_ROSE_THORNS = 2;
   public static final int MINE_ROSE_SPROUTING = 3;
   public static final int MINE_DANDELION_WEBS = 4;
   public static final int MINE_DANDELION_INK = 5;
   public static final int MINE_DANDELION_THORNS = 6;
   public static final int MINE_DANDELION_SPROUTING = 7;
   public static final int MINE_SHRUB_WEBS = 8;
   public static final int MINE_SHRUB_INK = 9;
   public static final int MINE_SHRUB_THORNS = 10;
   public static final int MINE_SHRUB_SPROUTING = 11;


   public BlockPlantMine() {
      super(Material.plants, BlockPlantMine.ClassItemBlock.class);
      this.setTickRandomly(true);
      this.setHardness(6.0F);
      this.setResistance(1000.0F);
      this.setStepSound(Block.soundTypeGrass);
      this.setBlockBounds(0.3F, 0.0F, 0.3F, 0.7F, 0.6F, 0.7F);
   }

   public void onEntityCollidedWithBlock(World world, int posX, int posY, int posZ, Entity entity) {
      if(!world.isRemote) {
         int metadata = world.getBlockMetadata(posX, posY, posZ);
         int effect = metadata & 3;
         world.setBlockToAir(posX, posY, posZ);
         ParticleEffect.MAGIC_CRIT.send(SoundEffect.RANDOM_EXPLODE, world, 0.5D + (double)posX, 0.05D + (double)posY, 0.5D + (double)posZ, 0.5D, 1.0D, 16);
         switch(effect) {
         case 0:
            EntityWitchProjectile.explodeWeb(world, posX, posY, posZ, 1, false);
            break;
         case 1:
            EntityWitchProjectile.explodeInk(world, (double)posX, (double)posY, (double)posZ, entity, AxisAlignedBB.getBoundingBox((double)posX, (double)posY, (double)posZ, (double)(posX + 1), (double)(posY + 1), (double)(posZ + 1)), false);
            break;
         case 2:
            EntityWitchProjectile.plantCactus(world, posX, posY, posZ, 4);
            break;
         case 3:
            EntityWitchProjectile.growBranch(posX, posY, posZ, world, 1, 10, AxisAlignedBB.getBoundingBox((double)posX, (double)posY, (double)posZ, (double)(posX + 1), (double)(posY + 1), (double)(posZ + 1)));
         }
      }

   }

   public Item getItemDropped(int par1, Random rand, int fortune) {
      return null;
   }

   public int damageDropped(int metadata) {
      return 0;
   }

   public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
      return new ItemStack(this, 1, world.getBlockMetadata(x, y, z));
   }

   @SideOnly(Side.CLIENT)
   public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list) {
      list.add(new ItemStack(item, 1, 0));
      list.add(new ItemStack(item, 1, 1));
      list.add(new ItemStack(item, 1, 2));
      list.add(new ItemStack(item, 1, 3));
      list.add(new ItemStack(item, 1, 4));
      list.add(new ItemStack(item, 1, 5));
      list.add(new ItemStack(item, 1, 6));
      list.add(new ItemStack(item, 1, 7));
      list.add(new ItemStack(item, 1, 8));
      list.add(new ItemStack(item, 1, 9));
      list.add(new ItemStack(item, 1, 10));
      list.add(new ItemStack(item, 1, 11));
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIcon(int par1, int metadata) {
      metadata = metadata >>> 2 & 3;
      if(metadata < 0 || metadata >= 4) {
         metadata = 0;
      }

      switch(metadata) {
      case 0:
      default:
         return Blocks.red_flower.getIcon(0, 0);
      case 1:
         return Blocks.yellow_flower.getIcon(0, 0);
      case 2:
         return Blocks.deadbush.getIcon(0, 0);
      }
   }

   public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4) {
      return super.canPlaceBlockAt(par1World, par2, par3, par4) && this.canBlockStay(par1World, par2, par3, par4);
   }

   protected boolean canPlaceBlockOn(Block block) {
      return block == Blocks.grass || block == Blocks.dirt || block == Blocks.farmland || block == Blocks.sand || block == Blocks.mycelium;
   }

   public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, Block par5) {
      super.onNeighborBlockChange(par1World, par2, par3, par4, par5);
      this.checkFlowerChange(par1World, par2, par3, par4);
   }

   public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random) {
      this.checkFlowerChange(par1World, par2, par3, par4);
   }

   protected final void checkFlowerChange(World par1World, int par2, int par3, int par4) {
      if(!this.canBlockStay(par1World, par2, par3, par4)) {
         this.dropBlockAsItem(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), 0);
         par1World.setBlock(par2, par3, par4, Blocks.air, 0, 2);
      }

   }

   public boolean canBlockStay(World par1World, int par2, int par3, int par4) {
      Block soil = par1World.getBlock(par2, par3 - 1, par4);
      return (par1World.getFullBlockLightValue(par2, par3, par4) >= 8 || par1World.canBlockSeeTheSky(par2, par3, par4)) && soil != null && (soil.canSustainPlant(par1World, par2, par3 - 1, par4, ForgeDirection.UP, this) || soil == Blocks.sand || soil == Blocks.mycelium);
   }

   public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
      float f = 0.0625F;
      return AxisAlignedBB.getBoundingBox((double)((float)par2 + 0.5F - 0.2F + f), (double)par3, (double)((float)par4 + 0.5F - 0.2F + f), (double)((float)par2 + 0.5F + 0.2F - f), (double)((float)par3 + 0.6F - f), (double)((float)par4 + 0.5F + 0.2F - f));
   }

   @SideOnly(Side.CLIENT)
   public AxisAlignedBB getSelectedBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
      float f = 0.0625F;
      return AxisAlignedBB.getBoundingBox((double)((float)par2 + 0.5F - 0.2F + f), (double)par3, (double)((float)par4 + 0.5F - 0.2F + f), (double)((float)par2 + 0.5F + 0.2F - f), (double)((float)par3 + 0.6F - f), (double)((float)par4 + 0.5F + 0.2F - f));
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

   public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z) {
      return EnumPlantType.Plains;
   }

   public Block getPlant(IBlockAccess world, int x, int y, int z) {
      return this;
   }

   public int getPlantMetadata(IBlockAccess world, int x, int y, int z) {
      return world.getBlockMetadata(x, y, z);
   }


   public static class ClassItemBlock extends MultiItemBlock {

      public ClassItemBlock(Block block) {
         super(block);
      }

      protected String[] getNames() {
         return BlockPlantMine.woodType;
      }

      @SideOnly(Side.CLIENT)
      public IIcon getIconFromDamage(int par1) {
         return super.field_150939_a.getIcon(0, par1);
      }
   }
}
