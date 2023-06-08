/*    */ package water.of.cup.boardgames.game.inventories.wager;
/*    */ 
/*    */ import water.of.cup.boardgames.game.GamePlayer;
/*    */ import water.of.cup.boardgames.game.wagers.RequestWager;
/*    */ 
/*    */ public class WagerOption
/*    */ {
/*    */   private double wagerAmount;
/*    */   private GamePlayer gamePlayer;
/*    */   private boolean opened;
/*    */   private RequestWager selectedWager;
/*    */   
/*    */   public WagerOption(GamePlayer paramGamePlayer) {
/* 14 */     this.gamePlayer = paramGamePlayer;
/* 15 */     this.wagerAmount = 1.0D;
/* 16 */     this.opened = false;
/* 17 */     this.selectedWager = null;
/*    */   }
/*    */   
/*    */   public void setWagerAmount(double paramDouble) {
/* 21 */     this.wagerAmount = paramDouble;
/*    */   }
/*    */   
/*    */   public void setGamePlayer(GamePlayer paramGamePlayer) {
/* 25 */     this.gamePlayer = paramGamePlayer;
/*    */   }
/*    */   
/*    */   public double getWagerAmount() {
/* 29 */     return this.wagerAmount;
/*    */   }
/*    */   
/*    */   public GamePlayer getGamePlayer() {
/* 33 */     return this.gamePlayer;
/*    */   }
/*    */   
/*    */   public boolean isOpened() {
/* 37 */     return this.opened;
/*    */   }
/*    */   
/*    */   public void setOpened(boolean paramBoolean) {
/* 41 */     this.opened = paramBoolean;
/*    */   }
/*    */   
/*    */   public RequestWager getSelectedWager() {
/* 45 */     return this.selectedWager;
/*    */   }
/*    */   
/*    */   public void setSelectedWager(RequestWager paramRequestWager) {
/* 49 */     this.selectedWager = paramRequestWager;
/*    */   }
/*    */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\inventories\wager\WagerOption.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */