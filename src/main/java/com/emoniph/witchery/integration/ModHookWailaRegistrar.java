package com.emoniph.witchery.integration;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockPitDirt;
import com.emoniph.witchery.blocks.BlockPitGrass;
import com.emoniph.witchery.blocks.BlockPlantMine;
import com.emoniph.witchery.blocks.BlockWitchDoor;
import com.emoniph.witchery.entity.EntityIllusionCreeper;
import com.emoniph.witchery.entity.EntityIllusionSpider;
import com.emoniph.witchery.entity.EntityIllusionZombie;
import com.emoniph.witchery.entity.EntityVillagerWere;
import java.util.List;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaEntityAccessor;
import mcp.mobius.waila.api.IWailaEntityProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ModHookWailaRegistrar implements IWailaDataProvider, IWailaEntityProvider {

   private static final ItemStack yellowPlant = new ItemStack(Blocks.yellow_flower);
   private static final ItemStack redPlant = new ItemStack(Blocks.red_flower);
   private static final ItemStack shrubPlant = new ItemStack(Blocks.deadbush);
   private static final ItemStack door = new ItemStack(Items.wooden_door);
   private static final ItemStack dirt = new ItemStack(Blocks.dirt);
   private static final ItemStack grass = new ItemStack(Blocks.grass);
   private static final ItemStack rowandoor = new ItemStack(Witchery.Blocks.DOOR_ROWAN);
   private static Entity CREEPER;
   private static Entity ZOMBIE;
   private static Entity SPIDER;
   private static EntityVillager VILLAGER;


   public static void callbackRegister(IWailaRegistrar registrar) {
      ModHookWailaRegistrar provider = new ModHookWailaRegistrar();
      registrar.registerStackProvider(provider, BlockPlantMine.class);
      registrar.registerStackProvider(provider, BlockWitchDoor.class);
      registrar.registerStackProvider(provider, BlockPitDirt.class);
      registrar.registerStackProvider(provider, BlockPitGrass.class);
      registrar.registerOverrideEntityProvider(provider, EntityIllusionCreeper.class);
      registrar.registerOverrideEntityProvider(provider, EntityIllusionSpider.class);
      registrar.registerOverrideEntityProvider(provider, EntityIllusionZombie.class);
      registrar.registerOverrideEntityProvider(provider, EntityVillagerWere.class);
   }

   public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
      if(accessor.getBlock() == Witchery.Blocks.TRAPPED_PLANT) {
         int foundMeta = accessor.getMetadata();
         if(foundMeta == 5 || foundMeta == 6 || foundMeta == 7 || foundMeta == 4) {
            return yellowPlant;
         }

         if(foundMeta == 1 || foundMeta == 2 || foundMeta == 3 || foundMeta == 0) {
            return redPlant;
         }

         if(foundMeta == 9 || foundMeta == 10 || foundMeta == 11 || foundMeta == 8) {
            return shrubPlant;
         }
      } else {
         if(accessor.getBlock() == Witchery.Blocks.DOOR_ALDER) {
            return door;
         }

         if(accessor.getBlock() == Witchery.Blocks.DOOR_ROWAN) {
            return rowandoor;
         }

         if(accessor.getBlock() == Witchery.Blocks.PIT_DIRT) {
            return dirt;
         }

         if(accessor.getBlock() == Witchery.Blocks.PIT_GRASS) {
            return grass;
         }
      }

      return null;
   }

   public List getWailaHead(ItemStack itemStack, List currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
      return currenttip;
   }

   public List getWailaBody(ItemStack itemStack, List currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
      return currenttip;
   }

   public List getWailaTail(ItemStack itemStack, List currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
      return currenttip;
   }

   public Entity getWailaOverride(IWailaEntityAccessor accessor, IWailaConfigHandler config) {
      if(accessor.getEntity() instanceof EntityIllusionCreeper) {
         if(CREEPER == null || CREEPER.worldObj != accessor.getWorld()) {
            CREEPER = new EntityCreeper(accessor.getWorld());
         }

         return CREEPER;
      } else if(accessor.getEntity() instanceof EntityIllusionZombie) {
         if(ZOMBIE == null || ZOMBIE.worldObj != accessor.getWorld()) {
            ZOMBIE = new EntityZombie(accessor.getWorld());
         }

         return ZOMBIE;
      } else if(accessor.getEntity() instanceof EntityIllusionSpider) {
         if(SPIDER == null || SPIDER.worldObj != accessor.getWorld()) {
            SPIDER = new EntitySpider(accessor.getWorld());
         }

         return SPIDER;
      } else if(!(accessor.getEntity() instanceof EntityVillagerWere)) {
         return null;
      } else {
         EntityVillagerWere were = (EntityVillagerWere)accessor.getEntity();
         if(VILLAGER == null || VILLAGER.worldObj != accessor.getWorld()) {
            VILLAGER = new EntityVillager(accessor.getWorld());
         }

         VILLAGER.setProfession(were.getProfession());
         return VILLAGER;
      }
   }

   public List getWailaHead(Entity entity, List currenttip, IWailaEntityAccessor accessor, IWailaConfigHandler config) {
      return currenttip;
   }

   public List getWailaBody(Entity entity, List currenttip, IWailaEntityAccessor accessor, IWailaConfigHandler config) {
      return currenttip;
   }

   public List getWailaTail(Entity entity, List currenttip, IWailaEntityAccessor accessor, IWailaConfigHandler config) {
      return currenttip;
   }
   
   //in orig file - no find after decomp
   public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, int x, int y, int z) { return tag; }
   public NBTTagCompound getNBTData(EntityPlayerMP player, Entity ent, NBTTagCompound tag, World world) { return tag; }

}
