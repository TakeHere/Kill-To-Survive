package fr.badobadadev.killtosurvive.commands;

import java.sql.PseudoColumnUsage;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffCommand implements CommandExecutor {
	StringBuilder sb = new StringBuilder();
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if(sender instanceof Player) {
			Player psender = (Player) sender;
			if(args.length != 0) {
				String msgString = "";
				for (String string : args) {
					msgString += " " + string;
				}
				for (Player oplayer : Bukkit.getOnlinePlayers()) {
					if(oplayer.isOp()) {
						oplayer.sendMessage("§6Message de " + psender.getName() + ":" + msgString);
					}
				}
			}else {
				psender.sendMessage("§4Erreur de syntaxe: /staff <Message>");
			}
		}else {
			System.out.println("Vous ne pouvez pas executer cette commande par la console");
		}
		return false;
	}

}
