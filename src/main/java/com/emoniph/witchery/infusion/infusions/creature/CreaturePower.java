package com.emoniph.witchery.infusion.infusions.creature;

import com.emoniph.witchery.infusion.Infusion;
import com.emoniph.witchery.util.Log;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class CreaturePower {

   private final int creaturePowerID;
   private final Class creatureType;
   private static final String BEAST_POWER_KEY = "WITCBeastPower";
   private static final String BEAST_POWER_CHARGES_KEY = "WITCBeastPowerCharges";
   protected static final int DEFAULT_CHARGES_PER_SACRIFICE = 10;


   public CreaturePower(int creaturePowerID, Class creatureType) {
      this.creaturePowerID = creaturePowerID;
      this.creatureType = creatureType;
   }

   public int getCreaturePowerID() {
      return this.creaturePowerID;
   }

   public int activateCost(World world, EntityPlayer player, int elapsedTicks, MovingObjectPosition mop) {
      return 1;
   }

   public void onActivate(World world, EntityPlayer player, int elapsedTicks, MovingObjectPosition mop) {}

   public void onUpdate(World world, EntityPlayer player) {}

   public void onDamage(World world, EntityPlayer player, LivingHurtEvent event) {}

   public void onFalling(World worldObj, EntityPlayer player, LivingFallEvent event) {}

   public static int getCreaturePowerID(EntityPlayer player) {
      NBTTagCompound nbt = Infusion.getNBT(player);
      return nbt.getInteger("WITCBeastPower");
   }

   public static void setCreaturePowerID(EntityPlayer playerEntity, int beastPower, int beastCharges) {
      NBTTagCompound nbt = Infusion.getNBT(playerEntity);
      if(beastPower > 0) {
         nbt.setInteger("WITCBeastPower", beastPower);
         nbt.setInteger("WITCBeastPowerCharges", beastCharges);
      } else {
         if(nbt.hasKey("WITCBeastPower")) {
            nbt.removeTag("WITCBeastPower");
         }

         if(nbt.hasKey("WITCBeastPowerCharges")) {
            nbt.removeTag("WITCBeastPowerCharges");
         }
      }

   }

   public static int getCreaturePowerCharges(EntityPlayer player) {
      NBTTagCompound nbt = Infusion.getNBT(player);
      return nbt.hasKey("WITCBeastPower") && nbt.hasKey("WITCBeastPowerCharges")?nbt.getInteger("WITCBeastPowerCharges"):0;
   }

   public static void setCreaturePowerCharges(EntityPlayer player, int charges) {
      NBTTagCompound nbt = Infusion.getNBT(player);
      nbt.setInteger("WITCBeastPowerCharges", charges);
   }

   public IIcon getPowerBarIcon(World worldObj, EntityPlayer player) {
      return Blocks.clay.getIcon(0, 0);
   }

   public int getChargesPerSacrifice() {
      return 10;
   }

   public static class Registry {

      private static final CreaturePower.Registry INSTANCE = new CreaturePower.Registry();
      private ArrayList registry = new ArrayList();


      public static CreaturePower.Registry instance() {
         return INSTANCE;
      }

      public void add(CreaturePower power) {
         if(power.creaturePowerID == this.registry.size() + 1) {
            this.registry.add(power);
         } else if(power.creaturePowerID > this.registry.size() + 1) {
            for(int existingPower = this.registry.size(); existingPower < power.creaturePowerID; ++existingPower) {
               this.registry.add((Object)null);
            }

            this.registry.add(power);
         } else {
            CreaturePower var3 = (CreaturePower)this.registry.get(power.creaturePowerID);
            if(var3 != null) {
               Log.instance().warning(String.format("Creature power %s at id %d is being overwritten by another creature power %s.", new Object[]{var3, Integer.valueOf(power.creaturePowerID), power}));
            }

            this.registry.set(power.creaturePowerID, power);
         }

      }

      public CreaturePower get(EntityLiving creature) {
         Iterator i$ = this.registry.iterator();

         CreaturePower power;
         do {
            if(!i$.hasNext()) {
               return null;
            }

            power = (CreaturePower)i$.next();
         } while(power == null || power.creatureType != creature.getClass());

         return power;
      }

      public CreaturePower get(int creaturePowerID) {
         return (CreaturePower)this.registry.get(creaturePowerID - 1);
      }

   }
}
