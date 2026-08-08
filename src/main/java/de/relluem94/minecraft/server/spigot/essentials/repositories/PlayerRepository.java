package de.relluem94.minecraft.server.spigot.essentials.repositories;

import de.relluem94.minecraft.server.spigot.essentials.helpers.DatabaseHelper;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerPartnerEntry;
import java.util.List;

public class PlayerRepository {
  private final DatabaseHelper databaseHelper;

  public PlayerRepository(DatabaseHelper databaseHelper) {
    this.databaseHelper = databaseHelper;
  }

  public List<PlayerEntry> findAll() {
    return databaseHelper.getPlayers();
  }

  public void update(PlayerEntry playerEntry) {
    databaseHelper.updatePlayer(playerEntry);
  }

  public PlayerPartnerEntry findPartnerByPlayerId(int playerId) {
    return databaseHelper.getPlayerPartner(playerId);
  }
}