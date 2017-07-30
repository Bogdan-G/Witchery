package com.emoniph.witchery.blocks;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockBaseContainer;
import com.emoniph.witchery.blocks.TileEntityBase;
import com.emoniph.witchery.entity.EntityWolfman;
import com.emoniph.witchery.infusion.infusions.InfusionInfernal;
import com.emoniph.witchery.util.BlockUtil;
import com.emoniph.witchery.util.CreatureUtil;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.emoniph.witchery.util.TimeUtil;
import com.mojang.authlib.GameProfile;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockBeartrap extends BlockBaseContainer {

   private final boolean silvered;


   public BlockBeartrap(boolean silvered) {
      super(Material.iron, BlockBeartrap.TileEntityBeartrap.class);
      this.silvered = silvered;
      this.setHardness(5.0F);
      this.setResistance(10.0F);
      this.setStepSound(Block.soundTypeMetal);
      float w = 0.3F;
      this.setBlockBounds(0.19999999F, 0.01F, 0.19999999F, 0.8F, 0.1F, 0.8F);
   }

   public TileEntity createNewTileEntity(World world, int metadata) {
      BlockBeartrap.TileEntityBeartrap tile = new BlockBeartrap.TileEntityBeartrap(this.silvered);
      return tile;
   }

   public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
      return null;
   }

   public int getRenderType() {
      return -1;
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
      return false;
   }

   public boolean renderAsNormalBlock() {
      return false;
   }

   public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
      switch(MathHelper.floor_double((double)(entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3) {
      case 0:
         world.setBlockMetadataWithNotify(x, y, z, 2, 2);
         break;
      case 1:
         world.setBlockMetadataWithNotify(x, y, z, 5, 2);
         break;
      case 2:
         world.setBlockMetadataWithNotify(x, y, z, 3, 2);
         break;
      case 3:
         world.setBlockMetadataWithNotify(x, y, z, 4, 2);
      }

      if(!world.isRemote && entity instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer)entity;
         BlockBeartrap.TileEntityBeartrap tile = (BlockBeartrap.TileEntityBeartrap)BlockUtil.getTileEntity(world, x, y, z, BlockBeartrap.TileEntityBeartrap.class);
         if(tile != null) {
            tile.owner = player.getGameProfile();
            tile.sprung = true;
            tile.markBlockForUpdate(false);
         }
      }

   }

   public ArrayList getDrops(World world, int x, int y, int z, int metadata, int fortune) {
      return this.silvered?new ArrayList():super.getDrops(world, x, y, z, metadata, fortune);
   }

   public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
      if(!world.isRemote && entity instanceof EntityLivingBase) {
         EntityLivingBase living = (EntityLivingBase)entity;
         BlockBeartrap.TileEntityBeartrap tile = (BlockBeartrap.TileEntityBeartrap)BlockUtil.getTileEntity(world, x, y, z, BlockBeartrap.TileEntityBeartrap.class);
         if(tile != null && !tile.sprung && world.getTotalWorldTime() > tile.setTime + 20L && (!this.silvered || CreatureUtil.isWerewolf(entity, false))) {
            AxisAlignedBB trapBounds = AxisAlignedBB.getBoundingBox((double)x + super.minX, (double)y + super.minY, (double)z + super.minZ, (double)x + super.maxX, (double)y + super.maxY, (double)z + super.maxZ);
            if(trapBounds.intersectsWith(entity.boundingBox) && (!this.silvered || tile.tryTrapWolf(living))) {
               boolean isCreative = entity instanceof EntityPlayer && ((EntityPlayer)entity).capabilities.isCreativeMode;
               if(!isCreative) {
                  living.addPotionEffect(new PotionEffect(Witchery.Potions.PARALYSED.id, TimeUtil.secsToTicks(30), 2, true));
               }

               living.attackEntityFrom(DamageSource.anvil, 4.0F);
               ParticleEffect.REDDUST.send(SoundEffect.WITCHERY_RANDOM_MANTRAP, world, 0.5D + (double)x, 0.5D + (double)y, 0.5D + (double)z, 0.25D, 0.5D, 16);
               tile.sprung = true;
               tile.markBlockForUpdate(true);
            }
         }
      }

   }

   public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
      if(!world.isRemote) {
         BlockBeartrap.TileEntityBeartrap tile = (BlockBeartrap.TileEntityBeartrap)BlockUtil.getTileEntity(world, x, y, z, BlockBeartrap.TileEntityBeartrap.class);
         if(tile != null) {
            SoundEffect.WITCHERY_RANDOM_CLICK.playAtPlayer(world, player);
            tile.sprung = !tile.sprung;
            if(!tile.sprung) {
               tile.setTime = world.getTotalWorldTime();
            }

            tile.markBlockForUpdate(false);
         }
      }

      return true;
   }

   @SideOnly(Side.CLIENT)
   public static boolean checkForHiddenTrap(EntityPlayer player, MovingObjectPosition mop) {
      if(mop != null && mop.typeOfHit == MovingObjectType.BLOCK && player.worldObj.getBlock(mop.blockX, mop.blockY, mop.blockZ) == Witchery.Blocks.BEARTRAP) {
         BlockBeartrap.TileEntityBeartrap tile = (BlockBeartrap.TileEntityBeartrap)BlockUtil.getTileEntity(player.worldObj, mop.blockX, mop.blockY, mop.blockZ, BlockBeartrap.TileEntityBeartrap.class);
         if(tile != null) {
            return !tile.isVisibleTo(player);
         }
      }

      return false;
   }

   public static class TileEntityBeartrap extends TileEntityBase {

      private final boolean silvered;
      private GameProfile owner;
      private boolean sprung = true;
      private long setTime = 0L;
      private long startTime = 0L;
      private UUID spawnedWolfID = null;
      private static final int MIN_LURE_TIME = TimeUtil.minsToTicks(1);
      private static final int LURE_EXTRA = TimeUtil.minsToTicks(1);


      public TileEntityBeartrap() {
         this.silvered = false;
      }

      public TileEntityBeartrap(boolean silvered) {
         this.silvered = silvered;
      }

      public boolean tryTrapWolf(EntityLivingBase living) {
         if(this.silvered && living instanceof EntityWolfman) {
            EntityWolfman wolf = (EntityWolfman)living;
            if(this.spawnedWolfID != null && wolf != null && wolf.getPersistentID().equals(this.spawnedWolfID)) {
               SoundEffect.WITCHERY_MOB_WOLFMAN_LORD.playAt((TileEntity)this, 1.0F);
               wolf.setInfectious();
               return true;
            }
         }

         return false;
      }

      public boolean isSprung() {
         return this.sprung;
      }

      public boolean canUpdate() {
         return this.silvered;
      }

      public void updateEntity() {
         super.updateEntity();
         if(!super.worldObj.isRemote && this.silvered && !this.sprung && this.spawnedWolfID == null && TimeUtil.secondsElapsed(10, super.ticks)) {
            if(this.baitFound() && CreatureUtil.isFullMoon(super.worldObj)) {
               long time = super.worldObj.getTotalWorldTime();
               if(this.startTime > 0L) {
                  long activateTime = this.startTime;
                  if(time > activateTime && CreatureUtil.isFullMoon(super.worldObj)) {
                     EntityCreature creature = InfusionInfernal.spawnCreature(super.worldObj, EntityWolfman.class, super.xCoord, super.yCoord, super.zCoord, (EntityLivingBase)null, 16, 32, ParticleEffect.SMOKE, SoundEffect.WITCHERY_MOB_WOLFMAN_TALK);
                     if(creature != null) {
                        creature.func_110163_bv();
                        this.spawnedWolfID = creature.getPersistentID();
                     }
                  }
               } else {
                  this.startTime = time;
               }
            } else {
               this.startTime = 0L;
            }
         }

      }

      private boolean baitFound() {
         double R = 8.0D;
         double RSQ = 64.0D;
         boolean foundSheep = false;
         AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(0.5D + (double)super.xCoord - 8.0D, 0.5D + (double)super.yCoord - 8.0D, 0.5D + (double)super.zCoord - 8.0D, 0.5D + (double)super.xCoord + 8.0D, 0.5D + (double)super.yCoord + 8.0D, 0.5D + (double)super.zCoord + 8.0D);
         List sheep = super.worldObj.getEntitiesWithinAABB(EntitySheep.class, bounds);
         Iterator wolfaltar = sheep.iterator();

         while(wolfaltar.hasNext()) {
            EntitySheep aSheep = (EntitySheep)wolfaltar.next();
            if(aSheep.getDistanceSq(0.5D + (double)super.xCoord, 0.5D + (double)super.yCoord, 0.5D + (double)super.zCoord) <= 64.0D && aSheep.getLeashed()) {
               foundSheep = true;
               break;
            }
         }

         boolean wolfaltar1 = super.worldObj.getBlock(super.xCoord + 1, super.yCoord, super.zCoord) == Witchery.Blocks.WOLF_ALTAR || super.worldObj.getBlock(super.xCoord - 1, super.yCoord, super.zCoord) == Witchery.Blocks.WOLF_ALTAR || super.worldObj.getBlock(super.xCoord, super.yCoord, super.zCoord + 1) == Witchery.Blocks.WOLF_ALTAR || super.worldObj.getBlock(super.xCoord, super.yCoord, super.zCoord - 1) == Witchery.Blocks.WOLF_ALTAR;
         return wolfaltar1 && foundSheep;
      }

      public boolean isVisibleTo(EntityPlayer player) {
         return !this.sprung && this.owner != null && !this.silvered?(player == null?false:player.getGameProfile().equals(this.owner)):true;
      }

      public void writeToNBT(NBTTagCompound nbtRoot) {
         super.writeToNBT(nbtRoot);
         nbtRoot.setBoolean("Sprung", this.sprung);
         nbtRoot.setLong("WolftrapStart", this.startTime);
         if(this.spawnedWolfID != null) {
            nbtRoot.setLong("WolfLeast", this.spawnedWolfID.getLeastSignificantBits());
            nbtRoot.setLong("WolfMost", this.spawnedWolfID.getMostSignificantBits());
         }

         if(this.owner != null) {
            NBTTagCompound nbtPlayer = new NBTTagCompound();
            NBTUtil.func_152460_a(nbtPlayer, this.owner);
            nbtRoot.setTag("Owner", nbtPlayer);
         }

      }

      public void readFromNBT(NBTTagCompound nbtRoot) {
         super.readFromNBT(nbtRoot);
         this.sprung = nbtRoot.getBoolean("Sprung");
         this.startTime = nbtRoot.getLong("WolftrapStart");
         if(nbtRoot.hasKey("Owner", 10)) {
            this.owner = NBTUtil.func_152459_a(nbtRoot.getCompoundTag("Owner"));
         } else {
            this.owner = null;
         }

         if(nbtRoot.hasKey("WolfLeast") && nbtRoot.hasKey("WolfMost")) {
            this.spawnedWolfID = new UUID(nbtRoot.getLong("WolfMost"), nbtRoot.getLong("WolfLeast"));
         } else {
            this.spawnedWolfID = null;
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
