package fr.cocoraid.prodigygui.event;

import fr.cocoraid.prodigygui.ProdigyGUIPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinQuitEvent implements Listener {

    @EventHandler
    public void quitEvent(PlayerQuitEvent e) {
        if(ProdigyGUIPlayer.getProdigyPlayers().containsKey(e.getPlayer().getUniqueId())) {
            ProdigyGUIPlayer pp = ProdigyGUIPlayer.instanceOf(e.getPlayer());
            if(pp.getThreeDimensionGUI() != null && pp.getThreeDimensionGUI().isSpawned())
                pp.getThreeDimensionGUI().closeGui();
            ProdigyGUIPlayer.getProdigyPlayers().remove(e.getPlayer().getUniqueId());
        }
    }
}
