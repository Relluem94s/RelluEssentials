package de.relluem94.minecraft.server.spigot.essentials.services;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.PlayerPartnerEntry;
import de.relluem94.minecraft.server.spigot.essentials.registry.PlayerRegistry;
import java.util.List;
import java.util.UUID;

public class PlayerService {

  private final PlayerRegistry playerRegistry;

  public PlayerService(PlayerRegistry playerRegistry) {
    this.playerRegistry = playerRegistry;
  }

  public PlayerPartnerEntry getPartner(PlayerEntry playerEntry) {
    if (playerEntry.getPartner() == null) {
      return RelluEssentials.getInstance().getDatabaseHelper().getPlayerPartner(playerEntry.getId());
    }
    return playerEntry.getPartner();
  }

  public void reloadPlayerHomes() {
    List<PlayerEntry> playerEntries = RelluEssentials.getInstance().getDatabaseHelper().getPlayers();

    playerRegistry.clearPlayerEntries();

    playerEntries.forEach(playerEntry ->
        playerRegistry.putPlayerEntry(UUID.fromString(playerEntry.getUuid()), playerEntry));
  }
}