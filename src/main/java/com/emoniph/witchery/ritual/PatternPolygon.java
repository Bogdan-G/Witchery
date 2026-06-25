package com.emoniph.witchery.ritual;

import net.minecraft.world.World;
import net.minecraft.block.Block;

public class PatternPolygon implements IRitualPattern {
    private final int[][] offsets;
    private final Block requiredGlyph;
    private final int maxRadius;
    
    public PatternPolygon(Block requiredGlyph, int[][] offsets) {
        this.requiredGlyph = requiredGlyph;
        this.offsets = offsets;
        int r = 0;
        for (int[] offset : offsets) {
            r = Math.max(r, Math.abs(offset[0]));
            r = Math.max(r, Math.abs(offset[1]));
        }
        this.maxRadius = r;
    }
    
    @Override
    public boolean isMatch(World world, int posX, int posY, int posZ) {
        for (int[] offset : offsets) {
            if (world.getBlock(posX + offset[0], posY, posZ + offset[1]) != requiredGlyph) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public int getRadius() {
        return maxRadius + 1;
    }
}
