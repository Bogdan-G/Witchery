package com.emoniph.witchery.blocks;

import com.emoniph.witchery.blocks.BlockBaseContainer;
import com.emoniph.witchery.blocks.TileEntityBase;
import com.emoniph.witchery.common.ExtendedPlayer;
import com.emoniph.witchery.entity.EntityGoblin;
import com.emoniph.witchery.entity.EntityGoblinGulg;
import com.emoniph.witchery.entity.EntityGoblinMog;
import com.emoniph.witchery.infusion.Infusion;
import com.emoniph.witchery.infusion.infusions.InfusionInfernal;
import com.emoniph.witchery.util.BlockUtil;
import com.emoniph.witchery.util.Config;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.emoniph.witchery.util.TimeUtil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockStatueOfWorship extends BlockBaseContainer {

   public BlockStatueOfWorship() {
      super(Material.rock, BlockStatueOfWorship.TileEntityStatueOfWorship.class, BlockStatueOfWorship.ClassItemBlock.class);
      this.setHardness(3.5F);
      this.setResistance(20.0F);
      this.setStepSound(Block.soundTypeStone);
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

   public String getLocalizedName() {
      return super.getLocalizedName();
   }

   public void onBlockPlacedBy(World world, int posX, int posY, int posZ, EntityLivingBase player, ItemStack stack) {
      int l = MathHelper.floor_double((double)(player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
      if(l == 0) {
         world.setBlockMetadataWithNotify(posX, posY, posZ, 2, 2);
      } else if(l == 1) {
         world.setBlockMetadataWithNotify(posX, posY, posZ, 5, 2);
      } else if(l == 2) {
         world.setBlockMetadataWithNotify(posX, posY, posZ, 3, 2);
      } else if(l == 3) {
         world.setBlockMetadataWithNotify(posX, posY, posZ, 4, 2);
      }

      if(stack != null && player instanceof EntityPlayer) {
         BlockStatueOfWorship.TileEntityStatueOfWorship tile = (BlockStatueOfWorship.TileEntityStatueOfWorship)BlockUtil.getTileEntity(world, posX, posY, posZ, BlockStatueOfWorship.TileEntityStatueOfWorship.class);
         if(tile != null) {
            NBTTagCompound nbtRoot = stack.getTagCompound();
            if(nbtRoot != null && nbtRoot.hasKey("WITCBoundPlayer")) {
               String playerName = nbtRoot.getString("WITCBoundPlayer");
               if(playerName != null && !playerName.isEmpty()) {
                  tile.setOwner(playerName);
               }
            }
         }
      }

   }

   public void onBlockAdded(World world, int posX, int posY, int posZ) {
      super.onBlockAdded(world, posX, posY, posZ);
      BlockUtil.setBlockDefaultDirection(world, posX, posY, posZ);
   }

   public void onBlockHarvested(World world, int posX, int posY, int posZ, int par5, EntityPlayer par6EntityPlayer) {
      if(par6EntityPlayer.capabilities.isCreativeMode) {
         par5 |= 8;
         world.setBlockMetadataWithNotify(posX, posY, posZ, par5, 4);
      }

      this.dropBlockAsItem(world, posX, posY, posZ, par5, 0);
      super.onBlockHarvested(world, posX, posY, posZ, par5, par6EntityPlayer);
   }

   public ArrayList getDrops(World world, int x, int y, int z, int metadata, int fortune) {
      ArrayList drops = new ArrayList();
      if((metadata & 8) == 0) {
         BlockStatueOfWorship.TileEntityStatueOfWorship tile = (BlockStatueOfWorship.TileEntityStatueOfWorship)BlockUtil.getTileEntity(world, x, y, z, BlockStatueOfWorship.TileEntityStatueOfWorship.class);
         if(tile != null) {
            ItemStack stack = new ItemStack(tile.getBlockType());
            NBTTagCompound nbtRoot = new NBTTagCompound();
            stack.setTagCompound(nbtRoot);
            nbtRoot.setString("WITCBoundPlayer", tile.owner != null?tile.owner:"");
            drops.add(stack);
         }
      }

      return drops;
   }

   private static String getBoundPlayerName(ItemStack stack) {
      NBTTagCompound nbtRoot = stack.getTagCompound();
      return nbtRoot != null?nbtRoot.getString("WITCBoundPlayer"):"";
   }

   public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hiyY, float hitZ) {
      if(!world.isRemote) {
         ItemStack item = player.getHeldItem();
         if(item != null && item.getItem() == Items.nether_star) {
            BlockStatueOfWorship.TileEntityStatueOfWorship tile = (BlockStatueOfWorship.TileEntityStatueOfWorship)BlockUtil.getTileEntity(world, x, y, z, BlockStatueOfWorship.TileEntityStatueOfWorship.class);
            if(tile != null && tile.owner != null && tile.owner.equals(player.getCommandSenderName())) {
               int worshippers = tile.updateWorshippersAndGetLevel();
               if(worshippers >= 5 && tile.summonGoblinGods(player, 16.0D, 8)) {
                  if(!player.capabilities.isCreativeMode) {
                     if(--item.stackSize <= 0) {
                        player.setCurrentItemOrArmor(0, (ItemStack)null);
                     }

                     double R = 8.0D;
                     AxisAlignedBB bb = AxisAlignedBB.getBoundingBox((double)x + 0.5D - R, (double)y + 0.5D - R, (double)z + 0.5D - R, (double)x + 0.5D + R, (double)y + 0.5D + R, (double)z + 0.5D + R);
                     List entities = world.getEntitiesWithinAABB(EntityGoblin.class, bb);
                     int i = 0;

                     for(int iMax = Math.min(entities.size(), 5); i < iMax; ++i) {
                        if(entities.get(i) instanceof EntityGoblin) {
                           EntityGoblin goblin = (EntityGoblin)entities.get(i);
                           goblin.attackEntityFrom(DamageSource.magic, goblin.getMaxHealth());
                        }
                     }
                  }

                  ParticleEffect.FLAME.send(SoundEffect.MOB_ENDERDRAGON_GROWL, world, 0.5D + (double)x, 0.5D + (double)y, 0.5D + (double)z, 0.5D, 0.5D, 16);
                  return true;
               }
            }
         }

         ParticleEffect.SMOKE.send(SoundEffect.NOTE_SNARE, world, 0.5D + (double)x, 0.5D + (double)y, 0.5D + (double)z, 0.5D, 0.5D, 16);
      }

      return false;
   }

   public static class ClassItemBlock extends ItemBlock {

      public ClassItemBlock(Block block) {
         super(block);
      }

      public String getItemStackDisplayName(ItemStack stack) {
         String s = super.getItemStackDisplayName(stack);
         String player = BlockStatueOfWorship.getBoundPlayerName(stack);
         return player != null && !player.isEmpty()?s + " (" + player + ")":s;
      }
   }

   public static class TileEntityStatueOfWorship extends TileEntityBase {

      private String owner;
      @SideOnly(Side.CLIENT)
      private ThreadDownloadImageData downloadImageSkin;
      @SideOnly(Side.CLIENT)
      private ResourceLocation locationSkin;


      public void setOwner(EntityPlayer player) {
         this.owner = player.getCommandSenderName();
         if(!super.worldObj.isRemote) {
            super.worldObj.markBlockForUpdate(super.xCoord, super.yCoord, super.zCoord);
         }

      }

      public void setOwner(String player) {
         this.owner = player;
         if(!super.worldObj.isRemote) {
            super.worldObj.markBlockForUpdate(super.xCoord, super.yCoord, super.zCoord);
         }

      }

      public boolean hasOwner() {
         return this.owner != null && !this.owner.isEmpty();
      }

      public void writeToNBT(NBTTagCompound nbtRoot) {
         super.writeToNBT(nbtRoot);
         nbtRoot.setString("Owner", this.owner != null?this.owner:"");
      }

      public void readFromNBT(NBTTagCompound nbtRoot) {
         super.readFromNBT(nbtRoot);
         this.owner = nbtRoot.getString("Owner");
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

      @SideOnly(Side.CLIENT)
      public ResourceLocation getLocationSkin() {
         if(this.locationSkin == null) {
            this.setupCustomSkin();
         }

         return this.locationSkin != null?this.locationSkin:AbstractClientPlayer.locationStevePng;
      }

      @SideOnly(Side.CLIENT)
      private void setupCustomSkin() {
         if(this.owner != null && !this.owner.isEmpty()) {
            this.locationSkin = AbstractClientPlayer.getLocationSkin(this.owner);
            this.downloadImageSkin = AbstractClientPlayer.getDownloadImageSkin(this.locationSkin, this.owner);
         }

      }

      public int updateWorshippersAndGetLevel() {
         double R = 8.0D;
         AxisAlignedBB bb = AxisAlignedBB.getBoundingBox((double)super.xCoord + 0.5D - 8.0D, (double)super.yCoord + 0.5D - 8.0D, (double)super.zCoord + 0.5D - 8.0D, (double)super.xCoord + 0.5D + 8.0D, (double)super.yCoord + 0.5D + 8.0D, (double)super.zCoord + 0.5D + 8.0D);
         List entities = super.worldObj.getEntitiesWithinAABB(EntityGoblin.class, bb);
         int worshipCount = 0;
         Iterator i$ = entities.iterator();

         while(i$.hasNext()) {
            Object entity = i$.next();
            if(entity instanceof EntityGoblin) {
               EntityGoblin goblin = (EntityGoblin)entity;
               if(goblin.isWorshipping()) {
                  ++worshipCount;
               } else {
                  goblin.beginWorship(this);
               }
            }
         }

         return worshipCount;
      }

      public void updateEntity() {
         super.updateEntity();
         if(!super.worldObj.isRemote && this.hasOwner()) {
            boolean PULSE_INTERVAL_IN_SECS = true;
            if(TimeUtil.secondsElapsed(5, super.ticks)) {
               int worshipCount = this.updateWorshippersAndGetLevel();
               EntityPlayerMP player = MinecraftServer.getServer().getConfigurationManager().func_152612_a(this.owner);
               if(player != null) {
                  NBTTagCompound nbtPlayer = Infusion.getNBT(player);
                  boolean WORSHIP_LEVEL_1 = true;
                  boolean WORSHIP_LEVEL_2 = true;
                  boolean WORSHIP_LEVEL_3 = true;
                  if(worshipCount >= 5) {
                     boolean GODS_SUMMON_CHANCE = true;
                     boolean RECHARGE_RADIUS_SQ = true;
                     if(player.getDistanceSq(0.5D + (double)super.xCoord, 0.5D + (double)super.yCoord, 0.5D + (double)super.zCoord) <= 4096.0D) {
                        int currentEnergy = Infusion.getCurrentEnergy(player);
                        int maxEnergy = Infusion.getMaxEnergy(player);
                        if(currentEnergy < maxEnergy) {
                           boolean ENERGY_PER_PULSE = true;
                           Infusion.setCurrentEnergy(player, Math.min(currentEnergy + 30, maxEnergy));
                           ParticleEffect.INSTANT_SPELL.send(SoundEffect.NOTE_PLING, player, 1.0D, 2.0D, 8);
                        }
                     }
                  }

                  if(worshipCount >= 10) {
                     ExtendedPlayer GODS_SUMMON_CHANCE2 = ExtendedPlayer.get(player);
                     if(GODS_SUMMON_CHANCE2 != null) {
                        GODS_SUMMON_CHANCE2.addWorship(worshipCount >= 15?1:0);
                     }
                  }

                  double GODS_SUMMON_CHANCE1 = 0.01D * (double)Config.instance().hobgoblinGodSpawnChance * 0.01D;
                  if(worshipCount >= 15 && Config.instance().hobgoblinGodSpawnChance > 0 && super.worldObj.rand.nextDouble() < GODS_SUMMON_CHANCE1) {
                     this.summonGoblinGods(player, 64.0D, 16);
                  }
               }
            }
         }

      }

      public boolean summonGoblinGods(EntityPlayer player, double detectDistance, int spawnDistance) {
         AxisAlignedBB bb2 = AxisAlignedBB.getBoundingBox((double)super.xCoord + 0.5D - detectDistance, (double)super.yCoord + 0.5D - detectDistance, (double)super.zCoord + 0.5D - detectDistance, (double)super.xCoord + 0.5D + detectDistance, (double)super.yCoord + 0.5D + detectDistance, (double)super.zCoord + 0.5D + detectDistance);
         if(super.worldObj.getEntitiesWithinAABB(EntityGoblinMog.class, bb2).size() == 0 && super.worldObj.getEntitiesWithinAABB(EntityGoblinGulg.class, bb2).size() == 0) {
            EntityCreature mog = InfusionInfernal.spawnCreature(super.worldObj, EntityGoblinMog.class, super.xCoord, super.yCoord, super.zCoord, player, 0, spawnDistance, ParticleEffect.FLAME, SoundEffect.MOB_WITHER_SPAWN);
            if(mog != null) {
               mog.onSpawnWithEgg((IEntityLivingData)null);
               mog.setAttackTarget(player);
               EntityGoblinGulg gulg = new EntityGoblinGulg(super.worldObj);
               gulg.setLocationAndAngles(mog.posX, mog.posY, mog.posZ, 0.0F, 0.0F);
               super.worldObj.spawnEntityInWorld(gulg);
               gulg.onSpawnWithEgg((IEntityLivingData)null);
               gulg.setAttackTarget(player);
               return true;
            }
         }

         return false;
      }
   }
}
