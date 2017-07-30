package com.emoniph.witchery;

import com.emoniph.witchery.WitcheryBlocks;
import com.emoniph.witchery.WitcheryEntities;
import com.emoniph.witchery.WitcheryFluids;
import com.emoniph.witchery.WitcheryItems;
import com.emoniph.witchery.WitcheryRecipes;
import com.emoniph.witchery.blocks.BlockAreaMarker;
import com.emoniph.witchery.blocks.BlockPoppetShelf;
import com.emoniph.witchery.brewing.WitcheryBrewRegistry;
import com.emoniph.witchery.brewing.potions.WitcheryPotions;
import com.emoniph.witchery.client.KeyboardHandler;
import com.emoniph.witchery.client.PlayerRender;
import com.emoniph.witchery.common.ChantCommand;
import com.emoniph.witchery.common.CommonProxy;
import com.emoniph.witchery.common.PowerSources;
import com.emoniph.witchery.common.ServerTickEvents;
import com.emoniph.witchery.dimension.WorldProviderDreamWorld;
import com.emoniph.witchery.dimension.WorldProviderMirror;
import com.emoniph.witchery.dimension.WorldProviderTorment;
import com.emoniph.witchery.integration.ModHookArsMagica2;
import com.emoniph.witchery.integration.ModHookBaubles;
import com.emoniph.witchery.integration.ModHookBloodMagic;
import com.emoniph.witchery.integration.ModHookForestry;
import com.emoniph.witchery.integration.ModHookManager;
import com.emoniph.witchery.integration.ModHookMineFactoryReloaded;
import com.emoniph.witchery.integration.ModHookMorph;
import com.emoniph.witchery.integration.ModHookMystCraft;
import com.emoniph.witchery.integration.ModHookThaumcraft4;
import com.emoniph.witchery.integration.ModHookTinkersConstruct;
import com.emoniph.witchery.integration.ModHookTreecapitator;
import com.emoniph.witchery.integration.ModHookWaila;
import com.emoniph.witchery.item.DispenseBehaviourItemBrew;
import com.emoniph.witchery.item.DispenseBehaviourItemGeneral;
import com.emoniph.witchery.network.PacketPipeline;
import com.emoniph.witchery.util.Config;
import com.emoniph.witchery.util.Log;
import com.emoniph.witchery.worldgen.BiomeManager;
import com.emoniph.witchery.worldgen.ComponentVillageApothecary;
import com.emoniph.witchery.worldgen.ComponentVillageBookShop;
import com.emoniph.witchery.worldgen.ComponentVillageWitchHut;
import com.emoniph.witchery.worldgen.WitcheryWorldGenerator;
import com.emoniph.witchery.worldgen.WorldHandlerVillageApothecary;
import com.emoniph.witchery.worldgen.WorldHandlerVillageBookShop;
import com.emoniph.witchery.worldgen.WorldHandlerVillageDistrict;
import com.emoniph.witchery.worldgen.WorldHandlerVillageWitchHut;
import com.google.common.collect.Lists;
import cpw.mods.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent.MissingMapping;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.VillagerRegistry;
import cpw.mods.fml.common.registry.GameRegistry.Type;
import cpw.mods.fml.relauncher.Side;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ForgeChunkManager.OrderedLoadingCallback;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.config.Configuration;

@Mod(
   modid = "witchery",
   name = "Witchery",
   version = "0.24.1",
   guiFactory = "com.emoniph.witchery.util.WitcheryGuiFactory",
   dependencies = "required-after:Forge@[10.13.2.1277,);after:MineFactoryReloaded;after:NotEnoughItems;after:Waila;after:ForgeMultipart;after:AWWayofTime"
)
public class Witchery {

   public static final String MOD_ID = "witchery";
   public static final String MOD_PREFIX = "WITC";
   @SidedProxy(
      clientSide = "com.emoniph.witchery.client.ClientProxy",
      serverSide = "com.emoniph.witchery.common.CommonProxy"
   )
   public static CommonProxy proxy;
   @Instance("witchery")
   public static Witchery instance;
   public static final PacketPipeline packetPipeline = new PacketPipeline();
   public static WitcheryRecipes Recipes = new WitcheryRecipes();
   public static final ModHookManager modHooks = new ModHookManager();
   public static WitcheryPotions Potions;
   public static WitcheryFluids Fluids;
   public static WitcheryBlocks Blocks;
   public static WitcheryItems Items;
   public static WitcheryEntities Entities;
   private static WitcheryWorldGenerator worldGenerator;
   public static File configDirectoryPath;
   public static Configuration config;
   public static Configuration config_debug;
   public static boolean isDeathChestModInstalled;


