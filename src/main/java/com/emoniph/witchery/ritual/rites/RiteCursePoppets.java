package com.emoniph.witchery.ritual.rites;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockCircle;
import com.emoniph.witchery.familiar.Familiar;
import com.emoniph.witchery.ritual.Rite;
import com.emoniph.witchery.ritual.RitualStep;
import com.emoniph.witchery.util.ChatUtil;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class RiteCursePoppets extends Rite {

   private final int level;


   public RiteCursePoppets(int level) {
      this.level = level;
   }

   public void addSteps(ArrayList steps, int intialStage) {
      steps.add(new RiteCursePoppets.StepCursePoppets(this));
   }

   private static class StepCursePoppets extends RitualStep {

      private final RiteCursePoppets rite;


      public StepCursePoppets(RiteCursePoppets rite) {
         super(false);
         this.rite = rite;
      }

      public RitualStep.Result process(World world, int posX, int posY, int posZ, long ticks, BlockCircle.TileEntityCircle.ActivatedRitual ritual) {
         if(ticks % 20L != 0L) {
            return RitualStep.Result.STARTING;
         } else {
            if(!world.isRemote) {
               boolean complete = false;
               EntityPlayer curseMasterPlayer = ritual.getInitiatingPlayer(world);
               boolean curseMaster = curseMasterPlayer != null && Familiar.hasActiveCurseMasteryFamiliar(curseMasterPlayer);
               if(curseMaster) {
                  Iterator i$ = ritual.sacrificedItems.iterator();
                  if(i$.hasNext()) {
                     RitualStep.SacrificedItem item = (RitualStep.SacrificedItem)i$.next();
                     if(item.itemstack.getItem() == Witchery.Items.TAGLOCK_KIT && item.itemstack.getItemDamage() == 1) {
                        EntityLivingBase entity = Witchery.Items.TAGLOCK_KIT.getBoundEntity(world, (Entity)null, item.itemstack, Integer.valueOf(1));
                        if(entity != null && !Witchery.Items.POPPET.poppetProtectionActivated(curseMasterPlayer, (ItemStack)null, entity, true)) {
                           Witchery.Items.POPPET.destroyAntiVoodooPoppets(curseMasterPlayer, entity, 10);
                        }

                        complete = true;
                     }
                  }
               } else if(curseMasterPlayer != null) {
                  ChatUtil.sendTranslated(curseMasterPlayer, "witchery.rite.requirescursemastery", new Object[0]);
               }

               if(!complete) {
                  return RitualStep.Result.ABORTED_REFUND;
               }

               ParticleEffect.FLAME.send(SoundEffect.MOB_ENDERDRAGON_GROWL, world, 0.5D + (double)posX, 0.1D + (double)posY, 0.5D + (double)posZ, 1.0D, 2.0D, 16);
            }

            return RitualStep.Result.COMPLETED;
         }
      }
   }
}
