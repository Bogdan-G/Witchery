package com.emoniph.witchery.brewing.potions;

import com.emoniph.witchery.brewing.potions.IHandleLivingUpdate;
import com.emoniph.witchery.brewing.potions.PotionBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class PotionOverheating extends PotionBase implements IHandleLivingUpdate {

   public PotionOverheating(int id, int color) {
      super(id, true, color);
   }

   public void postContructInitialize() {
      this.setPermenant();
      this.setIncurable();
   }

   public void onLivingUpdate(World world, EntityLivingBase entity, LivingUpdateEvent event, int amplifier, int duration) {
      if(!world.isRemote && world.getTotalWorldTime() % 5L == 3L && !entity.isBurning() && world.rand.nextInt(amplifier > 1?20:(amplifier > 0?25:30)) == 0) {
         int x = MathHelper.floor_double(entity.posX);
         int z = MathHelper.floor_double(entity.posZ);
         BiomeGenBase biome = world.getBiomeGenForCoords(x, z);
         if((double)biome.temperature >= 1.5D && (!biome.canSpawnLightningBolt() || !world.isRaining()) && !entity.isInWater()) {
            entity.setFire(Math.min(world.rand.nextInt(amplifier < 3?2:amplifier) + 1, 4));
         }
      }

   }
}
