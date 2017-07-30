package com.emoniph.witchery.blocks;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockBaseContainer;
import com.emoniph.witchery.common.ExtendedPlayer;
import com.emoniph.witchery.util.BlockUtil;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class BlockBloodCrucible extends BlockBaseContainer {

   public BlockBloodCrucible() {
      super(Material.rock, BlockBloodCrucible.TileEntityBloodCrucible.class);
      this.setResistance(1000.0F);
      this.setHardness(2.5F);
      this.setStepSound(Block.soundTypeStone);
      this.setBlockBounds(0.25F, 0.0F, 0.25F, 0.75F, 0.31F, 0.75F);
   }

   public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
      if(world.isRemote) {
         return true;
      } else {
         BlockBloodCrucible.TileEntityBloodCrucible crucible = (BlockBloodCrucible.TileEntityBloodCrucible)BlockUtil.getTileEntity(world, x, y, z, BlockBloodCrucible.TileEntityBloodCrucible.class);
         if(crucible != null && world instanceof WorldServer) {
            ExtendedPlayer playerEx = ExtendedPlayer.get(player);
            ItemStack stack = player.getHeldItem();
            if(playerEx.getVampireLevel() >= 10 && (crucible.isFull() || player.capabilities.isCreativeMode) && stack != null) {
               boolean success = false;
               if(Witchery.Items.GENERIC.itemArtichoke.isMatch(stack)) {
                  playerEx.setVampireUltimate(ExtendedPlayer.VampireUltimate.STORM);
                  success = true;
               } else if(Witchery.Items.GENERIC.itemBatWool.isMatch(stack)) {
                  playerEx.setVampireUltimate(ExtendedPlayer.VampireUltimate.SWARM);
                  success = true;
               } else if(stack.getItem() == Items.bone) {
                  playerEx.setVampireUltimate(ExtendedPlayer.VampireUltimate.FARM);
                  success = true;
               }

               if(success) {
                  crucible.drainAll();
                  --stack.stackSize;
                  ParticleEffect.REDDUST.send(SoundEffect.RANDOM_FIZZ, world, 0.5D + (double)x, (double)y, 0.5D + (double)z, 0.5D, 0.5D, 16);
               } else {
                  ParticleEffect.SMOKE.send(SoundEffect.NOTE_SNARE, world, 0.5D + (double)x, (double)y, 0.5D + (double)z, 0.5D, 0.5D, 16);
               }
            } else {
               ParticleEffect.SMOKE.send(SoundEffect.NOTE_SNARE, world, 0.5D + (double)x, (double)y, 0.5D + (double)z, 0.5D, 0.5D, 16);
            }
         }

         return true;
      }
   }

   public int quantityDropped(Random rand) {
      return 1;
   }

   @SideOnly(Side.CLIENT)
   public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
      return false;
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public boolean renderAsNormalBlock() {
      return false;
   }

   @SideOnly(Side.CLIENT)
   public void randomDisplayTick(World world, int x, int y, int z, Random rand) {}

   public static class TileEntityBloodCrucible extends TileEntity {

      private static final int MAX_BLOOD_LEVEL = 20;
      private int bloodLevel;


      public boolean canUpdate() {
         return false;
      }

      public boolean isFull() {
         return this.bloodLevel == 20;
      }

      public void drainAll() {
         this.bloodLevel = 0;
         this.markBlockForUpdate(false);
      }

      public int getBloodLevel() {
         return this.bloodLevel;
      }

      public void increaseBloodLevel() {
         if(this.bloodLevel < 20) {
            this.bloodLevel = Math.min(5 + this.bloodLevel, 20);
            this.markBlockForUpdate(false);
         }

      }

      public float getPercentFilled() {
         return (float)this.bloodLevel / 20.0F;
      }

      public void writeToNBT(NBTTagCompound nbtRoot) {
         super.writeToNBT(nbtRoot);
         nbtRoot.setInteger("BloodLevel", this.bloodLevel);
      }

      public void readFromNBT(NBTTagCompound nbtRoot) {
         super.readFromNBT(nbtRoot);
         this.bloodLevel = nbtRoot.getInteger("BloodLevel");
      }

      public void markBlockForUpdate(boolean notifyNeighbours) {
         super.worldObj.markBlockForUpdate(super.xCoord, super.yCoord, super.zCoord);
         if(notifyNeighbours && super.worldObj != null) {
            super.worldObj.notifyBlockChange(super.xCoord, super.yCoord, super.zCoord, this.getBlockType());
         }

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
