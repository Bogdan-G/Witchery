package com.emoniph.witchery.entity.ai;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.entity.EntityGoblin;
import com.emoniph.witchery.util.BlockUtil;
import com.mojang.authlib.GameProfile;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.oredict.OreDictionary;

public class EntityAIDigBlocks extends EntityAIBase {

   protected final EntityGoblin entity;
   protected final double range;
   protected final double kobolditeChance;
   public static final GameProfile NORMAL_MINER_PROFILE = new GameProfile(UUID.fromString("AB06ACB0-0CDB-11E4-9191-0800200C9A66"), "[Minecraft]");
   public static final GameProfile KOBOLDITE_MINER_PROFILE = new GameProfile(UUID.fromString("24818AE0-0CDE-11E4-9191-0800200C9A66"), "[Minecraft]");
   MovingObjectPosition mop = null;
   int failedChecks = 0;
   private int waitTimer = 60;


   public EntityAIDigBlocks(EntityGoblin entity, double range, double kobolditeChance) {
      this.entity = entity;
      this.range = range;
      this.kobolditeChance = kobolditeChance;
      this.setMutexBits(7);
   }

   public boolean shouldExecute() {
      if(this.entity != null && !this.entity.isWorshipping() && this.entity.getHeldItem() != null && this.entity.getHeldItem().getItem() instanceof ItemPickaxe && this.entity.getLeashed() && this.entity.worldObj.rand.nextInt(2) == 0) {
         MovingObjectPosition mop = raytraceBlocks(this.entity.worldObj, this.entity, true, this.failedChecks == 15?1.0D:4.0D, this.failedChecks == 15);
         if(mop != null && mop.typeOfHit == MovingObjectType.BLOCK) {
            Block block = BlockUtil.getBlock(this.entity.worldObj, mop);
            if(this.isMineable(block, this.entity.worldObj, mop.blockX, mop.blockY, mop.blockZ)) {
               this.failedChecks = 0;
               this.mop = mop;
               return true;
            } else {
               this.mop = null;
               ++this.failedChecks;
               return false;
            }
         } else {
            ++this.failedChecks;
            mop = null;
            return false;
         }
      } else {
         return false;
      }
   }

   private boolean isMineable(Block block, World world, int x, int y, int z) {
      return block.getMaterial() != Material.rock && block.getMaterial() != Material.sand && block.getMaterial() != Material.grass && block.getMaterial() != Material.snow && block.getMaterial() != Material.ground?false:block.getBlockHardness(world, x, y, z) >= 0.0F;
   }

   private static MovingObjectPosition raytraceBlocks(World world, EntityLiving player, boolean collisionFlag, double reachDistance, boolean down) {
      Vec3 playerPosition = Vec3.createVectorHelper(player.posX, player.posY + (double)player.getEyeHeight(), player.posZ);
      float rotationYaw = (float)world.rand.nextInt(360);
      player.rotationYaw = rotationYaw;
      float rotationPitch = down?90.0F:0.0F;
      float f1 = MathHelper.cos(-rotationYaw * 0.017453292F - 3.1415927F);
      float f2 = MathHelper.sin(-rotationYaw * 0.017453292F - 3.1415927F);
      float f3 = -MathHelper.cos(-rotationPitch * 0.017453292F);
      float f4 = MathHelper.sin(-rotationPitch * 0.017453292F);
      Vec3 playerLook = Vec3.createVectorHelper((double)(f2 * f3), (double)f4, (double)(f1 * f3));
      Vec3 playerViewOffset = Vec3.createVectorHelper(playerPosition.xCoord + playerLook.xCoord * reachDistance, playerPosition.yCoord + playerLook.yCoord * reachDistance, playerPosition.zCoord + playerLook.zCoord * reachDistance);
      return world.func_147447_a(playerPosition, playerViewOffset, collisionFlag, !collisionFlag, false);
   }

   public void startExecuting() {
      double SPEED = 0.6D;
      this.entity.getNavigator().tryMoveToXYZ((double)this.mop.blockX, (double)this.mop.blockY, (double)this.mop.blockZ, 0.6D);
   }

   public boolean continueExecuting() {
      return this.entity != null && !this.entity.isWorshipping() && this.entity.getHeldItem() != null && this.entity.getHeldItem().getItem() instanceof ItemPickaxe && this.entity.getLeashed() && this.mop != null;
   }

   public void resetTask() {
      if(this.entity.isWorking()) {
         this.entity.setWorking(false);
      }

   }

   public void updateTask() {
      double DROP_RANGE = 2.5D;
      double DROP_RANGE_SQ = 6.25D;
      double dist = this.entity.getDistanceSq((double)this.mop.blockX + 0.5D, (double)this.mop.blockY + 0.5D, (double)this.mop.blockZ + 0.5D);
      boolean retry = true;
      if(dist <= 6.25D) {
         if(!this.entity.isWorking()) {
            this.entity.setWorking(true);
         }

         if(--this.waitTimer == 0) {
            if(!tryHarvestBlock(this.entity.worldObj, this.mop.blockX, this.mop.blockY, this.mop.blockZ, this.entity)) {
               retry = false;
            }

            this.mop = null;
            this.waitTimer = this.getNextHarvestDelay();
         }
      } else if(this.entity.getNavigator().noPath()) {
         this.mop = null;
         this.waitTimer = this.getNextHarvestDelay();
         if(this.entity.isWorking()) {
            this.entity.setWorking(false);
         }
      } else if(!this.entity.isWorking()) {
         this.entity.setWorking(true);
      }

      if(this.mop == null && retry && this.entity.worldObj.rand.nextInt(20) != 0) {
         MovingObjectPosition mop = raytraceBlocks(this.entity.worldObj, this.entity, true, 4.0D, false);
         if(mop != null && mop.typeOfHit == MovingObjectType.BLOCK) {
            Block block = BlockUtil.getBlock(this.entity.worldObj, mop);
            if(this.isMineable(block, this.entity.worldObj, mop.blockX, mop.blockY, mop.blockZ)) {
               this.mop = mop;
               this.waitTimer = this.getNextHarvestDelay();
            }
         }
      }

   }

