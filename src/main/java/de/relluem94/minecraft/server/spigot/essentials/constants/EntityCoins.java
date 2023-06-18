package de.relluem94.minecraft.server.spigot.essentials.constants;

public enum EntityCoins {
  /**
   * Reg Ex to Replace all () to compare and add new Entries
   * \((.*?)\)
   */
  DROPPED_ITEM(0),
  EXPERIENCE_ORB(0),
  AREA_EFFECT_CLOUD(0),
  ELDER_GUARDIAN(500),
  WITHER_SKELETON(200),
  STRAY(20),
  EGG(0),
  LEASH_HITCH(0),
  PAINTING(0),
  ARROW(0),
  SNOWBALL(0),
  FIREBALL(0),
  SMALL_FIREBALL(0),
  ENDER_PEARL(0),
  ENDER_SIGNAL(0),
  SPLASH_POTION(0),
  THROWN_EXP_BOTTLE(0),
  ITEM_FRAME(0),
  WITHER_SKULL(0),
  PRIMED_TNT(0),
  FALLING_BLOCK(0),
  FIREWORK(0),
  HUSK(20),
  SPECTRAL_ARROW(0),
  SHULKER_BULLET(0),
  DRAGON_FIREBALL(0),
  ZOMBIE_VILLAGER(0),
  SKELETON_HORSE(0),
  ZOMBIE_HORSE(0),
  ARMOR_STAND(0),
  DONKEY(0),
  MULE(0),
  EVOKER_FANGS(0),
  EVOKER(100),
  VEX(100),
  VINDICATOR(100),
  ILLUSIONER(100),
  MINECART_COMMAND(0),
  BOAT(0),
  MINECART(0),
  MINECART_CHEST(0),
  MINECART_FURNACE(0),
  MINECART_TNT(0),
  MINECART_HOPPER(0),
  MINECART_MOB_SPAWNER(0),
  CREEPER(25),
  SKELETON(15),
  SPIDER(15),
  GIANT(100),
  ZOMBIE(10),
  SLIME(5),
  GHAST(1250),
  ZOMBIFIED_PIGLIN(150),
  ENDERMAN(100),
  CAVE_SPIDER(15),
  SILVERFISH(15),
  BLAZE(250),
  MAGMA_CUBE(50),
  ENDER_DRAGON(500000),
  WITHER(250000),
  BAT(2),
  WITCH(125),
  ENDERMITE(50),
  GUARDIAN(150),
  SHULKER(525),
  PIG(0),
  SHEEP(0),
  COW(0),
  CHICKEN(0),
  SQUID(0),
  WOLF(0),
  MUSHROOM_COW(0),
  SNOWMAN(0),
  OCELOT(0),
  IRON_GOLEM(1000),
  HORSE(0),
  RABBIT(0),
  POLAR_BEAR(0),
  LLAMA(0),
  LLAMA_SPIT(0),
  PARROT(0),
  VILLAGER(0),
  ENDER_CRYSTAL(0),
  TURTLE(0),
  PHANTOM(100),
  TRIDENT(0),
  COD(0),
  SALMON(0),
  PUFFERFISH(0),
  TROPICAL_FISH(0),
  DROWNED(0),
  DOLPHIN(0),
  CAT(0),
  PANDA(0),
  PILLAGER(0),
  RAVAGER(0),
  TRADER_LLAMA(0),
  WANDERING_TRADER(0),
  FOX(0),
  BEE(0),
  HOGLIN(500),
  PIGLIN(150),
  STRIDER(0),
  ZOGLIN(500),
  PIGLIN_BRUTE(150),
  AXOLOTL(0),
  GLOW_ITEM_FRAME(0),
  GLOW_SQUID(0),
  GOAT(0),
  MARKER(0),
  ALLAY(0),
  CHEST_BOAT(0),
  FROG(0),
  TADPOLE(0),
  WARDEN(25000),
  CAMEL(0),
  FISHING_HOOK(0),
  LIGHTNING(0),
  PLAYER(50),
  UNKNOWN(0),
  TEXT_DISPLAY(0),
  SNIFFER(0),
  ITEM_DISPLAY(0),
  INTERACTION(0),
  BLOCK_DISPLAY(0);


  private int coins;

  private EntityCoins(int coins) {
    this.coins = coins;
  }

  public int getCoins(){
    return coins;
  }
}