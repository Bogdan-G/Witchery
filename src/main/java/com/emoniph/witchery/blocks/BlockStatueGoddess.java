package com.emoniph.witchery.blocks;

import com.emoniph.witchery.blocks.BlockBaseContainer;
import com.emoniph.witchery.infusion.Infusion;
import com.emoniph.witchery.util.BlockUtil;
import com.emoniph.witchery.util.ChatUtil;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockStatueGoddess extends BlockBaseContainer {

   public BlockStatueGoddess() {
      super(Material.rock, BlockStatueGoddess.TileEntityStatueGoddess.class);
      this.setBlockUnbreakable();
      this.setResistance(1000.0F);
      this.setHardness(2.5F);
      this.setStepSound(Block.soundTypeStone);
      this.setBlockBounds(0.0F, 0.0F, 0.1F, 1.0F, 2.0F, 0.9F);
   }

   public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack) {
      int l = MathHelper.floor_double((double)(par5EntityLivingBase.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
      if(l == 0) {
         par1World.setBlockMetadataWithNotify(par2, par3, par4, 2, 2);
      }

      if(l == 1) {
         par1World.setBlockMetadataWithNotify(par2, par3, par4, 5, 2);
      }

      if(l == 2) {
         par1World.setBlockMetadataWithNotify(par2, par3, par4, 3, 2);
      }

      if(l == 3) {
         par1World.setBlockMetadataWithNotify(par2, par3, par4, 4, 2);
      }

      if(!par1World.isRemote && par5EntityLivingBase instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer)par5EntityLivingBase;
         BlockStatueGoddess.TileEntityStatueGoddess tile = (BlockStatueGoddess.TileEntityStatueGoddess)BlockUtil.getTileEntity(par1World, par2, par3, par4, BlockStatueGoddess.TileEntityStatueGoddess.class);
         if(tile != null) {
            tile.setOwner(player.getCommandSenderName());
         }
      }

   }

   public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player) {
      if(!world.isRemote) {
         BlockStatueGoddess.TileEntityStatueGoddess tile = (BlockStatueGoddess.TileEntityStatueGoddess)BlockUtil.getTileEntity(world, x, y, z, BlockStatueGoddess.TileEntityStatueGoddess.class);
         if(tile != null && (player.capabilities.isCreativeMode || player.getCommandSenderName().equals(tile.getOwner()) && player.isSneaking())) {
            for(int dy = y; world.getBlock(x, dy, z) == this; ++dy) {
               world.setBlockToAir(x, dy, z);
               world.spawnEntityInWorld(new EntityItem(world, 0.5D + (double)x, 0.5D + (double)dy, 0.5D + (double)z, new ItemStack(this)));
            }
         }
      }

   }

   public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {
      if(world.isRemote) {
         return super.onBlockActivated(world, x, y, z, player, par6, par7, par8, par9);
      } else {
         NBTTagCompound nbtTag = Infusion.getNBT(player);
         if(nbtTag != null && (nbtTag.hasKey("witcheryCursed") || nbtTag.hasKey("witcheryInsanity") || nbtTag.hasKey("witcherySinking") || nbtTag.hasKey("witcheryOverheating") || nbtTag.hasKey("witcheryWakingNightmare"))) {
            if(nbtTag.hasKey("witcheryCursed")) {
               nbtTag.removeTag("witcheryCursed");
               ChatUtil.sendTranslated(EnumChatFormatting.BLUE, player, "tile.witcheryStatusGoddess.curemisfortune", new Object[0]);
            }

            if(nbtTag.hasKey("witcheryInsanity")) {
               nbtTag.removeTag("witcheryInsanity");
               ChatUtil.sendTranslated(EnumChatFormatting.BLUE, player, "tile.witcheryStatusGoddess.cureinsanity", new Object[0]);
            }

            if(nbtTag.hasKey("witcherySinking")) {
               nbtTag.removeTag("witcherySinking");
               ChatUtil.sendTranslated(EnumChatFormatting.BLUE, player, "tile.witcheryStatusGoddess.curesinking", new Object[0]);
               Infusion.syncPlayer(world, player);
            }

            if(nbtTag.hasKey("witcheryOverheating")) {
               nbtTag.removeTag("witcheryOverheating");
               ChatUtil.sendTranslated(EnumChatFormatting.BLUE, player, "tile.witcheryStatusGoddess.cureoverheating", new Object[0]);
            }

            if(nbtTag.hasKey("witcheryWakingNightmare")) {
               nbtTag.removeTag("witcheryWakingNightmare");
               ChatUtil.sendTranslated(EnumChatFormatting.BLUE, player, "tile.witcheryStatusGoddess.curenightmare", new Object[0]);
            }

            if(player.isPotionActive(Potion.poison)) {
               player.removePotionEffect(Potion.poison.id);
            }

            if(player.isPotionActive(Potion.weakness)) {
               player.removePotionEffect(Potion.weakness.id);
            }

            if(player.isPotionActive(Potion.blindness)) {
               player.removePotionEffect(Potion.blindness.id);
            }

            if(player.isPotionActive(Potion.digSlowdown)) {
               player.removePotionEffect(Potion.digSlowdown.id);
            }

            if(player.isPotionActive(Potion.moveSlowdown)) {
               player.removePotionEffect(Potion.moveSlowdown.id);
            }

            ParticleEffect.INSTANT_SPELL.send(SoundEffect.RANDOM_FIZZ, player, 1.0D, 2.0D, 8);
         } else {
            SoundEffect.NOTE_SNARE.playAtPlayer(world, player);
         }

         return true;
      }
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public boolean renderAsNormalBlock() {
      return false;
   }

   public int quantityDropped(Random rand) {
      return 1;
   }

   @SideOnly(Side.CLIENT)
   public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
      return false;
   }

   @SideOnly(Side.CLIENT)
   public void randomDisplayTick(World world, int x, int y, int z, Random rand) {}

   @SideOnly(Side.CLIENT)
   public void registerBlockIcons(IIconRegister par1IconRegister) {
      super.blockIcon = par1IconRegister.registerIcon("stone");
   }

   public static class TileEntityStatueGoddess extends TileEntity {

      private static final String OWNER_KEY = "WITCPlacer";
      private String owner;


      public boolean canUpdate() {
         return false;
      }

      public void setOwner(String username) {
         this.owner = username;
      }

      public String getOwner() {
         return this.owner != null?this.owner:"";
      }

      public void writeToNBT(NBTTagCompound nbtTag) {
         super.writeToNBT(nbtTag);
         nbtTag.setString("WITCPlacer", this.getOwner());
      }

      public void readFromNBT(NBTTagCompound nbtTag) {
         super.readFromNBT(nbtTag);
         if(nbtTag.hasKey("WITCPlacer")) {
            this.owner = nbtTag.getString("WITCPlacer");
         } else {
            this.owner = "";
         }

      }
   }
}
