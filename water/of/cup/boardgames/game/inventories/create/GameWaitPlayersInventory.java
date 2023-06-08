/*     */ package water.of.cup.boardgames.game.inventories.create;
/*     */ import de.themoep.inventorygui.GuiElement;
/*     */ import de.themoep.inventorygui.GuiElementGroup;
/*     */ import de.themoep.inventorygui.InventoryGui;
/*     */ import de.themoep.inventorygui.StaticGuiElement;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.entity.HumanEntity;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.InventoryHolder;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.plugin.java.JavaPlugin;
/*     */ import water.of.cup.boardgames.BoardGames;
/*     */ import water.of.cup.boardgames.config.ConfigUtil;
/*     */ import water.of.cup.boardgames.game.Game;
/*     */ import water.of.cup.boardgames.game.inventories.GameInventory;
/*     */ import water.of.cup.boardgames.game.inventories.InventoryScreen;
/*     */ import water.of.cup.boardgames.game.inventories.InventoryUtils;
/*     */ 
/*     */ public class GameWaitPlayersInventory extends InventoryScreen {
/*     */   private final GameInventory gameInventory;
/*     */   private final Game game;
/*     */   
/*     */   public GameWaitPlayersInventory(GameInventory paramGameInventory) {
/*  27 */     super(paramGameInventory);
/*  28 */     this.gameInventory = paramGameInventory;
/*  29 */     this.game = paramGameInventory.getGame();
/*     */   }
/*     */   
/*     */   public void build(Player paramPlayer, WaitPlayersCallback paramWaitPlayersCallback) {
/*  33 */     String[] arrayOfString = getGuiSetup();
/*     */     
/*  35 */     String str = this.game.getAltName() + " (" + this.gameInventory.getAcceptedPlayers().size() + "/" + (this.gameInventory.getMaxPlayers() - 1) + ")";
/*     */     
/*  37 */     InventoryGui inventoryGui = new InventoryGui((JavaPlugin)BoardGames.getInstance(), (InventoryHolder)paramPlayer, str, arrayOfString, new GuiElement[0]);
/*     */     
/*  39 */     inventoryGui.setFiller(new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
/*     */     
/*  41 */     renderGameOptions(inventoryGui, 's', 'g');
/*     */ 
/*     */     
/*  44 */     ArrayList arrayList = this.gameInventory.getJoinPlayerQueue();
/*  45 */     GuiElementGroup guiElementGroup = new GuiElementGroup('q', new GuiElement[0]);
/*  46 */     for (Player player : arrayList) {
/*  47 */       guiElementGroup.addElement((GuiElement)new StaticGuiElement('q', 
/*  48 */             InventoryUtils.getPlayerHead(player), paramClick -> { if (paramClick.getEvent().isLeftClick()) { paramWaitPlayersCallback.onAccept(paramPlayer); } else if (paramClick.getEvent().isRightClick()) { paramWaitPlayersCallback.onDecline(paramPlayer); }  return true; }new String[] { ChatColor.GREEN + player
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/*  58 */               .getDisplayName(), ConfigUtil.GUI_ACCEPT_PLAYER
/*  59 */               .toString(), ConfigUtil.GUI_DECLINE_PLAYER
/*  60 */               .toString() }));
/*     */     } 
/*     */     
/*     */     int i;
/*     */     
/*  65 */     for (i = arrayList.size(); i < 12; i++) {
/*  66 */       guiElementGroup.addElement((GuiElement)new StaticGuiElement('q', new ItemStack(Material.WHITE_STAINED_GLASS_PANE), new String[] { " " }));
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  72 */     inventoryGui.addElement((GuiElement)guiElementGroup);
/*     */ 
/*     */     
/*  75 */     i = this.gameInventory.getAcceptedPlayers().size();
/*  76 */     if (i >= this.gameInventory.getMinPlayers() - 1) {
/*  77 */       ItemStack itemStack = InventoryUtils.getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjJmM2EyZGZjZTBjM2RhYjdlZTEwZGIzODVlNTIyOWYxYTM5NTM0YThiYTI2NDYxNzhlMzdjNGZhOTNiIn19fQ==");
/*  78 */       inventoryGui.addElement((GuiElement)new StaticGuiElement('b', itemStack, paramClick -> { paramWaitPlayersCallback.onStart(); return true; }new String[] { ConfigUtil.GUI_START_GAME_WITH
/*     */ 
/*     */ 
/*     */               
/*  82 */               .buildString(i) }));
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  87 */     inventoryGui.setCloseAction(paramClose -> {
/*     */           paramWaitPlayersCallback.onLeave();
/*     */           
/*     */           return false;
/*     */         });
/*     */     
/*  93 */     inventoryGui.show((HumanEntity)paramPlayer);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private String[] getGuiSetup() {
/*  99 */     char[][] arrayOfChar = new char[6][9];
/*     */ 
/*     */     
/* 102 */     for (char[] arrayOfChar1 : arrayOfChar) {
/* 103 */       Arrays.fill(arrayOfChar1, ' ');
/*     */     }
/*     */     
/*     */     byte b;
/* 107 */     for (b = 2; b <= 4; b++) {
/* 108 */       for (byte b1 = 1; b1 <= 3; b1++) {
/* 109 */         arrayOfChar[b][b1] = 'g';
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 114 */     for (b = 1; b <= 4; b++) {
/* 115 */       for (byte b1 = 5; b1 <= 7; b1++) {
/* 116 */         arrayOfChar[b][b1] = 'q';
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 121 */     arrayOfChar[1][2] = 's';
/*     */ 
/*     */     
/* 124 */     arrayOfChar[1][4] = 'b';
/*     */     
/* 126 */     return formatGuiSetup(arrayOfChar);
/*     */   }
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\inventories\create\GameWaitPlayersInventory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */