/*    */ package water.of.cup.boardgames.listeners;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import org.bukkit.Material;
/*    */ import org.bukkit.block.Block;
/*    */ import org.bukkit.entity.Entity;
/*    */ import org.bukkit.entity.ItemFrame;
/*    */ import org.bukkit.event.EventHandler;
/*    */ import org.bukkit.event.Listener;
/*    */ import org.bukkit.event.block.BlockBreakEvent;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ import water.of.cup.boardgames.game.games.chess.ChessBoardsUtil;
/*    */ import water.of.cup.boardgames.game.maps.GameMap;
/*    */ 
/*    */ public class BlockBreak
/*    */   implements Listener
/*    */ {
/*    */   @EventHandler
/*    */   public void onBlockBreak(BlockBreakEvent paramBlockBreakEvent) {
/* 20 */     Block block = paramBlockBreakEvent.getBlock();
/* 21 */     if (block.getType().equals(Material.BARRIER)) {
/* 22 */       Collection collection = paramBlockBreakEvent.getPlayer().getWorld().getNearbyEntities(block.getBoundingBox());
/* 23 */       for (Entity entity : collection) {
/* 24 */         if (!(entity instanceof ItemFrame))
/*    */           continue; 
/* 26 */         ItemFrame itemFrame = (ItemFrame)entity;
/* 27 */         ItemStack itemStack = itemFrame.getItem();
/*    */ 
/*    */         
/* 30 */         if (GameMap.isGameMap(itemStack) || ChessBoardsUtil.isChessBoardsMap(itemStack))
/* 31 */           paramBlockBreakEvent.setCancelled(true); 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\listeners\BlockBreak.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */