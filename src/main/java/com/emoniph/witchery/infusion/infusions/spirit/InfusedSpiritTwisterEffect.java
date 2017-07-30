package com.emoniph.witchery.infusion.infusions.spirit;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.infusion.infusions.spirit.InfusedSpiritEffect;
import com.emoniph.witchery.util.EntityUtil;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.tileentity.TileEntity;

public class InfusedSpiritTwisterEffect extends InfusedSpiritEffect {

   private static final double RANDOM_SPIN_RADIUS = 3.0D;
   private static final double RANDOM_SPIN_RADIUS_SQ = 9.0D;


   public InfusedSpiritTwisterEffect(int id, int spirits, int spectres, int banshees, int poltergeists) {
      super(id, "twister", spirits, spectres, banshees, poltergeists);
   }

   public int getCooldownTicks() {
      return 10;
   }

   public double getRadius() {
      return 8.0D;
   }

   public boolean doUpdateEffect(TileEntity tile, boolean triggered, ArrayList foundEntities) {
      if(triggered) {
         Iterator i$ = foundEntities.iterator();

         while(i$.hasNext()) {
            EntityLivingBase entity = (EntityLivingBase)i$.next();
            if(entity instanceof EntityPlayer) {
               EntityPlayer creature1 = (EntityPlayer)entity;
               if(creature1.inventory.armorItemInSlot(0) != null || creature1.inventory.armorItemInSlot(1) != null || creature1.inventory.armorItemInSlot(2) != null || creature1.inventory.armorItemInSlot(3) != null || creature1.getHeldItem() != null) {
                  double yawRadians = Math.atan2(creature1.posZ - (0.5D + (double)tile.zCoord), creature1.posX - (0.5D + (double)tile.xCoord));
                  double yaw = Math.toDegrees(yawRadians) + 180.0D;
                  double playerYaw = (double)((creature1.rotationYaw + 90.0F) % 360.0F);
                  if(playerYaw < 0.0D) {
                     playerYaw += 360.0D;
                  }

                  float rev = ((float)yaw + 90.0F) % 360.0F;
                  double ARC = 45.0D;
                  double diff = Math.abs(yaw - playerYaw);
                  if((360.0D - diff % 360.0D < 45.0D || diff % 360.0D < 45.0D) && creature1 instanceof EntityPlayerMP) {
                     S08PacketPlayerPosLook packet = new S08PacketPlayerPosLook(creature1.posX, creature1.posY, creature1.posZ, rev, creature1.rotationPitch, false);
                     Witchery.packetPipeline.sendTo((Packet)packet, creature1);
                  }
               }
            } else if(entity instanceof EntityLiving) {
               EntityLiving creature = (EntityLiving)entity;
               if(creature.getMaxHealth() < 50.0F) {
                  EntityUtil.dropAttackTarget(creature);
                  if(foundEntities.size() > 1) {
                     EntityUtil.setTarget(creature, (EntityLivingBase)foundEntities.get(tile.getWorldObj().rand.nextInt(foundEntities.size())));
                  }
               }
            }
         }
      }

      return triggered;
   }
}
