package com.emoniph.witchery.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

import java.util.HashMap;
import java.util.Map;

public class CommandCrucio extends CommandBase {

    // Mapa para almacenar cuántos corazones de daño configuró cada jugador para su próximo hechizo Crucio.
    public static final Map<EntityPlayer, Integer> CRUCIO_POWER = new HashMap<EntityPlayer, Integer>();

    @Override
    public String getCommandName() {
        return "crucio";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/crucio <corazones>";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return sender instanceof EntityPlayer;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (!(sender instanceof EntityPlayer)) return;
        EntityPlayer player = (EntityPlayer) sender;

        if (args.length == 0) {
            player.addChatMessage(new ChatComponentText("Uso: " + getCommandUsage(sender)));
            return;
        }

        try {
            int corazones = Integer.parseInt(args[0]);
            if (corazones < 1) corazones = 1;
            
            // Límite de corazones solicitado: 10 (20 de vida total)
            if (corazones > 10) {
                corazones = 10;
                player.addChatMessage(new ChatComponentText("No puedes torturar por más de 10 corazones. ¡Para matar instantáneamente debes usar Avada Kedavra!"));
            }

            CRUCIO_POWER.put(player, corazones);
            player.addChatMessage(new ChatComponentText("Has configurado tu próxima Maldición Cruciatus a " + corazones + " corazones de agonía progresiva."));

        } catch (NumberFormatException e) {
            player.addChatMessage(new ChatComponentText("Por favor, introduce un número válido de corazones."));
        }
    }
}
