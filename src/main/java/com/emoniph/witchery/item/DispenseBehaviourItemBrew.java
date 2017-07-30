package com.emoniph.witchery.item;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.brewing.EntityBrew;
import com.emoniph.witchery.brewing.WitcheryBrewRegistry;
import com.emoniph.witchery.entity.EntityGrenade;
import com.emoniph.witchery.util.BlockUtil;
import com.emoniph.witchery.util.SoundEffect;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.material.Material;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class DispenseBehaviourItemBrew implements IBehaviorDispenseItem {

   private final BehaviorDefaultDispenseItem defaultDispenserItemBehavior = new BehaviorDefaultDispenseItem();


   public ItemStack dispense(IBlockSource block, ItemStack stack) {
      if(stack.getItem() == Witchery.Items.BREW && WitcheryBrewRegistry.INSTANCE.isSplash(stack.getTagCompound())) {
         return (new DispenseBehaviourItemBrew.DispenserBehaviorBrew(this, stack)).dispense(block, stack);
      } else {
         EnumFacing facing;
         int z;
         if(stack.getItem() == Items.glass_bottle) {
            facing = BlockDispenser.func_149937_b(block.getBlockMetadata());
            EnumFacing[] var15 = new EnumFacing[]{EnumFacing.DOWN, EnumFacing.UP, EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.EAST, EnumFacing.WEST};
            EnumFacing[] var16 = var15;
            z = var15.length;

            for(int var17 = 0; var17 < z; ++var17) {
               EnumFacing var18 = var16[var17];
               if(var18 != facing) {
                  int x1 = block.getXInt() + var18.getFrontOffsetX();
                  int y1 = block.getYInt() + var18.getFrontOffsetY();
                  int z1 = block.getZInt() + var18.getFrontOffsetZ();
                  Block replaceBlock1 = block.getWorld().getBlock(x1, y1, z1);
                  if(replaceBlock1 == Witchery.Blocks.CAULDRON) {
                     ItemStack brew = Witchery.Blocks.CAULDRON.fillBottleFromCauldron(block.getWorld(), x1, y1, z1, 3000);
                     if(brew != null) {
                        IPosition position = BlockDispenser.func_149939_a(block);
                        BehaviorDefaultDispenseItem.doDispense(block.getWorld(), brew, 6, facing, position);
                        stack.splitStack(1);
                        block.getWorld().playAuxSFX(1000, block.getXInt(), block.getYInt(), block.getZInt(), 0);
                     }

                     return stack;
                  }
               }
            }

            return this.defaultDispenserItemBehavior.dispense(block, stack);
         } else if(stack.getItem() != Witchery.Items.BREW_ENDLESS_WATER) {
            return stack.getItem() == Witchery.Items.SUN_GRENADE?(new DispenseBehaviourItemBrew.DispenserGrenade(this, stack)).dispense(block, stack):(stack.getItem() == Witchery.Items.DUP_GRENADE?(new DispenseBehaviourItemBrew.DispenserGrenade(this, stack)).dispense(block, stack):this.defaultDispenserItemBehavior.dispense(block, stack));
         } else {
            if(!block.getWorld().isRemote) {
               facing = BlockDispenser.func_149937_b(block.getBlockMetadata());
               int x = block.getXInt() + facing.getFrontOffsetX();
               int y = block.getYInt() + facing.getFrontOffsetY();
               z = block.getZInt() + facing.getFrontOffsetZ();
               Block replaceBlock = block.getWorld().getBlock(x, y, z);
               FakePlayer fakePlayer = FakePlayerFactory.getMinecraft((WorldServer)block.getWorld());
               if(stack.getItemDamage() <= stack.getMaxDamage()) {
                  if(BlockUtil.isReplaceableBlock(block.getWorld(), x, y, z, fakePlayer) && replaceBlock.getMaterial() != Material.water) {
                     stack.damageItem(1, fakePlayer);
                     block.getWorld().setBlock(x, y, z, Blocks.flowing_water);
                     block.getWorld().markBlockForUpdate(x, y, z);
                     SoundEffect.WATER_SPLASH.playAt(block.getWorld(), (double)x, (double)y, (double)z);
                  } else if(replaceBlock == Witchery.Blocks.CAULDRON) {
                     if(Witchery.Blocks.CAULDRON.tryFillWith(block.getWorld(), x, y, z, new FluidStack(FluidRegistry.WATER, 3000))) {
                        stack.damageItem(1, fakePlayer);
                     }
                  } else if(replaceBlock == Witchery.Blocks.KETTLE && Witchery.Blocks.KETTLE.tryFillWith(block.getWorld(), x, y, z, new FluidStack(FluidRegistry.WATER, 1000))) {
                     stack.damageItem(1, fakePlayer);
                  }
               }
            }

            return stack;
         }
      }
   }

   public static class DispenserGrenade extends BehaviorDefaultDispenseItem {

      final ItemStack potionItemStack;
      final DispenseBehaviourItemBrew dispenserPotionBehavior;


      DispenserGrenade(DispenseBehaviourItemBrew behavior, ItemStack brewStack) {
         this.dispenserPotionBehavior = behavior;
         this.potionItemStack = brewStack;
      }

      public ItemStack dispenseStack(IBlockSource dispenserBlock, ItemStack stack) {
         World world = dispenserBlock.getWorld();
         IPosition iposition = BlockDispenser.func_149939_a(dispenserBlock);
         EnumFacing enumfacing = BlockDispenser.func_149937_b(dispenserBlock.getBlockMetadata());
         EntityGrenade iprojectile = this.getProjectileEntity(world, iposition);
         iprojectile.setThrowableHeading((double)enumfacing.getFrontOffsetX(), (double)((float)enumfacing.getFrontOffsetY() + 0.1F), (double)enumfacing.getFrontOffsetZ(), this.func_82500_b(), this.func_82498_a());
         world.spawnEntityInWorld(iprojectile);
         stack.splitStack(1);
         return stack;
      }

      protected void playDispenseSound(IBlockSource dispenserBlock) {
         dispenserBlock.getWorld().playAuxSFX(1002, dispenserBlock.getXInt(), dispenserBlock.getYInt(), dispenserBlock.getZInt(), 0);
      }

      protected EntityGrenade getProjectileEntity(World world, IPosition position) {
         return new EntityGrenade(world, position.getX(), position.getY(), position.getZ(), this.potionItemStack);
      }

      protected float func_82498_a() {
         return 3.0F;
      }

      protected float func_82500_b() {
         return 1.375F;
      }
   }

   public static class DispenserBehaviorBrew extends BehaviorDefaultDispenseItem {

      final ItemStack potionItemStack;
      final DispenseBehaviourItemBrew dispenserPotionBehavior;


      DispenserBehaviorBrew(DispenseBehaviourItemBrew behavior, ItemStack brewStack) {
         this.dispenserPotionBehavior = behavior;
         this.potionItemStack = brewStack;
      }

      public ItemStack dispenseStack(IBlockSource dispenserBlock, ItemStack stack) {
         World world = dispenserBlock.getWorld();
         IPosition iposition = BlockDispenser.func_149939_a(dispenserBlock);
         EnumFacing enumfacing = BlockDispenser.func_149937_b(dispenserBlock.getBlockMetadata());
         EntityBrew iprojectile = this.getProjectileEntity(world, iposition);
         iprojectile.setThrowableHeading((double)enumfacing.getFrontOffsetX(), (double)((float)enumfacing.getFrontOffsetY() + 0.1F), (double)enumfacing.getFrontOffsetZ(), this.func_82500_b(), this.func_82498_a());
         world.spawnEntityInWorld(iprojectile);
         stack.splitStack(1);
         return stack;
      }

      protected void playDispenseSound(IBlockSource dispenserBlock) {
         dispenserBlock.getWorld().playAuxSFX(1002, dispenserBlock.getXInt(), dispenserBlock.getYInt(), dispenserBlock.getZInt(), 0);
      }

      protected EntityBrew getProjectileEntity(World world, IPosition position) {
         return new EntityBrew(world, position.getX(), position.getY(), position.getZ(), this.potionItemStack, false);
      }

      protected float func_82498_a() {
         return 3.0F;
      }

      protected float func_82500_b() {
         return 1.375F;
      }
   }
}
