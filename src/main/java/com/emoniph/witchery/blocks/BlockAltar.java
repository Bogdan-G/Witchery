package com.emoniph.witchery.blocks;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockBaseContainer;
import com.emoniph.witchery.blocks.BlockChalice;
import com.emoniph.witchery.blocks.BlockPlacedItem;
import com.emoniph.witchery.blocks.TileEntityBase;
import com.emoniph.witchery.client.particle.NaturePowerFX;
import com.emoniph.witchery.common.IPowerSource;
import com.emoniph.witchery.common.PowerSources;
import com.emoniph.witchery.util.BlockSide;
import com.emoniph.witchery.util.Coord;
import com.emoniph.witchery.util.Log;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.IIcon;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.oredict.OreDictionary;

public class BlockAltar extends BlockBaseContainer {

   @SideOnly(Side.CLIENT)
   private IIcon blockIconTop;
   @SideOnly(Side.CLIENT)
   private IIcon blockIconJoined;
   @SideOnly(Side.CLIENT)
   private IIcon blockIconTopJoined;
   private static final int ELEMENTS_IN_COMPLETE_ALTAR = 6;


   public BlockAltar() {
      super(Material.rock, BlockAltar.TileEntityAltar.class);
      this.setHardness(2.0F);
   }

   public TileEntity createNewTileEntity(World world, int metadata) {
      return new BlockAltar.TileEntityAltar();
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIcon(int side, int metadata) {
      switch(BlockAltar.NamelessClass2119415077.$SwitchMap$com$emoniph$witchery$util$BlockSide[BlockSide.fromInteger(side).ordinal()]) {
      case 1:
         return metadata == 0?this.blockIconTop:this.blockIconTopJoined;
      case 2:
         return this.blockIconTop;
      default:
         return metadata == 0?super.blockIcon:this.blockIconJoined;
      }
   }

   @SideOnly(Side.CLIENT)
   public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
      int i = Minecraft.getMinecraft().gameSettings.particleSetting;
      if(i != 2 && (i != 1 || rand.nextInt(3) != 0)) {
         if(world.getBlockMetadata(x, y, z) == 1) {
            boolean RADIUS = true;
            boolean VERT = true;
            int plantX = x - 16 + rand.nextInt(32) + 1;
            int plantY = y - 4 + rand.nextInt(8) + 1;
            int plantZ = z - 16 + rand.nextInt(32) + 1;
            Block block = world.getBlock(plantX, plantY, plantZ);
            if(block != null && (block instanceof BlockFlower || block instanceof BlockLeaves || block instanceof BlockCrops || block instanceof IPlantable)) {
               int dir_x = x - plantX;
               int dir_y = y - plantY;
               int dir_z = z - plantZ;
               double distance = Math.sqrt((double)(dir_x * dir_x + dir_y * dir_y + dir_z * dir_z));
               double speed = 0.25D;
               double factor = speed / distance;
               double vel_x = (double)dir_x * factor;
               double vel_y = (double)dir_y * factor;
               double vel_z = (double)dir_z * factor;
               NaturePowerFX sparkle = new NaturePowerFX(world, 0.5D + (double)plantX, 0.5D + (double)plantY, 0.5D + (double)plantZ);
               sparkle.setMaxAge((int)(distance / speed)).setGravity(0.0F).setScale(1.0F).setVelocity(vel_x, vel_y, vel_z);
               sparkle.setCanMove(true);
               sparkle.setRBGColorF(0.2F, 0.8F, 0.0F);
               Minecraft.getMinecraft().effectRenderer.addEffect(sparkle);
            }
         }

      }
   }

   @SideOnly(Side.CLIENT)
   public void registerBlockIcons(IIconRegister iconRegister) {
      super.blockIcon = iconRegister.registerIcon(this.getTextureName());
      this.blockIconTop = iconRegister.registerIcon(this.getTextureName() + "_top");
      this.blockIconJoined = iconRegister.registerIcon(this.getTextureName() + "_joined");
      this.blockIconTopJoined = iconRegister.registerIcon(this.getTextureName() + "_joined_top");
   }

