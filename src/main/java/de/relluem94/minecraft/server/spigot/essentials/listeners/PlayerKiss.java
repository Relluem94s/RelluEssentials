package de.relluem94.minecraft.server.spigot.essentials.listeners;

import de.relluem94.minecraft.server.spigot.essentials.annotations.ListenerName;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.jspecify.annotations.NonNull;

@ListenerName("PlayerKiss")
public class PlayerKiss implements ListenerConstruct {

  private ServiceContext serviceContext;

  @Override
  public void injectContext(ServiceContext context) {
    this.serviceContext = context;
  }

  @EventHandler
  public void onPlayerKiss(@NonNull PlayerInteractEntityEvent e) {
    if (!(e.getRightClicked() instanceof Player clickedPlayer)) {
      return;
    }

    Player initiatingPlayer = e.getPlayer();

    boolean playersAreMarried = isMarried(initiatingPlayer, clickedPlayer);
    if (!playersAreMarried) {
      return;
    }

    displayKissParticles(initiatingPlayer, clickedPlayer);
  }

  private boolean isMarried(Player firstPlayer, Player secondPlayer) {
    var firstPlayerEntry = serviceContext.getPlayerService().getPlayerEntry(firstPlayer);
    var secondPlayerEntry = serviceContext.getPlayerService().getPlayerEntry(secondPlayer);

    if (firstPlayerEntry.getPartner() == null || secondPlayerEntry.getPartner() == null) {
      return false;
    }

    var partnerEntry = firstPlayerEntry.getPartner();
    return (partnerEntry.getFirstPartnerId() == firstPlayerEntry.getId()
        && partnerEntry.getSecondPartnerId() == secondPlayerEntry.getId())
        || (partnerEntry.getFirstPartnerId() == secondPlayerEntry.getId()
        && partnerEntry.getSecondPartnerId() == firstPlayerEntry.getId());
  }

  private void displayKissParticles(@NonNull Player firstPlayer, @NonNull Player secondPlayer) {
    firstPlayer.spawnParticle(org.bukkit.Particle.HEART, firstPlayer.getLocation().add(0, 2, 0), 30,
        0.5, 1.0, 0.5, 0.1);
    secondPlayer.spawnParticle(org.bukkit.Particle.HEART, secondPlayer.getLocation().add(0, 2, 0),
        30, 0.5, 1.0, 0.5, 0.1);
  }
}