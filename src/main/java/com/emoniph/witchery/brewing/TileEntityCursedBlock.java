package com.emoniph.witchery.brewing;

import com.emoniph.witchery.blocks.TileEntityBase;
import com.emoniph.witchery.brewing.ModifiersEffect;
import com.emoniph.witchery.brewing.ModifiersImpact;
import com.emoniph.witchery.brewing.WitcheryBrewRegistry;
import com.emoniph.witchery.util.EntityPosition;
import com.emoniph.witchery.util.EntityUtil;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;

public class TileEntityCursedBlock extends TileEntityBase {

   NBTTagCompound nbtEffect;
   int color;
   int duration;
   int expansion;
   int count;
   String thrower;


   public boolean canUpdate() {
      return false;
   }

   public void initalise(ModifiersImpact impactModifiers, NBTTagCompound nbtBrew) {
      if(nbtBrew != null) {
         this.nbtEffect = (NBTTagCompound)nbtBrew.copy();
      }

      this.color = WitcheryBrewRegistry.INSTANCE.getBrewColor(this.nbtEffect);
      this.duration = impactModifiers.lifetime >= 0?5 + impactModifiers.lifetime * impactModifiers.lifetime * 5:100;
      this.expansion = Math.min(4 + impactModifiers.extent, 10);
      if(impactModifiers.thrower != null) {
         this.thrower = impactModifiers.thrower.getCommandSenderName();
      }

      this.count = 1;
      super.worldObj.markBlockForUpdate(super.xCoord, super.yCoord, super.zCoord);
   }

   public void updateCurse(ModifiersImpact impactModifiers, NBTTagCompound nbtBrew) {
      if(nbtBrew != null) {
         if(this.nbtEffect != null && this.nbtEffect.getTagList("Items", 10).equals(nbtBrew.getTagList("Items", 10))) {
            ++this.count;
         } else {
            this.nbtEffect = nbtBrew;
            this.count = 1;
            this.color = WitcheryBrewRegistry.INSTANCE.getBrewColor(this.nbtEffect);
            this.duration = impactModifiers.lifetime >= 0?5 + impactModifiers.lifetime * impactModifiers.lifetime * 5:100;
            this.expansion = Math.min(4 + impactModifiers.extent, 10);
            if(impactModifiers.thrower != null) {
               this.thrower = impactModifiers.thrower.getCommandSenderName();
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

   public void writeToNBT(NBTTagCompound nbtRoot) {
      super.writeToNBT(nbtRoot);
      if(this.nbtEffect != null) {
         nbtRoot.setTag("Effect", this.nbtEffect);
      }

      nbtRoot.setInteger("Color", this.color);
      nbtRoot.setInteger("Duration", this.duration);
      nbtRoot.setInteger("Expansion", this.expansion);
      nbtRoot.setInteger("Count", this.count);
      if(this.thrower != null) {
         nbtRoot.setString("Thrower", this.thrower);
      }

   }

   public void readFromNBT(NBTTagCompound nbtRoot) {
      super.readFromNBT(nbtRoot);
      if(nbtRoot.hasKey("Effect")) {
         this.nbtEffect = nbtRoot.getCompoundTag("Effect");
      }

      this.color = nbtRoot.getInteger("Color");
      this.duration = nbtRoot.getInteger("Duration");
      this.expansion = nbtRoot.getInteger("Expansion");
      this.thrower = nbtRoot.getString("Thrower");
      this.count = nbtRoot.getInteger("Count");
   }

   public boolean applyToEntityAndDestroy(Entity entity) {
      if(this.nbtEffect != null && entity != null && entity instanceof EntityLivingBase) {
         EntityLivingBase living = (EntityLivingBase)entity;
         WitcheryBrewRegistry.INSTANCE.applyToEntity(entity.worldObj, living, this.nbtEffect, new ModifiersEffect(1.0D, 1.0D, false, new EntityPosition(living), false, 0, EntityUtil.playerOrFake(entity.worldObj, this.thrower)));
         ParticleEffect.SPELL_COLORED.send(SoundEffect.RANDOM_POP, living, 1.0D, 1.0D, 16);
      }

      return --this.count > 0;
   }
}
