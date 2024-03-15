package nl.chimpgamer.donatorreclaim.listeners;

import nl.chimpgamer.donatorreclaim.DonatorReclaim;
import nl.chimpgamer.donatorreclaim.models.Rank;
import nl.chimpgamer.donatorreclaim.configuration.Message;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {
    private final DonatorReclaim donatorReclaim;

    public JoinListener(DonatorReclaim donatorReclaim) {
        this.donatorReclaim = donatorReclaim;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        if (donatorReclaim.getSettings().executeRedeemOnFirstJoin()) {
            if (player.hasPlayedBefore()) {
                return;
            }
            if (donatorReclaim.getSettings().isOnlyReclaimHighestRank()) {
                Rank rank = donatorReclaim.getSettings().getHighestAvailableRank(player);

                if (rank == null) return;
                donatorReclaim.getDonators().redeemRank(player, rank);
                player.sendMessage(donatorReclaim.getMessages().getString(Message.SUCCESSFULLYRECLAIMEDRANK)
                        .replace("%rank%", rank.getName()));
            } else {
                for (Rank rank : donatorReclaim.getSettings().getAvailableRanks(player)) {
                    if (!donatorReclaim.getDonators().hasRedeemed(player, rank)) {
                        donatorReclaim.getDonators().redeemRank(player, rank);
                        player.sendMessage(donatorReclaim.getMessages().getString(Message.SUCCESSFULLYRECLAIMEDRANK)
                                .replace("%rank%", rank.getName()));
                    }
                }
            }
            return;
        }

        if (!donatorReclaim.getDonators().hasRedeemed(player, donatorReclaim.getSettings().getHighestAvailableRank(player))) {
            player.sendMessage(donatorReclaim.getMessages().getString(Message.RECLAIMJOINNOTIFICATION));
        }
    }
}