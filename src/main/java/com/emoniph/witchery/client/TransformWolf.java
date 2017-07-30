package com.emoniph.witchery.client;

import com.emoniph.witchery.entity.EntityBroom;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelWolf;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderWolf;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.client.IItemRenderer.ItemRendererHelper;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class TransformWolf {

   private EntityWolf proxyEntity;
   private RenderWolf proxyRenderer = new RenderWolf(new ModelWolf(), new ModelWolf(), 0.5F);


   public EntityLivingBase getModel() {
      return this.proxyEntity;
   }

   public void syncModelWith(EntityLivingBase entity, boolean frontface) {
      if(this.proxyEntity == null) {
         this.proxyEntity = new EntityWolf(entity.worldObj);
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
      this.proxyEntity.moveForward = entity.moveForward;
      this.proxyEntity.moveStrafing = entity.moveStrafing;
      this.proxyEntity.onGround = entity.onGround;
      this.proxyEntity.prevPosX = entity.prevPosX;
      this.proxyEntity.prevPosY = entity.prevPosY;
      this.proxyEntity.prevPosZ = entity.prevPosZ;
      this.proxyEntity.rotationPitch = entity.rotationPitch;
      this.proxyEntity.rotationYaw = entity.rotationYaw;
      this.proxyEntity.rotationYawHead = entity.rotationYawHead;
      this.proxyEntity.prevRotationPitch = entity.prevRotationPitch;
      this.proxyEntity.prevRotationYaw = entity.prevRotationYaw;
      this.proxyEntity.prevRotationYawHead = entity.prevRotationYawHead;
      this.proxyEntity.limbSwing = entity.limbSwing;
      this.proxyEntity.limbSwingAmount = entity.limbSwingAmount;
      this.proxyEntity.prevLimbSwingAmount = entity.prevLimbSwingAmount;
      this.proxyEntity.isSwingInProgress = entity.isSwingInProgress;
      this.proxyEntity.swingProgress = entity.swingProgress;
      this.proxyEntity.prevSwingProgress = entity.prevSwingProgress;
      this.proxyEntity.renderYawOffset = frontface?0.0F:entity.renderYawOffset;
      this.proxyEntity.prevRenderYawOffset = frontface?0.0F:entity.prevRenderYawOffset;
      this.proxyEntity.ticksExisted = entity.ticksExisted;
      this.proxyEntity.isDead = false;
      this.proxyEntity.isAirBorne = entity.isAirBorne;
      this.proxyEntity.yOffset = 0.0F;
      this.proxyEntity.setSneaking(entity.isSneaking());
      this.proxyEntity.setSprinting(entity.isSprinting());
      this.proxyEntity.setInvisible(entity.isInvisible());
      this.proxyEntity.setSitting(entity.isRiding());
   }

   public void render(World worldObj, EntityLivingBase entity, double x, double y, double z, RendererLivingEntity renderer, float partialTicks, boolean frontface) {
      this.syncModelWith(entity, frontface);
      this.proxyRenderer.setRenderManager(RenderManager.instance);
      float f1 = this.proxyEntity.prevRotationYaw + (this.proxyEntity.rotationYaw - this.proxyEntity.prevRotationYaw) * partialTicks;
      double d3 = -((double)this.proxyEntity.yOffset);
      if(this.proxyEntity.isSneaking() && !(entity instanceof EntityPlayerSP)) {
         d3 -= 0.125D;
      }

      if(entity.isRiding()) {
         Entity f2 = entity.ridingEntity;
         d3 += f2.getMountedYOffset() + (entity.ridingEntity instanceof EntityBroom?(double)f2.height - 0.2D:0.0D);
      }

      float f21 = 1.0F;
      GL11.glColor3f(f21, f21, f21);
      this.proxyRenderer.doRender(this.proxyEntity, x, y + d3, z, frontface?0.0F:f1, partialTicks);
   }

   protected void renderEquippedItems(ItemRenderer itemRenderer, EntityLivingBase p_77029_1_, float p_77029_2_) {
      GL11.glColor3f(1.0F, 1.0F, 1.0F);
      ItemStack itemstack = p_77029_1_.getHeldItem();
      if(itemstack != null && itemstack.getItem() != null) {
         Item item = itemstack.getItem();
         GL11.glPushMatrix();
         GL11.glTranslatef(-0.0625F, 0.4375F, 0.0625F);
         IItemRenderer customRenderer = MinecraftForgeClient.getItemRenderer(itemstack, ItemRenderType.EQUIPPED);
         boolean is3D = customRenderer != null && customRenderer.shouldUseRenderHelper(ItemRenderType.EQUIPPED, itemstack, ItemRendererHelper.BLOCK_3D);
         float f1;
         if(item instanceof ItemBlock && (is3D || RenderBlocks.renderItemIn3d(Block.getBlockFromItem(item).getRenderType()))) {
            f1 = 0.5F;
            GL11.glTranslatef(0.0F, 0.1875F, -0.3125F);
            f1 *= 0.75F;
            GL11.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
            GL11.glScalef(-f1, -f1, f1);
         } else if(item == Items.bow) {
            f1 = 0.625F;
            GL11.glTranslatef(0.0F, 0.125F, 0.3125F);
            GL11.glRotatef(-20.0F, 0.0F, 1.0F, 0.0F);
            GL11.glScalef(f1, -f1, f1);
            GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
         } else if(item.isFull3D()) {
            f1 = 0.625F;
            if(item.shouldRotateAroundWhenRendering()) {
               GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
               GL11.glTranslatef(0.0F, -0.125F, 0.0F);
            }

            GL11.glScalef(f1, -f1, f1);
            GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
         } else {
            f1 = 0.375F;
            GL11.glTranslatef(0.25F, 0.1875F, -0.1875F);
            GL11.glScalef(f1, f1, f1);
            GL11.glRotatef(60.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(20.0F, 0.0F, 0.0F, 1.0F);
         }

         float f2;
         int i;
         float f5;
         if(itemstack.getItem().requiresMultipleRenderPasses()) {
            for(i = 0; i < itemstack.getItem().getRenderPasses(itemstack.getItemDamage()); ++i) {
               int f4 = itemstack.getItem().getColorFromItemStack(itemstack, i);
               f5 = (float)(f4 >> 16 & 255) / 255.0F;
               f2 = (float)(f4 >> 8 & 255) / 255.0F;
               float f3 = (float)(f4 & 255) / 255.0F;
               GL11.glColor4f(f5, f2, f3, 1.0F);
               itemRenderer.renderItem(p_77029_1_, itemstack, i);
            }
         } else {
            i = itemstack.getItem().getColorFromItemStack(itemstack, 0);
            float var14 = (float)(i >> 16 & 255) / 255.0F;
            f5 = (float)(i >> 8 & 255) / 255.0F;
            f2 = (float)(i & 255) / 255.0F;
            GL11.glColor4f(var14, f5, f2, 1.0F);
            itemRenderer.renderItem(p_77029_1_, itemstack, 0);
         }

         GL11.glPopMatrix();
      }

   }
}
