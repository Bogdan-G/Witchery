package com.emoniph.witchery.ritual;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.ritual.Circle;
import com.emoniph.witchery.ritual.Rite;
import com.emoniph.witchery.ritual.RitualTraits;
import com.emoniph.witchery.ritual.Sacrifice;
import com.emoniph.witchery.util.ChatUtil;
import com.emoniph.witchery.util.Const;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class RiteRegistry {

   private static final RiteRegistry INSTANCE = new RiteRegistry();
   final ArrayList rituals = new ArrayList();


   public static RiteRegistry instance() {
      return INSTANCE;
   }

   public final List getRituals() {
      return this.rituals;
   }

   public static RiteRegistry.Ritual addRecipe(int ritualID, int bookIndex, Rite rite, Sacrifice initialSacrifice, EnumSet traits, Circle ... circles) {
      RiteRegistry.Ritual ritual = new RiteRegistry.Ritual((byte)ritualID, bookIndex, rite, initialSacrifice, traits, circles);
      instance().rituals.add(ritual);
      return ritual;
   }

   public RiteRegistry.Ritual getRitual(byte ritualID) {
      return (RiteRegistry.Ritual)this.rituals.get(ritualID - 1);
   }

   public List getSortedRituals() {
      ArrayList sortedRituals = new ArrayList();
      sortedRituals.addAll(this.rituals);
      Collections.sort(sortedRituals, new RiteRegistry.IndexComparitor());
      return sortedRituals;
   }

   public static void RiteError(String translationID, String username, World world) {
      if(world != null && !world.isRemote && username != null) {
         Iterator i$ = world.playerEntities.iterator();

         while(i$.hasNext()) {
            Object obj = i$.next();
            if(obj instanceof EntityPlayer) {
               EntityPlayer worldPlayer = (EntityPlayer)obj;
               if(worldPlayer.getCommandSenderName().equals(username)) {
                  RiteError(translationID, worldPlayer, world);
                  return;
               }
            }
         }
      }

   }

   public static void RiteError(String translationID, EntityPlayer player, World world) {
      if(world != null && !world.isRemote && player != null) {
         ChatUtil.sendTranslated(EnumChatFormatting.RED, player, translationID, new Object[0]);
      }

   }


   public static class Ritual {

      private String unlocalizedName;
      public final Rite rite;
      final Sacrifice initialSacrifice;
      final EnumSet traits;
      final Circle[] circles;
      final byte ritualID;
      final int bookIndex;
      boolean visibleInBook;
      private boolean consumeAttunedStoneCharged = false;
      private boolean consumeNecroStone = false;


      public String getUnlocalizedName() {
         return this.unlocalizedName;
      }

      public RiteRegistry.Ritual setUnlocalizedName(String unlocalizedName) {
         this.unlocalizedName = unlocalizedName;
         return this;
      }

      public String getLocalizedName() {
         return this.unlocalizedName != null?Witchery.resource(this.getUnlocalizedName()):this.toString();
      }

      Ritual(byte ritualID, int bookIndex, Rite rite, Sacrifice initialSacrifice, EnumSet traits, Circle[] circles) {
         this.ritualID = ritualID;
         this.bookIndex = bookIndex;
         this.rite = rite;
         this.initialSacrifice = initialSacrifice;
         this.traits = traits;
         this.circles = circles;
         this.visibleInBook = true;
      }

      public String getDescription() {
         StringBuffer sb = new StringBuffer();
         sb.append("§n");
         sb.append(this.getLocalizedName());
         sb.append("§r");
         sb.append(Const.BOOK_NEWLINE);
         sb.append(Const.BOOK_NEWLINE);
         this.initialSacrifice.addDescription(sb);
         return sb.toString();
      }

      public boolean isMatch(World world, int posX, int posY, int posZ, Circle[] nearbyCircles, ArrayList entities, ArrayList grassperStacks, boolean isDaytime, boolean isRaining, boolean isThundering) {
         if((!this.traits.contains(RitualTraits.ONLY_AT_NIGHT) || !isDaytime) && (!this.traits.contains(RitualTraits.ONLY_AT_DAY) || isDaytime) && (!this.traits.contains(RitualTraits.ONLY_IN_RAIN) || isRaining) && (!this.traits.contains(RitualTraits.ONLY_IN_STROM) || isThundering) && (!this.traits.contains(RitualTraits.ONLY_OVERWORLD) || world.provider.dimensionId == 0)) {
            if(this.circles.length > 0) {
               ArrayList circlesToFind = new ArrayList(Arrays.asList(this.circles));
               Circle[] arr$ = nearbyCircles;
               int len$ = nearbyCircles.length;

               for(int i$ = 0; i$ < len$; ++i$) {
                  Circle circle = arr$[i$];
                  circle.removeIfRequired(circlesToFind);
                  if(circlesToFind.size() == 0) {
                     break;
                  }
               }

               if(circlesToFind.size() > 0) {
                  return false;
               }
            }

            return this.initialSacrifice.isMatch(world, posX, posY, posZ, this.getMaxDistance(), entities, grassperStacks);
         } else {
            return false;
         }
      }

      public void addSteps(ArrayList steps, AxisAlignedBB bounds) {
         int maxDistance = this.getMaxDistance();
         this.initialSacrifice.addSteps(steps, bounds, maxDistance);
         this.addRiteSteps(steps, 0);
      }

      private int getMaxDistance() {
         int maxDistance = this.circles.length > 0?0:4;
         Circle[] arr$ = this.circles;
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Circle circle = arr$[i$];
            maxDistance = Math.max(circle.getRadius(), maxDistance);
         }

         return maxDistance;
      }

      public byte getRitualID() {
         return this.ritualID;
      }

      public void addRiteSteps(ArrayList ritualSteps, int stage) {
         this.rite.addSteps(ritualSteps, stage);
      }

      public byte[] getCircles() {
         byte[] result = new byte[this.circles.length];
         int index = 0;
         Circle[] arr$ = this.circles;
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Circle circle = arr$[i$];
            result[index++] = (byte)circle.getTextureIndex();
         }

         return result;
      }

      public RiteRegistry.Ritual setShowInBook(boolean show) {
         this.visibleInBook = show;
         return this;
      }

      public boolean showInBook() {
         return this.visibleInBook;
      }

      public boolean isConsumeAttunedStoneCharged() {
         return this.consumeAttunedStoneCharged;
      }

      public void setConsumeAttunedStoneCharged() {
         this.consumeAttunedStoneCharged = true;
      }

      public boolean isConsumeNecroStone() {
         return this.consumeNecroStone;
      }

      public void setConsumeNecroStone() {
         this.consumeNecroStone = true;
      }
   }

   public static class IndexComparitor implements Comparator<RiteRegistry.Ritual> {

      @Override
      public int compare(RiteRegistry.Ritual o1, RiteRegistry.Ritual o2) {
         return Integer.valueOf(o1.bookIndex).compareTo(Integer.valueOf(o2.bookIndex));
      }
   }
}
