/*    */ package water.of.cup.boardgames.game;
/*    */ 
/*    */ import net.md_5.bungee.api.ChatColor;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ import org.bukkit.inventory.meta.ItemMeta;
/*    */ import org.bukkit.persistence.PersistentDataContainer;
/*    */ import org.bukkit.persistence.PersistentDataType;
/*    */ 
/*    */ public class BoardItem
/*    */   extends ItemStack
/*    */ {
/*    */   String name;
/*    */   
/*    */   public BoardItem(String paramString, ItemStack paramItemStack) {
/* 15 */     super(paramItemStack);
/* 16 */     this.name = paramString;
/* 17 */     assert paramItemStack.hasItemMeta();
/* 18 */     ItemMeta itemMeta = paramItemStack.getItemMeta();
/* 19 */     PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
/* 20 */     persistentDataContainer.set(Game.getGameNameKey(), PersistentDataType.STRING, paramString);
/* 21 */     itemMeta.setDisplayName(ChatColor.BLUE + paramString);
/* 22 */     setItemMeta(itemMeta);
/*    */   }
/*    */   
/*    */   public BoardItem(ItemStack paramItemStack) {
/* 26 */     super(paramItemStack);
/* 27 */     assert isBoardItem(paramItemStack);
/* 28 */     ItemMeta itemMeta = paramItemStack.getItemMeta();
/* 29 */     PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
/* 30 */     this.name = (String)persistentDataContainer.get(Game.getGameNameKey(), PersistentDataType.STRING);
/*    */   }
/*    */   
/*    */   public static boolean isBoardItem(ItemStack paramItemStack) {
/* 34 */     if (!paramItemStack.hasItemMeta()) {
/* 35 */       return false;
/*    */     }
/* 37 */     PersistentDataContainer persistentDataContainer = paramItemStack.getItemMeta().getPersistentDataContainer();
/*    */ 
/*    */     
/* 40 */     if (!persistentDataContainer.has(Game.getGameNameKey(), PersistentDataType.STRING))
/* 41 */       return false; 
/* 42 */     return true;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 46 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\BoardItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */