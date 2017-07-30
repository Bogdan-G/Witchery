package com.emoniph.witchery.item;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.WitcheryCreativeTab;
import com.emoniph.witchery.client.model.ModelHunterClothes;
import com.emoniph.witchery.common.ExtendedPlayer;
import com.emoniph.witchery.util.CreatureUtil;
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

public class ItemHunterClothes extends ItemArmor implements ISpecialArmor {

   private boolean silvered;
   private boolean garlicked;
   @SideOnly(Side.CLIENT)
   private IIcon iconOverlaySilver;
   @SideOnly(Side.CLIENT)
   private IIcon iconOverlaySilverGarlic;
   @SideOnly(Side.CLIENT)
   private ModelHunterClothes modelClothesChest;
   @SideOnly(Side.CLIENT)
   private ModelHunterClothes modelClothesLegs;
   private static final String BIBLIOCRAFT_ARMOR_STAND_ENTITY_NAME = "AbstractSteve";


   public ItemHunterClothes(int armorSlot, boolean silvered, boolean garlicked) {
      super(ArmorMaterial.CLOTH, 1, armorSlot);
      this.silvered = silvered;
      this.garlicked = garlicked;
      this.setMaxDamage(ArmorMaterial.IRON.getDurability(armorSlot));
      this.setCreativeTab(WitcheryCreativeTab.INSTANCE);
   }

   public Item setUnlocalizedName(String itemName) {
      ItemUtil.registerItem(this, itemName);
      return super.setUnlocalizedName(itemName);
   }

   public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
      return stack != null && super.armorType == 2?"witchery:textures/entities/hunterclothes2" + (type == null?"":"_overlay") + ".png":(stack != null?"witchery:textures/entities/hunterclothes" + (type == null?"":"_overlay") + ".png":null);
   }

   public boolean hasColor(ItemStack stack) {
      return true;
   }

   public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {
      if(!world.isRemote && player.ticksExisted % 20 == 2) {
         ExtendedPlayer playerEx = ExtendedPlayer.get(player);
         if(this.silvered && playerEx.getWerewolfLevel() > 0 || this.garlicked && playerEx.isVampire()) {
            player.attackEntityFrom(DamageSource.inFire, 1.0F);
         }
      }

   }

   public int getColor(ItemStack stack) {
      if(!this.hasColor(stack)) {
         return super.getColor(stack);
      } else {
         int color = super.getColor(stack);
         if(color == 10511680) {
            if(stack.getItem() == Witchery.Items.HUNTER_BOOTS) {
               color = 1642763;
            } else if(stack.getItem() == Witchery.Items.HUNTER_LEGS) {
               color = 4798251;
            } else {
               color = 4139550;
            }
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
      return this.silvered || this.garlicked;
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIconFromDamageForRenderPass(int damage, int renderPass) {
      return renderPass == 1?(this.garlicked?this.iconOverlaySilverGarlic:this.iconOverlaySilver):this.getIconFromDamage(damage);
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister iconRegister) {
      super.registerIcons(iconRegister);
      this.iconOverlaySilver = iconRegister.registerIcon(this.getIconString() + "_silvered");
      this.iconOverlaySilverGarlic = iconRegister.registerIcon(this.getIconString() + "_garlicked");
   }

   @SideOnly(Side.CLIENT)
   public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack stack, int armorSlot) {
      if(this.modelClothesChest == null) {
         this.modelClothesChest = new ModelHunterClothes(0.4F, false);
      }

      if(this.modelClothesLegs == null) {
         this.modelClothesLegs = new ModelHunterClothes(0.01F, false);
      }

      ModelHunterClothes armorModel = null;
      if(stack != null && stack.getItem() instanceof ItemHunterClothes) {
         int type = ((ItemArmor)stack.getItem()).armorType;
         if(type != 1 && type != 3) {
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

   public static boolean isFullSetWorn(EntityLivingBase entity, boolean silvered) {
      for(int i = 1; i <= 4; ++i) {
         ItemStack item = entity.getEquipmentInSlot(i);
         if(item == null) {
            return false;
         }

         if(!(item.getItem() instanceof ItemHunterClothes)) {
            return false;
         }

         ItemHunterClothes clothes = (ItemHunterClothes)item.getItem();
         if(silvered && !clothes.silvered) {
            return false;
         }
      }

      return true;
   }

   public static boolean isMagicalProtectionActive(EntityLivingBase entity) {
      return entity != null && isFullSetWorn(entity, false) && entity.worldObj != null && entity.worldObj.rand.nextDouble() < 0.25D;
   }

   public static boolean isCurseProtectionActive(EntityLivingBase entity) {
      return entity != null && isFullSetWorn(entity, false) && entity.worldObj != null && entity.worldObj.rand.nextDouble() < 0.9D;
   }

   public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot) {
      if(this.silvered && source != null && CreatureUtil.isWerewolf(source.getEntity())) {
         source.getEntity().attackEntityFrom(DamageSource.inFire, 1.0F);
         return new ArmorProperties(0, (double)super.damageReduceAmount * 2.5D / 25.0D, armor.getMaxDamage() + 1 - armor.getItemDamage());
      } else {
         return this.garlicked && source != null && CreatureUtil.isVampire(source.getEntity())?new ArmorProperties(0, (double)super.damageReduceAmount * 2.5D / 25.0D, armor.getMaxDamage() + 1 - armor.getItemDamage()):new ArmorProperties(0, (double)super.damageReduceAmount / 25.0D, armor.getMaxDamage() + 1 - armor.getItemDamage());
      }
   }

   public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
      return super.damageReduceAmount;
   }

   public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {
      if(!this.silvered || source == null || !CreatureUtil.isWerewolf(source.getEntity())) {
         if(!this.garlicked || source == null || !CreatureUtil.isVampire(source.getEntity())) {
            stack.damageItem(damage, entity);
         }
      }
   }

   public static boolean isWolfProtectionActive(EntityLivingBase entity) {
      return entity != null && isFullSetWorn(entity, true);
   }
}
