package nl.chimpgamer.donatorreclaim.utils;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;
import java.util.Set;

public class FileUtils {
    private final File file;
    private FileConfiguration config;

    public FileUtils(String path, String file) {
        this.file = new File(path, file);
        this.config = YamlConfiguration.loadConfiguration(this.file);
    }

    public FileUtils addDefault(String path, Object value) {
        this.config.addDefault(path, value);
        return this;
    }

    public FileUtils saveToFile(InputStream in) {
        try {
            Files.copy(in, file.toPath());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return this;
    }

    public FileUtils copyDefaults(boolean copyDefaults) {
        this.config.options().copyDefaults(copyDefaults);
        return this;
    }

    public Object getConfigValue(String path) {
        if (file == null || !file.exists() || config == null) return null;
        return config.get(path);
    }

    public boolean hasConfigValue(String path) {
        if (file == null || !file.exists() || config == null) return false;
        return config.contains(path);
    }

    public <K> K getConfigValue(String path, Class<K> clazz) {
        if (file == null || !file.exists() || config == null) return null;
        return (K) config.get(path);
    }

    public void setConfigValue(String path, Object value) {
        if (file == null || !file.exists()) return;

        this.config = YamlConfiguration.loadConfiguration(this.file);
        if (this.config == null) return;

        try {
            config.set(path, value);
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileUtils set(String path, Object value) {
        this.config.set(path, value);
        return this;
    }

    public String getString(String path) {
        return this.getConfig().getString(path);
    }

    public String getString(String path, String def) {
        return this.getConfig().getString(path, def);
    }

    public List<String> getStringList(String path) {
        return this.getConfig().getStringList(path);
    }

    public boolean contains(String path) {
        return this.getConfig().contains(path);
    }

    public int getInt(String path) {
        return this.getConfig().getInt(path);
    }

    public boolean getBoolean(String path) {
        return this.getConfig().getBoolean(path);
    }

    public long getLong(String path) {
        return this.getConfig().getLong(path);
    }

    public FileUtils save() {
        try {
            this.config.save(this.file);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return this;
    }

    public File getFile() {
        return this.file;
    }

    public void reload() {
        try {
            this.config.load(this.file);
        } catch (InvalidConfigurationException | IOException ex) {
            ex.printStackTrace();
        }
    }

    public Set<String> getKeys(boolean deep) {
        return this.config.getKeys(deep);
    }

    public Object get(String key) {
        return this.config.get(key);
    }

    public double getDouble(String key) {
        return this.config.getDouble(key);
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void delete() {
        this.file.delete();
    }
}