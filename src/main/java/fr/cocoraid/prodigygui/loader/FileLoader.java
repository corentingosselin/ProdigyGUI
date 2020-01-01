package fr.cocoraid.prodigygui.loader;

import fr.cocoraid.prodigygui.ProdigyGUI;
import fr.cocoraid.prodigygui.threedimensionalgui.ThreeDimensionalMenu;
import fr.cocoraid.prodigygui.threedimensionalgui.itemdata.*;
import fr.cocoraid.prodigygui.utils.PluginFile;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class FileLoader {


    /**
     *
     * Big thanks to filoghost
     * @from Chestcommands sources
     *
     */

    private ProdigyGUI plugin;
    private boolean debug = false;





    public FileLoader(ProdigyGUI plugin) {
        this.plugin = plugin;


        File menusFolder = new File(ProdigyGUI.getInstance().getDataFolder(), "menu");
        if (!menusFolder.isDirectory()) {
            // Create the directory with the default menu.
            menusFolder.mkdirs();
            try {
                plugin.saveResource( "menu" + File.separator + "example.yml", false);
            } catch (Exception ex) {
                // Shhh...
            }
        }


        LinkedList<PluginFile> list = loadFileMenus();
        list.forEach(file -> {
            ThreeDimensionalMenu menu = loadMenuSettings(file);
            menu.setBarItemsList(loadItemBarData(file));
            menu.setItemDataList(loadItemDatas(file));
            ThreeDimensionalMenu.getMenus().add(menu);

            if(debug) {
                System.out.println("settings .... :");
                System.out.println("menu " + menu.getTitle());
                System.out.println("open item " + menu.getOpenItem());
                System.out.println("item name " + menu.getOpenItemName());
                System.out.println("command " + menu.getCommands());
                System.out.println("Items  .... :");
                menu.getItemDataList().forEach(item -> {

                    System.out.println("displayname " + item.getDisplayname());
                    System.out.println("lore " + item.getLore());
                    System.out.println("no perm message " + item.getNopermissionmessage());
                    System.out.println("perm " + item.getPermission());
                    System.out.println("price " + item.getPrice());
                });


            }
        });

    }

    /**
     * Loads all the configuration files recursively into a list.
     */
    private LinkedList<PluginFile> checkForMenus(File file) {
        LinkedList<PluginFile> list = new LinkedList<>();
        if (file.isDirectory()) {
            for (File subFile : file.listFiles()) {
                list.addAll(checkForMenus(subFile));
            }
        } else if (file.isFile()) {
            if (file.getName().endsWith(".yml")) {
                list.add(new PluginFile(plugin, file));
            }
        }
        return list;
    }

    private LinkedList<PluginFile> loadFileMenus() {
        File menusFolder = new File(plugin.getDataFolder(), "menu");

        LinkedList<PluginFile> menusList = checkForMenus(menusFolder);
        for (PluginFile menuConfig : menusList) {
            try {
                menuConfig.load();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("I/O error while loading the menu \"" + menuConfig.getFileName() + "\". Is the file in use?");
                continue;
            } catch (InvalidConfigurationException e) {
                e.printStackTrace();
                System.out.println("Invalid YAML configuration for the menu \"" + menuConfig.getFileName() + "\". Please look at the error above, or use an online YAML parser (google is your friend).");
                continue;
            }
        }
        return menusList;
    }


    private static final String MENU_TITLE = "menu-settings.title";


    private static final String MENU_RADIUS_DISTANCE = "menu-settings.radius-distance";
    private static final String MENU_PERMISSION = "menu-settings.permission";
    private static final String MENU_ANGLE_STEP = "menu-settings.angle-step";
    private static final String MENU_COMMAND = "menu-settings.command";
    private static final String PREVIOUS_MENU = "menu-settings.previous-menu";
    private static final String OPEN_ITEM_MATERIAL = "menu-settings.open-with-item.id";
    private static final String OPEN_ITEM_NAME = "menu-settings.open-with-item.name";

    private static final String DEFAULT_CLICK_SOUND_PATH = "menu-settings.slot-click-sound";
    private static final String DEFAULT_CHANGE_SOUND_PATH = "menu-settings.slot-change-sound";
    private static final String SOUND_NAME = "name";
    private static final String SOUND_VOLUME = "volume";
    private static final String SOUND_PITCH = "pitch";

    //all particles
    private static final String DEFAULT_CLICK_PARTICLE_PATH = "menu-settings.slot-click-particle";
    private static final String PARTICLE_NAME = "name";
    private static final String PARTICLE_AMOUNT = "amount";

    //normal
    private static final String PARTICLE_OFFSET_X = "offsetX";
    private static final String PARTICLE_OFFSET_Y = "offsetY";
    private static final String PARTICLE_OFFSET_Z = "offsetZ";
    private static final String PARTICLE_SPEED = "speed";

    //colored
    private static final String PARTICLE_RED = "red";
    private static final String PARTICLE_GREEN = "green";
    private static final String PARTICLE_BLUE = "blue";
    private static final String PARTICLE_RADIUS = "radius";



    private ThreeDimensionalMenu loadMenuSettings(PluginFile file) {

        //required
        ThreeDimensionalMenu menu = new ThreeDimensionalMenu(file.getFileName())
                .setTitle(file.getString(MENU_TITLE))
                .setRadius(file.getDouble(MENU_RADIUS_DISTANCE));

        //optionals
        if (file.isString(MENU_COMMAND)) {
            List<String> list = new ArrayList<>(Arrays.asList(file.getString(MENU_COMMAND).split("; ")));
            menu.setCommand(list);
        }
        if (file.isString(OPEN_ITEM_MATERIAL)) {
            menu.setOpenItem(Material.valueOf(file.getString(OPEN_ITEM_MATERIAL).toUpperCase()));
        }
        if(file.isInt(MENU_ANGLE_STEP))
            menu.setAngleStep(file.getInt(MENU_ANGLE_STEP));
        if (file.isString(OPEN_ITEM_NAME))
            menu.setOpenItemName(file.getString(OPEN_ITEM_NAME));

        if (file.isString(PREVIOUS_MENU) && !file.getString(PREVIOUS_MENU).isEmpty()) {
            menu.setPreviousMenu(file.getString(PREVIOUS_MENU));
        }

        if(file.isString(MENU_PERMISSION) && !file.getString(MENU_PERMISSION).isEmpty()) {
            menu.setPermission(file.getString(MENU_PERMISSION));
        }


        menu.setDefaultClickSound(soundDataConverter(file,DEFAULT_CLICK_SOUND_PATH));
        menu.setDefaultChangeSound(soundDataConverter(file,DEFAULT_CHANGE_SOUND_PATH));
        menu.setDefaultClickParticle(particleDataConverter(file,DEFAULT_CLICK_PARTICLE_PATH));
        return menu;
    }


    private ParticleData particleDataConverter(PluginFile file, String mainpath) {
        ConfigurationSection sectionParticle = file.getConfigurationSection(mainpath);
        if(sectionParticle != null) {
            ParticleData data;
            Particle particle = Particle.valueOf(sectionParticle.getString(PARTICLE_NAME));

            if(ColoredParticleData.getColorableParticles().contains(particle)) {
                ColoredParticleData coloredData = new ColoredParticleData(particle);
                if(sectionParticle.isInt(PARTICLE_RED))
                    coloredData.setR(sectionParticle.getInt(PARTICLE_RED));
                if(sectionParticle.isInt(PARTICLE_GREEN))
                    coloredData.setG(sectionParticle.getInt(PARTICLE_GREEN));
                if(sectionParticle.isInt(PARTICLE_BLUE))
                    coloredData.setB(sectionParticle.getInt(PARTICLE_BLUE));
                if(sectionParticle.isDouble(PARTICLE_RADIUS))
                    coloredData.setRadius(sectionParticle.getDouble(PARTICLE_RADIUS));
                data = coloredData;
            } else {
                NormalParticleData normalData = new NormalParticleData(particle);
                if(sectionParticle.isDouble(PARTICLE_OFFSET_X))
                    normalData.setOffsetX(sectionParticle.getDouble(PARTICLE_OFFSET_X));
                if(sectionParticle.isDouble(PARTICLE_OFFSET_Y))
                    normalData.setOffsetY(sectionParticle.getDouble(PARTICLE_OFFSET_Y));
                if(sectionParticle.isDouble(PARTICLE_OFFSET_Z))
                    normalData.setOffsetZ(sectionParticle.getDouble(PARTICLE_OFFSET_Z));
                if(sectionParticle.isDouble(PARTICLE_SPEED))
                    normalData.setSpeed(sectionParticle.getDouble(PARTICLE_SPEED));
                data = normalData;
            }
            if(data != null)
                if(sectionParticle.isInt(PARTICLE_AMOUNT))
                    data.setAmount(sectionParticle.getInt(PARTICLE_AMOUNT));
            return data;
        }
        return null;

    }


    private SoundData soundDataConverter(PluginFile file, String mainpath) {
        ConfigurationSection section = file.getConfigurationSection(mainpath);
        if (section != null) {
            SoundData soundData = new SoundData(Sound.valueOf(section.getString(SOUND_NAME)));
            if(section.isDouble(SOUND_VOLUME))
                soundData.setVolume((float)section.getDouble(SOUND_VOLUME));
            if(section.isDouble(SOUND_PITCH))
                soundData.setPitch((float)section.getDouble(SOUND_PITCH));
            return soundData;
        }
        return null;
    }

    public static final	String
            ID = "ID",
            SKULL_TEXTURE = "SKULL-TEXTURE",
            NAME = "NAME",
            //Optionals
            LORE = "LORE",
            COMMAND = "COMMAND",
            PRICE = "PRICE",
            PERMISSION = "PERMISSION",
            PERMISSION_MESSAGE = "PERMISSION-MESSAGE",
            SOUND = "SOUND",
            PARTICLE = "PARTICLE",
            ROTATION = "ROTATION";




    private LinkedList<ItemData> loadItemBarData(PluginFile file) {
        LinkedList<ItemData> itemdatas = new LinkedList<>();

        for (String subSectionName : file.getKeys(false)) {
            if (!subSectionName.equalsIgnoreCase("bar-items")) {
                continue;
            }
            ConfigurationSection section = file.getConfigurationSection(subSectionName);
            for (String item : section.getKeys(false)) {
                ConfigurationSection s = file.getConfigurationSection(section.getCurrentPath() + "." + item);

                ItemData itemdata = null;
                if(s.isString(ID)) {
                    itemdata = new ItemData(s.getString(NAME),  Material.valueOf(s.getString(ID).toUpperCase()));
                } else if(s.isSet(SKULL_TEXTURE)) {
                    itemdata = new ItemData(s.getString(NAME),  s.getString(SKULL_TEXTURE));
                } else {
                    //THROW ERROR
                    System.out.println("ProdigyGUI ERROR: You need at least ID or SKULL TEXTURE to create any item");
                }
                itemdata.setCommand(s.getString(COMMAND));
                itemdata.setLore(s.getString(LORE));
                itemdata.setPrice(s.getInt(PRICE));
                itemdata.setPermission(s.getString(PERMISSION));
                itemdata.setNopermissionmessage(s.getString(PERMISSION_MESSAGE));
                itemdata.setParticleData(particleDataConverter(file,item + "." + PARTICLE));
                itemdata.setSoundData(soundDataConverter(file,item + "." + SOUND));
                itemdata.setRotation(s.getInt(ROTATION));
                itemdatas.add(itemdata);


            }

        }
        return itemdatas;

    }

    private LinkedList<ItemData> loadItemDatas(PluginFile file) {
        LinkedList<ItemData> itemdatas = new LinkedList<>();

        for (String subSectionName : file.getKeys(false)) {
            if (subSectionName.equals("menu-settings") || subSectionName.equals("bar-items")) {
                continue;
            }

            ConfigurationSection s = file.getConfigurationSection(subSectionName);
            ItemData itemdata = null;
            if(s.isString(ID)) {
                itemdata = new ItemData(s.getString(NAME), Material.valueOf(s.getString(ID).toUpperCase()));
            } else if(s.isSet(SKULL_TEXTURE)) {
                itemdata = new ItemData(s.getString(NAME),  s.getString(SKULL_TEXTURE));
            } else {
                //THROW ERROR
                System.out.println("ProdigyGUI ERROR: You need at least ID or SKULL TEXTURE to create any item");
            }

            itemdata.setCommand(s.getString(COMMAND));
            itemdata.setLore(s.getString(LORE));
            itemdata.setPrice(s.getInt(PRICE));
            itemdata.setPermission(s.getString(PERMISSION));
            itemdata.setNopermissionmessage(s.getString(PERMISSION_MESSAGE));
            itemdata.setParticleData(particleDataConverter(file,subSectionName + "." + PARTICLE));
            itemdata.setSoundData(soundDataConverter(file,subSectionName + "." + SOUND));
            itemdata.setRotation(s.getInt(ROTATION));
            itemdatas.add(itemdata);
        }
        return itemdatas;

    }




}
