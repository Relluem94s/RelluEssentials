package de.relluem94.minecraft.server.spigot.essentials.contexts;

import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.BagDao;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.CropDao;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.DropDao;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.LocationDao;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.LocationTypeDao;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.NpcDao;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.PlayerDao;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.PluginInformationDao;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.ProtectionDao;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.SettingDao;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.WorldGroupDao;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PersistenceContext {

  private CropDao cropDao;
  private DropDao dropDao;
  private LocationDao locationDao;
  private LocationTypeDao locationTypeDao;
  private NpcDao npcDao;
  private PlayerDao playerDao;
  private PluginInformationDao pluginInformationDao;
  private ProtectionDao protectionDao;
  private SettingDao settingDao;
  private WorldGroupDao worldGroupDao;
  private BagDao bagDao;
}