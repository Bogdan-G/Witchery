package com.emoniph.witchery.ritual.rites;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockCircle;
import com.emoniph.witchery.ritual.Rite;
import com.emoniph.witchery.ritual.RitualStep;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class RitePhilosopherStone extends Rite {
    @Override
    public void addSteps(ArrayList steps, int initialStage) {
        steps.add(new StepRitePhilosopherStone(this, initialStage));
    }

    private static class StepRitePhilosopherStone extends RitualStep {
        private final RitePhilosopherStone rite;
        private static final int RADIUS = 4;

        public StepRitePhilosopherStone(RitePhilosopherStone rite, int initialStage) {
            super(false);
            this.rite = rite;
        }

        @Override
        public RitualStep.Result process(World world, int x, int y, int z, long ticks, BlockCircle.TileEntityCircle.ActivatedRitual circleType) {
            if (ticks % 20L != 0L) {
                return RitualStep.Result.STARTING;
            }
            if (!world.isRemote) {
                int transmuted = 0;
                // The Great Work: base metals and stone become gold.
                for (int dx = -RADIUS; dx <= RADIUS; ++dx) {
                    for (int dz = -RADIUS; dz <= RADIUS; ++dz) {
                        for (int dy = -3; dy <= 0; ++dy) {
                            int bx = x + dx;
                            int by = y + dy;
                            int bz = z + dz;
                            Block block = world.getBlock(bx, by, bz);
                            if (block == Blocks.iron_block || block == Blocks.iron_ore) {
                                world.setBlock(bx, by, bz, Blocks.gold_block, 0, 3);
                                ++transmuted;
                            } else if (block == Blocks.cobblestone || block == Blocks.gravel) {
                                world.setBlock(bx, by, bz, Blocks.iron_ore, 0, 3);
                                ++transmuted;
                            }
                        }
                    }
                }

                // The stone yields its priceless gift regardless of nearby ore.
                spawnReward(world, x, y, z, new ItemStack(Items.diamond, 1));
                spawnReward(world, x, y, z, new ItemStack(Items.gold_ingot, 3));
                spawnReward(world, x, y, z, Witchery.Items.GENERIC.itemAttunedStoneCharged.createStack());

                ParticleEffect.INSTANT_SPELL.send(SoundEffect.RANDOM_LEVELUP, world, 0.5D + (double)x, (double)y + 1.0D, 0.5D + (double)z, 2.0D, 2.0D, 24);
            }
            return RitualStep.Result.COMPLETED;
        }

        private static void spawnReward(World world, int x, int y, int z, ItemStack stack) {
            EntityItem drop = new EntityItem(world, 0.5D + (double)x, (double)y + 1.5D, 0.5D + (double)z, stack);
            drop.motionX = 0.0D;
            drop.motionY = 0.3D;
            drop.motionZ = 0.0D;
            world.spawnEntityInWorld(drop);
        }
    }
}
