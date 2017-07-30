package com.emoniph.witchery.infusion.infusions.symbols;

import com.emoniph.witchery.entity.EntitySpellEffect;
import com.emoniph.witchery.infusion.infusions.symbols.SymbolEffect;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public abstract class SymbolEffectProjectile extends SymbolEffect {

   private float size = 1.0F;
   private int color = 16711680;
   private int timetolive = -1;


   public SymbolEffectProjectile(int effectID, String unlocalisedName) {
      super(effectID, unlocalisedName);
   }

   public SymbolEffectProjectile(int effectID, String unlocalisedName, int spellCost, boolean curse, boolean fallsToEarth, String knowledgeKey, int cooldown) {
      super(effectID, unlocalisedName, spellCost, curse, fallsToEarth, knowledgeKey, cooldown);
   }

   public SymbolEffectProjectile(int effectID, String unlocalisedName, int spellCost, boolean curse, boolean fallsToEarth, String knowledgeKey, int cooldown, boolean isVisible) {
      super(effectID, unlocalisedName, spellCost, curse, fallsToEarth, knowledgeKey, cooldown, isVisible);
   }

   public SymbolEffectProjectile setColor(int color) {
      this.color = color;
      return this;
   }

   public SymbolEffectProjectile setSize(float size) {
      this.size = size;
      return this;
   }

   public SymbolEffectProjectile setTimeToLive(int ticks) {
      this.timetolive = ticks;
      return this;
   }

   public int getColor() {
      return this.color;
   }

   public float getSize() {
      return this.size;
   }

   public void perform(World world, EntityPlayer player, int effectLevel) {
      world.playAuxSFXAtEntity((EntityPlayer)null, 1008, (int)player.posX, (int)player.posY, (int)player.posZ, 0);
      float f = 1.0F;
      double motionX = (double)(-MathHelper.sin(player.rotationYaw / 180.0F * 3.1415927F) * MathHelper.cos(player.rotationPitch / 180.0F * 3.1415927F) * f);
      double motionZ = (double)(MathHelper.cos(player.rotationYaw / 180.0F * 3.1415927F) * MathHelper.cos(player.rotationPitch / 180.0F * 3.1415927F) * f);
      double motionY = (double)(-MathHelper.sin(player.rotationPitch / 180.0F * 3.1415927F) * f);
      EntitySpellEffect entity = new EntitySpellEffect(world, player, motionX, motionY, motionZ, this, effectLevel);
      if(this.timetolive > 0) {
         entity.setLifeTime(this.timetolive);
      }

      entity.setLocationAndAngles(player.posX, player.posY + (double)player.getEyeHeight(), player.posZ, entity.rotationYaw, entity.rotationPitch);
      entity.setPosition(player.posX, player.posY + (double)player.getEyeHeight(), player.posZ);
      double d6 = (double)MathHelper.sqrt_double(motionX * motionX + motionY * motionY + motionZ * motionZ);
      entity.accelerationX = motionX / d6 * 0.3D;
      entity.accelerationY = motionY / d6 * 0.3D;
      entity.accelerationZ = motionZ / d6 * 0.3D;
      double d8 = 1.5D;
      Vec3 vec3 = player.getLook(1.0F);
      entity.posX = player.posX + vec3.xCoord * 1.5D;
      entity.posY = player.posY + (double)player.eyeHeight - 0.10000000149011612D + vec3.yCoord * 1.5D;
      entity.posZ = player.posZ + vec3.zCoord * 1.5D;
      world.spawnEntityInWorld(entity);
   }

   public abstract void onCollision(World var1, EntityLivingBase var2, MovingObjectPosition var3, EntitySpellEffect var4);
}
