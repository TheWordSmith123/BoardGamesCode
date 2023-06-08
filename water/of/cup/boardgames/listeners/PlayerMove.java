/*    */ package water.of.cup.boardgames.listeners;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import org.bukkit.Location;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.EventHandler;
/*    */ import org.bukkit.event.Listener;
/*    */ import org.bukkit.event.player.PlayerMoveEvent;
/*    */ import org.bukkit.plugin.Plugin;
/*    */ import water.of.cup.boardgames.BoardGames;
/*    */ import water.of.cup.boardgames.config.ConfigUtil;
/*    */ import water.of.cup.boardgames.game.Game;
/*    */ 
/*    */ 
/*    */ public class PlayerMove
/*    */   implements Listener
/*    */ {
/* 19 */   private final HashMap<Player, Long> moveTimer = new HashMap<>();
/* 20 */   private final ArrayList<Player> returnPlayers = new ArrayList<>();
/* 21 */   public static final int DISTANCE = ConfigUtil.PLAYER_DISTANCE_AMOUNT.toInteger();
/*    */   
/*    */   @EventHandler
/*    */   public void onPlayerMove(PlayerMoveEvent paramPlayerMoveEvent) {
/* 25 */     final Player player = paramPlayerMoveEvent.getPlayer();
/*    */     
/* 27 */     if (!this.moveTimer.containsKey(player)) {
/* 28 */       this.moveTimer.put(player, Long.valueOf(System.currentTimeMillis()));
/*    */       
/* 30 */       Game game = BoardGames.getInstance().getGameManager().getGameByPlayer(player);
/* 31 */       if (game != null && game.getPlacedMapLoc() != null && 
/* 32 */         !playerIsInRange(game.getPlacedMapLoc(), player.getLocation()) && !this.returnPlayers.contains(player)) {
/* 33 */         this.returnPlayers.add(player);
/*    */         
/* 35 */         player.sendMessage(ConfigUtil.CHAT_RETURN_TO_GAME.buildString(game.getAltName()));
/*    */         
/* 37 */         (new PlayerOutOfBoundsTimer(game, player, game.getPlacedMapLoc(), new PlayerOutOfBoundsCallback()
/*    */             {
/*    */               public void onComplete() {
/* 40 */                 PlayerMove.this.returnPlayers.remove(player);
/*    */               }
/* 42 */             })).runTaskTimer((Plugin)BoardGames.getInstance(), 5L, 5L);
/*    */       }
/*    */     
/*    */     }
/* 46 */     else if (System.currentTimeMillis() - ((Long)this.moveTimer.get(player)).longValue() >= 500L) {
/* 47 */       this.moveTimer.remove(player);
/*    */     } 
/*    */   }
/*    */   
/*    */   public static boolean playerIsInRange(Location paramLocation1, Location paramLocation2) {
/* 52 */     if (paramLocation1.getWorld() == null || paramLocation2.getWorld() == null) {
/* 53 */       return false;
/*    */     }
/*    */     
/* 56 */     if (paramLocation1.getWorld() != paramLocation2.getWorld()) {
/* 57 */       return false;
/*    */     }
/*    */     
/* 60 */     if (paramLocation1.distance(paramLocation2) >= ConfigUtil.PLAYER_DISTANCE_AMOUNT.toInteger()) {
/* 61 */       return false;
/*    */     }
/*    */     
/* 64 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\listeners\PlayerMove.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */