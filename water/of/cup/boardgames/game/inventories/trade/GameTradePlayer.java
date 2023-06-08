/*    */ package water.of.cup.boardgames.game.inventories.trade;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GameTradePlayer
/*    */ {
/*    */   private final Player player;
/*    */   private final ArrayList<ItemStack> items;
/*    */   private boolean ready;
/*    */   
/*    */   public GameTradePlayer(Player paramPlayer) {
/* 17 */     this.player = paramPlayer;
/* 18 */     this.items = new ArrayList<>();
/* 19 */     this.ready = false;
/*    */   }
/*    */   
/*    */   public Player getPlayer() {
/* 23 */     return this.player;
/*    */   }
/*    */   
/*    */   public ArrayList<ItemStack> getItems() {
/* 27 */     return new ArrayList<>(this.items);
/*    */   }
/*    */   
/*    */   public void updateItems(ArrayList<ItemStack> paramArrayList) {
/* 31 */     this.items.clear();
/* 32 */     this.items.addAll(paramArrayList);
/*    */   }
/*    */   
/*    */   public void setReady(boolean paramBoolean) {
/* 36 */     this.ready = paramBoolean;
/*    */   }
/*    */   
/*    */   public boolean isReady() {
/* 40 */     return this.ready;
/*    */   }
/*    */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\inventories\trade\GameTradePlayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */