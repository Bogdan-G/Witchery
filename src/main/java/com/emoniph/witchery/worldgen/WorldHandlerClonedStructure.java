package com.emoniph.witchery.worldgen;

import com.emoniph.witchery.util.Config;
import com.emoniph.witchery.worldgen.ComponentClonedStructure;
import com.emoniph.witchery.worldgen.IWorldGenHandler;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;
import net.minecraft.world.World;

public class WorldHandlerClonedStructure implements IWorldGenHandler {

   private final double chance;
   private final int range;
   private final int width;
   private final int height;
   private final int depth;
   Class clazz;


   public WorldHandlerClonedStructure(Class clazz, double chance, int range, int width, int height, int depth) {
      this.clazz = clazz;
      this.chance = chance;
      this.range = range;
      this.width = width;
      this.height = height;
      this.depth = depth;
   }

   public int getExtentX() {
      return this.width;
   }

   public int getExtentZ() {
      return this.depth;
   }

   public int getRange() {
      return this.range;
   }

   public boolean generate(World world, Random random, int x, int z) {
      if(Config.instance().generateGoblinHuts && random.nextDouble() < this.chance) {
         int direction = random.nextInt(4);

         try {
            Constructor ex = this.clazz.getConstructor(new Class[]{Integer.TYPE, Random.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE});
            ComponentClonedStructure component = (ComponentClonedStructure)ex.newInstance(new Object[]{Integer.valueOf(direction), random, Integer.valueOf(x), Integer.valueOf(z), Integer.valueOf(this.width), Integer.valueOf(this.height), Integer.valueOf(this.depth)});
            component.addComponentParts(world, random);
         } catch (NoSuchMethodException var8) {
            ;
         } catch (InvocationTargetException var9) {
            ;
         } catch (InstantiationException var10) {
            ;
         } catch (IllegalAccessException var11) {
            ;
         }

         return true;
      } else {
         return false;
      }
   }

   public void initiate() {}
}
