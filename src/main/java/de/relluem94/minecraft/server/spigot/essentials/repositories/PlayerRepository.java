package de.relluem94.minecraft.server.spigot.essentials.repositories;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerPartnerEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.PlayerDao;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class PlayerRepository {

  private final PlayerDao playerDao;

  public PlayerRepository(PlayerDao playerDao) {
    this.playerDao = playerDao;
  }

  public List<PlayerEntry> findAll() {
    return playerDao.findAll();
  }

  public PlayerEntry findByUuid(@NotNull String uuid) {
    return playerDao.findByUuid(uuid);
  }

  public void save(@NotNull PlayerEntry playerEntry) {
    playerDao.insert(playerEntry);
  }

  public void update(@NotNull PlayerEntry playerEntry) {
    playerDao.update(playerEntry);
  }

  public PlayerPartnerEntry findPartnerByPlayerId(int playerId) {
    return playerDao.findPartnerByPlayerId(playerId);
  }

  public void savePartner(@NotNull PlayerPartnerEntry partnerEntry) {
    playerDao.insertPartner(partnerEntry);
  }

  public void deletePartner(@NotNull PlayerPartnerEntry partnerEntry) {
    playerDao.deletePartner(partnerEntry);
  }

  public void updatePartner(@NotNull PlayerPartnerEntry partnerEntry) {
    playerDao.updatePartner(partnerEntry);
  }
}