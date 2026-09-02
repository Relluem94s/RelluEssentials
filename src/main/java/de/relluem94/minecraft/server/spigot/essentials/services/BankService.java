package de.relluem94.minecraft.server.spigot.essentials.services;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_MONEY;

import de.relluem94.minecraft.server.spigot.essentials.builders.CustomItemBuilder;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.InventoryHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.StringHelper;
import de.relluem94.minecraft.server.spigot.essentials.models.RelluEssentialsNamespacedKey;
import de.relluem94.minecraft.server.spigot.essentials.models.items.CustomItem;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.BankAccountEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.BankTierEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.BankTransactionEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.registries.BankTierRegistry;
import de.relluem94.minecraft.server.spigot.essentials.repositories.BankRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

/**
 * Service handling all bank-related business logic. including deposits, withdrawals, account
 * upgrades and interest calculations.
 */
public class BankService {
  public static final Material UPGRADE_MATERIAL = Material.AMETHYST_SHARD;

  private final ServiceContext serviceContext;
  private final BankTierRegistry bankTierRegistry;
  private final BankRepository bankRepository;
  private final Map<UUID, BankAccountEntry> bankInterestMap = new HashMap<>();
  private final JavaPlugin plugin;

  /**
   * Creates a new BankService Instance.
   *
   * @param serviceContext   the shared service context
   * @param bankTierRegistry the in-memory registry of all bank tiers
   * @param bankRepository   the repository for bank persistence operations
   * @param plugin           the owning plugin instance
   */
  public BankService(ServiceContext serviceContext, BankTierRegistry bankTierRegistry,
      BankRepository bankRepository, JavaPlugin plugin) {
    this.serviceContext = serviceContext;
    this.bankTierRegistry = bankTierRegistry;
    this.bankRepository = bankRepository;
    this.plugin = plugin;
  }

  /**
   * Retrieves a specific bank item by its constant name.
   *
   * @param constantName the constant name of the item
   * @return the {@link CustomItem}, or {@code null} if not found
   */
  public @Nullable CustomItem getBankItem(String constantName) {
    return serviceContext.getItemService().find(
        new RelluEssentialsNamespacedKey(serviceContext.getPluginMetadataService().getName(),
            constantName)).orElse(null);
  }

  /**
   * Appends or replaces the second lore line of the given item stack.
   *
   * @param is   the item stack to modify
   * @param line the lore line to set
   * @return the modified item stack
   */
  public @NotNull ItemStack addLoreLine(@NotNull ItemStack is, String line) {
    ItemMeta im = is.getItemMeta();
    if (im == null) {
      return is;
    }

    List<String> lore = im.getLore() != null ? new ArrayList<>(im.getLore()) : new ArrayList<>();
    if (lore.size() == 1) {
      lore.add(line);
    } else {
      lore.set(1, line);
    }

    im.setLore(lore);
    is.setItemMeta(im);
    return is;
  }

