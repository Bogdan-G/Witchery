package com.emoniph.witchery.brewing;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.brewing.Dispersal;
import com.emoniph.witchery.brewing.ModifiersEffect;
import com.emoniph.witchery.brewing.ModifiersImpact;
import com.emoniph.witchery.brewing.ModifiersRitual;
import com.emoniph.witchery.brewing.RitualStatus;
import com.emoniph.witchery.brewing.TileEntityCursedBlock;
import com.emoniph.witchery.brewing.WitcheryBrewRegistry;
import com.emoniph.witchery.familiar.Familiar;
import com.emoniph.witchery.util.BlockProtect;
import com.emoniph.witchery.util.BlockUtil;
import com.emoniph.witchery.util.Coord;
import com.emoniph.witchery.util.EntityPosition;
import com.emoniph.witchery.util.EntityUtil;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;

public class DispersalTriggered extends Dispersal {

   public void onImpactSplashPotion(World world, NBTTagCompound nbtBrew, MovingObjectPosition mop, ModifiersImpact modifiers) {
      Coord coord = new Coord(mop, modifiers.impactPosition, true);
      boolean replaceable = BlockProtect.checkModsForBreakOK(world, coord.x, coord.y, coord.z, modifiers.thrower);
      if(replaceable) {
         Block block = coord.getBlock(world);
         if(block == Blocks.stone_button) {
            Witchery.Blocks.CURSED_BUTTON_STONE.replaceButton(world, coord.x, coord.y, coord.z, modifiers, nbtBrew);
            return;
         }

         if(block == Blocks.wooden_button) {
            Witchery.Blocks.CURSED_BUTTON_WOOD.replaceButton(world, coord.x, coord.y, coord.z, modifiers, nbtBrew);
            return;
         }

         if(block == Blocks.lever) {
            Witchery.Blocks.CURSED_LEVER.replaceButton(world, coord.x, coord.y, coord.z, modifiers, nbtBrew);
            return;
         }

         if(block.hasTileEntity(coord.getBlockMetadata(world))) {
            TileEntityCursedBlock y = (TileEntityCursedBlock)BlockUtil.getTileEntity(world, coord.x, coord.y, coord.z, TileEntityCursedBlock.class);
            if(y != null) {
               y.updateCurse(modifiers, nbtBrew);
            }
         }

         coord = new Coord(mop, modifiers.impactPosition, false);
         block = coord.getBlock(world);
         if(block == Blocks.wooden_pressure_plate) {
            Witchery.Blocks.CURSED_WOODEN_PRESSURE_PLATE.replaceButton(world, coord.x, coord.y, coord.z, modifiers, nbtBrew);
         } else if(block == Blocks.stone_pressure_plate) {
            Witchery.Blocks.CURSED_STONE_PRESSURE_PLATE.replaceButton(world, coord.x, coord.y, coord.z, modifiers, nbtBrew);
         } else if(block == Witchery.Blocks.SNOW_PRESSURE_PLATE) {
            Witchery.Blocks.CURSED_SNOW_PRESSURE_PLATE.replaceButton(world, coord.x, coord.y, coord.z, modifiers, nbtBrew);
         } else if(block == Blocks.wooden_door) {
            Witchery.Blocks.CURSED_WOODEN_DOOR.replaceButton(world, coord.x, coord.y, coord.z, modifiers, nbtBrew);
         } else if(block.hasTileEntity(coord.getBlockMetadata(world))) {
            int var10 = coord.y;
            if(block == Witchery.Blocks.CURSED_WOODEN_DOOR) {
               int tile = ((BlockDoor)Blocks.wooden_door).func_150012_g(world, coord.x, coord.y, coord.z);
               if((tile & 8) != 0) {
                  --var10;
               }
            }

            TileEntityCursedBlock var11 = (TileEntityCursedBlock)BlockUtil.getTileEntity(world, coord.x, var10, coord.z, TileEntityCursedBlock.class);
            if(var11 != null) {
               var11.updateCurse(modifiers, nbtBrew);
            }
         }
      }

   }

   public String getUnlocalizedName() {
      return "witchery:brew.dispersal.triggered";
   }

   public RitualStatus onUpdateRitual(World world, int x, int y, int z, NBTTagCompound nbtBrew, ModifiersRitual modifiers, ModifiersImpact impactModifiers) {
      AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox((double)x, (double)(y + 1), (double)z, (double)(x + 1), (double)(y + 2), (double)(z + 1));
      List items = world.getEntitiesWithinAABB(EntityItem.class, bounds);
      Iterator i$ = items.iterator();

      EntityItem item;
      ItemStack stack;
      do {
         if(!i$.hasNext()) {
            return RitualStatus.FAILED;
         }

         item = (EntityItem)i$.next();
         stack = item.getEntityItem();
      } while(stack == null);

      if(stack.stackSize > 1) {
         stack = stack.splitStack(1);
         EntityItem nbtRoot = new EntityItem(item.worldObj, item.posX, item.posY, item.posZ, stack);
         world.spawnEntityInWorld(nbtRoot);
      }

      if(!stack.hasTagCompound()) {
         stack.setTagCompound(new NBTTagCompound());
      }

      NBTTagCompound nbtRoot1 = stack.getTagCompound();
      AxisAlignedBB playerBounds = bounds.expand(3.0D, 3.0D, 3.0D);
      List players = world.getEntitiesWithinAABB(EntityPlayer.class, playerBounds);
      boolean catNear = false;
      Iterator nbtCurse = players.iterator();

      while(nbtCurse.hasNext()) {
         EntityPlayer player = (EntityPlayer)nbtCurse.next();
         if(Familiar.hasActiveCurseMasteryFamiliar(player)) {
            catNear = true;
            break;
         }
      }

      NBTTagCompound nbtCurse1;
      if(nbtRoot1.hasKey("WITCCurse") && ((NBTTagCompound)nbtRoot1.getCompoundTag("WITCCurse").getTag("Curse")).getTagList("Items", 10).equals(nbtBrew.getTagList("Items", 10))) {
         nbtCurse1 = nbtRoot1.getCompoundTag("WITCCurse");
         nbtCurse1.setInteger("Count", nbtCurse1.getInteger("Count") + (catNear?2:1));
      } else {
         nbtCurse1 = new NBTTagCompound();
         nbtCurse1.setTag("Curse", nbtBrew.copy());
         nbtCurse1.setInteger("Count", catNear?2:1);
         nbtRoot1.setTag("WITCCurse", nbtCurse1);
      }

      ParticleEffect.EXPLODE.send(SoundEffect.RANDOM_ORB, item, 0.5D, 0.5D, 16);
      return RitualStatus.COMPLETE;
   }

   public static class EventHooks {

      @SubscribeEvent
      public void onPlayerInteract(PlayerInteractEvent event) {
         if(!event.entityPlayer.worldObj.isRemote && (event.action == Action.RIGHT_CLICK_AIR || event.action == Action.RIGHT_CLICK_BLOCK) && event.entityPlayer.getHeldItem() != null && event.entityPlayer.getHeldItem().hasTagCompound() && event.entityPlayer.getHeldItem().getTagCompound().hasKey("WITCCurse")) {
            NBTTagCompound root = event.entityPlayer.getHeldItem().getTagCompound().getCompoundTag("WITCCurse");
            NBTTagCompound tag = root.getCompoundTag("Curse");
            int charges = root.getInteger("Count");
            long delay = root.getLong("Delay");
            if(delay == 0L || MinecraftServer.getSystemTimeMillis() > delay) {
               if(charges > 0) {
                  WitcheryBrewRegistry.INSTANCE.applyToEntity(event.entityPlayer.worldObj, event.entityPlayer, tag, new ModifiersEffect(1.0D, 1.0D, false, new EntityPosition(event.entityPlayer), false, 0, EntityUtil.playerOrFake(event.entityPlayer.worldObj, (EntityLivingBase)null)));
                  ParticleEffect.SPELL_COLORED.send(SoundEffect.RANDOM_POP, event.entityPlayer, 1.0D, 1.0D, 16);
                  --charges;
               }

               if(charges > 0) {
                  root.setInteger("Count", charges);
                  root.setLong("Delay", MinecraftServer.getSystemTimeMillis() + 5000L);
               } else {
                  event.entityPlayer.getHeldItem().getTagCompound().removeTag("WITCCurse");
                  if(event.entityPlayer.getHeldItem().getTagCompound().hasNoTags()) {
                     event.entityPlayer.getHeldItem().setTagCompound((NBTTagCompound)null);
                  }
               }
            }
         }

      }
   }
}