   private int getNextHarvestDelay() {
      return isHoldingKobolditePick(this.entity)?4:60;
   }

   private static boolean isHoldingKobolditePick(EntityLivingBase entity) {
      return entity.getHeldItem() != null && entity.getHeldItem().getItem() == Witchery.Items.KOBOLDITE_PICKAXE;
   }

   public static boolean tryHarvestBlock(World world, int par1, int par2, int par3, EntityLivingBase harvester) {
      boolean kobolditePick = isHoldingKobolditePick(harvester);
      FakePlayer minerPlayer = FakePlayerFactory.get((WorldServer)world, kobolditePick?KOBOLDITE_MINER_PROFILE:NORMAL_MINER_PROFILE);
      return tryHarvestBlock(world, par1, par2, par3, harvester, minerPlayer);
   }

   public static boolean tryHarvestBlock(World world, int par1, int par2, int par3, EntityLivingBase harvester, EntityPlayer minerPlayer) {
      Block block = world.getBlock(par1, par2, par3);
      int blockMeta = world.getBlockMetadata(par1, par2, par3);
      BreakEvent event = new BreakEvent(par1, par2, par3, world, block, blockMeta, minerPlayer);
      event.setCanceled(false);
      MinecraftForge.EVENT_BUS.post(event);
      if(event.isCanceled()) {
         return false;
      } else {
         ItemStack stack = harvester.getHeldItem();
         if(stack != null && stack.getItem().onBlockStartBreak(stack, par1, par2, par3, minerPlayer)) {
            return false;
         } else {
            world.playAuxSFX(2001, par1, par2, par3, Block.getIdFromBlock(block) + (blockMeta << 12));
            boolean canHarvest = false;
            if(block.getBlockHardness(world, par1, par2, par3) >= 0.0F) {
               if(block.getMaterial().isToolNotRequired()) {
                  canHarvest = true;
               }

               String tool = block.getHarvestTool(blockMeta);
               int toolLevel = stack != null?stack.getItem().getHarvestLevel(stack, tool):0;
               if(toolLevel < 0) {
                  canHarvest = true;
               }

               if(toolLevel >= block.getHarvestLevel(blockMeta)) {
                  canHarvest = true;
               }
            }

            if(canHarvest) {
               canHarvest = removeBlock(world, par1, par2, par3, minerPlayer);
               if(canHarvest) {
                  block.harvestBlock(world, minerPlayer, par1, par2, par3, blockMeta);
               }
            }

            return canHarvest;
         }
      }
   }

   private static boolean removeBlock(World world, int x, int y, int z, EntityPlayer player) {
      Block block = world.getBlock(x, y, z);
      int metadata = world.getBlockMetadata(x, y, z);
      block.onBlockHarvested(world, x, y, z, metadata, player);
      boolean flag = block.removedByPlayer(world, player, x, y, z, true);
      if(flag) {
         block.onBlockDestroyedByPlayer(world, x, y, z, metadata);
      }

      return flag;
   }

   public static void onHarvestDrops(EntityPlayer harvester, HarvestDropsEvent event) {
      if(harvester != null && !harvester.worldObj.isRemote && !event.isCanceled() && (isEqual(harvester.getGameProfile(), KOBOLDITE_MINER_PROFILE) || isEqual(harvester.getGameProfile(), NORMAL_MINER_PROFILE))) {
         boolean hasKobolditePick = isEqual(harvester.getGameProfile(), KOBOLDITE_MINER_PROFILE);
         ArrayList newDrops = new ArrayList();
         double kobolditeChance = hasKobolditePick?0.02D:0.01D;
         Iterator i$ = event.drops.iterator();

         ItemStack newDrop;
         while(i$.hasNext()) {
            newDrop = (ItemStack)i$.next();
            int[] oreIDs = OreDictionary.getOreIDs(newDrop);
            boolean addOriginal = true;
            if(oreIDs.length > 0) {
               String oreName = OreDictionary.getOreName(oreIDs[0]);
               if(oreName != null && oreName.startsWith("ore")) {
                  ItemStack smeltedDrop = FurnaceRecipes.smelting().getSmeltingResult(newDrop);
                  if(smeltedDrop != null && hasKobolditePick && harvester.worldObj.rand.nextDouble() < 0.5D) {
                     addOriginal = false;
                     newDrops.add(smeltedDrop.copy());
                     newDrops.add(smeltedDrop.copy());
                     if(harvester.worldObj.rand.nextDouble() < 0.25D) {
                        newDrops.add(smeltedDrop.copy());
                     }
                  }

                  kobolditeChance = hasKobolditePick?0.08D:0.05D;
               }
            }

            if(addOriginal) {
               newDrops.add(newDrop);
            }
         }

         event.drops.clear();
         i$ = newDrops.iterator();

         while(i$.hasNext()) {
            newDrop = (ItemStack)i$.next();
            event.drops.add(newDrop);
         }

         if(kobolditeChance > 0.0D && harvester.worldObj.rand.nextDouble() < kobolditeChance) {
            event.drops.add(Witchery.Items.GENERIC.itemKobolditeDust.createStack());
         }
      }

   }

   private static boolean isEqual(GameProfile a, GameProfile b) {
      return a != null && b != null && a.getId() != null && b.getId() != null?a.getId().equals(b.getId()):false;
   }

}
