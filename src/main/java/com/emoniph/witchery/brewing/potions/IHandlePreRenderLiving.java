package com.emoniph.witchery.brewing.potions;

import com.emoniph.witchery.brewing.potions.PotionBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderLivingEvent.Pre;

public interface IHandlePreRenderLiving {

   PotionBase getPotion();

   @SideOnly(Side.CLIENT)
   void onLivingRender(World var1, EntityLivingBase var2, Pre var3, int var4);
}
