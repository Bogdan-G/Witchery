package com.emoniph.witchery.ritual.rites;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockCircle;
import com.emoniph.witchery.ritual.Rite;
import com.emoniph.witchery.ritual.RitualStep;
import com.emoniph.witchery.util.ChatUtil;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.emoniph.witchery.util.TimeUtil;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class RiteFidelio extends Rite {
    @Override
    public void addSteps(ArrayList steps, int initialStage) {
        steps.add(new StepRiteFidelio(this, initialStage));
    }

    private static class StepRiteFidelio extends RitualStep {
        private final RiteFidelio rite;

        public StepRiteFidelio(RiteFidelio rite, int initialStage) {
            super(false);
            this.rite = rite;
        }

        @Override
        public RitualStep.Result process(World world, int x, int y, int z, long ticks, BlockCircle.TileEntityCircle.ActivatedRitual circleType) {
            if (ticks % 20L != 0L) {
                return RitualStep.Result.STARTING;
            }
            if (!world.isRemote) {
                EntityLivingBase subject = null;
                Iterator i$ = circleType.sacrificedItems.iterator();
                while (i$.hasNext()) {
                    RitualStep.SacrificedItem item = (RitualStep.SacrificedItem)i$.next();
                    if (item.itemstack.getItem() == Witchery.Items.TAGLOCK_KIT && item.itemstack.getItemDamage() == 1) {
                        subject = Witchery.Items.TAGLOCK_KIT.getBoundEntity(world, (Entity)null, item.itemstack, Integer.valueOf(1));
                        break;
                    }
                }
                if (subject == null) {
                    subject = circleType.getInitiatingPlayer(world);
                }
                if (subject == null) {
                    return RitualStep.Result.ABORTED_REFUND;
                }

                // The Fidelius charm hides the secret-keeper: cleanse and conceal.
                int dur = TimeUtil.minsToTicks(20);
                subject.clearActivePotions();
                subject.addPotionEffect(new PotionEffect(Potion.invisibility.id, dur, 0));
                subject.addPotionEffect(new PotionEffect(Potion.nightVision.id, dur, 0));
                subject.addPotionEffect(new PotionEffect(Potion.resistance.id, dur, 0));
                if (subject instanceof EntityPlayer) {
                    ChatUtil.sendTranslated(EnumChatFormatting.AQUA, (EntityPlayer)subject, "witchery.rite.fidelio.hidden", new Object[0]);
                }
                ParticleEffect.PORTAL.send(SoundEffect.MOB_ENDERMEN_PORTAL, subject, 1.0D, 2.0D, 24);
                ParticleEffect.INSTANT_SPELL.send(SoundEffect.RANDOM_LEVELUP, world, 0.5D + (double)x, (double)y + 1.0D, 0.5D + (double)z, 2.0D, 2.0D, 24);
            }
            return RitualStep.Result.COMPLETED;
        }
    }
}
