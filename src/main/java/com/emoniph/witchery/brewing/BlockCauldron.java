package com.emoniph.witchery.brewing;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockBaseContainer;
import com.emoniph.witchery.brewing.EffectLevelCounter;
import com.emoniph.witchery.brewing.ModifierYield;
import com.emoniph.witchery.brewing.TileEntityCauldron;
import com.emoniph.witchery.brewing.WitcheryBrewRegistry;
import com.emoniph.witchery.brewing.action.BrewAction;
import com.emoniph.witchery.client.particle.BubblesFX;
import com.emoniph.witchery.client.particle.NaturePowerFX;
import com.emoniph.witchery.common.ExtendedPlayer;
import com.emoniph.witchery.common.IPowerSource;
import com.emoniph.witchery.common.PowerSources;
import com.emoniph.witchery.familiar.Familiar;
import com.emoniph.witchery.util.BlockUtil;
import com.emoniph.witchery.util.EntityUtil;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class BlockCauldron extends BlockBaseContainer {

   public BlockCauldron() {
      super(Material.iron, TileEntityCauldron.class);
      this.setHardness(2.0F);
      this.setStepSound(Block.soundTypeMetal);
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.85F, 1.0F);
   }

   public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
      float f = 0.0625F;
      return AxisAlignedBB.getBoundingBox((double)((float)x + 0.0625F), (double)y, (double)((float)z + 0.0625F), (double)((float)(x + 1) - 0.0625F), (double)((float)(y + 1) - 0.0625F), (double)((float)(z + 1) - 0.0625F));
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public boolean renderAsNormalBlock() {
      return false;
   }

   @SideOnly(Side.CLIENT)
   public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
      return false;
   }

   @SideOnly(Side.CLIENT)
   public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
      TileEntityCauldron cauldron = (TileEntityCauldron)BlockUtil.getTileEntity(world, x, y, z, TileEntityCauldron.class);
      if(cauldron != null && cauldron.isBoiling()) {
         double yPos = 0.2D + cauldron.getPercentFilled() * 0.5D;
         int color = cauldron.getColor();
         if(color == -1) {
            color = 3432410;
         } else if(rand.nextInt(5) == 0) {
            world.playSound((double)x, (double)y, (double)z, "witchery:random.blop", 0.8F + rand.nextFloat() * 0.2F, 0.8F + rand.nextFloat() * 0.2F, false);
         }

         float red = (float)(color >>> 16 & 255) / 256.0F;
         float green = (float)(color >>> 8 & 255) / 256.0F;
         float blue = (float)(color & 255) / 256.0F;

         int i;
         double width;
         double xPos;
         double zPos;
         for(i = 0; i < 2; ++i) {
            width = 0.6D;
            xPos = 0.2D + rand.nextDouble() * 0.6D;
            zPos = 0.2D + rand.nextDouble() * 0.6D;
            BubblesFX d0 = new BubblesFX(world, (double)x + xPos, (double)y + yPos, (double)z + zPos);
            d0.setScale(0.4F);
            if(rand.nextInt(4) == 0) {
               d0.setGravity(-0.02F);
               d0.setCanMove(true);
               d0.setMaxAge(15 + rand.nextInt(10));
            } else {
               d0.setGravity(0.0F);
               d0.setCanMove(false);
               d0.setMaxAge(10 + rand.nextInt(10));
            }

            d0.setRBGColorF(red, green, blue);
            d0.setAlphaF(0.2F);
            Minecraft.getMinecraft().effectRenderer.addEffect(d0);
         }

         if(cauldron.isPowered()) {
            for(i = 0; i < 1 + Math.min(cauldron.getRitualSeconds(), 5); ++i) {
               width = 0.4D;
               xPos = 0.3D + rand.nextDouble() * 0.4D;
               zPos = 0.3D + rand.nextDouble() * 0.4D;
               double var36 = (double)x + xPos;
               double d1 = (double)y + yPos;
               double d2 = (double)z + zPos;
               NaturePowerFX sparkle = new NaturePowerFX(world, var36, d1, d2);
               sparkle.setCircling(cauldron.isRitualInProgress());
               sparkle.setScale(0.6F);
               sparkle.setGravity(0.25F);
               sparkle.setCanMove(true);
               double maxSpeed = 0.04D;
               double doubleSpeed = 0.08D;
               sparkle.setVelocity(rand.nextDouble() * 0.08D - 0.04D, rand.nextDouble() * 0.05D + 0.08D, rand.nextDouble() * 0.08D - 0.04D);
               sparkle.setMaxAge(25 + rand.nextInt(cauldron.isRitualInProgress()?10:10));
               float maxColorShift = 0.2F;
               float doubleColorShift = maxColorShift * 2.0F;
               float colorshiftR = rand.nextFloat() * doubleColorShift - maxColorShift;
               float colorshiftG = rand.nextFloat() * doubleColorShift - maxColorShift;
               float colorshiftB = rand.nextFloat() * doubleColorShift - maxColorShift;
               sparkle.setRBGColorF(red + colorshiftR, green + colorshiftG, blue + colorshiftB);
               sparkle.setAlphaF(0.1F);
               Minecraft.getMinecraft().effectRenderer.addEffect(sparkle);
            }
         }
      }

   }

   public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
      if(!world.isRemote && entity instanceof EntityItem) {
         TileEntityCauldron cauldron = (TileEntityCauldron)BlockUtil.getTileEntity(world, x, y, z, TileEntityCauldron.class);
         if(cauldron != null) {
            EntityItem itemEntity = (EntityItem)entity;
            if(cauldron.isFilled()) {
               if(Witchery.Items.GENERIC.itemGypsum.isMatch(itemEntity.getEntityItem())) {
                  entity.setDead();
                  SoundEffect.RANDOM_FIZZ.playAt(world, (double)x + 0.5D, (double)y + 0.6D, (double)z + 0.5D, 1.0F, 2.0F);
                  ParticleEffect.SMOKE.send(SoundEffect.NONE, world, (double)x + 0.5D, (double)y + 0.6D, (double)z + 0.5D, 0.5D, 1.0D, 8);
                  cauldron.drain(ForgeDirection.UNKNOWN, cauldron.getLiquidQuantity(), true);
                  cauldron.markBlockForUpdate(true);
               } else if(Witchery.Items.GENERIC.itemQuicklime.isMatch(itemEntity.getEntityItem())) {
                  EntityPlayer brewAction = (EntityPlayer)EntityUtil.findNearestEntityWithinAABB(world, EntityPlayer.class, entity.boundingBox.expand(5.0D, 5.0D, 5.0D), entity);
                  if(brewAction != null && cauldron.explodeBrew(brewAction)) {
                     ParticleEffect.SMOKE.send(SoundEffect.NONE, world, (double)x + 0.5D, (double)y + 0.6D, (double)z + 0.5D, 0.5D, 1.0D, 8);
                     cauldron.drain(ForgeDirection.UNKNOWN, cauldron.getLiquidQuantity(), true);
                     entity.setDead();
                     cauldron.markBlockForUpdate(true);
                  }
               } else if(cauldron.isBoiling()) {
                  BrewAction brewAction1 = WitcheryBrewRegistry.INSTANCE.getActionForItemStack(itemEntity.getEntityItem());
                  if(brewAction1 != null && cauldron.addItem(brewAction1, itemEntity.getEntityItem())) {
                     Item containerItem = itemEntity.getEntityItem().getItem().getContainerItem();
                     if(containerItem != null) {
                        EntityUtil.spawnEntityInWorld(world, new EntityItem(world, 0.5D + (double)x, 1.0D + (double)y, 0.5D + (double)z, new ItemStack(containerItem)));
                     }

                     entity.setDead();
                     ParticleEffect.SPLASH.send(SoundEffect.WATER_SPLASH, world, (double)x + 0.5D, (double)y + 0.6D, (double)z + 0.5D, 0.5D, 0.5D, 8);
                  }
               }
            }
         }
      }

   }

   public boolean hasComparatorInputOverride() {
      return true;
   }

   public int getComparatorInputOverride(World world, int x, int y, int z, int side) {
      TileEntityCauldron cauldron = (TileEntityCauldron)BlockUtil.getTileEntity(world, x, y, z, TileEntityCauldron.class);
      byte signal = 0;
      return cauldron != null?cauldron.getRedstoneSignalStrength():signal;
   }

   public boolean tryFillWith(World world, int x, int y, int z, FluidStack fluidStack) {
      if(world.isRemote) {
         return true;
      } else {
         TileEntityCauldron cauldron = (TileEntityCauldron)BlockUtil.getTileEntity(world, x, y, z, TileEntityCauldron.class);
         if(cauldron != null) {
            new FluidStack(FluidRegistry.WATER.getID(), 1000);
            if(cauldron.canFill(ForgeDirection.UNKNOWN, fluidStack.getFluid())) {
               int quantity = cauldron.fill(ForgeDirection.UNKNOWN, fluidStack, true);
               fluidStack.amount -= quantity;
               if(fluidStack.amount < 0) {
                  fluidStack.amount = 0;
               }

               if(quantity > 0) {
                  SoundEffect.WATER_SWIM.playAt(world, (double)x, (double)y, (double)z);
                  cauldron.markBlockForUpdate(true);
               }

               return quantity > 0;
            }
         }

         return false;
      }
   }

   public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
      if(world.isRemote) {
         return true;
      } else {
         TileEntityCauldron cauldron = (TileEntityCauldron)BlockUtil.getTileEntity(world, x, y, z, TileEntityCauldron.class);
         ItemStack heldStack = player.getHeldItem();
         if(cauldron != null && heldStack != null) {
            FluidStack fluidStackToFill = FluidContainerRegistry.getFluidForFilledItem(heldStack);
            if(fluidStackToFill != null) {
               fluidStackToFill.tag = heldStack.hasTagCompound()?(NBTTagCompound)heldStack.getTagCompound().copy():null;
               if(cauldron.canFill(ForgeDirection.UNKNOWN, fluidStackToFill.getFluid())) {
                  int fluidStackInCauldron1 = cauldron.fill(ForgeDirection.UNKNOWN, fluidStackToFill, true);
                  if(fluidStackInCauldron1 != 0) {
                     if(!player.capabilities.isCreativeMode) {
                        player.inventory.setInventorySlotContents(player.inventory.currentItem, consumeItem(heldStack));
                     }

                     SoundEffect.WATER_SWIM.playAtPlayer(world, player);
                     cauldron.markBlockForUpdate(true);
                  }
               }

               return true;
            }

            if(heldStack.getItem() == Witchery.Items.BREW_ENDLESS_WATER) {
               if(this.tryFillWith(world, x, y, z, new FluidStack(FluidRegistry.WATER, 3000))) {
                  heldStack.damageItem(1, player);
               }

               return true;
            }

            FluidStack fluidStackInCauldron = cauldron.getTankInfo(ForgeDirection.UNKNOWN)[0].fluid;
            if(fluidStackInCauldron != null) {
               ItemStack filledBucketStack = FluidContainerRegistry.fillFluidContainer(fluidStackInCauldron, heldStack);
               FluidStack fluidStackToEmpty = FluidContainerRegistry.getFluidForFilledItem(filledBucketStack);
               if(fluidStackToEmpty != null) {
                  if(fluidStackInCauldron.tag != null) {
                     filledBucketStack.setTagCompound((NBTTagCompound)fluidStackInCauldron.tag.copy());
                  }

                  if(!player.capabilities.isCreativeMode) {
                     if(heldStack.stackSize > 1) {
                        if(!player.inventory.addItemStackToInventory(filledBucketStack)) {
                           return false;
                        }

                        player.inventory.setInventorySlotContents(player.inventory.currentItem, consumeItem(heldStack));
                     } else {
                        player.inventory.setInventorySlotContents(player.inventory.currentItem, consumeItem(heldStack));
                        player.inventory.setInventorySlotContents(player.inventory.currentItem, filledBucketStack);
                     }
                  }

                  cauldron.drain(ForgeDirection.UNKNOWN, fluidStackToEmpty.amount, true);
                  SoundEffect.WATER_SWIM.playAtPlayer(world, player);
                  world.markBlockForUpdate(x, y, z);
                  cauldron.markBlockForUpdate(true);
               } else if(heldStack.getItem() == Items.glass_bottle) {
                  int drainAmount = this.getDrainAmount(player, fluidStackInCauldron.tag);
                  if(drainAmount > 0 && cauldron.isPowered() && cauldron.isBoiling()) {
                     NBTTagCompound nbtFluid = (NBTTagCompound)fluidStackInCauldron.tag.copy();
                     boolean enoughLiquid = drainAmount <= cauldron.getLiquidQuantity();
                     if(enoughLiquid) {
                        IPowerSource source = PowerSources.findClosestPowerSource(cauldron);
                        int power = cauldron.getPower();
                        if(power == 0 || source != null && source.consumePower((float)cauldron.getPower())) {
                           cauldron.drain(ForgeDirection.UNKNOWN, Math.min(drainAmount, cauldron.getLiquidQuantity()), true);
                           SoundEffect.WATER_SWIM.playAtPlayer(world, player);
                           cauldron.markBlockForUpdate(true);
                           this.processSkillChanges(player, fluidStackInCauldron.tag);
                           ItemStack brewStack = new ItemStack(Witchery.Items.BREW);
                           brewStack.setTagCompound(nbtFluid);
                           if(heldStack.stackSize > 1) {
                              if(player.inventory.addItemStackToInventory(brewStack)) {
                                 player.inventory.setInventorySlotContents(player.inventory.currentItem, consumeItem(heldStack));
                                 EntityUtil.syncInventory(player);
                                 return false;
                              }

                              return true;
                           }

                           player.inventory.setInventorySlotContents(player.inventory.currentItem, consumeItem(heldStack));
                           player.inventory.setInventorySlotContents(player.inventory.currentItem, brewStack);
                           EntityUtil.syncInventory(player);
                           return true;
                        }
                     }
                  }
               }
            }
         }

         return super.onBlockActivated(world, x, y, z, player, side, hitX, hitY, hitZ);
      }
   }

   public ItemStack fillBottleFromCauldron(World world, int x, int y, int z, int drainAmount) {
      TileEntityCauldron cauldron = (TileEntityCauldron)BlockUtil.getTileEntity(world, x, y, z, TileEntityCauldron.class);
      if(cauldron != null) {
         FluidStack fluidStackInCauldron = cauldron.getTankInfo(ForgeDirection.UNKNOWN)[0].fluid;
         if(fluidStackInCauldron != null && drainAmount > 0 && cauldron.isPowered() && cauldron.isBoiling()) {
            NBTTagCompound nbtFluid = (NBTTagCompound)fluidStackInCauldron.tag.copy();
            boolean enoughLiquid = drainAmount <= cauldron.getLiquidQuantity();
            cauldron.drain(ForgeDirection.UNKNOWN, Math.min(drainAmount, cauldron.getLiquidQuantity()), true);
            SoundEffect.WATER_SWIM.playAt(world, (double)x, (double)y, (double)z);
            cauldron.markBlockForUpdate(true);
            if(enoughLiquid) {
               ItemStack brewStack = new ItemStack(Witchery.Items.BREW);
               brewStack.setTagCompound(nbtFluid);
               return brewStack;
            }
         }
      }

      return null;
   }

   private void processSkillChanges(EntityPlayer player, NBTTagCompound nbtBrew) {
      ExtendedPlayer props = ExtendedPlayer.get(player);
      if(props != null) {
         EffectLevelCounter levels = WitcheryBrewRegistry.INSTANCE.getBrewLevel(nbtBrew);
         int currentLevel = props.getSkillPotionBottling();
         if(levels.canIncreasePlayerSkill(currentLevel)) {
            props.increaseSkillPotionBottling();
         }
      }

   }

   private int getDrainAmount(EntityPlayer player, NBTTagCompound nbtFluid) {
      ModifierYield yieldModifier = WitcheryBrewRegistry.INSTANCE.getYieldModifier(nbtFluid);
      int[][] yieldLevels = new int[][]{{1, 3000}, {2, 1500}, {3, 1000}, {4, 750}, {5, 600}, {6, 500}, {8, 375}, {10, 300}, {15, 200}, {30, 100}};
      byte yield = 0;
      ExtendedPlayer props = ExtendedPlayer.get(player);
      if(props != null) {
         int levelSkill = props.getSkillPotionBottling() / 30;
         PotionEffect potionEffect = player.getActivePotionEffect(Witchery.Potions.BREWING_EXPERT);
         int levelPotion = potionEffect != null?potionEffect.getAmplifier() + 1:0;
         byte gearLevel = 0;
         ItemStack headItem = player.inventory.armorItemInSlot(3);
         int gearLevel1 = gearLevel + (Witchery.Items.WITCH_HAT.isHatWorn(player)?1:0);
         gearLevel1 += Witchery.Items.BABAS_HAT.isHatWorn(player)?2:0;
         gearLevel1 += Witchery.Items.WITCH_ROBES.isRobeWorn(player)?1:0;
         gearLevel1 += Witchery.Items.NECROMANCERS_ROBES.isRobeWorn(player)?1:0;
         int familiarLevel = Familiar.hasActiveBrewMasteryFamiliar(player)?1:0;
         if(levelSkill == 0) {
            yield = 0;
         } else if(levelSkill == 1) {
            if(gearLevel1 < 1 && levelPotion < 1) {
               yield = 1;
            } else {
               yield = 2;
            }
         } else if(levelSkill >= 2) {
            if(gearLevel1 >= 3 && levelPotion >= 3 && familiarLevel >= 1) {
               yield = 9;
            } else if(gearLevel1 >= 2 && levelPotion >= 3 && familiarLevel >= 1) {
               yield = 8;
            } else if(gearLevel1 >= 2 && levelPotion >= 3) {
               yield = 7;
            } else if(gearLevel1 >= 2 && levelPotion >= 2) {
               yield = 6;
            } else if(gearLevel1 >= 2 && levelPotion >= 1) {
               yield = 5;
            } else if(gearLevel1 < 2 && levelPotion < 1) {
               if(gearLevel1 < 1 && levelPotion < 1) {
                  yield = 2;
               } else {
                  yield = 3;
               }
            } else {
               yield = 4;
            }
         }
      }

      return yieldLevels[Math.max(yield - yieldModifier.getYieldModification(), 0)][1];
   }

   private static ItemStack consumeItem(ItemStack stack) {
      if(stack.stackSize == 1) {
         return stack.getItem().hasContainerItem(stack)?stack.getItem().getContainerItem(stack):null;
      } else {
         stack.splitStack(1);
         return stack;
      }
   }
}
