package com.emoniph.witchery.common;

import com.emoniph.witchery.common.INullSource;
import com.emoniph.witchery.common.IPowerSource;
import com.emoniph.witchery.util.Coord;
import com.emoniph.witchery.util.Log;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class PowerSources {

   private static PowerSources INSTANCE_CLIENT;
   private static PowerSources INSTANCE_SERVER;
   private final ArrayList powerSources = new ArrayList();
   private final ArrayList nullSources = new ArrayList();


   public static PowerSources instance() {
      return FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER?INSTANCE_SERVER:INSTANCE_CLIENT;
   }

   public static void initiate() {
      INSTANCE_CLIENT = new PowerSources();
      INSTANCE_SERVER = new PowerSources();
   }

   public String getDebugData() {
      StringBuilder sb = new StringBuilder();

      IPowerSource source;
      for(Iterator i$ = this.powerSources.iterator(); i$.hasNext(); sb.append(String.format("Altar (%s) [dim=%d] power=%f", new Object[]{source.getLocation(), Integer.valueOf(source.getWorld().provider.dimensionId), Float.valueOf(source.getCurrentPower())}))) {
         source = (IPowerSource)i$.next();
         if(sb.length() > 0) {
            sb.append('\n');
         }
      }

      return sb.length() > 0?sb.insert(0, "** ALTARS **\n").toString():"No power sources";
   }

   public void registerPowerSource(IPowerSource powerSource) {
      if(!this.powerSources.contains(powerSource)) {
         try {
            Iterator e = this.powerSources.iterator();

            while(e.hasNext()) {
               IPowerSource source = (IPowerSource)e.next();
               if(source == null || source.isPowerInvalid() || source.getLocation().equals(powerSource.getLocation())) {
                  e.remove();
               }
            }
         } catch (Throwable var4) {
            Log.instance().warning(var4, "Exception occured validating existing power source entries");
         }

         this.powerSources.add(powerSource);
      }

   }

   public void removePowerSource(IPowerSource powerSource) {
      if(this.powerSources.contains(powerSource)) {
         this.powerSources.remove(powerSource);
      }

      try {
         Iterator e = this.powerSources.iterator();

         while(e.hasNext()) {
            IPowerSource source = (IPowerSource)e.next();
            if(source != null && !source.isPowerInvalid()) {
               if(source.getLocation().getBlockTileEntity(source.getWorld()) != source) {
                  e.remove();
               }
            } else {
               e.remove();
            }
         }
      } catch (Throwable var4) {
         Log.instance().warning(var4, "Exception occured removing existing power source entries");
      }

   }

   public ArrayList get(World world, Coord location, int radius) {
      ArrayList nearbyPowerSources = new ArrayList();
      double radiusSq = (double)(radius * radius);
      Iterator i$ = this.powerSources.iterator();

      while(i$.hasNext()) {
         IPowerSource registeredSource = (IPowerSource)i$.next();
         PowerSources.RelativePowerSource powerSource = new PowerSources.RelativePowerSource(registeredSource, location);
         if(powerSource.isInWorld(world) && powerSource.isInRange()) {
            nearbyPowerSources.add(powerSource);
         }
      }

      Collections.sort(nearbyPowerSources, new Comparator<PowerSources.RelativePowerSource>() {
         @Override
         public int compare(PowerSources.RelativePowerSource a, PowerSources.RelativePowerSource b) {
            return Double.compare(a.distanceSq, b.distanceSq);
         }
      });
      return nearbyPowerSources;
   }

   public void registerNullSource(INullSource nullSource) {
      if(!this.nullSources.contains(nullSource)) {
         Coord newLocation = new Coord(nullSource);

         try {
            Iterator e = this.nullSources.iterator();

            while(e.hasNext()) {
               INullSource source = (INullSource)e.next();
               if(source == null || source.isPowerInvalid() || (new Coord(source)).equals(newLocation)) {
                  e.remove();
               }
            }
         } catch (Throwable var5) {
            Log.instance().warning(var5, "Exception occured validating existing null source entries");
         }

         this.nullSources.add(nullSource);
      }

   }

   public void removeNullSource(INullSource nullSource) {
      if(this.nullSources.contains(nullSource)) {
         this.nullSources.remove(nullSource);
      }

      try {
         Iterator e = this.nullSources.iterator();

         while(e.hasNext()) {
            INullSource source = (INullSource)e.next();
            if(source != null && !source.isPowerInvalid()) {
               if((new Coord(source)).getBlockTileEntity(source.getWorld()) != source) {
                  e.remove();
               }
            } else {
               e.remove();
            }
         }
      } catch (Throwable var4) {
         Log.instance().warning(var4, "Exception occured removing existing null source entries");
      }

   }

   public boolean isAreaNulled(World world, int posX, int posY, int posZ) {
      Iterator i$ = this.nullSources.iterator();

      INullSource source;
      double rangeSq;
      do {
         if(!i$.hasNext()) {
            return false;
         }

         source = (INullSource)i$.next();
         rangeSq = (double)(source.getRange() * source.getRange());
      } while(Coord.distanceSq((double)posX, (double)posY, (double)posZ, (double)source.getPosX(), (double)source.getPosY(), (double)source.getPosZ()) >= rangeSq);

      return true;
   }

   public static IPowerSource findClosestPowerSource(World world, int posX, int posY, int posZ) {
      ArrayList sources = instance() != null?instance().get(world, new Coord(posX, posY, posZ), 16):null;
      return sources != null && sources.size() > 0?((PowerSources.RelativePowerSource)sources.get(0)).source():null;
   }

   public static IPowerSource findClosestPowerSource(World world, Coord coord) {
      return findClosestPowerSource(world, coord.x, coord.y, coord.z);
   }

   public static IPowerSource findClosestPowerSource(TileEntity tile) {
      return findClosestPowerSource(tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord);
   }

   public static class RelativePowerSource {

      private final IPowerSource powerSource;
      private final double distanceSq;
      private final double rangeSq;


      public RelativePowerSource(IPowerSource powerSource, Coord relativeLocation) {
         this.powerSource = powerSource;
         this.distanceSq = relativeLocation.distanceSqTo(this.powerSource.getLocation());
         double range = (double)powerSource.getRange();
         this.rangeSq = range * range;
      }

      public boolean equals(Object obj) {
         return obj == this?true:(obj != null && obj.getClass() == this.getClass()?((PowerSources.RelativePowerSource)obj).powerSource == this.powerSource:false);
      }

      public boolean isInWorld(World world) {
         return this.powerSource.getWorld() == world;
      }

      public IPowerSource source() {
         return this.powerSource;
      }

      public boolean isInRange() {
         return this.distanceSq <= this.rangeSq;
      }
   }
}
