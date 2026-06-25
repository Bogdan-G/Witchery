package com.emoniph.witchery.network;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.infusion.infusions.symbols.EffectRegistry;
import com.emoniph.witchery.infusion.infusions.symbols.SymbolEffect;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public class PacketSpellPrepared implements IMessage {

   private int effectID;
   private int level;


   public PacketSpellPrepared() {}

   public PacketSpellPrepared(SymbolEffect effect, int level) {
      this.effectID = effect.getEffectID();
      this.level = level;
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeInt(this.effectID);
      buffer.writeInt(this.level);
   }

   public void fromBytes(ByteBuf buffer) {
      this.effectID = buffer.readInt();
      this.level = buffer.readInt();
   }

   public static class Handler implements IMessageHandler<PacketSpellPrepared, IMessage> {

      public IMessage onMessage(PacketSpellPrepared message, MessageContext ctx) {
         EntityPlayer player = Witchery.proxy.getPlayer(ctx);
         boolean changed = player.getEntityData().getInteger("WITCSpellEffectID") != message.effectID;
         player.getEntityData().setInteger("WITCSpellEffectID", message.effectID);
         player.getEntityData().setInteger("WITCSpellEffectEnhanced", message.level);
         SoundEffect.NOTE_HARP.playAtPlayer(player.worldObj, player, 1.0F);
         if(changed && !player.worldObj.isRemote) {
            SymbolEffect effect = EffectRegistry.instance().getEffect(message.effectID);
            if(effect != null) {
               int color = effect.getDisplayColor();
               // Telegraph visible to all nearby players: a colored aura around the caster.
               ParticleEffect.SPELL_COLORED.send(SoundEffect.NONE, player, 0.6D, 1.8D, 32, color);
            }
         }

         return null;
      }
   }
}
