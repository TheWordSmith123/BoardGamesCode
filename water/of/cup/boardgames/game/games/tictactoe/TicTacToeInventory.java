/*    */ package water.of.cup.boardgames.game.games.tictactoe;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import water.of.cup.boardgames.config.ConfigUtil;
/*    */ import water.of.cup.boardgames.game.GamePlayer;
/*    */ import water.of.cup.boardgames.game.inventories.GameInventory;
/*    */ import water.of.cup.boardgames.game.inventories.GameOption;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TicTacToeInventory
/*    */   extends GameInventory
/*    */ {
/*    */   private final TicTacToe game;
/*    */   
/*    */   public TicTacToeInventory(TicTacToe paramTicTacToe) {
/* 23 */     super(paramTicTacToe);
/* 24 */     this.game = paramTicTacToe;
/*    */   }
/*    */ 
/*    */   
/*    */   protected ArrayList<GameOption> getOptions() {
/* 29 */     return new ArrayList();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected int getMaxQueue() {
/* 52 */     return 3;
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getMaxGame() {
/* 57 */     return 2;
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getMinGame() {
/* 62 */     return 2;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean hasTeamSelect() {
/* 67 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean hasGameWagers() {
/* 73 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean hasWagerScreen() {
/* 79 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean hasForfeitScreen() {
/* 84 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onGameCreate(HashMap<String, Object> paramHashMap, ArrayList<GamePlayer> paramArrayList) {
/* 89 */     for (GamePlayer gamePlayer : paramArrayList) {
/* 90 */       gamePlayer.getPlayer().sendMessage(ConfigUtil.CHAT_WELCOME_GAME.buildString(this.game.getAltName()));
/*    */     }
/*    */     
/* 93 */     this.game.startGame();
/*    */   }
/*    */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\games\tictactoe\TicTacToeInventory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */