package fr.badobadadev.killtosurvive.teams;

import fr.badobadadev.killtosurvive.players.UHCPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class Team {
    private List<UHCPlayer> members;
    private ChatColor color;
    private String name;
    private int id;
    private Location teleportLocation;

    public Team(String name, int id, ChatColor chatColor, Location teleportLocation) {
        this.members = new ArrayList<>();
        this.name = name;
        this.id = id;
        this.color = chatColor;
        this.teleportLocation = teleportLocation;
    }
    public List<UHCPlayer> getMembers() {
        return members;
    }
    public int getMemberCount(){
        return members.size();
    }
    public ChatColor getColor() {
        return color;
    }
    public void setColor(ChatColor color) {
        this.color = color;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Location getTeleportLocation() {
        return teleportLocation;
    }
    public void setTeleportLocation(Location teleportLocation) {
        this.teleportLocation = teleportLocation;
    }
}

