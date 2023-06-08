/*     */ package water.of.cup.boardgames.game.games.connectfour;
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
/*     */ import water.of.cup.boardgames.game.maps.Screen;
/*     */ import water.of.cup.boardgames.game.storage.BoardGamesStorageType;
/*     */ import water.of.cup.boardgames.game.storage.GameStorage;
/*     */ import water.of.cup.boardgames.game.storage.StorageType;
/*     */ 
/*     */ public class ConnectFour extends Game {
/*     */   Screen redScreen;
/*     */   Screen blueScreen;
/*     */   String[][] chipLocations;
/*     */   Button[][] redBoardButtons;
/*     */   Button[][] blueBoardButtons;
/*     */   
/*     */   public ConnectFour(int paramInt) {
/*  28 */     super(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setMapInformation(int paramInt) {
/*  33 */     this.mapStructure = new int[][] { { 0 }, { -1 } };
/*  34 */     this.placedMapVal = -1;
/*  35 */     this.redScreen = new Screen(this, "CONNECTFOUR_BOARD", 0, new int[] { 0, 0 }, new int[][] { { 1 },  }, paramInt);
/*  36 */     this.screens.add(this.redScreen);
/*  37 */     this.blueScreen = new Screen(this, "CONNECTFOUR_BOARD", 2, new int[] { 0, 1 }, new int[][] { { 2 },  }, paramInt);
/*  38 */     this.screens.add(this.blueScreen);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void startGame() {
/*  43 */     this.buttons.clear();
/*  44 */     resetChips();
/*  45 */     createChipButtons();
/*  46 */     setChipImages();
/*  47 */     setInGame();
/*  48 */     this.mapManager.renderBoard();
/*     */   }
/*     */   
/*     */   private void resetChips() {
/*  52 */     this.chipLocations = new String[6][7];
/*  53 */     for (byte b = 0; b < 6; b++) {
/*  54 */       for (byte b1 = 0; b1 < 7; b1++) {
/*  55 */         this.chipLocations[b][b1] = "EMPTY";
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void createChipButtons() {
/*  61 */     this.redBoardButtons = new Button[6][7];
/*  62 */     this.blueBoardButtons = new Button[6][7];
/*  63 */     for (byte b = 0; b < 6; b++) {
/*  64 */       for (byte b1 = 0; b1 < 7; b1++) {
/*  65 */         Button button1 = new Button(this, "CONNECTFOUR_EMPTY", new int[] { 5 + b1 * 17, 1 + b * 17 }, 0, "EMPTY");
/*     */         
/*  67 */         button1.setClickable(true);
/*  68 */         button1.setScreen(this.redScreen);
/*  69 */         this.buttons.add(button1);
/*  70 */         this.redBoardButtons[b][b1] = button1;
/*     */         
/*  72 */         Button button2 = new Button(this, "CONNECTFOUR_EMPTY", new int[] { 124 - (b1 + 1) * 17, 1 + b * 17 }, 0, "EMPTY");
/*     */         
/*  74 */         button2.setClickable(true);
/*  75 */         button2.setScreen(this.blueScreen);
/*  76 */         this.buttons.add(button2);
/*  77 */         this.blueBoardButtons[b][b1] = button2;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private String checkGameOver() {
/*     */     byte b;
/*  84 */     for (b = 0; b < 6; b++) {
/*  85 */       for (byte b1 = 0; b1 < 4; b1++) {
/*  86 */         String str = this.chipLocations[b][b1];
/*  87 */         byte b2 = 0; while (true) { if (b2 < 4) {
/*  88 */             if (this.chipLocations[b][b1 + b2].equals("EMPTY") || !this.chipLocations[b][b1 + b2].equals(str))
/*     */               break;  b2++; continue;
/*     */           } 
/*  91 */           return str; }
/*     */       
/*     */       } 
/*     */     } 
/*  95 */     for (b = 0; b < 7; b++) {
/*  96 */       for (byte b1 = 0; b1 < 3; b1++) {
/*  97 */         String str = this.chipLocations[b1][b];
/*  98 */         byte b2 = 0; while (true) { if (b2 < 4) {
/*  99 */             if (this.chipLocations[b1 + b2][b].equals("EMPTY") || !this.chipLocations[b1 + b2][b].equals(str))
/*     */               break;  b2++; continue;
/*     */           } 
/* 102 */           return str; }
/*     */       
/*     */       } 
/*     */     } 
/*     */     
/* 107 */     for (b = 0; b < 4; b++) {
/* 108 */       for (byte b1 = 0; b1 < 3; b1++) {
/* 109 */         String str = this.chipLocations[b1][b];
/* 110 */         byte b2 = 0; while (true) { if (b2 < 4) {
/* 111 */             if (this.chipLocations[b1 + b2][b + b2].equals("EMPTY") || !this.chipLocations[b1 + b2][b + b2].equals(str))
/*     */               break;  b2++; continue;
/*     */           } 
/* 114 */           return str + "_CROSS"; }
/*     */       
/*     */       } 
/*     */     } 
/* 118 */     for (b = 0; b < 4; b++) {
/* 119 */       for (byte b1 = 3; b1 < 6; b1++) {
/* 120 */         String str = this.chipLocations[b1][b];
/* 121 */         byte b2 = 0; while (true) { if (b2 < 4) {
/* 122 */             if (this.chipLocations[b1 - b2][b + b2].equals("EMPTY") || !this.chipLocations[b1 - b2][b + b2].equals(str))
/*     */               break;  b2++; continue;
/*     */           } 
/* 125 */           return str + "_CROSS"; }
/*     */       
/*     */       } 
/*     */     } 
/*     */     
/* 130 */     b = 1;
/* 131 */     label90: for (String[] arrayOfString : this.chipLocations) {
/* 132 */       for (String str : arrayOfString) {
/* 133 */         if (str.equals("EMPTY")) {
/* 134 */           b = 0;
/*     */           
/*     */           break label90;
/*     */         } 
/*     */       } 
/*     */     } 
/* 140 */     if (b != 0) return "t";
/*     */     
/* 142 */     return "EMPTY";
/*     */   }
/*     */   
/*     */   private int[] getClickedChipLocation(Button paramButton) {
/* 146 */     for (byte b = 0; b < 6; b++) {
/* 147 */       for (byte b1 = 0; b1 < 7; b1++) {
/* 148 */         if (paramButton == this.redBoardButtons[b][b1])
/* 149 */           return new int[] { b1, b }; 
/* 150 */         if (paramButton == this.blueBoardButtons[b][b1])
/* 151 */           return new int[] { b1, b }; 
/*     */       } 
/*     */     } 
/* 154 */     return null;
/*     */   }
/*     */   
/*     */   private void setChipImages() {
/* 158 */     for (byte b = 0; b < 6; b++) {
/* 159 */       for (byte b1 = 0; b1 < 7; b1++) {
/* 160 */         String str = "CONNECTFOUR_" + this.chipLocations[b][b1];
/* 161 */         this.redBoardButtons[b][b1].setImage(str);
/* 162 */         this.blueBoardButtons[b][b1].setImage(str);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean placeChip(int paramInt, String paramString) {
/* 168 */     int i = paramInt;
/* 169 */     byte b = 0;
/* 170 */     while (b < 5 && this.chipLocations[b + 1][i].equals("EMPTY")) {
/* 171 */       b++;
/*     */     }
/* 173 */     if (!this.chipLocations[b][i].equals("EMPTY")) {
/* 174 */       return false;
/*     */     }
/* 176 */     this.chipLocations[b][i] = paramString;
/* 177 */     setChipImages();
/*     */     
/* 179 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setGameName() {
/* 184 */     this.gameName = "ConnectFour";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setBoardImage() {
/* 190 */     this.gameImage = new GameImage("CONNECTFOUR_EMPTY");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Clock getClock() {
/* 197 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected GameInventory getGameInventory() {
/* 202 */     return new ConnectFourInventory(this);
/*     */   }
/*     */ 
/*     */   
/*     */   protected GameStorage getGameStorage() {
/* 207 */     return new ConnectFourStorage(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public ArrayList<String> getTeamNames() {
/* 212 */     return new ArrayList<String>()
/*     */       {
/*     */       
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   protected GameConfig getGameConfig() {
/* 220 */     return new ConnectFourConfig(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void click(Player paramPlayer, double[] paramArrayOfdouble, ItemStack paramItemStack) {
/* 225 */     GamePlayer gamePlayer = getGamePlayer(paramPlayer);
/* 226 */     if (!this.teamManager.getTurnPlayer().equals(gamePlayer))
/*     */       return; 
/* 228 */     int[] arrayOfInt1 = this.mapManager.getClickLocation(paramArrayOfdouble, paramItemStack);
/* 229 */     Screen screen = this.mapManager.getClickedScreen(paramItemStack);
/* 230 */     if (screen == null) {
/*     */       return;
/*     */     }
/* 233 */     Button button = screen.getClickedButton(gamePlayer, arrayOfInt1);
/* 234 */     int[] arrayOfInt2 = getClickedChipLocation(button);
/*     */     
/* 236 */     if (arrayOfInt2 == null) {
/*     */       return;
/*     */     }
/* 239 */     String str = this.teamManager.getTurnTeam();
/*     */     
/* 241 */     int i = arrayOfInt2[0];
/* 242 */     if (placeChip(i, str)) {
/* 243 */       playGameSound("click");
/*     */       
/* 245 */       this.mapManager.renderBoard();
/*     */       
/* 247 */       this.teamManager.nextTurn();
/*     */       
/* 249 */       String str1 = checkGameOver();
/*     */       
/* 251 */       if (!str1.equals("EMPTY")) {
/*     */         
/* 253 */         boolean bool = false;
/*     */         
/* 255 */         if (str1.indexOf("_CROSS") > -1) {
/* 256 */           str1 = str1.substring(0, str1.indexOf("_CROSS"));
/* 257 */           bool = true;
/*     */         } 
/*     */         
/* 260 */         GamePlayer gamePlayer1 = this.teamManager.getGamePlayerByTeam(str1);
/* 261 */         endGame(gamePlayer1, bool);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void endGame(GamePlayer paramGamePlayer) {
/* 267 */     endGame(paramGamePlayer, false);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void endGame(GamePlayer paramGamePlayer, boolean paramBoolean) {
/* 273 */     updateGameStorage(paramGamePlayer, paramBoolean);
/*     */     
/* 275 */     super.endGame(paramGamePlayer);
/*     */   }
/*     */   
/*     */   private void updateGameStorage(GamePlayer paramGamePlayer, boolean paramBoolean) {
/* 279 */     if (!hasGameStorage())
/*     */       return; 
/* 281 */     if (paramGamePlayer != null && paramBoolean) {
/* 282 */       this.gameStorage.updateData(paramGamePlayer.getPlayer(), (StorageType)BoardGamesStorageType.CROSS_WINS, Integer.valueOf(1));
/*     */     }
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
/* 294 */     return (ItemStack)new BoardItem(getAltName(), new ItemStack(Material.OAK_TRAPDOOR, 1));
/*     */   }
/*     */   
/*     */   protected void clockOutOfTime() {}
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\games\connectfour\ConnectFour.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */