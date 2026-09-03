package net.azisaba.leoncsaddon;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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
                        List<String> requirements = configuration.getStringList(key + ".requirements");
                        double damage = configuration.getDouble(key + ".damage",0);
                        double headshotBonusDamage = configuration.getDouble(key + ".headshotBonusDamage",0);
                        double criticalBonusDamage = configuration.getDouble(key + ".criticalBonusDamage",0);
                        double backstabBonusDamage = configuration.getDouble(key + ".backstabBonusDamage", 0);
                        double guardMult = configuration.getDouble(key + ".guardMult",1.0);
                        float walkSpeed = (float) configuration.getDouble(key + ".walkSpeed",0.2);
                        boolean canSprint = configuration.getBoolean(key + ".canSprint",false);
                        int reduceStartTick = configuration.getInt(key + ".reduceStartTick",0);
                        int reduceEndTick = configuration.getInt(key + ".reduceEndTick",0);
                        double reduceDamage = configuration.getDouble(key + ".reduceDamage",0);
                        double projectileSizeXZ = configuration.getDouble(key + ".projectileSize.xz",0);
                        double projectileSizeY = configuration.getDouble(key + ".projectileSize.y",0);
                        Integer dualWieldWeaponCMD = (Integer) configuration.get(key + ".dualWieldWeaponCMD", null);
                        String dualWieldWeaponModel = configuration.getString(key + ".dualWieldWeaponModel", null);

                        String[] allyPotion = configuration.getString(key + ".allypotion", "").split(",");
                        List<PotionEffect> potionEffectList = new ArrayList<>();

                        for (String s : allyPotion) {
                            String potFX = s;
                            potFX = potFX.replace(" ", "");
                            String[] args = potFX.split("-");
                            if (args.length == 3) {
                                try {
                                    org.bukkit.NamespacedKey keyObj = org.bukkit.NamespacedKey.fromString(args[0].toLowerCase(java.util.Locale.ROOT));
                                    PotionEffectType potionType = keyObj != null ? org.bukkit.Registry.POTION_EFFECT_TYPE.get(keyObj) : null;
                                    if (potionType == null) {
                                        throw new IllegalArgumentException("Unknown potion effect type: " + args[0]);
                                    }
                                    int duration = Integer.parseInt(args[1]);
                                    potionEffectList.add(potionType.createEffect(duration, Integer.parseInt(args[2]) - 1));
                                } catch (Exception var15) {
                                    System.out.println("[LeonCSAddon] '" + potFX + "' of weapon '" + key + "' has an incorrect potion type, duration or level!");
                                }
                            }
                        }
                        weaponsMap.put(key,new WeaponConfigData(key, type, isMain, requirements, damage, headshotBonusDamage, criticalBonusDamage, backstabBonusDamage,  guardMult, walkSpeed, canSprint, reduceStartTick, reduceEndTick, reduceDamage, projectileSizeXZ, projectileSizeY, dualWieldWeaponCMD, dualWieldWeaponModel, potionEffectList));
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