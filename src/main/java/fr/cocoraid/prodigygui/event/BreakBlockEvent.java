package fr.cocoraid.prodigygui.event;

import fr.cocoraid.prodigygui.ProdigyGUIPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BreakBlockEvent implements Listener {

    @EventHandler
    public void breakBlock(BlockBreakEvent e) {
        if(ProdigyGUIPlayer.getProdigyPlayers().containsKey(e.getPlayer().getUniqueId())) {
            ProdigyGUIPlayer pp = ProdigyGUIPlayer.instanceOf(e.getPlayer());
            if(pp.getThreeDimensionGUI() != null && pp.getThreeDimensionGUI().isSpawned()) {
                e.setCancelled(true);
            }
        }
    }
}
