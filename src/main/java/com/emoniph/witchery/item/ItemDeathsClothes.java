package com.emoniph.witchery.item;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.WitcheryCreativeTab;
import com.emoniph.witchery.client.model.ModelClothesDeath;
import com.emoniph.witchery.infusion.infusions.InfusionOtherwhere;
import com.emoniph.witchery.util.Config;
import com.emoniph.witchery.util.ItemUtil;
import com.emoniph.witchery.util.TimeUtil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;

public class ItemDeathsClothes extends ItemArmor {

   @SideOnly(Side.CLIENT)
   private ModelClothesDeath modelClothesChest;
   private static final String BIBLIOCRAFT_ARMOR_STAND_ENTITY_NAME = "AbstractSteve";


   public ItemDeathsClothes(int armorSlot) {
      super(ArmorMaterial.IRON, 1, armorSlot);
      this.setMaxDamage(ArmorMaterial.DIAMOND.getDurability(armorSlot));
      this.setCreativeTab(WitcheryCreativeTab.INSTANCE);
   }

   public Item setUnlocalizedName(String itemName) {
      ItemUtil.registerItem(this, itemName);
      return super.setUnlocalizedName(itemName);
   }

   public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
      return stack != null?"witchery:textures/entities/deathsclothes" + (type == null?"":"_overlay") + ".png":null;
   }

   public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {
      if(!world.isRemote && player.getCurrentArmor(3) == stack) {
         int offset = (int)(world.getTotalWorldTime() % 10L);
         if(Config.instance().allowDeathsHoodToFreezeVictims && offset == 1) {
            MovingObjectPosition x1 = InfusionOtherwhere.raytraceEntities(world, player, true, 16.0D);
            if(x1 != null && x1.typeOfHit == MovingObjectType.ENTITY && x1.entityHit instanceof EntityLivingBase) {
               EntityLivingBase y1 = (EntityLivingBase)x1.entityHit;
               if(y1.canEntityBeSeen(player) && !y1.isPotionActive(Potion.moveSlowdown)) {
                  y1.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 60, 2));
               }
            }
         } else if(offset == 2) {
            int x = MathHelper.floor_double(player.posX);
            int y = MathHelper.floor_double(player.posY);
            int z = MathHelper.floor_double(player.posZ);
            if(world.getBlockLightValue(x, y, z) < 8) {
               PotionEffect potion = player.getActivePotionEffect(Potion.nightVision);
               if(potion == null || potion.getDuration() <= TimeUtil.secsToTicks(15)) {
                  player.removePotionEffect(Potion.nightVision.id);
                  player.addPotionEffect(new PotionEffect(Potion.nightVision.id, TimeUtil.secsToTicks(20), 0, true));
               }
            }
         }
      }

   }

   @SideOnly(Side.CLIENT)
   public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack stack, int armorSlot) {
      if(this.modelClothesChest == null) {
         this.modelClothesChest = new ModelClothesDeath(0.4F);
      }

      ModelClothesDeath armorModel = null;
      if(stack != null && stack.getItem() instanceof ItemDeathsClothes) {
         int type = ((ItemArmor)stack.getItem()).armorType;
         armorModel = this.modelClothesChest;
         if(armorModel != null) {
            boolean isVisible = true;
            if(entityLiving != null && entityLiving.isInvisible()) {
               String heldStack = entityLiving.getClass().getSimpleName();
               isVisible = heldStack == null || heldStack.isEmpty() || heldStack.equals("AbstractSteve");
            }

            armorModel.bipedHead.showModel = isVisible && armorSlot == 0;
            armorModel.bipedHeadwear.showModel = isVisible && armorSlot == 0;
            armorModel.bipedBody.showModel = isVisible && armorSlot == 1;
            armorModel.bipedRightArm.showModel = isVisible && armorSlot == 1;
            armorModel.bipedLeftArm.showModel = isVisible && armorSlot == 1;
            armorModel.bipedRightLeg.showModel = isVisible && (armorSlot == 3 || armorSlot == 2);
            armorModel.bipedLeftLeg.showModel = isVisible && (armorSlot == 3 || armorSlot == 2);
            armorModel.isSneak = entityLiving.isSneaking();
            armorModel.isRiding = entityLiving.isRiding();
            armorModel.isChild = entityLiving.isChild();
            ItemStack heldStack1 = entityLiving.getEquipmentInSlot(0);
            armorModel.heldItemRight = heldStack1 != null?1:0;
            armorModel.aimedBow = false;
            if(entityLiving instanceof EntityPlayer && heldStack1 != null && ((EntityPlayer)entityLiving).getItemInUseDuration() > 0) {
               EnumAction enumaction = heldStack1.getItemUseAction();
               if(enumaction == EnumAction.block) {
                  armorModel.heldItemRight = 3;
               }

               armorModel.aimedBow = enumaction == EnumAction.bow;
            }

            return armorModel;
         }
      }

      return null;
   }

   @SideOnly(Side.CLIENT)
   public EnumRarity getRarity(ItemStack stack) {
      return EnumRarity.epic;
   }

   public String getItemStackDisplayName(ItemStack stack) {
      String baseName = super.getItemStackDisplayName(stack);
      return baseName;
   }

   public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advancedTooltips) {
      if(stack != null && (stack.getItem() != Witchery.Items.DEATH_HOOD || Config.instance().allowDeathsHoodToFreezeVictims)) {
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

   }

   public static boolean isFullSetWorn(EntityLivingBase entity) {
      int count = 0;

      for(int i = 1; i <= 4; ++i) {
         ItemStack item = entity.getEquipmentInSlot(i);
         if(item != null && item.getItem() instanceof ItemDeathsClothes) {
            ++count;
         }
      }

      return count >= 3;
   }
}
