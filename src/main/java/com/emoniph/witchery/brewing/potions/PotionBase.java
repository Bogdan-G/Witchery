package com.emoniph.witchery.brewing.potions;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.brewing.potions.IHandleRenderLiving;
import cpw.mods.fml.relauncher.ReflectionHelper;
import java.lang.reflect.Field;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.network.play.server.S1EPacketRemoveEntityEffect;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class PotionBase extends Potion {

   private boolean inventoryTextHidden;
   private boolean incurable;
   private boolean permenant;
   private static Field fieldPotionIsBadEffect;


   public PotionBase(int id, int color) {
      this(id, false, color);
   }

   protected PotionBase(int id, boolean debuff, int color) {
      super(id, debuff, color);
   }

   public void postContructInitialize() {}

   public static boolean isDebuff(Potion potion) {
      try {
         if(fieldPotionIsBadEffect == null) {
            fieldPotionIsBadEffect = ReflectionHelper.findField(Potion.class, new String[]{"isBadEffect", "field_76418_K", "K"});
         }

         boolean ex = ((Boolean)fieldPotionIsBadEffect.get(potion)).booleanValue();
         return ex;
      } catch (IllegalAccessException var2) {
         return false;
      }
   }

   protected boolean isDebuff() {
      return false;
   }

   public PotionBase getPotion() {
      return this;
   }

   public static boolean isCurable(Potion potion) {
      return !(potion instanceof PotionBase) || ((PotionBase)potion).isCurable();
   }

   public static boolean isPermenant(Potion potion) {
      return potion instanceof PotionBase && ((PotionBase)potion).isPermenant();
   }

   public boolean isCurable() {
      return !this.incurable;
   }

   public boolean isPermenant() {
      return this.permenant;
   }

   protected void setIncurable() {
      this.incurable = true;
   }

   protected void setPermenant() {
      this.permenant = true;
   }

   protected void hideInventoryText() {
      this.inventoryTextHidden = true;
   }

   public void applyAttributesModifiersToEntity(EntityLivingBase entity, BaseAttributeMap attributes, int amplifier) {
      super.applyAttributesModifiersToEntity(entity, attributes, amplifier);
      if(this instanceof IHandleRenderLiving) {
         PotionEffect effect = entity.getActivePotionEffect(this);
         Witchery.packetPipeline.sendToAll((Packet)(new S1DPacketEntityEffect(entity.getEntityId(), effect)));
      }

   }

   public void removeAttributesModifiersFromEntity(EntityLivingBase entity, BaseAttributeMap attributes, int amplifier) {
      super.removeAttributesModifiersFromEntity(entity, attributes, amplifier);
      if(this instanceof IHandleRenderLiving) {
         Witchery.packetPipeline.sendToAll((Packet)(new S1EPacketRemoveEntityEffect(entity.getEntityId(), new PotionEffect(super.id, 1))));
      }

   }

   public boolean shouldRenderInvText(PotionEffect effect) {
      return !this.inventoryTextHidden;
   }

   public void renderInventoryEffect(int x, int y, PotionEffect effect, Minecraft mc) {
      if(this.inventoryTextHidden) {
         mc.fontRenderer.drawStringWithShadow(Witchery.resource("witchery:potion.unknown"), x + 10 + 18, y + 6, 16777215);
      }

   }
}
