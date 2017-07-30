package com.emoniph.witchery.blocks;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockBaseContainer;
import com.emoniph.witchery.common.ExtendedPlayer;
import com.emoniph.witchery.util.BlockUtil;
import com.emoniph.witchery.util.ChatUtil;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.emoniph.witchery.util.TimeUtil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockStatueWerewolf extends BlockBaseContainer {

   public BlockStatueWerewolf() {
      super(Material.rock, BlockStatueWerewolf.TileEntityStatueWerewolf.class);
      this.setResistance(1000.0F);
      this.setHardness(2.5F);
      this.setStepSound(Block.soundTypeStone);
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F);
   }

   public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
      switch(MathHelper.floor_double((double)(entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3) {
      case 0:
         world.setBlockMetadataWithNotify(x, y, z, 2, 2);
         break;
      case 1:
         world.setBlockMetadataWithNotify(x, y, z, 5, 2);
         break;
      case 2:
         world.setBlockMetadataWithNotify(x, y, z, 3, 2);
         break;
      case 3:
         world.setBlockMetadataWithNotify(x, y, z, 4, 2);
      }

   }

   public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
      if(world.isRemote) {
         return false;
      } else {
         BlockStatueWerewolf.TileEntityStatueWerewolf statue = (BlockStatueWerewolf.TileEntityStatueWerewolf)BlockUtil.getTileEntity(world, x, y, z, BlockStatueWerewolf.TileEntityStatueWerewolf.class);
         if(statue != null) {
            int meta = world.getBlockMetadata(x, y, z);
            ForgeDirection direction = ForgeDirection.getOrientation(meta);
            ExtendedPlayer playerEx = ExtendedPlayer.get(player);
            ItemStack heldStack = player.getHeldItem();
            SoundEffect.WITCHERY_MOB_WOLFMAN_LORD.playOnlyTo(player, 1.0F, 1.0F);
            int level = playerEx.getWerewolfLevel();
            boolean GOLD_REQUIRED = true;
            EntityItem MUTTON_REQUIRED1;
            if(level >= 2 && heldStack != null && heldStack.getItem() == Items.gold_ingot && heldStack.stackSize >= 3) {
               ChatUtil.sendTranslated(EnumChatFormatting.GOLD, player, "witchery.werewolf.mooncharmcrafted", new Object[0]);
               heldStack.splitStack(3);
               MUTTON_REQUIRED1 = new EntityItem(world, (double)direction.offsetX + 0.5D + (double)x, 1.1D + (double)y, 0.5D + (double)z + (double)direction.offsetZ, new ItemStack(Witchery.Items.MOON_CHARM));
               MUTTON_REQUIRED1.motionX = MUTTON_REQUIRED1.motionY = MUTTON_REQUIRED1.motionZ = 0.0D;
               world.spawnEntityInWorld(MUTTON_REQUIRED1);
               ParticleEffect.REDDUST.send(SoundEffect.RANDOM_ORB, MUTTON_REQUIRED1, 0.2D, 0.2D, 16);
            } else {
               switch(level) {
               case 0:
                  ParticleEffect.MOB_SPELL.send(SoundEffect.NONE, player, 1.0D, 1.0D, 16);
                  player.addPotionEffect(new PotionEffect(Potion.digSlowdown.id, TimeUtil.secsToTicks(60), 0));
                  ChatUtil.sendTranslated(EnumChatFormatting.GOLD, player, "witchery.werewolf.notworthy", new Object[0]);
                  break;
               case 1:
                  if(heldStack != null && heldStack.getItem() == Items.gold_ingot) {
                     if(heldStack.stackSize >= 3) {
                        ChatUtil.sendTranslated(EnumChatFormatting.GOLD, player, "witchery.werewolf.level2complete", new Object[0]);
                        heldStack.splitStack(3);
                        MUTTON_REQUIRED1 = new EntityItem(world, (double)direction.offsetX + 0.5D + (double)x, 1.1D + (double)y, 0.5D + (double)z + (double)direction.offsetZ, new ItemStack(Witchery.Items.MOON_CHARM));
                        MUTTON_REQUIRED1.motionX = MUTTON_REQUIRED1.motionY = MUTTON_REQUIRED1.motionZ = 0.0D;
                        world.spawnEntityInWorld(MUTTON_REQUIRED1);
                        ParticleEffect.REDDUST.send(SoundEffect.RANDOM_LEVELUP, MUTTON_REQUIRED1, 0.2D, 0.2D, 16);
                        playerEx.increaseWerewolfLevel();
                     } else {
                        ChatUtil.sendTranslated(EnumChatFormatting.GOLD, player, "witchery.werewolf.level2progress", new Object[]{Integer.valueOf(3).toString(), Integer.valueOf(3 - heldStack.stackSize).toString()});
                     }
                  } else {
                     ChatUtil.sendTranslated(EnumChatFormatting.GOLD, player, "witchery.werewolf.level2begin", new Object[]{Integer.valueOf(3).toString()});
                  }
                  break;
               case 2:
                  boolean MUTTON_REQUIRED = true;
                  if(heldStack != null && Witchery.Items.GENERIC.itemMuttonRaw.isMatch(heldStack)) {
                     if(heldStack.stackSize >= 30) {
                        ChatUtil.sendTranslated(EnumChatFormatting.GOLD, player, "witchery.werewolf.level3complete", new Object[0]);
                        heldStack.splitStack(30);
                        ParticleEffect.REDDUST.send(SoundEffect.RANDOM_LEVELUP, world, (double)direction.offsetX + 0.5D + (double)x, 1.1D + (double)y, 0.5D + (double)z + (double)direction.offsetZ, 0.2D, 0.2D, 16);
                        playerEx.increaseWerewolfLevel();
                     } else {
                        ChatUtil.sendTranslated(EnumChatFormatting.GOLD, player, "witchery.werewolf.level3progress", new Object[]{Integer.valueOf(30).toString(), Integer.valueOf(30 - heldStack.stackSize).toString()});
                     }
                  } else {
                     ChatUtil.sendTranslated(EnumChatFormatting.GOLD, player, "witchery.werewolf.level3begin", new Object[]{Integer.valueOf(30).toString()});
                  }
                  break;
               case 3:
                  boolean TONGUES_REQUIRED = true;
                  if(heldStack != null && Witchery.Items.GENERIC.itemDogTongue.isMatch(heldStack)) {
                     if(heldStack.stackSize >= 10) {
                        ChatUtil.sendTranslated(EnumChatFormatting.GOLD, player, "witchery.werewolf.level4complete", new Object[0]);
                        heldStack.splitStack(10);
                        ParticleEffect.REDDUST.send(SoundEffect.RANDOM_LEVELUP, world, (double)direction.offsetX + 0.5D + (double)x, 1.1D + (double)y, 0.5D + (double)z + (double)direction.offsetZ, 0.2D, 0.2D, 16);
                        playerEx.increaseWerewolfLevel();
                     } else {
                        ChatUtil.sendTranslated(EnumChatFormatting.GOLD, player, "witchery.werewolf.level4progress", new Object[]{Integer.valueOf(10).toString(), Integer.valueOf(10 - heldStack.stackSize).toString()});
                     }
                  } else {
                     ChatUtil.sendTranslated(EnumChatFormatting.GOLD, player, "witchery.werewolf.level4begin", new Object[]{Integer.valueOf(10).toString()});
                  }
                  break;
               case 4:
                  switch(BlockStatueWerewolf.NamelessClass1260014180.$SwitchMap$com$emoniph$witchery$common$ExtendedPlayer$QuestState[playerEx.getWolfmanQuestState().ordinal()]) {
                  case 1:
                     ChatUtil.sendTranslated(EnumChatFormatting.GOLD, player, "witchery.werewolf.level5begin", new Object[0]);
                     EntityItem KILLS_REQUIRED1 = new EntityItem(world, (double)direction.offsetX + 0.5D + (double)x, 1.1D + (double)y, 0.5D + (double)z + (double)direction.offsetZ, new ItemStack(Witchery.Items.HORN_OF_THE_HUNT));
                     KILLS_REQUIRED1.motionX = KILLS_REQUIRED1.motionY = KILLS_REQUIRED1.motionZ = 0.0D;
                     world.spawnEntityInWorld(KILLS_REQUIRED1);
                     ParticleEffect.REDDUST.send(SoundEffect.RANDOM_FIZZ, KILLS_REQUIRED1, 0.2D, 0.2D, 16);
                     playerEx.setWolfmanQuestState(ExtendedPlayer.QuestState.STARTED);
                     return true;
                  case 2:
                     ChatUtil.sendTranslated(EnumChatFormatting.GOLD, player, "witchery.werewolf.level5progress", new Object[0]);
                     return true;
                  case 3:
                     ChatUtil.sendTranslated(EnumChatFormatting.GOLD, player, "witchery.werewolf.level5complete", new Object[0]);
                     ParticleEffect.REDDUST.send(SoundEffect.RANDOM_LEVELUP, world, (double)direction.offsetX + 0.5D + (double)x, 1.1D + (double)y, 0.5D + (double)z + (double)direction.offsetZ, 0.2D, 0.2D, 16);
                     playerEx.increaseWerewolfLevel();
                     return true;
                  default:
                     return true;
                  }
               case 5:
                  boolean KILLS_REQUIRED = true;
                  if(playerEx.getWolfmanQuestCounter() >= 10) {
                     playerEx.setWolfmanQuestState(ExtendedPlayer.QuestState.COMPLETE);
                  }

                  switch(BlockStatueWerewolf.NamelessClass1260014180.$SwitchMap$com$emoniph$witchery$common$ExtendedPlayer$QuestState[playerEx.getWolfmanQuestState().ordinal()]) {
                  case 1:
                     ChatUtil.sendTranslated(EnumChatFormatting.GOLD, player, "witchery.werewolf.level6begin", new Object[]{Integer.valueOf(10).toString()});
                     playerEx.setWolfmanQuestState(ExtendedPlayer.QuestState.STARTED);
                     return true;
                  case 2:
                     ChatUtil.sendTranslated(EnumChatFormatting.GOLD, player, "witchery.werewolf.level6progress", new Object[]{Integer.valueOf(10).toString(), Integer.valueOf(10 - playerEx.getWolfmanQuestCounter()).toString()});
                     return true;
                  case 3:
                     ChatUtil.sendTranslated(EnumChatFormatting.GOLD, player, "witchery.werewolf.level6complete", new Object[0]);
                     ParticleEffect.REDDUST.send(SoundEffect.RANDOM_LEVELUP, world, (double)direction.offsetX + 0.5D + (double)x, 1.1D + (double)y, 0.5D + (double)z + (double)direction.offsetZ, 0.2D, 0.2D, 16);
                     playerEx.increaseWerewolfLevel();
                     return true;
                  default:
                     return true;
                  }
               case 6:
                  boolean PLACES_HOWLED_AT = true;
                  if(playerEx.getWolfmanQuestCounter() >= 16) {
                     playerEx.setWolfmanQuestState(ExtendedPlayer.QuestState.COMPLETE);
                  }

                  switch(BlockStatueWerewolf.NamelessClass1260014180.$SwitchMap$com$emoniph$witchery$common$ExtendedPlayer$QuestState[playerEx.getWolfmanQuestState().ordinal()]) {
                  case 1:
                     ChatUtil.sendTranslated(EnumChatFormatting.GOLD, player, "witchery.werewolf.level7begin", new Object[]{Integer.valueOf(16).toString()});
                     playerEx.setWolfmanQuestState(ExtendedPlayer.QuestState.STARTED);
                     return true;
                  case 2:
                     ChatUtil.sendTranslated(EnumChatFormatting.GOLD, player, "witchery.werewolf.level7progress", new Object[]{Integer.valueOf(16).toString(), Integer.valueOf(16 - playerEx.getWolfmanQuestCounter()).toString()});
                     return true;
                  case 3:
                     ChatUtil.sendTranslated(EnumChatFormatting.GOLD, player, "witchery.werewolf.level7complete", new Object[0]);
                     ParticleEffect.REDDUST.send(SoundEffect.RANDOM_LEVELUP, world, (double)direction.offsetX + 0.5D + (double)x, 1.1D + (double)y, 0.5D + (double)z + (double)direction.offsetZ, 0.2D, 0.2D, 16);
                     playerEx.increaseWerewolfLevel();
                     return true;
                  default:
                     return true;
                  }
               case 7:
                  boolean WOLVES_TAMED = true;
                  if(playerEx.getWolfmanQuestCounter() >= 6) {
                     playerEx.setWolfmanQuestState(ExtendedPlayer.QuestState.COMPLETE);
                  }

                  switch(BlockStatueWerewolf.NamelessClass1260014180.$SwitchMap$com$emoniph$witchery$common$ExtendedPlayer$QuestState[playerEx.getWolfmanQuestState().ordinal()]) {
                  case 1:
                     ChatUtil.sendTranslated(EnumChatFormatting.GOLD, player, "witchery.werewolf.level8begin", new Object[]{Integer.valueOf(6).toString()});
                     playerEx.setWolfmanQuestState(ExtendedPlayer.QuestState.STARTED);
                     return true;
                  case 2:
                     ChatUtil.sendTranslated(EnumChatFormatting.GOLD, player, "witchery.werewolf.level8progress", new Object[]{Integer.valueOf(6).toString(), Integer.valueOf(6 - playerEx.getWolfmanQuestCounter()).toString()});
                     return true;
                  case 3:
                     ChatUtil.sendTranslated(EnumChatFormatting.GOLD, player, "witchery.werewolf.level8complete", new Object[0]);
                     ParticleEffect.REDDUST.send(SoundEffect.RANDOM_LEVELUP, world, (double)direction.offsetX + 0.5D + (double)x, 1.1D + (double)y, 0.5D + (double)z + (double)direction.offsetZ, 0.2D, 0.2D, 16);
                     playerEx.increaseWerewolfLevel();
                     return true;
                  default:
                     return true;
                  }
               case 8:
                  boolean PIGMEN_KILLED = true;
                  if(playerEx.getWolfmanQuestCounter() >= 30) {
                     playerEx.setWolfmanQuestState(ExtendedPlayer.QuestState.COMPLETE);
                  }

                  switch(BlockStatueWerewolf.NamelessClass1260014180.$SwitchMap$com$emoniph$witchery$common$ExtendedPlayer$QuestState[playerEx.getWolfmanQuestState().ordinal()]) {
                  case 1:
                     ChatUtil.sendTranslated(EnumChatFormatting.GOLD, player, "witchery.werewolf.level9begin", new Object[]{Integer.valueOf(30).toString()});
                     playerEx.setWolfmanQuestState(ExtendedPlayer.QuestState.STARTED);
                     return true;
                  case 2:
                     ChatUtil.sendTranslated(EnumChatFormatting.GOLD, player, "witchery.werewolf.level9progress", new Object[]{Integer.valueOf(30).toString(), Integer.valueOf(30 - playerEx.getWolfmanQuestCounter()).toString()});
                     return true;
                  case 3:
                     ChatUtil.sendTranslated(EnumChatFormatting.GOLD, player, "witchery.werewolf.level9complete", new Object[0]);
                     ParticleEffect.REDDUST.send(SoundEffect.RANDOM_LEVELUP, world, (double)direction.offsetX + 0.5D + (double)x, 1.1D + (double)y, 0.5D + (double)z + (double)direction.offsetZ, 0.2D, 0.2D, 16);
                     playerEx.increaseWerewolfLevel();
                     return true;
                  default:
                     return true;
                  }
               case 9:
                  boolean PEOPLE_KILLED = true;
                  if(playerEx.getWolfmanQuestCounter() >= 1) {
                     playerEx.setWolfmanQuestState(ExtendedPlayer.QuestState.COMPLETE);
                  }

                  switch(BlockStatueWerewolf.NamelessClass1260014180.$SwitchMap$com$emoniph$witchery$common$ExtendedPlayer$QuestState[playerEx.getWolfmanQuestState().ordinal()]) {
                  case 1:
                     ChatUtil.sendTranslated(EnumChatFormatting.GOLD, player, "witchery.werewolf.level10begin", new Object[]{Integer.valueOf(1).toString()});
                     playerEx.setWolfmanQuestState(ExtendedPlayer.QuestState.STARTED);
                     return true;
                  case 2:
                     ChatUtil.sendTranslated(EnumChatFormatting.GOLD, player, "witchery.werewolf.level10progress", new Object[]{Integer.valueOf(1).toString(), Integer.valueOf(1 - playerEx.getWolfmanQuestCounter()).toString()});
                     return true;
                  case 3:
                     ChatUtil.sendTranslated(EnumChatFormatting.GOLD, player, "witchery.werewolf.level10complete", new Object[0]);
                     ParticleEffect.REDDUST.send(SoundEffect.RANDOM_LEVELUP, world, (double)direction.offsetX + 0.5D + (double)x, 1.1D + (double)y, 0.5D + (double)z + (double)direction.offsetZ, 0.2D, 0.2D, 16);
                     playerEx.increaseWerewolfLevel();
                     return true;
                  default:
                     return true;
                  }
               case 10:
                  SoundEffect.WITCHERY_MOB_WOLFMAN_LORD.playOnlyTo(player, 1.0F, 1.0F);
                  ChatUtil.sendTranslated(EnumChatFormatting.GOLD, player, "witchery.werewolf.level10complete", new Object[0]);
               }
            }
         }

         return true;
      }
   }

   public int quantityDropped(Random rand) {
      return 1;
   }

   @SideOnly(Side.CLIENT)
   public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
      return false;
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public boolean renderAsNormalBlock() {
      return false;
   }

   @SideOnly(Side.CLIENT)
   public void randomDisplayTick(World world, int x, int y, int z, Random rand) {}

   public static class TileEntityStatueWerewolf extends TileEntity {

      public boolean canUpdate() {
         return false;
      }

      public void writeToNBT(NBTTagCompound nbtRoot) {
         super.writeToNBT(nbtRoot);
      }

      public void readFromNBT(NBTTagCompound nbtRoot) {
         super.readFromNBT(nbtRoot);
      }
   }

   // $FF: synthetic class
   static class NamelessClass1260014180 {

      // $FF: synthetic field
      static final int[] $SwitchMap$com$emoniph$witchery$common$ExtendedPlayer$QuestState = new int[ExtendedPlayer.QuestState.values().length];


      static {
         try {
            $SwitchMap$com$emoniph$witchery$common$ExtendedPlayer$QuestState[ExtendedPlayer.QuestState.NOT_STATED.ordinal()] = 1;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            $SwitchMap$com$emoniph$witchery$common$ExtendedPlayer$QuestState[ExtendedPlayer.QuestState.STARTED.ordinal()] = 2;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            $SwitchMap$com$emoniph$witchery$common$ExtendedPlayer$QuestState[ExtendedPlayer.QuestState.COMPLETE.ordinal()] = 3;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }
}
