package com.emoniph.witchery.entity;

import com.emoniph.witchery.dimension.WorldProviderDreamWorld;
import com.emoniph.witchery.util.Config;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class EntityCorpse extends EntityLiving {

   private ThreadDownloadImageData downloadImageSkin;
   private ResourceLocation locationSkin;


   public EntityCorpse(World world) {
      super(world);
      this.setSize(1.2F, 0.5F);
   }

   public boolean canBePushed() {
      return false;
   }

   public boolean canBeCollidedWith() {
      return super.canBeCollidedWith();
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20.0D);
      this.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(1.0D);
   }

   public void moveEntity(double par1, double par3, double par5) {}

   protected void entityInit() {
      super.entityInit();
      super.dataWatcher.addObject(17, "");
   }

   protected boolean interact(EntityPlayer par1EntityPlayer) {
      return true;
   }

   public boolean attackEntityFrom(DamageSource par1DamageSource, float par2) {
      if(!super.worldObj.isRemote) {
         if(par1DamageSource.getSourceOfDamage() != null && par1DamageSource.getSourceOfDamage() instanceof EntityPlayer && ((EntityPlayer)par1DamageSource.getSourceOfDamage()).capabilities.isCreativeMode) {
            return super.attackEntityFrom(par1DamageSource, par2);
         } else {
            String username = this.getOwnerName();
            WorldServer[] arr$ = MinecraftServer.getServer().worldServers;
            int len$ = arr$.length;

            for(int i$ = 0; i$ < len$; ++i$) {
               WorldServer world = arr$[i$];
               EntityPlayer player = world.getPlayerEntityByName(username);
               if(player != null) {
                  return super.attackEntityFrom(par1DamageSource, par2);
               }
            }

            return false;
         }
      } else {
         return super.attackEntityFrom(par1DamageSource, par2);
      }
   }

   public String getCommandSenderName() {
      return this.hasCustomNameTag()?this.getCustomNameTag():StatCollector.translateToLocal("entity.witchery.body.name");
   }

   public boolean isAIEnabled() {
      return true;
   }

   public void writeEntityToNBT(NBTTagCompound nbtRoot) {
      super.writeEntityToNBT(nbtRoot);
      if(this.getOwnerName() == null) {
         nbtRoot.setString("Owner", "");
      } else {
         nbtRoot.setString("Owner", this.getOwnerName());
      }

   }

   public void readEntityFromNBT(NBTTagCompound nbtRoot) {
      super.readEntityFromNBT(nbtRoot);
      String s = nbtRoot.getString("Owner");
      if(s.length() > 0) {
         this.setOwner(s);
      }

   }

   public String getOwnerName() {
      return super.dataWatcher.getWatchableObjectString(17);
   }

   public void setOwner(String username) {
      this.func_110163_bv();
      super.dataWatcher.updateObject(17, username);
   }

   protected void setupCustomSkin() {
      String username = this.getOwnerName();
      this.locationSkin = AbstractClientPlayer.getLocationSkin(username);
      this.downloadImageSkin = AbstractClientPlayer.getDownloadImageSkin(this.locationSkin, username);
   }

   public EntityPlayer getOwnerEntity() {
      return super.worldObj.getPlayerEntityByName(this.getOwnerName());
   }

   public void onDeath(DamageSource par1DamageSource) {
      super.onDeath(par1DamageSource);
      if(!super.worldObj.isRemote) {
         String username = this.getOwnerName();
         WorldServer[] arr$ = MinecraftServer.getServer().worldServers;
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            WorldServer world = arr$[i$];
            EntityPlayer player = world.getPlayerEntityByName(username);
            if(player != null) {
               if(player.dimension == Config.instance().dimensionDreamID) {
                  WorldProviderDreamWorld.returnPlayerToOverworld(player);
               } else if(WorldProviderDreamWorld.getPlayerIsGhost(player)) {
                  WorldProviderDreamWorld.returnGhostPlayerToSpiritWorld(player);
                  WorldProviderDreamWorld.returnPlayerToOverworld(player);
               }
               break;
            }
         }
      }

   }

   public void onLivingUpdate() {
      super.onLivingUpdate();
   }

   public ResourceLocation getLocationSkin() {
      if(this.locationSkin == null) {
         this.setupCustomSkin();
      }

      return this.locationSkin != null?this.locationSkin:AbstractClientPlayer.locationStevePng;
   }
}
