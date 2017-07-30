package com.emoniph.witchery.blocks;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockBaseContainer;
import com.emoniph.witchery.blocks.TileEntityBase;
import com.emoniph.witchery.entity.EntityCorpse;
import com.emoniph.witchery.entity.EntityCovenWitch;
import com.emoniph.witchery.entity.EntityGoblin;
import com.emoniph.witchery.entity.EntityIllusion;
import com.emoniph.witchery.entity.EntitySpirit;
import com.emoniph.witchery.familiar.IFamiliar;
import com.emoniph.witchery.infusion.infusions.spirit.IFetishTile;
import com.emoniph.witchery.infusion.infusions.spirit.InfusedSpiritEffect;
import com.emoniph.witchery.item.ItemTaglockKit;
import com.emoniph.witchery.util.BlockUtil;
import com.emoniph.witchery.util.ChatUtil;
import com.emoniph.witchery.util.Config;
import com.emoniph.witchery.util.SoundEffect;
import com.emoniph.witchery.util.TimeUtil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class BlockFetish extends BlockBaseContainer {

   public BlockFetish() {
      super(Material.wood, BlockFetish.TileEntityFetish.class, BlockFetish.ClassItemBlock.class);
      this.setBlockBounds(0.2F, 0.0F, 0.2F, 0.8F, 1.0F, 0.8F);
      this.setResistance(100000.0F);
      this.setHardness(3.5F);
      this.setStepSound(Block.soundTypeWood);
   }

   public void getSubBlocks(Item item, CreativeTabs tabs, List list) {
      super.getSubBlocks(item, tabs, list);
      if(Item.getItemFromBlock(Witchery.Blocks.FETISH_SCARECROW) == item) {
         list.add(InfusedSpiritEffect.setEffect(new ItemStack(item, 1, 0), InfusedSpiritEffect.POPPET_ENHANCEMENT));
      }

      if(Item.getItemFromBlock(Witchery.Blocks.FETISH_SCARECROW) == item || Item.getItemFromBlock(Witchery.Blocks.FETISH_WITCHS_LADDER) == item) {
         list.add(InfusedSpiritEffect.setEffect(new ItemStack(item, 1, 0), InfusedSpiritEffect.SCREAMER));
      }

      if(Item.getItemFromBlock(Witchery.Blocks.FETISH_SCARECROW) == item) {
         list.add(InfusedSpiritEffect.setEffect(new ItemStack(item, 1, 0), InfusedSpiritEffect.SENTINAL));
      }

      if(Item.getItemFromBlock(Witchery.Blocks.FETISH_SCARECROW) == item) {
         list.add(InfusedSpiritEffect.setEffect(new ItemStack(item, 1, 0), InfusedSpiritEffect.TWISTER));
      }

      if(Item.getItemFromBlock(Witchery.Blocks.FETISH_SCARECROW) == item) {
         list.add(InfusedSpiritEffect.setEffect(new ItemStack(item, 1, 0), InfusedSpiritEffect.GHOST_WALKER));
      }

   }

   public void onBlockAdded(World world, int posX, int posY, int posZ) {
      super.onBlockAdded(world, posX, posY, posZ);
      BlockUtil.setBlockDefaultDirection(world, posX, posY, posZ);
   }

   public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int posX, int posY, int posZ) {
      BlockFetish.TileEntityFetish tile = (BlockFetish.TileEntityFetish)BlockUtil.getTileEntity(world, posX, posY, posZ, BlockFetish.TileEntityFetish.class);
      return this != Witchery.Blocks.FETISH_WITCHS_LADDER && (tile == null || !tile.isSpectral())?super.getCollisionBoundingBoxFromPool(world, posX, posY, posZ):null;
   }

   public float getBlockHardness(World world, int posX, int posY, int posZ) {
      BlockFetish.TileEntityFetish tile = (BlockFetish.TileEntityFetish)BlockUtil.getTileEntity(world, posX, posY, posZ, BlockFetish.TileEntityFetish.class);
      return tile != null && tile.isSpectral()?-1.0F:super.getBlockHardness(world, posX, posY, posZ);
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

      if(stack != null) {
         BlockFetish.TileEntityFetish overworld = (BlockFetish.TileEntityFetish)BlockUtil.getTileEntity(world, posX, posY, posZ, BlockFetish.TileEntityFetish.class);
         if(overworld != null) {
            NBTTagCompound tile = stack.getTagCompound();
            overworld.setEffectType(InfusedSpiritEffect.getEffectID(stack));
            if(tile != null && tile.hasKey("TileData")) {
               NBTTagCompound nbtTileData = tile.getCompoundTag("TileData");
               overworld.readSubDataFromNBT(nbtTileData);
               if(overworld.getEffectType() == 0 && InfusedSpiritEffect.getEffectID(stack) != 0) {
                  overworld.setEffectType(InfusedSpiritEffect.getEffectID(stack));
               }
            }
         }
      }

      if(!world.isRemote && world.provider.dimensionId == Config.instance().dimensionDreamID) {
         WorldServer overworld1 = MinecraftServer.getServer().worldServerForDimension(0);
         if(overworld1 != null && overworld1.isAirBlock(posX, posY, posZ)) {
            BlockUtil.setBlock(overworld1, posX, posY, posZ, this);
            this.onBlockPlacedBy(overworld1, posX, posY, posZ, player, stack);
            BlockFetish.TileEntityFetish tile1 = (BlockFetish.TileEntityFetish)BlockUtil.getTileEntity(overworld1, posX, posY, posZ, BlockFetish.TileEntityFetish.class);
            if(tile1 != null) {
               tile1.setSpectral(true);
            }
         }
      }

   }

   public void onBlockHarvested(World world, int posX, int posY, int posZ, int par5, EntityPlayer par6EntityPlayer) {
      if(par6EntityPlayer.capabilities.isCreativeMode) {
         par5 |= 8;
         world.setBlockMetadataWithNotify(posX, posY, posZ, par5, 4);
      }

      this.dropBlockAsItem(world, posX, posY, posZ, par5, 0);
      super.onBlockHarvested(world, posX, posY, posZ, par5, par6EntityPlayer);
      if(!world.isRemote && world.provider.dimensionId == Config.instance().dimensionDreamID) {
         WorldServer overworld = MinecraftServer.getServer().worldServerForDimension(0);
         if(overworld != null && BlockUtil.getBlock(overworld, posX, posY, posZ) == this) {
            overworld.setBlockToAir(posX, posY, posZ);
         }
      }

   }

   public ArrayList getDrops(World world, int x, int y, int z, int metadata, int fortune) {
      ArrayList drops = new ArrayList();
      if((metadata & 8) == 0) {
         BlockFetish.TileEntityFetish tile = (BlockFetish.TileEntityFetish)BlockUtil.getTileEntity(world, x, y, z, BlockFetish.TileEntityFetish.class);
         if(tile != null) {
            ItemStack stack = new ItemStack(tile.getBlockType());
            NBTTagCompound nbtRoot = new NBTTagCompound();
            stack.setTagCompound(nbtRoot);
            nbtRoot.setByte("BlockColor", (byte)tile.getColor());
            InfusedSpiritEffect.setEffectID(stack, tile.getEffectType());
            NBTTagCompound nbtTileData = new NBTTagCompound();
            tile.writeSubDataToNBT(nbtTileData);
            nbtRoot.setTag("TileData", nbtTileData);
            drops.add(stack);
         }
      }

      return drops;
   }

   public int getRenderType() {
      return this == Witchery.Blocks.FETISH_WITCHS_LADDER?1:super.getRenderType();
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public boolean renderAsNormalBlock() {
      return false;
   }

   @SideOnly(Side.CLIENT)
   public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
      return this == Witchery.Blocks.FETISH_WITCHS_LADDER;
   }

   public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
      return null;
   }

   public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
      BlockFetish.TileEntityFetish tile = (BlockFetish.TileEntityFetish)BlockUtil.getTileEntity(world, x, y, z, BlockFetish.TileEntityFetish.class);
      if(tile != null && player != null) {
         ItemStack stack = player.getHeldItem();
         if(stack != null) {
            if(!tile.isSpectral()) {
               if(stack.getItem() == Items.dye) {
                  int color = BlockColored.func_150032_b(stack.getItemDamage());
                  tile.setColor(color);
                  if(!player.capabilities.isCreativeMode && --stack.stackSize == 0) {
                     player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
                  }

                  return true;
               }

               if(stack.getItem() == Items.water_bucket) {
                  tile.clearBoundEntities(stack, player);
                  SoundEffect.WATER_SWIM.playAtPlayer(world, player);
                  return true;
               }

               if(stack.getItem() == Witchery.Items.BOLINE) {
                  tile.cycleBoundMode(player);
                  return true;
               }
            }

            if(stack.getItem() == Witchery.Items.TAGLOCK_KIT) {
               tile.setBoundEntity(stack, player, tile.isSpectral());
               return true;
            }
         }
      }

      return false;
   }

   public int isProvidingWeakPower(IBlockAccess par1IBlockAccess, int posX, int posY, int posZ, int side) {
      BlockFetish.TileEntityFetish tile = (BlockFetish.TileEntityFetish)BlockUtil.getTileEntity(par1IBlockAccess, posX, posY, posZ, BlockFetish.TileEntityFetish.class);
      return tile != null?tile.getPowerLevel():super.isProvidingWeakPower(par1IBlockAccess, posX, posY, posZ, side);
   }

   public int isProvidingStrongPower(IBlockAccess par1IBlockAccess, int posX, int posY, int posZ, int side) {
      return side == 1?this.isProvidingWeakPower(par1IBlockAccess, posX, posY, posZ, side):0;
   }

   public boolean canProvidePower() {
      return true;
   }

   public static class ClassItemBlock extends ItemBlock {

      public ClassItemBlock(Block block) {
         super(block);
      }

      public String getItemStackDisplayName(ItemStack stack) {
         String s = super.getItemStackDisplayName(stack);
         String effect = InfusedSpiritEffect.getEffectDisplayName(stack);
         return effect != null?s + " (" + effect + ")":s;
      }
   }

   public static class TileEntityFetish extends TileEntityBase implements IFetishTile {

      private BlockFetish.CreatureID testID = new BlockFetish.CreatureID(new UUID(0L, 0L), "");
      boolean lastRaiseAlarm;
      long lastActivationTime;
      final int TRIGGER_WHEN_PLAYER_NOT_IN_WHITELIST = 0;
      final int TRIGGER_WHEN_PLAYER_IN_BLACKLIST = 1;
      final int TRIGGER_WHEN_CREATURE_NOT_IN_WHITELIST = 2;
      final int TRIGGER_WHEN_NOT_ALL_CREATURES_FOUND = 3;
      final int TRIGGER_WHEN_SOME_CREATURES_NOT_FOUND = 4;
      final int TRIGGER_OFF = 5;
      int alarmMode = 5;
      private Block expectedBlock;
      private boolean spectral;
      private int color = 0;
      private int effectType = 0;
      private static ArrayList groupables = null;
      private ArrayList knownPlayers = new ArrayList();
      private ArrayList knownCreatureTypes = new ArrayList();
      private ArrayList knownCreatures = new ArrayList();


      public BlockFetish.TileEntityFetish setExpectedBlock(Block block) {
         this.expectedBlock = block;
         return this;
      }

      public Block getExpectedBlock() {
         return this.expectedBlock;
      }

      public void updateEntity() {
         super.updateEntity();
         if(!super.worldObj.isRemote && TimeUtil.secondsElapsed(1, super.ticks)) {
            InfusedSpiritEffect effect = InfusedSpiritEffect.getEffect(this);
            if(effect != null && effect.getRadius() > 0.0D) {
               boolean someFound = false;
               int found = 0;
               int someLeft = 0;
               HashSet foundTypes = new HashSet();
               List entities = null;
               ArrayList foundEntities = new ArrayList();
               if(this.alarmMode != 5) {
                  double raiseAlarm = effect.getRadius();
                  double currentTime = raiseAlarm * raiseAlarm;
                  AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(0.5D + (double)super.xCoord - raiseAlarm, 0.5D + (double)super.yCoord - raiseAlarm, 0.5D + (double)super.zCoord - raiseAlarm, 0.5D + (double)super.xCoord + raiseAlarm, 0.5D + (double)super.yCoord + raiseAlarm, 0.5D + (double)super.zCoord + raiseAlarm);
                  if(this.alarmMode != 0 && this.alarmMode != 1) {
                     entities = super.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, bb);
                  } else {
                     entities = super.worldObj.getEntitiesWithinAABB(EntityPlayer.class, bb);
                  }

                  someLeft = entities.size();
                  Iterator i$ = entities.iterator();

                  while(i$.hasNext()) {
                     Object obj = i$.next();
                     if(obj instanceof EntityPlayer) {
                        EntityPlayer var18 = (EntityPlayer)obj;
                        if(this.knownPlayers != null && this.knownPlayers.contains(var18.getCommandSenderName())) {
                           someFound = true;
                           ++found;
                           --someLeft;
                           if(this.alarmMode == 1) {
                              foundEntities.add(var18);
                           }
                        } else if(this.alarmMode == 2 || this.alarmMode == 0) {
                           foundEntities.add(var18);
                        }
                     } else if(obj instanceof EntityLiving && !this.isIgnorableEntity((EntityLiving)obj)) {
                        EntityLiving creature = (EntityLiving)obj;
                        if(this.knownCreatureTypes != null && this.knownCreatureTypes.contains(creature.getCommandSenderName())) {
                           someFound = true;
                           foundTypes.add(creature.getCommandSenderName());
                           --someLeft;
                        } else {
                           this.testID.id = creature.getUniqueID();
                           if(this.knownCreatures != null && this.knownCreatures.contains(this.testID)) {
                              someFound = true;
                              ++found;
                              --someLeft;
                           } else if(this.alarmMode == 2) {
                              foundEntities.add(creature);
                           }
                        }
                     }
                  }
               }

               boolean var16 = false;
               switch(this.alarmMode) {
               case 0:
               case 2:
                  var16 = someLeft > 0;
                  break;
               case 1:
                  var16 = someFound;
                  break;
               case 3:
                  var16 = found != this.knownCreatures.size() + this.knownPlayers.size() || this.knownCreatureTypes.size() != foundTypes.size();
                  break;
               case 4:
                  var16 = !someFound;
               }

               int cooldown = effect.getCooldownTicks();
               long var17 = super.worldObj.getTotalWorldTime();
               if((cooldown == -1 || var17 > this.lastActivationTime + (long)cooldown) && effect.doUpdateEffect(this, var16, foundEntities)) {
                  this.lastActivationTime = var17;
               }

               if(this.lastRaiseAlarm != var16) {
                  this.lastRaiseAlarm = var16;
                  if(effect.isRedstoneSignaller()) {
                     BlockUtil.notifyNeighborsOfBlockChange(super.worldObj, super.xCoord, super.yCoord, super.zCoord, this.getBlockType());
                  }
               }
            }
         }

      }

      private boolean isFamiliar(Entity entity) {
         if(entity instanceof IFamiliar) {
            IFamiliar familiar = (IFamiliar)entity;
            return familiar.isFamiliar();
         } else {
            return false;
         }
      }

      private boolean isIgnorableEntity(EntityLiving entity) {
         return entity instanceof EntityCorpse || entity instanceof EntityIllusion || entity instanceof EntitySpirit || this.isFamiliar(entity);
      }

      public boolean isSpectral() {
         return this.spectral;
      }

      public void setSpectral(boolean spectral) {
         this.spectral = spectral;
         if(super.worldObj != null && !super.worldObj.isRemote) {
            super.worldObj.markBlockForUpdate(super.xCoord, super.yCoord, super.zCoord);
         }

      }

      public int getPowerLevel() {
         InfusedSpiritEffect effect = InfusedSpiritEffect.getEffect(this);
         return effect != null && effect.isRedstoneSignaller()?(this.lastRaiseAlarm?15:0):0;
      }

      public void setColor(int dyeColor) {
         this.color = dyeColor;
         if(super.worldObj != null && !super.worldObj.isRemote) {
            super.worldObj.markBlockForUpdate(super.xCoord, super.yCoord, super.zCoord);
            this.syncSpectralEntities();
         }

      }

      public int getColor() {
         return this.color;
      }

      public int getEffectType() {
         return this.effectType;
      }

      public void setEffectType(int effectID) {
         this.effectType = effectID;
         if(super.worldObj != null && !super.worldObj.isRemote) {
            super.worldObj.markBlockForUpdate(super.xCoord, super.yCoord, super.zCoord);
         }

      }

      public int getX() {
         return super.xCoord;
      }

      public int getY() {
         return super.yCoord;
      }

      public int getZ() {
         return super.zCoord;
      }

      public void syncSpectralEntities() {
         if(super.worldObj != null && !super.worldObj.isRemote && super.worldObj.provider.dimensionId == Config.instance().dimensionDreamID) {
            WorldServer overworld = MinecraftServer.getServer().worldServerForDimension(0);
            if(overworld != null && BlockUtil.getBlock(overworld, super.xCoord, super.yCoord, super.zCoord) == this.getBlockType()) {
               BlockFetish.TileEntityFetish tile = (BlockFetish.TileEntityFetish)BlockUtil.getTileEntity(overworld, super.xCoord, super.yCoord, super.zCoord, BlockFetish.TileEntityFetish.class);
               if(tile != null) {
                  NBTTagCompound nbtOurData = new NBTTagCompound();
                  this.writeSubDataToNBT(nbtOurData);
                  tile.readSubDataFromNBT(nbtOurData);
               }
            }
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

      public void readFromNBT(NBTTagCompound nbtRoot) {
         super.readFromNBT(nbtRoot);
         this.lastActivationTime = nbtRoot.getLong("LastActivation");
         if(nbtRoot.hasKey("Spectral")) {
            this.spectral = nbtRoot.getBoolean("Spectral");
         }

         this.readSubDataFromNBT(nbtRoot);
      }

      public void readSubDataFromNBT(NBTTagCompound nbtRoot) {
         if(nbtRoot.hasKey("BlockColor")) {
            this.color = nbtRoot.getByte("BlockColor");
         }

         if(nbtRoot.hasKey("EffectTypeID")) {
            this.effectType = nbtRoot.getInteger("EffectTypeID");
         }

         if(nbtRoot.hasKey("AlarmMode")) {
            this.alarmMode = nbtRoot.getInteger("AlarmMode");
         } else {
            this.alarmMode = 5;
         }

         NBTTagList nbtCreatures;
         int i;
         NBTTagCompound nbtKnownCreature;
         String playerName;
         if(nbtRoot.hasKey("KnownPlayers")) {
            nbtCreatures = nbtRoot.getTagList("KnownPlayers", 10);
            this.knownPlayers = new ArrayList();

            for(i = 0; i < nbtCreatures.tagCount(); ++i) {
               nbtKnownCreature = nbtCreatures.getCompoundTagAt(i);
               playerName = nbtKnownCreature.getString("PlayerName");
               if(playerName != null && !playerName.isEmpty()) {
                  this.knownPlayers.add(playerName);
               }
            }
         }

         if(nbtRoot.hasKey("KnownCreatureTypes")) {
            nbtCreatures = nbtRoot.getTagList("KnownCreatureTypes", 10);
            this.knownCreatureTypes = new ArrayList();

            for(i = 0; i < nbtCreatures.tagCount(); ++i) {
               nbtKnownCreature = nbtCreatures.getCompoundTagAt(i);
               playerName = nbtKnownCreature.getString("CreatureTypeName");
               if(playerName != null && !playerName.isEmpty()) {
                  this.knownCreatureTypes.add(playerName);
               }
            }
         }

         if(nbtRoot.hasKey("KnownCreatures")) {
            nbtCreatures = nbtRoot.getTagList("KnownCreatures", 10);
            this.knownCreatures = new ArrayList();

            for(i = 0; i < nbtCreatures.tagCount(); ++i) {
               nbtKnownCreature = nbtCreatures.getCompoundTagAt(i);
               playerName = nbtKnownCreature.getString("PlayerName");
               long uuidMost = nbtKnownCreature.getLong("CreatureMost");
               long uuidLeast = nbtKnownCreature.getLong("CreatureLeast");
               String cname = nbtKnownCreature.getString("CreatureName");
               if(uuidMost != 0L || uuidLeast != 0L) {
                  UUID creatureID = new UUID(uuidMost, uuidLeast);
                  this.knownCreatures.add(new BlockFetish.CreatureID(creatureID, cname));
               }
            }
         }

      }

      public void writeToNBT(NBTTagCompound nbtRoot) {
         super.writeToNBT(nbtRoot);
         nbtRoot.setLong("LastActivation", this.lastActivationTime);
         nbtRoot.setBoolean("Spectral", this.spectral);
         this.writeSubDataToNBT(nbtRoot);
      }

      public void writeSubDataToNBT(NBTTagCompound nbtRoot) {
         nbtRoot.setByte("BlockColor", (byte)this.color);
         nbtRoot.setInteger("EffectTypeID", this.effectType);
         nbtRoot.setInteger("AlarmMode", this.alarmMode);
         NBTTagList nbtCreatures;
         Iterator i$;
         String creatureID;
         NBTTagCompound nbtKnownCreature;
         if(this.knownPlayers.size() > 0) {
            nbtCreatures = new NBTTagList();
            i$ = this.knownPlayers.iterator();

            while(i$.hasNext()) {
               creatureID = (String)i$.next();
               nbtKnownCreature = new NBTTagCompound();
               nbtKnownCreature.setString("PlayerName", creatureID);
               nbtCreatures.appendTag(nbtKnownCreature);
            }

            nbtRoot.setTag("KnownPlayers", nbtCreatures);
         }

         if(this.knownCreatureTypes.size() > 0) {
            nbtCreatures = new NBTTagList();
            i$ = this.knownCreatureTypes.iterator();

            while(i$.hasNext()) {
               creatureID = (String)i$.next();
               nbtKnownCreature = new NBTTagCompound();
               nbtKnownCreature.setString("CreatureTypeName", creatureID);
               nbtCreatures.appendTag(nbtKnownCreature);
            }

            nbtRoot.setTag("KnownCreatureTypes", nbtCreatures);
         }

         if(this.knownCreatures.size() > 0) {
            nbtCreatures = new NBTTagList();
            i$ = this.knownCreatures.iterator();

            while(i$.hasNext()) {
               BlockFetish.CreatureID creatureID1 = (BlockFetish.CreatureID)i$.next();
               nbtKnownCreature = new NBTTagCompound();
               nbtKnownCreature.setLong("CreatureMost", creatureID1.id.getMostSignificantBits());
               nbtKnownCreature.setLong("CreatureLeast", creatureID1.id.getLeastSignificantBits());
               nbtKnownCreature.setString("CreatureName", creatureID1.name);
               nbtCreatures.appendTag(nbtKnownCreature);
            }

            nbtRoot.setTag("KnownCreatures", nbtCreatures);
         }

      }

      private boolean isGroupableCreature(UUID otherCreature, String creatureName) {
         if(groupables == null) {
            groupables = new ArrayList();
            this.addGroupableType(EntityVillager.class);
            this.addGroupableType(EntityGoblin.class);
            this.addGroupableType(EntitySheep.class);
            this.addGroupableType(EntityCow.class);
            this.addGroupableType(EntityMooshroom.class);
            this.addGroupableType(EntityChicken.class);
            this.addGroupableType(EntityPig.class);
            this.addGroupableType(EntityHorse.class);
            this.addGroupableType(EntityBat.class);
            this.addGroupableType(EntitySquid.class);
            this.addGroupableType(EntityCovenWitch.class);
         }

         return groupables.contains(creatureName);
      }

      public void setBoundEntity(ItemStack stack, EntityPlayer player, boolean readonly) {
         if(!super.worldObj.isRemote && stack != null) {
            ItemTaglockKit.BoundType boundEntityType = Witchery.Items.TAGLOCK_KIT.getBoundEntityType(stack, Integer.valueOf(1));
            switch(BlockFetish.NamelessClass1427998064.$SwitchMap$com$emoniph$witchery$item$ItemTaglockKit$BoundType[boundEntityType.ordinal()]) {
            case 1:
               if(!readonly) {
                  String var8 = Witchery.Items.TAGLOCK_KIT.getBoundUsername(stack, Integer.valueOf(1));
                  if(!this.knownPlayers.contains(var8)) {
                     this.knownPlayers.add(var8);
                  } else {
                     this.knownPlayers.remove(var8);
                  }

                  if(!player.capabilities.isCreativeMode && --stack.stackSize <= 0) {
                     player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
                  }

                  if(player instanceof EntityPlayerMP) {
                     ((EntityPlayerMP)player).sendContainerToPlayer(player.inventoryContainer);
                  }

                  this.syncSpectralEntities();
               }

               this.showCurrentKnownEntities(player);
               break;
            case 2:
               if(!readonly) {
                  UUID otherCreature = Witchery.Items.TAGLOCK_KIT.getBoundCreatureID(stack, Integer.valueOf(1));
                  String creatureName = Witchery.Items.TAGLOCK_KIT.getBoundEntityDisplayName(stack, Integer.valueOf(1));
                  if(this.isGroupableCreature(otherCreature, creatureName)) {
                     if(!this.knownCreatureTypes.contains(creatureName)) {
                        this.knownCreatureTypes.add(creatureName);
                     } else {
                        this.knownCreatureTypes.remove(creatureName);
                     }
                  } else {
                     BlockFetish.CreatureID creatureID = new BlockFetish.CreatureID(otherCreature, creatureName);
                     if(!this.knownCreatures.contains(creatureID)) {
                        this.knownCreatures.add(creatureID);
                     } else {
                        this.knownCreatures.remove(creatureID);
                     }
                  }

                  if(!player.capabilities.isCreativeMode && --stack.stackSize <= 0) {
                     player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
                  }

                  if(player instanceof EntityPlayerMP) {
                     ((EntityPlayerMP)player).sendContainerToPlayer(player.inventoryContainer);
                  }

                  this.syncSpectralEntities();
               }

               this.showCurrentKnownEntities(player);
               break;
            case 3:
               this.showCurrentKnownEntities(player);
            }
         }

      }

      public void clearBoundEntities(ItemStack stack, EntityPlayer player) {
         if(player != null && !player.worldObj.isRemote && stack != null) {
            this.knownCreatureTypes.clear();
            this.knownCreatures.clear();
            this.knownPlayers.clear();
            if(!player.capabilities.isCreativeMode) {
               player.inventory.setInventorySlotContents(player.inventory.currentItem, new ItemStack(Items.bucket));
            }

            if(player instanceof EntityPlayerMP) {
               ((EntityPlayerMP)player).sendContainerToPlayer(player.inventoryContainer);
            }

            this.syncSpectralEntities();
            this.showCurrentKnownEntities(player);
         }

      }

      public void cycleBoundMode(EntityPlayer player) {
         if(!super.worldObj.isRemote) {
            if(++this.alarmMode > 5) {
               this.alarmMode = 0;
            }

            this.syncSpectralEntities();
            this.showCurrentKnownEntities(player);
         }

      }

      private void addGroupableType(Class className) {
         String name = (String)EntityList.classToStringMapping.get(className);
         if(name != null) {
            String localName = StatCollector.translateToLocal("entity." + name + ".name");
            groupables.add(localName);
         }

      }

      private void showCurrentKnownEntities(EntityPlayer player) {
         StringBuffer sb = new StringBuffer();

         Iterator message;
         String key;
         for(message = this.knownPlayers.iterator(); message.hasNext(); sb.append(key)) {
            key = (String)message.next();
            if(sb.length() > 0) {
               sb.append(", ");
            }
         }

         message = this.knownCreatureTypes.iterator();

         while(message.hasNext()) {
            key = (String)message.next();
            if(sb.length() > 0) {
               sb.append(", ");
            }

            sb.append("#");
            sb.append(key);
         }

         BlockFetish.CreatureID key1;
         for(message = this.knownCreatures.iterator(); message.hasNext(); sb.append(key1.toString())) {
            key1 = (BlockFetish.CreatureID)message.next();
            if(sb.length() > 0) {
               sb.append(", ");
            }
         }

         String message1 = sb.toString();
         key = "";
         switch(this.alarmMode) {
         case 0:
            key = "tile.witchery.scarecrow.operation.playerwhitelist";
            break;
         case 1:
            key = "tile.witchery.scarecrow.operation.playerblacklist";
            break;
         case 2:
            key = "tile.witchery.scarecrow.operation.creaturewhitelist";
            break;
         case 3:
            key = "tile.witchery.scarecrow.operation.allnotfound";
            break;
         case 4:
            key = "tile.witchery.scarecrow.operation.onenotfound";
            break;
         case 5:
            key = "tile.witchery.scarecrow.operation.off";
         }

         ChatUtil.sendTranslated(player, key, new Object[]{message1});
      }

   }

   private static class CreatureID {

      UUID id;
      String name;


      public CreatureID(UUID id, String name) {
         this.id = id;
         this.name = name;
      }

      public boolean equals(Object obj) {
         return obj == null?false:(obj == this?true:(obj instanceof UUID?this.id.equals((UUID)obj):(obj.getClass() == this.getClass()?this.id.equals(((BlockFetish.CreatureID)obj).id):false)));
      }

      public String toString() {
         return this.name;
      }
   }

   // $FF: synthetic class
   static class NamelessClass1427998064 {

      // $FF: synthetic field
      static final int[] $SwitchMap$com$emoniph$witchery$item$ItemTaglockKit$BoundType = new int[ItemTaglockKit.BoundType.values().length];


      static {
         try {
            $SwitchMap$com$emoniph$witchery$item$ItemTaglockKit$BoundType[ItemTaglockKit.BoundType.PLAYER.ordinal()] = 1;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            $SwitchMap$com$emoniph$witchery$item$ItemTaglockKit$BoundType[ItemTaglockKit.BoundType.CREATURE.ordinal()] = 2;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            $SwitchMap$com$emoniph$witchery$item$ItemTaglockKit$BoundType[ItemTaglockKit.BoundType.NONE.ordinal()] = 3;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }
}
