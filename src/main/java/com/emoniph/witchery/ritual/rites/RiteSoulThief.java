package com.emoniph.witchery.ritual.rites;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockCircle;
import com.emoniph.witchery.entity.EntityWitchHunter;
import com.emoniph.witchery.ritual.Rite;
import com.emoniph.witchery.ritual.RitualStep;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.emoniph.witchery.util.TimeUtil;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class RiteSoulThief extends Rite {
    @Override
    public void addSteps(ArrayList steps, int initialStage) {
        steps.add(new StepRiteSoulThief(this, initialStage));
    }

    private static class StepRiteSoulThief extends RitualStep {
        private final RiteSoulThief rite;

        public StepRiteSoulThief(RiteSoulThief rite, int initialStage) {
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
                boolean stolen = false;
                Iterator i$ = circleType.sacrificedItems.iterator();
                while (i$.hasNext()) {
                    RitualStep.SacrificedItem item = (RitualStep.SacrificedItem)i$.next();
                    if (item.itemstack.getItem() == Witchery.Items.TAGLOCK_KIT && item.itemstack.getItemDamage() == 1) {
                        EntityLivingBase target = Witchery.Items.TAGLOCK_KIT.getBoundEntity(world, (Entity)null, item.itemstack, Integer.valueOf(1));
                        if (target != null) {
                            EntityWitchHunter.blackMagicPerformed(initiator);
                            // Tear a fragment of the soul free: serious magic damage and lingering weakness.
                            target.attackEntityFrom(DamageSource.magic, 12.0F);
                            int dur = TimeUtil.secsToTicks(60);
                            target.addPotionEffect(new PotionEffect(Potion.weakness.id, dur, 1));
                            target.addPotionEffect(new PotionEffect(Potion.digSlowdown.id, dur, 1));
                            target.addPotionEffect(new PotionEffect(Potion.hunger.id, dur, 1));

                            // The harvested soul manifests as a subdued spirit.
                            ItemStack soul = Witchery.Items.GENERIC.itemSubduedSpirit.createStack();
                            EntityItem drop = new EntityItem(world, 0.5D + (double)x, (double)y + 1.5D, 0.5D + (double)z, soul);
                            drop.motionX = 0.0D;
                            drop.motionY = 0.3D;
                            drop.motionZ = 0.0D;
                            world.spawnEntityInWorld(drop);
                            ParticleEffect.MOB_SPELL.send(SoundEffect.MOB_ENDERMEN_PORTAL, target, 1.0D, 1.0D, 16);
                            stolen = true;
                        }
                        break;
                    }
                }

                if (!stolen) {
                    return RitualStep.Result.ABORTED_REFUND;
                }
                ParticleEffect.PORTAL.send(SoundEffect.MOB_WITHER_SPAWN, world, 0.5D + (double)x, (double)y + 1.0D, 0.5D + (double)z, 1.0D, 2.0D, 16);
            }
            return RitualStep.Result.COMPLETED;
        }
    }
}
