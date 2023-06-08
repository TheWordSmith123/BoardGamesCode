/*    */ package water.of.cup.boardgames.game.wagers;
/*    */ 
/*    */ import org.bukkit.OfflinePlayer;
/*    */ import org.bukkit.entity.Player;
/*    */ import water.of.cup.boardgames.BoardGames;
/*    */ import water.of.cup.boardgames.game.GamePlayer;
/*    */ 
/*    */ public class RequestWager
/*    */ {
/* 10 */   private final BoardGames instance = BoardGames.getInstance();
/*    */   private final Player owner;
/*    */   private final GamePlayer ownerBet;
/*    */   private final double amount;
/*    */   
/*    */   public RequestWager(Player paramPlayer, GamePlayer paramGamePlayer, double paramDouble) {
/* 16 */     this.owner = paramPlayer;
/* 17 */     this.ownerBet = paramGamePlayer;
/* 18 */     this.amount = paramDouble;
/*    */   }
/*    */   
/*    */   public EconomyWager createWager(Player paramPlayer) {
/* 22 */     this.instance.getEconomy().withdrawPlayer((OfflinePlayer)paramPlayer, this.amount);
/*    */     
/* 24 */     return new EconomyWager(this.owner, paramPlayer, this.ownerBet, this.amount);
/*    */   }
/*    */   
/*    */   public Player getOwner() {
/* 28 */     return this.owner;
/*    */   }
/*    */   
/*    */   public GamePlayer getOwnerBet() {
/* 32 */     return this.ownerBet;
/*    */   }
/*    */   
/*    */   public double getAmount() {
/* 36 */     return this.amount;
/*    */   }
/*    */   
/*    */   public void cancel() {
/* 40 */     this.instance.getEconomy().depositPlayer((OfflinePlayer)this.owner, this.amount);
/*    */   }
/*    */   
/*    */   public boolean canCreate() {
/* 44 */     return (this.instance.getEconomy().getBalance((OfflinePlayer)this.owner) >= this.amount);
/*    */   }
/*    */   
/*    */   public boolean canAccept(Player paramPlayer) {
/* 48 */     return (this.instance.getEconomy().getBalance((OfflinePlayer)paramPlayer) >= this.amount);
/*    */   }
/*    */   
/*    */   public void withdrawInitial() {
/* 52 */     this.instance.getEconomy().withdrawPlayer((OfflinePlayer)this.owner, this.amount);
/*    */   }
/*    */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\wagers\RequestWager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */