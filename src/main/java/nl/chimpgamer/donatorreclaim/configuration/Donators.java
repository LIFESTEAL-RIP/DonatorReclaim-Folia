package nl.chimpgamer.donatorreclaim.configuration;

import nl.chimpgamer.donatorreclaim.DonatorReclaim;
import nl.chimpgamer.donatorreclaim.models.Rank;
import nl.chimpgamer.donatorreclaim.utils.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Donators extends FileUtils {
    private final DonatorReclaim donatorReclaim;

    public Donators(DonatorReclaim donatorReclaim) {
        super(donatorReclaim.getDataFolder().getPath(), "donators.yml");
        this.donatorReclaim = donatorReclaim;
    }

    public void load() {
        setupFile();
    }

    public void executeReclaim(Player player, Rank rank) {
        if (this.hasRedeemed(player, rank)) {
            player.sendMessage("&6You've already claimed your rank.");
            return;
        }

        if (rank.canUpgrade()) {

        }
    }

    public boolean donatorExists(OfflinePlayer player) {
        ConfigurationSection section = this.getConfig().getConfigurationSection("donators");
        return section != null && section.getKeys(false).contains(player.getUniqueId().toString());
    }

    @NotNull
    public List<String> hasRedeemed(OfflinePlayer player) {
        if (!donatorExists(player)) {
            return new ArrayList<>();
        }
        return this.getStringList("donators." + player.getUniqueId().toString());
    }

    public boolean hasRedeemed(Player player, Rank rank) {
        if (rank == null) {
            return true;
        }
        return this.hasRedeemed(player).contains(rank.getName().toLowerCase());
    }

    public void redeemRank(Player player, Rank rank) {
        donatorReclaim.getFoliaLib().getImpl().runNextTick((task) -> {
            CommandSender commandSender = Bukkit.getConsoleSender();

            rank.getCommands().forEach(command -> Bukkit.dispatchCommand(commandSender, command
                    .replace("%playername%", player.getName())
                    .replace("%rank%", rank.getName())));
        });

        List<String> redeemed = this.hasRedeemed(player);
        redeemed.add(rank.getName().toLowerCase());
        this.set("donators." + player.getUniqueId().toString(), redeemed);
        this.save();
    }

    // Remove unused code.
    public void upgradeRank(Player player, Rank rank) {
        CommandSender commandSender = donatorReclaim.getServer().getConsoleSender();
        rank.getUpgradeCommands().forEach(command -> donatorReclaim.getServer().dispatchCommand(commandSender, command
                .replace("%playername%", player.getName())
                .replace("%rank%", rank.getName())));

        List<String> redeemed = this.hasRedeemed(player);
        redeemed.add(rank.getName().toLowerCase());
        this.set("donators." + player.getUniqueId().toString(), redeemed);
        this.save();
    }

    public void removeRank(OfflinePlayer player, Rank rank) {
        List<String> redeemed = this.hasRedeemed(player);
        redeemed.remove(rank.getName().toLowerCase());
        this.set("donators." + player.getUniqueId().toString(), redeemed);
        this.save();
    }

    public void reset(UUID uuid) {
        this.set("donators." + uuid.toString(), null);
        this.save();
    }

    public void resetAll() {
        this.set("donators", null);
        this.save();
    }

    private void setupFile() {
        if (!this.getFile().exists()) {
            try {
                this.saveToFile(donatorReclaim.getResource("donators.yml"));
            } catch (NullPointerException ex) {
                try {
                    this.getFile().createNewFile();
                } catch (IOException ex1) {
                    ex1.printStackTrace();
                }
            }
        }
    }
}