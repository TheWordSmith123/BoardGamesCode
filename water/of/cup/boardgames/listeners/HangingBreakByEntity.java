/*    */ package water.of.cup.boardgames.listeners;
/*    */ 
/*    */ import org.bukkit.entity.Hanging;
/*    */ import org.bukkit.entity.ItemFrame;
/*    */ import org.bukkit.event.EventHandler;
/*    */ import org.bukkit.event.Listener;
/*    */ import org.bukkit.event.hanging.HangingBreakEvent;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ import water.of.cup.boardgames.game.maps.GameMap;
/*    */ 
/*    */ public class HangingBreakByEntity
/*    */   implements Listener
/*    */ {
/*    */   @EventHandler
/*    */   public void HangingBreakEvent(HangingBreakEvent paramHangingBreakEvent) {
/* 16 */     if (paramHangingBreakEvent.getCause() == HangingBreakEvent.RemoveCause.ENTITY && 
/* 17 */       paramHangingBreakEvent.getEntity() instanceof ItemFrame) {
/* 18 */       Hanging hanging = paramHangingBreakEvent.getEntity();
/* 19 */       if (!(hanging instanceof ItemFrame))
/*    */         return; 
/* 21 */       ItemFrame itemFrame = (ItemFrame)hanging;
/* 22 */       ItemStack itemStack = itemFrame.getItem();
/* 23 */       if (GameMap.isGameMap(itemStack))
/* 24 */         paramHangingBreakEvent.setCancelled(true); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\listeners\HangingBreakByEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */