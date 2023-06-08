/*    */ package water.of.cup.boardgames.listeners;
/*    */ 
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.EventHandler;
/*    */ import org.bukkit.event.Listener;
/*    */ import org.bukkit.event.inventory.PrepareItemCraftEvent;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ import water.of.cup.boardgames.config.ConfigUtil;
/*    */ import water.of.cup.boardgames.game.BoardItem;
/*    */ 
/*    */ 
/*    */ public class PlayerItemCraft
/*    */   implements Listener
/*    */ {
/*    */   @EventHandler
/*    */   public void onItemCraft(PrepareItemCraftEvent paramPrepareItemCraftEvent) {
/* 17 */     if (paramPrepareItemCraftEvent.getInventory().getResult() != null) {
/* 18 */       ItemStack itemStack = paramPrepareItemCraftEvent.getInventory().getResult();
/* 19 */       if (paramPrepareItemCraftEvent.getViewers().size() > 0) {
/* 20 */         Player player = paramPrepareItemCraftEvent.getViewers().get(0);
/* 21 */         if (BoardItem.isBoardItem(itemStack)) {
/* 22 */           BoardItem boardItem = new BoardItem(itemStack);
/* 23 */           if (ConfigUtil.PERMISSIONS_ENABLED.toBoolean() && 
/* 24 */             !player.hasPermission("boardgames.recipe." + boardItem.getName()))
/* 25 */             paramPrepareItemCraftEvent.getInventory().setResult(null); 
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\listeners\PlayerItemCraft.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */