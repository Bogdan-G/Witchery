package com.emoniph.witchery.dimension;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockDreamCatcher;
import com.emoniph.witchery.blocks.BlockFetish;
import com.emoniph.witchery.entity.EntityCorpse;
import com.emoniph.witchery.entity.EntityDemon;
import com.emoniph.witchery.entity.EntityNightmare;
import com.emoniph.witchery.infusion.Infusion;
import com.emoniph.witchery.infusion.infusions.spirit.InfusedSpiritEffect;
import com.emoniph.witchery.item.ItemGeneral;
import com.emoniph.witchery.network.PacketPlayerStyle;
import com.emoniph.witchery.network.PacketPushTarget;
import com.emoniph.witchery.util.ChatUtil;
import com.emoniph.witchery.util.Config;
import com.emoniph.witchery.util.Coord;
import com.emoniph.witchery.util.Log;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.ForgeDirection;

public class WorldProviderDreamWorld extends WorldProvider {

   int nightmare = 0;
   private static final String SPIRIT_WORLD_KEY = "WITCSpiritWorld";
   private static final String SPIRIT_WORLD_WALKING_KEY = "WITCSpiritWalking";
   private static final String SPIRIT_WORLD_NIGHTMARE_KEY = "Nightmare";
   private static final String SPIRIT_WORLD_DEMONIC_KEY = "Demonic";
   private static final String SPIRIT_WORLD_OVERWORLD_BODY_KEY = "OverworldBody";
   private static final String SPIRIT_WORLD_OVERWORLD_HEALTH_KEY = "OverworldHealth";
   private static final String SPIRIT_WORLD_SPIRIT_HEALTH_KEY = "SpiritHealth";
   private static final String SPIRIT_WORLD_OVERWORLD_HUNGER_FOOD_KEY = "OverworldHunger";
   private static final String SPIRIT_WORLD_SPIRIT_HUNGER_FOOD_KEY = "SpiritHunger";
   private static final String SPIRIT_WORLD_OVERWORLD_INVENTORY_KEY = "OverworldInventory";
   private static final String SPIRIT_WORLD_SPIRIT_INVENTORY_KEY = "SpiritInventory";
   private static final String SPIRIT_WORLD_MANIFEST_GHOST_KEY = "WITCManifested";
   public static final String SPIRIT_WORLD_MANIFEST_TIME_KEY = "WITCManifestDuration";
   public static final String SPIRIT_WORLD_AWAKEN_PLAYER_KEY = "WITCForceAwaken";
   private static final String SPIRIT_WORLD_LAST_NIGHTMARE_KILL_KEY = "LastNightmareKillTime";
   public static final String SPIRIT_WORLD_MANIFEST_SKIP_TIME_TICK_KEY = "WITCManifestSkipTick";


   public void setDimension(int dim) {
      super.dimensionId = dim;
      super.setDimension(dim);
   }

   public long getSeed() {
      Long seed = Long.valueOf(super.getSeed());
      return seed.longValue();
   }

   public IChunkProvider createChunkGenerator() {
      WorldProvider overworldProvider = DimensionManager.getProvider(0);
      return overworldProvider.terrainType.getChunkGenerator(super.worldObj, super.worldObj.getWorldInfo().getGeneratorOptions());
   }

   public void registerWorldChunkManager() {
      super.registerWorldChunkManager();
      super.dimensionId = Config.instance().dimensionDreamID;
   }

   public String getWelcomeMessage() {
      return this instanceof WorldProviderDreamWorld?"Entering the " + this.getDimensionName():null;
   }

   public String getDepartMessage() {
      return this instanceof WorldProviderDreamWorld?"Leaving the " + this.getDimensionName():null;
   }

   public String getDimensionName() {
      return "Spirit World";
   }

   public float getStarBrightness(float par1) {
      return 0.0F;
   }

   public boolean canRespawnHere() {
      return false;
   }

   public double getMovementFactor() {
      return 1.0D;
   }

   public float calculateCelestialAngle(long par1, float par3) {
      return this.nightmare > 0?0.5F:1.0F;
   }

   public float getCloudHeight() {
      return 0.0F;
   }

   public boolean canCoordinateBeSpawn(int par1, int par2) {
      int var3 = super.worldObj.getTopSolidOrLiquidBlock(par1, par2);
      return var3 != -1;
   }

   public ChunkCoordinates getEntrancePortalLocation() {
      return new ChunkCoordinates(100, 50, 0);
   }

   public int getAverageGroundLevel() {
      return 64;
   }

   public double getHorizon() {
      return 0.0D;
   }

   @SideOnly(Side.CLIENT)
   public boolean hasVoidParticles(boolean var1) {
      return false;
   }

   @SideOnly(Side.CLIENT)
   public boolean isSkyColored() {
      return true;
   }

   public double getVoidFogYFactor() {
      return 1.0D;
   }

   @SideOnly(Side.CLIENT)
   public Vec3 getFogColor(float par1, float par2) {
      float var3 = MathHelper.cos(par1 * 3.1415927F * 2.0F) * 2.0F + 0.5F;
      if(var3 < 0.0F) {
         var3 = 0.0F;
      }

      if(var3 > 1.0F) {
         var3 = 1.0F;
      }

      float var4;
      float var5;
      float var6;
      if(this.nightmare == 0) {
         var4 = 0.8F;
         var5 = 0.2F;
         var6 = 0.6F;
      } else if(this.nightmare == 1) {
         var4 = 0.0F;
         var5 = 1.0F;
         var6 = 0.0F;
      } else {
         var4 = 1.0F;
         var5 = 0.0F;
         var6 = 0.0F;
      }

      var4 *= var3 * 0.94F + 0.06F;
      var5 *= var3 * 0.94F + 0.06F;
      var6 *= var3 * 0.91F + 0.09F;
      return Vec3.createVectorHelper((double)var4, (double)var5, (double)var6);
   }

   public void setAllowedSpawnTypes(boolean allowHostile, boolean allowPeaceful) {
      allowPeaceful = true;
   }

   public void updateWeather() {
      if(super.worldObj != null && super.worldObj.rand.nextInt(20) == 0) {
         int playerHasNightmare = 0;
         Iterator i$ = super.worldObj.playerEntities.iterator();

         while(i$.hasNext()) {
            Object obj = i$.next();
            EntityPlayer player = (EntityPlayer)obj;
            int level = getPlayerHasNightmare(player);
            if(level > playerHasNightmare) {
               playerHasNightmare = level;
               break;
            }
         }

         if(this.nightmare != playerHasNightmare) {
            this.nightmare = playerHasNightmare;
         }
      }

      super.updateWeather();
   }

   public boolean isNightmare() {
      return this.nightmare > 0;
   }

   public boolean isDemonicNightmare() {
      return this.nightmare > 1;
   }

   public static int getPlayerHasNightmare(EntityPlayer player) {
      NBTTagCompound nbtPlayer = Infusion.getNBT(player);
      return getPlayerHasNightmare(nbtPlayer);
   }

   public static int getPlayerHasNightmare(NBTTagCompound nbtPlayer) {
      if(!nbtPlayer.hasKey("WITCSpiritWorld")) {
         return 0;
      } else {
         NBTTagCompound nbtSpirit = nbtPlayer.getCompoundTag("WITCSpiritWorld");
         boolean nightmare = nbtSpirit.getBoolean("Nightmare");
         boolean demonic = nbtSpirit.getBoolean("Demonic");
         return nightmare && demonic?2:(nightmare?1:0);
      }
   }

   public static void setPlayerHasNightmare(EntityPlayer player, boolean nightmare, boolean demonic) {
      NBTTagCompound nbtPlayer = Infusion.getNBT(player);
      setPlayerHasNightmare(nbtPlayer, nightmare, demonic);
   }

   public static void setPlayerHasNightmare(NBTTagCompound nbtPlayer, boolean nightmare, boolean demonic) {
      if(!nbtPlayer.hasKey("WITCSpiritWorld")) {
         nbtPlayer.setTag("WITCSpiritWorld", new NBTTagCompound());
      }

      NBTTagCompound nbtSpirit = nbtPlayer.getCompoundTag("WITCSpiritWorld");
      nbtSpirit.setBoolean("Nightmare", nightmare);
      nbtSpirit.setBoolean("Demonic", demonic);
   }

   public static void setPlayerLastNightmareKillNow(EntityPlayer player) {
      if(player != null) {
         NBTTagCompound nbtPlayer = Infusion.getNBT(player);
         long time = MinecraftServer.getSystemTimeMillis();
         setPlayerLastNightmareKill(nbtPlayer, time);
      }

   }

   public static void setPlayerLastNightmareKill(NBTTagCompound nbtPlayer, long time) {
      if(!nbtPlayer.hasKey("WITCSpiritWorld")) {
         nbtPlayer.setTag("WITCSpiritWorld", new NBTTagCompound());
      }

      NBTTagCompound nbtSpirit = nbtPlayer.getCompoundTag("WITCSpiritWorld");
      nbtSpirit.setLong("LastNightmareKillTime", time);
   }

   public static long getPlayerLastNightmareKill(NBTTagCompound nbtPlayer) {
      if(!nbtPlayer.hasKey("WITCSpiritWorld")) {
         return 0L;
      } else {
         NBTTagCompound nbtSpirit = nbtPlayer.getCompoundTag("WITCSpiritWorld");
         if(!nbtSpirit.hasKey("LastNightmareKillTime")) {
            return 0L;
         } else {
            long time = nbtSpirit.getLong("LastNightmareKillTime");
            return time;
         }
      }
   }

   public static boolean getPlayerIsSpiritWalking(EntityPlayer player) {
      NBTTagCompound nbtPlayer = Infusion.getNBT(player);
      return getPlayerIsSpiritWalking(nbtPlayer);
   }

   public static boolean getPlayerIsSpiritWalking(NBTTagCompound nbtPlayer) {
      boolean walking = nbtPlayer.getBoolean("WITCSpiritWalking");
      return walking;
   }

   public static void setPlayerIsSpiritWalking(EntityPlayer player, boolean walking) {
      NBTTagCompound nbtPlayer = Infusion.getNBT(player);
      setPlayerIsSpiritWalking(nbtPlayer, walking);
   }

   public static void setPlayerIsSpiritWalking(NBTTagCompound nbtPlayer, boolean walking) {
      nbtPlayer.setBoolean("WITCSpiritWalking", walking);
   }

