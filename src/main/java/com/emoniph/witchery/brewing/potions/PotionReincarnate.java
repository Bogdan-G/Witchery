package com.emoniph.witchery.brewing.potions;

import com.emoniph.witchery.brewing.potions.IHandleLivingDeath;
import com.emoniph.witchery.brewing.potions.PotionBase;
import com.emoniph.witchery.infusion.Infusion;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

public class PotionReincarnate extends PotionBase implements IHandleLivingDeath {

   public PotionReincarnate(int id, int color) {
      super(id, color);
   }

   public void onLivingDeath(World world, EntityLivingBase entity, LivingDeathEvent event, int amplifier) {
      if(!world.isRemote) {
         Class creatureToSpawn = null;
         if(!(entity instanceof EntityAnimal) && !(entity instanceof EntitySpider)) {
            if(amplifier > 2) {
               creatureToSpawn = EntityBlaze.class;
            } else if(amplifier > 1) {
               creatureToSpawn = EntitySkeleton.class;
            } else {
               creatureToSpawn = EntityZombie.class;
            }
         } else if(amplifier > 2) {
            creatureToSpawn = EntityCreeper.class;
         } else if(amplifier > 1) {
            creatureToSpawn = EntityCaveSpider.class;
         } else {
            creatureToSpawn = EntitySpider.class;
         }

         Entity attacker = event.source.getEntity();
         Infusion.spawnCreature(world, creatureToSpawn, (int)entity.posX, (int)entity.posY, (int)entity.posZ, attacker != null && attacker instanceof EntityLivingBase?(EntityLivingBase)attacker:null, 0, 0, ParticleEffect.INSTANT_SPELL, SoundEffect.MOB_ZOMBIE_SAY);
      }

   }
}
