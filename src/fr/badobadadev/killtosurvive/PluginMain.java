package fr.badobadadev.killtosurvive;

import fr.badobadadev.killtosurvive.players.PlayersManager;
import fr.badobadadev.killtosurvive.teams.Team;
import fr.badobadadev.killtosurvive.teams.TeamsManager;
import fr.badobadadev.killtosurvive.utils.GuiBuilder;
import fr.badobadadev.killtosurvive.utils.GuiManager;
import fr.badobadadev.killtosurvive.utils.ItemBuilder;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import fr.badobadadev.killtosurvive.commands.AddToTeamCommand;
import fr.badobadadev.killtosurvive.commands.StartCommand;
import fr.badobadadev.killtosurvive.commands.VisualiseTeamCommand;
import fr.badobadadev.killtosurvive.gui.TeamsGui;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PluginMain extends JavaPlugin{
	int updateTask, episodeTask, episode = 1, episodeLenght = getConfig().getInt("parametres.dureeEpisode"),
            timerTask, timerSecond = 00, timerMin = 0, endTaskk = 0, endT = 0;

	private static PluginMain instance;
    private PlayersManager playersManager;
    private TeamsManager teamsManager;
    private Map<Class<? extends GuiBuilder>, GuiBuilder> registeredMenus;
    private GuiManager guiManager;
    
    
	public String[] spawnStr = getConfig().getString("Coordinates.spawn").split(",");
	public String[] verteStr = getConfig().getString("Coordinates.verte").split(",");
	public String[] rougeStr = getConfig().getString("Coordinates.rouge").split(",");
	public String[] orangeStr = getConfig().getString("Coordinates.orange").split(",");
	public String[] bleueStr = getConfig().getString("Coordinates.bleue").split(",");
	public String[] coffre1 = getConfig().getString("Coordinates.coffre1").split(",");
	public String[] coffre2 = getConfig().getString("Coordinates.coffre2").split(",");
	public String[] coffre3 = getConfig().getString("Coordinates.coffre3").split(",");
	
	static Boolean gameStarted = false;

	Boolean end = false;
	
	//LISTE
	
	
	//PAS DE PVP P1 CHECK !
	
	
	
	//COFFRE A EPISODE CHECK !
	//TP ON START
	//mettre les coo de doxren et rempllir les coffres
	//SECU DECO CORUPTION
	//SECURI2EE START SI START = TRUE ALORS PAS POSSIBLE ADD ET CLEAr et start
	//TP au spawn quand on rejoint
	//TAB EQUIPECOULEUR ET AFFICAGE VIE
	//FAIRE PROTECT EELEVER TEAM LORSQUE DISCONECT
	//REGLER PROLEME TABCOMPLETER PSEUDO ADDOTEAM
	//faire secu si start mais pas touts les joueurs dans team
	//FAIRE ANTI DAMGage �quipe (if verte team cotai playerdameged etc
	//PROEC MI  2 tEAMs
	//FAIRE UNE FIN Qand tout le monde est mort (if 1 team left)
	//METRE MES NOMS ET FAIRE DE LA DECO
	//SEUP NGROK
	//EERNAL DAY  
	//Color chat 
	//HEAL AU LACEME E AU RL //A TEST
	//NOM EQUIPE QUI GAGNE
	//PAS BAN LES GENS FIN DE GAME
	//COFFRES
	//CANCEL ASKS fi de game
	//ZOLI TIMER
	//TP MONDE world SI GAME FINI
	//REGLER PEIN DE PROGLZEME END
	//tests
	
	//TODO DIS ALLOW NETHER PAS NETHER
	

	@Override
	public void onEnable() {
		getServer().createWorld(new WorldCreator("world"));
		Bukkit.getOnlinePlayers().forEach(player -> player.kickPlayer("Redemarrage du serveur !"));
		instance = this;
	    this.playersManager = new PlayersManager();
	    this.teamsManager = new TeamsManager();
	    registeredMenus = new HashMap<>();
	    guiManager = new GuiManager();
	    
	    Location loc1 = new Location(Bukkit.getWorld("world"), Integer.parseInt(coffre1[0]), Integer.parseInt(coffre1[1]), Integer.parseInt(coffre1[2]));
	    loc1.getBlock().setType(Material.AIR);
	    Location loc2 = new Location(Bukkit.getWorld("world"), Integer.parseInt(coffre2[0]), Integer.parseInt(coffre2[1]), Integer.parseInt(coffre2[2]));
	    loc2.getBlock().setType(Material.AIR);
	    Location loc3 = new Location(Bukkit.getWorld("world"), Integer.parseInt(coffre3[0]), Integer.parseInt(coffre3[1]), Integer.parseInt(coffre3[2]));
	    loc3.getBlock().setType(Material.AIR);
	    
	    getTeamsManager().create("Verte", 1, ChatColor.DARK_GREEN, new Location(Bukkit.getWorld("world"), Integer.parseInt(verteStr[0]), Integer.parseInt(verteStr[1]), Integer.parseInt(verteStr[2])));
        getTeamsManager().create("Rouge", 2, ChatColor.DARK_RED, new Location(Bukkit.getWorld("world"), Integer.parseInt(rougeStr[0]), Integer.parseInt(rougeStr[1]), Integer.parseInt(rougeStr[2])));
        getTeamsManager().create("Bleue", 4, ChatColor.DARK_AQUA, new Location(Bukkit.getWorld("world"), Integer.parseInt(bleueStr[0]), Integer.parseInt(bleueStr[1]), Integer.parseInt(bleueStr[2])));

		for (Player player : Bukkit.getOnlinePlayers()) {
			player.setGameMode(GameMode.ADVENTURE);
			player.setHealth(20);
			player.setFoodLevel(20);
			player.getInventory().clear();
			Location loc = new Location(Bukkit.getWorld("Lobby"), Integer.parseInt(spawnStr[0]), Integer.parseInt(spawnStr[1]), Integer.parseInt(spawnStr[2]));
			player.teleport(loc);
		}

		for(OfflinePlayer oplayer : Bukkit.getBannedPlayers()) {
			oplayer.setBanned(false);
		}

		
		getServer().getPluginManager().registerEvents(new PluginListener(this), this);
		for(int i=0;i<200;i++) Bukkit.getServer().broadcastMessage("");
		Bukkit.getServer().broadcastMessage("�6-=�4Kill �8To Survive �aSaison 4�6=-");
		Bukkit.getServer().broadcastMessage("�6D�velopp� par TakeHere#0001");
		
		getCommand("start").setExecutor(new StartCommand(this));
		getCommand("addToTeam").setExecutor(new AddToTeamCommand(this));
		getCommand("addToTeam").setTabCompleter(new AddToTeamCommand(this));
		getCommand("visualiseTeam").setExecutor(new VisualiseTeamCommand(this));
		getCommand("visualiseTeam").setTabCompleter(new VisualiseTeamCommand(this));
		Bukkit.getPluginManager().registerEvents(new GuiManager(), this);
		saveDefaultConfig();
		
		Bukkit.getWorld("world").setGameRuleValue("doDaylightCycle", String.valueOf(false));
		Bukkit.getWorld("Lobby").setGameRuleValue("doDaylightCycle", String.valueOf(false));
		
		if (getConfig().getBoolean("parametres.clearItemsOnGround")) {
			Bukkit.getWorld("world").getEntities().stream().filter(Item.class::isInstance).forEach(Entity::remove);
			Bukkit.getWorld("Lobby").getEntities().stream().filter(Item.class::isInstance).forEach(Entity::remove);
		}
		initTabList();
		guiManager.addMenu(new TeamsGui(this));
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			@Override
			public void run() {
				tasks();
			}
		},20);
	}
	@SuppressWarnings("deprecation")
	public void tasks() {
		updateTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new BukkitRunnable() {
			@Override
			public void run() {
				for (Player player: Bukkit.getOnlinePlayers()) {
					PluginListener.updateScoreBoard(player);
				}
				
			}
		}, 0, 2);
	}
	
	public void episodeTask() {
		episodeTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            episode++;
            for(Player player : Bukkit.getOnlinePlayers()) {
                player.playSound(player.getLocation(), Sound.NOTE_PLING, 5f, 1.5f);
            }
            switch (episode) {
            case 2:
                Location loc1 = new Location(Bukkit.getWorld("world"), Integer.parseInt(coffre1[0]), Integer.parseInt(coffre1[1]), Integer.parseInt(coffre1[2]));
                Block block = loc1.getBlock();
                loc1.getBlock().setType(Material.CHEST);
                Chest chest = (Chest)block.getState();
                Inventory inventory = chest.getInventory();

                ItemStack swordDiams = new ItemBuilder(Material.DIAMOND_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 1).toItemStack();
                ItemStack pomme = new ItemBuilder(Material.GOLDEN_APPLE).toItemStack();
                inventory.addItem(swordDiams, pomme);

                for(Player player : Bukkit.getOnlinePlayers()) {
                player.sendTitle("�2Un coffre est apparu !", "�8"+ loc1.getX() + ", " + loc1.getY() + ", " + loc1.getZ());
            }
                break;
            case 3:
                Location loc2 = new Location(Bukkit.getWorld("world"), Integer.parseInt(coffre2[0]), Integer.parseInt(coffre2[1]), Integer.parseInt(coffre2[2]));
                block = loc2.getBlock();
                loc2.getBlock().setType(Material.CHEST);
                chest = (Chest)block.getState();
                inventory = chest.getInventory();

                ItemStack plastronDiams = new ItemBuilder(Material.DIAMOND_CHESTPLATE).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack();
                pomme = new ItemBuilder(Material.GOLDEN_APPLE).toItemStack();
                inventory.addItem(plastronDiams, pomme);
            for(Player player : Bukkit.getOnlinePlayers()) {
                player.sendTitle("�2Un coffre est apparu !", "�8"+ loc2.getX() + ", " + loc2.getY() + ", " + loc2.getZ());
            }
                break;
            case 4:
                Location loc3 = new Location(Bukkit.getWorld("world"), Integer.parseInt(coffre3[0]), Integer.parseInt(coffre3[1]), Integer.parseInt(coffre3[2]));
                block = loc3.getBlock();
                loc3.getBlock().setType(Material.CHEST);
                chest = (Chest)block.getState();
                inventory = chest.getInventory();

                ItemStack arc = new ItemBuilder(Material.BOW).addEnchant(Enchantment.ARROW_KNOCKBACK, 1).toItemStack();
                pomme = new ItemBuilder(Material.GOLDEN_APPLE).toItemStack();
                inventory.addItem(arc, pomme);
                for(Player player : Bukkit.getOnlinePlayers()) {
                    player.sendTitle("�2Un coffre est apparu !", "�8"+ loc3.getX() + ", " + loc3.getY() + ", " + loc3.getZ());
                }
                break;
            }
        }, episodeLenght*20*60, episodeLenght*20*60);
	}
	public void timerTask() {
		timerTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            timerSecond++;
            if(timerSecond == 60) {
                timerSecond = 0;
                timerMin++;
            }
            if(timerMin == episodeLenght) {
                timerSecond = 0;
                timerMin = 0;
            }
        }, 0, 20);
	}

    public void endTask() {
        endTaskk = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            if(PluginMain.getInstance().getTeamsManager().getTeams().size() == 1) {
                for(Player player : Bukkit.getOnlinePlayers()){
                    Team team = getPlayersManager().getUhcPlayer(player).getTeam();
                    player.sendTitle("�6Fin de la partie !", "�e Victoire de l'�quipe "+ team.getColor() + team.getName() + "�e !");
                    player.playSound(player.getLocation(), Sound.LEVEL_UP, 10f, 0.7f);
                }
                Bukkit.broadcastMessage("�6Fin de la partie !");
                System.out.println("�6Fin de la partie !");

                Bukkit.getScheduler().cancelTask(endTaskk);
                Bukkit.getBannedPlayers().forEach(offlinePlayer -> offlinePlayer.setBanned(false));

                Bukkit.getScheduler().cancelTask(episodeTask);
                Bukkit.getScheduler().cancelTask(timerTask);
                episode = 1;
                timerMin = 0;
                timerSecond = 0;
                end = true;
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.setGameMode(GameMode.SPECTATOR);
                }
            }
        },0,10);
    }

    private void initTabList(){
    	PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    Field a = packet.getClass().getDeclaredField("a");
                    a.setAccessible(true);
                    Field b = packet.getClass().getDeclaredField("b");
                    b.setAccessible(true);
                    Object header = new ChatComponentText("�4Kill �8To Survive �aSaison 4");
                    Object footer = new ChatComponentText("�6D�velopp� par TakeHere#0001");
                    a.set(packet, header);
                    b.set(packet, footer);

                    if (Bukkit.getOnlinePlayers().size() == 0) return;
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
                    }

                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskTimer(this, 0, 2);
    }

	public void onDisable() {
	    Bukkit.getScheduler().cancelAllTasks();
	}
	public int getEpisode() {
		return episode;
	}
	public int getTimerSecond() {
		return timerSecond;
	}
	public int getTimerMin() {
		return timerMin;
	}
	public static Boolean getGameStarted() {
		return gameStarted;
	}
	public void setGameStarted(Boolean gameStarted) {
		this.gameStarted = gameStarted;
	}
    public static PluginMain getInstance() {
        return instance;
    }
    public PlayersManager getPlayersManager() {
        return playersManager;
    }
    public TeamsManager getTeamsManager() {
        return teamsManager;
    }
	public Map<Class<? extends GuiBuilder>, GuiBuilder> getRegisteredMenus() {
		return registeredMenus;
	}
	public GuiManager getGuiManager() {
		return guiManager;
	}
    
}
