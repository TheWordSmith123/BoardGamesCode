/*    */ package water.of.cup.boardgames.listeners;
/*    */ 
/*    */ import org.bukkit.NamespacedKey;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.EventHandler;
/*    */ import org.bukkit.event.Listener;
/*    */ import org.bukkit.event.player.PlayerJoinEvent;
/*    */ import org.bukkit.plugin.Plugin;
/*    */ import water.of.cup.boardgames.BoardGames;
/*    */ import water.of.cup.boardgames.config.ConfigUtil;
/*    */ import water.of.cup.boardgames.game.Game;
/*    */ import water.of.cup.boardgames.game.GameManager;
/*    */ 
/*    */ public class PlayerJoin implements Listener {
/* 15 */   private final BoardGames instance = BoardGames.getInstance();
/* 16 */   private final GameManager gameManager = this.instance.getGameManager();
/*    */   
/*    */   @EventHandler
/*    */   public void onPlayerJoin(PlayerJoinEvent paramPlayerJoinEvent) {
/* 20 */     Player player = paramPlayerJoinEvent.getPlayer();
/* 21 */     this.gameManager.rerender(player);
/*    */     
/* 23 */     if (ConfigUtil.RECIPE_ENABLED.toBoolean() && ConfigUtil.RECIPE_AUTO_DISCOVER_ENABLED.toBoolean())
/* 24 */       for (String str : this.gameManager.getGameNames()) {
/* 25 */         if (!ConfigUtil.PERMISSIONS_ENABLED.toBoolean() || player.hasPermission("boardgames.recipe." + str)) {
/*    */           
/* 27 */           Game game = this.gameManager.newGame(str, 0);
/*    */           
/* 29 */           if (game != null && game.getGameRecipe() != null)
/* 30 */             player.discoverRecipe(new NamespacedKey((Plugin)this.instance, game.getName())); 
/*    */         } 
/*    */       }  
/*    */   }
/*    */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\listeners\PlayerJoin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */