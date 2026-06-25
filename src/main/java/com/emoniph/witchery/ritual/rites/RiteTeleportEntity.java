package com.emoniph.witchery.ritual.rites;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockAreaMarker;
import com.emoniph.witchery.blocks.BlockCircle;
import com.emoniph.witchery.brewing.potions.PotionEnderInhibition;
import com.emoniph.witchery.item.ItemGeneral;
import com.emoniph.witchery.item.ItemHunterClothes;
import com.emoniph.witchery.ritual.RitualStep;
import com.emoniph.witchery.ritual.rites.RiteTeleportation;
import com.emoniph.witchery.util.ChatUtil;
import com.emoniph.witchery.util.Config;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class RiteTeleportEntity extends RiteTeleportation {

   public RiteTeleportEntity(int radius) {
      super(radius);
   }

   protected boolean teleport(World world, int posX, int posY, int posZ, BlockCircle.TileEntityCircle.ActivatedRitual ritual) {
      if(!world.isRemote) {
         ItemStack taglockStack = null;
         Iterator entity = ritual.sacrificedItems.iterator();

         while(entity.hasNext()) {
            RitualStep.SacrificedItem player = (RitualStep.SacrificedItem)entity.next();
            if(Witchery.Items.TAGLOCK_KIT == player.itemstack.getItem() && player.itemstack.getItemDamage() == 1) {
               taglockStack = player.itemstack;
               break;
            }
         }

         if(taglockStack != null) {
            EntityLivingBase entity1 = Witchery.Items.TAGLOCK_KIT.getBoundEntity(world, (Entity)null, taglockStack, Integer.valueOf(1));
            if(entity1 != null && entity1.dimension != Config.instance().dimensionDreamID && world.provider.dimensionId != Config.instance().dimensionDreamID) {
               EntityPlayer player1 = ritual.getInitiatingPlayer(world);
               boolean isImmune = ItemHunterClothes.isCurseProtectionActive(entity1);
               if(!isImmune) {
                  isImmune = BlockAreaMarker.AreaMarkerRegistry.instance().isProtectionActive(entity1, this);
               }

               if(!isImmune && !Witchery.Items.POPPET.voodooProtectionActivated(player1, (ItemStack)null, entity1, true, true) && !PotionEnderInhibition.isActive(entity1, 3)) {
                  ItemGeneral var10000 = Witchery.Items.GENERIC;
                  ItemGeneral.teleportToLocation(world, (double)posX, (double)posY, (double)posZ, world.provider.dimensionId, entity1, true);
                  return true;
               }

               if(player1 != null) {
                  if(isImmune) {
                     ChatUtil.sendTranslated(EnumChatFormatting.RED, player1, "witchery.rite.blackmagicdampening", new Object[0]);
                  } else {
                     ChatUtil.sendTranslated(EnumChatFormatting.RED, player1, "witchery.rite.voodooprotectionactivated", new Object[0]);
                  }
               }

               return false;
            }
         }
      }

      return false;
   }
}
