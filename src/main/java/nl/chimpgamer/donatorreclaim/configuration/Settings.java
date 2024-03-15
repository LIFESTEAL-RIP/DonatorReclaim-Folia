package nl.chimpgamer.donatorreclaim.configuration;

import nl.chimpgamer.donatorreclaim.DonatorReclaim;
import nl.chimpgamer.donatorreclaim.models.Rank;
import nl.chimpgamer.donatorreclaim.utils.FileUtils;
import nl.chimpgamer.donatorreclaim.utils.Utils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Settings extends FileUtils {
    private final DonatorReclaim donatorReclaim;

    public Settings(DonatorReclaim donatorReclaim) {
        super(donatorReclaim.getDataFolder().getPath(), "settings.yml");
        this.donatorReclaim = donatorReclaim;
    }

    public void load() {
        setupFile();
    }

    public boolean isOnlyReclaimHighestRank() {
        return this.getBoolean("onlyReclaimHighestRank");
    }

    public boolean fullRedeemWhenNotInOrder() {
        return this.getBoolean("fullRedeemWhenNotInOrder");
    }

    public boolean executeRedeemOnFirstJoin() {
        return this.getBoolean("executeRedeemOnFirstJoin");
    }

    @Nullable
    public Rank getHighestAvailableRank(Player player) {
        Rank rank = null;
        for (Rank rank1 : this.getRanks()) {
            if (player.hasPermission(rank1.getPermission())) {
                rank = rank1;
            }
        }
        return rank;
    }

    @NotNull
    public List<Rank> getAvailableRanks(Player player) {
        return getRanks().stream().filter(rank -> player.hasPermission(rank.getPermission())).collect(Collectors.toList());
    }

    @NotNull
    public List<Rank> getRanks() {
        List<Rank> ranks = new ArrayList<>();
        ConfigurationSection section = this.getConfig().getConfigurationSection("ranks");
        if (section == null) {
            return ranks;
        }
        for (String key : section.getKeys(false)) {
            String permission = section.getString(key + ".permission");
            List<String> commands = section.getStringList(key + ".commands");
            List<String> upgradeCommands = section.getStringList(key + ".upgrade-commands");
            Rank rank = new Rank(Utils.capitalize(key), permission, commands, upgradeCommands);
            ranks.add(rank);
        }
        return ranks;
    }

    @Nullable
    public Rank getRank(String rankName) {
        return this.getRanks().stream().filter(rank -> rank.getName().equalsIgnoreCase(rankName)).findFirst().orElse(null);
    }

    private void setupFile() {
        if (!this.getFile().exists()) {
            try {
                this.saveToFile(donatorReclaim.getResource("settings.yml"));
                this.reload();
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