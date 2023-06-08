/*     */ package water.of.cup.boardgames.game.games.chess;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import water.of.cup.boardgames.game.BoardItem;
/*     */ import water.of.cup.boardgames.game.Button;
/*     */ import water.of.cup.boardgames.game.Clock;
/*     */ import water.of.cup.boardgames.game.Game;
/*     */ import water.of.cup.boardgames.game.GameConfig;
/*     */ import water.of.cup.boardgames.game.GameImage;
/*     */ import water.of.cup.boardgames.game.GamePlayer;
/*     */ import water.of.cup.boardgames.game.inventories.GameInventory;
/*     */ import water.of.cup.boardgames.game.storage.GameStorage;
/*     */ 
/*     */ 
/*     */ public class Chess
/*     */   extends Game
/*     */ {
/*     */   private ChessBoard board;
/*     */   private Button[][] boardButtons;
/*     */   private int[] selected;
/*     */   private String promotion;
/*     */   private Button[] promotionButtons;
/*  26 */   private String[] promotionNames = new String[] { "ROOK", "KNIGHT", "BISHOP", "QUEEN" };
/*     */   
/*     */   public Chess(int paramInt) {
/*  29 */     super(paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void renderInitial() {
/*  35 */     super.renderInitial();
/*  36 */     setBoard();
/*  37 */     paintBoard();
/*  38 */     this.mapManager.renderBoard();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setMapInformation(int paramInt) {
/*  43 */     this.mapStructure = new int[][] { { 1 } };
/*  44 */     this.placedMapVal = 1;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void startGame() {
/*  49 */     super.startGame();
/*  50 */     setInGame();
/*  51 */     setBoard();
/*  52 */     paintBoard();
/*     */   }
/*     */   
/*     */   private void setBoard() {
/*  56 */     this.selected = null;
/*  57 */     this.promotion = "NONE";
/*  58 */     this.buttons.clear();
/*  59 */     this.board = new ChessBoard();
/*  60 */     this.boardButtons = new Button[8][8];
/*  61 */     this.promotionButtons = new Button[4];
/*     */     byte b;
/*  63 */     for (b = 0; b < 4; b++) {
/*  64 */       Button button = new Button(this, "CHESS_EMPTY", new int[] { 24 + b * 16, 56 }, 0, "PROMOTION");
/*  65 */       button.setClickable(false);
/*  66 */       this.promotionButtons[b] = button;
/*  67 */       this.buttons.add(button);
/*     */     } 
/*     */     
/*  70 */     for (b = 0; b < 8; b++) {
/*  71 */       for (byte b1 = 0; b1 < 8; b1++) {
/*  72 */         Button button = new Button(this, "CHESS_EMPTY", new int[] { b * 16, b1 * 16 }, 0, "PIECE");
/*  73 */         button.setClickable(true);
/*  74 */         this.boardButtons[b1][b] = button;
/*  75 */         this.buttons.add(button);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   private void togglePromotionButtons() {
/*  80 */     for (byte b = 0; b < 4; b++) {
/*  81 */       if (this.promotion == "NONE") {
/*  82 */         this.promotionButtons[b].setClickable(false);
/*  83 */         this.promotionButtons[b].setVisibleForAll(false);
/*     */       } else {
/*  85 */         ChessPiece chessPiece = ChessPiece.valueOf(this.promotion + "_" + this.promotionNames[b]);
/*  86 */         this.promotionButtons[b].setImage(chessPiece.getImage());
/*  87 */         this.promotionButtons[b].setClickable(true);
/*  88 */         this.promotionButtons[b].setVisibleForAll(true);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   private int[] getButtonLocation(Button paramButton) {
/*  93 */     for (byte b = 0; b < 8; b++) {
/*  94 */       for (byte b1 = 0; b1 < 8; b1++) {
/*  95 */         if (paramButton == this.boardButtons[b][b1])
/*  96 */           return new int[] { b1, b }; 
/*     */       } 
/*     */     } 
/*  99 */     return null;
/*     */   }
/*     */   
/*     */   private int getPromotionButtonLocation(Button paramButton) {
/* 103 */     for (byte b = 0; b < 4; b++) {
/* 104 */       if (paramButton == this.promotionButtons[b]) {
/* 105 */         return b;
/*     */       }
/*     */     } 
/* 108 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setGameName() {
/* 113 */     this.gameName = "Chess";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setBoardImage() {
/* 119 */     this.gameImage = new GameImage("CHESS_BOARD");
/*     */   }
/*     */   
/*     */   private void paintBoard() {
/* 123 */     boolean[][] arrayOfBoolean = new boolean[8][8];
/* 124 */     if (this.selected != null && this.board.getStructure()[this.selected[1]][this.selected[0]] != null) {
/* 125 */       arrayOfBoolean = this.board.getMoves(this.selected);
/*     */     }
/*     */     
/* 128 */     ChessPiece[][] arrayOfChessPiece = this.board.getStructure();
/*     */     
/* 130 */     for (byte b = 0; b < 8; b++) {
/* 131 */       for (byte b1 = 0; b1 < 8; b1++) {
/* 132 */         Button button = this.boardButtons[b][b1];
/* 133 */         button.setImage("CHESS_EMPTY");
/* 134 */         if (this.selected != null && this.selected[0] == b1 && this.selected[1] == b)
/* 135 */           button.setImage("CHESS_SELECTED"); 
/* 136 */         if (arrayOfBoolean[b][b1])
/* 137 */           button.setImage("CHESS_MOVE"); 
/* 138 */         ChessPiece chessPiece = arrayOfChessPiece[b][b1];
/* 139 */         if (chessPiece != null) {
/* 140 */           button.getImage().addGameImage(new GameImage(chessPiece.getImage(), 0), new int[] { 0, 0 });
/*     */         }
/*     */       } 
/*     */     } 
/* 144 */     togglePromotionButtons();
/* 145 */     this.mapManager.renderBoard();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Clock getClock() {
/* 152 */     Clock clock = new Clock(getClockTime(), this, true);
/* 153 */     clock.setIncrement(getClockIncrement());
/* 154 */     return clock;
/*     */   }
/*     */   
/*     */   private int getClockTime() {
/* 158 */     String str = (String)getGameData("time");
/* 159 */     int i = (new Integer(str.substring(0, str.indexOf(" ")))).intValue();
/* 160 */     return i * 60;
/*     */   }
/*     */ 
/*     */   
/*     */   private int getClockIncrement() {
/* 165 */     String str = (String)getGameData("time");
/* 166 */     if (str.contains("|")) {
/* 167 */       int i = str.indexOf("|");
/* 168 */       return (new Integer(str.substring(i + 2, i + 3))).intValue();
/*     */     } 
/* 170 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected GameInventory getGameInventory() {
/* 177 */     return new ChessInventory(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public ArrayList<String> getTeamNames() {
/* 182 */     ArrayList<String> arrayList = new ArrayList();
/* 183 */     arrayList.add("WHITE");
/* 184 */     arrayList.add("BLACK");
/* 185 */     return arrayList;
/*     */   }
/*     */ 
/*     */   
/*     */   public void click(Player paramPlayer, double[] paramArrayOfdouble, ItemStack paramItemStack) {
/* 190 */     GamePlayer gamePlayer = getGamePlayer(paramPlayer);
/* 191 */     if (!this.teamManager.getTurnPlayer().equals(gamePlayer)) {
/*     */       return;
/*     */     }
/* 194 */     int[] arrayOfInt1 = this.mapManager.getClickLocation(paramArrayOfdouble, paramItemStack);
/* 195 */     Button button = getClickedButton(gamePlayer, arrayOfInt1);
/*     */     
/* 197 */     String str = this.teamManager.getTurnTeam();
/*     */     
/* 199 */     if (!this.board.getPawnPromotion().equals("NONE")) {
/* 200 */       int i = getPromotionButtonLocation(button);
/* 201 */       if (i == -1)
/*     */         return; 
/* 203 */       this.board.promotePawn(str, ChessPiece.valueOf(this.promotion + "_" + this.promotionNames[i]));
/* 204 */       this.promotion = "NONE";
/* 205 */       this.clock.run();
/* 206 */       this.teamManager.nextTurn();
/* 207 */       paintBoard();
/*     */       
/*     */       return;
/*     */     } 
/* 211 */     int[] arrayOfInt2 = getButtonLocation(button);
/*     */     
/* 213 */     if (arrayOfInt2 == null) {
/*     */       return;
/*     */     }
/* 216 */     if (this.selected == null) {
/* 217 */       ChessPiece chessPiece1 = this.board.getStructure()[arrayOfInt2[1]][arrayOfInt2[0]];
/* 218 */       if (chessPiece1 != null && chessPiece1.getColor().equals(str))
/* 219 */         this.selected = arrayOfInt2; 
/* 220 */       paintBoard();
/*     */       
/*     */       return;
/*     */     } 
/* 224 */     if (this.board.move(this.selected, arrayOfInt2, str)) {
/*     */       
/* 226 */       this.selected = null;
/*     */       
/* 228 */       playGameSound("click");
/*     */       
/* 230 */       if (!this.board.checkGameOver().equals("")) {
/*     */         
/* 232 */         GamePlayer gamePlayer1 = this.teamManager.getGamePlayerByTeam(this.board.checkGameOver());
/* 233 */         paintBoard();
/* 234 */         endGame(gamePlayer1);
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 239 */       if (this.board.getPawnPromotion().equals("NONE")) {
/*     */         
/* 241 */         this.clock.run();
/* 242 */         this.teamManager.nextTurn();
/* 243 */         paintBoard();
/*     */         
/*     */         return;
/*     */       } 
/* 247 */       this.promotion = this.board.getPawnPromotion();
/* 248 */       paintBoard();
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 253 */     ChessPiece chessPiece = this.board.getStructure()[arrayOfInt2[1]][arrayOfInt2[0]];
/* 254 */     if (chessPiece != null && chessPiece.getColor().equals(str)) {
/* 255 */       this.selected = arrayOfInt2;
/* 256 */       paintBoard();
/*     */       return;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void gamePlayerOutOfTime(GamePlayer paramGamePlayer) {
/* 265 */     endGame((this.teamManager.getTurnPlayer() == paramGamePlayer) ? this.teamManager.nextTurn() : this.teamManager.getTurnPlayer());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack getBoardItem() {
/* 271 */     return (ItemStack)new BoardItem(getAltName(), new ItemStack(Material.OAK_TRAPDOOR, 1));
/*     */   }
/*     */ 
/*     */   
/*     */   protected GameConfig getGameConfig() {
/* 276 */     return new ChessConfig(this);
/*     */   }
/*     */ 
/*     */   
/*     */   protected GameStorage getGameStorage() {
/* 281 */     return new ChessStorage(this);
/*     */   }
/*     */   
/*     */   protected void clockOutOfTime() {}
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\games\chess\Chess.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */