/*     */ package water.of.cup.boardgames.game.games.minesweaper;
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
/*     */ public class MineSweeper
/*     */   extends Game
/*     */ {
/*     */   private Button[][] boardButtons;
/*     */   private boolean[][] bombLocations;
/*     */   private boolean[][] discoveredTiles;
/*     */   private boolean[][] flaggedTiles;
/*  24 */   private int openedTiles = 0;
/*  25 */   private int numberOfBombs = 32;
/*     */   
/*     */   public MineSweeper(int paramInt) {
/*  28 */     super(paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setMapInformation(int paramInt) {
/*  34 */     this.mapStructure = new int[][] { { 1 } };
/*  35 */     this.placedMapVal = 1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void startGame() {
/*  41 */     super.startGame();
/*  42 */     this.buttons.clear();
/*  43 */     setInGame();
/*  44 */     createBoard();
/*  45 */     this.mapManager.renderBoard();
/*     */   }
/*     */   
/*     */   private void createBoard() {
/*  49 */     this.bombLocations = new boolean[16][16];
/*  50 */     this.discoveredTiles = new boolean[16][16];
/*  51 */     this.boardButtons = new Button[16][16];
/*  52 */     this.flaggedTiles = new boolean[16][16];
/*     */     
/*     */     byte b;
/*  55 */     for (b = 0; b < 16; b++) {
/*  56 */       for (byte b1 = 0; b1 < 16; b1++) {
/*  57 */         this.boardButtons[b][b1] = new Button(this, "MINESWEEPER_HIDDEN", new int[] { b1 * 8, b * 8 }, 0, "HIDDEN");
/*  58 */         this.boardButtons[b][b1].setClickable(true);
/*  59 */         this.buttons.add(this.boardButtons[b][b1]);
/*     */       } 
/*     */     } 
/*     */     
/*  63 */     for (b = 0; b < this.numberOfBombs; b++) {
/*  64 */       int i = (int)(Math.random() * 16.0D * 16.0D);
/*  65 */       if (this.bombLocations[i % 16][i / 16]) {
/*  66 */         b--;
/*     */       } else {
/*     */         
/*  69 */         this.bombLocations[i % 16][i / 16] = true;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   private boolean tileOnBoard(int[] paramArrayOfint) {
/*  74 */     return (paramArrayOfint[0] >= 0 && paramArrayOfint[1] >= 0 && paramArrayOfint[0] <= 15 && paramArrayOfint[1] <= 15);
/*     */   }
/*     */ 
/*     */   
/*     */   private void toggleFlag(int[] paramArrayOfint) {
/*  79 */     if (this.discoveredTiles[paramArrayOfint[1]][paramArrayOfint[0]]) {
/*     */       return;
/*     */     }
/*  82 */     if (this.flaggedTiles[paramArrayOfint[1]][paramArrayOfint[0]]) {
/*  83 */       this.flaggedTiles[paramArrayOfint[1]][paramArrayOfint[0]] = false;
/*  84 */       this.boardButtons[paramArrayOfint[1]][paramArrayOfint[0]].setImage("MINESWEEPER_HIDDEN");
/*     */     } else {
/*  86 */       this.flaggedTiles[paramArrayOfint[1]][paramArrayOfint[0]] = true;
/*  87 */       this.boardButtons[paramArrayOfint[1]][paramArrayOfint[0]].setImage("MINESWEEPER_FLAG");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean openTile(int[] paramArrayOfint) {
/*  94 */     if (this.discoveredTiles[paramArrayOfint[1]][paramArrayOfint[0]]) {
/*  95 */       return true;
/*     */     }
/*     */     
/*  98 */     if (this.flaggedTiles[paramArrayOfint[1]][paramArrayOfint[0]]) {
/*  99 */       return true;
/*     */     }
/* 101 */     if (this.bombLocations[paramArrayOfint[1]][paramArrayOfint[0]]) {
/* 102 */       this.boardButtons[paramArrayOfint[1]][paramArrayOfint[0]].setImage("MINESWEEPER_MINE");
/* 103 */       return false;
/*     */     } 
/*     */     
/* 106 */     int i = countNearbyBombs(paramArrayOfint);
/* 107 */     this.boardButtons[paramArrayOfint[1]][paramArrayOfint[0]].setImage("MINESWEEPER_" + i);
/* 108 */     this.openedTiles++;
/* 109 */     this.discoveredTiles[paramArrayOfint[1]][paramArrayOfint[0]] = true;
/*     */     
/* 111 */     if (i == 0)
/*     */     {
/* 113 */       for (byte b = -1; b < 2; b++) {
/* 114 */         for (byte b1 = -1; b1 < 2; b1++) {
/* 115 */           if (b1 != 0 || b != 0)
/*     */           {
/* 117 */             if (tileOnBoard(new int[] { paramArrayOfint[0] + b1, paramArrayOfint[1] + b }))
/*     */             {
/* 119 */               if (!this.discoveredTiles[paramArrayOfint[1] + b][paramArrayOfint[0] + b1])
/*     */               {
/* 121 */                 openTile(new int[] { paramArrayOfint[0] + b1, paramArrayOfint[1] + b }); }  }  } 
/*     */         } 
/*     */       } 
/*     */     }
/* 125 */     return true;
/*     */   }
/*     */   
/*     */   private int countNearbyBombs(int[] paramArrayOfint) {
/* 129 */     byte b = 0;
/* 130 */     for (byte b1 = -1; b1 < 2; b1++) {
/* 131 */       for (byte b2 = -1; b2 < 2; b2++) {
/* 132 */         if (b2 != 0 || b1 != 0)
/*     */         {
/* 134 */           if (tileOnBoard(new int[] { paramArrayOfint[0] + b2, paramArrayOfint[1] + b1 }))
/*     */           {
/* 136 */             if (this.bombLocations[paramArrayOfint[1] + b1][paramArrayOfint[0] + b2])
/* 137 */               b++;  }  } 
/*     */       } 
/* 139 */     }  return b;
/*     */   }
/*     */   
/*     */   private int[] getButtonLocation(Button paramButton) {
/* 143 */     for (byte b = 0; b < 16; b++) {
/* 144 */       for (byte b1 = 0; b1 < 16; b1++) {
/* 145 */         if (paramButton == this.boardButtons[b][b1])
/* 146 */           return new int[] { b1, b }; 
/*     */       } 
/*     */     } 
/* 149 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setGameName() {
/* 154 */     this.gameName = "Minesweeper";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setBoardImage() {
/* 160 */     this.gameImage = new GameImage("MINESWEEPER_BOARD");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Clock getClock() {
/* 167 */     Clock clock = new Clock(1, this, false);
/* 168 */     clock.setTimer(true);
/* 169 */     return clock;
/*     */   }
/*     */ 
/*     */   
/*     */   protected GameInventory getGameInventory() {
/* 174 */     return new MineSweeperInventory(this);
/*     */   }
/*     */ 
/*     */   
/*     */   protected GameStorage getGameStorage() {
/* 179 */     return new MineSweeperStorage(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public ArrayList<String> getTeamNames() {
/* 184 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected GameConfig getGameConfig() {
/* 189 */     return new MineSweeperConfig(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void click(Player paramPlayer, double[] paramArrayOfdouble, ItemStack paramItemStack) {
/* 194 */     GamePlayer gamePlayer = getGamePlayer(paramPlayer);
/* 195 */     if (!this.teamManager.getTurnPlayer().equals(gamePlayer))
/*     */       return; 
/* 197 */     int[] arrayOfInt1 = this.mapManager.getClickLocation(paramArrayOfdouble, paramItemStack);
/* 198 */     Button button = getClickedButton(gamePlayer, arrayOfInt1);
/* 199 */     int[] arrayOfInt2 = getButtonLocation(button);
/*     */     
/* 201 */     if (arrayOfInt2 == null) {
/*     */       return;
/*     */     }
/* 204 */     if (paramPlayer.isSneaking()) {
/*     */       
/* 206 */       toggleFlag(arrayOfInt2);
/*     */     
/*     */     }
/* 209 */     else if (openTile(arrayOfInt2)) {
/* 210 */       playGameSound("click");
/*     */ 
/*     */       
/* 213 */       if (this.openedTiles + this.numberOfBombs >= 256)
/*     */       {
/* 215 */         endGame(gamePlayer);
/*     */       }
/*     */     } else {
/*     */       
/* 219 */       endGame((GamePlayer)null);
/*     */     } 
/*     */     
/* 222 */     this.mapManager.renderBoard();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void endGame(GamePlayer paramGamePlayer) {
/* 228 */     this.openedTiles = 0;
/*     */     
/* 230 */     super.endGame(paramGamePlayer);
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
/* 241 */     return (ItemStack)new BoardItem(getAltName(), new ItemStack(Material.OAK_TRAPDOOR, 1));
/*     */   }
/*     */   
/*     */   protected void clockOutOfTime() {}
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\games\minesweaper\MineSweeper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */