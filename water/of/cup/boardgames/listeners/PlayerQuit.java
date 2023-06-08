/*    */ package water.of.cup.boardgames.listeners;
/*    */ 
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.EventHandler;
/*    */ import org.bukkit.event.Listener;
/*    */ import org.bukkit.event.player.PlayerQuitEvent;
/*    */ import water.of.cup.boardgames.BoardGames;
/*    */ import water.of.cup.boardgames.game.Game;
/*    */ import water.of.cup.boardgames.game.GameManager;
/*    */ 
/*    */ public class PlayerQuit
/*    */   implements Listener {
/* 13 */   private final BoardGames instance = BoardGames.getInstance();
/* 14 */   private final GameManager gameManager = this.instance.getGameManager();
/*    */   
/*    */   @EventHandler
/*    */   public void onPlayerQuit(PlayerQuitEvent paramPlayerQuitEvent) {
/* 18 */     Player player = paramPlayerQuitEvent.getPlayer();
/* 19 */     Game game = this.gameManager.getGameByPlayer(player);
/* 20 */     if (game != null)
/* 21 */       game.exitPlayer(player); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\listeners\PlayerQuit.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */