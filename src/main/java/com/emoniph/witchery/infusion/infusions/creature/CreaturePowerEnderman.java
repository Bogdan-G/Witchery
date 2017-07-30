package com.emoniph.witchery.infusion.infusions.creature;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.infusion.infusions.InfusionOtherwhere;
import com.emoniph.witchery.infusion.infusions.creature.CreaturePower;
import com.emoniph.witchery.network.PacketParticles;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.emoniph.witchery.util.TargetPointUtil;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class CreaturePowerEnderman extends CreaturePower {

   public CreaturePowerEnderman(int powerID) {
      super(powerID, EntityEnderman.class);
   }

   public void onActivate(World world, EntityPlayer player, int elapsedTicks, MovingObjectPosition mop) {
      if(!world.isRemote) {
         if(mop != null) {
            Witchery.packetPipeline.sendToAllAround(new PacketParticles(ParticleEffect.PORTAL, SoundEffect.MOB_ENDERMEN_PORTAL, player, 0.5D, 2.0D), TargetPointUtil.from(player, 16.0D));
            InfusionOtherwhere.teleportEntity(player, mop);
            Witchery.packetPipeline.sendToAllAround(new PacketParticles(ParticleEffect.PORTAL, SoundEffect.MOB_ENDERMEN_PORTAL, player, 0.5D, 2.0D), TargetPointUtil.from(player, 16.0D));
         } else {
            world.playSoundAtEntity(player, "note.snare", 0.5F, 0.4F / ((float)Math.random() * 0.4F + 0.8F));
         }
      }

   }
}
