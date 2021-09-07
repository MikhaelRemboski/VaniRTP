package main;

import cmds.RtpCMD;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import utils.Constants;

import java.io.File;


public class Main extends JavaPlugin {
    public static Main instance;
    public String configPath;

    @Override
    public void onEnable() {
        instance = this;
        registerCommands();
        Bukkit.getConsoleSender().sendMessage(colorize(Constants.ON_ENABLE_MESSAGE));
        loadConfig();
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(colorize(Constants.ON_DISABLE_MESSAGE));
    }


    private void registerCommands() {
        getCommand(Constants.VANIRTP_CMD).setExecutor(new RtpCMD(this));
    }

    private void loadConfig() {
        File config = new File(this.getDataFolder(), Constants.CONFIG_MAIN);
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
