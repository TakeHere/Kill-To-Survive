package fr.badobadadev.killtosurvive.commands;

import java.util.ArrayList;
import java.util.List;

import fr.badobadadev.killtosurvive.players.UHCPlayer;
import fr.badobadadev.killtosurvive.teams.Team;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import fr.badobadadev.killtosurvive.PluginMain;

public class VisualiseTeamCommand implements CommandExecutor, TabCompleter {
	public PluginMain main;
	public VisualiseTeamCommand(PluginMain pluginMain) {
	this.main = pluginMain;
	}
	
	List<String> argumentsEquipes = new ArrayList<>();
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		Player playerSender = (Player) sender;

		if(args.length == 1) {
		    if(main.getTeamsManager().getTeam(args[0]) == null){
                sender.sendMessage("§2Cette team n'existe pas !");
                playerSender.playSound(playerSender.getLocation(), Sound.NOTE_PLING, 3f, 1f);
                return false;
            }else{
                Team team = main.getTeamsManager().getTeam(args[0]);

                if(team.getMembers().isEmpty()) sender.sendMessage("§2La team "+team.getName()+" est vide !");
                else {
                    for(UHCPlayer uhcPlayer : team.getMembers()){
                        sender.sendMessage("§2 La team "+team.getName()+" contient: " + uhcPlayer.getName());
                    }
                    playerSender.playSound(playerSender.getLocation(), Sound.NOTE_PLING, 3f, 1f);
                }
            }
		}else {
			sender.sendMessage(main.getConfig().getString("VisualiseTeamCommand.WrongSyntax"));
			playerSender.playSound(playerSender.getLocation(), Sound.VILLAGER_NO, 3f, 1f);
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String arg2, String[] args) {
        if(argumentsEquipes.isEmpty())
            main.getTeamsManager().getTeams().forEach(team -> argumentsEquipes.add(team.getName()));

		List<String> resultat = new ArrayList<>();
		if(args.length == 1){
            for(String str : argumentsEquipes){
                if(str.toLowerCase().startsWith(args[0].toLowerCase())) resultat.add(str);
                if(str.equalsIgnoreCase(cmd.toString())) resultat.add(str);
            }
            
		}
		return resultat;
	}

}
