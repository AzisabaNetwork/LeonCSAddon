package net.azisaba.leoncsaddon;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WeaponConfig extends Config{

    HashMap<String, WeaponConfigData> weaponsMap;
    HashMap<String,WeaponConfigData> cache;

    public WeaponConfig(File folder, String name) {
        super(folder, name);
    }

    @Override
    public void init(){
        weaponsMap = new HashMap<>();
        cache = new HashMap<>();
        File weaponsFolder = new File(folder, "weapons/");
        if(!weaponsFolder.exists()) {
            weaponsFolder.mkdir();
        }

        if(weaponsFolder.listFiles() == null) {
            return;
        }

        if(weaponsFolder.listFiles().length == 0){
            System.out.print("[LeonCSAddon] [WARN] Not found Weapons file.");
        }else {

            for ( File file : weaponsFolder.listFiles() ) {
                if ( file.isFile() ) {
                    Configuration configuration = YamlConfiguration.loadConfiguration(file);
                    configuration.getKeys(false).forEach(key -> {

                        List<String> type = configuration.getStringList(key + ".type");
                        boolean isMain = configuration.getBoolean(key + ".isMain",true);
                        double damage = configuration.getDouble(key + ".damage",0);
                        double headshotBonusDamage = configuration.getDouble(key + ".headshotBonusDamage",0);
                        double criticalBonusDamage = configuration.getDouble(key + ".criticalBonusDamage",0);
                        double guardMult = configuration.getDouble(key + ".guardMult",1.0);
                        float walkSpeed = (float) configuration.getDouble(key + ".walkSpeed",0.2);
                        boolean canSprint = configuration.getBoolean(key + ".canSprint",false);
                        int reduceStartTick = configuration.getInt(key + ".reduceStartTick",0);
                        int reduceEndTick = configuration.getInt(key + ".reduceEndTick",0);
                        double reduceDamage = configuration.getDouble(key + ".reduceDamage",0);
                        double projectileSizeXZ = configuration.getDouble(key + ".projectileSize.xz",1.5);
                        double projectileSizeY = configuration.getDouble(key + ".projectileSize.y",1.5);

                        weaponsMap.put(key,new WeaponConfigData(key, type, isMain, damage, headshotBonusDamage, criticalBonusDamage,  guardMult, walkSpeed, canSprint, reduceStartTick, reduceEndTick, reduceDamage, projectileSizeXZ, projectileSizeY));

                    });
                }
            }
            //LeonGunWar.getPlugin().getLogger().info(String.format("[Weapons] Loaded %s weapons data.", weaponsMap.size()));
        }
    }

    public WeaponConfigData getWeaponConfigData(String name){

        if(cache.containsKey(name)){
            return cache.get(name);
        }else {
            WeaponConfigData data = weaponsMap.getOrDefault(name,null);
            cache.put(name,data);
            return data;
        }

    }

}
