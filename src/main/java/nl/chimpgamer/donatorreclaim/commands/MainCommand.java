package nl.chimpgamer.donatorreclaim.commands;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.optional.OptionalArg;
import dev.rollczi.litecommands.annotations.permission.Permission;
import nl.chimpgamer.donatorreclaim.DonatorReclaim;
import nl.chimpgamer.donatorreclaim.configuration.Message;
import nl.chimpgamer.donatorreclaim.models.Rank;
import nl.chimpgamer.donatorreclaim.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

@Command(name = "reclaim")
@Permission("donatorreclaim.commands.reclaim")
public class MainCommand {
    private final DonatorReclaim donatorReclaim;

    public MainCommand(DonatorReclaim donatorReclaim) {
        this.donatorReclaim = donatorReclaim;
    }

    @Execute()
    public void reclaimCommand(@Context Player player) {
        if (donatorReclaim.getSettings().isOnlyReclaimHighestRank()) {
            Rank rank = donatorReclaim.getSettings().getHighestAvailableRank(player);
            if (rank == null) {
                player.sendMessage(donatorReclaim.getMessages().getString(Message.NOTHINGTORECLAIM));
                return;
            }

            if (donatorReclaim.getDonators().hasRedeemed(player, rank)) {
                player.sendMessage(donatorReclaim.getMessages().getString(Message.ALREADYCLAIMEDRANK)
                        .replace("%rank%", rank.getName()));
            }
            else {
                donatorReclaim.getDonators().redeemRank(player, rank);
                player.sendMessage(donatorReclaim.getMessages().getString(Message.SUCCESSFULLYRECLAIMEDRANK)
                        .replace("%rank%", rank.getName()));
            }
        }
        else {
            for (Rank rank : donatorReclaim.getSettings().getAvailableRanks(player)) {
                if (donatorReclaim.getDonators().hasRedeemed(player, rank)) {
                    player.sendMessage(donatorReclaim.getMessages().getString(Message.ALREADYCLAIMEDRANK)
                            .replace("%rank%", rank.getName()));
                }
                else {
                    donatorReclaim.getDonators().redeemRank(player, rank);
                    player.sendMessage(donatorReclaim.getMessages().getString(Message.SUCCESSFULLYRECLAIMEDRANK)
                            .replace("%rank%", rank.getName()));
                }
            }
        }
    }

    @Execute(name = "reload")
    public void reloadCommand(@Context CommandSender sender) {
        if (sender.hasPermission("donatorreclaim.commands.reclaim.reload")) {
            donatorReclaim.getSettings().reload();
            donatorReclaim.getMessages().reload();

            sender.sendMessage(donatorReclaim.getMessages().getString(Message.RELOAD));
        }
        else {
            sender.sendMessage(donatorReclaim.getMessages().getString(Message.NOPERMISSION));
        }
    }

    @Execute(name = "reset")
    public void resetCommand(@Context Player sender, @Arg("all/player") String player, @OptionalArg("rank") String rankName) {
        if (!sender.hasPermission("donatorreclaim.commands.reclaim.reset")) {
            sender.sendMessage(donatorReclaim.getMessages().getString(Message.NOPERMISSION));
        }

        if (player.equalsIgnoreCase("all")) {
            donatorReclaim.getDonators().resetAll();
            return;
        }

        final OfflinePlayer target;
        if (Utils.isUUID(player)) {
            target = Bukkit.getOfflinePlayer(UUID.fromString(player));
        }
        else {
            target = Bukkit.getPlayer(player);
        }

        if (target == null) {
            sender.sendMessage(donatorReclaim.getMessages().getString(Message.PLAYEROFFLINE)
                    .replace("%player%", player));

            return;
        }

        if (rankName != null) {
                Rank rank = donatorReclaim.getSettings().getRank(rankName);
                if (rank == null) {
                    sender.sendMessage(donatorReclaim.getMessages().getString(Message.RECLAIMRANKINVALID)
                            .replace("%rank%", rankName));

                    return;
                }

                donatorReclaim.getDonators().removeRank(target, rank);
        }
        else {
                donatorReclaim.getDonators().reset(target.getUniqueId());
        }
    }

    @Execute(name = "help")
    public void helpCommand(@Context CommandSender sender) {
        sender.sendMessage(Utils.formatColorCodes("      Donator Reclaim Help       "));
        sender.sendMessage(Utils.formatColorCodes("&8- &6/reclaim"));
        sender.sendMessage(Utils.formatColorCodes("&8- &6/reclaim reset <all/player> [rank]"));
        sender.sendMessage(Utils.formatColorCodes("&8- &6/reclaim reload"));
    }
}
