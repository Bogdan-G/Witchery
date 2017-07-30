package com.emoniph.witchery.item;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.WitcheryCreativeTab;
import com.emoniph.witchery.util.ItemUtil;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import java.util.UUID;
import net.minecraft.block.Block;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.Item.ToolMaterial;

public class ItemHuntsmanSpear extends ItemSword {

   protected static final UUID UUID_KB = UUID.fromString("032f4b80-ad10-11e3-a5e2-0800200c9a66");
   private float effectiveWeaponDamage;
   private ToolMaterial effectiveMaterial;
   private static final float BONUS_DAMAGE = 1.0F;


   public ItemHuntsmanSpear() {
      super(ToolMaterial.WOOD);
      this.effectiveMaterial = ToolMaterial.EMERALD;
      this.effectiveWeaponDamage = 4.0F + this.effectiveMaterial.getDamageVsEntity() + 1.0F;
      this.setMaxDamage(this.effectiveMaterial.getMaxUses());
      this.setCreativeTab(WitcheryCreativeTab.INSTANCE);
   }

   public Item setUnlocalizedName(String itemName) {
      ItemUtil.registerItem(this, itemName);
      return super.setUnlocalizedName(itemName);
   }

   @SideOnly(Side.CLIENT)
   public EnumRarity getRarity(ItemStack stack) {
      return EnumRarity.epic;
   }

   public boolean hasEffect(ItemStack stack) {
      return true;
   }

   public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean moreTips) {
      list.add(Witchery.resource("item.witchery:huntsmanspear.tip"));
   }

   public Multimap getItemAttributeModifiers() {
      HashMultimap multimap = HashMultimap.create();
      multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(Item.field_111210_e, "Weapon modifier", (double)this.effectiveWeaponDamage, 0));
      multimap.put(SharedMonsterAttributes.knockbackResistance.getAttributeUnlocalizedName(), new AttributeModifier(UUID_KB, "Knockback resist", 1.0D, 0));
      return multimap;
   }

   public boolean canHarvestBlock(Block par1Block, ItemStack stack) {
      return false;
   }

   public float func_150931_i() {
      return this.effectiveMaterial.getDamageVsEntity();
   }

}
