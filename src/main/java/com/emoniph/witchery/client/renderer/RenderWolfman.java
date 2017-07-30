package com.emoniph.witchery.client.renderer;

import com.emoniph.witchery.client.model.ModelWolfman;
import com.emoniph.witchery.entity.EntityWolfman;
import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Map;
import java.util.UUID;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.client.IItemRenderer.ItemRendererHelper;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderWolfman extends RenderLiving {

   public ModelWolfman modelBipedMain;
   protected float field_77070_b;
   protected ModelWolfman field_82423_g;
   protected ModelWolfman field_82425_h;
   private static final Map field_110859_k = Maps.newHashMap();
   public static String[] bipedArmorFilenamePrefix = new String[]{"leather", "chainmail", "iron", "diamond", "gold"};
   private static final String __OBFID = "CL_00001001";
   public static final ResourceLocation TEXTURE = new ResourceLocation("witchery", "textures/entities/wolfman.png");


   public RenderWolfman(ModelWolfman model, float shadow) {
      this(model, shadow, 1.0F);
   }

   public RenderWolfman(ModelWolfman p_i1258_1_, float p_i1258_2_, float p_i1258_3_) {
      super(p_i1258_1_, p_i1258_2_);
      this.modelBipedMain = p_i1258_1_;
      this.field_77070_b = p_i1258_3_;
      this.func_82421_b();
   }

   protected void func_82421_b() {
      this.field_82423_g = new ModelWolfman(1.0F);
      this.field_82425_h = new ModelWolfman(0.5F);
   }

   @Deprecated
   public static ResourceLocation func_110857_a(ItemArmor p_110857_0_, int p_110857_1_) {
      return func_110858_a(p_110857_0_, p_110857_1_, (String)null);
   }

   @Deprecated
   public static ResourceLocation func_110858_a(ItemArmor p_110858_0_, int p_110858_1_, String p_110858_2_) {
      String s1 = String.format("textures/models/armor/%s_layer_%d%s.png", new Object[]{bipedArmorFilenamePrefix[p_110858_0_.renderIndex], Integer.valueOf(p_110858_1_ == 2?2:1), p_110858_2_ == null?"":String.format("_%s", new Object[]{p_110858_2_})});
      ResourceLocation resourcelocation = (ResourceLocation)field_110859_k.get(s1);
      if(resourcelocation == null) {
         resourcelocation = new ResourceLocation(s1);
         field_110859_k.put(s1, resourcelocation);
      }

      return resourcelocation;
   }

   protected int shouldRenderPass(EntityLiving p_77032_1_, int p_77032_2_, float p_77032_3_) {
      ItemStack itemstack = p_77032_1_.func_130225_q(3 - p_77032_2_);
      if(itemstack != null) {
         Item item = itemstack.getItem();
         if(item instanceof ItemArmor) {
            ItemArmor itemarmor = (ItemArmor)item;
            this.bindTexture(getArmorResource(p_77032_1_, itemstack, p_77032_2_, (String)null));
            ModelWolfman modelbiped = p_77032_2_ == 2?this.field_82425_h:this.field_82423_g;
            modelbiped.headMain.showModel = p_77032_2_ == 0;
            modelbiped.bodyUpper.showModel = p_77032_2_ == 1 || p_77032_2_ == 2;
            modelbiped.armRight.showModel = p_77032_2_ == 1;
            modelbiped.armLeft.showModel = p_77032_2_ == 1;
            modelbiped.legRightUpper.showModel = p_77032_2_ == 2 || p_77032_2_ == 3;
            modelbiped.legLeftUpper.showModel = p_77032_2_ == 2 || p_77032_2_ == 3;
            this.setRenderPassModel(modelbiped);
            modelbiped.onGround = super.mainModel.onGround;
            modelbiped.isRiding = super.mainModel.isRiding;
            modelbiped.isChild = super.mainModel.isChild;
            int j = itemarmor.getColor(itemstack);
            if(j != -1) {
               float f1 = (float)(j >> 16 & 255) / 255.0F;
               float f2 = (float)(j >> 8 & 255) / 255.0F;
               float f3 = (float)(j & 255) / 255.0F;
               GL11.glColor3f(f1, f2, f3);
               if(itemstack.isItemEnchanted()) {
                  return 31;
               }

               return 16;
            }

            GL11.glColor3f(1.0F, 1.0F, 1.0F);
            if(itemstack.isItemEnchanted()) {
               return 15;
            }

            return 1;
         }
      }

      return -1;
   }

   protected void func_82408_c(EntityLiving p_82408_1_, int p_82408_2_, float p_82408_3_) {
      ItemStack itemstack = p_82408_1_.func_130225_q(3 - p_82408_2_);
      if(itemstack != null) {
         Item item = itemstack.getItem();
         if(item instanceof ItemArmor) {
            this.bindTexture(getArmorResource(p_82408_1_, itemstack, p_82408_2_, "overlay"));
            float f1 = 1.0F;
            GL11.glColor3f(1.0F, 1.0F, 1.0F);
         }
      }

   }

   public void doRender(EntityLiving p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
      GL11.glColor3f(1.0F, 1.0F, 1.0F);
      ItemStack itemstack = p_76986_1_.getHeldItem();
      this.func_82420_a(p_76986_1_, itemstack);
      double d3 = p_76986_4_ - (double)p_76986_1_.yOffset;
      if(p_76986_1_.isSneaking()) {
         d3 -= 0.125D;
      }

      if(p_76986_1_ instanceof EntityWolfman) {
         EntityWolfman wolfman = (EntityWolfman)p_76986_1_;
         if(itemstack != null && wolfman.itemInUseCount > 0) {
            EnumAction enumaction = itemstack.getItemUseAction();
            if(enumaction == EnumAction.block) {
               this.modelBipedMain.heldItemRight = 3;
            } else if(enumaction == EnumAction.bow) {
               this.modelBipedMain.aimedBow = true;
            }
         }
      }

      super.doRender(p_76986_1_, p_76986_2_, d3, p_76986_6_, p_76986_8_, p_76986_9_);
      this.field_82423_g.aimedBow = this.field_82425_h.aimedBow = this.modelBipedMain.aimedBow = false;
      this.field_82423_g.isSneak = this.field_82425_h.isSneak = this.modelBipedMain.isSneak = false;
      this.field_82423_g.heldItemRight = this.field_82425_h.heldItemRight = this.modelBipedMain.heldItemRight = 0;
      this.modelBipedMain.isRiding = false;
   }

   protected ResourceLocation getEntityTexture(EntityLiving p_110775_1_) {
      return TEXTURE;
   }

   protected void func_82420_a(EntityLiving p_82420_1_, ItemStack p_82420_2_) {
      this.field_82423_g.heldItemRight = this.field_82425_h.heldItemRight = this.modelBipedMain.heldItemRight = p_82420_2_ != null?1:0;
      this.field_82423_g.isSneak = this.field_82425_h.isSneak = this.modelBipedMain.isSneak = p_82420_1_.isSneaking();
   }

   protected void renderEquippedItems(EntityLiving p_77029_1_, float p_77029_2_) {
      GL11.glColor3f(1.0F, 1.0F, 1.0F);
      super.renderEquippedItems(p_77029_1_, p_77029_2_);
      ItemStack itemstack = p_77029_1_.getHeldItem();
      ItemStack itemstack1 = p_77029_1_.func_130225_q(3);
      Item item;
      float f1;
      IItemRenderer customRenderer;
      boolean is3D;
      if(itemstack1 != null) {
         GL11.glPushMatrix();
         this.modelBipedMain.headMain.postRender(0.0625F);
         item = itemstack1.getItem();
         customRenderer = MinecraftForgeClient.getItemRenderer(itemstack1, ItemRenderType.EQUIPPED);
         is3D = customRenderer != null && customRenderer.shouldUseRenderHelper(ItemRenderType.EQUIPPED, itemstack1, ItemRendererHelper.BLOCK_3D);
         if(item instanceof ItemBlock) {
            if(is3D || RenderBlocks.renderItemIn3d(Block.getBlockFromItem(item).getRenderType())) {
               f1 = 0.625F;
               GL11.glTranslatef(0.0F, -0.25F, 0.0F);
               GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
               GL11.glScalef(f1, -f1, -f1);
            }

            super.renderManager.itemRenderer.renderItem(p_77029_1_, itemstack1, 0);
         } else if(item == Items.skull) {
            f1 = 1.0625F;
            GL11.glScalef(f1, -f1, -f1);
            GameProfile f2 = null;
            if(itemstack1.hasTagCompound()) {
               NBTTagCompound i = itemstack1.getTagCompound();
               if(i.hasKey("SkullOwner", 10)) {
                  f2 = NBTUtil.func_152459_a(i.getCompoundTag("SkullOwner"));
               } else if(i.hasKey("SkullOwner", 8) && !StringUtils.isNullOrEmpty(i.getString("SkullOwner"))) {
                  f2 = new GameProfile((UUID)null, i.getString("SkullOwner"));
               }
            }

            TileEntitySkullRenderer.field_147536_b.func_152674_a(-0.5F, 0.0F, -0.5F, 1, 180.0F, itemstack1.getItemDamage(), f2);
         }

         GL11.glPopMatrix();
      }

      if(itemstack != null && itemstack.getItem() != null) {
         item = itemstack.getItem();
         GL11.glPushMatrix();
         if(super.mainModel.isChild) {
            f1 = 0.5F;
            GL11.glTranslatef(0.0F, 0.625F, 0.0F);
            GL11.glRotatef(-20.0F, -1.0F, 0.0F, 0.0F);
            GL11.glScalef(f1, f1, f1);
         }

         this.modelBipedMain.armRight.postRender(0.0625F);
         GL11.glTranslatef(-0.0625F, 0.4375F, 0.0625F);
         customRenderer = MinecraftForgeClient.getItemRenderer(itemstack, ItemRenderType.EQUIPPED);
         is3D = customRenderer != null && customRenderer.shouldUseRenderHelper(ItemRenderType.EQUIPPED, itemstack, ItemRendererHelper.BLOCK_3D);
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

            this.func_82422_c();
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

         float f5;
         int var14;
         float var15;
         if(itemstack.getItem().requiresMultipleRenderPasses()) {
            for(var14 = 0; var14 < itemstack.getItem().getRenderPasses(itemstack.getItemDamage()); ++var14) {
               int f4 = itemstack.getItem().getColorFromItemStack(itemstack, var14);
               f5 = (float)(f4 >> 16 & 255) / 255.0F;
               var15 = (float)(f4 >> 8 & 255) / 255.0F;
               float f3 = (float)(f4 & 255) / 255.0F;
               GL11.glColor4f(f5, var15, f3, 1.0F);
               super.renderManager.itemRenderer.renderItem(p_77029_1_, itemstack, var14);
            }
         } else {
            var14 = itemstack.getItem().getColorFromItemStack(itemstack, 0);
            float var16 = (float)(var14 >> 16 & 255) / 255.0F;
            f5 = (float)(var14 >> 8 & 255) / 255.0F;
            var15 = (float)(var14 & 255) / 255.0F;
            GL11.glColor4f(var16, f5, var15, 1.0F);
            super.renderManager.itemRenderer.renderItem(p_77029_1_, itemstack, 0);
         }

         GL11.glPopMatrix();
      }

   }

   protected void func_82422_c() {
      GL11.glTranslatef(0.0F, 0.1875F, 0.0F);
   }

   protected void func_82408_c(EntityLivingBase p_82408_1_, int p_82408_2_, float p_82408_3_) {
      this.func_82408_c((EntityLiving)p_82408_1_, p_82408_2_, p_82408_3_);
   }

   protected int shouldRenderPass(EntityLivingBase p_77032_1_, int p_77032_2_, float p_77032_3_) {
      return this.shouldRenderPass((EntityLiving)p_77032_1_, p_77032_2_, p_77032_3_);
   }

   protected void renderEquippedItems(EntityLivingBase p_77029_1_, float p_77029_2_) {
      this.renderEquippedItems((EntityLiving)p_77029_1_, p_77029_2_);
   }

   public void doRender(EntityLivingBase p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
      this.doRender((EntityLiving)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
   }

   protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
      return this.getEntityTexture((EntityLiving)p_110775_1_);
   }

   public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
      this.doRender((EntityLiving)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
   }

   public static ResourceLocation getArmorResource(Entity entity, ItemStack stack, int slot, String type) {
      ItemArmor item = (ItemArmor)stack.getItem();
      String s1 = String.format("textures/models/armor/%s_layer_%d%s.png", new Object[]{bipedArmorFilenamePrefix[item.renderIndex], Integer.valueOf(slot == 2?2:1), type == null?"":String.format("_%s", new Object[]{type})});
      s1 = ForgeHooksClient.getArmorTexture(entity, stack, s1, slot, type);
      ResourceLocation resourcelocation = (ResourceLocation)field_110859_k.get(s1);
      if(resourcelocation == null) {
         resourcelocation = new ResourceLocation(s1);
         field_110859_k.put(s1, resourcelocation);
      }

      return resourcelocation;
   }

}
