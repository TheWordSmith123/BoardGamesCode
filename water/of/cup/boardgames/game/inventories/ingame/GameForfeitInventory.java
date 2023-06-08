/*    */ package water.of.cup.boardgames.game.inventories.ingame;
/*    */ 
/*    */ import de.themoep.inventorygui.GuiElement;
/*    */ import de.themoep.inventorygui.InventoryGui;
/*    */ import de.themoep.inventorygui.StaticGuiElement;
/*    */ import java.util.Arrays;
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.Material;
/*    */ import org.bukkit.entity.HumanEntity;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.inventory.InventoryHolder;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ import org.bukkit.plugin.java.JavaPlugin;
/*    */ import water.of.cup.boardgames.BoardGames;
/*    */ import water.of.cup.boardgames.config.ConfigUtil;
/*    */ import water.of.cup.boardgames.game.Game;
/*    */ import water.of.cup.boardgames.game.inventories.GameInventory;
/*    */ import water.of.cup.boardgames.game.inventories.InventoryScreen;
/*    */ import water.of.cup.boardgames.game.inventories.InventoryUtils;
/*    */ 
/*    */ public class GameForfeitInventory
/*    */   extends InventoryScreen {
/*    */   private final Game game;
/*    */   
/*    */   public GameForfeitInventory(GameInventory paramGameInventory) {
/* 26 */     super(paramGameInventory);
/* 27 */     this.game = paramGameInventory.getGame();
/*    */   }
/*    */   
/*    */   public void build(Player paramPlayer, GameForfeitCallback paramGameForfeitCallback) {
/* 31 */     String[] arrayOfString = getGuiSetup();
/*    */     
/* 33 */     InventoryGui inventoryGui = new InventoryGui((JavaPlugin)BoardGames.getInstance(), (InventoryHolder)paramPlayer, ConfigUtil.GUI_GAME_FORFEIT_TITLE.buildString(this.game.getAltName()), arrayOfString, new GuiElement[0]);
/*    */     
/* 35 */     inventoryGui.setFiller(new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
/*    */     
/* 37 */     inventoryGui.addElement((GuiElement)new StaticGuiElement('w', new ItemStack(Material.WHITE_STAINED_GLASS_PANE), new String[] { " " }));
/*    */     
/* 39 */     ItemStack itemStack = InventoryUtils.getPlayerHead(paramPlayer);
/* 40 */     inventoryGui.addElement((GuiElement)new StaticGuiElement('s', itemStack, paramClick -> true, new String[] { ChatColor.GREEN + paramPlayer
/* 41 */             .getDisplayName() }));
/*    */ 
/*    */ 
/*    */     
/* 45 */     inventoryGui.addElement((GuiElement)new StaticGuiElement('a', new ItemStack(Material.RED_STAINED_GLASS_PANE), paramClick -> { paramInventoryGui.close(true); paramGameForfeitCallback.onForfeit(paramPlayer); return true; }new String[] { ConfigUtil.GUI_FORFEIT_GAME
/*    */ 
/*    */ 
/*    */ 
/*    */             
/* 50 */             .toString() }));
/*    */ 
/*    */ 
/*    */     
/* 54 */     inventoryGui.setCloseAction(paramClose -> false);
/*    */     
/* 56 */     inventoryGui.show((HumanEntity)paramPlayer);
/*    */   }
/*    */   
/*    */   private String[] getGuiSetup() {
/* 60 */     char[][] arrayOfChar = new char[6][9];
/*    */ 
/*    */     
/* 63 */     for (char[] arrayOfChar1 : arrayOfChar) {
/* 64 */       Arrays.fill(arrayOfChar1, ' ');
/*    */     }
/*    */ 
/*    */     
/* 68 */     for (byte b = 1; b <= 4; b++) {
/* 69 */       for (byte b1 = 1; b1 <= 7; b1++) {
/* 70 */         arrayOfChar[b][b1] = 'w';
/*    */       }
/*    */     } 
/*    */     
/* 74 */     arrayOfChar[2][4] = 's';
/* 75 */     arrayOfChar[3][4] = 'a';
/*    */     
/* 77 */     return formatGuiSetup(arrayOfChar);
/*    */   }
/*    */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\inventories\ingame\GameForfeitInventory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */