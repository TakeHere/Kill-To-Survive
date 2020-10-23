package fr.badobadadev.killtosurvive.players;

import fr.badobadadev.killtosurvive.teams.Team;
import org.bukkit.entity.Player;

import java.util.UUID;

public class UHCPlayer {
    private final Player player;
    private final UUID uuid;
    public Team team;
    private String displayName, name;
    private boolean isOnTeam;

    public UHCPlayer(Player player) {
        this.player = player;
        this.uuid = player.getUniqueId();
        this.team = null;
        this.name = player.getName();
        this.displayName = player.getDisplayName();
        this.isOnTeam = false;
    }
    public Player getPlayer() {
        return player;
    }
    public UUID getUuid() {
        return uuid;
    }
    public Team getTeam() {
        return team;
    }
    public void setTeam(Team team) {
        this.team = team;
    }
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public boolean isOnTeam() {
        return isOnTeam;
    }
    public void setOnTeam(boolean onTeam) {
        isOnTeam = onTeam;
    }
}
