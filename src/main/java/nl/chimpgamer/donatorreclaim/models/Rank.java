package nl.chimpgamer.donatorreclaim.models;

import java.util.List;

public class Rank {
    private final String name, permission;
    private final List<String> commands, upgradeCommands;

    public Rank(String name, String permission, List<String> commands, List<String> upgradeCommands) {
        this.name = name;
        this.permission = permission;
        this.commands = commands;
        this.upgradeCommands = upgradeCommands;
    }

    public String getName() {
        return name;
    }

    public String getPermission() {
        return permission;
    }

    public List<String> getCommands() {
        return commands;
    }

    public List<String> getUpgradeCommands() {
        return upgradeCommands;
    }

    public boolean canUpgrade() {
        return upgradeCommands == null;
    }
}