/*     */ package water.of.cup.boardgames.game.games.uno;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import water.of.cup.boardgames.config.ConfigUtil;
/*     */ import water.of.cup.boardgames.game.BoardItem;
/*     */ import water.of.cup.boardgames.game.Button;
/*     */ import water.of.cup.boardgames.game.Clock;
/*     */ import water.of.cup.boardgames.game.Game;
/*     */ import water.of.cup.boardgames.game.GameConfig;
/*     */ import water.of.cup.boardgames.game.GameImage;
/*     */ import water.of.cup.boardgames.game.GamePlayer;
/*     */ import water.of.cup.boardgames.game.MathUtils;
/*     */ import water.of.cup.boardgames.game.inventories.GameInventory;
/*     */ import water.of.cup.boardgames.game.storage.GameStorage;
/*     */ 
/*     */ 
/*     */ public class Uno
/*     */   extends Game
/*     */ {
/*     */   private boolean isWild;
/*     */   private UnoDeck deck;
/*     */   private HashMap<GamePlayer, Integer> playerBoardPosition;
/*     */   private HashMap<GamePlayer, UnoHand> playerHands;
/*     */   private HashMap<GamePlayer, ArrayList<Button>> playerCardButtons;
/*     */   private HashMap<GamePlayer, Button> handButtons;
/*     */   private ArrayList<Button> colorButtons;
/*     */   private UnoCard currentCard;
/*     */   private Button currentCardButton;
/*  33 */   private int middleCardSize = 2;
/*     */   
/*  35 */   private static final HashMap<String, String> COLOR_MAP = new HashMap<>();
/*     */   
/*     */   static {
/*  38 */     COLOR_MAP.put("RED", ConfigUtil.CHAT_GAME_UNO_COLOR_RED.toString());
/*  39 */     COLOR_MAP.put("BLUE", ConfigUtil.CHAT_GAME_UNO_COLOR_BLUE.toString());
/*  40 */     COLOR_MAP.put("YELLOW", ConfigUtil.CHAT_GAME_UNO_COLOR_YELLOW.toString());
/*  41 */     COLOR_MAP.put("GREEN", ConfigUtil.CHAT_GAME_UNO_COLOR_GREEN.toString());
/*     */   }
/*     */   
/*     */   public Uno(int paramInt) {
/*  45 */     super(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public void exitPlayer(Player paramPlayer) {
/*  50 */     super.exitPlayer(paramPlayer);
/*  51 */     if (!isIngame())
/*     */       return; 
/*  53 */     toggleHandButtons();
/*  54 */     for (GamePlayer gamePlayer : this.teamManager.getGamePlayers()) {
/*  55 */       setCardButtons(gamePlayer);
/*     */     }
/*  57 */     this.mapManager.renderBoard();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setMapInformation(int paramInt) {
/*  63 */     this.mapStructure = new int[][] { { 3, 4 }, { 1, 2 } };
/*  64 */     this.placedMapVal = 1;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void startGame() {
/*  69 */     super.startGame();
/*  70 */     this.deck = new UnoDeck();
/*     */ 
/*     */     
/*  73 */     this.currentCard = this.deck.drawCards(1).get(0);
/*  74 */     while (this.currentCard.getColor().equals("ALL")) {
/*  75 */       this.currentCard = this.deck.drawCards(1).get(0);
/*     */     }
/*  77 */     this.playerBoardPosition = new HashMap<>();
/*  78 */     this.playerCardButtons = new HashMap<>();
/*  79 */     this.handButtons = new HashMap<>();
/*  80 */     createPlayerHands();
/*     */     
/*  82 */     if (getConfigValue("middle_card_size") != null) {
/*  83 */       String str = getConfigValue("middle_card_size") + "";
/*  84 */       if (MathUtils.isNumeric(str)) {
/*  85 */         int i = Integer.parseInt(str);
/*  86 */         if (i > 0) {
/*  87 */           this.middleCardSize = i;
/*     */         }
/*     */       } 
/*     */     } 
/*  91 */     GameImage gameImage = this.currentCard.getGameImage().clone();
/*  92 */     gameImage.resize(this.middleCardSize);
/*  93 */     this.currentCardButton = new Button(this, gameImage, new int[] { 128 - 4 * this.middleCardSize, 128 - 7 * this.middleCardSize }, 0, "currentCardButton");
/*     */     
/*  95 */     this.currentCardButton.setClickable(false);
/*  96 */     this.buttons.add(this.currentCardButton);
/*     */     
/*  98 */     this.isWild = false;
/*     */ 
/*     */     
/* 101 */     this.colorButtons = new ArrayList<>();
/* 102 */     byte b = 0;
/* 103 */     double d = this.middleCardSize / 2.0D;
/* 104 */     for (String str : new String[] { "RED", "BLUE", "YELLOW", "GREEN" }) {
/* 105 */       GameImage gameImage1 = new GameImage("UNOCARD_" + str);
/* 106 */       gameImage1.resize(d);
/* 107 */       Button button = new Button(this, gameImage1, new int[] { 128 - (int)(8.0D * d) + b / 2 * (int)(8.0D * d), 128 - (int)(14.0D * d) + b % 2 * (int)(14.0D * d) }, 0, str);
/* 108 */       this.buttons.add(button);
/* 109 */       this.colorButtons.add(button);
/* 110 */       toggleColorButtons();
/* 111 */       b++;
/*     */     } 
/*     */     
/* 114 */     toggleHandButtons();
/* 115 */     this.mapManager.renderBoard();
/* 116 */     setInGame();
/*     */   }
/*     */   
/*     */   private void toggleColorButtons() {
/* 120 */     this.currentCardButton.setVisibleForAll(!this.isWild);
/* 121 */     for (Button button : this.colorButtons) {
/* 122 */       button.setVisibleForAll(this.isWild);
/* 123 */       button.setClickable(this.isWild);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void toggleHandButtons() {
/* 128 */     for (Button button : this.handButtons.values()) {
/* 129 */       button.setImage("UNO_DECK");
/*     */     }
/* 131 */     ((Button)this.handButtons.get(this.teamManager.getTurnPlayer())).setImage("UNO_DECK_TURN");
/*     */   }
/*     */   
/*     */   private void createPlayerHands() {
/* 135 */     byte b = 0;
/* 136 */     this.playerHands = new HashMap<>();
/* 137 */     for (GamePlayer gamePlayer : getGamePlayers()) {
/*     */ 
/*     */       
/* 140 */       this.playerBoardPosition.put(gamePlayer, Integer.valueOf(b));
/* 141 */       this.playerHands.put(gamePlayer, new UnoHand());
/* 142 */       playerDrawCards(gamePlayer, 7);
/* 143 */       this.playerCardButtons.put(gamePlayer, new ArrayList<>());
/*     */ 
/*     */       
/* 146 */       int i = (b + 1) / 2;
/* 147 */       int[] arrayOfInt = { 0, 0 };
/*     */ 
/*     */ 
/*     */       
/* 151 */       arrayOfInt[1] = arrayOfInt[1] + (1 - (b + 2) / 4) * 128;
/* 152 */       arrayOfInt[0] = arrayOfInt[0] + (1 - b / 4) * 128;
/*     */       
/* 154 */       if (arrayOfInt[1] < 0) arrayOfInt[1] = arrayOfInt[1] * -1;
/*     */ 
/*     */       
/* 157 */       Button button = new Button(this, "UNO_DECK", arrayOfInt, (4 - i) % 4, "deck");
/* 158 */       this.handButtons.put(gamePlayer, button);
/* 159 */       this.buttons.add(button);
/*     */       
/* 161 */       setCardButtons(gamePlayer);
/* 162 */       b++;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void playerDrawCards(GamePlayer paramGamePlayer, int paramInt) {
/* 168 */     UnoHand unoHand = this.playerHands.get(paramGamePlayer);
/* 169 */     unoHand.draw(this.deck, paramInt);
/*     */   }
/*     */   
/*     */   private void setCardButtons(GamePlayer paramGamePlayer) {
/* 173 */     ArrayList<?> arrayList = this.playerCardButtons.get(paramGamePlayer);
/* 174 */     this.buttons.removeAll(arrayList);
/* 175 */     arrayList.clear();
/*     */     
/* 177 */     UnoHand unoHand = this.playerHands.get(paramGamePlayer);
/* 178 */     int i = ((Integer)this.playerBoardPosition.get(paramGamePlayer)).intValue();
/*     */ 
/*     */     
/* 181 */     int j = (i + 1) / 2;
/*     */     
/* 183 */     int[] arrayOfInt = { 33, 96 };
/*     */     byte b;
/* 185 */     for (b = 4; b > j; b--) {
/* 186 */       arrayOfInt = MathUtils.rotatePointAroundPoint90Degrees(new double[] { 63.5D, 63.5D }, arrayOfInt);
/*     */     } 
/*     */     
/* 189 */     arrayOfInt[1] = arrayOfInt[1] + (1 - (i + 2) / 4) * 128;
/* 190 */     arrayOfInt[0] = arrayOfInt[0] + (1 - i / 4) * 128;
/*     */     
/* 192 */     if (arrayOfInt[1] < 0) arrayOfInt[1] = 256 + arrayOfInt[1];
/*     */     
/* 194 */     b = 0;
/* 195 */     for (UnoCard unoCard : unoHand.getCards(this.currentCard)) {
/* 196 */       int[] arrayOfInt1 = { b % 7 * 9, b / 7 * 15 };
/*     */       
/* 198 */       for (byte b1 = 4; b1 > j; b1--) {
/* 199 */         arrayOfInt1 = MathUtils.rotatePointAroundPoint90Degrees(new double[] { 0.0D, 0.0D }, arrayOfInt1);
/*     */       } 
/*     */       
/* 202 */       int[] arrayOfInt2 = { arrayOfInt[0] + arrayOfInt1[0], arrayOfInt[1] + arrayOfInt1[1] };
/*     */ 
/*     */       
/* 205 */       Button button = new Button(this, unoCard.getGameImage(), arrayOfInt2, (4 - j) % 4, "" + b);
/* 206 */       button.changeLocationByRotation();
/* 207 */       button.setVisibleForAll(false);
/* 208 */       button.addVisiblePlayer(paramGamePlayer);
/* 209 */       arrayList.add(button);
/* 210 */       this.buttons.add(button);
/* 211 */       button.setClickable(true);
/* 212 */       b++;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private UnoCard getSelectedCard(GamePlayer paramGamePlayer, Button paramButton) {
/* 218 */     ArrayList arrayList = this.playerCardButtons.get(paramGamePlayer);
/* 219 */     int i = arrayList.indexOf(paramButton);
/* 220 */     if (i == -1)
/* 221 */       return null; 
/* 222 */     UnoHand unoHand = this.playerHands.get(paramGamePlayer);
/* 223 */     return unoHand.getCards(this.currentCard).get(i);
/*     */   }
/*     */   
/*     */   private boolean playCard(GamePlayer paramGamePlayer, UnoCard paramUnoCard) {
/* 227 */     if (paramUnoCard.matches(this.currentCard)) {
/* 228 */       UnoHand unoHand = this.playerHands.get(paramGamePlayer);
/* 229 */       unoHand.removeCard(paramUnoCard);
/* 230 */       if (unoHand.cardsLeft() == 1)
/*     */       {
/* 232 */         getGamePlayers()
/* 233 */           .forEach(paramGamePlayer2 -> paramGamePlayer2.getPlayer().sendMessage(ConfigUtil.CHAT_GAME_UNO_LAST_CARD.buildString(paramGamePlayer1.getPlayer().getDisplayName())));
/*     */       }
/*     */       
/* 236 */       this.currentCard = paramUnoCard;
/*     */       
/* 238 */       GameImage gameImage = this.currentCard.getGameImage().clone();
/* 239 */       gameImage.resize(this.middleCardSize);
/* 240 */       this.currentCardButton.setImage(gameImage);
/*     */       
/* 242 */       if (unoHand.cardsLeft() == 0) {
/* 243 */         endGame(paramGamePlayer);
/* 244 */         return false;
/*     */       } 
/*     */       
/* 247 */       setCardButtons(paramGamePlayer);
/*     */ 
/*     */       
/* 250 */       if (paramUnoCard.getColor().equals("ALL")) {
/* 251 */         this.isWild = true;
/* 252 */         toggleColorButtons();
/* 253 */         return true;
/*     */       } 
/*     */       
/* 256 */       doCardActions(paramUnoCard);
/*     */       
/* 258 */       return true;
/*     */     } 
/* 260 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private void doCardActions(UnoCard paramUnoCard) {
/* 265 */     this.teamManager.nextTurn();
/* 266 */     GamePlayer gamePlayer = this.teamManager.getTurnPlayer();
/*     */     
/* 268 */     for (String str : paramUnoCard.getActions()) {
/* 269 */       if (str.equals("DRAW2")) {
/* 270 */         ((UnoHand)this.playerHands.get(gamePlayer)).draw(this.deck, 2);
/* 271 */         gamePlayer.getPlayer().sendMessage(ConfigUtil.CHAT_GAME_UNO_FORCE_2.toString());
/* 272 */         setCardButtons(gamePlayer);
/*     */       
/*     */       }
/* 275 */       else if (str.equals("DRAW4")) {
/* 276 */         ((UnoHand)this.playerHands.get(gamePlayer)).draw(this.deck, 4);
/* 277 */         gamePlayer.getPlayer().sendMessage(ConfigUtil.CHAT_GAME_UNO_FORCE_4.toString());
/* 278 */         setCardButtons(gamePlayer);
/*     */       
/*     */       }
/* 281 */       else if (str.equals("SKIP")) {
/* 282 */         this.teamManager.nextTurn();
/* 283 */         gamePlayer.getPlayer().sendMessage(ConfigUtil.CHAT_GAME_UNO_SKIPPED.toString());
/*     */       
/*     */       }
/* 286 */       else if (str.equals("REVERSE")) {
/* 287 */         this.teamManager.switchTurnDirection();
/* 288 */         this.teamManager.nextTurn();
/* 289 */         this.teamManager.nextTurn();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setGameName() {
/* 297 */     this.gameName = "Uno";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setBoardImage() {
/* 303 */     this.gameImage = new GameImage("UNO_BOARD");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Clock getClock() {
/* 310 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected GameInventory getGameInventory() {
/* 315 */     return new UnoInventory(this);
/*     */   }
/*     */ 
/*     */   
/*     */   protected GameStorage getGameStorage() {
/* 320 */     return new UnoStorage(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public ArrayList<String> getTeamNames() {
/* 325 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected GameConfig getGameConfig() {
/* 330 */     return new UnoConfig(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void click(Player paramPlayer, double[] paramArrayOfdouble, ItemStack paramItemStack) {
/* 335 */     GamePlayer gamePlayer = getGamePlayer(paramPlayer);
/* 336 */     if (gamePlayer == null) {
/*     */       return;
/*     */     }
/*     */     
/* 340 */     if (gamePlayer != this.teamManager.getTurnPlayer()) {
/* 341 */       paramPlayer.sendMessage(ConfigUtil.CHAT_GAME_UNO_NOT_YOUR_TURN.toString());
/*     */       
/*     */       return;
/*     */     } 
/* 345 */     int[] arrayOfInt = this.mapManager.getClickLocation(paramArrayOfdouble, paramItemStack);
/* 346 */     Button button = getClickedButton(gamePlayer, arrayOfInt);
/*     */     
/* 348 */     if (button == null) {
/*     */       return;
/*     */     }
/*     */     
/* 352 */     if (this.isWild) {
/* 353 */       if (this.colorButtons.contains(button)) {
/* 354 */         this.isWild = false;
/* 355 */         toggleColorButtons();
/* 356 */         this.currentCard.setColor(button.getName());
/* 357 */         doCardActions(this.currentCard);
/* 358 */         toggleHandButtons();
/* 359 */         setCardButtons(this.teamManager.getTurnPlayer());
/* 360 */         this.mapManager.renderBoard();
/* 361 */         getGamePlayers().forEach(paramGamePlayer -> paramGamePlayer.getPlayer().sendMessage(buildColorString(paramPlayer, paramButton.getName())));
/*     */       } else {
/*     */         
/* 364 */         paramPlayer.sendMessage(ConfigUtil.CHAT_GAME_UNO_SELECT_COLOR.toString());
/*     */       } 
/*     */       
/*     */       return;
/*     */     } 
/* 369 */     UnoHand unoHand = this.playerHands.get(gamePlayer);
/* 370 */     UnoCard unoCard = getSelectedCard(gamePlayer, button);
/*     */     
/* 372 */     if (!unoHand.canPlay(this.currentCard)) {
/* 373 */       paramPlayer.sendMessage(ConfigUtil.CHAT_GAME_UNO_FORCE_DRAW.toString());
/* 374 */       UnoCard unoCard1 = unoHand.draw(this.deck, 1).get(0);
/* 375 */       if (!this.currentCard.matches(unoCard1) && !unoCard1.getColor().equals("ALL")) {
/* 376 */         setCardButtons(gamePlayer);
/* 377 */         this.teamManager.nextTurn();
/* 378 */         toggleHandButtons();
/* 379 */         this.mapManager.renderBoard();
/*     */         
/*     */         return;
/*     */       } 
/* 383 */       unoCard = unoCard1;
/*     */     } 
/*     */     
/* 386 */     if (unoCard == null) {
/*     */       return;
/*     */     }
/* 389 */     if (playCard(gamePlayer, unoCard)) {
/* 390 */       playGameSound("click");
/*     */ 
/*     */       
/* 393 */       toggleHandButtons();
/* 394 */       setCardButtons(this.teamManager.getTurnPlayer());
/* 395 */       this.mapManager.renderBoard();
/* 396 */     } else if (!unoCard.matches(this.currentCard)) {
/* 397 */       paramPlayer.sendMessage(ConfigUtil.CHAT_GAME_UNO_INVALID_CARD.toString());
/*     */     } else {
/*     */       
/* 400 */       this.mapManager.renderBoard();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void gamePlayerOutOfTime(GamePlayer paramGamePlayer) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BoardItem getBoardItem() {
/* 419 */     return new BoardItem(getAltName(), new ItemStack(Material.SPRUCE_TRAPDOOR, 1));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void clockOutOfTime() {}
/*     */ 
/*     */ 
/*     */   
/*     */   private String buildColorString(Player paramPlayer, String paramString) {
/* 429 */     String str = ConfigUtil.CHAT_GAME_UNO_COLOR.toString();
/*     */     
/* 431 */     str = str.replace("%player%", paramPlayer.getDisplayName()).replace("%color%", COLOR_MAP.get(paramString));
/*     */     
/* 433 */     return str;
/*     */   }
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\game\\uno\Uno.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */