/*    */ package water.of.cup.boardgames.listeners;
/*    */ 
/*    */ import org.bukkit.Location;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.scheduler.BukkitRunnable;
/*    */ import water.of.cup.boardgames.config.ConfigUtil;
/*    */ import water.of.cup.boardgames.game.Game;
/*    */ 
/*    */ public class PlayerOutOfBoundsTimer
/*    */   extends BukkitRunnable
/*    */ {
/*    */   private final long timeStart;
/*    */   private final Player player;
/*    */   private final Location boardLoc;
/*    */   private final Game game;
/*    */   private final PlayerOutOfBoundsCallback callback;
/* 17 */   private final int RETURN_TIME = ConfigUtil.PLAYER_DISTANCE_TIME.toInteger();
/* 18 */   private final int DISTANCE = ConfigUtil.PLAYER_DISTANCE_AMOUNT.toInteger();
/*    */   
/*    */   public PlayerOutOfBoundsTimer(Game paramGame, Player paramPlayer, Location paramLocation, PlayerOutOfBoundsCallback paramPlayerOutOfBoundsCallback) {
/* 21 */     this.timeStart = System.currentTimeMillis();
/* 22 */     this.player = paramPlayer;
/* 23 */     this.boardLoc = paramLocation;
/* 24 */     this.game = paramGame;
/* 25 */     this.callback = paramPlayerOutOfBoundsCallback;
/*    */   }
/*    */ 
/*    */   
/*    */   public void run() {
/* 30 */     if (!this.game.hasPlayer(this.player)) {
/* 31 */       this.callback.onComplete();
/* 32 */       cancel();
/*    */       
/*    */       return;
/*    */     } 
/* 36 */     if (PlayerMove.playerIsInRange(this.boardLoc, this.player.getLocation())) {
/* 37 */       this.callback.onComplete();
/* 38 */       cancel();
/*    */       
/*    */       return;
/*    */     } 
/* 42 */     if (System.currentTimeMillis() - this.timeStart >= this.RETURN_TIME) {
/* 43 */       this.callback.onComplete();
/*    */       
/* 45 */       if (this.game.hasPlayer(this.player))
/* 46 */         this.game.exitPlayer(this.player); 
/* 47 */       cancel();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\listeners\PlayerOutOfBoundsTimer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */