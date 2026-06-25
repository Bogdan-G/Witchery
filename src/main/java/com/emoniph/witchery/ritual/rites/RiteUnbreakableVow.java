package com.emoniph.witchery.ritual.rites;

import com.emoniph.witchery.blocks.BlockCircle;
import com.emoniph.witchery.ritual.Rite;
import com.emoniph.witchery.ritual.RitualStep;
import com.emoniph.witchery.util.ChatUtil;
import com.emoniph.witchery.util.Coord;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.emoniph.witchery.util.TimeUtil;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;
import com.emoniph.witchery.common.ExtendedPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class RiteUnbreakableVow extends Rite {
    @Override
    public void addSteps(ArrayList steps, int initialStage) {
        steps.add(new StepRiteUnbreakableVow(this, initialStage));
    }

    private static class StepRiteUnbreakableVow extends RitualStep {
        private final RiteUnbreakableVow rite;

        public StepRiteUnbreakableVow(RiteUnbreakableVow rite, int initialStage) {
            super(false);
            this.rite = rite;
        }

        @Override
        public RitualStep.Result process(World world, int x, int y, int z, long ticks, BlockCircle.TileEntityCircle.ActivatedRitual circleType) {
            if (ticks % 20L != 0L) {
                return RitualStep.Result.STARTING;
            }
            if (!world.isRemote) {
                boolean blessed = false;
                AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox((double)(x - 6), (double)(y - 2), (double)(z - 6), (double)(x + 6), (double)(y + 4), (double)(z + 6));
                List players = world.getEntitiesWithinAABB(EntityPlayer.class, bounds);
                ArrayList<EntityPlayer> validPlayers = new ArrayList<EntityPlayer>();

                Iterator i$ = players.iterator();
                while (i$.hasNext()) {
                    EntityPlayer player = (EntityPlayer)i$.next();
                    if (Coord.distance(player.posX, player.posY, player.posZ, (double)x + 0.5D, (double)y, (double)z + 0.5D) <= 6.0D) {
                        validPlayers.add(player);
                    }
                }

                if (validPlayers.size() > 1) {
                    String vowID = UUID.randomUUID().toString();
                    for (EntityPlayer p : validPlayers) {
                        ExtendedPlayer.get(p).setUnbreakableVowID(vowID);
                        ChatUtil.sendTranslated(EnumChatFormatting.LIGHT_PURPLE, p, "Your soul has been bound by the Unbreakable Vow.", new Object[0]);
                    }
                    blessed = true;
                } else if (validPlayers.size() == 1) {
                    ExtendedPlayer playerEx = ExtendedPlayer.get(validPlayers.get(0));
                    if (!playerEx.getUnbreakableVowID().isEmpty()) {
                        playerEx.setUnbreakableVowID("");
                        ChatUtil.sendTranslated(EnumChatFormatting.DARK_PURPLE, validPlayers.get(0), "Your soul has been freed from the Unbreakable Vow.", new Object[0]);
                        blessed = true;
                    }
                }

                if (!blessed) {
                    return RitualStep.Result.ABORTED_REFUND;
                }
                ParticleEffect.INSTANT_SPELL.send(SoundEffect.RANDOM_LEVELUP, world, 0.5D + (double)x, (double)y + 1.0D, 0.5D + (double)z, 2.0D, 2.0D, 24);
            }
            return RitualStep.Result.COMPLETED;
        }
    }
}
