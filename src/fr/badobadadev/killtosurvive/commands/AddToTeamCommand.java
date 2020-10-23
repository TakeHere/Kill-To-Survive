package fr.badobadadev.killtosurvive.commands;

import java.util.ArrayList;
import java.util.List;

import fr.badobadadev.killtosurvive.players.UHCPlayer;
import fr.badobadadev.killtosurvive.teams.Team;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import fr.badobadadev.killtosurvive.PluginMain;

public class AddToTeamCommand implements TabCompleter, CommandExecutor {
	 public PluginMain main;
	 public AddToTeamCommand(PluginMain pluginMain) {
		 this.main = pluginMain;
	 }

	 List<String> argumentsEquipes = new ArrayList<>();
	 List<String> argumentsPlayers = new ArrayList<>();


	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if(!main.getGameStarted()) {
			String ErrorTeamPlayer = main.getConfig().getString("AddToTeamCommand.errorNoTeamPlayer");

            Player playerSender = (Player) sender;

            if(args.length < 2) {
                playerSender.sendMessage(main.getConfig().getString("AddToTeamCommand.errorNoArgsTeam"));
                playerSender.playSound(playerSender.getLocation(), Sound.VILLAGER_NO, 3f, 1f);
                return false;
            }

            if(Bukkit.getPlayerExact(args[1]) == null){
                playerSender.sendMessage(main.getConfig().getString("AddToTeamCommand.errorPlayerDontExist"));
                return false;
            }

            Player player = Bukkit.getPlayer(args[1]);
            UHCPlayer uhcPlayer = main.getPlayersManager().getUhcPlayer(player);
            Team team = main.getTeamsManager().getTeam(args[0]);

            if(team == null){
                playerSender.sendMessage(ErrorTeamPlayer);
                return false;
            }
            
            if(team == uhcPlayer.getTeam()){
                sender.sendMessage("§cLe joueur " + args[1] + " est déjà dans cette équipe !");
                playerSender.playSound(playerSender.getLocation(), Sound.VILLAGER_NO, 3f, 1f);
                return false;
            }

            if(uhcPlayer.getTeam() != null)
                main.getTeamsManager().leave(uhcPlayer, uhcPlayer.getTeam());
            main.getTeamsManager().join(uhcPlayer, team);
            System.out.println("Player: " + player.getName() + " was added to team: " + args[0]);
            sender.sendMessage(main.getConfig().getString("AddToTeamCommand.playerAddedSuccess"));
            playerSender.playSound(playerSender.getLocation(), Sound.NOTE_PLING, 3f, 1f);
            return false;
        }else sender.sendMessage(main.getConfig().getString("parametres.alreadyStarted"));
        return false;
    }

	@Override
	public List<String> onTabComplete(CommandSender cmd, Command arg1, String arg2, String[] args) {
			argumentsEquipes.clear();
		    main.getTeamsManager().getTeams().forEach(team -> argumentsEquipes.add(team.getName()));
		
		argumentsPlayers.clear();
		Bukkit.getOnlinePlayers().forEach(player -> argumentsPlayers.add(player.getName()));
		
		List<String> resultat = new ArrayList<>();
		if(args.length == 1){
            for(String str : argumentsEquipes){
                if(str.toLowerCase().startsWith(args[0].toLowerCase())) resultat.add(str);
                if(str.equalsIgnoreCase(cmd.toString())) resultat.add(str);
            }
		}

		if(args.length == 2){
			for(String str : argumentsPlayers){
				if(str.toLowerCase().startsWith(args[1].toLowerCase())) resultat.add(str);
			}
		}
		return resultat;
	}

	
}
