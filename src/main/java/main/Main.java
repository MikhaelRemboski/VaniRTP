package main;

import cmds.RtpCMD;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;


public class Main extends JavaPlugin {
    public static Main instance;
    public String configPath;

    @Override
    public void onEnable() {
        instance = this;
        registerCommands();
        Bukkit.getConsoleSender().sendMessage(colorize("&2&lActivando VaniRTP"));
        loadConfig();
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(colorize("&2&lDesactivando VaniRTP"));
    }


    private void registerCommands() {
        getCommand("vanirtp").setExecutor(new RtpCMD(this));
    }

    private void loadConfig() {
        File config = new File(this.getDataFolder(), "config.yml");
        configPath = config.getPath();
        if (!config.exists()) {
            this.getConfig().options().copyDefaults(true);
            saveConfig();
        }
    }

    public static String colorize(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }


}
