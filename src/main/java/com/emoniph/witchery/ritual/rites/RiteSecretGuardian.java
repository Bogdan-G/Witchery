package com.emoniph.witchery.ritual.rites;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockBarrier;
import com.emoniph.witchery.blocks.BlockCircle;
import com.emoniph.witchery.ritual.Rite;
import com.emoniph.witchery.ritual.RitualStep;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.emoniph.witchery.util.TimeUtil;
import java.util.ArrayList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class RiteSecretGuardian extends Rite {
    @Override
    public void addSteps(ArrayList steps, int initialStage) {
        steps.add(new StepRiteSecretGuardian(this, initialStage));
    }

    private static class StepRiteSecretGuardian extends RitualStep {
        private final RiteSecretGuardian rite;

        public StepRiteSecretGuardian(RiteSecretGuardian rite, int initialStage) {
            super(false);
            this.rite = rite;
        }

        @Override
        public RitualStep.Result process(World world, int x, int y, int z, long ticks, BlockCircle.TileEntityCircle.ActivatedRitual circleType) {
            if (ticks % 20L != 0L) {
                return RitualStep.Result.STARTING;
            }
            if (!world.isRemote) {
                EntityPlayer owner = circleType.getInitiatingPlayer(world);
                int duration = TimeUtil.minsToTicks(10) + TimeUtil.minsToTicks(5) * circleType.covenSize;
                int radius = 5;
                int radiusSq = radius * radius;
                int height = 5;
                int placed = 0;
                for (int dx = -radius; dx <= radius; ++dx) {
                    for (int dz = -radius; dz <= radius; ++dz) {
                        for (int dy = 0; dy <= height; ++dy) {
                            int horizSq = dx * dx + dz * dz;
                            boolean shell = horizSq >= (radius - 1) * (radius - 1) && horizSq <= radiusSq && dy < height;
                            boolean cap = dy == height && horizSq <= (radius - 2) * (radius - 2);
                            if (shell || cap) {
                                int bx = x + dx;
                                int by = y + dy;
                                int bz = z + dz;
                                if (world.isAirBlock(bx, by, bz)) {
                                    BlockBarrier.setBlock(world, bx, by, bz, duration, true, owner);
                                    ++placed;
                                }
                            }
                        }
                    }
                }

                if (placed == 0) {
                    return RitualStep.Result.ABORTED_REFUND;
                }
                ParticleEffect.INSTANT_SPELL.send(SoundEffect.RANDOM_LEVELUP, world, 0.5D + (double)x, (double)y + 1.0D, 0.5D + (double)z, 3.0D, 3.0D, 32);
            }
            return RitualStep.Result.COMPLETED;
        }
    }
}