   public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {
      BlockAltar.TileEntityAltar tileEntity = (BlockAltar.TileEntityAltar)world.getTileEntity(x, y, z);
      if(tileEntity != null && tileEntity.isValidAndUpdate()) {
         player.openGui(Witchery.instance, 0, world, x, y, z);
         return true;
      } else {
         return false;
      }
   }

   public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack) {
      super.onBlockPlacedBy(world, x, y, z, par5EntityLivingBase, par6ItemStack);
      this.updateMultiblock(world, x, y, z, (Coord)null);
   }

   public void breakBlock(World world, int x, int y, int z, Block block, int par6) {
      this.updateMultiblock(world, x, y, z, new Coord(x, y, z));
      super.breakBlock(world, x, y, z, block, par6);
   }

   public void onBlockDestroyedByExplosion(World world, int posX, int posY, int posZ, Explosion explosion) {
      BlockAltar.TileEntityAltar tileEntity = (BlockAltar.TileEntityAltar)world.getTileEntity(posX, posY, posZ);
      this.updateMultiblock(world, posX, posY, posZ, (Coord)null);
   }

   public void onNeighborBlockChange(World world, int posX, int posY, int posZ, Block block) {
      TileEntity tileEntity = world.getTileEntity(posX, posY, posZ);
      if(tileEntity != null && tileEntity instanceof BlockAltar.TileEntityAltar && !world.isRemote) {
         BlockAltar.TileEntityAltar tileEntityAltar = (BlockAltar.TileEntityAltar)tileEntity;
         tileEntityAltar.updateCoreArtefacts();
      }

      super.onNeighborBlockChange(world, posX, posY, posZ, block);
   }

   private void updateMultiblock(World world, int x, int y, int z, Coord exclude) {
      if(!world.isRemote) {
         ArrayList visited = new ArrayList();
         ArrayList toVisit = new ArrayList();
         toVisit.add(new Coord(x, y, z));
         boolean valid = true;

         Coord newCore;
         while(toVisit.size() > 0) {
            newCore = (Coord)toVisit.get(0);
            toVisit.remove(0);
            int te = 0;
            Coord[] tile = new Coord[]{newCore.north(), newCore.south(), newCore.east(), newCore.west()};
            int te1 = tile.length;

            for(int tile1 = 0; tile1 < te1; ++tile1) {
               Coord newCoord = tile[tile1];
               if(newCoord.getBlock(world) == this) {
                  if(!visited.contains(newCoord) && !toVisit.contains(newCoord)) {
                     toVisit.add(newCoord);
                  }

                  ++te;
               }
            }

            if(!newCore.equals(exclude)) {
               if(te < 2 || te > 3) {
                  valid = false;
               }

               visited.add(newCore);
            }
         }

         newCore = valid && visited.size() == 6?(Coord)visited.get(0):null;
         Iterator var17 = visited.iterator();

         while(var17.hasNext()) {
            Coord var16 = (Coord)var17.next();
            TileEntity var18 = var16.getBlockTileEntity(world);
            if(var18 != null && var18 instanceof BlockAltar.TileEntityAltar) {
               BlockAltar.TileEntityAltar var20 = (BlockAltar.TileEntityAltar)var18;
               var20.setCore(newCore);
            }
         }

         if(exclude != null) {
            TileEntity var15 = exclude.getBlockTileEntity(world);
            if(var15 != null && var15 instanceof BlockAltar.TileEntityAltar) {
               BlockAltar.TileEntityAltar var19 = (BlockAltar.TileEntityAltar)var15;
               var19.setCore((Coord)null);
            }
         }
      }

   }

   public static class TileEntityAltar extends TileEntityBase implements IPowerSource {

      private Coord core;
      private float power;
      private float maxPower;
      private int powerScale;
      private int rechargeScale;
      private int enhancementLevel;
      private int rangeScale = 1;
      long lastPowerUpdate = 0L;
      private static final int SCAN_DISTANCE = 14;
      private ArrayList extraNatureIDs = null;


      public boolean isPowerInvalid() {
         return this.isInvalid();
      }

      protected void initiate() {
         super.initiate();
         if(!super.worldObj.isRemote && this.isCore()) {
            if(super.worldObj.getBlock(super.xCoord, super.yCoord, super.zCoord) == Witchery.Blocks.ALTAR) {
               Log.instance().debug("Initiating altar tile at: " + super.xCoord + ", " + super.yCoord + ", " + super.zCoord);
               PowerSources.instance().registerPowerSource(this);
            } else {
               Log.instance().warning("Altar tile entity exists without a corresponding block at: " + super.xCoord + ", " + super.yCoord + ", " + super.zCoord);
            }
         }

      }

      public void invalidate() {
         super.invalidate();
         if(!super.worldObj.isRemote) {
            if(this.isCore()) {
               Log.instance().debug("Invalidating void bramble tile at: " + super.xCoord + ", " + super.yCoord + ", " + super.zCoord);
            }

            PowerSources.instance().removePowerSource(this);
         }

      }

      public void updateEntity() {
         super.updateEntity();
         if(!super.worldObj.isRemote) {
            float maxPowerScaled = this.maxPower * (float)this.powerScale;
            if(this.isCore()) {
               if(this.power < maxPowerScaled) {
                  float basePowerPerUpdate = 10.0F;
                  if(super.ticks % 20L == 0L) {
                     this.power = (float)((int)Math.min(this.power + 10.0F * (float)this.rechargeScale, maxPowerScaled));
                     super.worldObj.markBlockForUpdate(super.xCoord, super.yCoord, super.zCoord);
                  }
               } else if(this.power > maxPowerScaled && super.ticks % 20L == 0L) {
                  this.power = maxPowerScaled;
                  super.worldObj.markBlockForUpdate(super.xCoord, super.yCoord, super.zCoord);
               }
            }
         }

      }

      public float getRange() {
         return (float)(16 * this.rangeScale);
      }

      public int getEnhancementLevel() {
         return this.enhancementLevel;
      }

      public boolean isValidAndUpdate() {
         if(this.isValid() && !super.worldObj.isRemote) {
            TileEntity tile = this.core.getBlockTileEntity(super.worldObj);
            if(tile != null && tile instanceof BlockAltar.TileEntityAltar) {
               BlockAltar.TileEntityAltar tileEntity = (BlockAltar.TileEntityAltar)tile;
               tileEntity.updateArtefacts();
               tileEntity.updatePower(true);
               return true;
            } else {
               return false;
            }
         } else {
            return this.isValid();
         }
      }

      public void writeToNBT(NBTTagCompound nbtTag) {
         if(this.core != null) {
            this.core.setNBT(nbtTag, "Core");
         }

         if(this.isCore()) {
            nbtTag.setFloat("Power", this.power);
            nbtTag.setFloat("MaxPower", this.maxPower);
            nbtTag.setInteger("PowerScale", this.powerScale);
            nbtTag.setInteger("RechargeScale", this.rechargeScale);
            nbtTag.setInteger("RangeScale", this.rangeScale);
            nbtTag.setInteger("EnhancementLevel", this.enhancementLevel);
         }

         super.writeToNBT(nbtTag);
      }

      public void readFromNBT(NBTTagCompound nbtTag) {
         this.core = Coord.createFrom(nbtTag, "Core");
         this.power = nbtTag.getFloat("Power");
         this.maxPower = nbtTag.getFloat("MaxPower");
         this.powerScale = nbtTag.getInteger("PowerScale");
         this.rechargeScale = nbtTag.getInteger("RechargeScale");
         if(nbtTag.hasKey("RangeScale")) {
            this.rangeScale = nbtTag.getInteger("RangeScale");
         } else {
            this.rangeScale = 1;
         }

         if(nbtTag.hasKey("EnhancementLevel")) {
            this.enhancementLevel = nbtTag.getInteger("EnhancementLevel");
         } else {
            this.enhancementLevel = 0;
         }

         super.readFromNBT(nbtTag);
      }

      private void setCore(Coord coord) {
         this.core = coord;
         if(this.isCore()) {
            this.updatePower(false);
            PowerSources.instance().registerPowerSource(this);
         }

         if(coord == null) {
            PowerSources.instance().removePowerSource(this);
            this.power = 0.0F;
            this.maxPower = 0.0F;
            this.powerScale = 1;
            this.rechargeScale = 1;
            this.rangeScale = 1;
            this.enhancementLevel = 0;
         }

         super.worldObj.setBlockMetadataWithNotify(super.xCoord, super.yCoord, super.zCoord, coord != null?1:0, 3);
         super.worldObj.markBlockForUpdate(super.xCoord, super.yCoord, super.zCoord);
      }

      private boolean isCore() {
         return this.core != null && this.core.isAtPosition(this);
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

      public float getCorePower() {
         if(this.core != null) {
            TileEntity te = this.core.getBlockTileEntity(super.worldObj);
            if(te != null && te instanceof BlockAltar.TileEntityAltar) {
               BlockAltar.TileEntityAltar tileEntity = (BlockAltar.TileEntityAltar)te;
               return tileEntity.power;
            }
         }

         return 0.0F;
      }

      private void updateCoreArtefacts() {
         if(this.core != null) {
            TileEntity tile = this.core.getBlockTileEntity(super.worldObj);
            if(tile != null && tile instanceof BlockAltar.TileEntityAltar) {
               BlockAltar.TileEntityAltar tileEntity = (BlockAltar.TileEntityAltar)tile;
               tileEntity.updateArtefacts();
            }
         }

      }

      public boolean consumePower(float requiredPower) {
         if(this.core != null) {
            BlockAltar.TileEntityAltar tileEntity = (BlockAltar.TileEntityAltar)this.core.getBlockTileEntity(super.worldObj);
            if(tileEntity != null) {
               return tileEntity.consumeOurPower(requiredPower);
            }
         }

         return false;
      }

      private boolean consumeOurPower(float requiredPower) {
         if(!super.worldObj.isRemote && this.power >= requiredPower) {
            this.power -= requiredPower;
            return true;
         } else {
            return false;
         }
      }

      public float getCurrentPower() {
         if(this.core != null) {
            BlockAltar.TileEntityAltar tileEntity = (BlockAltar.TileEntityAltar)this.core.getBlockTileEntity(super.worldObj);
            if(tileEntity != null) {
               return tileEntity.getOurCurrentPower();
            }
         }

         return -1.0F;
      }

      private float getOurCurrentPower() {
         return !super.worldObj.isRemote?this.power:-2.0F;
      }

      public float getCoreMaxPower() {
         if(this.core != null) {
            TileEntity tile = this.core.getBlockTileEntity(super.worldObj);
            if(tile != null && tile instanceof BlockAltar.TileEntityAltar) {
               BlockAltar.TileEntityAltar tileEntity = (BlockAltar.TileEntityAltar)tile;
               return tileEntity.maxPower * (float)tileEntity.powerScale;
            }
         }

         return 0.0F;
      }

      public int getCoreSpeed() {
         if(this.core != null) {
            TileEntity tile = this.core.getBlockTileEntity(super.worldObj);
            if(tile != null && tile instanceof BlockAltar.TileEntityAltar) {
               BlockAltar.TileEntityAltar tileEntity = (BlockAltar.TileEntityAltar)tile;
               return tileEntity.rechargeScale;
            }
         }

         return 0;
      }

      public boolean isValid() {
         return this.core != null;
      }

      public World getWorld() {
         return super.worldObj;
      }

      public Coord getLocation() {
         return new Coord(this);
      }

      public boolean isLocationEqual(Coord location) {
         return location != null && location.isAtPosition(this);
      }

      private void updatePower(boolean throttle) {
         if(!super.worldObj.isRemote && (!throttle || super.ticks - this.lastPowerUpdate <= 0L || super.ticks - this.lastPowerUpdate > 100L)) {
            this.lastPowerUpdate = super.ticks;
            Hashtable powerObjectTable = new Hashtable();

            Iterator newMax;
            ItemStack i$;
            Block source;
            try {
               newMax = OreDictionary.getOres("treeSapling").iterator();

               while(newMax.hasNext()) {
                  i$ = (ItemStack)newMax.next();
                  source = Block.getBlockFromItem(i$.getItem());
                  if(source != null) {
                     BlockAltar.TileEntityAltar.PowerSource.createInMap(powerObjectTable, source, 4, 20);
                  }
               }
            } catch (Exception var11) {
               Log.instance().warning(var11, "Exception occurred while creating power source list for sapling ores");
            }

            try {
               newMax = OreDictionary.getOres("logWood").iterator();

               while(newMax.hasNext()) {
                  i$ = (ItemStack)newMax.next();
                  source = Block.getBlockFromItem(i$.getItem());
                  if(source != null) {
                     BlockAltar.TileEntityAltar.PowerSource.createInMap(powerObjectTable, source, 2, 50);
                  }
               }
            } catch (Exception var10) {
               Log.instance().warning(var10, "Exception occurred while creating power source list for log ores");
            }

            try {
               newMax = OreDictionary.getOres("treeLeaves").iterator();

               while(newMax.hasNext()) {
                  i$ = (ItemStack)newMax.next();
                  source = Block.getBlockFromItem(i$.getItem());
                  if(source != null) {
                     BlockAltar.TileEntityAltar.PowerSource.createInMap(powerObjectTable, source, 3, 100);
                  }
               }
            } catch (Exception var9) {
               Log.instance().warning(var9, "Exception occurred while creating power source list for leaf ores");
            }

            BlockAltar.TileEntityAltar.PowerSource.createInMap(powerObjectTable, Blocks.grass, 2, 80);
            BlockAltar.TileEntityAltar.PowerSource.createInMap(powerObjectTable, Blocks.dirt, 1, 80);
            BlockAltar.TileEntityAltar.PowerSource.createInMap(powerObjectTable, Blocks.farmland, 1, 100);
            BlockAltar.TileEntityAltar.PowerSource.createInMap(powerObjectTable, Blocks.tallgrass, 3, 50);
            BlockAltar.TileEntityAltar.PowerSource.createInMap(powerObjectTable, Blocks.yellow_flower, 4, 30);
            BlockAltar.TileEntityAltar.PowerSource.createInMap(powerObjectTable, Blocks.red_flower, 4, 30);
            BlockAltar.TileEntityAltar.PowerSource.createInMap(powerObjectTable, Blocks.wheat, 4, 20);
            BlockAltar.TileEntityAltar.PowerSource.createInMap(powerObjectTable, Blocks.water, 1, 50);
            BlockAltar.TileEntityAltar.PowerSource.createInMap(powerObjectTable, Blocks.red_mushroom, 3, 20);
            BlockAltar.TileEntityAltar.PowerSource.createInMap(powerObjectTable, Blocks.brown_mushroom, 3, 20);
            BlockAltar.TileEntityAltar.PowerSource.createInMap(powerObjectTable, Blocks.cactus, 3, 50);
            BlockAltar.TileEntityAltar.PowerSource.createInMap(powerObjectTable, Blocks.reeds, 3, 50);
            BlockAltar.TileEntityAltar.PowerSource.createInMap(powerObjectTable, Blocks.pumpkin, 4, 20);
            BlockAltar.TileEntityAltar.PowerSource.createInMap(powerObjectTable, Blocks.pumpkin_stem, 3, 20);
            BlockAltar.TileEntityAltar.PowerSource.createInMap(powerObjectTable, Blocks.brown_mushroom_block, 3, 20);
            BlockAltar.TileEntityAltar.PowerSource.createInMap(powerObjectTable, Blocks.red_mushroom_block, 3, 20);
            BlockAltar.TileEntityAltar.PowerSource.createInMap(powerObjectTable, Blocks.melon_block, 4, 20);
            BlockAltar.TileEntityAltar.PowerSource.createInMap(powerObjectTable, Blocks.melon_stem, 3, 20);
            BlockAltar.TileEntityAltar.PowerSource.createInMap(powerObjectTable, Blocks.vine, 2, 50);
            BlockAltar.TileEntityAltar.PowerSource.createInMap(powerObjectTable, Blocks.mycelium, 1, 80);
            BlockAltar.TileEntityAltar.PowerSource.createInMap(powerObjectTable, Blocks.dragon_egg, 250, 1);
            BlockAltar.TileEntityAltar.PowerSource.createInMap(powerObjectTable, Witchery.Blocks.DEMON_HEART, 40, 2);
            BlockAltar.TileEntityAltar.PowerSource.createInMap(powerObjectTable, Blocks.cocoa, 3, 20);
            BlockAltar.TileEntityAltar.PowerSource.createInMap(powerObjectTable, Blocks.carrots, 4, 20);
            BlockAltar.TileEntityAltar.PowerSource.createInMap(powerObjectTable, Blocks.potatoes, 4, 20);
            BlockAltar.TileEntityAltar.PowerSource.createInMap(powerObjectTable, Witchery.Blocks.CROP_BELLADONNA, 4, 20);
            BlockAltar.TileEntityAltar.PowerSource.createInMap(powerObjectTable, Witchery.Blocks.CROP_MANDRAKE, 4, 20);
            BlockAltar.TileEntityAltar.PowerSource.createInMap(powerObjectTable, Witchery.Blocks.CROP_ARTICHOKE, 4, 20);
            BlockAltar.TileEntityAltar.PowerSource.createInMap(powerObjectTable, Witchery.Blocks.CROP_SNOWBELL, 4, 20);
            BlockAltar.TileEntityAltar.PowerSource.createInMap(powerObjectTable, Witchery.Blocks.EMBER_MOSS, 4, 20);
            BlockAltar.TileEntityAltar.PowerSource.createInMap(powerObjectTable, Witchery.Blocks.LEAVES, 4, 50);
            BlockAltar.TileEntityAltar.PowerSource.createInMap(powerObjectTable, Witchery.Blocks.LOG, 3, 100);
            BlockAltar.TileEntityAltar.PowerSource.createInMap(powerObjectTable, Witchery.Blocks.SPANISH_MOSS, 3, 20);
            BlockAltar.TileEntityAltar.PowerSource.createInMap(powerObjectTable, Witchery.Blocks.GLINT_WEED, 2, 20);
            BlockAltar.TileEntityAltar.PowerSource.createInMap(powerObjectTable, Witchery.Blocks.CRITTER_SNARE, 2, 10);
            BlockAltar.TileEntityAltar.PowerSource.createInMap(powerObjectTable, Witchery.Blocks.BLOOD_ROSE, 2, 10);
            BlockAltar.TileEntityAltar.PowerSource.createInMap(powerObjectTable, Witchery.Blocks.GRASSPER, 2, 10);
            BlockAltar.TileEntityAltar.PowerSource.createInMap(powerObjectTable, Witchery.Blocks.WISPY_COTTON, 3, 20);
            BlockAltar.TileEntityAltar.PowerSource.createInMap(powerObjectTable, Witchery.Blocks.INFINITY_EGG, 1000, 1);
            Block var14;
            if(this.extraNatureIDs == null) {
               try {
                  this.extraNatureIDs = new ArrayList();
                  newMax = Block.blockRegistry.iterator();

                  while(newMax.hasNext()) {
                     var14 = (Block)newMax.next();
                     if((var14 instanceof BlockFlower || var14 instanceof BlockCrops) && !powerObjectTable.containsKey(var14)) {
                        this.extraNatureIDs.add(var14);
                        Log.instance().debug(var14.getUnlocalizedName());
                     }
                  }
               } catch (Exception var8) {
                  Log.instance().warning(var8, "Exception occurred while creating power source list for other mod flowers and crops");
               }
            }

            newMax = this.extraNatureIDs.iterator();

            while(newMax.hasNext()) {
               var14 = (Block)newMax.next();
               BlockAltar.TileEntityAltar.PowerSource.createInMap(powerObjectTable, var14, 2, 4);
            }

            for(int var12 = super.yCoord - 14; var12 <= super.yCoord + 14; ++var12) {
               for(int var16 = super.zCoord + 14; var16 >= super.zCoord - 14; --var16) {
                  for(int var15 = super.xCoord - 14; var15 <= super.xCoord + 14; ++var15) {
                     Block block = super.worldObj.getBlock(var15, var12, var16);
                     BlockAltar.TileEntityAltar.PowerSource source1 = (BlockAltar.TileEntityAltar.PowerSource)powerObjectTable.get(block);
                     if(source1 != null) {
                        BlockAltar.TileEntityAltar.PowerSource.access$204(source1);
                     }
                  }
               }
            }

            float var13 = 0.0F;

            BlockAltar.TileEntityAltar.PowerSource var17;
            for(Iterator var18 = powerObjectTable.values().iterator(); var18.hasNext(); var13 += (float)var17.getPower()) {
               var17 = (BlockAltar.TileEntityAltar.PowerSource)var18.next();
            }

            if(var13 != this.maxPower) {
               this.maxPower = var13;
               super.worldObj.markBlockForUpdate(super.xCoord, super.yCoord, super.zCoord);
            }
         }

      }

      private void updateArtefacts() {
         ArrayList visited = new ArrayList();
         ArrayList toVisit = new ArrayList();
         toVisit.add(new Coord(super.xCoord, super.yCoord, super.zCoord));
         boolean headfound = false;
         boolean candlefound = false;
         boolean cupfound = false;
         boolean knifeFound = false;
         boolean wandFound = false;
         boolean pentacleFound = false;
         boolean infinityFound = false;
         int newPowerScale = 1;
         int newRechargeScale = 1;
         int newRangeScale = 1;
         int newEnhancementLevel = 0;

         while(toVisit.size() > 0) {
            Coord coord = (Coord)toVisit.get(0);
            toVisit.remove(0);
            Coord[] offsetY = new Coord[]{coord.north(), coord.south(), coord.east(), coord.west()};
            int block = offsetY.length;

            for(int tile = 0; tile < block; ++tile) {
               Coord tileEntityChalice = offsetY[tile];
               if(tileEntityChalice.getBlock(super.worldObj) == Witchery.Blocks.ALTAR && !visited.contains(tileEntityChalice) && !toVisit.contains(tileEntityChalice)) {
                  toVisit.add(tileEntityChalice);
               }
            }

            visited.add(coord);
            boolean var19 = true;
            Block var20 = coord.getBlock(super.worldObj, 0, 1, 0);
            TileEntity var21;
            if(!headfound && var20 == Blocks.skull) {
               var21 = coord.getBlockTileEntity(super.worldObj, 0, 1, 0);
               if(var21 != null && var21 instanceof TileEntitySkull) {
                  TileEntitySkull var22 = (TileEntitySkull)var21;
                  switch(var22.func_145904_a()) {
                  case 0:
                     ++newRechargeScale;
                     ++newPowerScale;
                     headfound = true;
                     break;
                  case 1:
                     newRechargeScale += 2;
                     newPowerScale += 2;
                     headfound = true;
                  case 2:
                  case 4:
                  default:
                     break;
                  case 3:
                     newRechargeScale += 3;
                     newPowerScale += 3;
                     headfound = true;
                  }
               }
            } else if(!candlefound && var20 == Witchery.Blocks.CANDELABRA) {
               candlefound = true;
               newRechargeScale += 2;
            } else if(!candlefound && var20 == Blocks.torch) {
               candlefound = true;
               ++newRechargeScale;
            } else if(var20 == Witchery.Blocks.PLACED_ITEMSTACK) {
               var21 = coord.getBlockTileEntity(super.worldObj, 0, 1, 0);
               if(var21 != null && var21 instanceof BlockPlacedItem.TileEntityPlacedItem) {
                  BlockPlacedItem.TileEntityPlacedItem var23 = (BlockPlacedItem.TileEntityPlacedItem)var21;
                  if(var23.getStack() != null) {
                     if(!knifeFound && var23.getStack().getItem() == Witchery.Items.ARTHANA) {
                        knifeFound = true;
                        ++newRangeScale;
                     } else if(!wandFound && var23.getStack().getItem() == Witchery.Items.MYSTIC_BRANCH) {
                        wandFound = true;
                        ++newEnhancementLevel;
                     } else if(!pentacleFound && Witchery.Items.GENERIC.itemKobolditePentacle.isMatch(var23.getStack())) {
                        pentacleFound = true;
                     }
                  }
               }
            } else if(!cupfound && var20 == Witchery.Blocks.CHALICE) {
               cupfound = true;
               var21 = coord.getBlockTileEntity(super.worldObj, 0, 1, 0);
               if(var21 != null && var21 instanceof BlockChalice.TileEntityChalice) {
                  BlockChalice.TileEntityChalice var24 = (BlockChalice.TileEntityChalice)var21;
                  newPowerScale += var24 != null && var24.isFilled()?2:1;
               }
            } else if(!infinityFound && var20 == Witchery.Blocks.INFINITY_EGG) {
               infinityFound = true;
            }
         }

         if(pentacleFound) {
            newRechargeScale *= 2;
         }

         if(infinityFound) {
            newRechargeScale *= 10;
            newPowerScale *= 10;
         }

         if(newRechargeScale != this.rechargeScale || newPowerScale != this.powerScale || newRangeScale != this.rangeScale || newEnhancementLevel != this.enhancementLevel) {
            this.rechargeScale = newRechargeScale;
            this.powerScale = newPowerScale;
            this.rangeScale = newRangeScale;
            this.enhancementLevel = newEnhancementLevel;
            if(!super.worldObj.isRemote) {
               super.worldObj.markBlockForUpdate(super.xCoord, super.yCoord, super.zCoord);
            }
         }

      }

      static class PowerSource {

         private final Block block;
         private final int factor;
         private final int limit;
         private int count;


         public PowerSource(Block block, int factor, int limit) {
            this.block = block;
            this.factor = factor;
            this.limit = limit;
            this.count = 0;
         }

         public int getPower() {
            return Math.min(this.count, this.limit) * this.factor;
         }

         static void createInMap(Map map, Block block, int factor, int limit) {
            BlockAltar.TileEntityAltar.PowerSource source = new BlockAltar.TileEntityAltar.PowerSource(block, factor, limit);
            map.put(block, source);
         }

         // $FF: synthetic method
         static int access$204(BlockAltar.TileEntityAltar.PowerSource x0) {
            return ++x0.count;
         }
      }
   }

   // $FF: synthetic class
   static class NamelessClass2119415077 {

      // $FF: synthetic field
      static final int[] $SwitchMap$com$emoniph$witchery$util$BlockSide = new int[BlockSide.values().length];


      static {
         try {
            $SwitchMap$com$emoniph$witchery$util$BlockSide[BlockSide.TOP.ordinal()] = 1;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            $SwitchMap$com$emoniph$witchery$util$BlockSide[BlockSide.BOTTOM.ordinal()] = 2;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }
}