   private static void addItemToInventory(EntityPlayer player, ItemStack protoStack, int totalQuantity) {
      if(totalQuantity > 0) {
         int itemsRemaining = totalQuantity;
         int maxStack = protoStack.getMaxStackSize();

         while(itemsRemaining > 0) {
            int quantity = itemsRemaining > maxStack?maxStack:itemsRemaining;
            itemsRemaining -= quantity;
            ItemStack newStack = new ItemStack(protoStack.getItem(), quantity, protoStack.getItemDamage());
            player.inventory.addItemStackToInventory(newStack);
         }
      }

   }

   private static void addItemToInventory(EntityPlayer player, ArrayList stacks) {
      Iterator i$ = stacks.iterator();

      while(i$.hasNext()) {
         ItemStack stack = (ItemStack)i$.next();
         if(!player.inventory.addItemStackToInventory(stack)) {
            player.worldObj.spawnEntityInWorld(new EntityItem(player.worldObj, player.posX, 0.5D + player.posY, player.posZ, stack));
         }
      }

   }

   public static void sendPlayerToSpiritWorld(EntityPlayer player, double nightmareChance) {
      if(player != null && !player.worldObj.isRemote) {
         NBTTagCompound nbtPlayer = Infusion.getNBT(player);
         if(!nbtPlayer.hasKey("WITCSpiritWorld")) {
            nbtPlayer.setTag("WITCSpiritWorld", new NBTTagCompound());
         }

         NBTTagCompound nbtSpirit = nbtPlayer.getCompoundTag("WITCSpiritWorld");
         Coord posBody = new Coord(player);
         posBody.setNBT(nbtSpirit, "OverworldBody");
         int fireFound = 0;
         int heartsFound = 0;
         int spiritPoolFound = 0;
         int cottonFound = 0;
         boolean nightmareCatcherFound = false;
         double modifiedNightmareChance = nightmareChance;
         boolean nightmare;
         int mutandis;
         int boneNeedles;
         if(nightmareChance > 0.0D && nightmareChance < 1.0D) {
            nightmare = true;
            int demonic = MathHelper.floor_double(player.posX);
            int corpse = MathHelper.floor_double(player.posY);
            boneNeedles = MathHelper.floor_double(player.posZ);

            for(mutandis = demonic - 8; mutandis <= demonic + 8; ++mutandis) {
               for(int nbtOverworldInventory = boneNeedles - 8; nbtOverworldInventory <= boneNeedles + 8; ++nbtOverworldInventory) {
                  for(int nbtOverworldFood = corpse - 8; nbtOverworldFood <= corpse + 8; ++nbtOverworldFood) {
                     Block nbtSpiritFood = player.worldObj.getBlock(mutandis, nbtOverworldFood, nbtOverworldInventory);
                     if(!nightmareCatcherFound && nbtSpiritFood == Witchery.Blocks.DREAM_CATCHER) {
                        BlockDreamCatcher var10000 = Witchery.Blocks.DREAM_CATCHER;
                        if(BlockDreamCatcher.causesNightmares(player.worldObj, mutandis, nbtOverworldFood, nbtOverworldInventory)) {
                           modifiedNightmareChance -= 0.5D;
                           nightmareCatcherFound = true;
                        }
                     }

                     if(spiritPoolFound < 3 && nbtSpiritFood == Witchery.Blocks.FLOWING_SPIRIT && player.worldObj.getBlockMetadata(mutandis, nbtOverworldFood, nbtOverworldInventory) == 0) {
                        ++spiritPoolFound;
                        modifiedNightmareChance -= 0.1D;
                     }

                     if(cottonFound < 2 && nbtSpiritFood == Witchery.Blocks.WISPY_COTTON) {
                        ++cottonFound;
                        modifiedNightmareChance -= 0.1D;
                     }

                     if(heartsFound < 2 && nbtSpiritFood == Witchery.Blocks.DEMON_HEART) {
                        ++heartsFound;
                        modifiedNightmareChance += 0.35D;
                     }

                     if(fireFound < 3 && nbtSpiritFood == Blocks.fire) {
                        ++fireFound;
                        modifiedNightmareChance += 0.1D;
                     }
                  }
               }
            }

            modifiedNightmareChance = nightmareCatcherFound?Math.min(Math.max(modifiedNightmareChance, 0.0D), 1.0D):nightmareChance;
         }

         nightmare = modifiedNightmareChance != 0.0D && (modifiedNightmareChance == 1.0D || player.worldObj.rand.nextDouble() < modifiedNightmareChance);
         boolean var22 = nightmare && nightmareCatcherFound && spiritPoolFound > 0 && heartsFound > 0 && player.worldObj.rand.nextDouble() < (double)heartsFound * 0.35D + (double)fireFound * 0.1D;
         setPlayerHasNightmare(nbtPlayer, nightmare, var22);
         setPlayerIsSpiritWalking(nbtPlayer, true);
         EntityCorpse var21 = new EntityCorpse(player.worldObj);
         var21.setHealth(player.getHealth());
         var21.setCustomNameTag(player.getCommandSenderName());
         var21.setOwner(player.getCommandSenderName());
         var21.setLocationAndAngles(0.5D + (double)MathHelper.floor_double(player.posX), player.posY, 0.5D + (double)MathHelper.floor_double(player.posZ), 0.0F, 0.0F);
         player.worldObj.spawnEntityInWorld(var21);
         boneNeedles = player.inventory.clearInventory(Witchery.Items.GENERIC, Witchery.Items.GENERIC.itemIcyNeedle.damageValue);
         mutandis = player.inventory.clearInventory(Witchery.Items.GENERIC, Witchery.Items.GENERIC.itemMutandis.damageValue);
         dropBetterBackpacks(player);
         NBTTagList var23 = new NBTTagList();
         player.inventory.writeToNBT(var23);
         nbtSpirit.setTag("OverworldInventory", var23);
         if(nbtSpirit.hasKey("SpiritInventory")) {
            NBTTagList var27 = nbtSpirit.getTagList("SpiritInventory", 10);
            player.inventory.readFromNBT(var27);
            nbtSpirit.removeTag("SpiritInventory");
         } else {
            player.inventory.clearInventory((Item)null, -1);
         }

         addItemToInventory(player, Witchery.Items.GENERIC.itemIcyNeedle.createStack(), boneNeedles);
         addItemToInventory(player, Witchery.Items.GENERIC.itemMutandis.createStack(), mutandis);
         nbtSpirit.setFloat("OverworldHealth", Math.max(player.getHealth(), 1.0F));
         if(nbtSpirit.hasKey("SpiritHealth")) {
            float var26 = Math.max(nbtSpirit.getFloat("SpiritHealth"), 10.0F);
            player.setHealth(var26);
            nbtSpirit.removeTag("SpiritHealth");
         }

         NBTTagCompound var24 = new NBTTagCompound();
         player.getFoodStats().writeNBT(var24);
         nbtSpirit.setTag("OverworldHunger", var24);
         if(nbtSpirit.hasKey("SpiritHunger")) {
            NBTTagCompound var25 = nbtSpirit.getCompoundTag("SpiritHunger");
            player.getFoodStats().readNBT(var25);
            player.getFoodStats().addStats(16, 0.8F);
            nbtSpirit.removeTag("SpiritHunger");
         }

         changeDimension(player, Config.instance().dimensionDreamID);
         findTopAndSetPosition(player.worldObj, player);
         Witchery.packetPipeline.sendToAll((IMessage)(new PacketPlayerStyle(player)));
         Witchery.packetPipeline.sendTo((IMessage)(new PacketPushTarget(0.0D, 0.1D, 0.0D)), player);
      }

   }

