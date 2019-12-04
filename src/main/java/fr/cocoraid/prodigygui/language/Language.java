package fr.cocoraid.prodigygui.language;

import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Language {


    private transient File file;
    private transient FileConfiguration langFile;
    public Language(File file, FileConfiguration fc) {
        this.file = file;
        this.langFile = fc;
    }

    //Default english

    public String no_permission = "§cYou do not have the permission to do that";
    public String no_money = "§cYou do not have enough money, §cyou need {money}$";
    public String button_next_name = "§bnext";
    public String button_previous_name = "§bprevious";



    public File getFile() {
        return file;
    }

    public FileConfiguration getLangFile() {
        return langFile;
    }
}
