package com.emoniph.witchery.brewing;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.TileEntityBase;
import com.emoniph.witchery.brewing.AltarPower;
import com.emoniph.witchery.brewing.RitualStatus;
import com.emoniph.witchery.brewing.WitcheryBrewRegistry;
import com.emoniph.witchery.brewing.action.BrewAction;
import com.emoniph.witchery.brewing.potions.PotionBase;
import com.emoniph.witchery.common.IPowerSource;
import com.emoniph.witchery.common.PowerSources;
import com.emoniph.witchery.entity.EntityCovenWitch;
import com.emoniph.witchery.entity.EntityLeonard;
import com.emoniph.witchery.util.CircleUtil;
import com.emoniph.witchery.util.EntityUtil;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.emoniph.witchery.util.TimeUtil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class TileEntityCauldron extends TileEntityBase implements IFluidHandler {

   private int ticksHeated;
   private boolean powered;
   private int ritualTicks;
   private static final int TICKS_TO_BOIL = TimeUtil.secsToTicks(5);
   private FluidTank tank = new FluidTank(3000);


   public boolean isBoiling() {
      return this.ticksHeated == TICKS_TO_BOIL;
   }

   public boolean isPowered() {
      return this.powered;
   }

   public int getRedstoneSignalStrength() {
      if(!this.isFilled()) {
         return 0;
      } else if(!this.isBoiling()) {
         return 3;
      } else {
         NBTTagCompound nbtRoot = this.tank.getFluid().tag;
         return nbtRoot != null && nbtRoot.getInteger("EffectCount") != 0?(!this.isPowered()?9:(nbtRoot.getInteger("RemainingCapacity") > 0?12:15)):6;
      }
   }

   public void updateEntity() {
      super.updateEntity();
      if(!super.worldObj.isRemote) {
         boolean sync = false;
         Block blockBelow = super.worldObj.getBlock(super.xCoord, super.yCoord - 1, super.zCoord);
         if(blockBelow == Blocks.fire && this.isFilled()) {
            if(this.ticksHeated < TICKS_TO_BOIL && ++this.ticksHeated == TICKS_TO_BOIL) {
               sync = true;
            }
         } else if(this.ticksHeated > 0) {
            this.ticksHeated = 0;
            sync = true;
         }

         boolean playerCoven;
         if(this.isBoiling() && super.ticks % 20L == 7L) {
            boolean oldRitualTicks = this.powered;
            int UPDATES_TO_ACTIVATE = this.getPower();
            if(UPDATES_TO_ACTIVATE == 0) {
               this.powered = true;
            } else if(UPDATES_TO_ACTIVATE > 0) {
               IPowerSource nbtRoot = PowerSources.findClosestPowerSource(this);
               double witchCount;
               if(this.tank.getFluid() != null && this.tank.getFluid().tag != null && this.tank.getFluidAmount() == 3000 && this.tank.getFluid().tag.getBoolean("RitualTriggered")) {
                  boolean playerWitches = CircleUtil.isSmallCircle(super.worldObj, super.xCoord, super.yCoord, super.zCoord, Witchery.Blocks.GLYPH_RITUAL);
                  playerCoven = CircleUtil.isSmallCircle(super.worldObj, super.xCoord, super.yCoord, super.zCoord, Witchery.Blocks.GLYPH_INFERNAL);
                  boolean powerSource = CircleUtil.isMediumCircle(super.worldObj, super.xCoord, super.yCoord, super.zCoord, Witchery.Blocks.GLYPH_RITUAL);
                  boolean status = CircleUtil.isMediumCircle(super.worldObj, super.xCoord, super.yCoord, super.zCoord, Witchery.Blocks.GLYPH_INFERNAL);
                  double neededPower = 1.4D;
                  if(playerWitches) {
                     neededPower -= 0.2D;
                  }

                  if(powerSource) {
                     neededPower -= 0.2D;
                  }

                  if(playerCoven) {
                     neededPower -= 0.37D;
                  }

                  if(status) {
                     neededPower -= 0.37D;
                  }

                  witchCount = (double)UPDATES_TO_ACTIVATE * neededPower;
               } else {
                  witchCount = (double)UPDATES_TO_ACTIVATE;
               }

               this.powered = UPDATES_TO_ACTIVATE == 0 || nbtRoot != null && witchCount <= (double)nbtRoot.getCurrentPower();
            } else {
               this.powered = false;
            }

            if(oldRitualTicks != this.powered) {
               sync = true;
            }
         }

         if(super.ticks % 10L == 8L) {
            int var28 = this.ritualTicks;
            boolean var29 = true;
            if(this.isBoiling() && this.isPowered() && this.tank.getFluid() != null && this.tank.getFluid().tag != null && this.tank.getFluidAmount() == 3000) {
               NBTTagCompound var30 = this.tank.getFluid().tag;
               if(var30.getBoolean("RitualTriggered")) {
                  ++this.ritualTicks;
                  int var31 = 0;
                  List covenWitches = EntityUtil.getEntitiesInRadius(EntityCovenWitch.class, this, 6.0D);
                  Iterator var33 = covenWitches.iterator();

                  while(var33.hasNext()) {
                     EntityCovenWitch var34 = (EntityCovenWitch)var33.next();
                     if(var34.getOwner() != null) {
                        ++var31;
                     }
                  }

                  List var32 = EntityUtil.getEntitiesInRadius(EntityPlayer.class, this, 6.0D);
                  playerCoven = false;
                  Iterator var36 = var32.iterator();

                  while(var36.hasNext()) {
                     EntityPlayer var35 = (EntityPlayer)var36.next();
                     if(EntityCovenWitch.getCovenSize(var35) > 0) {
                        if(playerCoven) {
                           ++var31;
                        } else {
                           playerCoven = true;
                        }
                     }
                  }

                  if(this.ritualTicks > 20) {
                     IPowerSource var37 = PowerSources.findClosestPowerSource(this);
                     int var39 = this.getPower();
                     boolean small = CircleUtil.isSmallCircle(super.worldObj, super.xCoord, super.yCoord, super.zCoord, Witchery.Blocks.GLYPH_RITUAL);
                     boolean smallPower = CircleUtil.isSmallCircle(super.worldObj, super.xCoord, super.yCoord, super.zCoord, Witchery.Blocks.GLYPH_INFERNAL);
                     boolean medium = CircleUtil.isMediumCircle(super.worldObj, super.xCoord, super.yCoord, super.zCoord, Witchery.Blocks.GLYPH_RITUAL);
                     boolean mediumPower = CircleUtil.isMediumCircle(super.worldObj, super.xCoord, super.yCoord, super.zCoord, Witchery.Blocks.GLYPH_INFERNAL);
                     double powerScale = 1.4D;
                     if(small) {
                        powerScale -= 0.2D;
                     }

                     if(medium) {
                        powerScale -= 0.2D;
                     }

                     int risk = 0;
                     if(!small && !medium) {
                        ++risk;
                     }

                     if(smallPower) {
                        ++risk;
                        powerScale -= 0.37D;
                     }

                     if(mediumPower) {
                        ++risk;
                        powerScale -= 0.37D;
                     }

                     if(var39 != 0 && (var37 == null || !var37.consumePower((float)((int)Math.floor((double)var39 * powerScale))))) {
                        if(this.ritualTicks > 21) {
                           this.drain(ForgeDirection.UNKNOWN, this.getLiquidQuantity(), true);
                           this.ritualTicks = 0;
                           this.powered = false;
                           this.ticksHeated = 0;
                           ParticleEffect.SPELL_COLORED.send(SoundEffect.RANDOM_POP, super.worldObj, (double)super.xCoord + 0.5D, (double)super.yCoord + 0.6D, (double)super.zCoord + 0.5D, 0.5D, 1.0D, 8, -7829504);
                           sync = true;
                        }
                     } else {
                        double R = 16.0D;
                        AxisAlignedBB bb = AxisAlignedBB.getBoundingBox((double)super.xCoord - R, (double)super.yCoord - R, (double)super.zCoord - R, (double)super.xCoord + R, (double)super.yCoord + R, (double)super.zCoord + R);
                        List leonards = super.worldObj.getEntitiesWithinAABB(EntityLeonard.class, bb);
                        boolean lenny = false;
                        Iterator failed = leonards.iterator();

                        while(failed.hasNext()) {
                           EntityLeonard nbtItems = (EntityLeonard)failed.next();
                           if(!nbtItems.isDead && nbtItems.getHealth() > 1.0F) {
                              lenny = true;
                              break;
                           }
                        }

                        RitualStatus var38 = WitcheryBrewRegistry.INSTANCE.updateRitual(MinecraftServer.getServer(), super.worldObj, super.xCoord, super.yCoord, super.zCoord, var30, var31, this.ritualTicks - 20, lenny);
                        boolean var40 = false;
                        switch(TileEntityCauldron.NamelessClass670578566.$SwitchMap$com$emoniph$witchery$brewing$RitualStatus[var38.ordinal()]) {
                        case 1:
                           this.checkForMisfortune(risk + (lenny?1:0), var39);
                           break;
                        case 2:
                           ParticleEffect.SPELL.send(SoundEffect.RANDOM_FIZZ, super.worldObj, (double)super.xCoord + 0.5D, (double)super.yCoord + 0.6D, (double)super.zCoord + 0.5D, 0.5D, 1.0D, 8);
                           this.drain(ForgeDirection.UNKNOWN, this.getLiquidQuantity(), true);
                           this.ritualTicks = 0;
                           this.powered = false;
                           this.ticksHeated = 0;
                           sync = true;
                           this.checkForMisfortune(risk + (lenny?1:0), var39);
                           break;
                        case 3:
                           ParticleEffect.SPELL_COLORED.send(SoundEffect.RANDOM_POP, super.worldObj, (double)super.xCoord + 0.5D, (double)super.yCoord + 0.6D, (double)super.zCoord + 0.5D, 0.5D, 1.0D, 8, -16742400);
                           var40 = true;
                           break;
                        case 4:
                           ParticleEffect.SPELL_COLORED.send(SoundEffect.RANDOM_POP, super.worldObj, (double)super.xCoord + 0.5D, (double)super.yCoord + 0.6D, (double)super.zCoord + 0.5D, 0.5D, 1.0D, 8, -16777080);
                           var40 = true;
                           break;
                        case 5:
                           ParticleEffect.SPELL_COLORED.send(SoundEffect.RANDOM_POP, super.worldObj, (double)super.xCoord + 0.5D, (double)super.yCoord + 0.6D, (double)super.zCoord + 0.5D, 0.5D, 1.0D, 8, -7864320);
                           var40 = true;
                           break;
                        case 6:
                           ParticleEffect.SPELL_COLORED.send(SoundEffect.RANDOM_POP, super.worldObj, (double)super.xCoord + 0.5D, (double)super.yCoord + 0.6D, (double)super.zCoord + 0.5D, 0.5D, 1.0D, 8, -7864184);
                           var40 = true;
                        }

                        if(var40) {
                           NBTTagList var41 = var30.getTagList("Items", 10);
                           ItemStack stack = ItemStack.loadItemStackFromNBT(var41.getCompoundTagAt(var41.tagCount() - 1));
                           var41.removeTag(var41.tagCount() - 1);
                           EntityUtil.spawnEntityInWorld(super.worldObj, new EntityItem(super.worldObj, (double)super.xCoord, (double)super.yCoord, (double)super.zCoord, stack));
                           var30.setBoolean("RitualTriggered", false);
                           this.ritualTicks = 0;
                           sync = true;
                        }
                     }
                  }
               }
            } else {
               if(this.ritualTicks > 20) {
                  this.drain(ForgeDirection.UNKNOWN, this.getLiquidQuantity(), true);
                  this.ritualTicks = 0;
                  this.powered = false;
                  this.ticksHeated = 0;
                  ParticleEffect.SPELL_COLORED.send(SoundEffect.RANDOM_POP, super.worldObj, (double)super.xCoord + 0.5D, (double)super.yCoord + 0.6D, (double)super.zCoord + 0.5D, 0.5D, 1.0D, 8, -7829504);
                  sync = true;
               }

               this.ritualTicks = 0;
            }

            if(this.ritualTicks != var28) {
               sync = true;
            }
         }

         if(sync) {
            this.markBlockForUpdate(true);
         }
      }

   }

   private void checkForMisfortune(int risk, int power) {
      if(risk > 0 && power > 0) {
         double roll = super.worldObj.rand.nextDouble() * (1.0D + (double)(risk - 1) * 0.2D);
         if(roll < 0.5D) {
            return;
         }

         if(roll < 0.75D) {
            this.applyToAllNear(new PotionEffect(Potion.moveSlowdown.id, TimeUtil.secsToTicks(60), 1));
         } else if(roll < 0.9D) {
            this.applyToAllNear(new PotionEffect(Witchery.Potions.PARALYSED.id, TimeUtil.secsToTicks(20), 2));
         } else if(roll < 0.98D) {
            this.applyToAllNear(new PotionEffect(Witchery.Potions.INSANITY.id, TimeUtil.minsToTicks(3), 2));
         } else {
            this.applyToAllNear(new PotionEffect(Witchery.Potions.PARALYSED.id, TimeUtil.secsToTicks(10), 2));

            for(int i = 0; i < super.worldObj.rand.nextInt(3) + 2; ++i) {
               this.spawnBolt(super.worldObj, super.xCoord, super.yCoord, super.zCoord, 1, 4);
            }
         }
      }

   }

   private void applyToAllNear(PotionEffect effect) {
      double R = 16.0D;
      double RSq = 256.0D;
      AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(0.5D + (double)super.xCoord - 16.0D, (double)(super.yCoord - 2), 0.5D + (double)super.zCoord - 16.0D, 0.5D + (double)super.xCoord + 16.0D, (double)(super.yCoord + 4), 0.5D + (double)super.zCoord + 16.0D);
      List entities = super.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, bounds);
      Iterator i$ = entities.iterator();

      while(i$.hasNext()) {
         EntityLivingBase entity = (EntityLivingBase)i$.next();
         if(entity.getDistanceSq(0.5D + (double)super.xCoord, entity.posY, 0.5D + (double)super.zCoord) < 256.0D && !(entity instanceof IMob) && !(entity instanceof IBossDisplayData)) {
            ArrayList effectsToRemove = new ArrayList();
            Collection effects = entity.getActivePotionEffects();
            Iterator i$1 = effects.iterator();

            while(i$1.hasNext()) {
               PotionEffect potion = (PotionEffect)i$1.next();
               Potion potion1 = Potion.potionTypes[effect.getPotionID()];
               if(!PotionBase.isDebuff(potion1)) {
                  effectsToRemove.add(potion1);
               }
            }

            i$1 = effectsToRemove.iterator();

            while(i$1.hasNext()) {
               Potion potion2 = (Potion)i$1.next();
               entity.removePotionEffect(potion2.id);
            }

            entity.addPotionEffect(new PotionEffect(effect));
         }
      }

   }

   private void spawnBolt(World world, int posX, int posY, int posZ, int min, int max) {
      int activeRadius = max - min;
      int ax = world.rand.nextInt(activeRadius * 2 + 1);
      if(ax > activeRadius) {
         ax += min * 2;
      }

      int x = posX - max + ax;
      int az = world.rand.nextInt(activeRadius * 2 + 1);
      if(az > activeRadius) {
         az += min * 2;
      }

      int z = posZ - max + az;
      EntityLightningBolt bolt = new EntityLightningBolt(world, (double)x, (double)posY, (double)z);
      world.addWeatherEffect(bolt);
   }

   public boolean isRitualInProgress() {
      return this.ritualTicks > 0;
   }

   public boolean addItem(BrewAction brewAction, ItemStack entityItem) {
      if(super.worldObj.isRemote) {
         return false;
      } else {
         NBTTagCompound nbtRoot;
         if(this.tank.getFluid().getFluid().getName().equals(FluidRegistry.WATER.getName())) {
            nbtRoot = new NBTTagCompound();
            nbtRoot.setTag("Items", new NBTTagList());
            if(!WitcheryBrewRegistry.INSTANCE.canAdd(nbtRoot, brewAction, this.tank.getFluidAmount() == this.tank.getCapacity())) {
               return false;
            }

            this.tank.setFluid(new FluidStack(Witchery.Fluids.BREW, this.tank.getFluid().amount));
            this.tank.getFluid().tag = nbtRoot;
         }

         if(this.tank.getFluid().tag == null) {
            this.tank.getFluid().tag = new NBTTagCompound();
         }

         nbtRoot = this.tank.getFluid().tag;
         if(!nbtRoot.hasKey("Items")) {
            nbtRoot.setTag("Items", new NBTTagList());
         }

         if(!WitcheryBrewRegistry.INSTANCE.canAdd(nbtRoot, brewAction, this.tank.getFluidAmount() == this.tank.getCapacity())) {
            return false;
         } else {
            if(!brewAction.removeWhenAddedToCauldron(super.worldObj)) {
               NBTTagList color = nbtRoot.getTagList("Items", 10);
               NBTTagCompound powerNeeded = new NBTTagCompound();
               WitcheryBrewRegistry.INSTANCE.nullifyItems(nbtRoot, color, brewAction);
               entityItem.writeToNBT(powerNeeded);
               color.appendTag(powerNeeded);
            }

            int color1 = brewAction.augmentColor(nbtRoot.getInteger("Color"));
            nbtRoot.setInteger("Color", color1);
            AltarPower powerNeeded1 = WitcheryBrewRegistry.INSTANCE.getPowerRequired(nbtRoot);
            nbtRoot.setInteger("Power", powerNeeded1.getPower());
            nbtRoot.setString("BrewName", WitcheryBrewRegistry.INSTANCE.getBrewName(nbtRoot));
            WitcheryBrewRegistry.INSTANCE.updateBrewInformation(nbtRoot);
            nbtRoot.setInteger("BrewDrinkSpeed", WitcheryBrewRegistry.INSTANCE.getBrewDrinkSpeed(nbtRoot));
            if(brewAction.createsSplash()) {
               nbtRoot.setBoolean("Splash", true);
            }

            if(brewAction.triggersRitual()) {
               nbtRoot.setBoolean("RitualTriggered", true);
               this.ritualTicks = 0;
            }

            this.markBlockForUpdate(true);
            return true;
         }
      }
   }

   public boolean explodeBrew(EntityPlayer nearestPlayer) {
      if(!super.worldObj.isRemote && nearestPlayer != null) {
         if(this.tank.getFluid() == null) {
            return false;
         } else if(this.tank.getFluid().getFluid().getName().equals(FluidRegistry.WATER.getName())) {
            return false;
         } else if(this.tank.getFluid().tag == null) {
            return false;
         } else {
            NBTTagCompound nbtRoot = this.tank.getFluid().tag;
            if(!nbtRoot.hasKey("Items")) {
               return false;
            } else {
               WitcheryBrewRegistry.INSTANCE.explodeBrew(super.worldObj, nbtRoot, nearestPlayer, 0.5D + (double)super.xCoord, 1.5D + (double)super.yCoord, 0.5D + (double)super.zCoord);
               return true;
            }
         }
      } else {
         return false;
      }
   }

   @SideOnly(Side.CLIENT)
   public int getColor() {
      NBTTagCompound nbtRoot = this.tank.getFluid() != null?this.tank.getFluid().tag:null;
      if(nbtRoot != null) {
         int color = nbtRoot.getInteger("Color");
         return color;
      } else {
         return -1;
      }
   }

   public int getPower() {
      NBTTagCompound nbtRoot = this.tank.getFluid() != null?this.tank.getFluid().tag:null;
      if(nbtRoot != null) {
         int power = nbtRoot.getInteger("Power");
         return power;
      } else {
         return -1;
      }
   }

   public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
      int filled;
      FluidStack newStack;
      if(this.tank.getFluid() == null) {
         filled = this.tank.fill(resource, doFill);
         newStack = this.tank.getFluid();
         if(newStack != null) {
            newStack.tag = resource.tag != null?(NBTTagCompound)resource.tag.copy():null;
            this.markBlockForUpdate(false);
         }

         return filled;
      } else if(resource.isFluidEqual(this.tank.getFluid()) && (this.tank.getFluid().tag == null || resource.tag != null && this.tank.getFluid().tag.getTagList("Items", 10).equals(resource.tag.getTagList("Items", 10)))) {
         filled = this.tank.fill(resource, doFill);
         newStack = this.tank.getFluid();
         if(newStack != null) {
            newStack.tag = resource.tag != null?(NBTTagCompound)resource.tag.copy():null;
         }

         this.markBlockForUpdate(false);
         return filled;
      } else {
         return 0;
      }
   }

   public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
      if(resource != null && resource.isFluidEqual(this.tank.getFluid())) {
         NBTTagCompound oldTag = this.tank.getFluid() != null && this.tank.getFluid().tag != null?this.tank.getFluid().tag:null;
         FluidStack drained = this.tank.drain(resource.amount, doDrain);
         drained.tag = oldTag != null?(NBTTagCompound)oldTag.copy():null;
         if(this.tank.getFluidAmount() == 0) {
            this.powered = false;
         }

         this.markBlockForUpdate(false);
         return drained;
      } else {
         return null;
      }
   }

   public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
      NBTTagCompound oldTag = this.tank.getFluid() != null && this.tank.getFluid().tag != null?this.tank.getFluid().tag:null;
      FluidStack fluid = this.tank.drain(maxDrain, doDrain);
      if(fluid != null) {
         fluid.tag = oldTag != null?(NBTTagCompound)oldTag.copy():null;
      }

      if(this.tank.getFluidAmount() == 0) {
         this.powered = false;
      }

      this.markBlockForUpdate(false);
      return fluid;
   }

   public boolean canFill(ForgeDirection from, Fluid fluid) {
      return fluid == null?false:fluid.getName().equals(FluidRegistry.WATER.getName()) || fluid == Witchery.Fluids.BREW;
   }

   public boolean canDrain(ForgeDirection from, Fluid fluid) {
      return fluid == null?false:fluid.getName().equals(FluidRegistry.WATER.getName()) || fluid == Witchery.Fluids.BREW;
   }

   public FluidTankInfo[] getTankInfo(ForgeDirection from) {
      return new FluidTankInfo[]{this.tank.getInfo()};
   }

   public boolean isFilled() {
      return this.tank.getFluid() != null;
   }

   public int getMaxLiquidQuantity() {
      return this.tank.getCapacity();
   }

   public int getLiquidQuantity() {
      return this.tank.getFluidAmount();
   }

   public double getPercentFilled() {
      return (double)this.tank.getFluidAmount() / (double)this.tank.getCapacity();
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
      if(this.tank.getFluidAmount() > 0) {
         this.tank.drain(this.tank.getFluidAmount(), true);
      }

      this.tank.readFromNBT(nbtRoot);
      this.ticksHeated = nbtRoot.getInteger("TicksHeated");
      this.powered = nbtRoot.getBoolean("Powered");
      this.ritualTicks = nbtRoot.getInteger("RitualTicks");
   }

   public void writeToNBT(NBTTagCompound nbtRoot) {
      super.writeToNBT(nbtRoot);
      this.tank.writeToNBT(nbtRoot);
      nbtRoot.setInteger("TicksHeated", this.ticksHeated);
      nbtRoot.setBoolean("Powered", this.powered);
      nbtRoot.setInteger("RitualTicks", this.ritualTicks);
   }

   public int getRitualSeconds() {
      return this.ritualTicks;
   }


   // $FF: synthetic class
   static class NamelessClass670578566 {

      // $FF: synthetic field
      static final int[] $SwitchMap$com$emoniph$witchery$brewing$RitualStatus = new int[RitualStatus.values().length];


      static {
         try {
            $SwitchMap$com$emoniph$witchery$brewing$RitualStatus[RitualStatus.ONGOING.ordinal()] = 1;
         } catch (NoSuchFieldError var6) {
            ;
         }

         try {
            $SwitchMap$com$emoniph$witchery$brewing$RitualStatus[RitualStatus.COMPLETE.ordinal()] = 2;
         } catch (NoSuchFieldError var5) {
            ;
         }

         try {
            $SwitchMap$com$emoniph$witchery$brewing$RitualStatus[RitualStatus.FAILED_DISTANCE.ordinal()] = 3;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            $SwitchMap$com$emoniph$witchery$brewing$RitualStatus[RitualStatus.FAILED_NO_COVEN.ordinal()] = 4;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            $SwitchMap$com$emoniph$witchery$brewing$RitualStatus[RitualStatus.FAILED_INVALID_CIRCLES.ordinal()] = 5;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            $SwitchMap$com$emoniph$witchery$brewing$RitualStatus[RitualStatus.FAILED.ordinal()] = 6;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }
}
