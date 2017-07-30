package com.emoniph.witchery.blocks;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockBase;
import com.emoniph.witchery.client.particle.NaturePowerFX;
import com.emoniph.witchery.util.ParticleEffect;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCircleGlyph extends BlockBase {

   private int color;
   private boolean charged;
   @SideOnly(Side.CLIENT)
   private IIcon[] icons;


   public BlockCircleGlyph(int color, boolean charged) {
      super(Material.vine);
      super.registerWithCreateTab = false;
      this.color = color;
      this.charged = charged;
      this.setResistance(1000.0F);
      this.setHardness(2.0F);
      float f = 0.5F;
      float f1 = 0.015625F;
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.015625F, 1.0F);
   }

   public void onBlockClicked(World world, int posX, int posY, int posZ, EntityPlayer player) {
      if(!world.isRemote) {
         ItemStack itemstack = player.getHeldItem();
         if(itemstack != null && (Witchery.Items.GENERIC.itemBroom.isMatch(itemstack) || Witchery.Items.GENERIC.itemBroomEnchanted.isMatch(itemstack))) {
            world.func_147480_a(posX, posY, posZ, false);
         }
      }

   }

   public void harvestBlock(World world, EntityPlayer player, int posX, int posY, int posZ, int meta) {}

   @SideOnly(Side.CLIENT)
   public void registerBlockIcons(IIconRegister iconRegistrar) {
      this.icons = new IIcon[12];

      for(int glyph = 0; glyph < this.icons.length; ++glyph) {
         this.icons[glyph] = iconRegistrar.registerIcon(this.getTextureName() + String.format("%d.%d", new Object[]{Integer.valueOf(this.color + 1), Integer.valueOf(glyph + 1)}));
      }

   }

   @SideOnly(Side.CLIENT)
   public IIcon getIcon(int face, int metadata) {
      return this.icons[MathHelper.clamp_int(metadata, 0, 12)];
   }

   public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
      return null;
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public boolean renderAsNormalBlock() {
      return false;
   }

   public int quantityDropped(Random rand) {
      return 0;
   }

   @SideOnly(Side.CLIENT)
   public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
      double d0;
      double d1;
      double d2;
      if(this.color > 0) {
         d0 = (double)((float)x + 0.4F + rand.nextFloat() * 0.2F);
         d1 = (double)((float)y + 0.1F + rand.nextFloat() * 0.3F);
         d2 = (double)((float)z + 0.4F + rand.nextFloat() * 0.2F);
         world.spawnParticle(this.color == 2?ParticleEffect.FLAME.toString():ParticleEffect.PORTAL.toString(), d0, d1, d2, 0.0D, 0.0D, 0.0D);
      } else if(this.charged) {
         d0 = (double)((float)x + 0.3F + rand.nextFloat() * 0.4F);
         d1 = (double)((float)y + 0.1F + rand.nextFloat() * 0.3F);
         d2 = (double)((float)z + 0.3F + rand.nextFloat() * 0.4F);
         NaturePowerFX sparkle = new NaturePowerFX(world, d0, d1, d2);
         sparkle.setScale(0.6F);
         sparkle.setGravity(0.01F);
         sparkle.setCanMove(true);
         double maxSpeed = 0.01D;
         double doubleSpeed = 0.02D;
         sparkle.setVelocity(rand.nextDouble() * 0.02D - 0.01D, rand.nextDouble() * 0.02D + 0.01D, rand.nextDouble() * 0.02D - 0.01D);
         sparkle.setMaxAge(10 + rand.nextInt(5));
         float maxColorShift = 0.2F;
         float doubleColorShift = maxColorShift * 2.0F;
         float colorshiftR = rand.nextFloat() * doubleColorShift - maxColorShift;
         float colorshiftG = rand.nextFloat() * doubleColorShift - maxColorShift;
         float colorshiftB = rand.nextFloat() * doubleColorShift - maxColorShift;
         float red = 1.0F;
         float green = 0.8F;
         float blue = 0.2F;
         sparkle.setRBGColorF(red + colorshiftR, green + colorshiftG, blue + colorshiftB);
         sparkle.setAlphaF(0.1F);
         Minecraft.getMinecraft().effectRenderer.addEffect(sparkle);
      }

   }

   public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
      this.func_111046_k(world, x, y, z);
   }

   private boolean func_111046_k(World world, int x, int y, int z) {
      if(!this.canBlockStay(world, x, y, z)) {
         world.setBlockToAir(x, y, z);
         return false;
      } else {
         return true;
      }
   }

   public boolean canBlockStay(World world, int x, int y, int z) {
      Material materialBelow = world.getBlock(x, y - 1, z).getMaterial();
      return !world.isAirBlock(x, y - 1, z) && materialBelow != null && materialBelow.isOpaque() && materialBelow.isSolid();
   }

   @SideOnly(Side.CLIENT)
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int x, int y, int z, int side) {
      return side == 1;
   }

   public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
      Block block = world.getBlock(x, y, z);
      return block == Witchery.Blocks.GLYPH_INFERNAL?new ItemStack(Witchery.Items.CHALK_INFERNAL):(block == Witchery.Blocks.GLYPH_OTHERWHERE?new ItemStack(Witchery.Items.CHALK_OTHERWHERE):new ItemStack(Witchery.Items.CHALK_RITUAL));
   }
}
