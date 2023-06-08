/*    */ package water.of.cup.boardgames.listeners;
/*    */ import org.bukkit.GameMode;
/*    */ import org.bukkit.Location;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.EventHandler;
/*    */ import org.bukkit.event.Listener;
/*    */ import org.bukkit.event.block.BlockPlaceEvent;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ import org.bukkit.plugin.Plugin;
/*    */ import water.of.cup.boardgames.BoardGames;
/*    */ import water.of.cup.boardgames.config.ConfigUtil;
/*    */ import water.of.cup.boardgames.game.BoardItem;
/*    */ import water.of.cup.boardgames.game.Game;
/*    */ import water.of.cup.boardgames.game.GameManager;
/*    */ import water.of.cup.boardgames.game.games.chess.ChessBoardsUtil;
/*    */ 
/*    */ public class BlockPlace implements Listener {
/* 18 */   private BoardGames instance = BoardGames.getInstance();
/* 19 */   private GameManager gameManager = this.instance.getGameManager();
/*    */   @EventHandler
/*    */   public void onBlockPlace(BlockPlaceEvent paramBlockPlaceEvent) {
/*    */     Game game1;
/* 23 */     final Player player = paramBlockPlaceEvent.getPlayer();
/* 24 */     ItemStack itemStack = player.getInventory().getItemInMainHand();
/*    */ 
/*    */     
/* 27 */     double d = player.getEyeLocation().getYaw();
/* 28 */     final int rotation = (int)((d - 45.0D) / 90.0D + 3.0D) % 4;
/*    */ 
/*    */     
/* 31 */     if (!BoardItem.isBoardItem(itemStack) && !ChessBoardsUtil.isChessBoardsItem(itemStack)) {
/*    */       return;
/*    */     }
/*    */     
/* 35 */     if (ChessBoardsUtil.isChessBoardsItem(itemStack)) {
/* 36 */       game1 = this.gameManager.newGame("Chess", i);
/*    */     } else {
/* 38 */       game1 = this.gameManager.newGame(new BoardItem(itemStack), i);
/*    */     } 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 49 */     if (game1 == null) {
/*    */       return;
/*    */     }
/* 52 */     if (ConfigUtil.PERMISSIONS_ENABLED.toBoolean() && 
/* 53 */       !player.hasPermission("boardgames.place")) {
/* 54 */       paramBlockPlaceEvent.setCancelled(true);
/*    */       
/*    */       return;
/*    */     } 
/* 58 */     final Game finalGame = game1;
/* 59 */     paramBlockPlaceEvent.setCancelled(true);
/*    */     
/* 61 */     final Location loc = paramBlockPlaceEvent.getBlock().getLocation();
/*    */     
/* 63 */     Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((Plugin)this.instance, new Runnable()
/*    */         {
/*    */           public void run() {
/* 66 */             if (finalGame.canPlaceBoard(loc, rotation)) {
/* 67 */               if (player.getGameMode() != GameMode.CREATIVE) {
/* 68 */                 player.getInventory().getItemInMainHand()
/* 69 */                   .setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
/*    */               }
/*    */               
/* 72 */               finalGame.placeBoard(loc, rotation);
/* 73 */               BlockPlace.this.gameManager.addGame(finalGame);
/* 74 */               player.sendMessage(ConfigUtil.CHAT_PLACED_BOARD.toString());
/*    */             } else {
/* 76 */               player.sendMessage(ConfigUtil.CHAT_NO_BOARD_ROOM.toString());
/*    */             } 
/*    */           }
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\listeners\BlockPlace.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */