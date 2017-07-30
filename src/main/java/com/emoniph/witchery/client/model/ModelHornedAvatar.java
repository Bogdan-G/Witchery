package com.emoniph.witchery.client.model;

import com.emoniph.witchery.entity.EntityHornedHuntsman;
import com.emoniph.witchery.util.Config;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class ModelHornedAvatar extends ModelBase {

   ModelRenderer horns;
   ModelRenderer head;
   ModelRenderer body;
   ModelRenderer rightarm;
   ModelRenderer rightleg;
   ModelRenderer chest;
   ModelRenderer rightshin;
   ModelRenderer rightfoot;
   ModelRenderer rightforearm;
   ModelRenderer hips;
   ModelRenderer leftarm;
   ModelRenderer leftforearm;
   ModelRenderer leftleg;
   ModelRenderer leftshin;
   ModelRenderer leftfoot;
   ModelRenderer spear;
   private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");


   public ModelHornedAvatar() {
      super.textureWidth = 128;
      super.textureHeight = 128;
      this.setTextureOffset("spear.shaft", 61, 14);
      this.setTextureOffset("spear.tip1", 60, 8);
      this.setTextureOffset("spear.tip2", 60, 5);
      this.horns = new ModelRenderer(this, 0, 88);
      this.horns.addBox(-10.0F, -24.0F, 0.0F, 20, 17, 0);
      this.horns.setRotationPoint(0.0F, -16.0F, 0.0F);
      this.horns.setTextureSize(128, 128);
      this.horns.mirror = true;
      this.setRotation(this.horns, 0.0F, 0.0F, 0.0F);
      this.head = new ModelRenderer(this, 4, 112);
      this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8);
      this.head.setRotationPoint(0.0F, -16.0F, 0.0F);
      this.head.setTextureSize(128, 128);
      this.head.mirror = true;
      this.setRotation(this.head, 0.0F, 0.0F, 0.0F);
      this.body = new ModelRenderer(this, 12, 61);
      this.body.addBox(-6.0F, 0.0F, -3.0F, 12, 8, 6);
      this.body.setRotationPoint(0.0F, -8.0F, 0.0F);
      this.body.setTextureSize(128, 128);
      this.body.mirror = true;
      this.setRotation(this.body, 0.0F, 0.0F, 0.0F);
      this.rightarm = new ModelRenderer(this, 72, 50);
      this.rightarm.addBox(-4.0F, -2.0F, -2.0F, 4, 13, 4);
      this.rightarm.setRotationPoint(-10.0F, -13.0F, 0.0F);
      this.rightarm.setTextureSize(128, 128);
      this.rightarm.mirror = true;
      this.setRotation(this.rightarm, 0.0F, 0.0F, 0.0F);
      this.rightleg = new ModelRenderer(this, 72, 0);
      this.rightleg.addBox(-2.0F, 0.0F, -2.0F, 4, 10, 4);
      this.rightleg.setRotationPoint(-4.0F, 3.0F, -1.0F);
      this.rightleg.setTextureSize(128, 128);
      this.rightleg.mirror = true;
      this.setRotation(this.rightleg, 0.5235988F, 0.0F, 0.0F);
      this.chest = new ModelRenderer(this, 0, 43);
      this.chest.addBox(0.0F, 0.0F, 0.0F, 20, 8, 10);
      this.chest.setRotationPoint(-10.0F, -16.0F, -5.0F);
      this.chest.setTextureSize(128, 128);
      this.chest.mirror = true;
      this.setRotation(this.chest, 0.0F, 0.0F, 0.0F);
      this.rightshin = new ModelRenderer(this, 68, 14);
      this.rightshin.addBox(-3.0F, -2.0F, -3.0F, 6, 14, 6);
      this.rightshin.setRotationPoint(-4.0F, 12.0F, 5.0F);
      this.rightshin.setTextureSize(128, 128);
      this.rightshin.mirror = true;
      this.setRotation(this.rightshin, -0.5235988F, 0.0F, 0.0F);
      this.rightfoot = new ModelRenderer(this, 69, 34);
      this.rightfoot.addBox(-2.0F, 0.0F, -5.0F, 4, 3, 7);
      this.rightfoot.setRotationPoint(-4.0F, 21.0F, 0.0F);
      this.rightfoot.setTextureSize(128, 128);
      this.rightfoot.mirror = true;
      this.setRotation(this.rightfoot, 0.0F, 0.0F, 0.0F);
      this.rightforearm = new ModelRenderer(this, 68, 67);
      this.rightforearm.addBox(-3.0F, 0.0F, -3.0F, 6, 14, 6);
      this.rightforearm.setRotationPoint(-12.0F, -3.0F, 0.0F);
      this.rightforearm.setTextureSize(128, 128);
      this.rightforearm.mirror = true;
      this.setRotation(this.rightforearm, -0.5235988F, 0.0F, 0.0F);
      this.hips = new ModelRenderer(this, 8, 75);
      this.hips.addBox(-7.0F, 0.0F, -4.0F, 14, 4, 8);
      this.hips.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.hips.setTextureSize(128, 128);
      this.hips.mirror = true;
      this.setRotation(this.hips, 0.0F, 0.0F, 0.0F);
      this.leftarm = new ModelRenderer(this, 72, 50);
      this.leftarm.addBox(0.0F, -2.0F, -2.0F, 4, 13, 4);
      this.leftarm.setRotationPoint(10.0F, -13.0F, 0.0F);
      this.leftarm.setTextureSize(128, 128);
      this.leftarm.mirror = true;
      this.setRotation(this.leftarm, 0.0F, 0.0F, 0.0F);
      this.leftforearm = new ModelRenderer(this, 68, 67);
      this.leftforearm.addBox(-3.0F, 0.0F, -3.0F, 6, 14, 6);
      this.leftforearm.setRotationPoint(12.0F, -3.0F, 0.0F);
      this.leftforearm.setTextureSize(128, 128);
      this.leftforearm.mirror = true;
      this.setRotation(this.leftforearm, -0.5235988F, 0.0F, 0.0F);
      this.leftleg = new ModelRenderer(this, 72, 0);
      this.leftleg.addBox(-2.0F, 0.0F, -2.0F, 4, 10, 4);
      this.leftleg.setRotationPoint(4.0F, 3.0F, -1.0F);
      this.leftleg.setTextureSize(128, 128);
      this.leftleg.mirror = true;
      this.setRotation(this.leftleg, 0.5235988F, 0.0F, 0.0F);
      this.leftshin = new ModelRenderer(this, 68, 14);
      this.leftshin.addBox(-3.0F, -2.0F, -3.0F, 6, 14, 6);
      this.leftshin.setRotationPoint(4.0F, 12.0F, 5.0F);
      this.leftshin.setTextureSize(128, 128);
      this.leftshin.mirror = true;
      this.setRotation(this.leftshin, -0.5235988F, 0.0F, 0.0F);
      this.leftfoot = new ModelRenderer(this, 69, 34);
      this.leftfoot.addBox(-2.0F, 0.0F, -5.0F, 4, 3, 7);
      this.leftfoot.setRotationPoint(4.0F, 21.0F, 0.0F);
      this.leftfoot.setTextureSize(128, 128);
      this.leftfoot.mirror = true;
      this.setRotation(this.leftfoot, 0.0F, 0.0F, 0.0F);
      this.head.addChild(this.horns);
      this.leftleg.addChild(this.leftshin);
      this.leftshin.addChild(this.leftfoot);
      this.rightleg.addChild(this.rightshin);
      this.rightshin.addChild(this.rightfoot);
      this.rightarm.addChild(this.rightforearm);
      this.leftarm.addChild(this.leftforearm);
      this.spear = new ModelRenderer(this, "spear");
      this.spear.setRotationPoint(-12.0F, 4.0F, -10.0F);
      this.setRotation(this.spear, 0.0F, 0.0F, 0.0F);
      this.spear.mirror = true;
      this.spear.addBox("shaft", -0.5F, -30.0F, -0.5F, 1, 50, 1);
      this.spear.addBox("tip1", -1.5F, -35.0F, 0.0F, 3, 6, 0);
      this.spear.addBox("tip2", 0.0F, -35.0F, -1.5F, 0, 6, 3);
      this.rightforearm.addChild(this.spear);
      this.horns.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.leftforearm.setRotationPoint(2.0F, 10.0F, 0.0F);
      this.rightforearm.setRotationPoint(-2.0F, 10.0F, 0.0F);
      this.leftshin.setRotationPoint(0.0F, 10.0F, 0.0F);
      this.leftshin.rotateAngleX = -0.9F;
      this.leftfoot.setRotationPoint(0.0F, 10.0F, 0.0F);
      this.leftfoot.rotateAngleX = 0.5F;
      this.rightshin.setRotationPoint(0.0F, 10.0F, 0.0F);
      this.rightshin.rotateAngleX = -0.9F;
      this.rightfoot.setRotationPoint(0.0F, 10.0F, 0.0F);
      this.rightfoot.rotateAngleX = 0.5F;
      this.spear.setRotationPoint(0.0F, 12.0F, 0.0F);
      this.spear.rotateAngleX = 1.5F;
   }

   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      super.render(entity, f, f1, f2, f3, f4, f5);
      this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
      this.head.render(f5);
      this.body.render(f5);
      this.rightarm.render(f5);
      this.rightleg.render(f5);
      this.chest.render(f5);
      this.hips.render(f5);
      this.leftarm.render(f5);
      this.leftleg.render(f5);
      Minecraft mc = Minecraft.getMinecraft();
      if(mc.gameSettings.fancyGraphics && Config.instance().renderHuntsmanGlintEffect) {
         float f9 = (float)entity.ticksExisted;
         mc.renderEngine.bindTexture(RES_ITEM_GLINT);
         GL11.glEnable(3042);
         float f10 = 0.5F;
         GL11.glColor4f(f10, f10, f10, 1.0F);
         GL11.glDepthFunc(514);
         GL11.glDepthMask(false);

         for(int k = 0; k < 2; ++k) {
            GL11.glDisable(2896);
            float f11 = 0.76F;
            GL11.glColor4f(0.0F * f11, 0.5F * f11, 0.0F * f11, 1.0F);
            GL11.glBlendFunc(768, 1);
            GL11.glMatrixMode(5890);
            GL11.glLoadIdentity();
            float f12 = f9 * (0.001F + (float)k * 0.003F) * 20.0F;
            float f13 = 0.33333334F;
            GL11.glScalef(f13, f13, f13);
            GL11.glRotatef(30.0F - (float)k * 60.0F, 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef(0.0F, f12, 0.0F);
            GL11.glMatrixMode(5888);
            this.head.render(f5);
            this.body.render(f5);
            this.rightarm.render(f5);
            this.rightleg.render(f5);
            this.chest.render(f5);
            this.hips.render(f5);
            this.leftarm.render(f5);
            this.leftleg.render(f5);
         }

         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         GL11.glMatrixMode(5890);
         GL11.glDepthMask(true);
         GL11.glLoadIdentity();
         GL11.glMatrixMode(5888);
         GL11.glEnable(2896);
         GL11.glDisable(3042);
         GL11.glDepthFunc(515);
      }

   }

   private void setRotation(ModelRenderer model, float x, float y, float z) {
      model.rotateAngleX = x;
      model.rotateAngleY = y;
      model.rotateAngleZ = z;
   }

   public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity) {
      this.head.rotateAngleY = par4 / 57.295776F;
      this.head.rotateAngleX = par5 / 57.295776F;
      this.leftleg.rotateAngleX = -1.3F * this.func_78172_a(par1, 13.0F) * par2 + 0.5F;
      this.rightleg.rotateAngleX = 1.3F * this.func_78172_a(par1, 13.0F) * par2 + 0.5F;
      float angle = -1.5F * this.func_78172_a(par1, 13.0F) * par2 - 1.0F;
      this.leftshin.rotateAngleX = 0.8F * (this.rightleg.rotateAngleX - 0.5F) - 1.0F;
      this.rightshin.rotateAngleX = 0.8F * (this.leftleg.rotateAngleX - 0.5F) - 1.0F;
      this.leftfoot.rotateAngleX = Math.max(1.4F * (this.leftleg.rotateAngleX - 0.5F) + 0.5F, 0.2F);
      this.rightfoot.rotateAngleX = Math.max(1.4F * (this.rightleg.rotateAngleX - 0.5F) + 0.5F, 0.2F);
      this.leftleg.rotateAngleY = 0.0F;
      this.rightleg.rotateAngleY = 0.0F;
   }

   public void setLivingAnimations(EntityLivingBase par1EntityLiving, float par2, float par3, float par4) {
      EntityHornedHuntsman entityDemon = (EntityHornedHuntsman)par1EntityLiving;
      int i = entityDemon.getAttackTimer();
      if(i > 0) {
         this.rightarm.rotateAngleX = -2.0F + 1.5F * this.func_78172_a((float)i - par4, 10.0F);
      } else {
         this.rightarm.rotateAngleX = (-0.2F + 1.5F * this.func_78172_a(par2, 13.0F)) * par3 * 0.2F;
         this.leftarm.rotateAngleX = (-0.2F - 1.5F * this.func_78172_a(par2, 13.0F)) * par3 * 0.2F;
      }

   }

   private float func_78172_a(float par1, float par2) {
      return (Math.abs(par1 % par2 - par2 * 0.5F) - par2 * 0.25F) / (par2 * 0.25F);
   }

}
