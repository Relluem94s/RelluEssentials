package de.relluem94.minecraft.server.spigot.essentials.contexts;

import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.NpcDao;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.QueryExecutor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PersistenceContext {

  private QueryExecutor queryExecutor;
  private NpcDao npcDao;

}