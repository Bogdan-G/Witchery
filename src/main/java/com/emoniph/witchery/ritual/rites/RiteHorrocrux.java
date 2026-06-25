package com.emoniph.witchery.ritual.rites;

import com.emoniph.witchery.blocks.BlockCircle;
import com.emoniph.witchery.entity.EntityWitchHunter;
import com.emoniph.witchery.infusion.Infusion;
import com.emoniph.witchery.ritual.Rite;
import com.emoniph.witchery.ritual.RitualStep;
import com.emoniph.witchery.util.ChatUtil;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import java.util.ArrayList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class RiteHorrocrux extends Rite {

    public static final String NBT_KEY = "WITCHorrocrux";

    @Override
    public void addSteps(ArrayList steps, int initialStage) {
        steps.add(new StepRiteHorrocrux(this, initialStage));
    }

    private static class StepRiteHorrocrux extends RitualStep {
        private final RiteHorrocrux rite;

        public StepRiteHorrocrux(RiteHorrocrux rite, int initialStage) {
            super(false);
            this.rite = rite;
        }

        @Override
        public RitualStep.Result process(World world, int x, int y, int z, long ticks, BlockCircle.TileEntityCircle.ActivatedRitual circleType) {
            if (ticks % 20L != 0L) {
                return RitualStep.Result.STARTING;
            }
            if (!world.isRemote) {
                EntityPlayer initiator = circleType.getInitiatingPlayer(world);
                if (initiator == null) {
                    return RitualStep.Result.ABORTED_REFUND;
                }
                NBTTagCompound nbt = Infusion.getNBT(initiator);
                if (nbt.getBoolean(NBT_KEY)) {
                    // A soul can only be split once at a time.
                    ChatUtil.sendTranslated(EnumChatFormatting.RED, initiator, "witchery.rite.horrocrux.exists", new Object[0]);
                    return RitualStep.Result.ABORTED_REFUND;
                }
                EntityWitchHunter.blackMagicPerformed(initiator);
                nbt.setBoolean(NBT_KEY, true);
                Infusion.syncPlayer(world, initiator);
                ChatUtil.sendTranslated(EnumChatFormatting.DARK_PURPLE, initiator, "witchery.rite.horrocrux.created", new Object[0]);
                ParticleEffect.MOB_SPELL.send(SoundEffect.MOB_WITHER_SPAWN, initiator, 1.0D, 2.0D, 32);
            }
            return RitualStep.Result.COMPLETED;
        }
    }
}
