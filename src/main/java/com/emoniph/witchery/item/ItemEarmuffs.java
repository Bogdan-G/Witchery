package com.emoniph.witchery.item;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.WitcheryCreativeTab;
import com.emoniph.witchery.client.model.ModelEarmuffs;
import com.emoniph.witchery.util.ItemUtil;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
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
import net.minecraftforge.client.event.sound.PlaySoundEvent17;

public class ItemEarmuffs extends ItemArmor {

   @SideOnly(Side.CLIENT)
   private ModelEarmuffs modelClothesChest;
   @SideOnly(Side.CLIENT)
   private ModelEarmuffs modelClothesLegs;


   public ItemEarmuffs(int armorSlot) {
      super(ArmorMaterial.CLOTH, 1, armorSlot);
      this.setMaxDamage(ArmorMaterial.CLOTH.getDurability(armorSlot));
      this.setCreativeTab(WitcheryCreativeTab.INSTANCE);
   }

   public Item setUnlocalizedName(String itemName) {
      ItemUtil.registerItem(this, itemName);
      return super.setUnlocalizedName(itemName);
   }

   public boolean hasColor(ItemStack stack) {
      return false;
   }

   @SideOnly(Side.CLIENT)
   public int getColorFromItemStack(ItemStack stack, int par2) {
      return super.getColorFromItemStack(stack, par2);
   }

   @SideOnly(Side.CLIENT)
   public boolean requiresMultipleRenderPasses() {
      return false;
   }

   public int getColor(ItemStack stack) {
      return !this.hasColor(stack)?16777215:super.getColor(stack);
   }

   @SideOnly(Side.CLIENT)
   public EnumRarity getRarity(ItemStack itemstack) {
      return EnumRarity.common;
   }

   public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advancedTooltips) {
      if(stack != null) {
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

   public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
      return stack != null?(type == null?"witchery:textures/entities/earmuffs.png":"witchery:textures/entities/empty64x64_overlay.png"):null;
   }

   @SideOnly(Side.CLIENT)
   public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack stack, int armorSlot) {
      if(this.modelClothesChest == null) {
         this.modelClothesChest = new ModelEarmuffs();
      }

      ModelEarmuffs armorModel = null;
      if(stack != null && stack.getItem() instanceof ItemArmor) {
         int type = ((ItemArmor)stack.getItem()).armorType;
         if(type != 2) {
            armorModel = this.modelClothesChest;
         } else {
            armorModel = this.modelClothesChest;
         }

         if(armorModel != null) {
            boolean isVisible = true;
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
            ItemStack heldStack = entityLiving.getEquipmentInSlot(0);
            armorModel.heldItemRight = heldStack != null?1:0;
            armorModel.aimedBow = false;
            if(entityLiving instanceof EntityPlayer && heldStack != null && ((EntityPlayer)entityLiving).getItemInUseDuration() > 0) {
               EnumAction enumaction = heldStack.getItemUseAction();
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

   public static boolean isHelmWorn(EntityPlayer entity) {
      ItemStack currentArmor = entity.getCurrentArmor(3);
      return currentArmor != null && currentArmor.getItem() == Witchery.Items.EARMUFFS;
   }

   public static class ClientEventHooks {

      @SubscribeEvent
      public void onSound(PlaySoundEvent17 event) {
         Minecraft MC = Minecraft.getMinecraft();
         EntityClientPlayerMP player = MC.thePlayer;
         if(player != null && ItemEarmuffs.isHelmWorn(player)) {
            event.result = null;
         }

      }
   }
}
