package com.emoniph.witchery.ritual.rites;

import com.emoniph.witchery.blocks.BlockCircle;
import com.emoniph.witchery.ritual.Rite;
import com.emoniph.witchery.ritual.RitualStep;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import java.util.ArrayList;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class RitePromisedLand extends Rite {
    @Override
    public void addSteps(ArrayList steps, int initialStage) {
        steps.add(new StepRitePromisedLand(this, initialStage));
    }

    private static class StepRitePromisedLand extends RitualStep {
        private final RitePromisedLand rite;
        private static final int RADIUS = 8;

        public StepRitePromisedLand(RitePromisedLand rite, int initialStage) {
            super(false);
            this.rite = rite;
        }

        @Override
        public RitualStep.Result process(World world, int x, int y, int z, long ticks, BlockCircle.TileEntityCircle.ActivatedRitual circleType) {
            if (ticks % 20L != 0L) {
                return RitualStep.Result.STARTING;
            }
            if (!world.isRemote) {
                Random rand = world.rand;
                int radiusSq = RADIUS * RADIUS;
                int blessed = 0;
                for (int dx = -RADIUS; dx <= RADIUS; ++dx) {
                    for (int dz = -RADIUS; dz <= RADIUS; ++dz) {
                        if (dx * dx + dz * dz > radiusSq) {
                            continue;
                        }
                        int bx = x + dx;
                        int bz = z + dz;
                        // Find the topmost solid ground within a few blocks of the circle plane.
                        for (int dy = 3; dy >= -3; --dy) {
                            int by = y + dy;
                            Block ground = world.getBlock(bx, by, bz);
                            boolean canGrow = ground == Blocks.dirt || ground == Blocks.sand || ground == Blocks.gravel || ground == Blocks.grass;
                            if (canGrow && world.isAirBlock(bx, by + 1, bz)) {
                                if (ground != Blocks.grass) {
                                    world.setBlock(bx, by, bz, Blocks.grass, 0, 3);
                                }
                                decorate(world, rand, bx, by + 1, bz);
                                ++blessed;
                                break;
                            }
                        }
                    }
                }

                if (blessed == 0) {
                    return RitualStep.Result.ABORTED_REFUND;
                }
                ParticleEffect.INSTANT_SPELL.send(SoundEffect.RANDOM_LEVELUP, world, 0.5D + (double)x, (double)y + 1.0D, 0.5D + (double)z, 3.0D, 2.0D, 32);
            }
            return RitualStep.Result.COMPLETED;
        }

        private static void decorate(World world, Random rand, int x, int y, int z) {
            int roll = rand.nextInt(10);
            if (roll == 0) {
                world.setBlock(x, y, z, Blocks.yellow_flower, 0, 3);
            } else if (roll == 1) {
                world.setBlock(x, y, z, Blocks.red_flower, rand.nextInt(8), 3);
            } else if (roll <= 5) {
                world.setBlock(x, y, z, Blocks.tallgrass, 1, 3);
            }
        }
    }
}
