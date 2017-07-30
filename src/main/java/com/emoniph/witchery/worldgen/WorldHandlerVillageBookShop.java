package com.emoniph.witchery.worldgen;

import com.emoniph.witchery.worldgen.ComponentVillageBookShop;
import cpw.mods.fml.common.registry.VillagerRegistry.IVillageCreationHandler;
import java.util.List;
import java.util.Random;
import net.minecraft.world.gen.structure.StructureVillagePieces.PieceWeight;
import net.minecraft.world.gen.structure.StructureVillagePieces.Start;

public class WorldHandlerVillageBookShop implements IVillageCreationHandler {

   public PieceWeight getVillagePieceWeight(Random random, int size) {
      return new PieceWeight(ComponentVillageBookShop.class, 15, 1 + (size > 2?random.nextInt(2):0));
   }

   public Class getComponentClass() {
      return ComponentVillageBookShop.class;
   }

   public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int p1, int p2, int p3, int p4, int p5) {
      return ComponentVillageBookShop.construct(startPiece, pieces, random, p1, p2, p3, p4, p5);
   }
}
