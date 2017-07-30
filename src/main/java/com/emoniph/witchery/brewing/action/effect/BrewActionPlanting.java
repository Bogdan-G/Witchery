package com.emoniph.witchery.brewing.action.effect;

import com.emoniph.witchery.brewing.AltarPower;
import com.emoniph.witchery.brewing.BrewItemKey;
import com.emoniph.witchery.brewing.BrewNamePart;
import com.emoniph.witchery.brewing.EffectLevel;
import com.emoniph.witchery.brewing.ModifiersEffect;
import com.emoniph.witchery.brewing.Probability;
import com.emoniph.witchery.brewing.action.BrewActionEffect;
import com.emoniph.witchery.util.BlockActionCircle;
import com.emoniph.witchery.util.EntityUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

public class BrewActionPlanting extends BrewActionEffect {

   public BrewActionPlanting(BrewItemKey itemKey, BrewNamePart namePart, AltarPower powerCost, EffectLevel effectLevel) {
      super(itemKey, namePart, powerCost, new Probability(1.0D), effectLevel);
   }

   protected void doApplyToEntity(World world, EntityLivingBase targetEntity, ModifiersEffect modifiers, ItemStack stack) {}

   protected void doApplyToBlock(World world, int x, int y, int z, ForgeDirection side, int radius, final ModifiersEffect modifiers, ItemStack stack) {
      int R = radius + modifiers.getStrength();
      double RADIUS_SQ = (double)(R * R);
      AxisAlignedBB areaOfEffect = AxisAlignedBB.getBoundingBox((double)(x - R), (double)(y - R), (double)(z - R), (double)(x + R), (double)(y + R), (double)(z + R));
      List entities = world.getEntitiesWithinAABB(EntityItem.class, areaOfEffect);
      if(entities != null && !entities.isEmpty()) {
         final ArrayList seeds = new ArrayList();
         Iterator Y_RANGE = entities.iterator();

         while(Y_RANGE.hasNext()) {
            EntityItem itemEntity = (EntityItem)Y_RANGE.next();
            ItemStack seedStack = itemEntity.getEntityItem();
            if(itemEntity.getDistanceSq((double)x, (double)y, (double)z) <= RADIUS_SQ && seedStack != null && seedStack.getItem() instanceof IPlantable) {
               seeds.add(seedStack);
            }
         }

         boolean Y_RANGE1 = true;
         (new BlockActionCircle() {
            public void onBlock(World world, int x, int y, int z) {
               int index = seeds.size() - 1;
               if(index >= 0) {
                  ItemStack seed = (ItemStack)seeds.get(index);

                  for(int dy = y - 2; dy <= y + 2; ++dy) {
                     Block block = world.getBlock(x, dy, z);
                     if(block.getMaterial().isSolid() && world.isAirBlock(x, dy + 1, z) && seed.getItem().onItemUse(seed, EntityUtil.playerOrFake(world, (EntityLivingBase)modifiers.caster), world, x, dy, z, 1, 0.0F, 0.0F, 0.0F)) {
                        break;
                     }
                  }

                  if(seed.stackSize <= 0) {
                     seeds.remove(index);
                  }
               }

            }
         }).processFilledCircle(world, x, y, z, radius + modifiers.getStrength());
      }

   }
}
