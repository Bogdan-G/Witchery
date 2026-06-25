package com.emoniph.witchery.ritual.rites;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockCircle;
import com.emoniph.witchery.entity.EntityWitchHunter;
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

public class RiteDementorKiss extends Rite {
    @Override
    public void addSteps(ArrayList steps, int initialStage) {
        steps.add(new StepRiteDementorKiss(this, initialStage));
    }

    private static class StepRiteDementorKiss extends RitualStep {
        private final RiteDementorKiss rite;

        public StepRiteDementorKiss(RiteDementorKiss rite, int initialStage) {
            super(false);
            this.rite = rite;
        }

        @Override
        public RitualStep.Result process(World world, int x, int y, int z, long ticks, BlockCircle.TileEntityCircle.ActivatedRitual circleType) {
            if (ticks % 20L != 0L) {
                return RitualStep.Result.STARTING;
            }
            if (!world.isRemote) {
                EntityPlayer initiator = circleType.getInitiatingPlayer(world);
                boolean kissed = false;
                Iterator i$ = circleType.sacrificedItems.iterator();
                while (i$.hasNext()) {
                    RitualStep.SacrificedItem item = (RitualStep.SacrificedItem)i$.next();
                    if (item.itemstack.getItem() == Witchery.Items.TAGLOCK_KIT && item.itemstack.getItemDamage() == 1) {
                        EntityLivingBase target = Witchery.Items.TAGLOCK_KIT.getBoundEntity(world, (Entity)null, item.itemstack, Integer.valueOf(1));
                        if (target != null) {
                            EntityWitchHunter.blackMagicPerformed(initiator);
                            int dur = TimeUtil.secsToTicks(30);
                            target.addPotionEffect(new PotionEffect(Potion.wither.id, dur, 1));
                            target.addPotionEffect(new PotionEffect(Potion.blindness.id, dur, 0));
                            target.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, dur, 2));
                            target.addPotionEffect(new PotionEffect(Potion.weakness.id, dur, 2));
                            target.addPotionEffect(new PotionEffect(Potion.confusion.id, dur, 0));
                            if (target instanceof EntityPlayer) {
                                EntityPlayer victim = (EntityPlayer)target;
                                // The Kiss devours the soul: strip all experience and starve the body.
                                victim.experience = 0.0F;
                                victim.experienceTotal = 0;
                                victim.experienceLevel = 0;
                                victim.getFoodStats().addStats(-20, 0.0F);
                                ChatUtil.sendTranslated(EnumChatFormatting.DARK_RED, victim, "witchery.rite.dementorkiss.victim", new Object[0]);
                            }
                            ParticleEffect.MOB_SPELL.send(SoundEffect.MOB_WITHER_DEATH, target, 1.0D, 1.0D, 16);
                            kissed = true;
                        }
                        break;
                    }
                }

                if (!kissed) {
                    return RitualStep.Result.ABORTED_REFUND;
                }
                ParticleEffect.LARGE_SMOKE.send(SoundEffect.MOB_ENDERMEN_PORTAL, world, 0.5D + (double)x, 0.5D + (double)y, 0.5D + (double)z, 1.0D, 2.0D, 16);
            }
            return RitualStep.Result.COMPLETED;
        }
    }
}
