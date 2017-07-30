package com.emoniph.witchery.common;

import com.emoniph.witchery.blocks.BlockAreaMarker;
import com.emoniph.witchery.blocks.BlockDistillery;
import com.emoniph.witchery.blocks.BlockSpinningWheel;
import com.emoniph.witchery.blocks.BlockWitchesOven;
import com.emoniph.witchery.brewing.DispersalTriggered;
import com.emoniph.witchery.brewing.potions.WitcheryPotions;
import com.emoniph.witchery.common.GenericEvents;
import com.emoniph.witchery.entity.EntityBroom;
import com.emoniph.witchery.infusion.Infusion;
import com.emoniph.witchery.item.ItemBrewBag;
import com.emoniph.witchery.item.ItemGoblinClothes;
import com.emoniph.witchery.item.ItemLeonardsUrn;
import com.emoniph.witchery.item.ItemPoppet;
import com.emoniph.witchery.item.ItemWitchHand;
import com.emoniph.witchery.ritual.rites.RitePriorIncarnation;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.emoniph.witchery.worldgen.WorldHandlerVillageDistrict;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy implements IGuiHandler {

   private static final Map extendedEntityData = new HashMap();


   public static void storeEntityData(String name, NBTTagCompound compound) {
      extendedEntityData.put(name, compound);
   }

   public static NBTTagCompound getEntityData(String name) {
      return (NBTTagCompound)extendedEntityData.remove(name);
   }

   public void preInit() {}

   public void registerEvents() {
      MinecraftForge.EVENT_BUS.register(new ItemPoppet.PoppetEventHooks());
      MinecraftForge.EVENT_BUS.register(new Infusion.EventHooks());
      MinecraftForge.EVENT_BUS.register(new ItemWitchHand.EventHooks());
      MinecraftForge.EVENT_BUS.register(new EntityBroom.EventHooks());
      MinecraftForge.EVENT_BUS.register(new RitePriorIncarnation.EventHooks());
      MinecraftForge.EVENT_BUS.register(new BlockAreaMarker.AreaMarkerEventHooks());
      MinecraftForge.EVENT_BUS.register(new GenericEvents());
      MinecraftForge.EVENT_BUS.register(new ItemGoblinClothes.EventHooks());
      MinecraftForge.EVENT_BUS.register(new WitcheryPotions.EventHooks());
      MinecraftForge.EVENT_BUS.register(new DispersalTriggered.EventHooks());
      MinecraftForge.TERRAIN_GEN_BUS.register(new WorldHandlerVillageDistrict.EventHooks());
   }

   public void registerRenderers() {}

   public void registerServerHandlers() {}

   public void registerHandlers() {}

   public void postInit() {}

   public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
      switch(ID) {
      case 0:
         return null;
      case 1:
      case 6:
      case 7:
      default:
         return null;
      case 2:
         return new BlockWitchesOven.ContainerWitchesOven(player.inventory, (BlockWitchesOven.TileEntityWitchesOven)world.getTileEntity(x, y, z));
      case 3:
         return new BlockDistillery.ContainerDistillery(player.inventory, (BlockDistillery.TileEntityDistillery)world.getTileEntity(x, y, z));
      case 4:
         return new BlockSpinningWheel.ContainerSpinningWheel(player.inventory, (BlockSpinningWheel.TileEntitySpinningWheel)world.getTileEntity(x, y, z));
      case 5:
         return new ItemBrewBag.ContainerBrewBag(player.inventory, new ItemBrewBag.InventoryBrewBag(player), player.getHeldItem());
      case 8:
         return new ItemLeonardsUrn.ContainerLeonardsUrn(player.inventory, new ItemLeonardsUrn.InventoryLeonardsUrn(player), player.getHeldItem());
      }
   }

   public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
      return null;
   }

   public boolean getGraphicsLevel() {
      return false;
   }

   public int getStockageRenderId() {
      return 0;
   }

   public int getGasRenderId() {
      return 0;
   }

   public int getPitGrassRenderId() {
      return 0;
   }

   public int getBrewLiquidRenderId() {
      return 0;
   }

   public void registerVillagers() {}

   public void generateParticle(World worldObj, double posX, double posY, double posZ, float f, float g, float h, int i, float j) {}

   public EntityPlayer getPlayer(MessageContext ctx) {
      return ctx.side == Side.SERVER?ctx.getServerHandler().playerEntity:null;
   }

   public int getVineRenderId() {
      return 0;
   }

   public void showParticleEffect(World world, double x, double y, double z, double width, double height, SoundEffect sound, int color, ParticleEffect particle) {}

}
