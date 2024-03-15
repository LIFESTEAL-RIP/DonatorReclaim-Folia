package nl.chimpgamer.donatorreclaim;

import nl.chimpgamer.donatorreclaim.commands.ReclaimCommand;
import nl.chimpgamer.donatorreclaim.configuration.Donators;
import nl.chimpgamer.donatorreclaim.configuration.Messages;
import nl.chimpgamer.donatorreclaim.configuration.Settings;
import nl.chimpgamer.donatorreclaim.listeners.JoinListener;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public final class DonatorReclaim extends JavaPlugin {
    private Settings settings;
    private Messages messages;
    private Donators donators;

    @Override
    public void onEnable() {
        // Plugin startup logic

        getDataFolder().mkdirs();
        this.initSettings();
        this.initMessages();
        this.initDonators();

        this.getCommand("reclaim").setExecutor(new ReclaimCommand(this));
        this.getServer().getPluginManager().registerEvents(new JoinListener(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        this.settings = null;
        this.messages = null;
        this.donators = null;
        HandlerList.unregisterAll(this);
    }

    private void initSettings() {
        this.settings = new Settings(this);
        this.settings.load();
    }

    private void initMessages() {
        this.messages = new Messages(this);
        this.messages.load();
    }

    private void initDonators() {
        this.donators = new Donators(this);
        this.donators.load();
    }

    public Settings getSettings() {
        return settings;
    }

    public Messages getMessages() {
        return messages;
    }

    public Donators getDonators() {
        return donators;
    }
}