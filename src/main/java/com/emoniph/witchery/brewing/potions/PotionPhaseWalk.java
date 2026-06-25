package com.emoniph.witchery.brewing.potions;

import com.emoniph.witchery.brewing.potions.IHandleLivingUpdate;
import com.emoniph.witchery.brewing.potions.PotionBase;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class PotionPhaseWalk extends PotionBase implements IHandleLivingUpdate {

   public PotionPhaseWalk(int id, int color) {
      super(id, color);
   }

   public void onLivingUpdate(World world, EntityLivingBase entity, LivingUpdateEvent event, int amplifier, int duration) {
      if(entity.isEntityInsideOpaqueBlock()) {
         entity.setAir(300);
         int x = MathHelper.floor_double(entity.posX);
         int y = MathHelper.floor_double(entity.posY + (double)entity.getEyeHeight());
         int z = MathHelper.floor_double(entity.posZ);
         double yaw = (double)(entity.rotationYaw + 90.0F) * Math.PI / 180.0D;
         double dx = Math.cos(yaw);
         double dz = Math.sin(yaw);
         boolean clearAhead = isPassable(world, x + (int)Math.round(dx), y, z + (int)Math.round(dz)) && isPassable(world, x + (int)Math.round(dx), y - 1, z + (int)Math.round(dz));
         if(clearAhead) {
            entity.motionX += dx * 0.18D;
            entity.motionZ += dz * 0.18D;
         } else {
            entity.motionX -= dx * 0.18D;
            entity.motionZ -= dz * 0.18D;
         }

         if(amplifier >= 1 && entity instanceof EntityPlayer) {
            entity.motionY += 0.08D;
         }
      }

   }

   private static boolean isPassable(World world, int x, int y, int z) {
      Block block = world.getBlock(x, y, z);
      return !block.getMaterial().isSolid() || !block.getMaterial().blocksMovement();
   }
}
