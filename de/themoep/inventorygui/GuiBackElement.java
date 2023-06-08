/*    */ package de.themoep.inventorygui;
/*    */ 
/*    */ import org.bukkit.entity.HumanEntity;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GuiBackElement
/*    */   extends StaticGuiElement
/*    */ {
/*    */   public GuiBackElement(char paramChar, ItemStack paramItemStack, String... paramVarArgs) {
/* 24 */     super(paramChar, paramItemStack, paramVarArgs);
/*    */     
/* 26 */     setAction(paramClick -> {
/*    */           if (canGoBack(paramClick.getEvent().getWhoClicked())) {
/*    */             InventoryGui.goBack(paramClick.getEvent().getWhoClicked());
/*    */           }
/*    */           return true;
/*    */         });
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack getItem(HumanEntity paramHumanEntity, int paramInt) {
/* 36 */     if (!canGoBack(paramHumanEntity)) {
/* 37 */       return (this.gui.getFiller() != null) ? this.gui.getFiller().getItem(paramHumanEntity, paramInt) : null;
/*    */     }
/*    */     
/* 40 */     return super.getItem(paramHumanEntity, paramInt).clone();
/*    */   }
/*    */   
/*    */   private boolean canGoBack(HumanEntity paramHumanEntity) {
/* 44 */     return (InventoryGui.getHistory(paramHumanEntity).size() > 1 || (InventoryGui.getHistory(paramHumanEntity).size() == 1 && InventoryGui.getHistory(paramHumanEntity).peekLast() != this.gui));
/*    */   }
/*    */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\de\themoep\inventorygui\GuiBackElement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */