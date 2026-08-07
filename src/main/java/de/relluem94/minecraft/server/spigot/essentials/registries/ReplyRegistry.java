package de.relluem94.minecraft.server.spigot.essentials.registries;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Player;

public class ReplyRegistry {

  private final Map<Player, Player> replyMap = new HashMap<>();

  public void register(Player sender, Player target) {
    replyMap.remove(sender);
    replyMap.remove(target);
    replyMap.put(sender, target);
    replyMap.put(target, sender);
  }

  public Player findReplyTarget(Player sender) {
    return replyMap.get(sender);
  }

  public boolean hasReplyTarget(Player sender) {
    return replyMap.containsKey(sender);
  }

  public void unregister(Player player) {
    replyMap.remove(player);
  }
}