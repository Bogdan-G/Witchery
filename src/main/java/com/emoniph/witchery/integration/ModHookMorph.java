package com.emoniph.witchery.integration;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.integration.ModHook;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class ModHookMorph extends ModHook {

   @SideOnly(Side.CLIENT)
   private static Method methodHasMorph;


   public String getModID() {
      return "Morph";
   }

   protected void doInit() {
      Witchery.modHooks.isMorphPresent = true;
   }

   protected void doPostInit() {}

   protected void doReduceMagicPower(EntityLivingBase entity, float factor) {}

   @SideOnly(Side.CLIENT)
   public static boolean isMorphed(EntityPlayer player, boolean client) {
      if(!Witchery.modHooks.isMorphPresent) {
         return false;
      } else {
         if(methodHasMorph == null) {
            try {
               methodHasMorph = Class.forName("morph.common.core.ApiHandler").getDeclaredMethod("hasMorph", new Class[]{String.class, Boolean.TYPE});
            } catch (ClassNotFoundException var5) {
               ;
            } catch (NoSuchMethodException var6) {
               ;
            }
         }

         if(methodHasMorph != null) {
            try {
               return ((Boolean)methodHasMorph.invoke((Object)null, new Object[]{player.getCommandSenderName(), Boolean.valueOf(client)})).booleanValue();
            } catch (IllegalAccessException var3) {
               ;
            } catch (InvocationTargetException var4) {
               ;
            }
         }

         return false;
      }
   }
}
