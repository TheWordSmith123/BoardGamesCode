/*     */ package water.of.cup.boardgames.game.games.tictactoe;
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
/*     */ public class TicTacToe
/*     */   extends Game
/*     */ {
/*     */   private Button[][] board;
/*     */   
/*     */   public TicTacToe(int paramInt) {
/*  24 */     super(paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void gamePlayerOutOfTime(GamePlayer paramGamePlayer) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setGameName() {
/*  36 */     this.gameName = "TicTacToe";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setBoardImage() {
/*  42 */     this.gameImage = new GameImage("TICTACTOE_BOARD");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setMapInformation(int paramInt) {
/*  48 */     this.mapStructure = new int[][] { { 1 } };
/*  49 */     this.placedMapVal = 1;
/*     */   }
/*     */   
/*     */   private void placeButtons() {
/*  53 */     this.board = new Button[][] { { null, null, null }, { null, null, null }, { null, null, null } };
/*  54 */     for (byte b = 0; b < ''; b += 44) {
/*  55 */       for (byte b1 = 0; b1 < ''; b1 += 44) {
/*  56 */         Button button = new Button(this, "TICTACTOE_EMPTY", new int[] { b, b1 }, 0, "empty");
/*  57 */         button.setClickable(true);
/*  58 */         this.buttons.add(button);
/*  59 */         this.board[b1 / 44][b / 43] = button;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Clock getClock() {
/*  67 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected GameInventory getGameInventory() {
/*  72 */     return new TicTacToeInventory(this);
/*     */   }
/*     */ 
/*     */   
/*     */   protected GameStorage getGameStorage() {
/*  77 */     return new TicTacToeStorage(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public ArrayList<String> getTeamNames() {
/*  82 */     return new ArrayList<String>()
/*     */       {
/*     */       
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   protected GameConfig getGameConfig() {
/*  90 */     return new TicTacToeConfig(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public BoardItem getBoardItem() {
/*  95 */     return new BoardItem(getAltName(), new ItemStack(Material.OAK_TRAPDOOR, 1));
/*     */   }
/*     */ 
/*     */   
/*     */   public void click(Player paramPlayer, double[] paramArrayOfdouble, ItemStack paramItemStack) {
/* 100 */     GamePlayer gamePlayer = getGamePlayer(paramPlayer);
/* 101 */     if (!this.teamManager.getTurnPlayer().equals(gamePlayer))
/*     */       return; 
/* 103 */     int[] arrayOfInt = this.mapManager.getClickLocation(paramArrayOfdouble, paramItemStack);
/* 104 */     Button button = getClickedButton(gamePlayer, arrayOfInt);
/*     */     
/* 106 */     if (button != null) {
/* 107 */       if (!button.getName().equals("empty"))
/*     */         return; 
/* 109 */       playGameSound("click");
/*     */       
/* 111 */       if (this.teamManager.getTurnTeam().equals("x")) {
/* 112 */         button.getImage().setImage("TICTACTOE_X");
/* 113 */         button.setName("x");
/*     */       } else {
/* 115 */         button.getImage().setImage("TICTACTOE_O");
/* 116 */         button.setName("o");
/*     */       } 
/*     */       
/* 119 */       this.teamManager.nextTurn();
/*     */       
/* 121 */       String str = checkForWinner();
/* 122 */       if (!str.equals("n")) {
/* 123 */         GamePlayer gamePlayer1 = this.teamManager.getGamePlayerByTeam(str);
/*     */         
/* 125 */         endGame(gamePlayer1);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 130 */     this.mapManager.renderBoard();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void startGame() {
/* 135 */     super.startGame();
/* 136 */     this.buttons.clear();
/* 137 */     setInGame();
/* 138 */     placeButtons();
/* 139 */     this.mapManager.renderBoard();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void endGame(GamePlayer paramGamePlayer) {
/* 146 */     this.mapManager.renderBoard();
/* 147 */     super.endGame(paramGamePlayer);
/*     */   }
/*     */   
/*     */   private String checkForWinner() {
/* 151 */     String[][] arrayOfString = { { null, null, null }, { null, null, null }, { null, null, null } }; byte b;
/* 152 */     for (b = 0; b < 3; b++) {
/* 153 */       for (byte b1 = 0; b1 < 3; b1++)
/* 154 */         arrayOfString[b][b1] = this.board[b][b1].getName(); 
/* 155 */     }  for (b = 0; b < 3; b++) {
/*     */       
/* 157 */       if (!arrayOfString[b][0].equals("empty") && arrayOfString[b][0].equals(arrayOfString[b][1]) && arrayOfString[b][0]
/* 158 */         .equals(arrayOfString[b][2])) {
/* 159 */         this.buttons.add(new Button(this, "TICTACTOE_HORIZONTAL_" + arrayOfString[b][0].toUpperCase(), new int[] { 0, b * 44 }, 0, "win"));
/* 160 */         return arrayOfString[b][0];
/*     */       } 
/*     */       
/* 163 */       if (!arrayOfString[0][b].equals("empty") && arrayOfString[0][b].equals(arrayOfString[1][b]) && arrayOfString[0][b]
/* 164 */         .equals(arrayOfString[2][b])) {
/* 165 */         this.buttons.add(new Button(this, "TICTACTOE_VERTICAL_" + arrayOfString[0][b].toUpperCase(), new int[] { b * 44, 0 }, 0, "win"));
/* 166 */         return arrayOfString[0][b];
/*     */       } 
/*     */     } 
/*     */     
/* 170 */     if (!arrayOfString[0][0].equals("empty") && arrayOfString[0][0].equals(arrayOfString[1][1]) && arrayOfString[0][0]
/* 171 */       .equals(arrayOfString[2][2])) {
/* 172 */       this.buttons.add(new Button(this, "TICTACTOE_CROSS_" + arrayOfString[0][0].toUpperCase(), new int[] { 0, 0 }, 0, "win"));
/* 173 */       return arrayOfString[0][0];
/*     */     } 
/*     */     
/* 176 */     if (!arrayOfString[0][2].equals("empty") && arrayOfString[0][2].equals(arrayOfString[1][1]) && arrayOfString[0][2]
/* 177 */       .equals(arrayOfString[2][0])) {
/* 178 */       this.buttons.add(new Button(this, "TICTACTOE_CROSS_" + arrayOfString[1][1].toUpperCase(), new int[] { 0, 0 }, 1, "win"));
/* 179 */       return arrayOfString[0][2];
/*     */     } 
/*     */ 
/*     */     
/* 183 */     b = 1;
/* 184 */     label49: for (String[] arrayOfString1 : arrayOfString) {
/* 185 */       for (String str : arrayOfString1) {
/* 186 */         if (str.equals("empty")) {
/* 187 */           b = 0;
/*     */           
/*     */           break label49;
/*     */         } 
/*     */       } 
/*     */     } 
/* 193 */     if (b != 0) return "t";
/*     */     
/* 195 */     return "n";
/*     */   }
/*     */   
/*     */   protected void clockOutOfTime() {}
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\games\tictactoe\TicTacToe.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */