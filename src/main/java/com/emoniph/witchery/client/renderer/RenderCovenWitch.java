package com.emoniph.witchery.client.renderer;

import com.emoniph.witchery.entity.EntityCovenWitch;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.model.ModelWitch;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderCovenWitch extends RenderLiving {

   private static final ResourceLocation BASIC_TEXTURE = new ResourceLocation("witchery", "textures/entities/covenwitch1.png");
   private static final ResourceLocation A_TEXTURE = new ResourceLocation("witchery", "textures/entities/covenwitch2.png");
   private static final ResourceLocation B_TEXTURE = new ResourceLocation("witchery", "textures/entities/covenwitch3.png");
   private static final ResourceLocation C_TEXTURE = new ResourceLocation("witchery", "textures/entities/covenwitch4.png");
   private static final ResourceLocation D_TEXTURE = new ResourceLocation("witchery", "textures/entities/covenwitch5.png");
   private final ModelWitch model;


   public RenderCovenWitch() {
      super(new ModelWitch(0.0F), 0.5F);
      this.model = (ModelWitch)super.mainModel;
   }

   public void func_82412_a(EntityCovenWitch par1EntityCovenWitch, double par2, double par4, double par6, float par8, float par9) {
      ItemStack itemstack = par1EntityCovenWitch.getHeldItem();
      this.model.field_82900_g = itemstack != null;
      super.doRender(par1EntityCovenWitch, par2, par4, par6, par8, par9);
   }

   protected ResourceLocation getCovenWitchTextures(EntityCovenWitch par1EntityCovenWitch) {
      switch(par1EntityCovenWitch.getTameSkin()) {
      case 0:
      default:
         return BASIC_TEXTURE;
      case 1:
         return A_TEXTURE;
      case 2:
         return B_TEXTURE;
      case 3:
         return C_TEXTURE;
      case 4:
         return D_TEXTURE;
      }
   }

   protected void func_82411_a(EntityCovenWitch par1EntityCovenWitch, float par2) {
      float f1 = 1.0F;
      GL11.glColor3f(f1, f1, f1);
      super.renderEquippedItems(par1EntityCovenWitch, par2);
      ItemStack itemstack = par1EntityCovenWitch.getHeldItem();
      if(itemstack != null) {
         GL11.glPushMatrix();
         float f2;
         if(super.mainModel.isChild) {
            f2 = 0.5F;
            GL11.glTranslatef(0.0F, 0.625F, 0.0F);
            GL11.glRotatef(-20.0F, -1.0F, 0.0F, 0.0F);
            GL11.glScalef(f2, f2, f2);
         }

         this.model.villagerNose.postRender(0.0625F);
         GL11.glTranslatef(-0.0625F, 0.53125F, 0.21875F);
         if(itemstack.getItem() instanceof ItemBlock && RenderBlocks.renderItemIn3d(Block.getBlockFromItem(itemstack.getItem()).getRenderType())) {
            f2 = 0.5F;
            GL11.glTranslatef(0.0F, 0.1875F, -0.3125F);
            f2 *= 0.75F;
            GL11.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
            GL11.glScalef(f2, -f2, f2);
         } else if(itemstack.getItem() == Items.bow) {
            f2 = 0.625F;
            GL11.glTranslatef(0.0F, 0.125F, 0.3125F);
            GL11.glRotatef(-20.0F, 0.0F, 1.0F, 0.0F);
            GL11.glScalef(f2, -f2, f2);
            GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
         } else if(itemstack.getItem().isFull3D()) {
            f2 = 0.625F;
            if(itemstack.getItem().shouldRotateAroundWhenRendering()) {
               GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
               GL11.glTranslatef(0.0F, -0.125F, 0.0F);
            }

            this.func_82410_b();
            GL11.glScalef(f2, -f2, f2);
            GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
         } else {
            f2 = 0.375F;
            GL11.glTranslatef(0.25F, 0.1875F, -0.1875F);
            GL11.glScalef(f2, f2, f2);
            GL11.glRotatef(60.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(20.0F, 0.0F, 0.0F, 1.0F);
         }

         GL11.glRotatef(-15.0F, 1.0F, 0.0F, 0.0F);
         GL11.glRotatef(40.0F, 0.0F, 0.0F, 1.0F);
         super.renderManager.itemRenderer.renderItem(par1EntityCovenWitch, itemstack, 0);
         if(itemstack.getItem().requiresMultipleRenderPasses()) {
            super.renderManager.itemRenderer.renderItem(par1EntityCovenWitch, itemstack, 1);
         }

         GL11.glPopMatrix();
      }

   }

   protected void func_82410_b() {
      GL11.glTranslatef(0.0F, 0.1875F, 0.0F);
   }

   protected void func_82409_b(EntityCovenWitch par1EntityCovenWitch, float par2) {
      float f1 = 0.9375F;
      GL11.glScalef(f1, f1, f1);
   }

   public void doRender(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9) {
      this.func_82412_a((EntityCovenWitch)par1EntityLiving, par2, par4, par6, par8, par9);
   }

   protected void preRenderCallback(EntityLivingBase par1EntityLivingBase, float par2) {
      this.func_82409_b((EntityCovenWitch)par1EntityLivingBase, par2);
   }

   protected void renderEquippedItems(EntityLivingBase par1EntityLivingBase, float par2) {
      this.func_82411_a((EntityCovenWitch)par1EntityLivingBase, par2);
   }

   public void doRender(EntityLivingBase par1EntityLivingBase, double par2, double par4, double par6, float par8, float par9) {
      this.func_82412_a((EntityCovenWitch)par1EntityLivingBase, par2, par4, par6, par8, par9);
   }

   protected ResourceLocation getEntityTexture(Entity par1Entity) {
      return this.getCovenWitchTextures((EntityCovenWitch)par1Entity);
   }

   public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
      this.func_82412_a((EntityCovenWitch)par1Entity, par2, par4, par6, par8, par9);
   }

}
