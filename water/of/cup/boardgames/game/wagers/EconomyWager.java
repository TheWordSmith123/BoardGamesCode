/*    */ package water.of.cup.boardgames.game.wagers;
/*    */ 
/*    */ import org.bukkit.OfflinePlayer;
/*    */ import org.bukkit.entity.Player;
/*    */ import water.of.cup.boardgames.BoardGames;
/*    */ import water.of.cup.boardgames.game.GamePlayer;
/*    */ 
/*    */ 
/*    */ public class EconomyWager
/*    */   implements Wager
/*    */ {
/* 12 */   private final BoardGames instance = BoardGames.getInstance();
/*    */   private final Player player1;
/*    */   private Player player2;
/*    */   private final GamePlayer player1Bet;
/*    */   private final double amount;
/*    */   
/*    */   public EconomyWager(Player paramPlayer1, Player paramPlayer2, GamePlayer paramGamePlayer, double paramDouble) {
/* 19 */     this.player1 = paramPlayer1;
/* 20 */     this.player2 = paramPlayer2;
/* 21 */     this.player1Bet = paramGamePlayer;
/* 22 */     this.amount = paramDouble;
/*    */   }
/*    */ 
/*    */   
/*    */   public void complete(GamePlayer paramGamePlayer) {
/* 27 */     if (paramGamePlayer == null) {
/* 28 */       cancel();
/*    */       
/*    */       return;
/*    */     } 
/* 32 */     if (paramGamePlayer.equals(this.player1Bet)) {
/*    */       
/* 34 */       this.instance.getEconomy().depositPlayer((OfflinePlayer)this.player1, this.amount * 2.0D);
/* 35 */     } else if (this.player2 != null) {
/*    */       
/* 37 */       this.instance.getEconomy().depositPlayer((OfflinePlayer)this.player2, this.amount * 2.0D);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void cancel() {
/* 43 */     this.instance.getEconomy().depositPlayer((OfflinePlayer)this.player1, this.amount);
/*    */ 
/*    */     
/* 46 */     if (this.player2 != null)
/* 47 */       this.instance.getEconomy().depositPlayer((OfflinePlayer)this.player2, this.amount); 
/*    */   }
/*    */   
/*    */   public Player getPlayer1() {
/* 51 */     return this.player1;
/*    */   }
/*    */   
/*    */   public Player getPlayer2() {
/* 55 */     return this.player2;
/*    */   }
/*    */   
/*    */   public double getAmount() {
/* 59 */     return this.amount;
/*    */   }
/*    */   
/*    */   public void setPlayer2(Player paramPlayer) {
/* 63 */     this.player2 = paramPlayer;
/*    */   }
/*    */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\wagers\EconomyWager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */