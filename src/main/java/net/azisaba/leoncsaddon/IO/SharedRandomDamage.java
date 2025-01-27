package net.azisaba.leoncsaddon.IO;

import net.azisaba.leoncsaddon.WeaponDamageRandomizer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class SharedRandomDamage {
    static File rDDataFolder = new File("shared", "LCSAConfig");
    public static void saveRD(){
        initSaveSettings(rDDataFolder);
        File rDDataFile = new File(rDDataFolder, "RandomDamage" + ".yml");
        if(!rDDataFile.exists()) {
            try {
                rDDataFile.createNewFile(); // 新規ファイルを生成
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileConfiguration rDDataYamlFile = YamlConfiguration.loadConfiguration(rDDataFile);

        rDDataYamlFile.set("RD", WeaponDamageRandomizer.randomDamage);

        //データ書き込み
        try {
            rDDataYamlFile.save(rDDataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static void initSaveSettings(File settingsFolder) {
        //指定されたフォルダがなかったら生成
        if(!settingsFolder.exists()) {
            if(!settingsFolder.mkdirs()){
                return;
            }
        }
        return;
    }

    public static double loadRD(){
        File rDDataFile = new File(rDDataFolder, "RandomDamage" + ".yml");
        FileConfiguration rDDataYamlFile = YamlConfiguration.loadConfiguration(rDDataFile);
        return rDDataYamlFile.getDouble("RD",0.0);
    }
}
