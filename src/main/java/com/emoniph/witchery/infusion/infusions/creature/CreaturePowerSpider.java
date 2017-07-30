package com.emoniph.witchery.infusion.infusions.creature;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.entity.EntityWitchProjectile;
import com.emoniph.witchery.infusion.infusions.creature.CreaturePower;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class CreaturePowerSpider extends CreaturePower {

   public CreaturePowerSpider(int powerID, Class creatureType) {
      super(powerID, creatureType);
   }

   public void onActivate(World world, EntityPlayer player, int elapsedTicks, MovingObjectPosition mop) {
      if(!world.isRemote) {
         world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (world.rand.nextFloat() * 0.4F + 0.8F));
         world.spawnEntityInWorld(new EntityWitchProjectile(world, player, Witchery.Items.GENERIC.itemWeb));
      }

   }

   public void onUpdate(World world, EntityPlayer player) {
      int blockX = MathHelper.floor_double(player.posX);
      int blockY = MathHelper.floor_double(player.posY + 1.0D);
      int blockZ = MathHelper.floor_double(player.posZ);
      if(world.getBlock(blockX, blockY, blockZ).getMaterial().isSolid()) {
         player.motionY *= 0.6D;
      }

      if(player.isCollidedHorizontally) {
         player.motionY = 0.3D;
      }

      if(player.isSneaking() && player instanceof EntityPlayer && player.isCollidedHorizontally) {
         player.motionY = 0.0D;
      }

   }
}
