package nl.chimpgamer.donatorreclaim.commands;

import nl.chimpgamer.donatorreclaim.DonatorReclaim;
import nl.chimpgamer.donatorreclaim.configuration.Message;
import nl.chimpgamer.donatorreclaim.models.Rank;
import nl.chimpgamer.donatorreclaim.utils.Utils;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ReclaimCommand implements CommandExecutor {
    private final DonatorReclaim donatorReclaim;

    public ReclaimCommand(DonatorReclaim donatorReclaim) {
        this.donatorReclaim = donatorReclaim;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        Player player = (Player) sender;
        if (args.length == 0) {
            if (donatorReclaim.getSettings().isOnlyReclaimHighestRank()) {
                Rank rank = donatorReclaim.getSettings().getHighestAvailableRank(player);
                if (rank == null) {
                    player.sendMessage(donatorReclaim.getMessages().getString(Message.NOTHINGTORECLAIM));
                    return true;
                }
                if (donatorReclaim.getDonators().hasRedeemed(player, rank)) {
                    player.sendMessage(donatorReclaim.getMessages().getString(Message.ALREADYCLAIMEDRANK)
                            .replace("%rank%", rank.getName()));
                } else {
                    donatorReclaim.getDonators().redeemRank(player, rank);
                    player.sendMessage(donatorReclaim.getMessages().getString(Message.SUCCESSFULLYRECLAIMEDRANK)
                            .replace("%rank%", rank.getName()));
                }
            } else {
                for (Rank rank : donatorReclaim.getSettings().getAvailableRanks(player)) {
                    if (donatorReclaim.getDonators().hasRedeemed(player, rank)) {
                        player.sendMessage(donatorReclaim.getMessages().getString(Message.ALREADYCLAIMEDRANK)
                                .replace("%rank%", rank.getName()));
                    } else {
                        donatorReclaim.getDonators().redeemRank(player, rank);
                        player.sendMessage(donatorReclaim.getMessages().getString(Message.SUCCESSFULLYRECLAIMEDRANK)
                                .replace("%rank%", rank.getName()));
                    }
                }
            }
            return true;
        } else {
            if (args[0].equalsIgnoreCase("reset") && args.length >= 2) {
                if (sender.hasPermission("donatorreclaim.commands.reclaim.reset")) {
                    if (args[1].equalsIgnoreCase("all")) {
                        donatorReclaim.getDonators().resetAll();
                        return true;
                    }
                    OfflinePlayer target;
                    if (Utils.isUUID(args[1])) {
                        target = donatorReclaim.getServer().getOfflinePlayer(UUID.fromString(args[1]));
                    } else {
                        target = donatorReclaim.getServer().getPlayer(args[1]);
                    }
                    if (target == null) {
                        player.sendMessage(donatorReclaim.getMessages().getString(Message.PLAYEROFFLINE)
                                .replace("%player%", args[1]));
                        return true;
                    }

                    if (args.length == 3) {
                        String rankName = args[2];
                        Rank rank = donatorReclaim.getSettings().getRank(rankName);
                        if (rank == null) {
                            sender.sendMessage(donatorReclaim.getMessages().getString(Message.RECLAIMRANKINVALID)
                                    .replace("%rank%", rankName));
                            return true;
                        }
                        donatorReclaim.getDonators().removeRank(target, rank);
                    } else {
                        donatorReclaim.getDonators().reset(target.getUniqueId());
                    }
                } else {
                    sender.sendMessage(donatorReclaim.getMessages().getString(Message.NOPERMISSION));
                }
                return true;
            } else if (args[0].equalsIgnoreCase("reload")) {
                if (sender.hasPermission("donatorreclaim.commands.reclaim.reload")) {
                    donatorReclaim.getSettings().reload();
                    donatorReclaim.getMessages().reload();
                    sender.sendMessage(donatorReclaim.getMessages().getString(Message.RELOAD));
                } else {
                    sender.sendMessage(donatorReclaim.getMessages().getString(Message.NOPERMISSION));
                }
                return true;
            } else {
                sender.sendMessage("      Donator Reclaim Help       ");
                sender.sendMessage("&8- &6/reclaim");
                sender.sendMessage("&8- &6/reclaim reset <all/player> [rank]");
                sender.sendMessage("&8- &6/reclaim reload");
            }
        }
        return false;
    }
}