package com.emoniph.witchery.item;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.entity.EntityCovenWitch;
import com.emoniph.witchery.entity.EntityDemon;
import com.emoniph.witchery.entity.EntityFamiliar;
import com.emoniph.witchery.entity.EntityImp;
import com.emoniph.witchery.infusion.infusions.InfusionOtherwhere;
import com.emoniph.witchery.item.ItemBase;
import com.emoniph.witchery.util.Dye;
import com.emoniph.witchery.util.SoundEffect;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;

public class ItemPolynesiaCharm extends ItemBase {

   private final boolean charmDemons;


   public ItemPolynesiaCharm(boolean charmDemons) {
      this.charmDemons = charmDemons;
      this.setMaxDamage(50);
      this.setMaxStackSize(1);
   }

   public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player) {
      if(!world.isRemote) {
         boolean success = false;
         double MAX_TARGET_RANGE = 5.0D;
         MovingObjectPosition mop = InfusionOtherwhere.doCustomRayTrace(world, player, true, 5.0D);
         if(mop != null && mop.typeOfHit == MovingObjectType.ENTITY && mop.entityHit instanceof EntityLiving) {
            EntityLiving living = (EntityLiving)mop.entityHit;
            if((living instanceof EntityAnimal || living instanceof EntityAmbientCreature || living instanceof EntitySpider || living instanceof EntityWaterMob || living instanceof EntityCreeper && Witchery.Items.WITCH_ROBES.isRobeWorn(player) || living.isEntityUndead() && Witchery.Items.NECROMANCERS_ROBES.isRobeWorn(player)) && !(living instanceof EntityFamiliar) && !(living instanceof EntityCovenWitch) && !(living instanceof EntityImp) && living.isEntityAlive() && !living.isChild() && living.getAttackTarget() == null && (!(living instanceof EntityBat) || this.canBatDrop(living))) {
               ItemPolynesiaCharm.AnimalMerchant merchant = new ItemPolynesiaCharm.AnimalMerchant(living);
               merchant.playIntro(player);
               merchant.setCustomer(player);
               String animalName = living.getCommandSenderName();
               player.displayGUIMerchant(merchant, animalName.substring(0, Math.min(30, animalName.length())));
               success = true;
            }
         }

         if(success && (mop == null || !(mop.entityHit instanceof EntityDemon))) {
            itemstack.damageItem(1, player);
            if(itemstack.stackSize <= 0) {
               player.destroyCurrentEquippedItem();
            }
         } else {
            SoundEffect.NOTE_SNARE.playAtPlayer(world, player);
         }
      }

      return super.onItemRightClick(itemstack, world, player);
   }

   private boolean canBatDrop(EntityLiving living) {
      NBTTagCompound nbtBat = living.getEntityData();
      return nbtBat == null || !nbtBat.hasKey("WITCNoDrops") || !nbtBat.getBoolean("WITCNoDrops");
   }

   public boolean canCharmDemons() {
      return this.charmDemons;
   }

   @SideOnly(Side.CLIENT)
   public EnumRarity getRarity(ItemStack itemstack) {
      return EnumRarity.uncommon;
   }

   public void addInformation(ItemStack itemstack, EntityPlayer player, List list, boolean par4) {
      String localText = Witchery.resource(this.getUnlocalizedName() + ".tip");
      if(localText != null) {
         String[] arr$ = localText.split("\n");
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            String s = arr$[i$];
            if(!s.isEmpty()) {
               list.add(s);
            }
         }
      }

   }

   public static boolean hasStockInventory(EntityLiving entity) {
      if(entity == null) {
         return false;
      } else {
         NBTTagCompound nbtTag = entity.getEntityData();
         boolean hasKey = nbtTag != null && nbtTag.hasKey("WitcheryShopStock");
         return hasKey;
      }
   }

   public static void setEmptyStockInventory(World world, EntityLiving entity) {
      if(entity != null && !world.isRemote) {
         NBTTagCompound nbtTag = entity.getEntityData();
         nbtTag.setTag("WitcheryShopStock", new NBTTagCompound());
      }

   }

   private static class AnimalMerchant implements IMerchant {

      private final EntityLiving animal;
      private EntityPlayer customer;
      static final String STOCKS_KEY = "WitcheryShopStock";
      private MerchantRecipeList currentList = null;


      public AnimalMerchant(EntityLiving animal) {
         this.animal = animal;
      }

      public void playIntro(EntityPlayer player) {
         this.playGreeting(this.animal, player);
      }

      public void setCustomer(EntityPlayer player) {
         this.customer = player;
      }

      public EntityPlayer getCustomer() {
         return this.customer;
      }

      public MerchantRecipeList getRecipes(EntityPlayer player) {
         NBTTagCompound nbtTag = this.animal.getEntityData();
         if(this.currentList != null) {
            return this.currentList;
         } else if(nbtTag.hasKey("WitcheryShopStock")) {
            NBTTagCompound nbtTagStocks = nbtTag.getCompoundTag("WitcheryShopStock");
            if(nbtTagStocks.hasNoTags()) {
               this.currentList = new MerchantRecipeList();
            } else {
               this.currentList = new MerchantRecipeList(nbtTagStocks);
            }

            return this.currentList;
         } else {
            this.currentList = new MerchantRecipeList();
            populateList(this.animal, this.currentList);
            nbtTag.setTag("WitcheryShopStock", this.currentList.getRecipiesAsTags());
            return this.currentList;
         }
      }

      public void useRecipe(MerchantRecipe recipe) {
         if(this.animal != null && this.animal.isEntityAlive() && !this.animal.worldObj.isRemote) {
            recipe.incrementToolUses();
            if(this.currentList != null) {
               NBTTagCompound nbtTag = this.animal.getEntityData();
               nbtTag.setTag("WitcheryShopStock", this.currentList.getRecipiesAsTags());
            }
         }

         this.animal.playLivingSound();
      }

      public void func_110297_a_(ItemStack itemstack) {
         this.animal.playLivingSound();
      }

      @SideOnly(Side.CLIENT)
      public void setRecipes(MerchantRecipeList list) {}

      private static void populateList(EntityLiving animal, MerchantRecipeList finalList) {
         Random r = animal.worldObj.rand;
         MerchantRecipeList list = new MerchantRecipeList();
         ItemStack[] stacks = new ItemStack[]{Witchery.Items.GENERIC.itemMandrakeRoot.createStack(3), Witchery.Items.GENERIC.itemBelladonnaFlower.createStack(3), Witchery.Items.GENERIC.itemArtichoke.createStack(3), new ItemStack(Blocks.sapling, 4, 0), new ItemStack(Blocks.sapling, 4, 1), new ItemStack(Blocks.sapling, 4, 2), new ItemStack(Blocks.sapling, 4, 3), new ItemStack(Blocks.reeds, 2), new ItemStack(Blocks.cactus, 2), new ItemStack(Items.gold_nugget, 5), new ItemStack(Items.iron_ingot, 2), new ItemStack(Items.bone, 4), new ItemStack(Items.flint, 5), Witchery.Items.GENERIC.itemDogTongue.createStack(2), new ItemStack(Items.potato, 5), new ItemStack(Items.poisonous_potato, 2), new ItemStack(Items.carrot, 5), new ItemStack(Items.clay_ball, 10)};
         ArrayList currencies = new ArrayList();
         ArrayList items = new ArrayList();
         items.add(stacks[r.nextInt(stacks.length)]);
         if(animal.worldObj.rand.nextDouble() < 0.03D) {
            items.add(Witchery.Items.GENERIC.itemSeedsTreefyd.createStack());
         }

         if(animal instanceof EntityPig) {
            currencies.add(new ItemStack(Items.carrot));
            currencies.add(new ItemStack(Items.apple));
            currencies.add(new ItemStack(Items.potato));
            items.add(new ItemStack(Blocks.red_mushroom, 5));
            items.add(new ItemStack(Blocks.brown_mushroom, 5));
            if(r.nextDouble() < 0.02D) {
               items.add(new ItemStack(Items.emerald, 1));
            }

            if(r.nextDouble() < 0.01D) {
               items.add(new ItemStack(Items.diamond, 1));
            }
         } else if(animal instanceof EntityHorse) {
            currencies.add(new ItemStack(Items.carrot));
            currencies.add(new ItemStack(Items.apple));
            currencies.add(new ItemStack(Items.wheat));
            if(r.nextDouble() < 0.01D) {
               items.add(new ItemStack(Items.saddle, 1));
            }
         } else if(animal instanceof EntityWolf) {
            currencies.add(new ItemStack(Items.beef));
            currencies.add(new ItemStack(Items.porkchop));
            currencies.add(new ItemStack(Items.chicken));
            items.add(new ItemStack(Items.bone, 5));
            if(r.nextDouble() < 0.02D) {
               items.add(new ItemStack(Items.emerald, 1));
            }

            if(r.nextDouble() < 0.01D) {
               items.add(new ItemStack(Items.diamond, 1));
            }
         } else if(animal instanceof EntityOcelot) {
            currencies.add(new ItemStack(Items.milk_bucket));
            currencies.add(new ItemStack(Items.fish));
         } else if(animal instanceof EntityCow) {
            currencies.add(new ItemStack(Items.wheat));
         } else if(animal instanceof EntityChicken) {
            currencies.add(new ItemStack(Items.wheat_seeds));
            items.add(new ItemStack(Items.feather, 10));
            items.add(new ItemStack(Items.egg, 5));
         } else if(animal instanceof EntityMooshroom) {
            currencies.add(new ItemStack(Blocks.red_mushroom));
            currencies.add(new ItemStack(Blocks.brown_mushroom));
         } else if(animal instanceof EntitySheep) {
            currencies.add(new ItemStack(Items.wheat));
         } else if(animal instanceof EntitySquid) {
            currencies.add(new ItemStack(Items.fish));
            items.add(Dye.INK_SAC.createStack(10));
         } else if(animal instanceof EntityBat) {
            currencies.add(new ItemStack(Items.wheat_seeds));
            currencies.add(new ItemStack(Items.wheat));
            currencies.add(new ItemStack(Items.beef));
            currencies.add(new ItemStack(Items.porkchop));
            items.add(Witchery.Items.GENERIC.itemBatWool.createStack(5));
         } else if(animal instanceof EntitySpider) {
            currencies.add(new ItemStack(Items.beef));
            currencies.add(new ItemStack(Items.porkchop));
            currencies.add(new ItemStack(Items.chicken));
            currencies.add(new ItemStack(Items.fish));
            items.add(new ItemStack(Items.string, 8));
            items.add(Witchery.Items.GENERIC.itemWeb.createStack(4));
         } else if(animal instanceof EntityCreeper) {
            currencies.add(new ItemStack(Items.gunpowder));
            currencies.add(new ItemStack(Items.fish));
            if(r.nextDouble() < 0.05D) {
               items.add(Witchery.Items.GENERIC.itemSpectralDust.createStack(2));
            }

            if(animal.worldObj.rand.nextDouble() < 0.1D) {
               items.add(Witchery.Items.GENERIC.itemSeedsTreefyd.createStack());
            }

            if(r.nextDouble() < 0.02D) {
               items.add(Witchery.Items.GENERIC.itemCreeperHeart.createStack(1));
            }
         } else if(animal.isEntityUndead()) {
            currencies.add(new ItemStack(Items.bone));
            items.add(Witchery.Items.GENERIC.itemSpectralDust.createStack(1));
         } else {
            currencies.add(new ItemStack(Items.beef));
            currencies.add(new ItemStack(Items.porkchop));
            currencies.add(new ItemStack(Items.chicken));
            currencies.add(new ItemStack(Items.fish));
            currencies.add(new ItemStack(Items.wheat));
            currencies.add(new ItemStack(Items.wheat_seeds));
            currencies.add(new ItemStack(Items.carrot));
            currencies.add(new ItemStack(Items.apple));
            currencies.add(new ItemStack(Items.potato));
         }

         Iterator MAX_ITEMS = items.iterator();

         while(MAX_ITEMS.hasNext()) {
            ItemStack i = (ItemStack)MAX_ITEMS.next();
            if(i != null && i.getItem() != null) {
               ItemStack goods = i.copy();
               goods.stackSize = Math.min(r.nextInt(i.stackSize) + (i.stackSize > 4?3:1), goods.getMaxStackSize());
               ItemStack currency = (ItemStack)currencies.get(r.nextInt(currencies.size()));
               ItemStack cost = currency.copy();
               byte multiplier = 1;
               if(goods.getItem() == Items.diamond || goods.getItem() == Items.emerald || goods.getItem() == Items.saddle || Witchery.Items.GENERIC.itemSeedsTreefyd.isMatch(goods) || animal.isEntityUndead()) {
                  multiplier = 2;
               }

               int factor = goods.stackSize > 4?1:2;
               cost.stackSize = Math.min(r.nextInt(2) + goods.stackSize * multiplier * (r.nextInt(2) + factor), currency.getMaxStackSize());
               MerchantRecipe recipe = new MerchantRecipe(cost, goods);
               recipe.func_82783_a(0 - (6 - r.nextInt(2)));
               list.add(recipe);
            }
         }

         Collections.shuffle(list);
         int var15 = r.nextInt(2) + 1;

         for(int var16 = 0; var16 < var15 && var16 < list.size(); ++var16) {
            finalList.add(list.get(var16));
         }

      }

      private void playGreeting(EntityLiving animal, EntityPlayer player) {
         animal.playLivingSound();
         animal.playLivingSound();
         animal.playLivingSound();
      }
   }
}
