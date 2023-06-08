/*    */ package water.of.cup.boardgames.game.wagers;
/*    */ 
/*    */ import water.of.cup.boardgames.game.GamePlayer;
/*    */ import water.of.cup.boardgames.game.inventories.trade.GameTrade;
/*    */ 
/*    */ public class ItemWager
/*    */   implements Wager
/*    */ {
/*    */   private final GameTrade gameTrade;
/*    */   
/*    */   public ItemWager(GameTrade paramGameTrade) {
/* 12 */     this.gameTrade = paramGameTrade;
/*    */   }
/*    */ 
/*    */   
/*    */   public void complete(GamePlayer paramGamePlayer) {
/* 17 */     if (paramGamePlayer == null) {
/* 18 */       cancel();
/*    */       
/*    */       return;
/*    */     } 
/* 22 */     this.gameTrade.sendWinnerItems(paramGamePlayer.getPlayer());
/*    */   }
/*    */ 
/*    */   
/*    */   public void cancel() {
/* 27 */     this.gameTrade.sendBackItems();
/*    */   }
/*    */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\wagers\ItemWager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */