/*     */ package water.of.cup.boardgames.game.games.conways_game_of_life;
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
/*     */ public class ConwaysGameOfLife
/*     */   extends Game {
/*     */   public ConwaysGameOfLife(int paramInt) {
/*  20 */     super(paramInt);
/*     */   }
/*     */   boolean[][] cells;
/*     */   Button[][] cellButtons;
/*     */   
/*     */   protected void setMapInformation(int paramInt) {
/*  26 */     this.mapStructure = new int[][] { { 1 } };
/*  27 */     this.placedMapVal = 1;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void startGame() {
/*  32 */     super.startGame();
/*  33 */     createCells();
/*  34 */     this.mapManager.renderBoard();
/*     */   }
/*     */   
/*     */   private void createCells() {
/*  38 */     this.cells = new boolean[32][32];
/*  39 */     this.cellButtons = new Button[32][32];
/*  40 */     for (byte b = 0; b < 32; b++) {
/*  41 */       for (byte b1 = 0; b1 < 32; b1++) {
/*  42 */         Button button = new Button(this, "CONWAYS_DEAD", new int[] { b1 * 4, b * 4 }, 0, "cell");
/*  43 */         button.setClickable(true);
/*  44 */         this.buttons.add(button);
/*  45 */         this.cellButtons[b][b1] = button;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   private void renderCells() {
/*  50 */     for (byte b = 0; b < 32; b++) {
/*  51 */       for (byte b1 = 0; b1 < 32; b1++)
/*  52 */         this.cellButtons[b][b1].setImage(this.cells[b][b1] ? "CONWAYS_ALIVE" : "CONWAYS_DEAD"); 
/*     */     } 
/*  54 */     this.mapManager.renderBoard();
/*     */   }
/*     */   
/*     */   private int[] getButtonLocation(Button paramButton) {
/*  58 */     for (byte b = 0; b < 32; b++) {
/*  59 */       for (byte b1 = 0; b1 < 32; b1++) {
/*  60 */         if (paramButton == this.cellButtons[b][b1])
/*  61 */           return new int[] { b1, b }; 
/*     */       } 
/*     */     } 
/*  64 */     return null;
/*     */   }
/*     */   
/*     */   private void toggleCell(Button paramButton) {
/*  68 */     int[] arrayOfInt = getButtonLocation(paramButton);
/*  69 */     if (arrayOfInt == null) {
/*     */       return;
/*     */     }
/*  72 */     this.cells[arrayOfInt[1]][arrayOfInt[0]] = !this.cells[arrayOfInt[1]][arrayOfInt[0]];
/*  73 */     renderCells();
/*     */   }
/*     */   
/*     */   private int getLiveNeighbors(int[] paramArrayOfint) {
/*  77 */     byte b = 0;
/*  78 */     for (byte b1 = -1; b1 <= 1; b1++) {
/*  79 */       for (byte b2 = -1; b2 <= 1; b2++) {
/*  80 */         if (b1 != 0 || b2 != 0) {
/*     */           
/*  82 */           int i = paramArrayOfint[1] + b1;
/*  83 */           int j = paramArrayOfint[0] + b2;
/*     */           
/*  85 */           if (i >= 0 && j >= 0 && i < 32 && j < 32)
/*     */           {
/*     */             
/*  88 */             if (this.cells[i][j])
/*  89 */               b++;  } 
/*     */         } 
/*     */       } 
/*  92 */     }  return b;
/*     */   }
/*     */   
/*     */   private void nextGeneration() {
/*  96 */     boolean[][] arrayOfBoolean = new boolean[32][32];
/*     */     
/*  98 */     for (byte b = 0; b < 32; b++) {
/*  99 */       for (byte b1 = 0; b1 < 32; b1++) {
/* 100 */         int i = getLiveNeighbors(new int[] { b1, b });
/* 101 */         if (this.cells[b][b1]) {
/*     */           
/* 103 */           if (i == 2 || i == 3) {
/* 104 */             arrayOfBoolean[b][b1] = true;
/*     */           }
/*     */         }
/* 107 */         else if (i == 3) {
/* 108 */           arrayOfBoolean[b][b1] = true;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 113 */     this.cells = arrayOfBoolean;
/* 114 */     renderCells();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setGameName() {
/* 120 */     this.gameName = "ConwaysGameOfLife";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setBoardImage() {
/* 126 */     this.gameImage = new GameImage("CONWAYS_BOARD");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Clock getClock() {
/* 133 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void click(Player paramPlayer, double[] paramArrayOfdouble, ItemStack paramItemStack) {
/* 138 */     if (paramPlayer.isSneaking()) {
/* 139 */       nextGeneration();
/*     */       
/*     */       return;
/*     */     } 
/* 143 */     int[] arrayOfInt = this.mapManager.getClickLocation(paramArrayOfdouble, paramItemStack);
/* 144 */     Button button = getClickedButton(getGamePlayer(paramPlayer), arrayOfInt);
/* 145 */     if (button == null) {
/*     */       return;
/*     */     }
/* 148 */     toggleCell(button);
/*     */   }
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
/*     */   public ItemStack getBoardItem() {
/* 161 */     return (ItemStack)new BoardItem(getAltName(), new ItemStack(Material.OAK_TRAPDOOR, 1));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected GameInventory getGameInventory() {
/* 167 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected GameStorage getGameStorage() {
/* 172 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayList<String> getTeamNames() {
/* 178 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected GameConfig getGameConfig() {
/* 183 */     return new ConwaysGameOfLifeConfig(this);
/*     */   }
/*     */   
/*     */   protected void clockOutOfTime() {}
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\games\conways_game_of_life\ConwaysGameOfLife.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */