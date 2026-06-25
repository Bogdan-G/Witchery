package com.emoniph.witchery.common;

import com.emoniph.witchery.Witchery;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

import java.util.List;

public class CommandWitcheryLevel extends CommandBase {

    @Override
    public String getCommandName() {
        return "witcherylevel";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/witcherylevel <player> <vampire|werewolf|spirit> <0-10>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length < 3) {
            throw new WrongUsageException(getCommandUsage(sender));
        }

        EntityPlayerMP player = getPlayer(sender, args[0]);
        String type = args[1].toLowerCase();
        int level = parseIntBounded(sender, args[2], 0, 10);

        ExtendedPlayer playerEx = ExtendedPlayer.get(player);
        if (playerEx == null) {
            throw new CommandException("Player does not have Witchery data");
        }

        if (type.equals("vampire")) {
            playerEx.setVampireLevel(level);
        } else if (type.equals("werewolf")) {
            playerEx.setWerewolfLevel(level);
        } else if (type.equals("spirit")) {
            playerEx.setSpiritLevel(level);
        } else {
            throw new WrongUsageException(getCommandUsage(sender));
        }

        sender.addChatMessage(new ChatComponentText("Successfully set " + type + " level to " + level + " for " + player.getCommandSenderName()));
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
        } else if (args.length == 2) {
            return getListOfStringsMatchingLastWord(args, new String[]{"vampire", "werewolf", "spirit"});
        }
        return null;
    }
}
