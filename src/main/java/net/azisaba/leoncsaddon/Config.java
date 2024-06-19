package net.azisaba.leoncsaddon;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

public class Config {

    protected final File folder;
    private final String name;
    protected YamlConfiguration config = new YamlConfiguration();

    public Config(File folder, String name){

        this.folder = folder;
        this.name = name;

        init();
    }

    public void init(){

        try {
            if (exists()) {
                config.loadFromString(loadAsString());
            } else if (existsResource()) {
                config.loadFromString(loadResourceAsString());
                saveResource();
            }
        } catch (InvalidConfigurationException exception) {
            exception.printStackTrace();
        }

        if(config == null){
            ErrorCode.C0001.log();
            return;
        }

    }

    public void load(){}

    public String loadAsString(){
        try {
            return Files.lines(getPath()).collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException exception) {
            exception.printStackTrace();
            return "";
        }
    }

    public String loadResourceAsString() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(getResource(), StandardCharsets.UTF_8));
        return reader.lines().collect(Collectors.joining(System.lineSeparator()));
    }

    public InputStream getResource() {
        return LeonCSAddon.INSTANCE.getClass().getClassLoader().getResourceAsStream("configs/" + name);
    }

    public Path getPath() {
        return LeonCSAddon.INSTANCE.getDataFolder().toPath().resolve(name);
    }

    public boolean exists() {
        return Files.isRegularFile(getPath());
    }

    public boolean existsResource() {
        return getResource() != null;
    }

    public void saveResource() {
        try {
            Files.createDirectories(getPath().getParent());
            Files.copy(getResource(), getPath());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void saveConfig() {
        try {
            Files.createDirectories(getPath().getParent());
            Files.write(getPath(), config.saveToString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
