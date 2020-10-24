package fr.badobadadev.killtosurvive.commands;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.badobadadev.killtosurvive.PluginMain;

public class ClearTeamsCommand implements CommandExecutor {
	public PluginMain main;
	public ClearTeamsCommand(PluginMain pluginMain) {
		this.main = pluginMain;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] arg3) {
        Player player = (Player) sender;
        if (!main.getGameStarted()) {
            player.playSound(player.getLocation(), Sound.NOTE_PLING, 5f, 1.5f);
            main.getTeamsManager().getTeams().forEach(team -> team.getMembers().clear());
			sender.sendMessage("Les équipes ont bien été suprimés");
        }else {
            player.sendMessage("§cLe jeu a déja commencé !");
			player.playSound(player.getLocation(), Sound.VILLAGER_NO, 3f, 1f);
        }
        return false;
    }

}
