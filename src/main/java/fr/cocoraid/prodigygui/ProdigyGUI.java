package fr.cocoraid.prodigygui;

import fr.cocoraid.prodigygui.bridge.EconomyBridge;
import fr.cocoraid.prodigygui.bridge.PlaceholderAPIBridge;
import fr.cocoraid.prodigygui.config.CoreConfig;
import fr.cocoraid.prodigygui.event.BreakBlockEvent;
import fr.cocoraid.prodigygui.event.ItemInteractEvent;
import fr.cocoraid.prodigygui.event.JoinQuitEvent;
import fr.cocoraid.prodigygui.language.Language;
import fr.cocoraid.prodigygui.language.LanguageLoader;
import fr.cocoraid.prodigygui.loader.CommandListener;
import fr.cocoraid.prodigygui.loader.FileLoader;
import fr.cocoraid.prodigygui.protocol.InteractableItemProtocol;
import fr.cocoraid.prodigygui.task.ThreeDimensionalGUITask;
import fr.cocoraid.prodigygui.threedimensionalgui.ThreeDimensionGUI;
import fr.cocoraid.prodigygui.threedimensionalgui.ThreeDimensionalMenu;
import fr.cocoraid.prodigygui.utils.CC;
import fr.cocoraid.prodigygui.utils.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.mineacademy.remain.Remain;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ProdigyGUI extends JavaPlugin {

    /**
     * TODO: make thing buyable
     */


    private Language language;
    private CoreConfig config;

    private static ProdigyGUI instance;
    private Metrics metrics;


    @Override
    public void onEnable() {
        instance = this;
        Remain.setPlugin(this);
        ConsoleCommandSender c = Bukkit.getServer().getConsoleSender();
        metrics = new Metrics(this);

        loadConfiguration();

        if (!EconomyBridge.setupEconomy()) {
            getLogger().warning("Vault with a compatible economy plugin was not found! Icons with a PRICE or commands that give money will not work.");
        }

        if (PlaceholderAPIBridge.setupPlugin()) {
            getLogger().info("Hooked PlaceholderAPI");
        }

        new LanguageLoader(this);


        if(!LanguageLoader.getLanguages().containsKey(config.language.toLowerCase())) {
            c.sendMessage("§c Language not found ! Please check your language folder");
        } else
            language = LanguageLoader.getLanguage(config.language.toLowerCase());
        c.sendMessage(CC.d_green + "Language: " + (language == null ? "english" : config.language.toLowerCase()));
        if(language == null)
            language = LanguageLoader.getLanguage("english");


        new FileLoader(this);
        new InteractableItemProtocol(this);
        new ThreeDimensionalGUITask(this);
        Bukkit.getPluginManager().registerEvents(new CommandListener(), this);
        Bukkit.getPluginManager().registerEvents(new JoinQuitEvent(), this);
        Bukkit.getPluginManager().registerEvents(new BreakBlockEvent(), this);
        Bukkit.getPluginManager().registerEvents(new ItemInteractEvent(), this);

    }

    @Override
    public void onDisable() {
        ProdigyGUIPlayer.getProdigyPlayers().values().stream().filter(pp -> pp.getThreeDimensionGUI() != null && pp.getThreeDimensionGUI().isSpawned()).forEach(pp -> {
            pp.getThreeDimensionGUI().closeGui();
        });

    }

    private void loadConfiguration() {
        final Logger logger = getLogger();
        try {
            config = new CoreConfig(new File(this.getDataFolder(), "configuration.yml"));
            config.load();
        } catch(final InvalidConfigurationException ex) {
            ex.printStackTrace();
            logger.log(Level.SEVERE, "Oooops ! Something went wrong while loading the configuration !");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (command.getName().equalsIgnoreCase("prodigygui")) {
            //prodigygui open <menu> <yawRotation> <x> <y> <z> <playername>
            if (args.length == 7) {
                if(args[0].equalsIgnoreCase("open")) {

                    if(sender instanceof Player) {
                        Player p = (Player) sender;
                        if(!p.hasPermission("prodigygui.other.open")) {
                            p.sendMessage(language.no_permission);
                            return false;
                        }
                    }

                    ThreeDimensionalMenu menu  = ThreeDimensionalMenu.getMenus().stream()
                            .filter(m -> m.getFileName().replace(".yml", "").equalsIgnoreCase(args[1])).findAny()
                            .orElseGet(() -> null);
                    if(menu == null) {
                        sender.sendMessage("§cMenu " + args[1] + " could not be found !");
                        return false;
                    }

                    try {
                        Double.valueOf(args[2]);
                    } catch (Exception e) {
                        sender.sendMessage("§cThe yaw rotation " + args[1] + " must be integer !");
                        sender.sendMessage("§c/prodigygui open <menu> <yawRotation> <x> <y> <z> <playername>");
                        return false;
                    }

                    try {
                        Double.valueOf(args[3]);
                        Double.valueOf(args[4]);
                        Double.valueOf(args[5]);
                    } catch (Exception e) {
                        sender.sendMessage("§cThe yaw rotation " + args[1] + " must be integer !");
                        sender.sendMessage("§c/prodigygui open <menu> <yawRotation> <x> <y> <z> <playername>");
                        return false;
                    }

                    if(Bukkit.getPlayer(args[6]) != null && Bukkit.getPlayer(args[6]).isOnline()) {

                        new ThreeDimensionGUI(Bukkit.getPlayer(args[6]),menu)
                                .setRotation(Float.valueOf(args[2]))
                                .setCenter(Double.valueOf(args[3]),Double.valueOf(args[4]),Double.valueOf(args[5]))
                                .openGui();

                    } else {
                        sender.sendMessage("Player " + args[6] + " is not online");
                        return false;
                    }



                }
                //prodigygui open <menu> <yawRotation> <playername>
            } else if(args.length == 4) {
                if(args[0].equalsIgnoreCase("open")) {
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        if (!p.hasPermission("prodigygui.other.open")) {
                            p.sendMessage(language.no_permission);
                            return false;
                        }
                    }

                    ThreeDimensionalMenu menu = ThreeDimensionalMenu.getMenus().stream()
                            .filter(m -> m.getFileName().replace(".yml", "").equalsIgnoreCase(args[1])).findAny()
                            .orElseGet(() -> null);
                    if (menu == null) {
                        sender.sendMessage("§cMenu " + args[1] + " could not be found !");
                        return false;
                    }

                    try {
                        Double.valueOf(args[2]);
                    } catch (Exception e) {
                        sender.sendMessage("§cThe yaw rotation " + args[1] + " must be integer !");
                        sender.sendMessage("§c/prodigygui open <menu> <yawRotation> <playername>");
                        return false;
                    }


                    if (Bukkit.getPlayer(args[3]) != null && Bukkit.getPlayer(args[3]).isOnline()) {

                        new ThreeDimensionGUI(Bukkit.getPlayer(args[3]), menu)
                                .setRotation(Float.valueOf(args[2]))
                                .openGui();

                    } else {
                        sender.sendMessage("Player " + args[3] + " is not online");
                        return false;
                    }
                }

            } else {
                sender.sendMessage("§c/prodigygui open <menu> <yawRotation> <x> <y> <z> <playername>");
                sender.sendMessage("§c/prodigygui open <menu> <yawRotation> <playername>");
            }

        }

        return false;
    }

    public CoreConfig getConfiguration() {
        return config;
    }

    public Language getLanguage() {
        return language;
    }

    public static ProdigyGUI getInstance() {
        return instance;
    }
}