   private static void dropBetterBackpacks(EntityPlayer player) {
      try {
         Class ex = Class.forName("net.mcft.copy.betterstorage.item.ItemBackpack");
         Method[] methods = ex.getDeclaredMethods();
         if(Config.instance().isDebugging()) {
            Method[] methodGetPackpack = methods;
            int stackBackpack = methods.length;

            for(int methodPlaceBackpack = 0; methodPlaceBackpack < stackBackpack; ++methodPlaceBackpack) {
               Method w = methodGetPackpack[methodPlaceBackpack];
               Log.instance().debug(w.toString());
            }
         }

         Method var19 = ex.getMethod("getBackpack", new Class[]{EntityLivingBase.class});
         if(var19 == null) {
            Log.instance().debug("No getBackpack method found");
         } else {
            Log.instance().debug("using method: " + var19.toString());
         }

         ItemStack var20 = (ItemStack)var19.invoke((Object)null, new Object[]{player});
         if(var20 == null) {
            Log.instance().debug("No backpack stack found");
         } else {
            Log.instance().debug("got backpack stack: " + var20.toString());
         }

         Method var21 = ex.getDeclaredMethod("placeBackpack", new Class[]{EntityLivingBase.class, EntityPlayer.class, ItemStack.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, ForgeDirection.class, Boolean.TYPE, Boolean.TYPE});
         if(var21 == null) {
            Log.instance().debug("No placebackpack method found");
         } else {
            Log.instance().debug("using method: " + var21.toString());
         }

         World var22 = player.worldObj;
         int x = MathHelper.floor_double(player.posX);
         int y = MathHelper.floor_double(player.posY);
         int z = MathHelper.floor_double(player.posZ);
         boolean found = true;
         if(isReplaceable(var22, x + 1, y, z)) {
            ++x;
         } else if(isReplaceable(var22, x - 1, y, z)) {
            --x;
         } else if(isReplaceable(var22, x, y, z + 1)) {
            ++z;
         } else if(isReplaceable(var22, x - 1, y, z - 1)) {
            --z;
         } else if(isReplaceable(var22, x + 1, y, z + 1)) {
            ++x;
            ++z;
         } else if(isReplaceable(var22, x - 1, y, z + 1)) {
            --x;
            ++z;
         } else if(isReplaceable(var22, x + 1, y, z - 1)) {
            ++x;
            --z;
         } else if(isReplaceable(var22, x - 1, y, z - 1)) {
            --x;
            --z;
         } else {
            found = false;
         }

         if(found) {
            if(!var22.getBlock(x, y - 1, z).isOpaqueCube()) {
               var22.setBlock(x, y - 1, z, Blocks.stone);
            }
         } else {
            found = true;
            ++y;
            if(isReplaceable(var22, x + 1, y, z)) {
               ++x;
            } else if(isReplaceable(var22, x - 1, y, z)) {
               --x;
            } else if(isReplaceable(var22, x, y, z + 1)) {
               ++z;
            } else if(isReplaceable(var22, x - 1, y, z - 1)) {
               --z;
            } else if(isReplaceable(var22, x + 1, y, z + 1)) {
               ++x;
               ++z;
            } else if(isReplaceable(var22, x - 1, y, z + 1)) {
               --x;
               ++z;
            } else if(isReplaceable(var22, x + 1, y, z - 1)) {
               ++x;
               --z;
            } else if(isReplaceable(var22, x - 1, y, z - 1)) {
               --x;
               --z;
            } else {
               found = false;
            }

            if(!found) {
               ++x;
               ++y;
               var22.setBlockToAir(x, y, z);
               if(!var22.getBlock(x, y - 1, z).isOpaqueCube()) {
                  var22.setBlock(x, y - 1, z, Blocks.stone);
               }
            }
         }

         Boolean result = (Boolean)var21.invoke((Object)null, new Object[]{player, player, var20, Integer.valueOf(x), Integer.valueOf(y), Integer.valueOf(z), Integer.valueOf(1), ForgeDirection.NORTH, Boolean.valueOf(false), Boolean.valueOf(false)});
         if(result.equals(Boolean.FALSE)) {
            Log.instance().debug("Backpack could not be placed");
         } else {
            Method methodSetBackpack = ex.getDeclaredMethod("setBackpack", new Class[]{EntityLivingBase.class, ItemStack.class, ItemStack[].class});
            if(methodSetBackpack == null) {
               Log.instance().debug("No setBackpack method found");
            } else {
               Log.instance().debug("using method: " + var21.toString());
            }

            methodSetBackpack.invoke((Object)null, new Object[]{player, null, null});
         }
      } catch (ClassNotFoundException var13) {
         Log.instance().debug("No class found for ItemBackpack");
      } catch (NoSuchMethodException var14) {
         Log.instance().debug("No onPlaceBackpack method found: " + var14.toString());
      } catch (InvocationTargetException var15) {
         Log.instance().debug("No onPlaceBackpack target found");
      } catch (IllegalAccessException var16) {
         Log.instance().debug("No onPlaceBackpack method access allowed");
      } catch (IndexOutOfBoundsException var17) {
         Log.instance().debug("No onPlaceBackpack method index");
      } catch (Throwable var18) {
         Log.instance().debug("Unexpected onPlaceBackpack error: " + var18.toString());
      }

   }

