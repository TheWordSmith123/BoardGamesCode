/*     */ package water.of.cup.boardgames.game.inventories.join;
/*     */ import de.themoep.inventorygui.GuiElement;
/*     */ import de.themoep.inventorygui.InventoryGui;
/*     */ import de.themoep.inventorygui.StaticGuiElement;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
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
/*     */ import water.of.cup.boardgames.game.inventories.GameOption;
/*     */ import water.of.cup.boardgames.game.inventories.InventoryScreen;
/*     */ import water.of.cup.boardgames.game.inventories.InventoryUtils;
/*     */ 
/*     */ public class GameJoinInventory extends InventoryScreen {
/*     */   private final GameInventory gameInventory;
/*     */   private final ArrayList<GameOption> gameOptions;
/*     */   private final Game game;
/*     */   
/*     */   public GameJoinInventory(GameInventory paramGameInventory) {
/*  27 */     super(paramGameInventory);
/*  28 */     this.gameInventory = paramGameInventory;
/*  29 */     this.gameOptions = paramGameInventory.getGameOptions();
/*  30 */     this.game = paramGameInventory.getGame();
/*     */   }
/*     */   
/*     */   public void build(Player paramPlayer, JoinGameCallback paramJoinGameCallback) {
/*  34 */     String[] arrayOfString = getGuiSetup();
/*     */     
/*  36 */     InventoryGui inventoryGui = new InventoryGui((JavaPlugin)BoardGames.getInstance(), (InventoryHolder)paramPlayer, ConfigUtil.GUI_GAME_JOIN_TITLE.buildString(this.game.getAltName()), arrayOfString, new GuiElement[0]);
/*     */     
/*  38 */     inventoryGui.setFiller(new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
/*     */     
/*  40 */     inventoryGui.addElement((GuiElement)new StaticGuiElement('w', new ItemStack(Material.WHITE_STAINED_GLASS_PANE), new String[] { " " }));
/*     */     
/*  42 */     renderGameOptions(inventoryGui, 's', 'g');
/*     */ 
/*     */     
/*  45 */     if (this.gameInventory.getJoinPlayerQueue().contains(paramPlayer)) {
/*  46 */       renderJoinStatus(inventoryGui, Material.CLOCK, ConfigUtil.GUI_WAIT_CREATOR.toString());
/*  47 */     } else if (this.gameInventory.getAcceptedPlayers().contains(paramPlayer)) {
/*  48 */       renderJoinStatus(inventoryGui, Material.LIME_WOOL, ConfigUtil.GUI_WAIT_PLAYERS.toString());
/*     */     } else {
/*  50 */       inventoryGui.addElement((GuiElement)new StaticGuiElement('z', InventoryUtils.getPlayerHead(paramPlayer), new String[] { ConfigUtil.GUI_CREATE_GAME_DATA_COLOR.toString() + paramPlayer.getDisplayName() }));
/*     */       
/*  52 */       inventoryGui.addElement((GuiElement)new StaticGuiElement('a', new ItemStack(Material.LIME_STAINED_GLASS_PANE), paramClick -> { paramJoinGameCallback.onJoin(paramPlayer); return true; }new String[] { ConfigUtil.GUI_JOIN_GAME
/*     */ 
/*     */ 
/*     */               
/*  56 */               .toString() }));
/*     */ 
/*     */ 
/*     */       
/*  60 */       inventoryGui.addElement((GuiElement)new StaticGuiElement('d', new ItemStack(Material.RED_STAINED_GLASS_PANE), paramClick -> { paramInventoryGui.close(true); paramJoinGameCallback.onLeave(paramPlayer); return true; }new String[] { ConfigUtil.GUI_LEAVE_GAME
/*     */ 
/*     */ 
/*     */ 
/*     */               
/*  65 */               .toString() }));
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  70 */     inventoryGui.setCloseAction(paramClose -> {
/*     */           paramJoinGameCallback.onLeave(paramPlayer);
/*     */           
/*     */           return false;
/*     */         });
/*     */     
/*  76 */     inventoryGui.show((HumanEntity)paramPlayer);
/*     */   }
/*     */   
/*     */   private void renderJoinStatus(InventoryGui paramInventoryGui, Material paramMaterial, String paramString) {
/*  80 */     paramInventoryGui.addElement((GuiElement)new StaticGuiElement('z', new ItemStack(paramMaterial), new String[] { ConfigUtil.GUI_CREATE_GAME_DATA_COLOR.toString() + paramString }));
/*  81 */     paramInventoryGui.addElement((GuiElement)new StaticGuiElement('a', new ItemStack(Material.WHITE_STAINED_GLASS_PANE), new String[] { " " }));
/*  82 */     paramInventoryGui.addElement((GuiElement)new StaticGuiElement('d', new ItemStack(Material.WHITE_STAINED_GLASS_PANE), new String[] { " " }));
/*     */   }
/*     */   
/*     */   private String[] getGuiSetup() {
/*  86 */     char[][] arrayOfChar = new char[6][9];
/*     */ 
/*     */     
/*  89 */     for (char[] arrayOfChar1 : arrayOfChar) {
/*  90 */       Arrays.fill(arrayOfChar1, ' ');
/*     */     }
/*     */     
/*     */     byte b;
/*  94 */     for (b = 2; b <= 4; b++) {
/*  95 */       for (byte b1 = 1; b1 <= 3; b1++) {
/*  96 */         arrayOfChar[b][b1] = 'g';
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 101 */     for (b = 1; b <= 4; b++) {
/* 102 */       for (byte b1 = 5; b1 <= 7; b1++) {
/* 103 */         arrayOfChar[b][b1] = 'w';
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 108 */     arrayOfChar[1][6] = 'z';
/* 109 */     arrayOfChar[3][6] = 'a';
/* 110 */     arrayOfChar[4][6] = 'd';
/*     */ 
/*     */     
/* 113 */     arrayOfChar[1][2] = 's';
/*     */     
/* 115 */     return formatGuiSetup(arrayOfChar);
/*     */   }
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\inventories\join\GameJoinInventory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */