package fr.cocoraid.prodigygui.language;

import fr.cocoraid.prodigygui.ProdigyGUI;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LanguageLoader {

    private static Map<String, Language> languages = new HashMap<>();


    /**
     * How to serialize / deserialize cosmetics
     * @param instance
     *
     *
     * Then get all lang files:
     *
     * get all categories, check if exist key with category name
     * if yes deserialize
     * nope serialize
     *
     *
     *
     */

    public LanguageLoader(ProdigyGUI instance)  {
        //Load language and main folder
        if (!instance.getDataFolder().exists())
            instance.getDataFolder().mkdir();

        File databaseFolder = new File(instance.getDataFolder(), "language");
        if(!databaseFolder.exists())
            databaseFolder.mkdirs();

        //Create Default language if not exist
        File file = getLanguageFile("english");
        if (!file.exists()) {
            try {
                FileConfiguration c = YamlConfiguration.loadConfiguration(file);
                file.createNewFile();
                Language language = new Language(file,c);
                for (Field field : language.getClass().getDeclaredFields()) {
                    //because of file field inside
                    if(!Modifier.isTransient(field.getModifiers())) {
                        if (field.getType().equals(String.class)) {
                            String s = (String) field.get(language);
                            c.set(field.getName(), s.replace("§", "&"));
                        } else if (field.getType().equals(List.class)) {
                            List<String> list = new ArrayList<>();
                            ((List<String>) field.get(language)).forEach(s -> list.add(s.replace("§", "&")));
                            c.set(field.getName(), list);
                        }
                    }
                }
                c.save(file);
            }
            catch (IOException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        //Load all language found
        File[] files = new File(instance.getDataFolder(), "language").listFiles();
        for (File f : files) {

            if (f.isFile() && f.getName().endsWith(".yml")) {
                try {
                    FileConfiguration c = YamlConfiguration.loadConfiguration(f);

                    //check if field is missing
                    Language lang = new Language(f,c);

                    //cache are all fields not found inside yml, so they ar new
                    List<String> cache = new ArrayList<>();
                    for (Field field : lang.getClass().getDeclaredFields()) {
                        cache.add(field.getName());
                    }
                    List<String> yml = new ArrayList<>();
                    c.getConfigurationSection("").getKeys(false).forEach(s -> yml.add(s));

                    yml.removeAll(cache);


                    c.getConfigurationSection("").getKeys(false).forEach(s -> yml.add(s));
                    cache.removeAll(yml);


                    for (Field field : lang.getClass().getDeclaredFields()) {
                        if(!Modifier.isTransient(field.getModifiers())) {
                            if (!cache.contains(field.getName())) {
                                if (field.getType().equals(String.class)) {
                                    String s = c.getString(field.getName());
                                    field.set(lang, s.replace("&", "§"));
                                } else if (field.getType().equals(List.class)) {
                                    List<String> list = new ArrayList<>();
                                    c.getStringList(field.getName()).forEach(s -> list.add(s.replace("&", "§")));
                                    field.set(lang, list);
                                }
                            } else {
                                if (field.getType().equals(String.class))
                                    c.set(field.getName(), ((String) field.get(lang)).replace("§", "&"));
                                else if (field.getType().equals(List.class)) {
                                    List<String> list = new ArrayList<>();
                                    c.getStringList(field.getName()).forEach(s -> list.add(s.replace("&", "§")));
                                    c.set(field.getName(), list);
                                }
                            }
                        }

                    }



                    languages.put(f.getName().replace(".yml", ""), lang);
                    c.save(f);
                } catch (IOException | IllegalAccessException  e) {
                    e.printStackTrace();
                }
            }
        }

    }



    public static Language getLanguage(String lang) {
        return getLanguages().get(lang);
    }


    public static Map<String, Language> getLanguages() {
        return languages;
    }

    private File getLanguageFile(String language) {
        return new File(ProdigyGUI.getInstance().getDataFolder() + "/language", language + ".yml");
    }
}
