package fr.badobadadev.killtosurvive.gui;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.badobadadev.killtosurvive.PluginMain;
import fr.badobadadev.killtosurvive.players.UHCPlayer;
import fr.badobadadev.killtosurvive.teams.Team;
import fr.badobadadev.killtosurvive.utils.GuiBuilder;
import fr.badobadadev.killtosurvive.utils.ItemBuilder;

public class TeamsGui implements GuiBuilder {

	PluginMain main;
	public TeamsGui(PluginMain pluginMain) {
		this.main = pluginMain;
	}

	@Override
	public String name() {
		
		return "Sélecteur de Teams";
	}

	@Override
	public int getSize() {
		return 9;
	}

	@Override
	public void contents(Player player, Inventory inv) {
		ItemStack teamRouge = new ItemBuilder(Material.WOOL).setWoolColor(DyeColor.RED).setName("§cEquipe rouge").setLore(getTeamMembers("rouge")).toItemStack();
		ItemStack teamBleue = new ItemBuilder(Material.WOOL).setWoolColor(DyeColor.BLUE).setName("§1Equipe bleue").setLore(getTeamMembers("bleue")).toItemStack();
		ItemStack teamVerte = new ItemBuilder(Material.WOOL).setWoolColor(DyeColor.GREEN).setName("§2Equipe verte").setLore(getTeamMembers("verte")).toItemStack();
		
		inv.setItem(2, teamRouge);
		inv.setItem(4, teamBleue);
		inv.setItem(6, teamVerte);
		
	}

	@Override
	public void onClick(Player player, Inventory inv, ItemStack current, int slot) {
		if(current.getType() == Material.AIR)return;
		switch (current.getItemMeta().getDisplayName()) {
			case "§cEquipe rouge":
				player.sendMessage(addtoteam(player, "Rouge"));
				player.closeInventory();
				break;
				
			case "§1Equipe bleue":
				player.sendMessage(addtoteam(player, "Bleue"));
				player.closeInventory();
				break;
				
			case "§2Equipe verte":
				player.sendMessage(addtoteam(player, "Verte"));
				player.closeInventory();
				break;
				
			default:
				break;
		}
	}
	
	public String addtoteam(Player player, String teamName) {
        if(!PluginMain.getGameStarted()) {
            UHCPlayer uhcPlayer = main.getPlayersManager().getUhcPlayer(player);
            Team team = main.getTeamsManager().getTeam(teamName);
            if(team == uhcPlayer.getTeam()){
                player.playSound(player.getLocation(), Sound.VILLAGER_NO, 3f, 1f);
                return "§cLe joueur " + player.getName() + " est déjà dans cette équipe !";
            }
            
            if(uhcPlayer.getTeam() != null)
                main.getTeamsManager().leave(uhcPlayer, uhcPlayer.getTeam());
            main.getTeamsManager().join(uhcPlayer, team);
            System.out.println("Player: " + player.getName() + " was added to team: " + teamName);
            player.playSound(player.getLocation(), Sound.NOTE_PLING, 3f, 1f);
            return "§aLe joueur a bien été ajouté(e) à l'équipe !";
		}else {
			player.playSound(player.getLocation(), Sound.VILLAGER_NO, 3f, 1f);
			return "§cLe jeu a déja commencé !";
		}
	}

	public String getTeamMembers(String name){
	    if(name.equalsIgnoreCase("rouge")){
            Team team = main.getTeamsManager().getTeam("rouge");

            if(team.getMembers().isEmpty()) return "§eCette team est vide.";
            else{
                StringBuilder stringBuilder = new StringBuilder();
                for(UHCPlayer uhcPlayer : team.getMembers()){
                    stringBuilder.append("§e").append(uhcPlayer.getDisplayName()).append(" ");
                }
                return stringBuilder.toString();
            }
        }else if(name.equalsIgnoreCase("bleue")){
            Team team = main.getTeamsManager().getTeam("bleue");

            if(team.getMembers().isEmpty()) return "§eCette team est vide.";
            else{
                StringBuilder stringBuilder = new StringBuilder();
                for(UHCPlayer uhcPlayer : team.getMembers()){
                    stringBuilder.append("§e").append(uhcPlayer.getDisplayName()).append(" ");
                }
                return stringBuilder.toString();
            }
        }else if(name.equalsIgnoreCase("verte")){
            Team team = main.getTeamsManager().getTeam("verte");

            if(team.getMembers().isEmpty()) return "§eCette team est vide.";
            else{
                StringBuilder stringBuilder = new StringBuilder();
                for(UHCPlayer uhcPlayer : team.getMembers()){
                    stringBuilder.append("§e").append(uhcPlayer.getDisplayName()).append(" ");
                }
                return stringBuilder.toString();
            }
        }else{
	        return "§eCette team est vide.";
        }
    }
}	
