package com.emoniph.witchery.util;

import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class KeyBindHelper {

   public static boolean isKeyBindDown(KeyBinding keyBinding) {
      return keyBinding.getKeyCode() >= 0?Keyboard.isKeyDown(keyBinding.getKeyCode()):Mouse.isButtonDown(keyBinding.getKeyCode() + 100);
   }
}