   private static boolean isReplaceable(World world, int x, int y, int z) {
      Material m = world.getBlock(x, y, z).getMaterial();
      return m == null?false:m.isReplaceable();
   }

   public static void changeDimension(EntityPlayer player, int dimension) {
      dismountEntity(player);
      ItemGeneral var10000 = Witchery.Items.GENERIC;
      ItemGeneral.travelToDimension(player, dimension);
   }

   private static void dismountEntity(EntityPlayer player) {
      if(player.isRiding()) {
         player.mountEntity((Entity)null);
      }

   }

   public static void findTopAndSetPosition(World world, EntityPlayer player) {
      findTopAndSetPosition(world, player, player.posX, player.posY, player.posZ);
   }

   private static void findTopAndSetPosition(World world, EntityPlayer player, double posX, double posY, double posZ) {
      int x = MathHelper.floor_double(posX);
      int y = MathHelper.floor_double(posY);
      int z = MathHelper.floor_double(posZ);
      if(!isValidSpawnPoint(world, x, y, z)) {
         for(int i = 1; i <= 256; ++i) {
            int yPlus = y + i;
            int yMinus = y - i;
            if(yPlus < 256 && isValidSpawnPoint(world, x, yPlus, z)) {
               y = yPlus;
               break;
            }

            if(yMinus > 2 && isValidSpawnPoint(world, x, yMinus, z)) {
               y = yMinus;
               break;
            }

            if(yMinus <= 2 && yPlus >= 255) {
               break;
            }
         }
      }

      player.setPositionAndUpdate(0.5D + (double)x, 0.1D + (double)y, 0.5D + (double)z);
   }

   private static boolean isValidSpawnPoint(World world, int x, int y, int z) {
      Material materialBelow = world.getBlock(x, y - 1, z).getMaterial();
      return !world.isAirBlock(x, y - 1, z) && materialBelow != Material.lava && world.isAirBlock(x, y, z) && world.isAirBlock(x, y + 1, z);
   }

