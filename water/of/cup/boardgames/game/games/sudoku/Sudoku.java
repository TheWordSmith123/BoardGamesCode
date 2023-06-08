/*     */ package water.of.cup.boardgames.game.games.sudoku;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
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
/*     */ public class Sudoku
/*     */   extends Game {
/*     */   private SudokuPuzzle puzzle;
/*     */   private Button[][] boardButtons;
/*     */   
/*     */   public Sudoku(int paramInt) {
/*  24 */     super(paramInt);
/*     */   }
/*     */   private Button[] numberButtons; private int[] selected;
/*     */   
/*     */   protected void setMapInformation(int paramInt) {
/*  29 */     this.mapStructure = new int[][] { { 1 } };
/*  30 */     this.placedMapVal = 1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void startGame() {
/*  36 */     super.startGame();
/*  37 */     this.puzzle = new SudokuPuzzle();
/*  38 */     this.selected = null;
/*  39 */     setInGame();
/*  40 */     createBoard();
/*  41 */     updateBoard();
/*     */   }
/*     */ 
/*     */   
/*     */   private void createBoard() {
/*  46 */     this.buttons.clear();
/*  47 */     this.boardButtons = new Button[9][9]; byte b;
/*  48 */     for (b = 0; b < 9; b++) {
/*  49 */       for (byte b1 = 0; b1 < 9; b1++) {
/*  50 */         this.boardButtons[b][b1] = new Button(this, "SUDOKU_0", new int[] { 9 + b1 * 12 + b1 / 3 * 2, 3 + b * 12 + b / 3 * 2 }, 0, "TILE");
/*     */         
/*  52 */         this.boardButtons[b][b1].setClickable(true);
/*  53 */         this.buttons.add(this.boardButtons[b][b1]);
/*     */       } 
/*     */     } 
/*  56 */     this.numberButtons = new Button[9];
/*  57 */     for (b = 0; b < 9; b++) {
/*  58 */       this.numberButtons[b] = new Button(this, "SUDOKU_" + (b + 1), new int[] { 3 + b * 14, 117 }, 0, "" + (b + 1));
/*  59 */       this.numberButtons[b].setClickable(true);
/*  60 */       this.buttons.add(this.numberButtons[b]);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateBoard() {
/*  66 */     int[][] arrayOfInt = this.puzzle.getKnownStructure();
/*  67 */     int i = -1;
/*  68 */     if (this.selected != null)
/*  69 */       i = arrayOfInt[this.selected[1]][this.selected[0]]; 
/*  70 */     if (i == 0) {
/*  71 */       i = -1;
/*     */     }
/*  73 */     for (byte b = 0; b < 9; b++) {
/*  74 */       for (byte b1 = 0; b1 < 9; b1++) {
/*  75 */         if (arrayOfInt[b][b1] == i || (this.selected != null && b1 == this.selected[0] && b == this.selected[1])) {
/*  76 */           this.boardButtons[b][b1].setImage("SUDOKU_HIGHLIGHTED");
/*     */         } else {
/*  78 */           this.boardButtons[b][b1].setImage("SUDOKU_0");
/*     */         } 
/*  80 */         this.boardButtons[b][b1].getImage().addGameImage(new GameImage("SUDOKU_" + arrayOfInt[b][b1]), new int[] { 0, 0 });
/*     */       } 
/*     */     } 
/*  83 */     this.mapManager.renderBoard();
/*     */   }
/*     */   
/*     */   private void removeFinishedNumberButtons() {
/*  87 */     for (Iterator<Integer> iterator = this.puzzle.getFinishedNumbers().iterator(); iterator.hasNext(); ) { int i = ((Integer)iterator.next()).intValue();
/*  88 */       if (this.numberButtons[i - 1] != null) {
/*  89 */         this.buttons.remove(this.numberButtons[i - 1]);
/*  90 */         this.numberButtons[i - 1] = null;
/*  91 */         updateBoard();
/*     */       }  }
/*     */   
/*     */   }
/*     */   
/*     */   private void checkGameOver() {
/*  97 */     if (this.puzzle.checkGameWon()) {
/*  98 */       this.mapManager.renderBoard();
/*  99 */       endGame(this.teamManager.getTurnPlayer());
/*     */     } 
/*     */   }
/*     */   
/*     */   private int[] getButtonLocation(Button paramButton) {
/* 104 */     for (byte b = 0; b < 9; b++) {
/* 105 */       for (byte b1 = 0; b1 < 9; b1++) {
/* 106 */         if (paramButton == this.boardButtons[b][b1])
/* 107 */           return new int[] { b1, b }; 
/*     */       } 
/*     */     } 
/* 110 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setGameName() {
/* 115 */     this.gameName = "Sudoku";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setBoardImage() {
/* 121 */     this.gameImage = new GameImage("SUDOKU_BOARD");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Clock getClock() {
/* 128 */     Clock clock = new Clock(1, this, false);
/* 129 */     clock.setTimer(true);
/* 130 */     return clock;
/*     */   }
/*     */ 
/*     */   
/*     */   protected GameInventory getGameInventory() {
/* 135 */     return new SudokuInventory(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public ArrayList<String> getTeamNames() {
/* 140 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void click(Player paramPlayer, double[] paramArrayOfdouble, ItemStack paramItemStack) {
/* 145 */     GamePlayer gamePlayer = getGamePlayer(paramPlayer);
/* 146 */     if (!this.teamManager.getTurnPlayer().equals(gamePlayer)) {
/*     */       return;
/*     */     }
/* 149 */     int[] arrayOfInt = this.mapManager.getClickLocation(paramArrayOfdouble, paramItemStack);
/* 150 */     Button button = getClickedButton(gamePlayer, arrayOfInt);
/*     */     
/* 152 */     if (button == null) {
/*     */       return;
/*     */     }
/* 155 */     if (button.getName().equals("TILE")) {
/* 156 */       int[] arrayOfInt1 = getButtonLocation(button);
/* 157 */       this.selected = arrayOfInt1;
/* 158 */       updateBoard();
/*     */       
/* 160 */       playGameSound("click");
/*     */     } else {
/* 162 */       if (this.selected == null)
/*     */         return; 
/* 164 */       for (Button button1 : this.numberButtons) {
/* 165 */         if (button == button1) {
/* 166 */           int i = Integer.parseInt(button1.getName());
/* 167 */           if (!this.puzzle.check(this.selected, i)) {
/* 168 */             this.mapManager.renderBoard();
/* 169 */             endGame((GamePlayer)null); break;
/*     */           } 
/* 171 */           removeFinishedNumberButtons();
/* 172 */           checkGameOver();
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*     */       
/* 178 */       updateBoard();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void endGame(GamePlayer paramGamePlayer) {
/* 184 */     this.clock.cancel();
/*     */     
/* 186 */     super.endGame(paramGamePlayer);
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
/*     */   public ItemStack getBoardItem() {
/* 198 */     return (ItemStack)new BoardItem(getAltName(), new ItemStack(Material.BIRCH_TRAPDOOR, 1));
/*     */   }
/*     */ 
/*     */   
/*     */   protected GameConfig getGameConfig() {
/* 203 */     return new SudokuConfig(this);
/*     */   }
/*     */ 
/*     */   
/*     */   protected GameStorage getGameStorage() {
/* 208 */     return new SudokuStorage(this);
/*     */   }
/*     */   
/*     */   protected void clockOutOfTime() {}
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\games\sudoku\Sudoku.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */