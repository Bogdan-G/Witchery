package com.emoniph.witchery.ritual;

import net.minecraft.world.World;
import net.minecraft.block.Block;

public class PatternLine implements IRitualPattern {
    private final int length;
    private final Block requiredGlyph;
    
    public PatternLine(int length, Block requiredGlyph) {
        this.length = length;
        this.requiredGlyph = requiredGlyph;
    }
    
    @Override
    public boolean isMatch(World world, int posX, int posY, int posZ) {
        // check x axis line
        boolean matchX = true;
        for (int i = 0; i < length; i++) {
            int offset = i - (length / 2);
            if (world.getBlock(posX + offset, posY, posZ) != requiredGlyph) {
                matchX = false;
                break;
            }
        }
        if (matchX) return true;
        
        // check z axis line
        boolean matchZ = true;
        for (int i = 0; i < length; i++) {
            int offset = i - (length / 2);
            if (world.getBlock(posX, posY, posZ + offset) != requiredGlyph) {
                matchZ = false;
                break;
            }
        }
        return matchZ;
    }
    
    @Override
    public int getRadius() {
        return length / 2 + 1;
    }
}
