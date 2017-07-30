package com.emoniph.witchery.blocks;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockBaseContainer;
import com.emoniph.witchery.blocks.TileEntityBase;
import com.emoniph.witchery.common.INullSource;
import com.emoniph.witchery.common.PowerSources;
import com.emoniph.witchery.item.ItemGeneral;
import com.emoniph.witchery.util.Log;
import com.emoniph.witchery.util.ParticleEffect;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class BlockVoidBramble extends BlockBaseContainer {

   public BlockVoidBramble() {
      super(Material.plants, BlockVoidBramble.TileEntityVoidBramble.class);
      this.setBlockUnbreakable();
      this.setResistance(1000.0F);
      this.setLightLevel(0.125F);
      this.setStepSound(Block.soundTypeGrass);
      float f = 0.45F;
      this.setBlockBounds(0.050000012F, 0.0F, 0.050000012F, 0.95F, 1.0F, 0.95F);
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
      if(!world.isRemote && entity instanceof EntityLivingBase && entity instanceof EntityLivingBase) {
         teleportRandomly(world, posX, posY, posZ, entity, 500);
      }

   }

   public static void teleportRandomly(World world, int posX, int posY, int posZ, Entity entity, int distance) {
      int doubleDistance = distance * 2;
      posX += world.rand.nextInt(doubleDistance) - distance;
      posZ += world.rand.nextInt(doubleDistance) - distance;

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

   @SideOnly(Side.CLIENT)
   public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
      if(rand.nextInt(2) == 0) {
         double d0 = (double)((float)x + rand.nextFloat());
         double d1 = (double)((float)y + 0.15F + rand.nextFloat() * 0.3F) + 0.5D;
         double d2 = (double)((float)z + rand.nextFloat());
         world.spawnParticle(ParticleEffect.PORTAL.toString(), d0, d1, d2, 0.0D, -1.2D, 0.0D);
      }

   }

   public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player) {
      if(!world.isRemote) {
         BlockVoidBramble.TileEntityVoidBramble tile = (BlockVoidBramble.TileEntityVoidBramble)world.getTileEntity(x, y, z);
         if(tile != null && (player.capabilities.isCreativeMode || player.getCommandSenderName().equals(tile.getOwner()))) {
            for(int dy = y; world.getBlock(x, dy, z) == this; ++dy) {
               world.setBlockToAir(x, dy, z);
               world.spawnEntityInWorld(new EntityItem(world, 0.5D + (double)x, 0.5D + (double)dy, 0.5D + (double)z, new ItemStack(this)));
            }
         }
      }

   }

   public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
      if(!world.isRemote && entity instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer)entity;
         BlockVoidBramble.TileEntityVoidBramble tile = (BlockVoidBramble.TileEntityVoidBramble)world.getTileEntity(x, y, z);
         if(tile != null) {
            tile.setOwner(player.getCommandSenderName());
         }
      }

   }

   public TileEntity createNewTileEntity(World world, int metadata) {
      return new BlockVoidBramble.TileEntityVoidBramble();
   }

   public static class TileEntityVoidBramble extends TileEntityBase implements INullSource {

      private String owner;
      private static final String OWNER_KEY = "WITCPlacer";


      public boolean isPowerInvalid() {
         return this.isInvalid();
      }

      protected void initiate() {
         super.initiate();
         if(!super.worldObj.isRemote) {
            if(super.worldObj.getBlock(super.xCoord, super.yCoord, super.zCoord) == Witchery.Blocks.VOID_BRAMBLE) {
               Log.instance().debug("Initiating void bramble tile at: " + super.xCoord + ", " + super.yCoord + ", " + super.zCoord);
               PowerSources.instance().registerNullSource(this);
            } else {
               Log.instance().warning("Void bramble tile entity exists without a corresponding block at: " + super.xCoord + ", " + super.yCoord + ", " + super.zCoord);
            }
         }

      }

      public void invalidate() {
         super.invalidate();
         if(!super.worldObj.isRemote) {
            Log.instance().debug("Invalidating void bramble tile at: " + super.xCoord + ", " + super.yCoord + ", " + super.zCoord);
            PowerSources.instance().removeNullSource(this);
         }

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

      public World getWorld() {
         return super.worldObj;
      }

      public int getPosX() {
         return super.xCoord;
      }

      public int getPosY() {
         return super.yCoord;
      }

      public int getPosZ() {
         return super.zCoord;
      }

      public float getRange() {
         return 32.0F;
      }
   }
}
