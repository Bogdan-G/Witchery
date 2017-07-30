package com.emoniph.witchery.blocks;

import com.emoniph.witchery.blocks.BlockBaseContainer;
import com.emoniph.witchery.common.IPowerSource;
import com.emoniph.witchery.common.PowerSources;
import com.emoniph.witchery.predictions.PredictionManager;
import com.emoniph.witchery.util.ChatUtil;
import com.emoniph.witchery.util.Coord;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCrystalBall extends BlockBaseContainer {

   private static final float ALTAR_POWER_PER_READING = 500.0F;
   private static final int POWER_SOURCE_RADIUS = 16;


   public BlockCrystalBall() {
      super(Material.anvil, BlockCrystalBall.TileEntityCrystalBall.class);
      this.setHardness(2.0F);
      this.setStepSound(Block.soundTypeMetal);
      this.setBlockBounds(0.3F, 0.0F, 0.3F, 0.7F, 0.6F, 0.7F);
   }

   public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
      if(world.isRemote) {
         return true;
      } else {
         TileEntity tile = world.getTileEntity(x, y, z);
         if(tile != null && tile instanceof BlockCrystalBall.TileEntityCrystalBall) {
            BlockCrystalBall.TileEntityCrystalBall ball = (BlockCrystalBall.TileEntityCrystalBall)tile;
            if(ball.canBeUsed()) {
               if(tryConsumePower(world, player, x, y, z)) {
                  AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(0.5D + (double)x - 5.0D, 0.5D + (double)y - 2.0D, 0.5D + (double)z - 5.0D, 0.5D + (double)x + 5.0D, 0.5D + (double)y + 2.0D, 0.5D + (double)z + 5.0D);
                  List players = world.getEntitiesWithinAABB(EntityPlayer.class, bounds);
                  EntityPlayer victim = player;
                  double closest = 10000.0D;

                  for(int i = 0; i < players.size(); ++i) {
                     EntityPlayer nearbyPlayer = players.get(i) != null?(EntityPlayer)players.get(i):null;
                     if(nearbyPlayer != null && nearbyPlayer != player) {
                        double distSq = player.getDistanceSq(0.5D + (double)x, 0.5D + (double)y, 0.5D + (double)z);
                        if(distSq < closest) {
                           victim = nearbyPlayer;
                           closest = distSq;
                        }
                     }
                  }

                  PredictionManager.instance().makePrediction(victim, player, true);
                  ball.onUsed();
                  ParticleEffect.EXPLODE.send(SoundEffect.RANDOM_LEVELUP, world, 0.5D + (double)x, 0.2D + (double)y, 0.5D + (double)z, 0.2D, 0.2D, 16);
               }
            } else {
               ChatUtil.sendTranslated(EnumChatFormatting.GRAY, player, "witchery.prediction.recharging", new Object[0]);
               SoundEffect.NOTE_SNARE.playAtPlayer(world, player);
            }
         }

         return true;
      }
   }

   public static boolean tryConsumePower(World world, EntityPlayer player, int x, int y, int z) {
      IPowerSource powerSource = findNewPowerSource(world, x, y, z);
      if(powerSource != null && powerSource.consumePower(500.0F)) {
         return true;
      } else {
         if(!world.isRemote) {
            ChatUtil.sendTranslated(EnumChatFormatting.GRAY, player, "witchery.prediction.nopower", new Object[0]);
            SoundEffect.NOTE_SNARE.playAtPlayer(world, player);
         }

         return false;
      }
   }

   private static IPowerSource findNewPowerSource(World world, int posX, int posY, int posZ) {
      ArrayList sources = PowerSources.instance() != null?PowerSources.instance().get(world, new Coord(posX, posY, posZ), 16):null;
      return sources != null && sources.size() > 0?((PowerSources.RelativePowerSource)sources.get(0)).source():null;
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

   public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, Block par5) {
      this.func_111046_k(par1World, par2, par3, par4);
   }

   private boolean func_111046_k(World par1World, int par2, int par3, int par4) {
      if(!this.canBlockStay(par1World, par2, par3, par4)) {
         this.dropBlockAsItem(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), 0);
         par1World.setBlockToAir(par2, par3, par4);
         return false;
      } else {
         return true;
      }
   }

   public boolean canBlockStay(World world, int x, int y, int z) {
      Material material = world.getBlock(x, y - 1, z).getMaterial();
      return !world.isAirBlock(x, y - 1, z) && material != null && material.isOpaque() && material.isSolid();
   }

   @SideOnly(Side.CLIENT)
   public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
      return false;
   }

   @SideOnly(Side.CLIENT)
   public void randomDisplayTick(World world, int x, int y, int z, Random rand) {}

   public static class TileEntityCrystalBall extends TileEntity {

      private long lastUsedTime = 0L;


      public boolean canUpdate() {
         return false;
      }

      public void onUsed() {
         this.lastUsedTime = super.worldObj.getTotalWorldTime();
      }

      public boolean canBeUsed() {
         return super.worldObj.getTotalWorldTime() - this.lastUsedTime > 100L;
      }

      public void writeToNBT(NBTTagCompound nbtTag) {
         super.writeToNBT(nbtTag);
         nbtTag.setLong("LastUsedTime", this.lastUsedTime);
      }

      public void readFromNBT(NBTTagCompound nbtTag) {
         super.readFromNBT(nbtTag);
         this.lastUsedTime = nbtTag.getLong("LastUsedTime");
      }

      public Packet getDescriptionPacket() {
         NBTTagCompound nbtTag = new NBTTagCompound();
         this.writeToNBT(nbtTag);
         return new S35PacketUpdateTileEntity(super.xCoord, super.yCoord, super.zCoord, 1, nbtTag);
      }

      public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
         super.onDataPacket(net, packet);
         this.readFromNBT(packet.func_148857_g());
         super.worldObj.func_147479_m(super.xCoord, super.yCoord, super.zCoord);
      }
   }
}