   @EventHandler
   public void preInit(FMLPreInitializationEvent event) {
      if(instance != this) {
         Log.instance().warning("instance static not set");
      }

      configDirectoryPath = event.getModConfigurationDirectory();
      config = new Configuration(new File(String.format("%s/%s.cfg", new Object[]{configDirectoryPath, "witchery"})));
      config_debug = new Configuration(new File(String.format("%s/%s_debug.cfg", new Object[]{configDirectoryPath, "witchery"})));
      Config.instance().init(config, config_debug);
      worldGenerator = new WitcheryWorldGenerator();
      GameRegistry.registerWorldGenerator(worldGenerator, 0);
      if(Config.instance().generateApothecaries) {
         WorldHandlerVillageApothecary villageBookShopHandler = new WorldHandlerVillageApothecary();
         VillagerRegistry.instance().registerVillagerId(Config.instance().apothecaryID);
         proxy.registerVillagers();
         VillagerRegistry.instance().registerVillageCreationHandler(villageBookShopHandler);
         VillagerRegistry.instance().registerVillageTradeHandler(Config.instance().apothecaryID, villageBookShopHandler);

         try {
            MapGenStructureIO.func_143031_a(ComponentVillageApothecary.class, "witchery:Apothecary");
         } catch (Throwable var6) {
            ;
         }
      }

      if(Config.instance().generateWitchHuts) {
         WorldHandlerVillageWitchHut villageBookShopHandler1 = new WorldHandlerVillageWitchHut();
         VillagerRegistry.instance().registerVillageCreationHandler(villageBookShopHandler1);

         try {
            MapGenStructureIO.func_143031_a(ComponentVillageWitchHut.class, "witchery:witchhut");
         } catch (Throwable var5) {
            ;
         }
      }

      if(Config.instance().generateBookShops) {
         WorldHandlerVillageBookShop villageBookShopHandler2 = new WorldHandlerVillageBookShop();
         VillagerRegistry.instance().registerVillageCreationHandler(villageBookShopHandler2);

         try {
            MapGenStructureIO.func_143031_a(ComponentVillageBookShop.class, "witchery:bookshop");
         } catch (Throwable var4) {
            ;
         }
      }

      WorldHandlerVillageDistrict.preInit();
      proxy.preInit();
      packetPipeline.preInit();
      Potions = new WitcheryPotions();
      Fluids = new WitcheryFluids();
      Blocks = new WitcheryBlocks();
      Items = new WitcheryItems();
      Entities = new WitcheryEntities();
      Recipes.preInit();
      Potions.preInit();
      FMLCommonHandler.instance().bus().register(new ServerTickEvents());
      if(event.getSide() == Side.CLIENT) {
         FMLCommonHandler.instance().bus().register(new PlayerRender());
         FMLCommonHandler.instance().bus().register(new KeyboardHandler());
      }

   }

   @EventHandler
   public void onMissingMappings(FMLMissingMappingsEvent event) {
      Iterator i$ = event.get().iterator();

      while(i$.hasNext()) {
         MissingMapping missing = (MissingMapping)i$.next();
         if(missing.name != null) {
            int index = missing.name.lastIndexOf(58);
            String strippedName = index != -1 && missing.name.length() > index?missing.name.substring(index + 1):missing.name;
            if(missing.type == Type.BLOCK) {
               Block replacement = GameRegistry.findBlock("witchery", strippedName);
               if(replacement != null) {
                  missing.remap(replacement);
               }
            } else if(missing.type == Type.ITEM) {
               Item replacement1 = GameRegistry.findItem("witchery", strippedName);
               if(replacement1 != null) {
                  missing.remap(replacement1);
               }
            }
         }
      }

   }

   @SubscribeEvent
   public void onConfigChanged(OnConfigChangedEvent event) {
      if(event.modID.equals("witchery")) {
         Config.instance().sync();
      }

   }

