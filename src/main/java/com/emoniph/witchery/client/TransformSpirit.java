package com.emoniph.witchery.client;

import com.emoniph.witchery.client.model.ModelSpectre;
import com.emoniph.witchery.entity.EntityBanshee;
import com.emoniph.witchery.infusion.Infusion;
import com.emoniph.witchery.util.RenderUtil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class TransformSpirit {

   private EntityBanshee proxyEntity;
   private int currentColor = 0xFFFFFF;

   private RenderLiving proxyRenderer = new RenderLiving(new ModelSpectre(false), 0.0F) {
       protected ResourceLocation getEntityTexture(Entity par1Entity) {
           return new ResourceLocation("witchery", "textures/entities/banshee.png");
       }
       public void doRender(EntityLivingBase entity, double x, double y, double z, float yaw, float partialTicks) {
           GL11.glPushMatrix();
           RenderUtil.blend(true);
           float r = (float)(currentColor >> 16 & 255) / 255.0F;
           float g = (float)(currentColor >> 8 & 255) / 255.0F;
           float b = (float)(currentColor & 255) / 255.0F;
           GL11.glColor4f(r, g, b, 0.7F);
           super.doRender(entity, x, y, z, yaw, partialTicks);
           RenderUtil.blend(false);
           GL11.glPopMatrix();
       }
   };

   public EntityLivingBase getModel() {
      return this.proxyEntity;
   }

   public void syncModelWith(EntityLivingBase entity, boolean frontface) {
      if(this.proxyEntity == null) {
         this.proxyEntity = new EntityBanshee(entity.worldObj);
      } else if(this.proxyEntity.worldObj != entity.worldObj) {
         this.proxyEntity.setWorld(entity.worldObj);
      }

      this.proxyEntity.setPosition(entity.posX, entity.posY, entity.posZ);
      this.proxyEntity.lastTickPosX = entity.lastTickPosX;
      this.proxyEntity.lastTickPosY = entity.lastTickPosY;
      this.proxyEntity.lastTickPosZ = entity.lastTickPosZ;
      this.proxyEntity.motionX = entity.motionX;
      this.proxyEntity.motionY = entity.motionY;
      this.proxyEntity.motionZ = entity.motionZ;
      this.proxyEntity.rotationPitch = entity.rotationPitch;
      this.proxyEntity.rotationYaw = entity.rotationYaw;
      this.proxyEntity.rotationYawHead = entity.rotationYawHead;
      this.proxyEntity.prevRotationPitch = entity.prevRotationPitch;
      this.proxyEntity.prevRotationYaw = entity.prevRotationYaw;
      this.proxyEntity.prevRotationYawHead = entity.prevRotationYawHead;
      this.proxyEntity.renderYawOffset = frontface ? 0.0F : entity.renderYawOffset;
      this.proxyEntity.prevRenderYawOffset = frontface ? 0.0F : entity.prevRenderYawOffset;
      this.proxyEntity.ticksExisted = entity.ticksExisted;
      this.proxyEntity.isDead = false;

      if (entity instanceof EntityPlayer) {
          EntityPlayer player = (EntityPlayer)entity;
          int infusionID = Infusion.getInfusionID(player);
          int color = 0xFFFFFF; 
          if (infusionID == 4) {
              color = 0xFF0000; 
          } else if (infusionID == 2) {
              color = 0x00FF00; 
          } else if (infusionID == 3) {
              color = 0x800080; 
          }
          this.currentColor = color;
      }
   }

   public void render(World worldObj, EntityLivingBase entity, double x, double y, double z, RendererLivingEntity renderer, float partialTicks, boolean frontface) {
      this.syncModelWith(entity, frontface);
      this.proxyRenderer.setRenderManager(RenderManager.instance);
      float f1 = this.proxyEntity.prevRotationYaw + (this.proxyEntity.rotationYaw - this.proxyEntity.prevRotationYaw) * partialTicks;
      double d3 = -((double)this.proxyEntity.yOffset);
      
      this.proxyRenderer.doRender(this.proxyEntity, x, y + d3 + 0.5D, z, frontface ? 0.0F : f1, partialTicks);
   }
}
