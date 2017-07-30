package com.emoniph.witchery.entity;

import com.emoniph.witchery.util.DemonicDamageSource;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntitySoulfire extends EntitySmallFireball {

   public EntitySoulfire(World par1World) {
      super(par1World);
      this.setSize(0.3125F, 0.3125F);
   }

   public EntitySoulfire(World par1World, EntityLivingBase par2EntityLivingBase, double par3, double par5, double par7) {
      super(par1World, par2EntityLivingBase, par3, par5, par7);
      this.setSize(0.3125F, 0.3125F);
   }

   public EntitySoulfire(World par1World, double par2, double par4, double par6, double par8, double par10, double par12) {
      super(par1World, par2, par4, par6, par8, par10, par12);
      this.setSize(0.3125F, 0.3125F);
   }

   protected void onImpact(MovingObjectPosition par1MovingObjectPosition) {
      super.onImpact(par1MovingObjectPosition);
      if(!super.worldObj.isRemote && par1MovingObjectPosition.entityHit != null) {
         par1MovingObjectPosition.entityHit.attackEntityFrom(new DemonicDamageSource(super.shootingEntity), 6.0F);
      }

   }
}
