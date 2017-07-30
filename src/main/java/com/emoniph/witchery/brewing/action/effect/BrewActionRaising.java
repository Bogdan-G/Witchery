package com.emoniph.witchery.brewing.action.effect;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.brewing.AltarPower;
import com.emoniph.witchery.brewing.BrewItemKey;
import com.emoniph.witchery.brewing.BrewNamePart;
import com.emoniph.witchery.brewing.EffectLevel;
import com.emoniph.witchery.brewing.ModifiersEffect;
import com.emoniph.witchery.brewing.Probability;
import com.emoniph.witchery.brewing.action.BrewActionEffect;
import com.emoniph.witchery.brewing.potions.PotionEnslaved;
import com.emoniph.witchery.util.Coord;
import com.emoniph.witchery.util.EntityUtil;
import com.emoniph.witchery.util.Log;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.emoniph.witchery.util.TimeUtil;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BrewActionRaising extends BrewActionEffect {

   public BrewActionRaising(Item axe, AltarPower powerCost, EffectLevel effectLevel) {
      super(new BrewItemKey(axe, 32767), new BrewNamePart("witchery:brew.raising"), powerCost, new Probability(1.0D), effectLevel);
   }

   protected void doApplyToBlock(World world, int posX, int posY, int posZ, ForgeDirection side, int radius, ModifiersEffect modifiers, ItemStack stack) {
      raiseDead(world, new Coord(posX, posY, posZ, side), modifiers.ritualised?0:modifiers.getStrength(), modifiers.caster, modifiers.ritualised?TimeUtil.secsToTicks(10 * (modifiers.getStrength() + 1)):0);
   }

   public static void raiseDead(World world, Coord coord, int strength, EntityPlayer raiser, int lifetime) {
      boolean MAX_DISTANCE = true;
      boolean MAX_DROP = true;
      raiseUndead(world, coord, raiser, lifetime);
      int extraCount = 0;
      double chance = world.rand.nextDouble();
      if(strength >= 1 && world.rand.nextDouble() < (double)strength * 0.5D) {
         ++extraCount;
      }

      if(strength >= 2 && world.rand.nextDouble() < (double)strength * 0.25D) {
         ++extraCount;
      }

      if(strength >= 3 && world.rand.nextDouble() < (double)strength * 0.25D) {
         ++extraCount;
      }

      int i = 0;

      while(i < extraCount) {
         int x = coord.x - 3 + world.rand.nextInt(6) + 1;
         int z = coord.z - 3 + world.rand.nextInt(6) + 1;
         int dy = coord.y + 6;
         int minY = coord.y - 6;

         while(true) {
            if(dy >= minY) {
               if(!world.getBlock(x, dy - 1, z).getMaterial().isSolid() || !world.isAirBlock(x, dy, z)) {
                  --dy;
                  continue;
               }

               raiseUndead(world, new Coord(x, dy, z), raiser, lifetime);
            }

            ++i;
            break;
         }
      }

   }

   private static void raiseUndead(World world, Coord coord, EntityPlayer thrower, int lifetime) {
      if(!world.isRemote) {
         EntityLiving undeadEntity = createUndeadCreature(world);
         undeadEntity.setLocationAndAngles(0.5D + (double)coord.x, 0.1D + (double)coord.y, 0.5D + (double)coord.z, 0.0F, 0.0F);
         Object entitylivingData = null;
         undeadEntity.onSpawnWithEgg((IEntityLivingData)entitylivingData);
         EntityUtil.persistanceRequired(undeadEntity);
         EntityUtil.setNoDrops(undeadEntity);
         if(lifetime > 0) {
            undeadEntity.addPotionEffect(new PotionEffect(Witchery.Potions.MORTAL_COIL.id, lifetime));
         }

         if(thrower != null) {
            try {
               PotionEnslaved.setEnslaverForMob(undeadEntity, thrower);
            } catch (Exception var7) {
               Log.instance().warning(var7, "Unhandled exception occurred setting enslaver from raiseUnded potion.");
            }
         }

         world.spawnEntityInWorld(undeadEntity);
         ParticleEffect.LARGE_SMOKE.send(SoundEffect.NONE, undeadEntity, 0.5D, 2.0D, 16);
      }

   }

   private static EntityLiving createUndeadCreature(World world) {
      double value = world.rand.nextDouble();
      return (EntityLiving)(value < 0.6D?new EntityZombie(world):(value < 0.97D?new EntitySkeleton(world):new EntityPigZombie(world)));
   }
}
