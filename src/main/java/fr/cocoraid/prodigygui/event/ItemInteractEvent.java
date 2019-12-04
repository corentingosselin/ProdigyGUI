package fr.cocoraid.prodigygui.event;

import fr.cocoraid.prodigygui.ProdigyGUI;
import fr.cocoraid.prodigygui.config.CoreConfig;
import fr.cocoraid.prodigygui.language.Language;
import fr.cocoraid.prodigygui.threedimensionalgui.ThreeDimensionGUI;
import fr.cocoraid.prodigygui.threedimensionalgui.ThreeDimensionalMenu;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class ItemInteractEvent implements Listener {


    private static Language lang = ProdigyGUI.getInstance().getLanguage();

    @EventHandler
    public void interact(PlayerInteractEvent e) {
        if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if(e.getItem() != null && e.getItem().hasItemMeta()) {
                ThreeDimensionalMenu.getMenus().stream()
                        .filter(m -> m.getOpenItem() == e.getItem().getType() && m.getOpenItemName().equalsIgnoreCase(e.getItem().getItemMeta().getDisplayName()))
                        .findAny().ifPresent(m -> {
                            if(m.getPermission() != null && !e.getPlayer().hasPermission(m.getPermission())) {
                                e.getPlayer().sendMessage(lang.no_permission);
                                return;
                            }
                            new ThreeDimensionGUI(e.getPlayer(), m).openGui();
                        });

                }
        }
    }


}
