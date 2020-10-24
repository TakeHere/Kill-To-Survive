package fr.badobadadev.killtosurvive.commands;

import fr.badobadadev.killtosurvive.players.UHCPlayer;
import fr.badobadadev.killtosurvive.teams.Team;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import fr.badobadadev.killtosurvive.PluginMain;

public class StartCommand implements CommandExecutor {

	public PluginMain main;
	public StartCommand(PluginMain pluginMain) {
		this.main = pluginMain;
	}
	public int sec = 0, task;
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] arg3) {
        if(!main.getGameStarted() && main.getTeamsManager().getTeams().size() > 1) {
            for(UHCPlayer uhcPlayer : main.getPlayersManager().getPlayersList()){
                if(!uhcPlayer.isOnTeam()){
                    Player player = (Player)sender;
                    //main.getPlayersManager().removeUhcPlayer(main.getPlayersManager().getUhcPlayer(player));
                }
            }

            task = Bukkit.getScheduler().scheduleSyncRepeatingTask(main, new BukkitRunnable() {
                @Override
                public void run() {
                    sec++;
                    switch (sec) {
                        case 1:
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                player.sendTitle("§cLancement du jeu dans:", "" + 3);
                                player.playSound(player.getLocation(), Sound.ORB_PICKUP, 10f, 1f);
                            }
                            break;
                        case 2:
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                player.sendTitle("§eLancement du jeu dans:", "" + 2);
                                player.playSound(player.getLocation(), Sound.ORB_PICKUP, 10f, 1f);
                            }
                            break;
                        case 3:
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                player.sendTitle("§2Lancement du jeu dans:", "" + 1);
                                player.playSound(player.getLocation(), Sound.ORB_PICKUP, 10f, 1f);
                            }
                            break;

                        case 4:
                            Bukkit.getServer().broadcastMessage("§6-=§4Kill §8To Survive §aSaison 4§6=-");
                            Bukkit.getServer().broadcastMessage("§6Développé par TakeHere#0001 de CodeMc Group");
                            for (UHCPlayer playerr : main.getPlayersManager().getPlayersList()) {
                                Player player = playerr.getPlayer();
                                player.setHealth(20);
                                player.setFoodLevel(20);
                        		player.getInventory().clear();
                                player.sendTitle("§7Lancement du jeu !","Teleportation...");
                                player.playSound(player.getLocation(), Sound.ORB_PICKUP, 10f, 2f);
                                player.setGameMode(GameMode.SURVIVAL);
                                if(playerr.isOnTeam()) {
                                	player.teleport(playerr.getTeam().getTeleportLocation());
                                }else {
                                	player.teleport(PluginMain.getInstance().getTeamsManager().getTeam("Verte").getTeleportLocation());
                                	player.setGameMode(GameMode.SPECTATOR);
                                }
                                Bukkit.getScheduler().cancelTask(task);
                                main.setGameStarted(true);
                            }

                            if(PluginMain.getInstance().getTeamsManager().getTeam("Rouge").getMembers().isEmpty())
                                PluginMain.getInstance().getTeamsManager().disband(PluginMain.getInstance().getTeamsManager().getTeam("Rouge"));

                            if(PluginMain.getInstance().getTeamsManager().getTeam("Bleue").getMembers().isEmpty())
                                PluginMain.getInstance().getTeamsManager().disband(PluginMain.getInstance().getTeamsManager().getTeam("Bleue"));

                            if(PluginMain.getInstance().getTeamsManager().getTeam("Verte").getMembers().isEmpty())
                                PluginMain.getInstance().getTeamsManager().disband(PluginMain.getInstance().getTeamsManager().getTeam("Verte"));

                            main.episodeTask();
                            main.timerTask();
                            main.endTask();
                            Bukkit.getScheduler().cancelTask(task);
                            break;
                    }
                }
            }, 0, 20);
        }else {
            Player player = (Player)sender;
            if(main.getTeamsManager().getTeams().size() > 1) {
                player.sendMessage("§c Il faut au moins 2 équipes pour lancer le jeu !");
            }else {
                player.sendMessage("§cLe jeu a déja commencé !");
            }
            player.playSound(player.getLocation(), Sound.VILLAGER_NO, 3f, 1f);
            return false;
        }
        return false;
    }
}
