package fr.cocoraid.prodigygui;

import fr.cocoraid.prodigygui.threedimensionalgui.ThreeDimensionGUI;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by cocoraid on 30/06/2016.
 */
public class ProdigyGUIPlayer {

    private static Map<UUID, ProdigyGUIPlayer> prodigyPlayers = new HashMap<>();

    private Player player;
    private ThreeDimensionGUI threeDimensionGUI;
    private float previousYaw;
    private int globalTime = 0;


    public ProdigyGUIPlayer(Player player) {
        this.player = player;
    }


    public static ProdigyGUIPlayer instanceOf(Player player) {
        prodigyPlayers.putIfAbsent(player.getUniqueId(), new ProdigyGUIPlayer(player));
        if (prodigyPlayers.containsKey(player.getUniqueId())) {
            prodigyPlayers.get(player.getUniqueId()).updatePlayer(player);
        }
        return prodigyPlayers.get(player.getUniqueId());
    }

    public static Map<UUID, ProdigyGUIPlayer> getProdigyPlayers() {
        return prodigyPlayers;
    }

    public void resetProdigyPlayer() {
        clearPlayer();

        prodigyPlayers.remove(player.getUniqueId());
    }

    public void clearPlayer() {
        if (player == null) return;
       // if (threeDimensionGUI != null)
            //threeDimensionGUI.closeGui();
    }



    public Player getPlayer() {
        return player;
    }

    public ThreeDimensionGUI getThreeDimensionGUI() {
        return threeDimensionGUI;
    }

    public void setThreeDimensionGUI(ThreeDimensionGUI threeDimensionGUI) {
        this.threeDimensionGUI = threeDimensionGUI;
    }

    public int getGlobalTime() {
        return globalTime;
    }

    public void addTime() {
        globalTime++;
        if (globalTime >= 999999)
            globalTime = 0;
    }

    public void updatePlayer(Player player) {
        this.player = player;
    }



    public float getPreviousYaw() {
        return previousYaw;
    }

    public void setPreviousYaw(float previousYaw) {
        this.previousYaw = previousYaw;
    }

}
