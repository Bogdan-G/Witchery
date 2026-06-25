package com.emoniph.witchery.ritual.rites;

import com.emoniph.witchery.blocks.BlockCircle;
import com.emoniph.witchery.entity.EntityDarkMark;
import com.emoniph.witchery.ritual.Rite;
import com.emoniph.witchery.ritual.RitualStep;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import java.util.ArrayList;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class RiteMorsmordre extends Rite {
    @Override
    public void addSteps(ArrayList steps, int initialStage) {
        steps.add(new StepRiteMorsmordre(this, initialStage));
    }

    private static class StepRiteMorsmordre extends RitualStep {
        private final RiteMorsmordre rite;

        public StepRiteMorsmordre(RiteMorsmordre rite, int initialStage) {
            super(false);
            this.rite = rite;
        }

        @Override
        public RitualStep.Result process(World world, int x, int y, int z, long ticks, BlockCircle.TileEntityCircle.ActivatedRitual circleType) {
            if (ticks % 20L != 0L) {
                return RitualStep.Result.STARTING;
            }
            if (!world.isRemote) {
                EntityDarkMark mark = new EntityDarkMark(world);
                mark.setLocationAndAngles(0.5D + (double)x, (double)y + 12.0D, 0.5D + (double)z, 0.0F, 0.0F);
                mark.func_110163_bv();
                world.spawnEntityInWorld((Entity)mark);
                ParticleEffect.LARGE_SMOKE.send(SoundEffect.MOB_ENDERDRAGON_GROWL, world, 0.5D + (double)x, (double)y + 1.0D, 0.5D + (double)z, 2.0D, 2.0D, 32);
            }
            return RitualStep.Result.COMPLETED;
        }
    }
}
