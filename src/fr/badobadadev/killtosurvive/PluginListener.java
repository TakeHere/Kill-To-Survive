package fr.badobadadev.killtosurvive;


import fr.badobadadev.killtosurvive.gui.TeamsGui;
import fr.badobadadev.killtosurvive.players.UHCPlayer;
import fr.badobadadev.killtosurvive.utils.ItemBuilder;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.Main;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.text.Format;


public class PluginListener implements Listener {
	
	private static PluginMain main;
	public PluginListener(PluginMain pluginMain) {
		main = pluginMain;
	}

	public static void updateScoreBoard(Player player) {
		Scoreboard board = player.getScoreboard();

		board.getTeam("episode").setPrefix(ChatColor.GOLD + "épisode " + ChatColor.GRAY + main.getEpisode());
		board.getTeam("joueurs").setPrefix("§7" + Bukkit.getOnlinePlayers().size() + ChatColor.GOLD + " Joueurs");
		
        board.getTeam("equipes").setPrefix("§7" + main.getTeamsManager().getTeams().size() + " §6 équipes");

        board.getTeam("timer").setPrefix(ChatColor.GOLD + "Timer: " + ChatColor.GRAY + main.getTimerMin() + ":" + main.getTimerSecond());
        if(!main.end) {
        	for(Player playerr : Bukkit.getOnlinePlayers()) {
                main.getTeamsManager().getTeams().forEach(team -> board.getTeam(team.getName()).removePlayer(playerr));
                board.getObjective("List").setDisplayName("");
                if(main.getPlayersManager().getUhcPlayer(playerr).getTeam() != null)
            	    board.getTeam(main.getPlayersManager().getUhcPlayer(playerr).getTeam().getName()).addPlayer(playerr);
            }
        }
        player.setScoreboard(board);
	}
	
	@EventHandler
	public void onLogin(PlayerLoginEvent e) {
		if(main.getGameStarted() && !main.end) {
			e.disallow(e.getResult().KICK_OTHER, "§cLe jeu a déja commencé !");
			return;
		}
	}
	
	@EventHandler
	public static void OnJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		System.out.println(main.spawnLoc);
		player.getInventory().clear();
		ItemStack compass = new ItemBuilder(Material.COMPASS).setName("Choisissez votre équipe").toItemStack();
		player.getInventory().setItem(4, compass);
		main.getPlayersManager().newUhcPlayer(player);
		player.setFoodLevel(20);
		player.setHealth(20);
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "xp -20000L " + player.getName());
		for (PotionEffect effect : player.getActivePotionEffects())
		    player.removePotionEffect(effect.getType());
		event.setJoinMessage(ChatColor.GOLD + player.getName() + " a rejoint l'aventure !");
		if(main.end) {
			player.teleport(main.spawnLoc);
			player.setGameMode(GameMode.SPECTATOR);
		}else {
			player.teleport(main.spawnLoc);
			player.setGameMode(GameMode.ADVENTURE);
		}
		makeScoreboard(player);
		updateScoreBoard(player);
		
	}
	
	public static void makeScoreboard(Player player) {
		Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective obj = board.registerNewObjective("Kill To Survive", "dummy");
        
        //Episodes
        Team episode = board.registerNewTeam("episode");
        episode.addEntry(ChatColor.DARK_RED + "" + ChatColor.DARK_RED);
        episode.setPrefix(ChatColor.GOLD + "épisode " + ChatColor.GRAY + main.getEpisode());
        obj.getScore(ChatColor.DARK_RED + "" + ChatColor.DARK_RED).setScore(4);
        
        //Nombres joueurs
        Team joueurs = board.registerNewTeam("joueurs");
        joueurs.addEntry(ChatColor.RED + "" + ChatColor.RED);
        joueurs.setPrefix("§7" + Bukkit.getOnlinePlayers().size() + ChatColor.GOLD + " Joueurs");
        obj.getScore(ChatColor.RED + "" + ChatColor.RED).setScore(3);
        
        //Nombres Equipes
        Team equipes = board.registerNewTeam("equipes");
        equipes.addEntry(ChatColor.BLACK + "" + ChatColor.BLACK);
        equipes.setPrefix("§7" + main.getTeamsManager().getTeams().size() + " §6 équipes");
        obj.getScore(ChatColor.BLACK + "" + ChatColor.BLACK).setScore(2);
        
        //ESPACE
        Team Espace = board.registerNewTeam("espace");
        Espace.addEntry(ChatColor.BLACK + "" + ChatColor.RED);
        Espace.setPrefix("");
        obj.getScore(ChatColor.BLACK + "" + ChatColor.RED).setScore(1);
        
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        player.setScoreboard(board);
        
        //Timer Episode
        Team Timer = board.registerNewTeam("timer");
        Timer.addEntry(ChatColor.YELLOW + "" + ChatColor.RED);
        Timer.setPrefix(ChatColor.GOLD + "Timer: " + ChatColor.GRAY + main.getTimerMin() + ":" + main.getTimerSecond());
        obj.getScore(ChatColor.YELLOW + "" + ChatColor.RED).setScore(0);
        
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        player.setScoreboard(board);
        makeListScoreBoard(player);
	}

	private static void makeListScoreBoard(Player player) {
		Scoreboard sb = player.getScoreboard();
		Objective obj = sb.registerNewObjective("List", "health");
		obj.setDisplaySlot(DisplaySlot.PLAYER_LIST);
		main.getTeamsManager().getPlayersList().forEach(team -> sb.registerNewTeam(team.getName()).setPrefix(team.getColor().toString()));
		player.setScoreboard(sb);
	}
	
	
	@EventHandler
	public static void onHeld(PlayerItemHeldEvent event) {
		Player player = event.getPlayer();
		if (player.getItemInHand().getEnchantments().size() > 0) {
			for (int i = 0; i < player.getItemInHand().getEnchantments().size(); i++) {
				if (player.getItemInHand().containsEnchantment(Enchantment.FIRE_ASPECT)) {
					player.giveExpLevels(10);
					player.playSound(player.getLocation(), Sound.LEVEL_UP, 3f, 2f);
					player.getItemInHand().removeEnchantment(Enchantment.FIRE_ASPECT);
					player.sendMessage("§cVous ne pouvez pas enchanter avec FireAspect ! (vous avez été remboursé(e))");
				}
			}
		}
	}

	@EventHandler
	public static void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (event.getItem() != null) {
			
			if (event.getItem().getEnchantments().size() > 0) {
				for (int i = 0; i < event.getItem().getEnchantments().size(); i++) {
					if (event.getItem().containsEnchantment(Enchantment.FIRE_ASPECT)) {
						player.playSound(player.getLocation(), Sound.VILLAGER_NO, 3f, 1f);
						player.giveExpLevels(10);
						player.playSound(player.getLocation(), Sound.LEVEL_UP, 3f, 2f);
						event.getItem().removeEnchantment(Enchantment.FIRE_ASPECT);
						player.sendMessage("§cVous ne pouvez pas enchanter avec FireAspect ! (vous avez été remboursé(e))");
					}
				}
			}
		}
	}
	
	@EventHandler
	public static void onCompass(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if(event.getItem() != null) {
			if(event.getItem().getType() == Material.AIR) return;
	        if(event.getItem().getType() == Material.COMPASS){
	        	if(event.getItem().getItemMeta().getDisplayName() == null) return;
	        	if(event.getItem().getItemMeta().getDisplayName().equals("Choisissez votre équipe")){
	                main.getGuiManager().open(player, TeamsGui.class);
	            }
	        }     
		}
	}
	
	@EventHandler
    public static void onPlayerRegainHealth(EntityRegainHealthEvent event) {
        if(event.getRegainReason() == RegainReason.SATIATED || event.getRegainReason() == RegainReason.REGEN) event.setCancelled(true);
    }

    @EventHandler
	public static void onSplashPotion(PotionSplashEvent event) {
    		event.setCancelled(true);
    }
    
    @EventHandler
    public static void onBrewPotion(BrewEvent event) {
    	Bukkit.getWorld("world").playSound(event.getBlock().getLocation(), Sound.BAT_DEATH, 10f, 0.5f);
    	event.setCancelled(true);
	}
    
    @EventHandler
    public static void onConsume(PlayerItemConsumeEvent event) {
		if(event.getItem().getType() == Material.POTION) {
			event.setCancelled(true);
			event.getPlayer().sendMessage("§cLes potions ont été désactivées !");
			event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.VILLAGER_NO, 3f, 1f);
		}
	}
    
    @SuppressWarnings("deprecation")
	@EventHandler
   public static void onDeath(PlayerDeathEvent event) {
	   if (event.getEntity() != null) {
	       Player player = event.getEntity();
	       Bukkit.broadcastMessage(ChatColor.GOLD + player.getName() + " est mort !");
	       Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ban " + player.getName() + " §2 Vous êtes mort !");
	   }
   }
    
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
    	if(main.getEpisode() < 2) {
    		if(e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
    			Player player = (Player) e.getDamager();
    			player.sendMessage("§cLe pvp est désactivé à l'épisode 1 !");
    			e.setCancelled(true);
    		}
    	}else if(e.getDamager() instanceof Player && e.getEntity() instanceof Player){
    		Player player = (Player) e.getDamager();
    		Player playerEntity = (Player) e.getEntity();

            UHCPlayer uhcPlayerDamager = main.getPlayersManager().getUhcPlayer(player);
            UHCPlayer uhcPlayerDamaged = main.getPlayersManager().getUhcPlayer(playerEntity);

            if(!uhcPlayerDamager.isOnTeam()) e.setCancelled(true);
            if(!uhcPlayerDamaged.isOnTeam()) e.setCancelled(true);

            if(uhcPlayerDamaged.getTeam() == uhcPlayerDamager.getTeam()) e.setCancelled(true);
    	}
    }
    
    @EventHandler
    public void onDisconnect(PlayerQuitEvent e) {
    	e.setQuitMessage("");
    	Player player = e.getPlayer();
    	if(main.getGameStarted() && !main.end){
    		if(main.getPlayersManager().getUhcPlayer(player).getTeam() != null){
        		if((main.getPlayersManager().getUhcPlayer(player).getTeam().getMembers().size() - 1) == 0) {
                    Bukkit.broadcastMessage(ChatColor.GOLD + "La team " + main.getPlayersManager().getUhcPlayer(player).getTeam().getName() + " vient d'être dissous !");
                    main.getTeamsManager().disband(main.getPlayersManager().getUhcPlayer(player).getTeam());
                }else {
                    Bukkit.broadcastMessage(ChatColor.GOLD + " Il reste "+(main.getPlayersManager().getUhcPlayer(player).getTeam().getMembers().size() -1) +" joueurs dans la team "+ main.getPlayersManager().getUhcPlayer(player).getTeam().getColor() + main.getPlayersManager().getUhcPlayer(player).getTeam().getName() + ChatColor.GOLD +" !");
                }
            }
    		main.getPlayersManager().quit(main.getPlayersManager().getUhcPlayer(player));
        	Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ban " + player.getName() + " §2Vous avez quitté la partie !");
    	}
    	Bukkit.broadcastMessage(ChatColor.GOLD + player.getName() + " a quitté la partie !");
    	if(main.getPlayersManager().doesPlayerExist(player)) {
    		main.getPlayersManager().quit(main.getPlayersManager().getUhcPlayer(player));
    	}
    	
    }

    @EventHandler
    public void onChat(PlayerChatEvent e) {
    	Player player = e.getPlayer();
    	UHCPlayer uhcPlayer = main.getPlayersManager().getUhcPlayer(player);

    	if(uhcPlayer.isOnTeam())
    	    e.setFormat(uhcPlayer.getTeam().getColor() + "<" + uhcPlayer.getName() + ">§f " + e.getMessage());
    }
    
    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
    	if (e.getItemDrop().getItemStack().getType() == Material.COMPASS && e.getItemDrop().getItemStack().getItemMeta().getDisplayName().equalsIgnoreCase("Choisissez votre équipe")) {
			e.setCancelled(true);
		}
    }
}
