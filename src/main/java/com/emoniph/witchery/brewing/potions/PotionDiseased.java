package com.emoniph.witchery.brewing.potions;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.brewing.potions.IHandleRenderLiving;
import com.emoniph.witchery.brewing.potions.ModelOverlayRenderer;
import com.emoniph.witchery.brewing.potions.PotionBase;
import com.emoniph.witchery.util.BlockProtect;
import com.emoniph.witchery.util.Coord;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderLivingEvent.Post;
import org.lwjgl.opengl.GL11;

public class PotionDiseased extends PotionBase implements IHandleRenderLiving {

   @SideOnly(Side.CLIENT)
   private static ResourceLocation TEXTURE;


   public PotionDiseased(int id, int color) {
      super(id, true, color);
   }

   public void postContructInitialize() {
      this.setIncurable();
      this.func_111184_a(SharedMonsterAttributes.attackDamage, "22653B89-116E-49DC-9B6B-9971489B5BE5", 2.0D, 0);
   }

   public double func_111183_a(int amplifier, AttributeModifier p_111183_2_) {
      return (double)(-0.5F * (float)(Math.min(amplifier, 1) + 1));
   }

   public boolean isReady(int duration, int amplifier) {
      return duration % 40 == 4;
   }

   public void performEffect(EntityLivingBase entity, int amplifier) {
      if(!entity.worldObj.isRemote && entity.worldObj.rand.nextInt(3) == 0) {
         Coord coord = new Coord(entity);
         if(entity.worldObj.isAirBlock(coord.x, coord.y, coord.z) && entity.worldObj.getBlock(coord.x, coord.y - 1, coord.z).getMaterial().isSolid() && BlockProtect.checkModsForBreakOK(entity.worldObj, coord.x, coord.y, coord.z, entity)) {
            coord.setBlock(entity.worldObj, Witchery.Blocks.DISEASE);
         }
      }

   }

   @SideOnly(Side.CLIENT)
   public void onLivingRender(World world, EntityLivingBase entity, Post event, int amplifier) {
      if(TEXTURE == null) {
         TEXTURE = new ResourceLocation("witchery", "textures/entities/disease_overlay.png");
      }

      GL11.glPushMatrix();
      Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);
      ModelOverlayRenderer.render(entity, event.x, event.y, event.z, event.renderer);
      GL11.glPopMatrix();
   }
}
