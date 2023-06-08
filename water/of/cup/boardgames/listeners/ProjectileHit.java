/*    */ package water.of.cup.boardgames.listeners;
/*    */ 
/*    */ import org.bukkit.block.Block;
/*    */ import org.bukkit.event.EventHandler;
/*    */ import org.bukkit.event.Listener;
/*    */ import org.bukkit.event.entity.ProjectileHitEvent;
/*    */ import water.of.cup.boardgames.BoardGames;
/*    */ import water.of.cup.boardgames.game.GameManager;
/*    */ 
/*    */ public class ProjectileHit
/*    */   implements Listener {
/* 12 */   private final BoardGames instance = BoardGames.getInstance();
/* 13 */   private final GameManager gameManager = this.instance.getGameManager();
/*    */ 
/*    */   
/*    */   @EventHandler
/*    */   public void onProjectileHit(ProjectileHitEvent paramProjectileHitEvent) {
/* 18 */     if (paramProjectileHitEvent.getHitBlock() != null) {
/* 19 */       Block block = paramProjectileHitEvent.getHitBlock();
/*    */       
/* 21 */       if (this.gameManager.getGamesInRegion(block.getWorld(), block.getBoundingBox().getMin(), block
/* 22 */           .getBoundingBox().getMax()).size() != 0 && 
/* 23 */         !paramProjectileHitEvent.getEntity().isDead())
/* 24 */         paramProjectileHitEvent.getEntity().remove(); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\listeners\ProjectileHit.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */