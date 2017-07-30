package com.emoniph.witchery.util;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.network.PacketSound;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public enum SoundEffect {

   NONE("NONE", 0, ""),
   RANDOM_ORB("RANDOM_ORB", 1, "random.orb"),
   RANDOM_FIZZ("RANDOM_FIZZ", 2, "random.fizz"),
   NOTE_SNARE("NOTE_SNARE", 3, "note.snare"),
   WATER_SPLASH("WATER_SPLASH", 4, "game.player.swim.splash"),
   DAMAGE_HIT("DAMAGE_HIT", 5, "damage.hit"),
   FIREWORKS_BLAST1("FIREWORKS_BLAST1", 6, "fireworks.blast"),
   WATER_SWIM("WATER_SWIM", 7, "game.player.swim"),
   NOTE_HARP("NOTE_HARP", 8, "note.harp"),
   NOTE_PLING("NOTE_PLING", 9, "note.pling"),
   RANDOM_EXPLODE("RANDOM_EXPLODE", 10, "random.explode"),
   RANDOM_POP("RANDOM_POP", 11, "random.pop"),
   DIG_CLOTH("DIG_CLOTH", 12, "dig.cloth"),
   MOB_SLIME_BIG("MOB_SLIME_BIG", 13, "mob.slime.big"),
   MOB_SLIME_SMALL("MOB_SLIME_SMALL", 14, "mob.slime.small"),
   MOB_ZOMBIE_DEATH("MOB_ZOMBIE_DEATH", 15, "mob.zombie.death"),
   MOB_ENDERMEN_PORTAL("MOB_ENDERMEN_PORTAL", 16, "mob.endermen.portal"),
   FIRE_FIRE("FIRE_FIRE", 17, "fire.fire"),
   FIRE_IGNITE("FIRE_IGNITE", 18, "fire.ignite"),
   MOB_GHAST_FIREBALL("MOB_GHAST_FIREBALL", 19, "mob.ghast.fireball"),
   MOB_WITHER_SPAWN("MOB_WITHER_SPAWN", 20, "mob.wither.spawn"),
   MOB_HORSE_SKELETON_DEATH("MOB_HORSE_SKELETON_DEATH", 21, "mob.horse.skeleton.death"),
   RANDOM_SPLASH("RANDOM_SPLASH", 22, "witchery:random.splash"),
   MOB_SILVERFISH_KILL("MOB_SILVERFISH_KILL", 23, "mob.silverfish.kill"),
   MOB_ZOMBIE_INFECT("MOB_ZOMBIE_INFECT", 24, "mob.zombie.infect"),
   MOB_WOLF_DEATH("MOB_WOLF_DEATH", 25, "mob.wolf.death"),
   MOB_OCELOT_DEATH("MOB_OCELOT_DEATH", 26, "mob.ocelot.death"),
   MOB_ENDERDRAGON_GROWL("MOB_ENDERDRAGON_GROWL", 27, "mob.enderdragon.growl"),
   MOB_HORSE_SKELETON_HIT("MOB_HORSE_SKELETON_HIT", 28, "mob.horse.skeleton.hit"),
   RANDOM_LEVELUP("RANDOM_LEVELUP", 29, "random.levelup"),
   MOB_SPIDER_SAY("MOB_SPIDER_SAY", 30, "mob.spider.say"),
   MOB_ZOMBIE_SAY("MOB_ZOMBIE_SAY", 31, "mob.zombie.say"),
   WITCHERY_RANDOM_THEYCOME("WITCHERY_RANDOM_THEYCOME", 32, "witchery:random.theycome"),
   MOB_ENDERDRAGON_HIT("MOB_ENDERDRAGON_HIT", 33, "mob.enderdragon.hit"),
   WITCHERY_MOB_BABA_DEATH("WITCHERY_MOB_BABA_DEATH", 34, "witchery:mob.baba.baba_death"),
   WITCHERY_MOB_BABA_LIVING("WITCHERY_MOB_BABA_LIVING", 35, "witchery:mob.baba.baba_living"),
   WITCHERY_RANDOM_CLICK("WITCHERY_RANDOM_CLICK", 36, "witchery:random.click"),
   WITCHERY_RANDOM_WINDUP("WITCHERY_RANDOM_WINDUP", 37, "witchery:random.wind_up"),
   WITCHERY_RANDOM_LOVED("WITCHERY_RANDOM_LOVED", 38, "witchery:random.loved"),
   MOB_ENDERMAN_IDLE("MOB_ENDERMAN_IDLE", 39, "mob.enderman.idle"),
   MOB_WITHER_DEATH("MOB_WITHER_DEATH", 40, "mob.wither.death"),
   RANDOM_BREATH("RANDOM_BREATH", 41, "random.breath"),
   WITCHERY_MOB_SPECTRE_SPECTRE_HIT("WITCHERY_MOB_SPECTRE_SPECTRE_HIT", 42, "witchery:mob.spectre.spectre_hit"),
   WITCHERY_MOB_SPECTRE_SPECTRE_SAY("WITCHERY_MOB_SPECTRE_SPECTRE_SAY", 43, "witchery:mob.spectre.spectre_say"),
   MOB_BLAZE_DEATH("MOB_BLAZE_DEATH", 44, "mob.blaze.death"),
   WITCHERY_MOB_IMP_LAUGH("WITCHERY_MOB_IMP_LAUGH", 45, "witchery:mob.imp.laugh"),
   MOB_GHAST_DEATH("MOB_GHAST_DEATH", 46, "mob.ghast.death"),
   MOB_CREEPER_DEATH("MOB_CREEPER_DEATH", 47, "mob.creeper.death"),
   WITCHERY_RANDOM_CHALK("WITCHERY_RANDOM_CHALK", 48, "witchery:random.chalk"),
   WITCHERY_MOB_WOLFMAN_HOWL("WITCHERY_MOB_WOLFMAN_HOWL", 49, "witchery:mob.wolfman.howl"),
   WITCHERY_MOB_WOLFMAN_EAT("WITCHERY_MOB_WOLFMAN_EAT", 50, "witchery:mob.wolfman.eat"),
   WITCHERY_MOB_WOLFMAN_LORD("WITCHERY_MOB_WOLFMAN_LORD", 51, "witchery:mob.wolfman.lord"),
   WITCHERY_RANDOM_HORN("WITCHERY_RANDOM_HORN", 52, "witchery:random.horn"),
   WITCHERY_RANDOM_MANTRAP("WITCHERY_RANDOM_MANTRAP", 53, "witchery:random.mantrap"),
   WITCHERY_MOB_WOLFMAN_TALK("WITCHERY_MOB_WOLFMAN_TALK", 54, "witchery:mob.wolfman.say"),
   WITCHERY_RANDOM_HYPNOSIS("WITCHERY_RANDOM_HYPNOSIS", 55, "witchery:random.hypnosis"),
   WITCHERY_RANDOM_DRINK("WITCHERY_RANDOM_DRINK", 56, "witchery:random.drink"),
   WITCHERY_RANDOM_POOF("WITCHERY_RANDOM_POOF", 57, "witchery:random.poof"),
   WITCHERY_MOB_LILITH_TALK("WITCHERY_MOB_LILITH_TALK", 58, "witchery:mob.lilith.say"),
   WITCHERY_RANDOM_SWORD_DRAW("WITCHERY_RANDOM_SWORD_DRAW", 59, "witchery:random.sworddraw"),
   WITCHERY_RANDOM_SWORD_SHEATHE("WITCHERY_RANDOM_SWORD_SHEATHE", 60, "witchery:random.swordsheathe"),
   WITCHERY_MOB_REFLECTION_SPEECH("WITCHERY_MOB_REFLECTION_SPEECH", 61, "witchery:mob.reflection.speech");
   final String sound;
   // $FF: synthetic field
   private static final SoundEffect[] $VALUES = new SoundEffect[]{NONE, RANDOM_ORB, RANDOM_FIZZ, NOTE_SNARE, WATER_SPLASH, DAMAGE_HIT, FIREWORKS_BLAST1, WATER_SWIM, NOTE_HARP, NOTE_PLING, RANDOM_EXPLODE, RANDOM_POP, DIG_CLOTH, MOB_SLIME_BIG, MOB_SLIME_SMALL, MOB_ZOMBIE_DEATH, MOB_ENDERMEN_PORTAL, FIRE_FIRE, FIRE_IGNITE, MOB_GHAST_FIREBALL, MOB_WITHER_SPAWN, MOB_HORSE_SKELETON_DEATH, RANDOM_SPLASH, MOB_SILVERFISH_KILL, MOB_ZOMBIE_INFECT, MOB_WOLF_DEATH, MOB_OCELOT_DEATH, MOB_ENDERDRAGON_GROWL, MOB_HORSE_SKELETON_HIT, RANDOM_LEVELUP, MOB_SPIDER_SAY, MOB_ZOMBIE_SAY, WITCHERY_RANDOM_THEYCOME, MOB_ENDERDRAGON_HIT, WITCHERY_MOB_BABA_DEATH, WITCHERY_MOB_BABA_LIVING, WITCHERY_RANDOM_CLICK, WITCHERY_RANDOM_WINDUP, WITCHERY_RANDOM_LOVED, MOB_ENDERMAN_IDLE, MOB_WITHER_DEATH, RANDOM_BREATH, WITCHERY_MOB_SPECTRE_SPECTRE_HIT, WITCHERY_MOB_SPECTRE_SPECTRE_SAY, MOB_BLAZE_DEATH, WITCHERY_MOB_IMP_LAUGH, MOB_GHAST_DEATH, MOB_CREEPER_DEATH, WITCHERY_RANDOM_CHALK, WITCHERY_MOB_WOLFMAN_HOWL, WITCHERY_MOB_WOLFMAN_EAT, WITCHERY_MOB_WOLFMAN_LORD, WITCHERY_RANDOM_HORN, WITCHERY_RANDOM_MANTRAP, WITCHERY_MOB_WOLFMAN_TALK, WITCHERY_RANDOM_HYPNOSIS, WITCHERY_RANDOM_DRINK, WITCHERY_RANDOM_POOF, WITCHERY_MOB_LILITH_TALK, WITCHERY_RANDOM_SWORD_DRAW, WITCHERY_RANDOM_SWORD_SHEATHE, WITCHERY_MOB_REFLECTION_SPEECH};


   private SoundEffect(String var1, int var2, String sound) {
      this.sound = sound;
   }

   public String toString() {
      return this.sound;
   }

   public void playAtPlayer(World world, EntityPlayer player) {
      this.playAtPlayer(world, player, 0.5F);
   }

   public void playAtPlayer(World world, EntityPlayer player, float volume) {
      if(!world.isRemote) {
         world.playSoundAtEntity(player, this.sound, volume, 0.4F / ((float)world.rand.nextDouble() * 0.4F + 0.8F));
      }

   }

   public void playAtPlayer(World world, EntityPlayer player, float volume, float pitch) {
      if(!world.isRemote) {
         world.playSoundAtEntity(player, this.sound, volume, pitch);
      }

   }

   public void playAt(EntityLiving entity) {
      this.playAt(entity, 0.5F);
   }

   public void playAt(EntityLiving entity, float volume) {
      this.playAt(entity, volume, 0.4F / ((float)entity.worldObj.rand.nextDouble() * 0.4F + 0.8F));
   }

   public void playAt(EntityLiving entity, float volume, float pitch) {
      if(!entity.worldObj.isRemote) {
         entity.worldObj.playSoundAtEntity(entity, this.sound, volume, pitch);
      }

   }

   public void playAt(TileEntity tile) {
      this.playAt(tile, 0.5F);
   }

   public void playAt(TileEntity tile, float volume) {
      this.playAt(tile.getWorldObj(), (double)tile.xCoord, (double)tile.yCoord, (double)tile.zCoord, volume);
   }

   public void playAt(World world, double x, double y, double z) {
      this.playAt(world, x, y, z, 0.5F);
   }

   public void playAt(World world, double x, double y, double z, float volume) {
      this.playAt(world, x, y, z, volume, 0.4F / ((float)world.rand.nextDouble() * 0.4F + 0.8F));
   }

   public void playAt(World world, double x, double y, double z, float volume, float pitch) {
      if(!world.isRemote) {
         world.playSoundEffect(x, y, z, this.sound, volume, pitch);
      }

   }

   public void playOnlyTo(EntityPlayer player) {
      this.playOnlyTo(player, -1.0F, -1.0F);
   }

   public void playOnlyTo(EntityPlayer player, float volume, float pitch) {
      if(this != NONE) {
         Witchery.packetPipeline.sendTo((IMessage)(new PacketSound(this, player, volume, pitch)), player);
      }

   }

}