   public static void returnPlayerToOverworld(EntityPlayer player) {
      if(player != null && !player.worldObj.isRemote) {
         if(player.dimension != Config.instance().dimensionDreamID) {
            Log.instance().warning("Player " + player.getDisplayName() + " is in incorrect dimension when returning frmo spirit world, dimension=" + player.dimension);
         }

         NBTTagCompound nbtPlayer = Infusion.getNBT(player);
         if(!nbtPlayer.hasKey("WITCSpiritWorld")) {
            nbtPlayer.setTag("WITCSpiritWorld", new NBTTagCompound());
         }

         NBTTagCompound nbtSpirit = nbtPlayer.getCompoundTag("WITCSpiritWorld");
         boolean isSpiritWorld = player.dimension == Config.instance().dimensionDreamID;
         int cottonRemoved = isSpiritWorld?player.inventory.clearInventory(Item.getItemFromBlock(Witchery.Blocks.WISPY_COTTON), 0):0;
         int disturbedCottonRemoved = isSpiritWorld?player.inventory.clearInventory(Witchery.Items.GENERIC, Witchery.Items.GENERIC.itemDisturbedCotton.damageValue):0;
         int hunger = isSpiritWorld?player.inventory.clearInventory(Witchery.Items.GENERIC, Witchery.Items.GENERIC.itemMellifluousHunger.damageValue):0;
         int spirit = isSpiritWorld?player.inventory.clearInventory(Witchery.Items.GENERIC, Witchery.Items.GENERIC.itemBrewOfFlowingSpirit.damageValue):0;
         int subduedSpirits = player.inventory.clearInventory(Witchery.Items.GENERIC, Witchery.Items.GENERIC.itemSubduedSpirit.damageValue);
         int boneNeedles = player.inventory.clearInventory(Witchery.Items.GENERIC, Witchery.Items.GENERIC.itemIcyNeedle.damageValue);
         dropBetterBackpacks(player);
         NBTTagList nbtSpiritFood;
         if(player.dimension == Config.instance().dimensionDreamID) {
            nbtSpiritFood = new NBTTagList();
            player.inventory.writeToNBT(nbtSpiritFood);
            nbtSpirit.setTag("SpiritInventory", nbtSpiritFood);
         }

         if(nbtSpirit.hasKey("OverworldInventory")) {
            nbtSpiritFood = nbtSpirit.getTagList("OverworldInventory", 10);
            player.inventory.readFromNBT(nbtSpiritFood);
            nbtSpirit.removeTag("OverworldInventory");
         } else {
            player.inventory.clearInventory((Item)null, -1);
         }

         addItemToInventory(player, new ItemStack(Witchery.Blocks.WISPY_COTTON, 1, 0), cottonRemoved);
         addItemToInventory(player, Witchery.Items.GENERIC.itemDisturbedCotton.createStack(), disturbedCottonRemoved);
         addItemToInventory(player, Witchery.Items.GENERIC.itemMellifluousHunger.createStack(), hunger);
         addItemToInventory(player, Witchery.Items.GENERIC.itemIcyNeedle.createStack(), boneNeedles);
         addItemToInventory(player, Witchery.Items.GENERIC.itemBrewOfFlowingSpirit.createStack(), spirit);
         addItemToInventory(player, Witchery.Items.GENERIC.itemSubduedSpirit.createStack(), subduedSpirits);
         nbtSpirit.setFloat("SpiritHealth", Math.max(player.getHealth(), 10.0F));
         if(nbtSpirit.hasKey("OverworldHealth")) {
            float nbtSpiritFood2 = nbtSpirit.getFloat("OverworldHealth");
            player.setHealth(nbtSpiritFood2);
            nbtSpirit.removeTag("OverworldHealth");
         }

         NBTTagCompound nbtSpiritFood1 = new NBTTagCompound();
         player.getFoodStats().writeNBT(nbtSpiritFood1);
         nbtSpirit.setTag("SpiritHunger", nbtSpiritFood1);
         if(nbtSpirit.hasKey("OverworldHunger")) {
            NBTTagCompound posBody = nbtSpirit.getCompoundTag("OverworldHunger");
            player.getFoodStats().readNBT(posBody);
            nbtSpirit.removeTag("OverworldHunger");
         }

         setPlayerHasNightmare(nbtPlayer, false, false);
         setPlayerIsGhost(nbtPlayer, false);
         setPlayerIsSpiritWalking(nbtPlayer, false);
         player.extinguish();
         Coord posBody1 = Coord.createFrom(nbtSpirit, "OverworldBody");
         if(player.dimension != 0) {
            if(posBody1 != null) {
               dismountEntity(player);
               player.setPositionAndUpdate((double)posBody1.x, (double)posBody1.y, (double)posBody1.z);
            }

            changeDimension(player, 0);
         }

         World world = player.worldObj;
         if(posBody1 != null) {
            findTopAndSetPosition(player.worldObj, player, (double)posBody1.x, (double)posBody1.y, (double)posBody1.z);
            nbtSpirit.removeTag("OverworldBody");
         } else {
            findTopAndSetPosition(player.worldObj, player);
         }

         Iterator i$ = player.worldObj.loadedEntityList.iterator();

         while(i$.hasNext()) {
            Object obj = i$.next();
            if(obj instanceof EntityCorpse) {
               EntityCorpse corpse = (EntityCorpse)obj;
               String owner = corpse.getOwnerName();
               if(owner != null && owner.equalsIgnoreCase(player.getCommandSenderName())) {
                  player.worldObj.removeEntity(corpse);
               }
            }
         }

         Witchery.packetPipeline.sendToAll((IMessage)(new PacketPlayerStyle(player)));
         Witchery.packetPipeline.sendTo((IMessage)(new PacketPushTarget(0.0D, 0.1D, 0.0D)), player);
      }

   }

   public static void manifestPlayerInOverworldAsGhost(EntityPlayer player) {
      if(player != null && !player.worldObj.isRemote) {
         NBTTagCompound nbtPlayer = Infusion.getNBT(player);
         if(!nbtPlayer.hasKey("WITCSpiritWorld")) {
            nbtPlayer.setTag("WITCSpiritWorld", new NBTTagCompound());
         }

         NBTTagCompound nbtSpirit = nbtPlayer.getCompoundTag("WITCSpiritWorld");
         int boneNeedles = player.inventory.clearInventory(Witchery.Items.GENERIC, Witchery.Items.GENERIC.itemIcyNeedle.damageValue);
         dropBetterBackpacks(player);
         NBTTagList nbtSpiritInventory = new NBTTagList();
         player.inventory.writeToNBT(nbtSpiritInventory);
         nbtSpirit.setTag("SpiritInventory", nbtSpiritInventory);
         player.inventory.clearInventory((Item)null, -1);
         addItemToInventory(player, Witchery.Items.GENERIC.itemIcyNeedle.createStack(), boneNeedles);
         nbtSpirit.setFloat("SpiritHealth", Math.max(player.getHealth(), 1.0F));
         setPlayerIsGhost(nbtPlayer, true);
         changeDimension(player, 0);
         findTopAndSetPosition(player.worldObj, player);
         Witchery.packetPipeline.sendToAll((IMessage)(new PacketPlayerStyle(player)));
      }

   }

