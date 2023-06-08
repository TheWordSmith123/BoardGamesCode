/*    */ package water.of.cup.boardgames.listeners;
/*    */ 
/*    */ import org.bukkit.Location;
/*    */ import org.bukkit.entity.Entity;
/*    */ import org.bukkit.entity.ItemFrame;
/*    */ import org.bukkit.event.EventHandler;
/*    */ import org.bukkit.event.Listener;
/*    */ import org.bukkit.event.world.ChunkLoadEvent;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ import water.of.cup.boardgames.BoardGames;
/*    */ import water.of.cup.boardgames.game.Game;
/*    */ import water.of.cup.boardgames.game.GameManager;
/*    */ import water.of.cup.boardgames.game.maps.GameMap;
/*    */ 
/*    */ public class ChunkLoad
/*    */   implements Listener {
/* 17 */   private BoardGames instance = BoardGames.getInstance();
/* 18 */   private GameManager gameManager = this.instance.getGameManager();
/*    */ 
/*    */   
/*    */   @EventHandler
/*    */   public void chunkLoad(ChunkLoadEvent paramChunkLoadEvent) {
/* 23 */     for (Entity entity : paramChunkLoadEvent.getChunk().getEntities()) {
/* 24 */       if (!entity.isDead())
/*    */       {
/* 26 */         if (entity instanceof ItemFrame) {
/*    */           
/* 28 */           ItemFrame itemFrame = (ItemFrame)entity;
/* 29 */           ItemStack itemStack = itemFrame.getItem();
/* 30 */           if (GameMap.isGameMap(itemStack)) {
/*    */             
/* 32 */             GameMap gameMap = new GameMap(itemStack);
/*    */             
/* 34 */             if (this.gameManager.getGameByGameId(gameMap.getGameId()) == null) {
/* 35 */               Game game = this.gameManager.newGame(gameMap.getGameName(), gameMap.getRotation());
/* 36 */               if (game != null) {
/*    */ 
/*    */ 
/*    */                 
/* 40 */                 Location location = itemFrame.getLocation().getBlock().getLocation();
/* 41 */                 game.replace(location, game.getRotation(), gameMap.getMapVal());
/* 42 */                 this.gameManager.addGame(game);
/*    */               } 
/*    */             } 
/*    */           } 
/*    */         } 
/*    */       }
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\listeners\ChunkLoad.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */