/*    */ package water.of.cup.boardgames.game.inventories.trade;
/*    */ 
/*    */ import org.bukkit.scheduler.BukkitRunnable;
/*    */ 
/*    */ 
/*    */ public class GameTradeTimer
/*    */   extends BukkitRunnable
/*    */ {
/*    */   private static final double TIME_UNTIL_START = 5.0D;
/*    */   private double lastTimeChange;
/*    */   private double timeLeft;
/*    */   private final GameTrade gameTrade;
/*    */   
/*    */   public GameTradeTimer(GameTrade paramGameTrade) {
/* 15 */     this.gameTrade = paramGameTrade;
/* 16 */     this.lastTimeChange = (System.currentTimeMillis() / 1000L);
/* 17 */     this.timeLeft = 5.0D;
/* 18 */     paramGameTrade.updateInventory((int)this.timeLeft);
/*    */   }
/*    */ 
/*    */   
/*    */   public void run() {
/* 23 */     double d1 = (System.currentTimeMillis() / 1000L);
/* 24 */     double d2 = d1 - this.lastTimeChange;
/* 25 */     this.lastTimeChange = d1;
/*    */     
/* 27 */     this.timeLeft -= d2;
/*    */     
/* 29 */     if (this.timeLeft <= 0.0D) {
/* 30 */       this.gameTrade.onAccept();
/* 31 */       cancel();
/*    */       
/*    */       return;
/*    */     } 
/* 35 */     if (d2 >= 1.0D)
/* 36 */       this.gameTrade.updateInventory((int)this.timeLeft); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\inventories\trade\GameTradeTimer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */