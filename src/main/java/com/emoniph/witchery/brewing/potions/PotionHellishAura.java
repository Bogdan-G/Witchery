package com.emoniph.witchery.brewing.potions;

import com.emoniph.witchery.brewing.potions.IHandleRenderLiving;
import com.emoniph.witchery.brewing.potions.PotionBase;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderLivingEvent.Post;
import org.lwjgl.opengl.GL11;

public class PotionHellishAura extends PotionBase implements IHandleRenderLiving {

   public PotionHellishAura(int id, int color) {
      super(id, color);
   }

   public boolean isReady(int duration, int amplifier) {
      byte frequencyFactor = 25;
      int k = frequencyFactor >> amplifier;
      return k > 0?duration % k == 0:true;
   }

   public void performEffect(EntityLivingBase entity, int amplifier) {
      World world = entity.worldObj;
      if(!world.isRemote) {
         List entities = world.getEntitiesWithinAABB(EntityLivingBase.class, entity.boundingBox.expand(1.5D, 0.0D, 1.5D));
         Iterator i$ = entities.iterator();

         while(i$.hasNext()) {
            EntityLivingBase otherEntity = (EntityLivingBase)i$.next();
            if(entity != otherEntity) {
               otherEntity.attackEntityFrom((new EntityDamageSource(DamageSource.onFire.damageType, entity)).setFireDamage().setDamageBypassesArmor(), 1.0F);
               ParticleEffect.FLAME.send(SoundEffect.MOB_GHAST_FIREBALL, otherEntity, (double)otherEntity.width, (double)otherEntity.height, 16);
               if(amplifier > 0) {
                  otherEntity.setFire(amplifier);
               }
            }
         }
      }

   }

   @SideOnly(Side.CLIENT)
   public void onLivingRender(World world, EntityLivingBase entity, Post event, int amplifier) {
      double p_76977_2_ = entity.posX;
      double p_76977_4_ = entity.posY;
      double p_76977_6_ = entity.posZ;
      GL11.glDisable(2896);
      IIcon iicon = Blocks.fire.getFireIcon(0);
      IIcon iicon1 = Blocks.fire.getFireIcon(1);
      GL11.glPushMatrix();
      float f1 = entity.width * 1.4F;
      GL11.glScalef(f1, f1, f1);
      Tessellator tessellator = Tessellator.instance;
      float f2 = 0.5F;
      float f3 = 0.0F;
      float f4 = entity.height / f1;
      float f5 = (float)(entity.posY - entity.boundingBox.minY);
      GL11.glRotatef(-RenderManager.instance.playerViewY, 0.0F, 1.0F, 0.0F);
      GL11.glTranslatef(0.0F, 0.0F, -0.3F + (float)((int)f4) * 0.02F);
      GL11.glColor4f(0.0F, 1.0F, 0.0F, 1.0F);
      float f6 = 0.0F;
      int i = 0;
      tessellator.startDrawingQuads();

      while(f4 > 0.0F) {
         IIcon iicon2 = i % 2 == 0?iicon:iicon1;
         RenderManager.instance.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
         float f7 = iicon2.getMinU();
         float f8 = iicon2.getMinV();
         float f9 = iicon2.getMaxU();
         float f10 = iicon2.getMaxV();
         if(i / 2 % 2 == 0) {
            float f11 = f9;
            f9 = f7;
            f7 = f11;
         }

         tessellator.addVertexWithUV((double)(f2 - f3), (double)(0.0F - f5), (double)f6, (double)f9, (double)f10);
         tessellator.addVertexWithUV((double)(-f2 - f3), (double)(0.0F - f5), (double)f6, (double)f7, (double)f10);
         tessellator.addVertexWithUV((double)(-f2 - f3), (double)(1.4F - f5), (double)f6, (double)f7, (double)f8);
         tessellator.addVertexWithUV((double)(f2 - f3), (double)(1.4F - f5), (double)f6, (double)f9, (double)f8);
         f4 -= 0.45F;
         f5 -= 0.45F;
         f2 *= 0.9F;
         f6 += 0.03F;
         ++i;
      }

      tessellator.draw();
      GL11.glPopMatrix();
      GL11.glEnable(2896);
   }
}
