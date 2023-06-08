/*     */ package water.of.cup.boardgames.game;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashMap;
/*     */ import javax.annotation.Nullable;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.NamespacedKey;
/*     */ import org.bukkit.OfflinePlayer;
/*     */ import org.bukkit.Sound;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.entity.Entity;
/*     */ import org.bukkit.entity.ItemFrame;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.inventory.meta.ItemMeta;
/*     */ import org.bukkit.inventory.meta.MapMeta;
/*     */ import org.bukkit.map.MapView;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import water.of.cup.boardgames.BoardGames;
/*     */ import water.of.cup.boardgames.config.ConfigUtil;
/*     */ import water.of.cup.boardgames.config.GameRecipe;
/*     */ import water.of.cup.boardgames.config.GameSound;
/*     */ import water.of.cup.boardgames.game.glicko2.Rating;
/*     */ import water.of.cup.boardgames.game.glicko2.RatingCalculator;
/*     */ import water.of.cup.boardgames.game.glicko2.RatingPeriodResults;
/*     */ import water.of.cup.boardgames.game.inventories.GameInventory;
/*     */ import water.of.cup.boardgames.game.maps.GameMap;
/*     */ import water.of.cup.boardgames.game.maps.MapData;
/*     */ import water.of.cup.boardgames.game.maps.MapManager;
/*     */ import water.of.cup.boardgames.game.maps.Screen;
/*     */ import water.of.cup.boardgames.game.npcs.GameNPC;
/*     */ import water.of.cup.boardgames.game.storage.BoardGamesStorageType;
/*     */ import water.of.cup.boardgames.game.storage.GameStorage;
/*     */ import water.of.cup.boardgames.game.storage.StorageType;
/*     */ import water.of.cup.boardgames.game.teams.TeamManager;
/*     */ import water.of.cup.boardgames.game.wagers.WagerManager;
/*     */ 
/*     */ 
/*     */ public abstract class Game
/*     */ {
/*     */   private static NamespacedKey gameIdKey;
/*     */   private static NamespacedKey gameNameKey;
/*     */   protected int gameId;
/*     */   protected String gameName;
/*     */   protected MapManager mapManager;
/*     */   protected ArrayList<Screen> screens;
/*     */   private boolean ingame;
/*     */   protected GameImage gameImage;
/*     */   protected ArrayList<Button> buttons;
/*     */   protected WagerManager wagerManager;
/*     */   protected Clock clock;
/*     */   protected GameInventory gameInventory;
/*     */   protected TeamManager teamManager;
/*     */   protected GameStorage gameStorage;
/*     */   private Location placedMapLoc;
/*     */   private final GameNPC gameNPC;
/*     */   protected ArrayList<GameMap> gameMaps;
/*     */   protected int[][] mapStructure;
/*     */   protected int placedMapVal;
/*     */   private HashMap<String, Object> gameData;
/*     */   private final GameConfig gameConfig;
/*     */   
/*     */   protected abstract void setMapInformation(int paramInt);
/*     */   
/*     */   private void setGameId() {
/*  71 */     this.gameId = BoardGames.getInstance().getGameManager().nextGameId();
/*     */   }
/*     */   
/*     */   protected void startGame() {
/*  75 */     this.clock = getClock();
/*  76 */     if (this.clock != null)
/*  77 */       this.clock.runTaskTimer((Plugin)BoardGames.getInstance(), 1L, 1L); 
/*  78 */     renderInitial();
/*     */   }
/*     */   
/*     */   protected abstract void setGameName();
/*     */   
/*     */   protected abstract void setBoardImage();
/*     */   
/*     */   protected abstract void clockOutOfTime();
/*     */   
/*     */   protected abstract Clock getClock();
/*     */   
/*     */   protected abstract GameInventory getGameInventory();
/*     */   
/*     */   protected abstract GameStorage getGameStorage();
/*     */   
/*     */   public abstract ArrayList<String> getTeamNames();
/*     */   
/*     */   protected abstract GameConfig getGameConfig();
/*     */   
/*     */   public Game(int paramInt) {
/*  98 */     this.screens = new ArrayList<>();
/*  99 */     setMapInformation(paramInt);
/* 100 */     assert this.placedMapVal != 0;
/* 101 */     createMapManager(paramInt);
/* 102 */     setGameId();
/* 103 */     assert this.gameId != 0;
/* 104 */     setGameName();
/* 105 */     assert this.gameName != null;
/* 106 */     setBoardImage();
/*     */     
/* 108 */     this.wagerManager = new WagerManager();
/*     */     
/* 110 */     this.buttons = new ArrayList<>();
/* 111 */     this.teamManager = new TeamManager(this);
/*     */ 
/*     */     
/* 114 */     this.gameMaps = new ArrayList<>();
/*     */     
/* 116 */     this.gameInventory = getGameInventory();
/* 117 */     this.gameStorage = getGameStorage();
/* 118 */     this.gameConfig = getGameConfig();
/* 119 */     this.gameNPC = BoardGames.hasCitizens() ? getGameNPC() : null;
/*     */     
/* 121 */     this.gameData = new HashMap<>();
/*     */   }
/*     */   
/*     */   public abstract void click(Player paramPlayer, double[] paramArrayOfdouble, ItemStack paramItemStack);
/*     */   
/*     */   public boolean canPlaceBoard(Location paramLocation, int paramInt) {
/* 127 */     int[] arrayOfInt1 = this.mapManager.getMapValsLocationOnRotatedBoard(this.placedMapVal);
/* 128 */     int[] arrayOfInt2 = this.mapManager.getRotatedDimensions();
/*     */ 
/*     */     
/* 131 */     int i = -arrayOfInt1[0];
/* 132 */     int j = arrayOfInt2[0] + i;
/*     */     
/* 134 */     boolean bool1 = false;
/* 135 */     boolean bool2 = false;
/*     */     
/* 137 */     int k = -arrayOfInt1[1];
/* 138 */     int m = arrayOfInt2[1] + k;
/*     */ 
/*     */     
/* 141 */     int n = Math.max(i, j);
/* 142 */     int i1 = Math.min(i, j);
/*     */     
/* 144 */     int i2 = Math.max(bool1, bool2);
/* 145 */     int i3 = Math.min(bool1, bool2);
/*     */     
/* 147 */     int i4 = Math.max(k, m);
/* 148 */     int i5 = Math.min(k, m);
/*     */     
/*     */     int i6;
/* 151 */     for (i6 = i1; i6 <= n; i6++) {
/* 152 */       for (int i7 = i3; i7 <= i2; i7++) {
/* 153 */         for (int i8 = i5; i8 <= i4; i8++) {
/*     */           
/* 155 */           if (!paramLocation.getWorld().getBlockAt(paramLocation.getBlockX() + i6, paramLocation.getBlockY() + i7, paramLocation.getBlockZ() + i8).isEmpty()) {
/* 156 */             return false;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 162 */     for (i6 = i1; i6 <= n; i6++) {
/* 163 */       for (int i7 = i5; i7 <= i4; i7++) {
/*     */ 
/*     */         
/* 166 */         boolean bool = false;
/* 167 */         for (MapData mapData : this.mapManager.getMapDataAtLocationOnRotatedBoard(i6 - i, i7 - k, 0)) {
/* 168 */           if (mapData.getMapVal() > 0) {
/* 169 */             bool = true; break;
/*     */           } 
/*     */         } 
/* 172 */         if (bool)
/*     */         {
/*     */           
/* 175 */           if (paramLocation.getWorld().getBlockAt(paramLocation.getBlockX() + i6, paramLocation.getBlockY() - 1, paramLocation.getBlockZ() + i7).isEmpty())
/* 176 */             return false; 
/*     */         }
/*     */       } 
/*     */     } 
/* 180 */     return true;
/*     */   }
/*     */   
/*     */   public void placeBoard(Location paramLocation, int paramInt) {
/* 184 */     placeBoard(paramLocation, paramInt, this.placedMapVal);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void placeBoard(Location paramLocation, int paramInt1, int paramInt2) {
/* 190 */     World world = paramLocation.getWorld();
/*     */     
/* 192 */     int[] arrayOfInt1 = this.mapManager.getMapValsLocationOnRotatedBoard(paramInt2);
/* 193 */     int[] arrayOfInt2 = this.mapManager.getRotatedDimensions();
/*     */ 
/*     */     
/* 196 */     int i = -arrayOfInt1[0];
/* 197 */     int j = arrayOfInt2[0] + i;
/*     */     
/* 199 */     byte b = 0;
/* 200 */     boolean bool = false;
/*     */     
/* 202 */     int k = -arrayOfInt1[1];
/* 203 */     int m = arrayOfInt2[1] + k;
/*     */ 
/*     */     
/* 206 */     int n = Math.max(i, j);
/* 207 */     int i1 = Math.min(i, j);
/*     */     
/* 209 */     int i2 = Math.max(b, bool);
/* 210 */     int i3 = Math.min(b, bool);
/*     */     
/* 212 */     int i4 = Math.max(k, m);
/* 213 */     int i5 = Math.min(k, m);
/*     */     
/* 215 */     int[] arrayOfInt3 = { 0, 0, 0 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 227 */     for (int i6 = i1; i6 <= n; i6++) {
/* 228 */       for (int i7 = i3; i7 <= i2; i7++) {
/* 229 */         for (int i8 = i5; i8 <= i4; i8++) {
/*     */           
/* 231 */           for (MapData mapData : this.mapManager.getMapDataAtLocationOnRotatedBoard(i6 - i, i8 - k, i7 - b)) {
/*     */             
/* 233 */             int i9 = mapData.getMapVal();
/*     */             
/* 235 */             if (i9 == getPlacedMapVal()) {
/* 236 */               arrayOfInt3 = new int[] { i6, i7, i8 };
/*     */             }
/*     */             
/* 239 */             if (i9 <= 0) {
/*     */               continue;
/*     */             }
/*     */             
/* 243 */             GameMap gameMap = new GameMap(this, i9, new ItemStack(Material.FILLED_MAP, 1));
/*     */ 
/*     */             
/* 246 */             MapView mapView = Bukkit.createMap(world);
/* 247 */             MapMeta mapMeta = (MapMeta)gameMap.getItemMeta();
/* 248 */             mapMeta.setMapView(mapView);
/* 249 */             gameMap.setItemMeta((ItemMeta)mapMeta);
/*     */             
/* 251 */             this.gameMaps.add(gameMap);
/*     */ 
/*     */ 
/*     */             
/* 255 */             Location location = new Location(paramLocation.getWorld(), (paramLocation.getBlockX() + i6), (paramLocation.getBlockY() + i7), (paramLocation.getBlockZ() + i8));
/*     */ 
/*     */             
/* 258 */             location.getBlock().setType(Material.AIR);
/* 259 */             Block block = location.getBlock().getRelative(mapData.getBlockFace().getOppositeFace());
/* 260 */             boolean bool1 = false;
/* 261 */             if (block.getType() == Material.AIR) {
/* 262 */               block.setType(Material.BARRIER);
/* 263 */               bool1 = true;
/*     */             } 
/*     */             
/* 266 */             ItemFrame itemFrame = (ItemFrame)world.spawn(location, ItemFrame.class);
/* 267 */             itemFrame.setItem((ItemStack)gameMap);
/* 268 */             itemFrame.setFacingDirection(mapData.getBlockFace(), true);
/* 269 */             itemFrame.setInvulnerable(true);
/* 270 */             itemFrame.setFixed(true);
/* 271 */             itemFrame.setVisible(true);
/* 272 */             if (bool1) {
/* 273 */               block.setType(Material.AIR);
/*     */             }
/* 275 */             location.getBlock().setType(Material.BARRIER);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 283 */     if (hasGameNPC()) {
/* 284 */       this.gameNPC.setMapValLoc(new Location(paramLocation.getWorld(), (paramLocation.getBlockX() + arrayOfInt3[0]), (paramLocation.getBlockY() + arrayOfInt3[1]), (paramLocation
/* 285 */             .getBlockZ() + arrayOfInt3[2])), paramInt1);
/*     */     }
/* 287 */     this.placedMapLoc = paramLocation.clone();
/*     */ 
/*     */     
/* 290 */     if (!hasGameInventory()) {
/* 291 */       startGame();
/*     */     } else {
/* 293 */       renderInitial();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void renderInitial() {
/* 298 */     this.mapManager.resetRenderers();
/*     */   }
/*     */   
/*     */   public void endGame(GamePlayer paramGamePlayer) {
/* 302 */     this.ingame = false;
/* 303 */     this.wagerManager.completeWagers(paramGamePlayer);
/* 304 */     sendGameWinMoney(paramGamePlayer);
/* 305 */     sendEndGameMessage(paramGamePlayer);
/* 306 */     updateGameStorage(paramGamePlayer);
/* 307 */     clearGamePlayers();
/*     */     
/* 309 */     if (this.clock != null) {
/* 310 */       this.clock.cancel();
/*     */     }
/* 312 */     if (hasGameNPC()) {
/* 313 */       this.gameNPC.removeNPC();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInGame() {
/* 320 */     this.ingame = true;
/*     */   }
/*     */   
/*     */   protected void setInGame(boolean paramBoolean) {
/* 324 */     this.ingame = paramBoolean;
/*     */   }
/*     */   
/*     */   public boolean isIngame() {
/* 328 */     return this.ingame;
/*     */   }
/*     */   
/*     */   protected Button getClickedButton(GamePlayer paramGamePlayer, int[] paramArrayOfint) {
/* 332 */     for (Button button : this.buttons) {
/* 333 */       if (button.clicked(paramGamePlayer, paramArrayOfint))
/* 334 */         return button; 
/*     */     } 
/* 336 */     return null;
/*     */   }
/*     */   
/*     */   public boolean hasPlayer(Player paramPlayer) {
/* 340 */     return (this.teamManager.getGamePlayer(paramPlayer) != null);
/*     */   }
/*     */   
/*     */   public ArrayList<Game> getPlayerQueue() {
/* 344 */     return null;
/*     */   }
/*     */   
/*     */   public ArrayList<Game> getPlayerDecideQueue() {
/* 348 */     return null;
/*     */   }
/*     */   
/*     */   public int getGameId() {
/* 352 */     return this.gameId;
/*     */   }
/*     */   
/*     */   public GamePlayer getGamePlayer(Player paramPlayer) {
/* 356 */     return this.teamManager.getGamePlayer(paramPlayer);
/*     */   }
/*     */   
/*     */   public ArrayList<GamePlayer> getGamePlayers() {
/* 360 */     return this.teamManager.getGamePlayers();
/*     */   }
/*     */   
/*     */   public GamePlayer addPlayer(Player paramPlayer, String paramString) {
/* 364 */     if (this.teamManager.getGamePlayer(paramPlayer) != null) {
/* 365 */       return this.teamManager.getGamePlayer(paramPlayer);
/*     */     }
/*     */     
/* 368 */     GamePlayer gamePlayer = new GamePlayer(paramPlayer);
/* 369 */     if (paramString == null) {
/* 370 */       this.teamManager.addTeam(gamePlayer);
/*     */     } else {
/* 372 */       this.teamManager.addTeam(gamePlayer, paramString);
/*     */     } 
/*     */     
/* 375 */     return gamePlayer;
/*     */   }
/*     */   
/*     */   public GamePlayer addPlayer(Player paramPlayer) {
/* 379 */     return addPlayer(paramPlayer, null);
/*     */   }
/*     */   
/*     */   public void removePlayer(Player paramPlayer) {
/* 383 */     this.teamManager.removeTeamByPlayer(paramPlayer);
/*     */   }
/*     */   
/*     */   public void clearGamePlayers() {
/* 387 */     this.teamManager.resetTeams();
/*     */   }
/*     */   
/*     */   public GamePlayer getTurn() {
/* 391 */     return this.teamManager.getTurnPlayer();
/*     */   }
/*     */   
/*     */   protected abstract void gamePlayerOutOfTime(GamePlayer paramGamePlayer);
/*     */   
/*     */   public static NamespacedKey getGameIdKey() {
/* 397 */     return gameIdKey;
/*     */   }
/*     */   
/*     */   public static void setGameIdKey(NamespacedKey paramNamespacedKey) {
/* 401 */     gameIdKey = paramNamespacedKey;
/*     */   }
/*     */   
/*     */   protected void createMapManager(int paramInt) {
/* 405 */     this.mapManager = new MapManager(this.mapStructure, paramInt, this);
/*     */   }
/*     */   
/*     */   public boolean destroy(ItemFrame paramItemFrame) {
/* 409 */     if (!GameMap.isGameMap(paramItemFrame.getItem())) {
/* 410 */       return false;
/*     */     }
/*     */     
/* 413 */     GameMap gameMap = new GameMap(paramItemFrame.getItem());
/* 414 */     int i = gameMap.getMapVal();
/* 415 */     destroyBoard(paramItemFrame.getLocation(), i);
/*     */     
/* 417 */     delete();
/* 418 */     return true;
/*     */   }
/*     */   
/*     */   private void destroyBoard(Location paramLocation, int paramInt) {
/* 422 */     int[] arrayOfInt1 = this.mapManager.getMapValsLocationOnRotatedBoard(paramInt);
/* 423 */     int[] arrayOfInt2 = this.mapManager.getRotatedDimensions();
/*     */ 
/*     */     
/* 426 */     int i = -arrayOfInt1[0];
/* 427 */     int j = arrayOfInt2[0] + i;
/*     */     
/* 429 */     boolean bool1 = false;
/* 430 */     boolean bool2 = false;
/*     */     
/* 432 */     int k = -arrayOfInt1[1];
/* 433 */     int m = arrayOfInt2[1] + k;
/*     */ 
/*     */     
/* 436 */     int n = Math.max(i, j);
/* 437 */     int i1 = Math.min(i, j);
/*     */     
/* 439 */     int i2 = Math.max(bool1, bool2);
/* 440 */     int i3 = Math.min(bool1, bool2);
/*     */     
/* 442 */     int i4 = Math.max(k, m);
/* 443 */     int i5 = Math.min(k, m);
/*     */ 
/*     */     
/* 446 */     for (int i6 = i1; i6 <= n; i6++) {
/* 447 */       for (int i7 = i3; i7 <= i2; i7++) {
/* 448 */         for (int i8 = i5; i8 <= i4; i8++) {
/* 449 */           Block block = paramLocation.getWorld().getBlockAt(paramLocation.getBlockX() + i6, paramLocation.getBlockY() + i7, paramLocation
/* 450 */               .getBlockZ() + i8);
/* 451 */           for (Entity entity : paramLocation.getWorld().getNearbyEntities(block.getBoundingBox())) {
/* 452 */             if (entity instanceof ItemFrame && GameMap.isGameMap(((ItemFrame)entity).getItem())) {
/* 453 */               ItemFrame itemFrame = (ItemFrame)entity;
/* 454 */               itemFrame.remove();
/*     */             } 
/*     */           } 
/* 457 */           block.setType(Material.AIR);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void delete() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getGameName() {
/* 474 */     return this.gameName;
/*     */   }
/*     */   
/*     */   public ArrayList<GameMap> getGameMaps() {
/* 478 */     return this.gameMaps;
/*     */   }
/*     */   
/*     */   public GameMap getGameMapByMapVal(int paramInt) {
/* 482 */     for (GameMap gameMap : this.gameMaps) {
/* 483 */       if (gameMap.getMapVal() == paramInt)
/* 484 */         return gameMap; 
/*     */     } 
/* 486 */     return null;
/*     */   }
/*     */   
/*     */   public GameImage getGameImage() {
/* 490 */     return this.gameImage;
/*     */   }
/*     */   
/*     */   public abstract ItemStack getBoardItem();
/*     */   
/*     */   public ArrayList<Button> getButtons() {
/* 496 */     return this.buttons;
/*     */   }
/*     */   
/*     */   public int getRotation() {
/* 500 */     return this.mapManager.getRotation();
/*     */   }
/*     */   
/*     */   public String getName() {
/* 504 */     return this.gameName + "";
/*     */   }
/*     */   
/*     */   public String getAltName() {
/* 508 */     String str = "settings.games." + getName() + ".altName";
/* 509 */     if (BoardGames.getInstance().getConfig().getString(str) == null) {
/* 510 */       return getName();
/*     */     }
/* 512 */     return BoardGames.getInstance().getConfig().getString(str);
/*     */   }
/*     */   
/*     */   public static NamespacedKey getGameNameKey() {
/* 516 */     return gameNameKey;
/*     */   }
/*     */   
/*     */   public static void setGameNameKey(NamespacedKey paramNamespacedKey) {
/* 520 */     gameNameKey = paramNamespacedKey;
/*     */   }
/*     */   
/*     */   public ArrayList<Screen> getScreens() {
/* 524 */     return this.screens;
/*     */   }
/*     */   
/*     */   public WagerManager getWagerManager() {
/* 528 */     return this.wagerManager;
/*     */   }
/*     */   
/*     */   public void displayGameInventory(Player paramPlayer) {
/* 532 */     if (this.gameInventory != null) {
/* 533 */       this.gameInventory.build(paramPlayer);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean hasGameInventory() {
/* 538 */     return (this.gameInventory != null);
/*     */   }
/*     */   
/*     */   public int getPlacedMapVal() {
/* 542 */     return this.placedMapVal;
/*     */   }
/*     */   
/*     */   public void replace(Location paramLocation, int paramInt1, int paramInt2) {
/* 546 */     destroyBoard(paramLocation, paramInt2);
/* 547 */     final Location loc = paramLocation;
/* 548 */     final int frotation = paramInt1;
/* 549 */     final int fmapVal = paramInt2;
/* 550 */     Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((Plugin)BoardGames.getInstance(), new Runnable()
/*     */         {
/*     */           public void run() {
/*     */             try {
/* 554 */               Game.this.placeBoard(loc, frotation, fmapVal);
/*     */             }
/* 556 */             catch (IllegalArgumentException illegalArgumentException) {
/* 557 */               Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((Plugin)BoardGames.getInstance(), this, 20L);
/*     */             } 
/*     */           }
/*     */         }20L);
/*     */   }
/*     */   
/*     */   public boolean hasGameStorage() {
/* 564 */     return (this.gameStorage != null && BoardGames.getInstance().getStorageManager() != null);
/*     */   }
/*     */   
/*     */   public void setGameData(HashMap<String, Object> paramHashMap) {
/* 568 */     this.gameData = new HashMap<>(paramHashMap);
/*     */   }
/*     */   
/*     */   public boolean hasGameData(String paramString) {
/* 572 */     return this.gameData.containsKey(paramString);
/*     */   }
/*     */   
/*     */   public Object getGameData(String paramString) {
/* 576 */     if (this.gameData.get(paramString) instanceof String) {
/* 577 */       return ChatColor.stripColor((String)this.gameData.get(paramString));
/*     */     }
/* 579 */     return this.gameData.get(paramString);
/*     */   }
/*     */   
/*     */   public GameStorage getGameStore() {
/* 583 */     return this.gameStorage;
/*     */   }
/*     */   
/*     */   public boolean hasGameConfig() {
/* 587 */     return (this.gameConfig != null);
/*     */   }
/*     */   
/*     */   public GameRecipe getGameRecipe() {
/* 591 */     if (!hasGameConfig()) {
/* 592 */       return null;
/*     */     }
/* 594 */     return this.gameConfig.getGameRecipe();
/*     */   }
/*     */   
/*     */   public ArrayList<GameSound> getGameSounds() {
/* 598 */     if (!hasGameConfig()) {
/* 599 */       return null;
/*     */     }
/* 601 */     return this.gameConfig.getGameSounds();
/*     */   }
/*     */   
/*     */   public HashMap<String, Object> getCustomValues() {
/* 605 */     if (!hasGameConfig()) {
/* 606 */       return null;
/*     */     }
/* 608 */     return this.gameConfig.getCustomValues();
/*     */   }
/*     */   
/*     */   public int getGameWinAmount() {
/* 612 */     if (!hasGameConfig()) {
/* 613 */       return 0;
/*     */     }
/* 615 */     return this.gameConfig.getWinAmount();
/*     */   }
/*     */   
/*     */   public boolean isEnabled() {
/* 619 */     String str = "settings.games." + getName() + ".enabled";
/* 620 */     if (BoardGames.getInstance().getConfig().getString(str) == null) {
/* 621 */       return true;
/*     */     }
/* 623 */     return ConfigUtil.getBoolean(str);
/*     */   }
/*     */   
/*     */   public void sendGameWinMoney(GamePlayer paramGamePlayer) {
/* 627 */     if (paramGamePlayer == null)
/*     */       return; 
/* 629 */     if (!hasGameConfig()) {
/*     */       return;
/*     */     }
/* 632 */     String str = "settings.games." + getName() + ".winAmount";
/* 633 */     int i = BoardGames.getInstance().getConfig().getInt(str);
/*     */     
/* 635 */     if (BoardGames.getInstance().getEconomy() != null && i != 0) {
/* 636 */       BoardGames.getInstance().getEconomy().depositPlayer((OfflinePlayer)paramGamePlayer.getPlayer(), i);
/*     */     }
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public Sound getGameSound(String paramString) {
/* 642 */     if (this.gameConfig.getGameSounds() == null) {
/* 643 */       return null;
/*     */     }
/* 645 */     String str1 = "settings.games." + getName() + ".sounds";
/* 646 */     if (!ConfigUtil.getBoolean(str1 + ".enabled")) {
/* 647 */       return null;
/*     */     }
/* 649 */     String str2 = BoardGames.getInstance().getConfig().getString(str1 + "." + paramString);
/*     */     
/*     */     try {
/* 652 */       return Sound.valueOf(str2);
/* 653 */     } catch (IllegalArgumentException illegalArgumentException) {
/* 654 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object getConfigValue(String paramString) {
/* 661 */     if (this.gameConfig.getCustomValues() == null) {
/* 662 */       return null;
/*     */     }
/* 664 */     String str = "settings.games." + getName() + ".misc." + paramString;
/*     */     
/* 666 */     return BoardGames.getInstance().getConfig().get(str);
/*     */   }
/*     */   
/*     */   protected void playGameSound(String paramString) {
/* 670 */     Sound sound = getGameSound(paramString);
/* 671 */     if (sound == null) {
/*     */       return;
/*     */     }
/* 674 */     for (GamePlayer gamePlayer : this.teamManager.getGamePlayers()) {
/* 675 */       gamePlayer.getPlayer().playSound(gamePlayer.getPlayer().getLocation(), sound, 5.0F, 1.0F);
/*     */     }
/*     */   }
/*     */   
/*     */   public void exitPlayer(Player paramPlayer) {
/* 680 */     for (GamePlayer gamePlayer : this.teamManager.getGamePlayers()) {
/* 681 */       if (gamePlayer.getPlayer().isOnline()) {
/* 682 */         gamePlayer.getPlayer().sendMessage(ConfigUtil.CHAT_GAME_PLAYER_LEAVE
/* 683 */             .buildStringPlayerGame(paramPlayer.getDisplayName(), getAltName()));
/*     */       }
/*     */     } 
/*     */     
/* 687 */     if (this.teamManager.getGamePlayers().size() == 1) {
/* 688 */       endGame(null);
/*     */       
/*     */       return;
/*     */     } 
/* 692 */     if (this.teamManager.getGamePlayers().size() == 2) {
/* 693 */       if (this.teamManager.getTurnPlayer().getPlayer() == paramPlayer)
/* 694 */         this.teamManager.nextTurn(); 
/* 695 */       endGame(this.teamManager.getTurnPlayer());
/*     */       
/*     */       return;
/*     */     } 
/* 699 */     this.teamManager.removeTeamByPlayer(paramPlayer);
/*     */     
/* 701 */     if (hasGameStorage() && 
/* 702 */       this.gameStorage.canExecute((StorageType)BoardGamesStorageType.LOSSES)) {
/* 703 */       this.gameStorage.updateData(paramPlayer, (StorageType)BoardGamesStorageType.LOSSES, Integer.valueOf(1));
/*     */     }
/*     */ 
/*     */     
/* 707 */     if (this.teamManager.getGamePlayers().size() == 1) {
/* 708 */       endGame(this.teamManager.getGamePlayers().get(0));
/*     */       return;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void rerender(Player paramPlayer) {
/* 714 */     this.mapManager.renderBoard(paramPlayer);
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateGameStorage(GamePlayer paramGamePlayer) {
/* 719 */     if (!hasGameStorage()) {
/*     */       return;
/*     */     }
/*     */     
/* 723 */     if (this.gameStorage.canExecute((StorageType)BoardGamesStorageType.Rating) && 
/* 724 */       this.teamManager.getGamePlayers().size() == 2) {
/*     */       
/* 726 */       GamePlayer gamePlayer1 = this.teamManager.getGamePlayers().get(0);
/* 727 */       GamePlayer gamePlayer2 = this.teamManager.getGamePlayers().get(1);
/* 728 */       if (paramGamePlayer != gamePlayer1 && paramGamePlayer != null) {
/* 729 */         gamePlayer2 = gamePlayer1;
/* 730 */         gamePlayer1 = paramGamePlayer;
/*     */       } 
/*     */       
/* 733 */       updateRatings(gamePlayer1, gamePlayer2, (paramGamePlayer == null));
/*     */     } 
/*     */ 
/*     */     
/* 737 */     if (paramGamePlayer == null) {
/* 738 */       if (this.teamManager.getGamePlayers().size() == 1) {
/* 739 */         this.gameStorage.updateData(((GamePlayer)this.teamManager.getGamePlayers().get(0)).getPlayer(), (StorageType)BoardGamesStorageType.LOSSES, Integer.valueOf(1));
/*     */         
/*     */         return;
/*     */       } 
/* 743 */       for (GamePlayer gamePlayer : this.teamManager.getGamePlayers()) {
/* 744 */         this.gameStorage.updateData(gamePlayer.getPlayer(), (StorageType)BoardGamesStorageType.TIES, Integer.valueOf(1));
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/* 749 */     this.gameStorage.updateData(paramGamePlayer.getPlayer(), (StorageType)BoardGamesStorageType.WINS, Integer.valueOf(1));
/*     */     
/* 751 */     if (this.gameStorage.canExecute((StorageType)BoardGamesStorageType.BEST_TIME)) {
/*     */       
/* 753 */       LinkedHashMap linkedHashMap = BoardGames.getInstance().getStorageManager().fetchPlayerStats((OfflinePlayer)paramGamePlayer.getPlayer(), getGameStore(), false);
/* 754 */       Double double_ = this.clock.getPlayerTimes().get(paramGamePlayer);
/*     */       
/* 756 */       double d = 0.0D;
/* 757 */       if (linkedHashMap != null && linkedHashMap.containsKey(BoardGamesStorageType.BEST_TIME)) d = ((Double)linkedHashMap.get(BoardGamesStorageType.BEST_TIME)).doubleValue(); 
/* 758 */       if (d <= 0.0D || d > double_.doubleValue()) {
/* 759 */         this.gameStorage.setData(paramGamePlayer.getPlayer(), (StorageType)BoardGamesStorageType.BEST_TIME, double_);
/*     */       }
/*     */     } 
/* 762 */     for (GamePlayer gamePlayer : this.teamManager.getGamePlayers()) {
/* 763 */       if (gamePlayer.getPlayer().getName().equals(paramGamePlayer.getPlayer().getName())) {
/*     */         continue;
/*     */       }
/* 766 */       this.gameStorage.updateData(gamePlayer.getPlayer(), (StorageType)BoardGamesStorageType.LOSSES, Integer.valueOf(1));
/*     */     } 
/*     */   }
/*     */   
/*     */   private void sendEndGameMessage(GamePlayer paramGamePlayer) {
/*     */     String str;
/* 772 */     if (paramGamePlayer == null) {
/* 773 */       if (this.teamManager.getGamePlayers().size() == 1) {
/* 774 */         str = ConfigUtil.CHAT_GAME_PLAYER_LOSE.buildString(getAltName());
/*     */       } else {
/* 776 */         str = ConfigUtil.CHAT_GAME_TIE.buildString(getAltName());
/*     */       } 
/*     */     } else {
/*     */       
/* 780 */       str = ConfigUtil.CHAT_GAME_PLAYER_WIN.buildStringPlayerGame(paramGamePlayer.getPlayer().getDisplayName(), getAltName());
/*     */     } 
/*     */     
/* 783 */     for (GamePlayer gamePlayer : this.teamManager.getGamePlayers()) {
/* 784 */       gamePlayer.getPlayer().sendMessage(str);
/*     */     }
/*     */   }
/*     */   
/*     */   private void updateRatings(GamePlayer paramGamePlayer1, GamePlayer paramGamePlayer2, boolean paramBoolean) {
/*     */     Rating rating1, rating2;
/* 790 */     if (!getGameData("ranked").equals(ConfigUtil.GUI_RANKED_OPTION_TEXT.toRawString()))
/*     */       return; 
/* 792 */     RatingCalculator ratingCalculator = new RatingCalculator();
/*     */     
/* 794 */     String str1 = paramGamePlayer1.getPlayer().getUniqueId().toString();
/* 795 */     String str2 = paramGamePlayer2.getPlayer().getUniqueId().toString();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 801 */     LinkedHashMap linkedHashMap1 = BoardGames.getInstance().getStorageManager().fetchPlayerStats((OfflinePlayer)paramGamePlayer1.getPlayer(), getGameStore(), false);
/*     */     
/* 803 */     LinkedHashMap linkedHashMap2 = BoardGames.getInstance().getStorageManager().fetchPlayerStats((OfflinePlayer)paramGamePlayer2.getPlayer(), getGameStore(), false);
/*     */     
/* 805 */     boolean bool1 = (linkedHashMap1 == null || linkedHashMap1.get(BoardGamesStorageType.Rating) == null) ? true : false;
/* 806 */     if (bool1 || ((Double)linkedHashMap1.get(BoardGamesStorageType.Rating)).doubleValue() <= 0.1D) {
/* 807 */       rating1 = new Rating(str1, ratingCalculator);
/*     */     }
/*     */     else {
/*     */       
/* 811 */       rating1 = new Rating(str1, ratingCalculator, ((Double)linkedHashMap1.get(BoardGamesStorageType.Rating)).doubleValue(), ((Double)linkedHashMap1.get(BoardGamesStorageType.RatingDeviation)).doubleValue(), ((Double)linkedHashMap1.get(BoardGamesStorageType.RatingVolatility)).doubleValue());
/*     */     } 
/*     */     
/* 814 */     boolean bool2 = (linkedHashMap2 == null || linkedHashMap2.get(BoardGamesStorageType.Rating) == null) ? true : false;
/* 815 */     if (bool2 || ((Double)linkedHashMap2.get(BoardGamesStorageType.Rating)).doubleValue() <= 0.1D) {
/* 816 */       rating2 = new Rating(str2, ratingCalculator);
/*     */     }
/*     */     else {
/*     */       
/* 820 */       rating2 = new Rating(str2, ratingCalculator, ((Double)linkedHashMap2.get(BoardGamesStorageType.Rating)).doubleValue(), ((Double)linkedHashMap2.get(BoardGamesStorageType.RatingDeviation)).doubleValue(), ((Double)linkedHashMap2.get(BoardGamesStorageType.RatingVolatility)).doubleValue());
/*     */     } 
/*     */     
/* 823 */     RatingPeriodResults ratingPeriodResults = new RatingPeriodResults();
/*     */     
/* 825 */     if (paramBoolean) {
/*     */       
/* 827 */       ratingPeriodResults.addDraw(rating1, rating2);
/*     */     } else {
/*     */       
/* 830 */       ratingPeriodResults.addResult(rating1, rating2);
/*     */     } 
/* 832 */     ratingCalculator.updateRatings(ratingPeriodResults);
/*     */     
/* 834 */     this.gameStorage.setData(paramGamePlayer1.getPlayer(), (StorageType)BoardGamesStorageType.Rating, Double.valueOf(rating1.getRating()));
/* 835 */     this.gameStorage.setData(paramGamePlayer1.getPlayer(), (StorageType)BoardGamesStorageType.RatingDeviation, Double.valueOf(rating1.getRatingDeviation()));
/* 836 */     this.gameStorage.setData(paramGamePlayer1.getPlayer(), (StorageType)BoardGamesStorageType.RatingVolatility, Double.valueOf(rating1.getVolatility()));
/*     */     
/* 838 */     this.gameStorage.setData(paramGamePlayer2.getPlayer(), (StorageType)BoardGamesStorageType.Rating, Double.valueOf(rating2.getRating()));
/* 839 */     this.gameStorage.setData(paramGamePlayer2.getPlayer(), (StorageType)BoardGamesStorageType.RatingDeviation, Double.valueOf(rating2.getRatingDeviation()));
/* 840 */     this.gameStorage.setData(paramGamePlayer2.getPlayer(), (StorageType)BoardGamesStorageType.RatingVolatility, Double.valueOf(rating2.getVolatility()));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean allowOutsideClicks() {
/* 845 */     return false;
/*     */   }
/*     */   
/*     */   public GameNPC getGameNPC() {
/* 849 */     return null;
/*     */   }
/*     */   
/*     */   public boolean hasGameNPC() {
/* 853 */     return (this.gameNPC != null && BoardGames.hasCitizens());
/*     */   }
/*     */   
/*     */   protected void spawnNPC() {
/* 857 */     if (!hasGameNPC())
/* 858 */       return;  this.gameNPC.spawnNPC();
/*     */   }
/*     */   
/*     */   protected void npcLookAt(Player paramPlayer) {
/* 862 */     if (!hasGameNPC())
/* 863 */       return;  this.gameNPC.lookAt(paramPlayer);
/*     */   }
/*     */   
/*     */   public Location getPlacedMapLoc() {
/* 867 */     return this.placedMapLoc;
/*     */   }
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\Game.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */