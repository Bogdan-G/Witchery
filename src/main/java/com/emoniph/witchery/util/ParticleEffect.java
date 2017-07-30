package com.emoniph.witchery.util;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.network.PacketParticles;
import com.emoniph.witchery.util.Coord;
import com.emoniph.witchery.util.SoundEffect;
import com.emoniph.witchery.util.TargetPointUtil;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public enum ParticleEffect {

   HUGE_EXPLOSION("HUGE_EXPLOSION", 0, "hugeexplosion"),
   LARGE_EXPLODE("LARGE_EXPLODE", 1, "largeexplode"),
   WATER_BUBBLE("WATER_BUBBLE", 2, "bubble"),
   SUSPENDED("SUSPENDED", 3, "suspended"),
   DEPTH_SUSPEND("DEPTH_SUSPEND", 4, "depthsuspend"),
   TOWN_AURA("TOWN_AURA", 5, "townaura"),
   CRIT("CRIT", 6, "crit"),
   MAGIC_CRIT("MAGIC_CRIT", 7, "magicCrit"),
   SMOKE("SMOKE", 8, "smoke"),
   MOB_SPELL("MOB_SPELL", 9, "mobSpell"),
   SPELL("SPELL", 10, "spell"),
   INSTANT_SPELL("INSTANT_SPELL", 11, "instantSpell"),
   NOTE("NOTE", 12, "note"),
   PORTAL("PORTAL", 13, "portal"),
   ENCHANTMENT_TABLE("ENCHANTMENT_TABLE", 14, "enchantmenttable"),
   EXPLODE("EXPLODE", 15, "explode"),
   FLAME("FLAME", 16, "flame"),
   LAVA("LAVA", 17, "lava"),
   FOOTSTEP("FOOTSTEP", 18, "footstep"),
   SPLASH("SPLASH", 19, "splash"),
   LARGE_SMOKE("LARGE_SMOKE", 20, "largesmoke"),
   CLOUD("CLOUD", 21, "cloud"),
   REDDUST("REDDUST", 22, "reddust"),
   SNOWBALL_POOF("SNOWBALL_POOF", 23, "snowballpoof"),
   DRIP_WATER("DRIP_WATER", 24, "dripWater"),
   DRIP_LAVA("DRIP_LAVA", 25, "dripLava"),
   SNOW_SHOVEL("SNOW_SHOVEL", 26, "snowshovel"),
   SLIME("SLIME", 27, "slime"),
   HEART("HEART", 28, "heart"),
   ICON_CRACK("ICON_CRACK", 29, "iconcrack_"),
   TILE_CRACK("TILE_CRACK", 30, "tilecrack_"),
   SPELL_COLORED("SPELL_COLORED", 31, "spell");
   final String particleID;
   // $FF: synthetic field
   private static final ParticleEffect[] $VALUES = new ParticleEffect[]{HUGE_EXPLOSION, LARGE_EXPLODE, WATER_BUBBLE, SUSPENDED, DEPTH_SUSPEND, TOWN_AURA, CRIT, MAGIC_CRIT, SMOKE, MOB_SPELL, SPELL, INSTANT_SPELL, NOTE, PORTAL, ENCHANTMENT_TABLE, EXPLODE, FLAME, LAVA, FOOTSTEP, SPLASH, LARGE_SMOKE, CLOUD, REDDUST, SNOWBALL_POOF, DRIP_WATER, DRIP_LAVA, SNOW_SHOVEL, SLIME, HEART, ICON_CRACK, TILE_CRACK, SPELL_COLORED};


   private ParticleEffect(String var1, int var2, String particleID) {
      this.particleID = particleID;
   }

   public String toString() {
      return this.particleID;
   }

   public void send(SoundEffect sound, World world, double x, double y, double z, double width, double height, int range) {
      this.send(sound, world, x, y, z, width, height, range, 16777215);
   }

   public void send(SoundEffect sound, World world, double x, double y, double z, double width, double height, int range, int color) {
      if(!world.isRemote) {
         Witchery.packetPipeline.sendToAllAround(new PacketParticles(this, sound, x, y, z, width, height, color), TargetPointUtil.from(world, x, y, z, (double)range));
      }

   }

   public void send(SoundEffect sound, Entity entity, double width, double height, int range) {
      if(!entity.worldObj.isRemote) {
         Witchery.packetPipeline.sendToAllAround(new PacketParticles(this, sound, entity, width, height), TargetPointUtil.from(entity, (double)range));
      }

   }

   public void send(SoundEffect sound, Entity entity, double width, double height, int range, int color) {
      if(!entity.worldObj.isRemote) {
         Witchery.packetPipeline.sendToAllAround(new PacketParticles(this, sound, entity, width, height, color), TargetPointUtil.from(entity, (double)range));
      }

   }

   public void send(SoundEffect sound, TileEntity tile, double width, double height, int range, int color) {
      if(!tile.getWorldObj().isRemote) {
         Witchery.packetPipeline.sendToAllAround(new PacketParticles(this, sound, 0.5D + (double)tile.xCoord, 0.5D + (double)tile.yCoord, 0.5D + (double)tile.zCoord, width, height, color), TargetPointUtil.from(tile.getWorldObj(), (double)tile.xCoord, (double)tile.yCoord, (double)tile.zCoord, (double)range));
      }

   }

   public void send(SoundEffect sound, World world, Coord center, double width, double height, int range) {
      this.send(sound, world, (double)center.x + 0.5D, (double)center.y, (double)center.z + 0.5D, width, height, range);
   }

}
