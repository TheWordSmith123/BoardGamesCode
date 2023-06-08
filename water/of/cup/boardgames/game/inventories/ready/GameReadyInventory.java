/*     */ package water.of.cup.boardgames.game.inventories.ready;
/*     */ import de.themoep.inventorygui.GuiElement;
/*     */ import de.themoep.inventorygui.GuiElementGroup;
/*     */ import de.themoep.inventorygui.InventoryGui;
/*     */ import de.themoep.inventorygui.StaticGuiElement;
/*     */ import java.util.Arrays;
/*     */ import java.util.Set;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.entity.HumanEntity;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.InventoryHolder;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import water.of.cup.boardgames.BoardGames;
/*     */ import water.of.cup.boardgames.config.ConfigUtil;
/*     */ import water.of.cup.boardgames.game.Game;
/*     */ import water.of.cup.boardgames.game.inventories.GameInventory;
/*     */ import water.of.cup.boardgames.game.inventories.InventoryScreen;
/*     */ import water.of.cup.boardgames.game.inventories.InventoryUtils;
/*     */ 
/*     */ public class GameReadyInventory extends InventoryScreen {
/*     */   private final GameInventory gameInventory;
/*     */   private final Game game;
/*     */   
/*     */   public GameReadyInventory(GameInventory paramGameInventory) {
/*  26 */     super(paramGameInventory);
/*  27 */     this.gameInventory = paramGameInventory;
/*  28 */     this.game = paramGameInventory.getGame();
/*     */   }
/*     */   
/*     */   public void build(Player paramPlayer, GameReadyCallback paramGameReadyCallback) {
/*  32 */     String[] arrayOfString = getGuiSetup();
/*     */     
/*  34 */     String str1 = ConfigUtil.GUI_GAME_READY_TITLE.buildString(this.game.getAltName()) + " (" + this.gameInventory.getNumReady() + "/" + this.gameInventory.getReadyPlayers().size() + ")";
/*     */     
/*  36 */     InventoryGui inventoryGui = new InventoryGui((JavaPlugin)BoardGames.getInstance(), (InventoryHolder)paramPlayer, str1, arrayOfString, new GuiElement[0]);
/*     */     
/*  38 */     inventoryGui.setFiller(new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
/*     */     
/*  40 */     inventoryGui.addElement((GuiElement)new StaticGuiElement('w', new ItemStack(Material.WHITE_STAINED_GLASS_PANE), new String[] { " " }));
/*     */     
/*  42 */     Set set = this.gameInventory.getReadyPlayers();
/*  43 */     GuiElementGroup guiElementGroup = new GuiElementGroup('g', new GuiElement[0]);
/*  44 */     for (Player player : set) {
/*  45 */       boolean bool1 = this.gameInventory.getReadyStatus(player);
/*  46 */       ItemStack itemStack = InventoryUtils.getPlayerHead(player, bool1);
/*     */ 
/*     */       
/*  49 */       String str = bool1 ? ConfigUtil.GUI_READY_TEXT.toString() : ConfigUtil.GUI_NOT_READY_TEXT.toString();
/*     */       
/*  51 */       guiElementGroup.addElement((GuiElement)new StaticGuiElement('g', itemStack, paramClick -> true, new String[] { ChatColor.GREEN + player
/*     */ 
/*     */               
/*  54 */               .getDisplayName(), str }));
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  60 */     for (int i = set.size(); i < 12; i++) {
/*  61 */       guiElementGroup.addElement((GuiElement)new StaticGuiElement('q', new ItemStack(Material.WHITE_STAINED_GLASS_PANE), new String[] { " " }));
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  67 */     inventoryGui.addElement((GuiElement)guiElementGroup);
/*     */     
/*  69 */     boolean bool = this.gameInventory.getReadyStatus(paramPlayer);
/*  70 */     Material material = bool ? Material.LIME_STAINED_GLASS_PANE : Material.YELLOW_STAINED_GLASS_PANE;
/*     */ 
/*     */     
/*  73 */     String str2 = bool ? ConfigUtil.GUI_READY_TEXT.toString() : ConfigUtil.GUI_UNREADY_TEXT.toString();
/*  74 */     inventoryGui.addElement((GuiElement)new StaticGuiElement('r', new ItemStack(material), paramClick -> { if (!paramBoolean) paramGameReadyCallback.onReady(paramPlayer);  return true; }new String[] { str2 }));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  85 */     inventoryGui.addElement((GuiElement)new StaticGuiElement('l', new ItemStack(Material.RED_STAINED_GLASS_PANE), paramClick -> { paramInventoryGui.close(true); paramGameReadyCallback.onLeave(paramPlayer); return true; }new String[] { ConfigUtil.GUI_LEAVE_GAME
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*  90 */             .toString() }));
/*     */ 
/*     */ 
/*     */     
/*  94 */     inventoryGui.setCloseAction(paramClose -> {
/*     */           paramGameReadyCallback.onLeave(paramPlayer);
/*     */           
/*     */           return false;
/*     */         });
/*     */     
/* 100 */     inventoryGui.show((HumanEntity)paramPlayer);
/*     */   }
/*     */   
/*     */   private String[] getGuiSetup() {
/* 104 */     char[][] arrayOfChar = new char[6][9];
/*     */ 
/*     */     
/* 107 */     for (char[] arrayOfChar1 : arrayOfChar) {
/* 108 */       Arrays.fill(arrayOfChar1, ' ');
/*     */     }
/*     */     
/*     */     byte b;
/* 112 */     for (b = 1; b <= 4; b++) {
/* 113 */       for (byte b1 = 1; b1 <= 3; b1++) {
/* 114 */         arrayOfChar[b][b1] = 'g';
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 119 */     for (b = 1; b <= 4; b++) {
/* 120 */       for (byte b1 = 5; b1 <= 7; b1++) {
/* 121 */         arrayOfChar[b][b1] = 'w';
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 126 */     arrayOfChar[2][6] = 'r';
/* 127 */     arrayOfChar[3][6] = 'l';
/*     */     
/* 129 */     return formatGuiSetup(arrayOfChar);
/*     */   }
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\inventories\ready\GameReadyInventory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */