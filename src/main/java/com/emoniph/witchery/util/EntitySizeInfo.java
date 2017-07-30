package com.emoniph.witchery.util;

import com.emoniph.witchery.common.ExtendedPlayer;
import com.emoniph.witchery.util.TransformCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class EntitySizeInfo {

   public final float defaultWidth;
   public final float defaultHeight;
   public final float eyeHeight;
   public final float stepSize;
   public final boolean isDefault;
   public final TransformCreature creature;


   public EntitySizeInfo(EntityLivingBase entity) {
      if(entity instanceof EntityPlayer) {
         EntityPlayer nbtEntity = (EntityPlayer)entity;
         this.creature = ExtendedPlayer.get(nbtEntity).getCreatureType();
         NBTTagCompound nbtEntity1 = entity.getEntityData();
         switch(EntitySizeInfo.NamelessClass1729390445.$SwitchMap$com$emoniph$witchery$util$TransformCreature[this.creature.ordinal()]) {
         case 1:
         case 2:
         default:
            this.isDefault = true;
            this.defaultWidth = 0.6F;
            this.defaultHeight = 1.8F;
            this.stepSize = 0.5F;
            this.eyeHeight = nbtEntity.getDefaultEyeHeight();
            break;
         case 3:
            this.isDefault = false;
            this.defaultWidth = 0.6F;
            this.defaultHeight = 0.8F;
            this.eyeHeight = this.defaultHeight * 0.92F;
            this.stepSize = 1.0F;
            break;
         case 4:
            this.isDefault = true;
            this.defaultWidth = 0.6F;
            this.defaultHeight = 1.8F;
            this.eyeHeight = nbtEntity.getDefaultEyeHeight();
            this.stepSize = 1.0F;
            break;
         case 5:
            this.isDefault = false;
            this.defaultWidth = 0.3F;
            this.defaultHeight = 0.6F;
            this.eyeHeight = this.defaultHeight * 0.8F;
            this.stepSize = 0.5F;
            break;
         case 6:
            this.isDefault = false;
            this.defaultWidth = 0.3F;
            this.defaultHeight = 0.5F;
            this.eyeHeight = this.defaultHeight * 0.92F;
            this.stepSize = 0.5F;
         }
      } else {
         NBTTagCompound nbtEntity2 = entity.getEntityData();
         this.defaultWidth = nbtEntity2.getFloat("WITCInitialWidth");
         this.defaultHeight = nbtEntity2.getFloat("WITCInitialHeight");
         this.stepSize = !(entity instanceof EntityHorse) && !(entity instanceof EntityEnderman)?0.5F:1.0F;
         this.eyeHeight = 0.12F;
         this.isDefault = true;
         this.creature = TransformCreature.NONE;
      }

   }

   // $FF: synthetic class
   static class NamelessClass1729390445 {

      // $FF: synthetic field
      static final int[] $SwitchMap$com$emoniph$witchery$util$TransformCreature = new int[TransformCreature.values().length];


      static {
         try {
            $SwitchMap$com$emoniph$witchery$util$TransformCreature[TransformCreature.NONE.ordinal()] = 1;
         } catch (NoSuchFieldError var6) {
            ;
         }

         try {
            $SwitchMap$com$emoniph$witchery$util$TransformCreature[TransformCreature.PLAYER.ordinal()] = 2;
         } catch (NoSuchFieldError var5) {
            ;
         }

         try {
            $SwitchMap$com$emoniph$witchery$util$TransformCreature[TransformCreature.WOLF.ordinal()] = 3;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            $SwitchMap$com$emoniph$witchery$util$TransformCreature[TransformCreature.WOLFMAN.ordinal()] = 4;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            $SwitchMap$com$emoniph$witchery$util$TransformCreature[TransformCreature.BAT.ordinal()] = 5;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            $SwitchMap$com$emoniph$witchery$util$TransformCreature[TransformCreature.TOAD.ordinal()] = 6;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }
}
