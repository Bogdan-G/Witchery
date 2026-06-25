package com.emoniph.witchery.ritual.rites;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockCircle;
import com.emoniph.witchery.ritual.Rite;
import com.emoniph.witchery.ritual.RitualStep;
import com.emoniph.witchery.util.ChatUtil;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class RiteLegilimency extends Rite {
    @Override
    public void addSteps(ArrayList steps, int initialStage) {
        steps.add(new StepRiteLegilimency(this, initialStage));
    }

    private static class StepRiteLegilimency extends RitualStep {
        private final RiteLegilimency rite;

        public StepRiteLegilimency(RiteLegilimency rite, int initialStage) {
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
                boolean read = false;
                Iterator i$ = circleType.sacrificedItems.iterator();
                while (i$.hasNext()) {
                    RitualStep.SacrificedItem item = (RitualStep.SacrificedItem)i$.next();
                    if (item.itemstack.getItem() == Witchery.Items.TAGLOCK_KIT && item.itemstack.getItemDamage() == 1) {
                        EntityLivingBase target = Witchery.Items.TAGLOCK_KIT.getBoundEntity(world, (Entity)null, item.itemstack, Integer.valueOf(1));
                        if (target != null) {
                            String name = Witchery.Items.TAGLOCK_KIT.getBoundEntityDisplayName(item.itemstack, Integer.valueOf(1));
                            String dim = target.worldObj.provider.getDimensionName();
                            String tx = Integer.toString(MathHelper.floor_double(target.posX));
                            String ty = Integer.toString(MathHelper.floor_double(target.posY));
                            String tz = Integer.toString(MathHelper.floor_double(target.posZ));
                            String hp = Integer.toString((int)Math.ceil((double)target.getHealth())) + "/" + Integer.toString((int)Math.ceil((double)target.getMaxHealth()));
                            String held = "-";
                            ItemStack heldStack = target.getHeldItem();
                            if (heldStack != null) {
                                held = heldStack.getDisplayName();
                            }
                            ChatUtil.sendTranslated(EnumChatFormatting.LIGHT_PURPLE, initiator, "witchery.rite.legilimency.read", new Object[]{name, dim, tx, ty, tz, hp, held});
                            read = true;
                        }
                        break;
                    }
                }

                if (!read) {
                    return RitualStep.Result.ABORTED_REFUND;
                }
                ParticleEffect.MAGIC_CRIT.send(SoundEffect.MOB_ENDERMAN_IDLE, world, 0.5D + (double)x, (double)y + 1.0D, 0.5D + (double)z, 1.0D, 1.0D, 16);
            }
            return RitualStep.Result.COMPLETED;
        }
    }
}
