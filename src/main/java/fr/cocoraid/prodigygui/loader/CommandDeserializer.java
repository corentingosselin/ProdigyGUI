package fr.cocoraid.prodigygui.loader;

import fr.cocoraid.prodigygui.ProdigyGUI;
import fr.cocoraid.prodigygui.ProdigyGUIPlayer;
import fr.cocoraid.prodigygui.bridge.PlaceholderAPIBridge;
import fr.cocoraid.prodigygui.threedimensionalgui.ThreeDimensionGUI;
import fr.cocoraid.prodigygui.threedimensionalgui.ThreeDimensionalMenu;
import fr.cocoraid.prodigygui.utils.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandDeserializer {



    private List<String> commands;
    private Player player;
    public CommandDeserializer(Player player, List<String> commands) {
        this.commands = commands;
        this.player = player;
    }

    public void execute() {
        if(commands.isEmpty()) return;


        commands.forEach(command -> {

            if(command.contains(":")) {
                String output = command.substring(0, command.indexOf(':'));
                String final_cmd = command.substring(command.lastIndexOf(":") + 1).trim();
                final_cmd = PlaceholderAPIBridge.setPlaceholders(final_cmd,player);



                switch (output.toLowerCase()) {
                    case "op":
                        if(player.isOp())
                            player.chat("/" + final_cmd);
                        else {
                            player.setOp(true);
                            player.chat("/" + final_cmd);
                            player.setOp(false);
                        }
                        break;
                    case "console":
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), final_cmd);
                        break;
                    case "tell":
                        player.sendMessage(CC.colored(final_cmd));
                        break;
                    case "close":
                        if(ProdigyGUIPlayer.getProdigyPlayers().containsKey(player.getUniqueId())) {
                            ProdigyGUIPlayer pp = ProdigyGUIPlayer.instanceOf(player);
                            if(pp.getThreeDimensionGUI() != null && pp.getThreeDimensionGUI().isSpawned()) {
                                pp.getThreeDimensionGUI().closeGui();
                            }
                        }
                        break;
                    case "open":
                        ThreeDimensionalMenu.getMenus().stream().filter(m -> m.getFileName().equalsIgnoreCase(command.substring(command.lastIndexOf(":") + 1).trim())).findAny().ifPresent(menu -> {
                            if(menu.getPermission() != null && !player.hasPermission(menu.getPermission())) {
                                player.sendMessage(ProdigyGUI.getInstance().getLanguage().no_permission);
                                return;
                            }
                            new ThreeDimensionGUI(player, menu).openGui();

                        });
                        break;

                }



            } else {
                player.chat("/" + command.replace("/" , ""));
            }




        });


    }


}
