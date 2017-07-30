package com.emoniph.witchery.item;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.WitcheryCreativeTab;
import com.emoniph.witchery.client.model.ModelVampireArmor;
import com.emoniph.witchery.util.ItemUtil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.common.ISpecialArmor.ArmorProperties;

public class ItemVampireClothes extends ItemArmor implements ISpecialArmor {

   boolean female;
   boolean metal;
   private int realDamageReduction;
   @SideOnly(Side.CLIENT)
   private IIcon iconUnderlay;
   @SideOnly(Side.CLIENT)
   private ModelVampireArmor modelClothesChest;
   @SideOnly(Side.CLIENT)
   private ModelVampireArmor modelClothesLegs;
   private static final String BIBLIOCRAFT_ARMOR_STAND_ENTITY_NAME = "AbstractSteve";


   public ItemVampireClothes(int armorSlot, boolean female, boolean metal) {
      super(ArmorMaterial.CLOTH, 1, armorSlot);
      this.female = female;
      this.metal = metal;
      this.setMaxDamage(ArmorMaterial.IRON.getDurability(armorSlot));
      this.realDamageReduction = metal?ArmorMaterial.IRON.getDamageReductionAmount(armorSlot):super.damageReduceAmount;
      this.setCreativeTab(WitcheryCreativeTab.INSTANCE);
   }

   public Item setUnlocalizedName(String itemName) {
      ItemUtil.registerItem(this, itemName);
      return super.setUnlocalizedName(itemName);
   }

   public int getItemEnchantability() {
      return ArmorMaterial.GOLD.getEnchantability();
   }

   public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
      return stack != null && super.armorType == 2?"witchery:textures/entities/vampirearmor.png":(stack != null?(type == null?"witchery:textures/entities/vampirearmor_over_first.png":"witchery:textures/entities/vampirearmor_over.png"):null);
   }

   public boolean hasColor(ItemStack stack) {
      return true;
   }

   public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {}

   public int getColor(ItemStack stack) {
      if(!this.hasColor(stack)) {
         return super.getColor(stack);
      } else {
         int color = super.getColor(stack);
         if(color == 10511680) {
            color = 13369344;
         }

         return color;
      }
   }

   @SideOnly(Side.CLIENT)
   public int getColorFromItemStack(ItemStack stack, int par2) {
      return super.getColorFromItemStack(stack, par2);
   }

   @SideOnly(Side.CLIENT)
   public boolean requiresMultipleRenderPasses() {
      return true;
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIconFromDamageForRenderPass(int damage, int renderPass) {
      return renderPass == 0?this.iconUnderlay:this.getIconFromDamage(damage);
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister iconRegister) {
      super.registerIcons(iconRegister);
      this.iconUnderlay = iconRegister.registerIcon(this.getIconString() + "_first");
   }

   @SideOnly(Side.CLIENT)
   public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack stack, int armorSlot) {
      if(this.modelClothesChest == null) {
         this.modelClothesChest = new ModelVampireArmor(0.3F, false, this.female, this.metal);
      }

      if(this.modelClothesLegs == null) {
         this.modelClothesLegs = new ModelVampireArmor(0.02F, true, this.female, this.metal);
      }

      ModelVampireArmor armorModel = null;
      if(stack != null && stack.getItem() instanceof ItemVampireClothes) {
         int type = ((ItemArmor)stack.getItem()).armorType;
         if(type == 2) {
            armorModel = this.modelClothesLegs;
         } else {
            armorModel = this.modelClothesChest;
         }

         if(armorModel != null) {
            boolean isVisible = true;
            if(entityLiving != null && entityLiving.isInvisible()) {
               String heldStack = entityLiving.getClass().getSimpleName();
               isVisible = heldStack == null || heldStack.isEmpty() || heldStack.equals("AbstractSteve");
            }

            armorModel.bipedHead.showModel = isVisible && armorSlot == 0;
            armorModel.bipedHeadwear.showModel = isVisible && armorSlot == 0;
            armorModel.bipedBody.showModel = isVisible && (armorSlot == 1 || armorSlot == 2);
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
      return EnumRarity.uncommon;
   }

   public String getItemStackDisplayName(ItemStack stack) {
      String baseName = super.getItemStackDisplayName(stack);
      return baseName;
   }

   public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advancedTooltips) {
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

   public static int numLightPiecesWorn(EntityLivingBase entity, boolean light) {
      int pieces = 0;

      for(int i = 1; i <= 4; ++i) {
         ItemStack item = entity.getEquipmentInSlot(i);
         if(item != null && item.getItem() instanceof ItemVampireClothes && !((ItemVampireClothes)item.getItem()).metal && light) {
            ++pieces;
         }
      }

      return pieces;
   }

   public static boolean isFlameProtectionActive(EntityLivingBase entity) {
      return numLightPiecesWorn(entity, true) >= 3 || numLightPiecesWorn(entity, true) >= 2;
   }

   public static boolean isExtendedFlameProtectionActive(EntityLivingBase entity) {
      return numLightPiecesWorn(entity, true) >= 4;
   }

   public static boolean isDrinkBoostActive(EntityLivingBase entity) {
      return numLightPiecesWorn(entity, true) >= 2;
   }

   public static boolean isMezmeriseBoostActive(EntityLivingBase entity) {
      return numLightPiecesWorn(entity, true) >= 3;
   }

   public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot) {
      return new ArmorProperties(0, (double)this.realDamageReduction / 25.0D, armor.getMaxDamage() + 1 - armor.getItemDamage());
   }

   public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
      return this.realDamageReduction;
   }

   public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {
      stack.damageItem(damage, entity);
   }
}
