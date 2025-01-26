package net.azisaba.leoncsaddon;

import net.azisaba.leoncsaddon.Command.RandomDamage;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class LeonCSAddon extends JavaPlugin {

    public static LeonCSAddon INSTANCE;
    private WeaponConfig weaponConfig;
    public static WeaponDamageRandomizer weaponDamageRandomizer;

    @Override
    public void onEnable() {
        // Plugin startup logic
        INSTANCE = this;
        this.saveDefaultConfig();
        weaponConfig = new WeaponConfig(getDataFolder(),"weaponConfig");
        weaponConfig.load();

        Bukkit.getPluginManager().registerEvents(new WeaponCustomDamageListener(),this);
        Bukkit.getPluginManager().registerEvents(new CustomProjectileSizeListener(),this);
        Bukkit.getPluginManager().registerEvents(new WeaponShootLimitListener(), this);
        Bukkit.getPluginManager().registerEvents(new WeaponPotionListener(), this);
        Bukkit.getPluginManager().registerEvents(new DualWieldWeaponListener(), this);
        Bukkit.getPluginManager().registerEvents(new WeaponPreShootListener(), this);

        weaponDamageRandomizer = new WeaponDamageRandomizer();
        weaponDamageRandomizer.randomDamageUpdaterDefaultTask();
        weaponDamageRandomizer.damageUpdaterTaskStarter();

        getCommand("randomdamage").setExecutor(new RandomDamage());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public WeaponConfig getWeaponConfig() {
        return weaponConfig;
    }
}
