/*     */ package water.of.cup.boardgames.game.inventories;
/*     */ 
/*     */ import de.themoep.inventorygui.InventoryGui;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Set;
/*     */ import org.bukkit.OfflinePlayer;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.InventoryHolder;
/*     */ import water.of.cup.boardgames.BoardGames;
/*     */ import water.of.cup.boardgames.config.ConfigUtil;
/*     */ import water.of.cup.boardgames.game.Game;
/*     */ import water.of.cup.boardgames.game.GamePlayer;
/*     */ import water.of.cup.boardgames.game.MathUtils;
/*     */ import water.of.cup.boardgames.game.inventories.create.CreateInventoryCallback;
/*     */ import water.of.cup.boardgames.game.inventories.create.GameCreateInventory;
/*     */ import water.of.cup.boardgames.game.inventories.create.GameWaitPlayersInventory;
/*     */ import water.of.cup.boardgames.game.inventories.create.WaitPlayersCallback;
/*     */ import water.of.cup.boardgames.game.inventories.ingame.GameForfeitCallback;
/*     */ import water.of.cup.boardgames.game.inventories.ingame.GameForfeitInventory;
/*     */ import water.of.cup.boardgames.game.inventories.join.GameJoinInventory;
/*     */ import water.of.cup.boardgames.game.inventories.join.JoinGameCallback;
/*     */ import water.of.cup.boardgames.game.inventories.ready.GameReadyCallback;
/*     */ import water.of.cup.boardgames.game.inventories.ready.GameReadyInventory;
/*     */ import water.of.cup.boardgames.game.inventories.trade.GameTrade;
/*     */ import water.of.cup.boardgames.game.inventories.trade.GameTradeCallback;
/*     */ import water.of.cup.boardgames.game.inventories.trade.GameTradeInventory;
/*     */ import water.of.cup.boardgames.game.inventories.wager.GameWagerCallback;
/*     */ import water.of.cup.boardgames.game.inventories.wager.GameWagerInventory;
/*     */ import water.of.cup.boardgames.game.inventories.wager.WagerOption;
/*     */ import water.of.cup.boardgames.game.wagers.ItemWager;
/*     */ import water.of.cup.boardgames.game.wagers.RequestWager;
/*     */ import water.of.cup.boardgames.game.wagers.Wager;
/*     */ import water.of.cup.boardgames.game.wagers.WagerManager;
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
/*     */ 
/*     */ 
/*     */ public abstract class GameInventory
/*     */ {
/*     */   private final Game game;
/*     */   private final GameCreateInventory gameCreateInventory;
/*     */   private final GameWaitPlayersInventory gameWaitPlayersInventory;
/*     */   private final GameJoinInventory gameJoinInventory;
/*     */   private final GameReadyInventory gameReadyInventory;
/*     */   private final GameWagerInventory gameWagerInventory;
/*     */   private final GameForfeitInventory gameForfeitInventory;
/*  57 */   private final BoardGames instance = BoardGames.getInstance();
/*     */   
/*     */   private final ArrayList<GameOption> gameOptions;
/*     */   
/*     */   private final int maxPlayers;
/*     */   
/*     */   private final int minPlayers;
/*     */   
/*     */   private final boolean hasWagers;
/*     */   
/*     */   private final boolean hasItemWagers;
/*     */   
/*     */   private final ArrayList<Player> joinPlayerQueue;
/*     */   
/*     */   private final ArrayList<Player> acceptedPlayers;
/*     */   
/*     */   private final HashMap<Player, Boolean> playerReadyMap;
/*     */   
/*     */   private final HashMap<Player, WagerOption> wagerViewPlayers;
/*     */   
/*     */   private final WagerManager wagerManager;
/*     */   
/*     */   private HashMap<String, Object> gameData;
/*     */   
/*     */   private Player gameCreator;
/*     */   
/*     */   public GameInventory(Game paramGame) {
/*  84 */     this.game = paramGame;
/*  85 */     this.joinPlayerQueue = new ArrayList<>();
/*  86 */     this.acceptedPlayers = new ArrayList<>();
/*  87 */     this.playerReadyMap = new HashMap<>();
/*  88 */     this.wagerViewPlayers = new HashMap<>();
/*  89 */     this.wagerManager = paramGame.getWagerManager();
/*     */     
/*  91 */     this.gameOptions = (getOptions() == null) ? new ArrayList<>() : getOptions();
/*  92 */     this.maxPlayers = getMaxGame();
/*  93 */     this.minPlayers = getMinGame();
/*  94 */     this.hasWagers = (hasGameWagers() && this.instance.getEconomy() != null);
/*  95 */     this.hasItemWagers = (this.maxPlayers == 2 && ConfigUtil.ITEM_WAGERS_ENABLED.toBoolean());
/*     */ 
/*     */     
/*  98 */     if (hasTeamSelect() && paramGame.getTeamNames() != null) {
/*  99 */       this.gameOptions.add(0, GameOption.getTeamSelectGameOption(paramGame.getTeamNames()));
/*     */     }
/*     */ 
/*     */     
/* 103 */     if (this.hasItemWagers) {
/* 104 */       this.gameOptions.add(0, GameOption.getTradeItemsOption());
/*     */     }
/*     */ 
/*     */     
/* 108 */     if (this.hasWagers) {
/* 109 */       this.gameOptions.add(0, GameOption.getWagerGameOption());
/*     */     }
/*     */ 
/*     */     
/* 113 */     ArrayList<GameOption> arrayList = new ArrayList();
/* 114 */     for (GameOption gameOption : this.gameOptions) {
/* 115 */       if (gameOption.requiresEconomy() && this.instance.getEconomy() == null) {
/* 116 */         arrayList.add(gameOption);
/*     */       }
/*     */     } 
/*     */     
/* 120 */     this.gameOptions.removeAll(arrayList);
/*     */ 
/*     */     
/* 123 */     this.gameData = null;
/*     */     
/* 125 */     this.gameCreateInventory = new GameCreateInventory(this);
/* 126 */     this.gameWaitPlayersInventory = new GameWaitPlayersInventory(this);
/* 127 */     this.gameJoinInventory = new GameJoinInventory(this);
/* 128 */     this.gameReadyInventory = new GameReadyInventory(this);
/* 129 */     this.gameWagerInventory = new GameWagerInventory(this);
/* 130 */     this.gameForfeitInventory = new GameForfeitInventory(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void build(Player paramPlayer) {
/* 139 */     if (hasForfeitScreen() && this.game.isIngame() && this.game.hasPlayer(paramPlayer)) {
/* 140 */       this.gameForfeitInventory.build(paramPlayer, handleForfeit());
/*     */       
/*     */       return;
/*     */     } 
/* 144 */     if (hasCustomInGameInventory() && this.game.isIngame() && this.game.hasPlayer(paramPlayer)) {
/* 145 */       openCustomInGameInventory(paramPlayer);
/*     */       
/*     */       return;
/*     */     } 
/* 149 */     if (this.game.isIngame())
/*     */       return; 
/* 151 */     if (this.gameData == null) {
/* 152 */       this.gameCreateInventory.build(paramPlayer, handleCreateGame(paramPlayer));
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 157 */     if (this.playerReadyMap.size() > 0) {
/* 158 */       if (this.hasWagers && hasWagerScreen()) {
/* 159 */         this.wagerViewPlayers.put(paramPlayer, new WagerOption(this.game.getGamePlayers().get(0)));
/* 160 */         this.gameWagerInventory.build(paramPlayer, handleWager());
/*     */       } 
/*     */       
/*     */       return;
/*     */     } 
/* 165 */     this.gameJoinInventory.build(paramPlayer, handleJoinGame());
/*     */   }
/*     */   
/*     */   private CreateInventoryCallback handleCreateGame(final Player player) {
/* 169 */     return new CreateInventoryCallback()
/*     */       {
/*     */         public void onCreateGame(HashMap<String, Object> param1HashMap)
/*     */         {
/* 173 */           if (GameInventory.this.gameData != null || GameInventory.this.game.isIngame()) {
/* 174 */             player.sendMessage(ConfigUtil.CHAT_GUI_GAME_ALREADY_CREATED.toString());
/*     */             
/*     */             return;
/*     */           } 
/*     */           
/* 179 */           if (param1HashMap == null) {
/*     */             return;
/*     */           }
/*     */ 
/*     */           
/* 184 */           String str = null;
/* 185 */           if (param1HashMap.containsKey("team") && GameInventory.this.hasTeamSelect()) {
/* 186 */             str = (new StringBuilder()).append(param1HashMap.get("team")).append("").toString();
/*     */           }
/*     */ 
/*     */           
/* 190 */           for (GameOption gameOption : GameInventory.this.gameOptions) {
/* 191 */             if (gameOption.requiresEconomy()) {
/* 192 */               String str1 = (new StringBuilder()).append(param1HashMap.get(gameOption.getKey())).append("").toString();
/* 193 */               if (!MathUtils.isNumeric(str1))
/*     */                 continue; 
/* 195 */               double d = Double.parseDouble(str1);
/* 196 */               if (GameInventory.this.instance.getEconomy().getBalance((OfflinePlayer)player) < d) {
/* 197 */                 player.sendMessage(ConfigUtil.CHAT_GUI_GAME_NO_MONEY_CREATE.toString());
/*     */                 
/*     */                 return;
/*     */               } 
/*     */             } 
/*     */           } 
/* 203 */           if (GameInventory.this.hasWagers) {
/* 204 */             double d = Double.parseDouble((new StringBuilder()).append(param1HashMap.get("wager")).append("").toString());
/* 205 */             GamePlayer gamePlayer = GameInventory.this.game.addPlayer(player, str);
/*     */ 
/*     */             
/* 208 */             GameInventory.this.wagerManager.initGameWager(gamePlayer, d);
/*     */           } else {
/* 210 */             GameInventory.this.game.addPlayer(player, str);
/*     */           } 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 216 */           GameInventory.this.gameCreator = player;
/* 217 */           GameInventory.this.gameData = new HashMap<>(param1HashMap);
/*     */           
/* 219 */           GameInventory.this.game.setGameData(GameInventory.this.gameData);
/*     */           
/* 221 */           if (GameInventory.this.maxPlayers == 1) {
/* 222 */             GameInventory.this.onGameCreate(GameInventory.this.gameData, GameInventory.this.game.getGamePlayers());
/* 223 */             GameInventory.this.resetGameInventory(null, false);
/*     */           } else {
/* 225 */             GameInventory.this.gameWaitPlayersInventory.build(player, GameInventory.this.handleWaitPlayers());
/*     */           } 
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private WaitPlayersCallback handleWaitPlayers() {
/* 232 */     return new WaitPlayersCallback()
/*     */       {
/*     */         public void onAccept(Player param1Player)
/*     */         {
/* 236 */           if (!GameInventory.this.hasMoneyToAccept(param1Player)) {
/* 237 */             GameInventory.this.gameCreator.sendMessage(ConfigUtil.CHAT_GUI_GAME_NO_MONEY_ACCEPT.toString());
/* 238 */             param1Player.sendMessage(ConfigUtil.CHAT_GUI_GAME_NO_MONEY_JOIN.toString());
/*     */             
/* 240 */             GameInventory.this.joinPlayerQueue.remove(param1Player);
/* 241 */             GameInventory.this.closeInventory(param1Player);
/*     */             
/* 243 */             GameInventory.this.updateWaitPlayersInventory();
/*     */             
/*     */             return;
/*     */           } 
/* 247 */           GameInventory.this.gameCreator.sendMessage(ConfigUtil.CHAT_GUI_GAME_ACCEPT.buildString(param1Player.getDisplayName()));
/*     */           
/* 249 */           GameInventory.this.joinPlayerQueue.remove(param1Player);
/* 250 */           GameInventory.this.acceptedPlayers.add(param1Player);
/*     */ 
/*     */           
/* 253 */           GameInventory.this.game.addPlayer(param1Player);
/*     */ 
/*     */           
/* 256 */           if (GameInventory.this.hasWagers) {
/* 257 */             GameInventory.this.wagerManager.addGameWagerPlayer(param1Player, GameInventory.this.gameCreator);
/*     */           }
/*     */ 
/*     */           
/* 261 */           if (GameInventory.this.acceptedPlayers.size() == GameInventory.this.getMaxPlayers() - 1) {
/* 262 */             if (GameInventory.this.hasItemWagers && GameInventory.this.gameData.get("trade").equals(ConfigUtil.GUI_WAGERITEMS_ENABLED_LABEL.toString())) {
/* 263 */               GameInventory.this.moveToItemWager();
/*     */             } else {
/* 265 */               GameInventory.this.moveToReady();
/*     */             } 
/*     */           } else {
/* 268 */             GameInventory.this.updateWaitPlayersInventory();
/* 269 */             GameInventory.this.updateJoinGameInventory(param1Player);
/*     */           } 
/*     */         }
/*     */ 
/*     */         
/*     */         public void onDecline(Player param1Player) {
/* 275 */           GameInventory.this.gameCreator.sendMessage(ConfigUtil.CHAT_GUI_GAME_DECLINE.buildString(param1Player.getDisplayName()));
/*     */           
/* 277 */           GameInventory.this.joinPlayerQueue.remove(param1Player);
/*     */ 
/*     */           
/* 280 */           GameInventory.this.closeInventory(param1Player);
/*     */           
/* 282 */           GameInventory.this.updateWaitPlayersInventory();
/*     */         }
/*     */ 
/*     */         
/*     */         public void onStart() {
/* 287 */           GameInventory.this.moveToReady();
/*     */         }
/*     */ 
/*     */         
/*     */         public void onLeave() {
/* 292 */           GameInventory.this.resetGameInventory(ConfigUtil.CHAT_GUI_GAME_OWNER_LEFT.toString(), true);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private JoinGameCallback handleJoinGame() {
/* 298 */     return new JoinGameCallback()
/*     */       {
/*     */         public void onJoin(Player param1Player)
/*     */         {
/* 302 */           if (GameInventory.this.gameData == null || GameInventory.this.playerReadyMap.size() > 0) {
/* 303 */             GameInventory.this.closeInventory(param1Player);
/* 304 */             param1Player.sendMessage(ConfigUtil.CHAT_GUI_GAME_NO_AVAIL_GAME.toString());
/*     */             
/*     */             return;
/*     */           } 
/*     */           
/* 309 */           if (!GameInventory.this.hasMoneyToAccept(param1Player)) {
/* 310 */             param1Player.sendMessage(ConfigUtil.CHAT_GUI_GAME_NO_MONEY_JOIN.toString());
/*     */             
/*     */             return;
/*     */           } 
/* 314 */           if (GameInventory.this.joinPlayerQueue.size() < GameInventory.this.getMaxQueue()) {
/* 315 */             GameInventory.this.joinPlayerQueue.add(param1Player);
/*     */ 
/*     */             
/* 318 */             GameInventory.this.updateWaitPlayersInventory();
/* 319 */             GameInventory.this.updateJoinGameInventory(param1Player);
/*     */           } else {
/* 321 */             GameInventory.this.closeInventory(param1Player);
/* 322 */             param1Player.sendMessage(ConfigUtil.CHAT_GUI_GAME_FULL_QUEUE.toString());
/*     */           } 
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public void onLeave(Player param1Player) {
/* 329 */           boolean bool = (GameInventory.this.joinPlayerQueue.contains(param1Player) || GameInventory.this.acceptedPlayers.contains(param1Player)) ? true : false;
/* 330 */           GameInventory.this.joinPlayerQueue.remove(param1Player);
/* 331 */           GameInventory.this.acceptedPlayers.remove(param1Player);
/*     */ 
/*     */           
/* 334 */           GameInventory.this.game.removePlayer(param1Player);
/*     */ 
/*     */           
/* 337 */           if (bool)
/* 338 */             GameInventory.this.updateWaitPlayersInventory(); 
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private GameReadyCallback handleReady() {
/* 344 */     return new GameReadyCallback()
/*     */       {
/*     */         public void onReady(Player param1Player) {
/* 347 */           GameInventory.this.playerReadyMap.put(param1Player, Boolean.valueOf(true));
/*     */ 
/*     */           
/* 350 */           boolean bool = true;
/* 351 */           for (Player player : GameInventory.this.playerReadyMap.keySet()) {
/* 352 */             if (!((Boolean)GameInventory.this.playerReadyMap.get(player)).booleanValue()) {
/* 353 */               bool = false;
/*     */               
/*     */               break;
/*     */             } 
/*     */           } 
/* 358 */           if (bool) {
/*     */             
/* 360 */             if (GameInventory.this.gameCreator != null) {
/*     */               
/* 362 */               GameInventory.this.onGameCreate(GameInventory.this.gameData, GameInventory.this.game.getGamePlayers());
/* 363 */               GameInventory.this.resetGameInventory(null, false);
/*     */             } 
/*     */           } else {
/* 366 */             GameInventory.this.updateReadyInventory();
/*     */           } 
/*     */         }
/*     */ 
/*     */         
/*     */         public void onLeave(Player param1Player) {
/* 372 */           GameInventory.this.resetGameInventory(ConfigUtil.CHAT_GUI_GAME_PLAYER_LEFT.toString(), true);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private GameWagerCallback handleWager() {
/* 378 */     return new GameWagerCallback()
/*     */       {
/*     */         public void onCreate(RequestWager param1RequestWager) {
/* 381 */           GameInventory.this.wagerManager.addRequestWager(param1RequestWager);
/*     */           
/* 383 */           GameInventory.this.updateWagerViewInventories();
/*     */         }
/*     */ 
/*     */         
/*     */         public void onCancel(RequestWager param1RequestWager) {
/* 388 */           GameInventory.this.wagerManager.cancelRequestWager(param1RequestWager);
/*     */           
/* 390 */           GameInventory.this.updateWagerViewInventories();
/*     */         }
/*     */ 
/*     */         
/*     */         public void onAccept(Player param1Player, RequestWager param1RequestWager) {
/* 395 */           GameInventory.this.wagerManager.acceptRequestWager(param1Player, param1RequestWager);
/*     */           
/* 397 */           Player player = param1RequestWager.getOwner();
/*     */           
/* 399 */           player.sendMessage(ConfigUtil.CHAT_GUI_WAGER_ACCEPT.buildString(param1Player.getDisplayName()));
/* 400 */           param1Player.sendMessage(ConfigUtil.CHAT_GUI_WAGER_ACCEPTED.buildString(player.getDisplayName()));
/*     */           
/* 402 */           GameInventory.this.updateWagerViewInventories();
/*     */         }
/*     */ 
/*     */         
/*     */         public void onLeave(Player param1Player) {
/* 407 */           GameInventory.this.wagerViewPlayers.remove(param1Player);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private GameForfeitCallback handleForfeit() {
/* 413 */     return new GameForfeitCallback()
/*     */       {
/*     */         public void onForfeit(Player param1Player) {
/* 416 */           if (GameInventory.this.game.isIngame() && GameInventory.this.game.hasPlayer(param1Player)) {
/* 417 */             GameInventory.this.game.exitPlayer(param1Player);
/*     */           }
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private GameTradeCallback handleItemWager() {
/* 424 */     return new GameTradeCallback()
/*     */       {
/*     */         public void onAccept(GameTrade param1GameTrade) {
/* 427 */           GameInventory.this.wagerManager.addWager((Wager)new ItemWager(param1GameTrade));
/*     */           
/* 429 */           GameInventory.this.moveToReady();
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public void onLeave(GameTrade param1GameTrade) {
/* 435 */           param1GameTrade.sendBackItems();
/* 436 */           param1GameTrade.cancelTimer();
/*     */           
/* 438 */           GameInventory.this.resetGameInventory(ConfigUtil.CHAT_GUI_GAME_PLAYER_LEFT.toString(), true);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private void updateWaitPlayersInventory() {
/* 444 */     this.gameWaitPlayersInventory.build(this.gameCreator, handleWaitPlayers());
/*     */   }
/*     */   
/*     */   private void updateJoinGameInventory(Player paramPlayer) {
/* 448 */     this.gameJoinInventory.build(paramPlayer, handleJoinGame());
/*     */   }
/*     */   
/*     */   private void updateReadyInventory() {
/* 452 */     for (Player player : this.playerReadyMap.keySet()) {
/* 453 */       this.gameReadyInventory.build(player, handleReady());
/*     */     }
/*     */   }
/*     */   
/*     */   private void updateWagerViewInventories() {
/* 458 */     for (Player player : this.wagerViewPlayers.keySet()) {
/* 459 */       this.gameWagerInventory.build(player, handleWager());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void moveToReady() {
/* 465 */     closePlayers(this.joinPlayerQueue, ConfigUtil.CHAT_GUI_GAME_ALREADY_CREATED.toString());
/*     */ 
/*     */     
/* 468 */     this.playerReadyMap.put(this.gameCreator, Boolean.valueOf(false));
/* 469 */     for (Player player : this.acceptedPlayers) {
/* 470 */       this.playerReadyMap.put(player, Boolean.valueOf(false));
/* 471 */       this.gameReadyInventory.build(player, handleReady());
/*     */     } 
/*     */     
/* 474 */     this.gameReadyInventory.build(this.gameCreator, handleReady());
/*     */   }
/*     */ 
/*     */   
/*     */   private void moveToItemWager() {
/* 479 */     closePlayers(this.joinPlayerQueue, ConfigUtil.CHAT_GUI_GAME_ALREADY_CREATED.toString());
/*     */     
/* 481 */     if (this.game.getGamePlayers().size() != 2)
/*     */       return; 
/* 483 */     Player player1 = ((GamePlayer)this.game.getGamePlayers().get(0)).getPlayer();
/* 484 */     Player player2 = ((GamePlayer)this.game.getGamePlayers().get(1)).getPlayer();
/*     */     
/* 486 */     GameTradeCallback gameTradeCallback = handleItemWager();
/* 487 */     GameTrade gameTrade = new GameTrade(player1, player2, this.game, gameTradeCallback);
/* 488 */     (new GameTradeInventory(gameTrade, gameTradeCallback)).build(player1);
/* 489 */     (new GameTradeInventory(gameTrade, gameTradeCallback)).build(player2);
/*     */   }
/*     */ 
/*     */   
/*     */   private void resetGameInventory(String paramString, boolean paramBoolean) {
/* 494 */     if (this.gameCreator == null) {
/*     */       return;
/*     */     }
/*     */     
/* 498 */     closePlayers(this.joinPlayerQueue, paramString);
/*     */     
/* 500 */     closePlayers(this.acceptedPlayers, paramString);
/*     */     
/* 502 */     closePlayers(new ArrayList<>(this.wagerViewPlayers.keySet()), paramString);
/*     */     
/* 504 */     closeInventory(this.gameCreator);
/* 505 */     if (paramString != null) {
/* 506 */       this.gameCreator.sendMessage(paramString);
/*     */     }
/*     */     
/* 509 */     this.wagerManager.endAllRequestWagers();
/*     */ 
/*     */     
/* 512 */     this.joinPlayerQueue.clear();
/* 513 */     this.acceptedPlayers.clear();
/* 514 */     this.playerReadyMap.clear();
/* 515 */     this.wagerViewPlayers.clear();
/* 516 */     this.gameCreator = null;
/* 517 */     this.gameData = null;
/*     */     
/* 519 */     if (paramBoolean) {
/* 520 */       this.game.clearGamePlayers();
/*     */ 
/*     */       
/* 523 */       this.wagerManager.endAllWagers();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void closeInventory(Player paramPlayer) {
/* 528 */     InventoryGui inventoryGui = InventoryGui.get((InventoryHolder)paramPlayer);
/* 529 */     if (inventoryGui != null)
/* 530 */       inventoryGui.close(true); 
/*     */   }
/*     */   
/*     */   private void closePlayers(ArrayList<Player> paramArrayList, String paramString) {
/* 534 */     for (Player player : paramArrayList) {
/* 535 */       closeInventory(player);
/* 536 */       if (paramString != null)
/* 537 */         player.sendMessage(paramString); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public Game getGame() {
/* 542 */     return this.game;
/*     */   }
/*     */ 
/*     */   
/*     */   public ArrayList<GameOption> getGameOptions() {
/* 547 */     return new ArrayList<>(this.gameOptions);
/*     */   }
/*     */   
/*     */   public ArrayList<Player> getJoinPlayerQueue() {
/* 551 */     return new ArrayList<>(this.joinPlayerQueue);
/*     */   }
/*     */   
/*     */   public ArrayList<Player> getAcceptedPlayers() {
/* 555 */     return new ArrayList<>(this.acceptedPlayers);
/*     */   }
/*     */   
/*     */   public WagerManager getWagerManager() {
/* 559 */     return this.wagerManager;
/*     */   }
/*     */   
/*     */   public WagerOption getWagerOption(Player paramPlayer) {
/* 563 */     return this.wagerViewPlayers.get(paramPlayer);
/*     */   }
/*     */   
/*     */   public Object getGameData(String paramString) {
/* 567 */     return this.gameData.get(paramString);
/*     */   }
/*     */   
/*     */   public Player getGameCreator() {
/* 571 */     return this.gameCreator;
/*     */   }
/*     */   
/*     */   public int getMaxPlayers() {
/* 575 */     return this.maxPlayers;
/*     */   }
/*     */   
/*     */   public int getMinPlayers() {
/* 579 */     return this.minPlayers;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<Player> getReadyPlayers() {
/* 584 */     return this.playerReadyMap.keySet();
/*     */   }
/*     */   
/*     */   public boolean getReadyStatus(Player paramPlayer) {
/* 588 */     return ((Boolean)this.playerReadyMap.get(paramPlayer)).booleanValue();
/*     */   }
/*     */   
/*     */   public int getNumReady() {
/* 592 */     byte b = 0;
/* 593 */     for (Player player : this.playerReadyMap.keySet()) {
/* 594 */       if (((Boolean)this.playerReadyMap.get(player)).booleanValue()) b++; 
/*     */     } 
/* 596 */     return b;
/*     */   }
/*     */   
/*     */   private double getGameWagerAmount() {
/* 600 */     if (this.hasWagers && this.gameData != null) {
/* 601 */       String str = (new StringBuilder()).append(this.gameData.get("wager")).append("").toString();
/* 602 */       if (MathUtils.isNumeric(str)) {
/* 603 */         return Double.parseDouble(str);
/*     */       }
/*     */     } 
/*     */     
/* 607 */     return 0.0D;
/*     */   }
/*     */   
/*     */   private double getGameDataNum(String paramString) {
/* 611 */     if (this.gameData != null && this.gameData.containsKey(paramString)) {
/* 612 */       String str = (new StringBuilder()).append(this.gameData.get(paramString)).append("").toString();
/* 613 */       if (MathUtils.isNumeric(str)) {
/* 614 */         return Double.parseDouble(str);
/*     */       }
/*     */     } 
/*     */     
/* 618 */     return 0.0D;
/*     */   }
/*     */   
/*     */   public String getCreateGameText() {
/* 622 */     return ConfigUtil.GUI_CREATE_GAME.toString();
/*     */   }
/*     */   
/*     */   public boolean hasCustomInGameInventory() {
/* 626 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void openCustomInGameInventory(Player paramPlayer) {}
/*     */   
/*     */   private boolean hasMoneyToAccept(Player paramPlayer) {
/* 633 */     if (this.hasWagers && this.instance.getEconomy().getBalance((OfflinePlayer)paramPlayer) < getGameWagerAmount()) {
/* 634 */       return false;
/*     */     }
/*     */     
/* 637 */     for (GameOption gameOption : this.gameOptions) {
/* 638 */       if (gameOption.requiresEconomy()) {
/* 639 */         double d = getGameDataNum(gameOption.getKey());
/* 640 */         if (this.instance.getEconomy().getBalance((OfflinePlayer)paramPlayer) < d) return false;
/*     */       
/*     */       } 
/*     */     } 
/* 644 */     return true;
/*     */   }
/*     */   
/*     */   protected abstract ArrayList<GameOption> getOptions();
/*     */   
/*     */   protected abstract int getMaxQueue();
/*     */   
/*     */   protected abstract int getMaxGame();
/*     */   
/*     */   protected abstract int getMinGame();
/*     */   
/*     */   protected abstract boolean hasTeamSelect();
/*     */   
/*     */   protected abstract boolean hasGameWagers();
/*     */   
/*     */   protected abstract boolean hasWagerScreen();
/*     */   
/*     */   protected abstract boolean hasForfeitScreen();
/*     */   
/*     */   protected abstract void onGameCreate(HashMap<String, Object> paramHashMap, ArrayList<GamePlayer> paramArrayList);
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\inventories\GameInventory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */