package com.emoniph.witchery.util;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.common.ExtendedPlayer;
import com.emoniph.witchery.entity.EntityHornedHuntsman;
import com.emoniph.witchery.entity.ai.EntityAIAttackOnCollide2;
import com.emoniph.witchery.network.PacketPushTarget;
import com.emoniph.witchery.util.BlockUtil;
import com.emoniph.witchery.util.EntityPosition;
import com.emoniph.witchery.util.IHandleDT;
import com.emoniph.witchery.util.Log;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.relauncher.ReflectionHelper;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.EntityTrackerEntry;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class EntityUtil {

   private static Field fieldTrackedEntities = null;
   public static Field fieldGhastTargetedEntity;
   public static Field fieldGhastAggroCooldown;


   public static EntityPlayer playerOrFake(World world, String thrower) {
      return playerOrFake(world, (EntityLivingBase)(world != null?world.getPlayerEntityByName(thrower):null));
   }

   public static EntityPlayer playerOrFake(World world, EntityLivingBase entity) {
      return (EntityPlayer)(entity != null && entity instanceof EntityPlayer?(EntityPlayer)entity:(world != null && world instanceof WorldServer?FakePlayerFactory.getMinecraft((WorldServer)world):null));
   }

   public static Entity findNearestEntityWithinAABB(World world, Class clazz, AxisAlignedBB bounds, Entity entity) {
      Entity foundEntity = world.findNearestEntityWithinAABB(clazz, bounds, entity);
      return foundEntity != null?foundEntity:null;
   }

   public static void spawnEntityInWorld(World world, Entity entity) {
      if(entity != null && world != null && !world.isRemote) {
         world.spawnEntityInWorld(entity);
      }

   }

   public static void correctProjectileTrackerSync(World world, Entity projectile) {
      if(!world.isRemote && world instanceof WorldServer) {
         try {
            if(fieldTrackedEntities == null) {
               fieldTrackedEntities = ReflectionHelper.findField(EntityTracker.class, new String[]{"trackedEntities", "field_72793_b", "b"});
            }

            if(fieldTrackedEntities != null) {
               EntityTracker e = ((WorldServer)world).getEntityTracker();
               Set trackedEntities = (Set)fieldTrackedEntities.get(e);
               Iterator iterator = trackedEntities.iterator();

               while(iterator.hasNext()) {
                  EntityTrackerEntry next = (EntityTrackerEntry)iterator.next();
                  if(next.myEntity == projectile) {
                     next.ticks = 1;
                     break;
                  }
               }
            }
         } catch (IllegalAccessException var6) {
            Log.instance().warning(var6, "Exception occurred setting entity tracking for bolt.");
         } catch (Exception var7) {
            Log.instance().debug(String.format("Exception occurred setting entity tracking for bolt. %s", new Object[]{var7.toString()}));
         }
      }

   }

   public static void push(World world, Entity entity, EntityPosition position, double power) {
      double d = position.x - entity.posX;
      double d1 = position.y - entity.posY;
      double d2 = position.z - entity.posZ;
      double d4 = d * d + d1 * d1 + d2 * d2;
      d4 *= d4;
      if(d4 <= Math.pow(6.0D, 4.0D)) {
         double d5 = -(d * 0.01999999955296516D / d4) * Math.pow(6.0D, 3.0D);
         double d6 = -(d1 * 0.01999999955296516D / d4) * Math.pow(6.0D, 3.0D);
         double d7 = -(d2 * 0.01999999955296516D / d4) * Math.pow(6.0D, 3.0D);
         if(d5 > 0.0D) {
            d5 = 0.22D;
         } else if(d5 < 0.0D) {
            d5 = -0.22D;
         }

         if(d6 > 0.2D) {
            d6 = 0.12D;
         } else if(d6 < -0.1D) {
            d6 = 0.12D;
         }

         if(d7 > 0.0D) {
            d7 = 0.22D;
         } else if(d7 < 0.0D) {
            d7 = -0.22D;
         }

         entity.motionX += d5 * power;
         entity.motionY += d6 * (power / 3.0D);
         entity.motionZ += d7 * power;
      }

   }

   public static void pullTowards(World world, Entity entity, EntityPosition target, double dy, double yy) {
      if(!(entity instanceof EntityDragon) && !(entity instanceof EntityHornedHuntsman) && !target.occupiedBy(entity)) {
         double d = target.x - entity.posX;
         double d1 = target.y - entity.posY;
         double d2 = target.z - entity.posZ;
         float distance = MathHelper.sqrt_double(d * d + d1 * d1 + d2 * d2);
         if((double)distance >= 0.01D) {
            float f2 = 0.1F + (float)dy;
            double mx = d / (double)distance * (double)f2 * (double)distance;
            double my = yy == 0.0D?0.4D:d1 / (double)distance * (double)distance * 0.2D + 0.2D + yy;
            double mz = d2 / (double)distance * (double)f2 * (double)distance;
            if(entity instanceof EntityLivingBase) {
               ((EntityLivingBase)entity).addPotionEffect(new PotionEffect(Potion.jump.id, 20, 1));
            }

            if(entity instanceof EntityPlayer) {
               Witchery.packetPipeline.sendTo((IMessage)(new PacketPushTarget(mx, my, mz)), (EntityPlayer)entity);
            } else {
               entity.motionX = mx;
               entity.motionY = my;
               entity.motionZ = mz;
            }

         }
      }
   }

   public static void pushback(World world, Entity entity, EntityPosition hit, double xyScale, double ySpeed) {
      double d = hit.x - entity.posX;
      double d1 = hit.y - entity.posY;
      double d2 = hit.z - entity.posZ;
      Vec3 vec = Vec3.createVectorHelper(d, d1, d2).normalize();
      double dx = -vec.xCoord * xyScale;
      double dy = Math.max(-vec.yCoord, ySpeed);
      double dz = -vec.zCoord * xyScale;
      if(entity instanceof EntityPlayer) {
         Witchery.packetPipeline.sendTo((IMessage)(new PacketPushTarget(dx, dy, dz)), (EntityPlayer)entity);
      } else {
         entity.motionX = dx;
         entity.motionY = dy;
         entity.motionZ = dz;
      }

   }

   public static List getEntitiesInRadius(Class clazz, TileEntity tile, double radius) {
      return getEntitiesInRadius(clazz, tile.getWorldObj(), 0.5D + (double)tile.xCoord, 0.5D + (double)tile.yCoord, 0.5D + (double)tile.zCoord, radius);
   }

   public static List getEntitiesInRadius(Class clazz, World world, double x, double y, double z, double radius) {
      AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius);
      List entities = world.getEntitiesWithinAABB(clazz, bounds);
      ArrayList nearbyEntities = new ArrayList();
      double radiusSq = radius * radius;
      Iterator i$ = entities.iterator();

      while(i$.hasNext()) {
         Entity entity = (Entity)i$.next();
         if(entity.getDistanceSq(x, entity.posY, z) <= radiusSq) {
            nearbyEntities.add(entity);
         }
      }

      return nearbyEntities;
   }

   public static void setTarget(EntityLiving attacker, EntityLivingBase victim) {
      attacker.setAttackTarget(victim);
      if(attacker instanceof EntityGhast) {
         try {
            EntityGhast attackerCreature = (EntityGhast)attacker;
            if(fieldGhastTargetedEntity == null) {
               fieldGhastTargetedEntity = ReflectionHelper.findField(EntityGhast.class, new String[]{"targetedEntity", "field_70792_g", "g"});
            }

            fieldGhastTargetedEntity.set(attackerCreature, victim);
            if(fieldGhastAggroCooldown == null) {
               fieldGhastAggroCooldown = ReflectionHelper.findField(EntityGhast.class, new String[]{"aggroCooldown", "field_70798_h", "h"});
            }

            fieldGhastAggroCooldown.set(attackerCreature, Integer.valueOf(20000));
         } catch (IllegalAccessException var9) {
            Log.instance().warning(var9, "Exception occurred setting ghast target.");
         } catch (Exception var10) {
            Log.instance().debug(String.format("Exception occurred setting ghast target. %s", new Object[]{var10.toString()}));
         }
      }

      if(attacker instanceof EntityCreature) {
         EntityCreature attackerCreature1 = (EntityCreature)attacker;
         attackerCreature1.setTarget(victim);
         attackerCreature1.setRevengeTarget(victim);
         if(attackerCreature1 instanceof EntityZombie || attackerCreature1 instanceof EntityCreeper) {
            boolean found = false;
            Class victimClass = victim.getClass();
            Iterator i$ = attackerCreature1.targetTasks.taskEntries.iterator();

            while(i$.hasNext()) {
               Object obj = i$.next();
               EntityAITaskEntry task = (EntityAITaskEntry)obj;
               if(task.action instanceof EntityAIAttackOnCollide2) {
                  EntityAIAttackOnCollide2 ai = (EntityAIAttackOnCollide2)task.action;
                  if(ai != null && ai.appliesToClass(victimClass)) {
                     found = true;
                  }
                  break;
               }
            }

            if(!found) {
               attacker.tasks.addTask(2, new EntityAIAttackOnCollide2(attackerCreature1, victimClass, 1.0D, false));
            }
         }
      }

   }

   public static void dropAttackTarget(EntityLiving entity) {
      entity.setAttackTarget((EntityLivingBase)null);
      if(entity instanceof EntityCreature) {
         EntityCreature creatureEntity = (EntityCreature)entity;
         creatureEntity.setTarget((Entity)null);
         creatureEntity.setRevengeTarget((EntityLivingBase)null);
      }

   }

   public static void syncInventory(EntityPlayer player) {
      if(player instanceof EntityPlayerMP) {
         ((EntityPlayerMP)player).sendContainerToPlayer(player.inventoryContainer);
      }

   }

   public static void persistanceRequired(EntityLiving entity) {
      entity.func_110163_bv();
   }

   public static void setNoDrops(EntityLiving entity) {
      if(entity != null) {
         NBTTagCompound nbtEntity = entity.getEntityData();
         nbtEntity.setBoolean("WITCNoDrops", true);
      }

   }

   public static boolean isNoDrops(EntityLivingBase entity) {
      if(entity != null && !(entity instanceof EntityPlayer)) {
         NBTTagCompound nbtEntity = entity.getEntityData();
         return nbtEntity.getBoolean("WITCNoDrops");
      } else {
         return false;
      }
   }

   public static float getHealthAfterDamage(LivingHurtEvent event, float currentHealth, EntityLivingBase entity) {
      if(event.source.isUnblockable()) {
         return currentHealth - event.ammount;
      } else {
         float damage = event.ammount;
         int i = 25 - entity.getTotalArmorValue();
         float f1 = damage * (float)i;
         damage = f1 / 25.0F;
         float j;
         if(entity.isPotionActive(Potion.resistance) && event.source != DamageSource.outOfWorld) {
            i = (entity.getActivePotionEffect(Potion.resistance).getAmplifier() + 1) * 5;
            j = (float)(25 - i);
            f1 = damage * j;
            damage = f1 / 25.0F;
         }

         if(damage <= 0.0F) {
            damage = 0.0F;
         } else {
            i = EnchantmentHelper.getEnchantmentModifierDamage(entity.getLastActiveItems(), event.source);
            if(i > 20) {
               i = 20;
            }

            if(i > 0 && i <= 20) {
               j = (float)(25 - i);
               f1 = damage * j;
               damage = f1 / 25.0F;
            }
         }

         return currentHealth - damage;
      }
   }

   public static void instantDeath(EntityLivingBase entity, EntityLivingBase attacker) {
      if(entity != null && entity.worldObj != null && !entity.worldObj.isRemote) {
         if(entity instanceof EntityLiving) {
            entity.setHealth(0.0F);
            if(attacker == null) {
               entity.onDeath(DamageSource.magic);
            } else {
               entity.onDeath(new EntityDamageSource(DamageSource.magic.getDamageType(), attacker));
            }

            entity.setDead();
         } else if(entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)entity;
            if(!player.capabilities.isCreativeMode) {
               if(player.isPlayerSleeping()) {
                  player.wakeUpPlayer(true, true, false);
               }

               entity.setHealth(0.0F);
               if(ExtendedPlayer.get(player).isVampire()) {
                  entity.onDeath(attacker == null?EntityUtil.DamageSourceSunlight.SUN:new EntityUtil.DamageSourceSunlight(attacker));
               } else {
                  entity.onDeath(new EntityDamageSource(DamageSource.magic.getDamageType(), attacker));
               }
            }
         }
      }

   }

   public static boolean touchOfDeath(Entity victim, EntityLivingBase attacker, float damage) {
      if(victim != null && victim.isEntityInvulnerable()) {
         return false;
      } else {
         if(victim != null && victim.worldObj != null && !victim.worldObj.isRemote) {
            if(victim instanceof EntityLiving) {
               EntityDamageSource player = new EntityDamageSource(DamageSource.magic.getDamageType(), attacker);
               EntityLiving creature = (EntityLiving)victim;
               float cap = 10000.0F;
               if(victim instanceof IHandleDT) {
                  cap = ((IHandleDT)victim).getCapDT(player, damage);
                  if(cap <= 0.0F) {
                     return false;
                  }

                  if(attacker instanceof EntityLiving) {
                     cap = Math.min(6.0F, cap);
                  }
               }

               creature.attackEntityFrom(player, 0.0F);
               creature.setHealth(Math.max(creature.getHealth() - Math.min(damage, cap), 0.0F));
               creature.attackEntityFrom(player, 0.0F);
            } else if(victim instanceof EntityPlayer) {
               EntityPlayer player1 = (EntityPlayer)victim;
               if(player1.capabilities.isCreativeMode) {
                  return false;
               }

               player1.setHealth(Math.max(player1.getHealth() - damage, 0.0F));
               if(player1.getHealth() <= 0.0F) {
                  if(attacker == null) {
                     player1.onDeath(DamageSource.magic);
                  } else {
                     player1.onDeath(new EntityDamageSource(DamageSource.magic.getDamageType(), attacker));
                  }
               } else {
                  player1.attackEntityFrom(new EntityDamageSource(DamageSource.magic.getDamageType(), attacker), 0.0F);
               }
            }
         }

         return true;
      }
   }

   public static boolean moveToBlockPositionAndUpdate(EntityLiving entity, int x, int y, int z, int maxDY) {
      World world = entity.worldObj;
      boolean done = false;
      int mod = 0;
      int sign = -1;

      while(!done && mod <= 2 * maxDY && y < 250 && y > 2) {
         if(BlockUtil.isNormalCube(world.getBlock(x, y, z)) && world.isAirBlock(x, y + 1, z) && world.isAirBlock(x, y + 2, z)) {
            done = true;
         } else {
            ++mod;
            sign *= -1;
            y += mod * sign;
         }
      }

      if(done) {
         entity.setPositionAndUpdate(0.5D + (double)x, 1.05D + (double)y, 0.5D + (double)z);
      }

      return done;
   }


   public static class DamageSourceVampireFire extends DamageSource {

      public static final EntityUtil.DamageSourceVampireFire SOURCE = new EntityUtil.DamageSourceVampireFire();


      public DamageSourceVampireFire() {
         super("onFire");
         this.setDamageBypassesArmor();
         this.setMagicDamage();
      }

   }

   public static class DamageSourceSunlight extends EntityDamageSource {

      public static final EntityUtil.DamageSourceSunlight SUN = new EntityUtil.DamageSourceSunlight((Entity)null);


      public DamageSourceSunlight(Entity attacker) {
         super("sun", attacker);
         this.setDamageBypassesArmor();
         this.setMagicDamage();
      }

      public IChatComponent func_151519_b(EntityLivingBase p_151519_1_) {
         EntityLivingBase entitylivingbase1 = p_151519_1_.func_94060_bK();
         String s = "witchery:death.attack." + super.damageType;
         String s1 = s + ".player";
         return entitylivingbase1 != null && StatCollector.canTranslate(s1)?new ChatComponentTranslation(s1, new Object[]{p_151519_1_.func_145748_c_(), entitylivingbase1.func_145748_c_()}):new ChatComponentTranslation(s, new Object[]{p_151519_1_.func_145748_c_()});
      }

   }
}
