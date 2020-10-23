package fr.badobadadev.killtosurvive.players;

import fr.badobadadev.killtosurvive.PluginMain;
import fr.badobadadev.killtosurvive.teams.Team;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class PlayersManager {
    private List<UHCPlayer> players;

    public PlayersManager(){
        players = new ArrayList<>();
    }
    public boolean doesPlayerExist(Player player){
        return getUhcPlayer(player.getUniqueId()) != null;
    }
    public UHCPlayer newUhcPlayer(Player player){
        UHCPlayer newPlayer = new UHCPlayer(player);
        getPlayersList().add(newPlayer);
        return newPlayer;
    }
    public List<UHCPlayer> getPlayersList(){
        return players;
    }
    public UHCPlayer getUhcPlayer(Player player){
        return getUhcPlayer(player.getUniqueId());
    }
    public UHCPlayer getUhcPlayer(String name){
        for(UHCPlayer uhcPlayer : getPlayersList()){
            if(uhcPlayer.getName().equals(name)) {
                return uhcPlayer;
            }
        }
        return null;
    }
    public UHCPlayer getUhcPlayer(UUID uuid){
        for(UHCPlayer uhcPlayer : getPlayersList()){
            if(uhcPlayer.getUuid().equals(uuid)) {
                return uhcPlayer;
            }
        }
        return null;
    }
    public void removeUhcPlayer(UHCPlayer uhcPlayer){
        getPlayersList().remove(uhcPlayer);
    }
    public void quit(UHCPlayer uhcPlayer){
        if(uhcPlayer.isOnTeam())
                PluginMain.getInstance().getTeamsManager().leave(uhcPlayer, uhcPlayer.getTeam());
        PluginMain.getInstance().getPlayersManager().removeUhcPlayer(uhcPlayer);
    }
}
