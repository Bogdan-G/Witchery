package com.emoniph.witchery.entity.ai;

import com.emoniph.witchery.entity.EntityFamiliar;
import com.emoniph.witchery.util.ChatUtil;
import com.emoniph.witchery.util.SoundEffect;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityAIFamiliarFindDiamonds extends EntityAIBase {

   private final EntityFamiliar theFamiliar;
   private final double field_75404_b;
   private int currentTick;
   private int field_75402_d;
   private int maxSittingTicks;
   private int sitableBlockX;
   private int sitableBlockY;
   private int sitableBlockZ;


   public EntityAIFamiliarFindDiamonds(EntityFamiliar familiarEntity, double par2) {
      this.theFamiliar = familiarEntity;
      this.field_75404_b = par2;
      this.setMutexBits(5);
   }

   public boolean shouldExecute() {
      EntityLivingBase entitylivingbase = this.theFamiliar.getOwner();
      return this.theFamiliar.isTamed() && !this.theFamiliar.isSitting() && this.theFamiliar.getBlockIDToFind() != null && entitylivingbase != null && this.theFamiliar.getDistanceSqToEntity(entitylivingbase) < 100.0D && this.theFamiliar.getRNG().nextDouble() <= 0.1D && this.getNearbySitableBlockDistance();
   }

   public boolean continueExecuting() {
      EntityLivingBase entitylivingbase = this.theFamiliar.getOwner();
      return this.currentTick <= this.maxSittingTicks && this.field_75402_d <= 60 && entitylivingbase != null && this.theFamiliar.getDistanceSqToEntity(entitylivingbase) < 100.0D && this.isSittableBlock(this.theFamiliar.worldObj, this.sitableBlockX, this.sitableBlockY, this.sitableBlockZ);
   }

   public void startExecuting() {
      if(!this.theFamiliar.getNavigator().tryMoveToXYZ((double)this.sitableBlockX + 0.5D, (double)(this.sitableBlockY + 1), (double)this.sitableBlockZ + 0.5D, this.field_75404_b)) {
         this.theFamiliar.getNavigator().tryMoveToXYZ((double)this.sitableBlockX + 0.5D, this.theFamiliar.posY + 1.0D, (double)this.sitableBlockZ + 0.5D, this.field_75404_b);
      }

      this.currentTick = 0;
      this.field_75402_d = 0;
      this.maxSittingTicks = this.theFamiliar.getRNG().nextInt(this.theFamiliar.getRNG().nextInt(1200) + 1200) + 1200;
      this.theFamiliar.func_70907_r().setSitting(false);
   }

   public void resetTask() {
      this.theFamiliar.setSitting(false);
   }

   public void updateTask() {
      ++this.currentTick;
      this.theFamiliar.func_70907_r().setSitting(false);
      if(this.theFamiliar.getDistanceSq((double)this.sitableBlockX, this.theFamiliar.posY, (double)this.sitableBlockZ) > 2.0D) {
         this.theFamiliar.setSitting(false);
         if(!this.theFamiliar.getNavigator().tryMoveToXYZ((double)this.sitableBlockX + 0.5D, (double)(this.sitableBlockY + 1), (double)this.sitableBlockZ + 0.5D, this.field_75404_b)) {
            this.theFamiliar.getNavigator().tryMoveToXYZ((double)this.sitableBlockX + 0.5D, this.theFamiliar.posY, (double)this.sitableBlockZ + 0.5D, this.field_75404_b);
         }

         ++this.field_75402_d;
      } else if(!this.theFamiliar.isSitting()) {
         EntityLivingBase living = this.theFamiliar.getOwner();
         if(living != null && living instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)living;
            SoundEffect.RANDOM_ORB.playAtPlayer(this.theFamiliar.worldObj, player);
            ChatUtil.sendTranslated(EnumChatFormatting.LIGHT_PURPLE, player, "witchery.familiar.foundsomething", new Object[]{Integer.valueOf(MathHelper.floor_double(this.theFamiliar.posX)).toString(), Integer.valueOf(MathHelper.floor_double(this.theFamiliar.posY)).toString(), Integer.valueOf(MathHelper.floor_double(this.theFamiliar.posZ)).toString()});
         }

         this.theFamiliar.clearItemToFind();
         this.theFamiliar.setAISitting(true);
      } else {
         --this.field_75402_d;
      }

   }

   protected boolean getNearbySitableBlockDistance() {
      boolean MAX_WIDTH = true;
      int DEPTH = this.theFamiliar.getDepthToFind();

      for(int i = 1; (double)i < (double)DEPTH; ++i) {
         for(int j = (int)this.theFamiliar.posX - 4; (double)j < this.theFamiliar.posX + 4.0D; ++j) {
            for(int k = (int)this.theFamiliar.posZ - 4; (double)k < this.theFamiliar.posZ + 4.0D; ++k) {
               if(this.isSittableBlock(this.theFamiliar.worldObj, j, i, k)) {
                  this.sitableBlockX = j;
                  this.sitableBlockY = i;
                  this.sitableBlockZ = k;
                  return true;
               }
            }
         }
      }

      return false;
   }

   protected boolean isSittableBlock(World par1World, int par2, int par3, int par4) {
      Block blockID = this.theFamiliar.getBlockIDToFind();
      Block foundBlockID = par1World.getBlock(par2, par3, par4);
      return blockID == Blocks.diamond_ore?foundBlockID == blockID || foundBlockID == Blocks.emerald_ore:(blockID != Blocks.emerald_ore?foundBlockID == blockID:foundBlockID == blockID || foundBlockID == Blocks.diamond_ore);
   }
}
