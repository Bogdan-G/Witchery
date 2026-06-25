package com.emoniph.witchery.brewing.action.effect;

import com.emoniph.witchery.brewing.AltarPower;
import com.emoniph.witchery.brewing.BrewItemKey;
import com.emoniph.witchery.brewing.BrewNamePart;
import com.emoniph.witchery.brewing.EffectLevel;
import com.emoniph.witchery.brewing.ModifiersEffect;
import com.emoniph.witchery.brewing.Probability;
import com.emoniph.witchery.brewing.action.BrewActionEffect;
import com.emoniph.witchery.util.BlockActionCircle;
import com.emoniph.witchery.util.BlockProtect;
import com.emoniph.witchery.util.BlockUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BrewActionRegrowth extends BrewActionEffect {

    public BrewActionRegrowth(BrewItemKey itemKey, BrewNamePart namePart, AltarPower powerCost, Probability baseProbability, EffectLevel effectLevel) {
        super(itemKey, namePart, powerCost, baseProbability, effectLevel);
    }

    protected void doApplyToBlock(World world, int x, int y, int z, ForgeDirection side, int radius, final ModifiersEffect modifiers, ItemStack stack) {
        if (BlockUtil.isReplaceableBlock(world, x, y, z)) {
            --y;
        }

        (new BlockActionCircle() {
            public void onBlock(World world, int x, int y, int z) {
                if (!BlockProtect.checkModsForBreakOK(world, x, y, z, modifiers.caster)) {
                    return;
                }
                Block ground = world.getBlock(x, y, z);
                // Bring barren ground back to life.
                if (ground == Blocks.dirt || ground == Blocks.sand || ground == Blocks.gravel) {
                    world.setBlock(x, y, z, Blocks.grass);
                    ground = Blocks.grass;
                }
                if (ground == Blocks.grass && world.isAirBlock(x, y + 1, z)) {
                    int roll = world.rand.nextInt(8);
                    if (roll == 0) {
                        world.setBlock(x, y + 1, z, Blocks.yellow_flower, 0, 3);
                    } else if (roll == 1) {
                        world.setBlock(x, y + 1, z, Blocks.red_flower, world.rand.nextInt(8), 3);
                    } else if (roll <= 4) {
                        world.setBlock(x, y + 1, z, Blocks.tallgrass, 1, 3);
                    }
                }
            }
        }).processFilledCircle(world, x, y + 1, z, radius);
    }

    protected void doApplyToEntity(World world, EntityLivingBase targetEntity, ModifiersEffect modifiers, ItemStack actionStack) {
        if (targetEntity instanceof EntityAnimal) {
            // Nature's vigour: heal and hasten the breeding of beasts.
            targetEntity.addPotionEffect(new PotionEffect(Potion.regeneration.id, 200, modifiers.getStrength()));
            EntityAnimal animal = (EntityAnimal)targetEntity;
            if (!animal.isInLove() && !animal.isChild() && world.rand.nextInt(3) == 0) {
                animal.func_146082_f((EntityPlayer)null);
            }
        }
    }
}
