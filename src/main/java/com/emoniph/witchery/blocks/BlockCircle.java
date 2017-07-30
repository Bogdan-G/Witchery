package com.emoniph.witchery.blocks;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockBaseContainer;
import com.emoniph.witchery.blocks.BlockGrassper;
import com.emoniph.witchery.blocks.TileEntityBase;
import com.emoniph.witchery.common.PowerSources;
import com.emoniph.witchery.entity.EntityCovenWitch;
import com.emoniph.witchery.ritual.Circle;
import com.emoniph.witchery.ritual.RiteRegistry;
import com.emoniph.witchery.ritual.RitualStep;
import com.emoniph.witchery.util.BlockUtil;
import com.emoniph.witchery.util.ChatUtil;
import com.emoniph.witchery.util.Config;
import com.emoniph.witchery.util.Coord;
import com.emoniph.witchery.util.Log;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCircle extends BlockBaseContainer {

   public BlockCircle() {
      super(Material.vine, BlockCircle.TileEntityCircle.class);
      super.registerWithCreateTab = false;
      this.setHardness(3.0F);
      this.setResistance(1000.0F);
      float f1 = 0.015625F;
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.015625F, 1.0F);
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIcon(int par1, int par2) {
      return super.blockIcon;
   }

   public void onBlockClicked(World world, int posX, int posY, int posZ, EntityPlayer player) {
      if(!world.isRemote) {
         ItemStack itemstack = player.getHeldItem();
         if(itemstack != null && (Witchery.Items.GENERIC.itemBroom.isMatch(itemstack) || Witchery.Items.GENERIC.itemBroomEnchanted.isMatch(itemstack))) {
            world.func_147480_a(posX, posY, posZ, false);
         }
      }

   }

   public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
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

   public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
      return new ItemStack(Witchery.Items.CHALK_GOLDEN);
   }

   public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, Block par5) {
      if(this.func_111046_k(par1World, par2, par3, par4)) {
         boolean flag = par1World.isBlockIndirectlyGettingPowered(par2, par3, par4);
         BlockCircle.TileEntityCircle tileCircle = (BlockCircle.TileEntityCircle)BlockUtil.getTileEntity(par1World, par2, par3, par4, BlockCircle.TileEntityCircle.class);
         if(tileCircle != null && tileCircle.previousRedstoneState != flag) {
            if(flag) {
               this.activateBlock(par1World, par2, par3, par4, (EntityPlayer)null, false);
            }

            tileCircle.previousRedstoneState = flag;
         }
      }

   }

   private boolean func_111046_k(World par1World, int par2, int par3, int par4) {
      if(!this.canBlockStay(par1World, par2, par3, par4)) {
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
      return par5 == 1;
   }

   @SideOnly(Side.CLIENT)
   public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
      int metadata = world.getBlockMetadata(x, y, z);
      if(metadata == 1) {
         double d0 = (double)((float)x + 0.4F + rand.nextFloat() * 0.2F);
         double d1 = (double)((float)y + 0.1F + rand.nextFloat() * 0.3F);
         double d2 = (double)((float)z + 0.4F + rand.nextFloat() * 0.2F);
         world.spawnParticle(ParticleEffect.REDDUST.toString(), d0, d1, d2, 0.0D, 0.0D, 0.0D);
      }

   }

   public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
      ItemStack stack = player.getHeldItem();
      this.activateBlock(world, x, y, z, player, stack != null && Witchery.Items.GENERIC.itemSeerStone.isMatch(stack));
      return true;
   }

   private void activateBlock(World world, int posX, int posY, int posZ, EntityPlayer player, boolean summonCoven) {
      BlockCircle.TileEntityCircle tileEntity = (BlockCircle.TileEntityCircle)BlockUtil.getTileEntity(world, posX, posY, posZ, BlockCircle.TileEntityCircle.class);
      if(tileEntity != null) {
         if(tileEntity.isRitualActive()) {
            tileEntity.deactivate();
         } else if(!world.isRemote) {
            if(!PowerSources.instance().isAreaNulled(world, posX, posY, posZ) && world.provider.dimensionId != Config.instance().dimensionDreamID) {
               Circle a = new Circle(16);
               Circle b = new Circle(28);
               Circle c = new Circle(40);
               Circle _ = new Circle(0);
               Circle[][] PATTERN = new Circle[][]{{_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _}, {_, _, _, _, _, c, c, c, c, c, c, c, _, _, _, _, _}, {_, _, _, _, c, _, _, _, _, _, _, _, c, _, _, _, _}, {_, _, _, c, _, _, b, b, b, b, b, _, _, c, _, _, _}, {_, _, c, _, _, b, _, _, _, _, _, b, _, _, c, _, _}, {_, c, _, _, b, _, _, a, a, a, _, _, b, _, _, c, _}, {_, c, _, b, _, _, a, _, _, _, a, _, _, b, _, c, _}, {_, c, _, b, _, a, _, _, _, _, _, a, _, b, _, c, _}, {_, c, _, b, _, a, _, _, _, _, _, a, _, b, _, c, _}, {_, c, _, b, _, a, _, _, _, _, _, a, _, b, _, c, _}, {_, c, _, b, _, _, a, _, _, _, a, _, _, b, _, c, _}, {_, c, _, _, b, _, _, a, a, a, _, _, b, _, _, c, _}, {_, _, c, _, _, b, _, _, _, _, _, b, _, _, c, _, _}, {_, _, _, c, _, _, b, b, b, b, b, _, _, c, _, _, _}, {_, _, _, _, c, _, _, _, _, _, _, _, c, _, _, _, _}, {_, _, _, _, _, c, c, c, c, c, c, c, _, _, _, _, _}, {_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _}};
               int offsetZ = (PATTERN.length - 1) / 2;

               int maxRadius;
               for(int isDaytime = 0; isDaytime < PATTERN.length - 1; ++isDaytime) {
                  int isRainPossible = posZ - offsetZ + isDaytime;
                  int isRaining = (PATTERN[isDaytime].length - 1) / 2;

                  for(int isThundering = 0; isThundering < PATTERN[isDaytime].length; ++isThundering) {
                     maxRadius = posX - isRaining + isThundering;
                     PATTERN[PATTERN.length - 1 - isDaytime][isThundering].addGlyph(world, maxRadius, posY, isRainPossible);
                  }
               }

               boolean var30 = world.isDaytime();
               boolean var29 = world.getBiomeGenForCoords(posX, posZ).canSpawnLightningBolt();
               boolean var31 = world.isRaining() && var29;
               boolean var32 = world.isThundering();
               maxRadius = PATTERN.length / 2;
               AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox((double)(posX - maxRadius), (double)posY, (double)(posZ - maxRadius), (double)(posX + maxRadius), (double)(posY + 1), (double)(posZ + maxRadius));
               ArrayList entities = new ArrayList();
               Iterator grassperStacks = world.getEntitiesWithinAABB(Entity.class, bounds).iterator();

               while(grassperStacks.hasNext()) {
                  Object radius = grassperStacks.next();
                  Entity circles = (Entity)radius;
                  entities.add(circles);
               }

               ArrayList var35 = new ArrayList();
               boolean var33 = true;

               for(int var34 = posX - 5; var34 <= posX + 5; ++var34) {
                  for(int ritualFound = posZ - 5; ritualFound <= posZ + 5; ++ritualFound) {
                     Block covenSize = world.getBlock(var34, posY, ritualFound);
                     if(covenSize == Witchery.Blocks.GRASSPER) {
                        TileEntity i$ = world.getTileEntity(var34, posY, ritualFound);
                        if(i$ != null && i$ instanceof BlockGrassper.TileEntityGrassper) {
                           BlockGrassper.TileEntityGrassper ritual = (BlockGrassper.TileEntityGrassper)i$;
                           ItemStack stack = ritual.getStackInSlot(0);
                           if(stack != null) {
                              var35.add(stack);
                           }
                        }
                     }
                  }
               }

               Circle[] var37 = new Circle[]{a, b, c};
               boolean var38 = false;
               int var36 = summonCoven?EntityCovenWitch.getCovenSize(player):0;
               Iterator var39 = RiteRegistry.instance().getRituals().iterator();

               while(var39.hasNext()) {
                  RiteRegistry.Ritual var40 = (RiteRegistry.Ritual)var39.next();
                  if(var40.isMatch(world, posX, posY, posZ, var37, entities, var35, var30, var31, var32)) {
                     tileEntity.queueRitual(var40, bounds, player, var36, summonCoven);
                     summonCoven = false;
                     var38 = true;
                  }
               }

               if(!var38 && !world.isRemote) {
                  RiteRegistry.RiteError("witchery.rite.unknownritual", player, world);
                  SoundEffect.NOTE_SNARE.playAt(world, (double)posX, (double)posY, (double)posZ);
               }

            } else {
               ChatUtil.sendTranslated(EnumChatFormatting.RED, player, "witchery.rite.nullfield", new Object[0]);
               SoundEffect.NOTE_SNARE.playAtPlayer(world, player);
            }
         }
      }
   }

   // $FF: synthetic class
   static class NamelessClass2004357897 {

      // $FF: synthetic field
      static final int[] $SwitchMap$com$emoniph$witchery$ritual$RitualStep$Result = new int[RitualStep.Result.values().length];


      static {
         try {
            $SwitchMap$com$emoniph$witchery$ritual$RitualStep$Result[RitualStep.Result.COMPLETED.ordinal()] = 1;
         } catch (NoSuchFieldError var5) {
            ;
         }

         try {
            $SwitchMap$com$emoniph$witchery$ritual$RitualStep$Result[RitualStep.Result.ABORTED.ordinal()] = 2;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            $SwitchMap$com$emoniph$witchery$ritual$RitualStep$Result[RitualStep.Result.ABORTED_REFUND.ordinal()] = 3;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            $SwitchMap$com$emoniph$witchery$ritual$RitualStep$Result[RitualStep.Result.UPKEEP.ordinal()] = 4;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            $SwitchMap$com$emoniph$witchery$ritual$RitualStep$Result[RitualStep.Result.STARTING.ordinal()] = 5;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }

   public static class TileEntityCircle extends TileEntityBase {

      public boolean previousRedstoneState;
      private final ArrayList activeRituals = new ArrayList();
      private final ArrayList upkeepRituals = new ArrayList();
      private boolean abortNext = false;


      public void writeToNBT(NBTTagCompound nbtTag) {
         super.writeToNBT(nbtTag);
         byte[] ritualIDs = new byte[this.upkeepRituals.size()];
         byte[] stages = new byte[this.upkeepRituals.size()];
         byte[] covenSizes = new byte[this.upkeepRituals.size()];
         NBTTagList nbtList = new NBTTagList();
         NBTTagList nbtLocationList = new NBTTagList();

         for(int i = 0; i < this.upkeepRituals.size(); ++i) {
            ritualIDs[i] = ((BlockCircle.TileEntityCircle.ActivatedRitual)this.upkeepRituals.get(i)).ritual.getRitualID();
            stages[i] = (byte)((BlockCircle.TileEntityCircle.ActivatedRitual)this.upkeepRituals.get(i)).getCurrentStage();
            covenSizes[i] = (byte)((BlockCircle.TileEntityCircle.ActivatedRitual)this.upkeepRituals.get(i)).covenSize;
            nbtList.appendTag(new NBTTagString(((BlockCircle.TileEntityCircle.ActivatedRitual)this.upkeepRituals.get(i)).getInitiatingPlayerName()));
            nbtLocationList.appendTag(((BlockCircle.TileEntityCircle.ActivatedRitual)this.upkeepRituals.get(i)).getLocationTag());
         }

         nbtTag.setByteArray("Rituals", ritualIDs);
         nbtTag.setByteArray("RitualStages", stages);
         nbtTag.setTag("Initiators", nbtList);
         nbtTag.setTag("Locations", nbtLocationList);
         nbtTag.setByteArray("RitualCovens", covenSizes);
      }

      public void readFromNBT(NBTTagCompound nbtTag) {
         super.readFromNBT(nbtTag);
         if(nbtTag.hasKey("Rituals") && nbtTag.hasKey("RitualStages")) {
            byte[] stages = nbtTag.getByteArray("RitualStages");
            byte[] ritualIDs = nbtTag.getByteArray("Rituals");
            Coord[] locations = new Coord[stages.length];
            if(nbtTag.hasKey("Locations")) {
               NBTTagList initators = nbtTag.getTagList("Locations", 10);

               for(int covenSizes = 0; covenSizes < Math.min(initators.tagCount(), locations.length); ++covenSizes) {
                  NBTTagCompound i = initators.getCompoundTagAt(covenSizes);
                  locations[covenSizes] = Coord.fromTagNBT(i);
               }
            }

            String[] var11 = new String[stages.length];
            int var13;
            if(nbtTag.hasKey("Initiators")) {
               NBTTagList var12 = nbtTag.getTagList("Initiators", 8);

               for(var13 = 0; var13 < Math.min(var12.tagCount(), var11.length); ++var13) {
                  String ritual = var12.getStringTagAt(var13);
                  var11[var13] = ritual != null && !ritual.isEmpty()?ritual:null;
               }
            }

            byte[] var14 = nbtTag.hasKey("RitualCovens")?nbtTag.getByteArray("RitualCovens"):null;

            for(var13 = 0; var13 < ritualIDs.length; ++var13) {
               RiteRegistry.Ritual var15 = RiteRegistry.instance().getRitual(ritualIDs[var13]);
               if(var15 != null) {
                  ArrayList ritualSteps = new ArrayList();
                  var15.addRiteSteps(ritualSteps, stages[var13]);
                  if(!ritualSteps.isEmpty()) {
                     BlockCircle.TileEntityCircle.ActivatedRitual activatedRitual = new BlockCircle.TileEntityCircle.ActivatedRitual(var15, ritualSteps, var11[var13], var14 != null?var14[var13]:0, (BlockCircle.NamelessClass2004357897)null);
                     activatedRitual.setLocation(locations[var13]);
                     this.upkeepRituals.add(activatedRitual);
                  }
               }
            }
         }

      }

      public void updateEntity() {
         super.updateEntity();
         if(!super.worldObj.isRemote) {
            if(!this.upkeepRituals.isEmpty()) {
               Iterator ritual = this.upkeepRituals.iterator();

               while(ritual.hasNext()) {
                  BlockCircle.TileEntityCircle.ActivatedRitual result = (BlockCircle.TileEntityCircle.ActivatedRitual)ritual.next();
                  RitualStep.Result i$ = ((RitualStep)result.steps.get(0)).run(super.worldObj, super.xCoord, super.yCoord, super.zCoord, super.ticks, result);
                  if(i$ != RitualStep.Result.UPKEEP && Config.instance().traceRites()) {
                     Log.instance().traceRite(String.format(" - Upkeep ritual=%s, step=%s, result=%s", new Object[]{result.ritual.getUnlocalizedName(), ((RitualStep)result.steps.get(0)).toString(), i$.toString()}));
                  }

                  switch(BlockCircle.NamelessClass2004357897.$SwitchMap$com$emoniph$witchery$ritual$RitualStep$Result[i$.ordinal()]) {
                  case 1:
                     result.steps.clear();
                     break;
                  case 2:
                  case 3:
                     result.steps.clear();
                     SoundEffect.NOTE_SNARE.playAt((TileEntity)this);
                  }
               }

               for(int var5 = this.upkeepRituals.size() - 1; var5 >= 0; --var5) {
                  if(((BlockCircle.TileEntityCircle.ActivatedRitual)this.upkeepRituals.get(var5)).steps.isEmpty()) {
                     this.upkeepRituals.remove(var5);
                  }
               }
            }

            if(!this.activeRituals.isEmpty()) {
               BlockCircle.TileEntityCircle.ActivatedRitual var6 = (BlockCircle.TileEntityCircle.ActivatedRitual)this.activeRituals.get(0);
               RitualStep.Result var7 = ((RitualStep)var6.steps.get(0)).run(super.worldObj, super.xCoord, super.yCoord, super.zCoord, super.ticks, var6);
               var6.postProcess(super.worldObj);
               if(this.abortNext) {
                  this.abortNext = false;
                  var7 = RitualStep.Result.ABORTED_REFUND;
                  this.activeRituals.clear();
               }

               if(var7 != RitualStep.Result.STARTING && Config.instance().traceRites()) {
                  Log.instance().traceRite(String.format("Active ritual=%s, step=%s, result=%s", new Object[]{var6.ritual.getUnlocalizedName(), ((RitualStep)var6.steps.get(0)).toString(), var7.toString()}));
               }

               switch(BlockCircle.NamelessClass2004357897.$SwitchMap$com$emoniph$witchery$ritual$RitualStep$Result[var7.ordinal()]) {
               case 1:
                  if(var6.steps.size() > 0) {
                     var6.steps.remove(0);
                  }

                  if(var6.steps.isEmpty()) {
                     this.activeRituals.remove(0);
                  }
                  break;
               case 2:
               case 3:
                  if(this.activeRituals.size() > 0) {
                     this.activeRituals.remove(0);
                  }

                  if(!super.worldObj.isRemote) {
                     SoundEffect.NOTE_SNARE.playAt((TileEntity)this);
                     if(var7 == RitualStep.Result.ABORTED_REFUND) {
                        Iterator var8 = var6.sacrificedItems.iterator();

                        while(var8.hasNext()) {
                           RitualStep.SacrificedItem sacrificedItem = (RitualStep.SacrificedItem)var8.next();
                           super.worldObj.spawnEntityInWorld(new EntityItem(super.worldObj, 0.5D + (double)sacrificedItem.location.x, 0.5D + (double)sacrificedItem.location.y, 0.5D + (double)sacrificedItem.location.z, sacrificedItem.itemstack));
                        }
                     }
                  }
                  break;
               case 4:
                  if(this.activeRituals.size() > 0) {
                     this.activeRituals.remove(0);
                  }

                  this.upkeepRituals.add(var6);
               case 5:
               }
            }

            if(!this.isRitualActive() && this.getBlockMetadata() != 0) {
               super.worldObj.setBlockMetadataWithNotify(super.xCoord, super.yCoord, super.zCoord, 0, 3);
            }
         }

      }

      public void deactivate() {
         if(!super.worldObj.isRemote) {
            if(this.activeRituals.size() > 0) {
               this.abortNext = true;
            }

            this.upkeepRituals.clear();
            super.worldObj.setBlockMetadataWithNotify(super.xCoord, super.yCoord, super.zCoord, 0, 3);
            SoundEffect.NOTE_SNARE.playAt((TileEntity)this);
         }

      }

      public boolean isRitualActive() {
         return !this.activeRituals.isEmpty() || !this.upkeepRituals.isEmpty();
      }

      public void queueRitual(RiteRegistry.Ritual ritual, AxisAlignedBB bounds, EntityPlayer player, int covenSize, boolean summonCoven) {
         ArrayList ritualSteps = new ArrayList();
         if(summonCoven) {
            EntityCovenWitch.summonCoven(ritualSteps, player.worldObj, player, new int[][]{{super.xCoord - 2, super.yCoord, super.zCoord - 2}, {super.xCoord + 2, super.yCoord, super.zCoord - 2}, {super.xCoord - 2, super.yCoord, super.zCoord + 2}, {super.xCoord + 2, super.yCoord, super.zCoord + 2}, {super.xCoord, super.yCoord, super.zCoord + 3}, {super.xCoord, super.yCoord, super.zCoord - 3}});
         }

         ritual.addSteps(ritualSteps, bounds);
         if(ritualSteps.size() > 0 && !super.worldObj.isRemote) {
            this.activeRituals.add(new BlockCircle.TileEntityCircle.ActivatedRitual(ritual, ritualSteps, player != null?player.getCommandSenderName():null, covenSize, (BlockCircle.NamelessClass2004357897)null));
            super.worldObj.setBlockMetadataWithNotify(super.xCoord, super.yCoord, super.zCoord, 1, 3);
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

      public static class ActivatedRitual {

         private final RiteRegistry.Ritual ritual;
         private final ArrayList steps;
         public final String playerName;
         public final ArrayList sacrificedItems;
         public final int covenSize;
         private Coord coord;


         private ActivatedRitual(RiteRegistry.Ritual ritual, ArrayList steps, String playerName, int covenSize) {
            this.sacrificedItems = new ArrayList();
            this.ritual = ritual;
            this.steps = steps;
            this.playerName = playerName;
            this.covenSize = covenSize;
         }

         public Coord getLocation() {
            return this.coord;
         }

         public NBTTagCompound getLocationTag() {
            return this.coord != null?this.coord.toTagNBT():new NBTTagCompound();
         }

         public void setLocation(Coord coord) {
            this.coord = coord;
         }

         public String getInitiatingPlayerName() {
            return this.playerName != null?this.playerName:"";
         }

         public EntityPlayer getInitiatingPlayer(World world) {
            return world.getPlayerEntityByName(this.getInitiatingPlayerName());
         }

         public void postProcess(World world) {
            for(int i = 0; i < this.sacrificedItems.size(); ++i) {
               RitualStep.SacrificedItem sacrificedItem = (RitualStep.SacrificedItem)this.sacrificedItems.get(i);
               if(sacrificedItem != null && sacrificedItem.itemstack != null) {
                  if(!this.ritual.isConsumeAttunedStoneCharged() && Witchery.Items.GENERIC.itemAttunedStoneCharged.isMatch(sacrificedItem.itemstack)) {
                     world.spawnEntityInWorld(new EntityItem(world, 0.5D + (double)sacrificedItem.location.x, 0.5D + (double)sacrificedItem.location.y, 0.5D + (double)sacrificedItem.location.z, Witchery.Items.GENERIC.itemAttunedStone.createStack()));
                     this.sacrificedItems.remove(i);
                     break;
                  }

                  if(sacrificedItem.itemstack.getItem() == Witchery.Items.ARTHANA) {
                     world.spawnEntityInWorld(new EntityItem(world, 0.5D + (double)sacrificedItem.location.x, 0.5D + (double)sacrificedItem.location.y, 0.5D + (double)sacrificedItem.location.z, sacrificedItem.itemstack));
                     this.sacrificedItems.remove(i);
                     break;
                  }

                  if(sacrificedItem.itemstack.getItem() == Witchery.Items.BOLINE) {
                     world.spawnEntityInWorld(new EntityItem(world, 0.5D + (double)sacrificedItem.location.x, 0.5D + (double)sacrificedItem.location.y, 0.5D + (double)sacrificedItem.location.z, sacrificedItem.itemstack));
                     this.sacrificedItems.remove(i);
                     break;
                  }

                  if(!this.ritual.isConsumeNecroStone() && Witchery.Items.GENERIC.itemNecroStone.isMatch(sacrificedItem.itemstack)) {
                     world.spawnEntityInWorld(new EntityItem(world, 0.5D + (double)sacrificedItem.location.x, 0.5D + (double)sacrificedItem.location.y, 0.5D + (double)sacrificedItem.location.z, sacrificedItem.itemstack));
                     this.sacrificedItems.remove(i);
                     break;
                  }
               }
            }

         }

         public int getCurrentStage() {
            return !this.steps.isEmpty()?((RitualStep)this.steps.get(0)).getCurrentStage():0;
         }

         // $FF: synthetic method
         ActivatedRitual(RiteRegistry.Ritual x0, ArrayList x1, String x2, int x3, BlockCircle.NamelessClass2004357897 x4) {
            this(x0, x1, x2, x3);
         }
      }
   }
}
