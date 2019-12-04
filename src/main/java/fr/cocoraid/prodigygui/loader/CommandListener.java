package fr.cocoraid.prodigygui.loader;

import fr.cocoraid.prodigygui.ProdigyGUI;
import fr.cocoraid.prodigygui.threedimensionalgui.ThreeDimensionGUI;
import fr.cocoraid.prodigygui.threedimensionalgui.ThreeDimensionalMenu;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandListener implements Listener {

	@EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onCommand(PlayerCommandPreprocessEvent event) {

		if (event.getMessage().contains(" ")) {
			return;
		}

		// Very fast method compared to split & substring.
		String command = getCleanCommand(event.getMessage());

		if (command.isEmpty()) {
			return;
		}

		ThreeDimensionalMenu.getMenus().stream().filter(m -> m.getCommands().contains(command)).findFirst().ifPresent(m -> {
			event.setCancelled(true);

			if(m.getPermission() != null && !event.getPlayer().hasPermission(m.getPermission())) {
				event.getPlayer().sendMessage(ProdigyGUI.getInstance().getLanguage().no_permission);
				return;
			}
			new ThreeDimensionGUI(event.getPlayer(),m).openGui();
		});

	}

	public static String getCleanCommand(String message) {
		char[] chars = message.toCharArray();

		if (chars.length <= 1) {
			return "";
		}

		int pos = 0;
		for (int i = 1; i < chars.length; i++) {
			if (chars[i] == ' ') {
				break;
			}

			chars[(pos++)] = chars[i];
		}

		return new String(chars, 0, pos);
	}

}