   public static void returnGhostPlayerToSpiritWorld(EntityPlayer player) {
      if(player != null && !player.worldObj.isRemote) {
         NBTTagCompound nbtPlayer = Infusion.getNBT(player);
         if(!nbtPlayer.hasKey("WITCSpiritWorld")) {
            nbtPlayer.setTag("WITCSpiritWorld", new NBTTagCompound());
         }

         NBTTagCompound nbtSpirit = nbtPlayer.getCompoundTag("WITCSpiritWorld");
         int boneNeedles = player.inventory.clearInventory(Witchery.Items.GENERIC, Witchery.Items.GENERIC.itemIcyNeedle.damageValue);
         ArrayList fetishes = getBoundFetishes(player.inventory);
         player.inventory.dropAllItems();
         dropBetterBackpacks(player);
         if(nbtSpirit.hasKey("SpiritInventory")) {
            NBTTagList nbtSpiritInventory = nbtSpirit.getTagList("SpiritInventory", 10);
            player.inventory.readFromNBT(nbtSpiritInventory);
            nbtSpirit.removeTag("SpiritInventory");
         }

         addItemToInventory(player, Witchery.Items.GENERIC.itemIcyNeedle.createStack(), boneNeedles);
         addItemToInventory(player, fetishes);
         setPlayerIsGhost(nbtPlayer, false);
         changeDimension(player, Config.instance().dimensionDreamID);
         findTopAndSetPosition(player.worldObj, player);
         Witchery.packetPipeline.sendToAll((IMessage)(new PacketPlayerStyle(player)));
      }

   }

   private static ArrayList getBoundFetishes(InventoryPlayer inventory) {
      ArrayList stacks = new ArrayList();

      for(int i = 0; i < inventory.getSizeInventory(); ++i) {
         ItemStack stack = inventory.getStackInSlot(i);
         if(stack != null && stack.getItem() instanceof BlockFetish.ClassItemBlock && InfusedSpiritEffect.getEffectID(stack) > 0) {
            stacks.add(stack);
         }
      }

      return stacks;
   }

