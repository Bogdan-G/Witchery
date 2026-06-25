package com.emoniph.witchery.ritual.rites;

import com.emoniph.witchery.blocks.BlockCircle;
import com.emoniph.witchery.ritual.Rite;
import com.emoniph.witchery.ritual.RitualStep;
import com.emoniph.witchery.util.BlockProtect;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class RiteAnnihilation extends Rite {
    @Override
    public void addSteps(ArrayList steps, int initialStage) {
        steps.add(new StepRiteAnnihilation(this, initialStage));
    }

    private static class StepRiteAnnihilation extends RitualStep {
        private final RiteAnnihilation rite;
        private static final int RADIUS = 8;

        public StepRiteAnnihilation(RiteAnnihilation rite, int initialStage) {
            super(false);
            this.rite = rite;
        }

        @Override
        public RitualStep.Result process(World world, int x, int y, int z, long ticks, BlockCircle.TileEntityCircle.ActivatedRitual circleType) {
            if (ticks % 20L != 0L) {
                return RitualStep.Result.STARTING;
            }
            if (!world.isRemote) {
                EntityPlayer caster = circleType.getInitiatingPlayer(world);
                int radiusSq = RADIUS * RADIUS;
                int cleared = 0;
                // Annihilate everything above the circle in a dome, sparing the circle floor itself.
                for (int dx = -RADIUS; dx <= RADIUS; ++dx) {
                    for (int dz = -RADIUS; dz <= RADIUS; ++dz) {
                        for (int dy = 1; dy <= RADIUS; ++dy) {
                            if (dx * dx + dy * dy + dz * dz > radiusSq) {
                                continue;
                            }
                            int bx = x + dx;
                            int by = y + dy;
                            int bz = z + dz;
                            Block block = world.getBlock(bx, by, bz);
                            if (block == Blocks.air) {
                                continue;
                            }
                            int meta = world.getBlockMetadata(bx, by, bz);
                            if (BlockProtect.canBreak(block, world) && BlockProtect.checkModsForBreakOK(world, bx, by, bz, block, meta, caster)) {
                                world.setBlockToAir(bx, by, bz);
                                ++cleared;
                            }
                        }
                    }
                }
                ParticleEffect.HUGE_EXPLOSION.send(SoundEffect.RANDOM_EXPLODE, world, 0.5D + (double)x, (double)y + 2.0D, 0.5D + (double)z, 3.0D, 3.0D, 48);
            }
            return RitualStep.Result.COMPLETED;
        }
    }
}
