package com.emoniph.witchery.client;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.common.ExtendedPlayer;
import com.emoniph.witchery.network.PacketClearFallDamage;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.relauncher.Side;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class KeyboardHandler {

   private final List bindings = new ArrayList();
   private final KeyboardHandler.KeyInfo JUMP;
   private final KeyboardHandler.KeyInfo HOTBAR1;


   public KeyboardHandler() {
      this.JUMP = new KeyboardHandler.KeyInfo(Minecraft.getMinecraft().gameSettings.keyBindJump, this.bindings) {

         private boolean isJumping;
         private int remainingJumps;
         private boolean clearFall;

         protected void onKeyDown(EntityPlayer player, boolean repeated, boolean end) {
            if(!player.capabilities.isCreativeMode && !end) {
               if(this.isJumping) {
                  if(this.remainingJumps > 0) {
                     int jumpsLeft = this.remainingJumps--;
                     player.motionY = 0.42D;
                     if(player.isPotionActive(Potion.jump)) {
                        player.motionY += 0.1D * (double)(1 + player.getActivePotionEffect(Potion.jump).getAmplifier());
                     }
                  }
               } else {
                  this.isJumping = player.isAirBorne;
                  if(player.isPotionActive(Witchery.Potions.DOUBLE_JUMP)) {
                     this.remainingJumps += 1 + player.getActivePotionEffect(Witchery.Potions.DOUBLE_JUMP).getAmplifier();
                  }
               }
            }

            if(this.clearFall) {
               this.clearFall = false;
               player.fallDistance = 0.0F;
               Witchery.packetPipeline.sendToServer(new PacketClearFallDamage());
            }

         }
         protected void onTick(EntityPlayer player, boolean end) {
            if(player.onGround) {
               this.isJumping = false;
               this.remainingJumps = 0;
            }

         }
      };
      this.HOTBAR1 = new KeyboardHandler.KeyInfo(Minecraft.getMinecraft().gameSettings.keyBindsHotbar[0], this.bindings) {
         protected void onKeyDown(EntityPlayer player, boolean repeated, boolean end) {
            if(!end) {
               ExtendedPlayer playerEx = ExtendedPlayer.get(player);
               if(playerEx.isVampire() && !Minecraft.getMinecraft().ingameGUI.getChatGUI().getChatOpen()) {
                  int MAXPOWER = playerEx.getMaxAvailablePowerOrdinal();
                  if(player.inventory.currentItem == 0) {
                     int power = playerEx.getSelectedVampirePower().ordinal();
                     if(power == MAXPOWER) {
                        playerEx.setSelectedVampirePower(ExtendedPlayer.VampirePower.NONE, true);
                     } else {
                        playerEx.setSelectedVampirePower(ExtendedPlayer.VampirePower.values()[power + 1], true);
                     }
                  }
               }
            }

         }
         protected void onKeyUp(EntityPlayer player, boolean end) {}
         protected void onTick(EntityPlayer player, boolean end) {}
      };

      for(int i = 1; i < Minecraft.getMinecraft().gameSettings.keyBindsHotbar.length; ++i) {
         final KeyBinding binding = Minecraft.getMinecraft().gameSettings.keyBindsHotbar[i];
         KeyboardHandler.KeyInfo var10001 = new KeyboardHandler.KeyInfo(binding, this.bindings) {
            protected void onKeyDown(EntityPlayer player, boolean repeated, boolean end) {
               if(!end) {
                  ExtendedPlayer playerEx = ExtendedPlayer.get(player);
                  if(playerEx.isVampire() && playerEx.getSelectedVampirePower() != ExtendedPlayer.VampirePower.NONE) {
                     playerEx.setSelectedVampirePower(ExtendedPlayer.VampirePower.NONE, true);
                  }
               }

            }
         };
      }

   }

   @SubscribeEvent
   public void onTick(ClientTickEvent event) {
      if(event.side == Side.CLIENT) {
         Minecraft mc = Minecraft.getMinecraft();
         EntityClientPlayerMP player = mc.thePlayer;
         if(player != null) {
            Iterator i$ = this.bindings.iterator();

            while(i$.hasNext()) {
               KeyboardHandler.KeyInfo keyInfo = (KeyboardHandler.KeyInfo)i$.next();
               keyInfo.doTick(player, event.phase == Phase.END);
            }
         }
      }

   }

   private abstract static class KeyInfo {

      private final KeyBinding bind;
      private boolean repeat;
      private boolean down;


      public KeyInfo(KeyBinding bind, List bindings) {
         this.bind = bind;
         bindings.add(this);
      }

      public void doTick(EntityPlayer player, boolean end) {
         int keyCode = this.bind.getKeyCode();
         boolean newlyDown = keyCode < 0?Mouse.isButtonDown(keyCode + 100):Keyboard.isKeyDown(keyCode);
         if(newlyDown != this.down || newlyDown && this.repeat) {
            if(newlyDown) {
               this.onKeyDown(player, newlyDown != this.down, end);
            } else {
               this.onKeyUp(player, end);
            }

            if(end) {
               this.down = newlyDown;
            }
         }

         if(end) {
            this.onTick(player, end);
         }

      }

      protected void onKeyDown(EntityPlayer player, boolean repeated, boolean end) {}

      protected void onKeyUp(EntityPlayer player, boolean end) {}

      protected void onTick(EntityPlayer player, boolean end) {}
   }
}