   @EventHandler
   public void init(FMLInitializationEvent event) {
      FMLCommonHandler.instance().bus().register(instance);
      WorldHandlerVillageDistrict.init();
      packetPipeline.init();
      Entities.init();
      NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);
      DimensionManager.registerProviderType(Config.instance().dimensionDreamID, WorldProviderDreamWorld.class, false);
      DimensionManager.registerDimension(Config.instance().dimensionDreamID, Config.instance().dimensionDreamID);
      DimensionManager.registerProviderType(Config.instance().dimensionTormentID, WorldProviderTorment.class, false);
      DimensionManager.registerDimension(Config.instance().dimensionTormentID, Config.instance().dimensionTormentID);
      DimensionManager.registerProviderType(Config.instance().dimensionMirrorID, WorldProviderMirror.class, false);
      DimensionManager.registerDimension(Config.instance().dimensionMirrorID, Config.instance().dimensionMirrorID);
      MinecraftForge.addGrassSeed(new ItemStack(Items.SEEDS_ARTICHOKE), 3);
      MinecraftForge.addGrassSeed(new ItemStack(Items.SEEDS_BELLADONNA), 4);
      MinecraftForge.addGrassSeed(new ItemStack(Items.SEEDS_MANDRAKE), 5);
      MinecraftForge.addGrassSeed(new ItemStack(Items.SEEDS_SNOWBELL), 2);
      MinecraftForge.addGrassSeed(new ItemStack(Items.SEEDS_WOLFSBANE), 1);
      MinecraftForge.addGrassSeed(new ItemStack(Items.SEEDS_GARLIC), 1);
      proxy.registerHandlers();
      proxy.registerServerHandlers();
      proxy.registerRenderers();
      isDeathChestModInstalled = Config.instance().respectOtherDeathChestMods && (Loader.isModLoaded("tombstone") || Loader.isModLoaded("OpenBlocks") || Loader.isModLoaded("Taigore_InventorySaver") || Loader.isModLoaded("KeepItemsOnDeath"));
      modHooks.register(ModHookArsMagica2.class);
      modHooks.register(ModHookBloodMagic.class);
      modHooks.register(ModHookForestry.class);
      modHooks.register(ModHookMineFactoryReloaded.class);
      modHooks.register(ModHookMystCraft.class);
      modHooks.register(ModHookThaumcraft4.class);
      modHooks.register(ModHookTinkersConstruct.class);
      modHooks.register(ModHookTreecapitator.class);
      modHooks.register(ModHookWaila.class);
      modHooks.register(ModHookMorph.class);
      modHooks.register(ModHookBaubles.class);
      modHooks.init();
      Potions.init();
      Recipes.init();
      WitcheryBrewRegistry.INSTANCE.init();
   }

   @EventHandler
   public void postInit(FMLPostInitializationEvent event) {
      Recipes.postInit();
      modHooks.postInit();

      try {
         BiomeManager.addModBiomes();
      } catch (Exception var3) {
         Log.instance().warning(var3, "Failed to add external mod biome postInit compatability");
      }

      proxy.postInit();
      proxy.registerEvents();
      BlockDispenser.dispenseBehaviorRegistry.putObject(Items.GENERIC, new DispenseBehaviourItemGeneral());
      BlockDispenser.dispenseBehaviorRegistry.putObject(Items.BREW, new DispenseBehaviourItemBrew());
      BlockDispenser.dispenseBehaviorRegistry.putObject(Items.BREW_ENDLESS_WATER, new DispenseBehaviourItemBrew());
      BlockDispenser.dispenseBehaviorRegistry.putObject(net.minecraft.init.Items.glass_bottle, new DispenseBehaviourItemBrew());
      BlockDispenser.dispenseBehaviorRegistry.putObject(Items.SUN_GRENADE, new DispenseBehaviourItemBrew());
      BlockDispenser.dispenseBehaviorRegistry.putObject(Items.DUP_GRENADE, new DispenseBehaviourItemBrew());
      ForgeChunkManager.setForcedChunkLoadingCallback(instance, new Witchery.ChunkloadCallback());
   }

   @EventHandler
   public void serverLoad(FMLServerStartingEvent event) {
      event.registerServerCommand(new ChantCommand());
      PowerSources.initiate();
      BlockAreaMarker.AreaMarkerRegistry.serverStart();
      worldGenerator.initiate();
   }

   public static String resource(String id) {
      String s = StatCollector.translateToLocal(id);
      return s.replace("|", "\n").replace("{", "§");
   }


   public static class ChunkloadCallback implements OrderedLoadingCallback {

      public void ticketsLoaded(List tickets, World world) {
         Iterator i$ = tickets.iterator();

         while(i$.hasNext()) {
            Ticket ticket = (Ticket)i$.next();
            int posX = ticket.getModData().getInteger("poppetX");
            int posY = ticket.getModData().getInteger("poppetY");
            int posZ = ticket.getModData().getInteger("poppetZ");
            BlockPoppetShelf.TileEntityPoppetShelf tileEntity = (BlockPoppetShelf.TileEntityPoppetShelf)world.getTileEntity(posX, posY, posZ);
            tileEntity.forceChunkLoading(ticket);
         }

      }

      public List ticketsLoaded(List tickets, World world, int maxTicketCount) {
         ArrayList validTickets = Lists.newArrayList();
         Iterator i$ = tickets.iterator();

         while(i$.hasNext()) {
            Ticket ticket = (Ticket)i$.next();
            int posX = ticket.getModData().getInteger("poppetX");
            int posY = ticket.getModData().getInteger("poppetY");
            int posZ = ticket.getModData().getInteger("poppetZ");
            Block block = world.getBlock(posX, posY, posZ);
            if(block == Witchery.Blocks.POPPET_SHELF) {
               validTickets.add(ticket);
            }
         }

         return validTickets;
      }
   }
}
