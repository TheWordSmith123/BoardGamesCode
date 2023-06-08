/*    */ package water.of.cup.boardgames.game.games.uno;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import water.of.cup.boardgames.config.ConfigUtil;
/*    */ import water.of.cup.boardgames.game.GamePlayer;
/*    */ import water.of.cup.boardgames.game.inventories.GameInventory;
/*    */ import water.of.cup.boardgames.game.inventories.GameOption;
/*    */ 
/*    */ public class UnoInventory
/*    */   extends GameInventory
/*    */ {
/*    */   private final Uno game;
/*    */   
/*    */   public UnoInventory(Uno paramUno) {
/* 16 */     super(paramUno);
/* 17 */     this.game = paramUno;
/*    */   }
/*    */ 
/*    */   
/*    */   protected ArrayList<GameOption> getOptions() {
/* 22 */     return new ArrayList();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected int getMaxQueue() {
/* 33 */     return 3;
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getMaxGame() {
/* 38 */     return 8;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean hasGameWagers() {
/* 44 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean hasWagerScreen() {
/* 50 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onGameCreate(HashMap<String, Object> paramHashMap, ArrayList<GamePlayer> paramArrayList) {
/* 55 */     for (GamePlayer gamePlayer : paramArrayList) {
/* 56 */       gamePlayer.getPlayer().sendMessage(ConfigUtil.CHAT_WELCOME_GAME.buildString(this.game.getAltName()));
/*    */     }
/*    */     
/* 59 */     this.game.startGame();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected int getMinGame() {
/* 65 */     return 2;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean hasTeamSelect() {
/* 71 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean hasForfeitScreen() {
/* 77 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\game\\uno\UnoInventory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */