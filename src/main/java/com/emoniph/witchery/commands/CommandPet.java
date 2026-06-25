package com.emoniph.witchery.commands;

import com.emoniph.witchery.util.TameableUtil;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

import java.util.List;

public class CommandPet extends CommandBase {

    @Override
    public String getCommandName() {
        return "pet";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/pet <follow|stay|attack|release|inventory>";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return sender instanceof EntityPlayer;
    }

    private EntityLivingBase getLookedAtEntity(EntityPlayer player, double range) {
        Vec3 vec3 = Vec3.createVectorHelper(player.posX, player.posY, player.posZ);
        vec3.yCoord += player.getEyeHeight();
        Vec3 vec31 = player.getLook(1.0F);
        Vec3 vec32 = vec3.addVector(vec31.xCoord * range, vec31.yCoord * range, vec31.zCoord * range);

        EntityLivingBase pointedEntity = null;
        List list = player.worldObj.getEntitiesWithinAABBExcludingEntity(player, player.boundingBox.addCoord(vec31.xCoord * range, vec31.yCoord * range, vec31.zCoord * range).expand(1.0D, 1.0D, 1.0D));
        double d2 = range;

        for (int i = 0; i < list.size(); ++i) {
            net.minecraft.entity.Entity entity = (net.minecraft.entity.Entity)list.get(i);
            if (entity.canBeCollidedWith() && entity instanceof EntityLivingBase) {
                float f1 = entity.getCollisionBorderSize();
                AxisAlignedBB axisalignedbb = entity.boundingBox.expand((double)f1, (double)f1, (double)f1);
                MovingObjectPosition mop = axisalignedbb.calculateIntercept(vec3, vec32);
                if (axisalignedbb.isVecInside(vec3)) {
                    if (d2 >= 0.0D) {
                        pointedEntity = (EntityLivingBase)entity;
                        d2 = 0.0D;
                    }
                } else if (mop != null) {
                    double d3 = vec3.distanceTo(mop.hitVec);
                    if (d3 < d2 || d2 == 0.0D) {
                        pointedEntity = (EntityLivingBase)entity;
                        d2 = d3;
                    }
                }
            }
        }
        return pointedEntity;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (!(sender instanceof EntityPlayer)) return;
        EntityPlayer player = (EntityPlayer) sender;

        if (args.length == 0) {
            player.addChatMessage(new ChatComponentText("Usage: " + getCommandUsage(sender)));
            return;
        }

        String subCommand = args[0].toLowerCase();
        EntityLivingBase lookedAtEntity = getLookedAtEntity(player, 30.0D);

        if (subCommand.equals("attack")) {
            if (lookedAtEntity == null) {
                player.addChatMessage(new ChatComponentText("You must look at the enemy you want your pets to attack."));
                return;
            }

            AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(player.posX - 30.0D, player.posY - 30.0D, player.posZ - 30.0D, player.posX + 30.0D, player.posY + 30.0D, player.posZ + 30.0D);
            List<EntityTameable> pets = player.worldObj.getEntitiesWithinAABB(EntityTameable.class, bb);
            
            int attackCount = 0;
            for (EntityTameable pet : pets) {
                if (pet.isTamed() && TameableUtil.isOwner(pet, player) && !pet.isSitting()) {
                    pet.setAttackTarget(lookedAtEntity);
                    attackCount++;
                }
            }
            
            if (attackCount > 0) {
                player.addChatMessage(new ChatComponentText(attackCount + " pets have locked onto the target."));
            } else {
                player.addChatMessage(new ChatComponentText("There are no tame pets nearby that can attack."));
            }
            return;
        }

        // For other commands, we MUST be looking at a tamed pet
        if (lookedAtEntity == null || !(lookedAtEntity instanceof EntityTameable)) {
            player.addChatMessage(new ChatComponentText("You must look directly at one of your pets to give this order."));
            return;
        }

        EntityTameable pet = (EntityTameable) lookedAtEntity;

        if (!pet.isTamed() || !TameableUtil.isOwner(pet, player)) {
            player.addChatMessage(new ChatComponentText("This pet is not yours. It will not obey your orders."));
            return;
        }

        if (subCommand.equals("stay")) {
            pet.setSitting(true);
            pet.setAttackTarget(null);
            pet.getNavigator().clearPathEntity();
            player.addChatMessage(new ChatComponentText("The pet will wait here."));
        } else if (subCommand.equals("follow")) {
            pet.setSitting(false);
            player.addChatMessage(new ChatComponentText("The pet will follow and protect you."));
        } else if (subCommand.equals("release")) {
            pet.setTamed(false);
            pet.func_152115_b(""); // Clear owner ID string
            pet.setSitting(false);
            pet.setAttackTarget(null);
            pet.getNavigator().clearPathEntity();
            player.addChatMessage(new ChatComponentText("You have released the pet. It will no longer follow you."));
        } else if (subCommand.equals("inventory")) {
            player.displayGUIChest(new com.emoniph.witchery.infusion.infusions.symbols.InventoryMobEquipment(pet));
        } else {
            player.addChatMessage(new ChatComponentText("Unknown command. Valid commands are: follow, stay, attack, release, inventory."));
        }
    }
}