  /**
   * Builds the list of upgrade items from all registered bank tiers.
   *
   * @return list of {@link CustomItem} representing each available bank tier upgrade
   */
  public @NonNull List<CustomItem> getBankTiers() {
    List<CustomItem> bankTierItems = new ArrayList<>();
    for (BankTierEntry bte : bankTierRegistry.getBankTiers()) {
      String lore1 = serviceContext.getTranslationService()
          .get(MessageKey.PLUGIN_EVENT_NPC_BANKER_COST_LORE, bte.getCost());
      String lore2 = serviceContext.getTranslationService()
          .get(MessageKey.PLUGIN_EVENT_NPC_BANKER_INTEREST_LORE, bte.getInterest());
      String lore3 = serviceContext.getTranslationService()
          .get(MessageKey.PLUGIN_EVENT_NPC_BANKER_LIMIT_LORE, bte.getLimit());

      CustomItem customItem = new CustomItemBuilder(
          new RelluEssentialsNamespacedKey(
              serviceContext.getPluginMetadataService().getName(),
              bte.getName()),
          UPGRADE_MATERIAL)
          .displayName(bte.getName())
          .type(CustomItem.Type.NPC_GUI)
          .rarity(CustomItem.Rarity.NONE)
          .lore(Arrays.asList(lore1, lore2, lore3))
          .addPersistentData(new NamespacedKey(plugin, "cost").toString(), String.valueOf(bte.getCost()))
          .build();

      bankTierItems.add(customItem);
    }
    return bankTierItems;
  }
  /**
   * Deposits a percentage of the player's purse into their bank account.
   *
   * @param pe         the player entry holding purse data
   * @param p          the online player
   * @param bae        the player's bank account
   * @param percentage the percentage of the purse to deposit (use 100 for all)
   */
  public void deposit(@NonNull PlayerEntry pe, Player p, BankAccountEntry bae, float percentage) {
    double purse = pe.getPurse();
    if (purse >= 1) {
      double transactionValue = (purse / 100) * percentage;

      if (bae.getTier().getLimit() >= transactionValue + bae.getValue()) {
        if (percentage == 100) {
          transactionValue = purse;
          pe.setPurse(0);
        } else {
          pe.setPurse(purse - transactionValue);
        }

        bankRepository.addTransactionToBank(pe.getId(), bae.getId(), transactionValue,
            bae.getValue(), bae.getTier().getId());

        pe.setUpdatedBy(pe.getId());
        pe.setHasToBeUpdated(true);

        p.playSound(p, "item.armor.equip_gold", SoundCategory.MASTER, 1f, 1f);
        p.sendMessage(serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BANKER_DEPOSIT_MESSAGE,
                StringHelper.formatDouble(transactionValue), PLUGIN_NAME_MONEY));
        p.sendMessage(serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BANKER_TOTAL,
                StringHelper.formatDouble(bae.getValue() + transactionValue), PLUGIN_NAME_MONEY));
      } else {
        transactionValue = bae.getTier().getLimit() - bae.getValue();
        if (transactionValue > 0) {
          pe.setPurse(purse - transactionValue);

          bankRepository.addTransactionToBank(pe.getId(), bae.getId(), transactionValue,
              bae.getValue(), bae.getTier().getId());

          pe.setUpdatedBy(pe.getId());
          pe.setHasToBeUpdated(true);

          p.playSound(p, "item.armor.equip_gold", SoundCategory.MASTER, 1f, 1f);
          p.sendMessage(serviceContext.getTranslationService()
              .getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BANKER_DEPOSIT_MESSAGE,
                  StringHelper.formatDouble(transactionValue), PLUGIN_NAME_MONEY));
          p.sendMessage(serviceContext.getTranslationService()
              .getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BANKER_TOTAL,
                  StringHelper.formatDouble(bae.getValue() + transactionValue), PLUGIN_NAME_MONEY));
        }

        p.sendMessage(serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BANKER_DEPOSIT_LIMIT_MESSAGE));
      }

      InventoryHelper.closeInventory(p);
    } else {
      p.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BANKER_DEPOSIT_NO_COINS_MESSAGE,
              PLUGIN_NAME_MONEY));
      p.playSound(p, "entity.villager.no", SoundCategory.MASTER, 1f, 1f);
      InventoryHelper.closeInventory(p);
    }
  }

  /**
   * Withdraws a percentage of the player's bank balance into their purse.
   *
   * @param pe         the player entry holding purse data
   * @param p          the online player
   * @param bae        the player's bank account
   * @param percentage the percentage of the bank balance to withdraw (use 100 for all)
   */
  public void withdraw(@NonNull PlayerEntry pe, Player p, @NonNull BankAccountEntry bae,
      float percentage) {
    double bank = bae.getValue();
    double purse = pe.getPurse();
    if (bank >= 1) {
      double transactionValue = (bank / 100) * percentage;

      if (percentage == 100) {
        transactionValue = bank;
      }

      pe.setPurse(purse + transactionValue);
      bankRepository.addTransactionToBank(pe.getId(), bae.getId(), transactionValue * -1,
          bae.getValue(), bae.getTier().getId());

      pe.setUpdatedBy(pe.getId());
      pe.setHasToBeUpdated(true);

      p.playSound(p, "item.armor.equip_gold", SoundCategory.MASTER, 1f, 1f);
      p.sendMessage(String.format(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BANKER_WITHDRAW_MESSAGE,
              StringHelper.formatDouble(transactionValue), PLUGIN_NAME_MONEY)));
      p.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BANKER_TOTAL,
              StringHelper.formatDouble(bae.getValue() - transactionValue), PLUGIN_NAME_MONEY));
      InventoryHelper.closeInventory(p);
    } else {
      p.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BANKER_NOT_ENOUGH_COINS, PLUGIN_NAME_MONEY));
      p.playSound(p, "entity.villager.no", SoundCategory.MASTER, 1f, 1f);
      InventoryHelper.closeInventory(p);
    }
  }

  /**
   * Processes a bank account tier upgrade purchase for the player. Payment is taken from the purse
   * first, then the bank, then both combined.
   *
   * @param itemStack the clicked upgrade item
   * @param p         the online player
   * @param pe        the player entry
   * @param bae       the player's current bank account
   */
  public void upgradeAccount(ItemStack itemStack, Player p, PlayerEntry pe, BankAccountEntry bae) {
    for (ItemHelper ih : getBankTiers()) {
      if (!ih.getCustomItem().equals(itemStack)) {
        continue;
      }

      long costs = 0;
      if (ih.hasData(new NamespacedKey(plugin, "cost"))) {
        costs = Long.parseLong(ih.getData(new NamespacedKey(plugin, "cost")));
      }

      BankTierEntry bt = getBankTierEntryByCost(costs);

      if (!isValidUpgrade(p, bae, bt, costs)) {
        return;
      }

      double purse = pe.getPurse();
      if (purse >= costs) {
        pe.setPurse(purse - costs);
        pe.setUpdatedBy(pe.getId());
        pe.setHasToBeUpdated(true);
        bankRepository.updateBankAccount(pe.getId(), 0f, bae.getValue(), bt.getId());
        p.sendMessage(serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BANKER_BUY_USING_PURSE));
        p.closeInventory();
        return;
      }

      double account = bae.getValue();
      if (account >= costs) {
        bankRepository.addTransactionToBank(pe.getId(), bae.getId(), -costs, bae.getValue(),
            bt.getId());
        p.sendMessage(serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BANKER_BUY_USING_BANK));
        p.closeInventory();
        return;
      }

      if (purse + account >= costs) {
        pe.setPurse(0);
        pe.setUpdatedBy(pe.getId());
        pe.setHasToBeUpdated(true);
        bankRepository.addTransactionToBank(pe.getId(), bae.getId(), -(costs - purse),
            bae.getValue(), bt.getId());
        p.sendMessage(serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BANKER_BUY_USING_BOTH));
        p.closeInventory();
        return;
      }

      p.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BANKER_NOT_ENOUGH_COINS, PLUGIN_NAME_MONEY));
    }
  }

  /**
   * Triggers interest calculation and payment for all currently online players.
   */
  public void triggerInterestForAllOnlinePlayers() {
    if (!Bukkit.getOnlinePlayers().isEmpty()) {
      for (Player p : Bukkit.getOnlinePlayers()) {
        checkInterest(p.getUniqueId(), true);
        payInterestToPlayer(p);
      }
    }
  }

  /**
   * Pays out any pending interest to the given player and removes them from the interest queue.
   *
   * @param p the online player to receive interest
   */
  public void payInterestToPlayer(@NonNull Player p) {
    if (!bankInterestMap.containsKey(p.getUniqueId())) {
      return;
    }

    BankAccountEntry bae = bankInterestMap.get(p.getUniqueId());
    double interest = (bae.getValue() / 100) * bae.getTier().getInterest();

    p.sendMessage(serviceContext.getTranslationService()
        .getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BANKER_INTEREST,
            StringHelper.formatDouble(interest), PLUGIN_NAME_MONEY));

    bankRepository.addTransactionToBank(bae.getPlayerId(), bae.getId(), interest, bae.getValue(),
        bae.getTier().getId());
    bankInterestMap.remove(p.getUniqueId());
  }

  /**
   * Checks whether a player is eligible for interest and queues it if so.
   *
   * @param uuid     the UUID of the player to check
   * @param midnight {@code true} if called at midnight (always queues interest), {@code false} to
   *                 check based on last played date
   */
  public void checkInterest(UUID uuid, boolean midnight) {
    OfflinePlayer op = Bukkit.getOfflinePlayer(uuid);

    if (!op.hasPlayedBefore()) {
      return;
    }

    PlayerEntry pe = serviceContext.getPlayerService().getPlayerEntry(uuid);
    if (pe == null) {
      return;
    }

    BankAccountEntry bae = bankRepository.findBankAccountByPlayerId(pe.getId());
    if (bae == null) {
      return;
    }

    if (midnight) {
      bankInterestMap.put(uuid, bae);
      return;
    }

    long lastPlayedTime = op.getLastPlayed() / 1000L;

    LocalDate localDate = LocalDate.now();
    ZonedDateTime startOfDayInZone = localDate.atStartOfDay(ZoneId.systemDefault());

    Date lastPlayedDate = new Date(lastPlayedTime * 1000L);
    Date todayDate = new Date(startOfDayInZone.toInstant().toEpochMilli());

    if (lastPlayedDate.before(todayDate)) {
      bankInterestMap.put(uuid, bae);
    }
  }

  /**
   * Finds a bank tier entry by its cost value.
   *
   * @param costs the cost of the tier to look up
   * @return the matching {@link BankTierEntry}, or {@code null} if none found
   */
  public @Nullable BankTierEntry getBankTierEntryByCost(long costs) {
    for (BankTierEntry bte : bankTierRegistry.getBankTiers()) {
      if (bte.getCost() == costs) {
        return bte;
      }
    }
    return null;
  }

  /**
   * Retrieves the bank account for the given player id from the repository.
   *
   * @param playerId the internal player id
   * @return the {@link BankAccountEntry}, or {@code null} if not found
   */
  public @Nullable BankAccountEntry findBankAccountByPlayerId(int playerId) {
    return bankRepository.findBankAccountByPlayerId(playerId);
  }

  /**
   * Inserts a new bank account via the repository.
   *
   * @param bae the bank account to insert
   */
  public void insertBankAccount(@NonNull BankAccountEntry bae) {
    bankRepository.insertBankAccount(bae);
  }

  /**
   * Retrieves all transactions for the given bank account.
   *
   * @param bankAccountId the bank account id
   * @return list of transaction entries
   */
  public List<BankTransactionEntry> findTransactionsByBankAccountId(int bankAccountId) {
    return bankRepository.findTransactionsByBankAccountId(bankAccountId);
  }

  private boolean isValidUpgrade(Player p, BankAccountEntry bae, BankTierEntry bt, long costs) {
    if (bt == null) {
      return false;
    }

    if (bae.getTier().getCost() == costs) {
      p.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BANKER_BUY_ALREADY_BOUGHT));
      return false;
    }

    if (bt.getId() == bae.getTier().getId()) {
      p.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BANKER_BUY_ALREADY_BOUGHT));
      return false;
    }

    if (bae.getTier().getCost() > costs) {
      p.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BANKER_BUY_LOWER_ACCOUNT));
      return false;
    }

    return true;
  }

  /**
   * Finds a bank tier entry by its unique id.
   *
   * @param id the primary key of the bank tier to look up
   * @return the matching {@link BankTierEntry}, or {@code null} if none found
   */
  public @Nullable BankTierEntry getBankTierEntryById(int id) {
    return bankTierRegistry.getBankTierById(id);
  }
}