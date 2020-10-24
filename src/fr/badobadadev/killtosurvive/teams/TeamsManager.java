package fr.badobadadev.killtosurvive.teams;

import fr.badobadadev.killtosurvive.players.UHCPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class TeamsManager {
    private List<Team> teams;

    public TeamsManager() {
        this.teams = new ArrayList<>();
    }
    public boolean doesTeamExist(Team team){
        return getTeam(team) != null;
    }
    public void create(String name, int id, ChatColor chatColor, Location teleportLocation){
        Team team = new Team(name, id, chatColor, teleportLocation);
        teams.add(team);
    }
    public List<Team> getPlayersList(){
        return teams;
    }
    public Team getTeam(Team team){
        return getTeam(team.getName());
    }
    public Team getTeam(String name){
        for(Team team : getTeams()){
            if(team.getName().equalsIgnoreCase(name)) {
                return team;
            }
        }
        return null;
    }
    public void disband(Team team){
        teams.remove(team);
    }
    public void leave(UHCPlayer player, Team team){
        player.setOnTeam(false);
        player.setTeam(null);
        team.getMembers().remove(player);
    }
    public void join(UHCPlayer player, Team team){
        player.setOnTeam(true);
        player.setTeam(team);
        team.getMembers().add(player);
    }
    public List<Team> getTeams(){
        return teams;
    }
    public Team getTeamByID(int id){
        for(Team team : teams){
            if(team.getId() == id) return team;
            else break;
        }
        throw new NullPointerException("Aucune team ne possède l'ID" + id + ".");
    }

    public void clearTeam(int id){
        for(UHCPlayer  uhcPlayer : getTeamByID(id).getMembers()){
            leave(uhcPlayer, getTeamByID(id));
        }
    }
}
