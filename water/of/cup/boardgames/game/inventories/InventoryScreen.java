/*    */ package water.of.cup.boardgames.game.inventories;
/*    */ 
/*    */ import de.themoep.inventorygui.GuiElement;
/*    */ import de.themoep.inventorygui.GuiElementGroup;
/*    */ import de.themoep.inventorygui.InventoryGui;
/*    */ import de.themoep.inventorygui.StaticGuiElement;
/*    */ import org.bukkit.Material;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ import water.of.cup.boardgames.config.ConfigUtil;
/*    */ 
/*    */ public class InventoryScreen
/*    */ {
/*    */   private final GameInventory gameInventory;
/*    */   
/*    */   public InventoryScreen(GameInventory paramGameInventory) {
/* 16 */     this.gameInventory = paramGameInventory;
/*    */   }
/*    */   
/*    */   public void renderGameOptions(InventoryGui paramInventoryGui, char paramChar1, char paramChar2) {
/* 20 */     ItemStack itemStack = InventoryUtils.getPlayerHead(this.gameInventory.getGameCreator());
/* 21 */     paramInventoryGui.addElement((GuiElement)new StaticGuiElement(paramChar1, itemStack, paramClick -> true, new String[] { ConfigUtil.GUI_CREATE_GAME_DATA_COLOR
/* 22 */             .toString() + this.gameInventory.getGameCreator().getDisplayName() }));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 27 */     GuiElementGroup guiElementGroup = new GuiElementGroup(paramChar2, new GuiElement[0]);
/* 28 */     for (GameOption gameOption : this.gameInventory.getGameOptions()) {
/* 29 */       String str = (gameOption.getLabel() == null) ? "" : gameOption.getLabel();
/*    */       
/* 31 */       guiElementGroup.addElement((GuiElement)new StaticGuiElement(paramChar2, new ItemStack(gameOption
/* 32 */               .getMaterial()), new String[] { str + ConfigUtil.GUI_CREATE_GAME_DATA_COLOR
/* 33 */               .toString() + ConfigUtil.translateTeamName(this.gameInventory.getGameData(gameOption.getKey()).toString()) }));
/*    */     } 
/*    */ 
/*    */ 
/*    */     
/* 38 */     for (int i = this.gameInventory.getGameOptions().size(); i < 9; i++) {
/* 39 */       guiElementGroup.addElement((GuiElement)new StaticGuiElement(paramChar2, new ItemStack(Material.WHITE_STAINED_GLASS_PANE), new String[] { " " }));
/*    */     } 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 45 */     paramInventoryGui.addElement((GuiElement)guiElementGroup);
/*    */   }
/*    */   
/*    */   public String[] formatGuiSetup(char[][] paramArrayOfchar) {
/* 49 */     String[] arrayOfString = new String[paramArrayOfchar.length];
/* 50 */     for (byte b = 0; b < paramArrayOfchar.length; b++) {
/* 51 */       StringBuilder stringBuilder = new StringBuilder();
/* 52 */       for (byte b1 = 0; b1 < (paramArrayOfchar[b]).length; b1++) {
/* 53 */         stringBuilder.append(paramArrayOfchar[b][b1]);
/*    */       }
/* 55 */       arrayOfString[b] = stringBuilder.toString();
/*    */     } 
/* 57 */     return arrayOfString;
/*    */   }
/*    */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\inventories\InventoryScreen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */