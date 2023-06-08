/*     */ package water.of.cup.boardgames.game.games.checkers;
/*     */ 
/*     */ import java.util.ArrayList;
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
/*     */ import water.of.cup.boardgames.game.inventories.GameInventory;
/*     */ import water.of.cup.boardgames.game.storage.GameStorage;
/*     */ 
/*     */ public class Checkers extends Game {
/*     */   String[][] board;
/*     */   int[] selected;
/*     */   Button[][] boardButtons;
/*  22 */   int movesSinceCapture = 0;
/*     */   
/*     */   boolean canDeSelect;
/*     */   
/*     */   public Checkers(int paramInt) {
/*  27 */     super(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setMapInformation(int paramInt) {
/*  32 */     this.mapStructure = new int[][] { { 1 } };
/*  33 */     this.placedMapVal = 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderInitial() {
/*  38 */     super.renderInitial();
/*  39 */     setUpBoard();
/*  40 */     this.mapManager.renderBoard();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void startGame() {
/*  45 */     super.startGame();
/*  46 */     setInGame();
/*  47 */     setUpBoard();
/*  48 */     this.mapManager.renderBoard();
/*     */   }
/*     */   
/*     */   private void setUpBoard() {
/*  52 */     this.buttons.clear();
/*     */     
/*  54 */     this.board = new String[8][8];
/*  55 */     this.boardButtons = new Button[8][8];
/*  56 */     for (byte b = 0; b < 8; b++) {
/*  57 */       for (byte b1 = 0; b1 < 8; b1++) {
/*  58 */         this.board[b1][b] = "EMPTY";
/*  59 */         if ((b + b1) % 2 != 1) {
/*     */           
/*  61 */           if (b1 > 5) {
/*  62 */             this.board[b1][b] = "RED";
/*  63 */           } else if (b1 < 2) {
/*  64 */             this.board[b1][b] = "BLACK";
/*     */           } 
/*  66 */           Button button = new Button(this, "CHECKERS_EMPTY", new int[] { 4 + b * 15, 4 + b1 * 15 }, 0, "EMPTY");
/*  67 */           button.setClickable(true);
/*  68 */           this.boardButtons[b1][b] = button;
/*  69 */           this.buttons.add(button);
/*     */         } 
/*     */       } 
/*  72 */     }  updateButtons();
/*     */   }
/*     */   
/*     */   private void updateButtons() {
/*  76 */     for (byte b = 0; b < 8; b++) {
/*  77 */       for (byte b1 = 0; b1 < 8; b1++) {
/*  78 */         if (this.boardButtons[b1][b] != null) {
/*     */           
/*  80 */           this.boardButtons[b1][b].setImage("CHECKERS_" + this.board[b1][b]);
/*  81 */           if (this.selected != null && this.selected[0] == b && this.selected[1] == b1)
/*  82 */             this.boardButtons[b1][b].getImage().addGameImage(new GameImage("CHECKERS_BLACK_HIGHLIGHT"), new int[] { 0, 0 }); 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private int[] getButtonLocation(Button paramButton) {
/*  89 */     for (byte b = 0; b < 8; b++) {
/*  90 */       for (byte b1 = 0; b1 < 8; b1++) {
/*  91 */         if (paramButton == this.boardButtons[b][b1])
/*  92 */           return new int[] { b1, b }; 
/*     */       } 
/*     */     } 
/*  95 */     return null;
/*     */   }
/*     */   
/*     */   private boolean isOnBoard(int[] paramArrayOfint) {
/*  99 */     return (paramArrayOfint[0] >= 0 && paramArrayOfint[1] >= 0 && paramArrayOfint[0] <= 7 && paramArrayOfint[1] <= 7);
/*     */   }
/*     */   
/*     */   private boolean canJump(int[] paramArrayOfint) {
/* 103 */     String str = this.board[paramArrayOfint[1]][paramArrayOfint[0]].split("_")[0];
/* 104 */     if (str.equals("EMPTY")) {
/* 105 */       return false;
/*     */     }
/* 107 */     int[][] arrayOfInt = new int[0][0];
/*     */     
/* 109 */     if (this.board[paramArrayOfint[1]][paramArrayOfint[0]].contains("KING")) {
/* 110 */       arrayOfInt = new int[][] { { 1, 1 }, { -1, 1 }, { 1, -1 }, { -1, -1 } };
/* 111 */     } else if (str.equals("RED")) {
/* 112 */       arrayOfInt = new int[][] { { 1, -1 }, { -1, -1 } };
/* 113 */     } else if (str.equals("BLACK")) {
/* 114 */       arrayOfInt = new int[][] { { 1, 1 }, { -1, 1 } };
/*     */     } 
/*     */     
/* 117 */     for (int[] arrayOfInt1 : arrayOfInt) {
/*     */       
/* 119 */       if (isOnBoard(new int[] { paramArrayOfint[0] + arrayOfInt1[0] * 2, paramArrayOfint[1] + arrayOfInt1[1] * 2
/*     */           }))
/*     */       {
/* 122 */         if (!this.board[paramArrayOfint[1] + arrayOfInt1[1]][paramArrayOfint[0] + arrayOfInt1[0]].equals("EMPTY") && 
/* 123 */           !this.board[paramArrayOfint[1] + arrayOfInt1[1]][paramArrayOfint[0] + arrayOfInt1[0]].contains(str) && this.board[paramArrayOfint[1] + arrayOfInt1[1] * 2][paramArrayOfint[0] + arrayOfInt1[0] * 2]
/* 124 */           .equals("EMPTY"))
/* 125 */           return true; 
/*     */       }
/*     */     } 
/* 128 */     return false;
/*     */   }
/*     */   
/*     */   private boolean canAdvance(int[] paramArrayOfint) {
/* 132 */     String str = this.board[paramArrayOfint[1]][paramArrayOfint[0]].split("_")[0];
/* 133 */     if (str.equals("EMPTY")) {
/* 134 */       return false;
/*     */     }
/* 136 */     int[][] arrayOfInt = new int[0][0];
/* 137 */     if (this.board[paramArrayOfint[1]][paramArrayOfint[0]].contains("KING")) {
/* 138 */       arrayOfInt = new int[][] { { 1, 1 }, { -1, 1 }, { 1, -1 }, { -1, -1 } };
/* 139 */     } else if (str.equals("RED")) {
/* 140 */       arrayOfInt = new int[][] { { 1, -1 }, { -1, -1 } };
/* 141 */     } else if (str.equals("BLACK")) {
/* 142 */       arrayOfInt = new int[][] { { 1, 1 }, { -1, 1 } };
/*     */     } 
/*     */ 
/*     */     
/* 146 */     for (int[] arrayOfInt1 : arrayOfInt) {
/*     */       
/* 148 */       if (isOnBoard(new int[] { paramArrayOfint[0] + arrayOfInt1[0], paramArrayOfint[1] + arrayOfInt1[1]
/*     */           }))
/*     */       {
/* 151 */         if (this.board[paramArrayOfint[1] + arrayOfInt1[1]][paramArrayOfint[0] + arrayOfInt1[0]].equals("EMPTY"))
/* 152 */           return true;  } 
/*     */     } 
/* 154 */     return false;
/*     */   }
/*     */   
/*     */   private boolean canMove(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/* 158 */     String str = this.board[paramArrayOfint1[1]][paramArrayOfint1[0]].split("_")[0];
/* 159 */     if (str.equals("EMPTY")) {
/* 160 */       return false;
/*     */     }
/* 162 */     int[][] arrayOfInt = new int[0][0];
/* 163 */     if (this.board[paramArrayOfint1[1]][paramArrayOfint1[0]].contains("KING")) {
/* 164 */       arrayOfInt = new int[][] { { 1, 1 }, { -1, 1 }, { 1, -1 }, { -1, -1 } };
/* 165 */     } else if (str.equals("RED")) {
/* 166 */       arrayOfInt = new int[][] { { 1, -1 }, { -1, -1 } };
/* 167 */     } else if (str.equals("BLACK")) {
/* 168 */       arrayOfInt = new int[][] { { 1, 1 }, { -1, 1 } };
/*     */     } 
/*     */ 
/*     */     
/* 172 */     for (int[] arrayOfInt1 : arrayOfInt) {
/*     */       
/* 174 */       if (isOnBoard(new int[] { paramArrayOfint1[0] + arrayOfInt1[0] * 2, paramArrayOfint1[1] + arrayOfInt1[1] * 2
/*     */           }))
/*     */       {
/* 177 */         if (!this.board[paramArrayOfint1[1] + arrayOfInt1[1]][paramArrayOfint1[0] + arrayOfInt1[0]].equals("EMPTY") && 
/* 178 */           !this.board[paramArrayOfint1[1] + arrayOfInt1[1]][paramArrayOfint1[0] + arrayOfInt1[0]].contains(str) && this.board[paramArrayOfint1[1] + arrayOfInt1[1] * 2][paramArrayOfint1[0] + arrayOfInt1[0] * 2]
/* 179 */           .equals("EMPTY") && paramArrayOfint1[1] + arrayOfInt1[1] * 2 == paramArrayOfint2[1] && paramArrayOfint1[0] + arrayOfInt1[0] * 2 == paramArrayOfint2[0])
/*     */         {
/* 181 */           return true;
/*     */         }
/*     */       }
/*     */     } 
/* 185 */     if (colorCanJump(str)) {
/* 186 */       return false;
/*     */     }
/*     */     
/* 189 */     for (int[] arrayOfInt1 : arrayOfInt) {
/*     */       
/* 191 */       if (isOnBoard(new int[] { paramArrayOfint1[0] + arrayOfInt1[0], paramArrayOfint1[1] + arrayOfInt1[1]
/*     */           }))
/*     */       {
/* 194 */         if (this.board[paramArrayOfint1[1] + arrayOfInt1[1]][paramArrayOfint1[0] + arrayOfInt1[0]].equals("EMPTY") && paramArrayOfint1[1] + arrayOfInt1[1] == paramArrayOfint2[1] && paramArrayOfint1[0] + arrayOfInt1[0] == paramArrayOfint2[0])
/*     */         {
/* 196 */           return true; } 
/*     */       }
/*     */     } 
/* 199 */     return false;
/*     */   }
/*     */   
/*     */   private boolean colorCanJump(String paramString) {
/* 203 */     for (byte b = 0; b < 8; b++) {
/* 204 */       for (byte b1 = 0; b1 < 8; b1++) {
/* 205 */         if (this.board[b1][b].contains(paramString) && canJump(new int[] { b, b1 }))
/* 206 */           return true; 
/*     */       } 
/*     */     } 
/* 209 */     return false;
/*     */   }
/*     */   
/*     */   private void promotePieces() {
/* 213 */     for (byte b = 0; b < 8; b++) {
/* 214 */       if (this.board[0][b].equals("RED")) {
/* 215 */         this.board[0][b] = "RED_KING";
/* 216 */       } else if (this.board[7][b].equals("BLACK")) {
/* 217 */         this.board[7][b] = "BLACK_KING";
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void setGameName() {
/* 223 */     this.gameName = "Checkers";
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setBoardImage() {
/* 228 */     this.gameImage = new GameImage("CHECKERS_BOARD");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Clock getClock() {
/* 235 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected GameInventory getGameInventory() {
/* 240 */     return new CheckersInventory(this);
/*     */   }
/*     */ 
/*     */   
/*     */   protected GameStorage getGameStorage() {
/* 245 */     return new CheckersStorage(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public ArrayList<String> getTeamNames() {
/* 250 */     return new ArrayList<String>()
/*     */       {
/*     */       
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   protected GameConfig getGameConfig() {
/* 258 */     return new CheckersConfig(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void click(Player paramPlayer, double[] paramArrayOfdouble, ItemStack paramItemStack) {
/* 263 */     GamePlayer gamePlayer = getGamePlayer(paramPlayer);
/* 264 */     if (!this.teamManager.getTurnPlayer().equals(gamePlayer))
/*     */       return; 
/* 266 */     int[] arrayOfInt1 = this.mapManager.getClickLocation(paramArrayOfdouble, paramItemStack);
/* 267 */     Button button = getClickedButton(gamePlayer, arrayOfInt1);
/* 268 */     int[] arrayOfInt2 = getButtonLocation(button);
/*     */     
/* 270 */     if (arrayOfInt2 == null) {
/*     */       return;
/*     */     }
/* 273 */     String str1 = this.teamManager.getTurnTeam();
/*     */     
/* 275 */     if (this.selected == null || (this.canDeSelect && this.board[arrayOfInt2[1]][arrayOfInt2[0]].contains(str1))) {
/*     */       
/* 277 */       if (!this.board[arrayOfInt2[1]][arrayOfInt2[0]].contains(str1)) {
/*     */         return;
/*     */       }
/*     */       
/* 281 */       if (colorCanJump(str1)) {
/*     */         
/* 283 */         if (!canJump(arrayOfInt2)) {
/* 284 */           paramPlayer.sendMessage(ConfigUtil.CHAT_GAME_FORCE_JUMP.toString());
/*     */           return;
/*     */         } 
/* 287 */       } else if (!canAdvance(arrayOfInt2)) {
/*     */         return;
/*     */       } 
/* 290 */       this.selected = arrayOfInt2;
/* 291 */       this.canDeSelect = true;
/*     */     } else {
/*     */       
/* 294 */       if (!this.board[this.selected[1]][this.selected[0]].contains(str1)) {
/*     */         return;
/*     */       }
/*     */       
/* 298 */       if (!canMove(this.selected, arrayOfInt2)) {
/*     */         return;
/*     */       }
/* 301 */       playGameSound("click");
/*     */       
/* 303 */       boolean bool = (2 == Math.abs(arrayOfInt2[1] - this.selected[1])) ? true : false;
/*     */ 
/*     */       
/* 306 */       this.board[arrayOfInt2[1]][arrayOfInt2[0]] = this.board[this.selected[1]][this.selected[0]];
/* 307 */       this.board[this.selected[1]][this.selected[0]] = "EMPTY";
/*     */       
/* 309 */       this.movesSinceCapture++;
/*     */ 
/*     */       
/* 312 */       if (bool) {
/* 313 */         this.board[(arrayOfInt2[1] + this.selected[1]) / 2][(arrayOfInt2[0] + this.selected[0]) / 2] = "EMPTY";
/* 314 */         this.movesSinceCapture = 0;
/*     */       } 
/*     */       
/* 317 */       this.selected = null;
/*     */ 
/*     */ 
/*     */       
/* 321 */       promotePieces();
/*     */       
/* 323 */       if (bool && canJump(arrayOfInt2)) {
/* 324 */         this.selected = arrayOfInt2;
/* 325 */         this.canDeSelect = false;
/*     */       } else {
/* 327 */         this.teamManager.nextTurn();
/*     */       } 
/*     */     } 
/*     */     
/* 331 */     String str2 = checkGameOver();
/* 332 */     if (!str2.equals("EMPTY")) {
/* 333 */       GamePlayer gamePlayer1 = this.teamManager.getGamePlayerByTeam(str2);
/* 334 */       endGame(gamePlayer1);
/*     */       
/*     */       return;
/*     */     } 
/* 338 */     updateButtons();
/* 339 */     this.mapManager.renderBoard();
/*     */   }
/*     */ 
/*     */   
/*     */   public void endGame(GamePlayer paramGamePlayer) {
/* 344 */     updateButtons();
/* 345 */     this.mapManager.renderBoard();
/*     */     
/* 347 */     this.movesSinceCapture = 0;
/* 348 */     this.selected = null;
/* 349 */     this.canDeSelect = false;
/*     */     
/* 351 */     super.endGame(paramGamePlayer);
/*     */   }
/*     */ 
/*     */   
/*     */   private String checkGameOver() {
/* 356 */     boolean bool1 = false;
/* 357 */     boolean bool2 = false;
/* 358 */     for (byte b = 0; b < 8; b++) {
/* 359 */       for (byte b1 = 0; b1 < 8; b1++) {
/* 360 */         if (this.board[b1][b].contains("RED"))
/* 361 */           bool1 = true; 
/* 362 */         if (this.board[b1][b].contains("BLACK"))
/* 363 */           bool2 = true; 
/*     */       } 
/*     */     } 
/* 366 */     if (!bool1)
/* 367 */       return "BLACK"; 
/* 368 */     if (!bool2)
/* 369 */       return "RED"; 
/* 370 */     if (this.movesSinceCapture >= 40) {
/* 371 */       return "TIE";
/*     */     }
/* 373 */     return "EMPTY";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void gamePlayerOutOfTime(GamePlayer paramGamePlayer) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack getBoardItem() {
/* 384 */     return (ItemStack)new BoardItem(getAltName(), new ItemStack(Material.OAK_TRAPDOOR, 1));
/*     */   }
/*     */   
/*     */   protected void clockOutOfTime() {}
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\games\checkers\Checkers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */