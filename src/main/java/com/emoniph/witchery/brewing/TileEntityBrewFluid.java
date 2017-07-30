package com.emoniph.witchery.brewing;

import com.emoniph.witchery.brewing.ModifiersImpact;
import com.emoniph.witchery.brewing.WitcheryBrewRegistry;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityBrewFluid extends TileEntity {

   NBTTagCompound nbtEffect;
   int color;
   int duration;
   int expansion;
   int updateCount;
   String thrower;
   private int runTicks = 0;


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

      super.worldObj.markBlockForUpdate(super.xCoord, super.yCoord, super.zCoord);
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
   }

   public boolean canUpdate() {
      return false;
   }

   public int incRunTicks() {
      return ++this.runTicks;
   }
}
