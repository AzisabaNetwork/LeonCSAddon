package net.azisaba.leoncsaddon;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class LeonCSAddon extends JavaPlugin {

    public static LeonCSAddon INSTANCE;
    private WeaponConfig weaponConfig;

    @Override
    public void onEnable() {
        // Plugin startup logic
        INSTANCE = this;
        weaponConfig = new WeaponConfig(getDataFolder(),"weaponConfig");
        weaponConfig.load();

        Bukkit.getPluginManager().registerEvents(new WeaponCustomDamageListener(),this);
        Bukkit.getPluginManager().registerEvents(new CustomProjectileSizeListener(),this);
        Bukkit.getPluginManager().registerEvents(new WeaponShootLimitListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public WeaponConfig getWeaponConfig() {
        return weaponConfig;
    }
}
