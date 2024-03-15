package nl.chimpgamer.donatorreclaim.configuration;

public enum Message {

    ALREADYCLAIMEDRANK("alreadyClaimedRank", "&6You've already reclaimed the rewards for your &e%rank% &6rank."),
    SUCCESSFULLYRECLAIMEDRANK("successfullyReclaimedRank", "&6You've successfully reclaimed the rewards for your &e%rank% &6rank."),
    RECLAIMRANKINVALID("reclaimRankInvalid", "&6The rank &e%rank &6does not exist in the config!"),
    RECLAIMJOINNOTIFICATION("reclaimJoinNotification", "&6You have unclaimed rank rewards. Claim them now with &e/reclaim!"),
    NOTHINGTORECLAIM("nothingToReclaim", "&6You have nothing to reclaim at the moment."),
    PLAYEROFFLINE("playerOffline", "&6The player &e%player% &6is offline."),
    NOPERMISSION("noPermission", "&cYou have no permission to use this command!"),
    RELOAD("reload", "&6Successfully reloaded the config and messages files.");

    private final String key, defaultValue;

    Message(String key, String defaultValue) {
        this.key = key;
        this.defaultValue = defaultValue;
    }

    public String getKey() {
        return key;
    }

    public String getDefaultValue() {
        return defaultValue;
    }
}