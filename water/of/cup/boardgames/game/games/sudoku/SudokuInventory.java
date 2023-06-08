/*    */ package water.of.cup.boardgames.game.games.sudoku;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import water.of.cup.boardgames.config.ConfigUtil;
/*    */ import water.of.cup.boardgames.game.GamePlayer;
/*    */ import water.of.cup.boardgames.game.inventories.GameInventory;
/*    */ import water.of.cup.boardgames.game.inventories.GameOption;
/*    */ 
/*    */ public class SudokuInventory
/*    */   extends GameInventory
/*    */ {
/*    */   private final Sudoku game;
/*    */   
/*    */   public SudokuInventory(Sudoku paramSudoku) {
/* 16 */     super(paramSudoku);
/* 17 */     this.game = paramSudoku;
/*    */   }
/*    */ 
/*    */   
/*    */   protected ArrayList<GameOption> getOptions() {
/* 22 */     return new ArrayList<>();
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getMaxQueue() {
/* 27 */     return 0;
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getMaxGame() {
/* 32 */     return 1;
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getMinGame() {
/* 37 */     return 1;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean hasTeamSelect() {
/* 42 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean hasGameWagers() {
/* 47 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean hasWagerScreen() {
/* 52 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean hasForfeitScreen() {
/* 57 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onGameCreate(HashMap<String, Object> paramHashMap, ArrayList<GamePlayer> paramArrayList) {
/* 62 */     for (GamePlayer gamePlayer : paramArrayList) {
/* 63 */       gamePlayer.getPlayer().sendMessage(ConfigUtil.CHAT_WELCOME_GAME.buildString(this.game.getAltName()));
/*    */     }
/*    */     
/* 66 */     this.game.startGame();
/*    */   }
/*    */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\games\sudoku\SudokuInventory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */