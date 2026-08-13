package de.relluem94.minecraft.server.spigot.essentials.contexts;

import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.LocationDao;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.NpcDao;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.PlayerDao;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.ProtectionDao;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PersistenceContext {

  private NpcDao npcDao;
  private LocationDao locationDao;
  private ProtectionDao protectionDao;
  private PlayerDao playerDao;

}