package com.emoniph.witchery.network;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.common.ExtendedPlayer;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public class PacketExtendedPlayerSync implements IMessage {

   private int werewolfLevel;
   private int vampireLevel;
   private int spiritLevel;
   private int bloodLevel;
   private int ultimate;
   private int creatureOrdinal;
   private int selected;
   private int ultimateCharges;
   private int reserveBlood;
   private boolean isAstralProjecting;


   public PacketExtendedPlayerSync() {}

   public PacketExtendedPlayerSync(ExtendedPlayer extendedPlayer) {
      this.werewolfLevel = extendedPlayer.getWerewolfLevel();
      this.creatureOrdinal = extendedPlayer.getCreatureTypeOrdinal();
      this.vampireLevel = extendedPlayer.getVampireLevel();
      this.spiritLevel = extendedPlayer.getSpiritLevel();
      this.bloodLevel = extendedPlayer.getBloodPower();
      this.selected = extendedPlayer.getSelectedVampirePower().ordinal();
      this.ultimate = extendedPlayer.getVampireUltimate().ordinal();
      this.ultimateCharges = extendedPlayer.getVampireUltimateCharges();
      this.reserveBlood = extendedPlayer.getBloodReserve();
      this.isAstralProjecting = extendedPlayer.isAstralProjecting();
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeInt(this.werewolfLevel);
      buffer.writeInt(this.creatureOrdinal);
      buffer.writeInt(this.vampireLevel);
      buffer.writeInt(this.spiritLevel);
      buffer.writeInt(this.bloodLevel);
      buffer.writeInt(this.selected);
      buffer.writeInt(this.ultimate);
      buffer.writeInt(this.ultimateCharges);
      buffer.writeInt(this.reserveBlood);
      buffer.writeBoolean(this.isAstralProjecting);
   }

   public void fromBytes(ByteBuf buffer) {
      this.werewolfLevel = buffer.readInt();
      this.creatureOrdinal = buffer.readInt();
      this.vampireLevel = buffer.readInt();
      this.spiritLevel = buffer.readInt();
      this.bloodLevel = buffer.readInt();
      this.selected = buffer.readInt();
      this.ultimate = buffer.readInt();
      this.ultimateCharges = buffer.readInt();
      this.reserveBlood = buffer.readInt();
      this.isAstralProjecting = buffer.readBoolean();
   }

   public static class Handler implements IMessageHandler<PacketExtendedPlayerSync, IMessage> {

      public IMessage onMessage(PacketExtendedPlayerSync message, MessageContext ctx) {
         EntityPlayer player = Witchery.proxy.getPlayer(ctx);
         if (player == null) {
            return null;
         }
         ExtendedPlayer playerEx = ExtendedPlayer.get(player);
         if (playerEx == null) {
            return null;
         }
         playerEx.applySyncData(message.werewolfLevel, message.creatureOrdinal, message.vampireLevel, message.spiritLevel, message.bloodLevel, message.selected, message.ultimate, message.ultimateCharges, message.reserveBlood);
         playerEx.setAstralProjecting(message.isAstralProjecting);
         return null;
      }
   }
}