   public static void updatePlayerEffects(World world, EntityPlayer player, NBTTagCompound nbtPlayer, long time, long counter) {
      if(!world.isRemote) {
         boolean done = false;
         if(counter % 20L == 0L) {
            boolean nightmareLevel = getPlayerMustAwaken(nbtPlayer);
            if(nightmareLevel) {
               setPlayerMustAwaken(nbtPlayer, false);
               if(player.dimension != Config.instance().dimensionDreamID && getPlayerIsSpiritWalking(player) && !getPlayerIsGhost(player)) {
                  returnPlayerToOverworld(player);
               } else if(player.dimension == Config.instance().dimensionDreamID) {
                  returnPlayerToOverworld(player);
               }
            }
         }

         if(!done && counter % 100L == 0L) {
            int nightmareLevel1 = getPlayerHasNightmare(nbtPlayer);
            if(player.dimension == Config.instance().dimensionDreamID && nightmareLevel1 > 0) {
               double timeRemaining1 = 18.0D;
               double H = 18.0D;
               AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(player.posX - 18.0D, player.posY - 18.0D, player.posZ - 18.0D, player.posX + 18.0D, player.posY + 18.0D, player.posZ + 18.0D);
               if(nightmareLevel1 > 1) {
                  double entities = world.rand.nextDouble();
                  if(entities < 0.5D) {
                     EntitySmallFireball obj = new EntitySmallFireball(world, player.posX - 2.0D + (double)world.rand.nextInt(5), player.posY + 15.0D, player.posZ - 2.0D + (double)world.rand.nextInt(5), 0.0D, -0.2D, 0.0D);
                     world.spawnEntityInWorld(obj);
                  } else if(entities < 0.65D) {
                     EntityLargeFireball obj2 = new EntityLargeFireball(world);
                     double lastKillTime = player.posX - 2.0D + (double)world.rand.nextInt(5);
                     double par4 = player.posY + 15.0D;
                     double par6 = player.posZ - 2.0D + (double)world.rand.nextInt(5);
                     double par8 = 0.0D;
                     double par10 = -0.2D;
                     double par12 = 0.0D;
                     obj2.setLocationAndAngles(lastKillTime, par4, par6, obj2.rotationYaw, obj2.rotationPitch);
                     obj2.setPosition(lastKillTime, par4, par6);
                     double d6 = (double)MathHelper.sqrt_double(par8 * par8 + par10 * par10 + par12 * par12);
                     obj2.accelerationX = par8 / d6 * 0.1D;
                     obj2.accelerationY = par10 / d6 * 0.1D;
                     obj2.accelerationZ = par12 / d6 * 0.1D;
                     world.spawnEntityInWorld(obj2);
                  } else if(entities < 0.75D) {
                     List obj1 = world.getEntitiesWithinAABB(EntityMob.class, bounds);
                     if(obj1.size() < 10 && !containsDemons(obj1, 2)) {
                        new EntityDemon(world);
                        Infusion.spawnCreature(world, EntityDemon.class, MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ), player, 4, 8, ParticleEffect.SMOKE, SoundEffect.MOB_WITHER_DEATH);
                     }
                  }
               }

               List entities1 = world.getEntitiesWithinAABB(EntityNightmare.class, bounds);
               Iterator currentTime = entities1.iterator();

               while(currentTime.hasNext()) {
                  Object obj3 = currentTime.next();
                  EntityNightmare lastKillTime2 = (EntityNightmare)obj3;
                  if(lastKillTime2.getVictimName().equalsIgnoreCase(player.getCommandSenderName())) {
                     return;
                  }
               }

               long currentTime1 = MinecraftServer.getSystemTimeMillis();
               long lastKillTime1 = getPlayerLastNightmareKill(nbtPlayer);
               if(lastKillTime1 < currentTime1 - 30000L) {
                  Infusion.spawnCreature(world, EntityNightmare.class, MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ), player, 2, 6);
               }
            } else if(player.dimension != Config.instance().dimensionDreamID && getPlayerIsGhost(nbtPlayer)) {
               int timeRemaining = 0;
               boolean skipNext = getPlayerSkipNextManifestTick(nbtPlayer);
               if(nbtPlayer.hasKey("WITCManifestDuration")) {
                  timeRemaining = nbtPlayer.getInteger("WITCManifestDuration");
                  timeRemaining = Math.max(0, timeRemaining - 5);
                  if((timeRemaining >= 60 && timeRemaining <= 64 || timeRemaining >= 30 && timeRemaining <= 34 || timeRemaining >= 15 && timeRemaining <= 19) && !skipNext) {
                     ChatUtil.sendTranslated(EnumChatFormatting.LIGHT_PURPLE, player, "witchery.rite.manifestation.countdown", new Object[]{Integer.valueOf(timeRemaining).toString()});
                  }
               }

               if(timeRemaining == 0) {
                  if(nbtPlayer.hasKey("WITCManifestDuration")) {
                     nbtPlayer.removeTag("WITCManifestDuration");
                  }

                  returnGhostPlayerToSpiritWorld(player);
               } else if(!skipNext) {
                  nbtPlayer.setInteger("WITCManifestDuration", timeRemaining);
               } else {
                  setPlayerSkipNextManifestationReduction(nbtPlayer, false);
               }
            }
         }
      }

   }

   public static void setPlayerSkipNextManifestationReduction(EntityPlayer player) {
      NBTTagCompound nbtPlayer = Infusion.getNBT(player);
      setPlayerSkipNextManifestationReduction(nbtPlayer, true);
   }

   public static void setPlayerSkipNextManifestationReduction(NBTTagCompound nbtPlayer, boolean skip) {
      nbtPlayer.setBoolean("WITCManifestSkipTick", skip);
   }

   public static boolean getPlayerSkipNextManifestTick(NBTTagCompound nbtPlayer) {
      return nbtPlayer.getBoolean("WITCManifestSkipTick");
   }

   private static boolean containsDemons(List entities, int max) {
      int count = 0;
      Iterator i$ = entities.iterator();

      while(i$.hasNext()) {
         Object obj = i$.next();
         if(obj instanceof EntityDemon) {
            ++count;
            if(count >= max) {
               return true;
            }
         }
      }

      return false;
   }

   public static void setPlayerIsGhost(NBTTagCompound nbtPlayer, boolean ghost) {
      nbtPlayer.setBoolean("WITCManifested", ghost);
   }

   public static boolean getPlayerIsGhost(EntityPlayer player) {
      NBTTagCompound nbtPlayer = Infusion.getNBT(player);
      return getPlayerIsGhost(nbtPlayer);
   }

   public static boolean getPlayerIsGhost(NBTTagCompound nbtPlayer) {
      return nbtPlayer.getBoolean("WITCManifested");
   }

   public static void setPlayerMustAwaken(EntityPlayer player, boolean awaken) {
      NBTTagCompound nbtPlayer = Infusion.getNBT(player);
      setPlayerMustAwaken(nbtPlayer, awaken);
   }

   public static void setPlayerMustAwaken(NBTTagCompound nbtPlayer, boolean ghost) {
      nbtPlayer.setBoolean("WITCForceAwaken", ghost);
   }

   public static boolean getPlayerMustAwaken(EntityPlayer player) {
      NBTTagCompound nbtPlayer = Infusion.getNBT(player);
      return getPlayerMustAwaken(nbtPlayer);
   }

   public static boolean getPlayerMustAwaken(NBTTagCompound nbtPlayer) {
      return nbtPlayer.getBoolean("WITCForceAwaken");
   }

   public static boolean canPlayerManifest(EntityPlayer player) {
      NBTTagCompound nbtPlayer = Infusion.getNBT(player);
      int timeRemaining = 0;
      if(nbtPlayer != null && nbtPlayer.hasKey("WITCManifestDuration")) {
         timeRemaining = nbtPlayer.getInteger("WITCManifestDuration");
      }

      return timeRemaining >= 5;
   }
}
