/*     */ package water.of.cup.boardgames.game.inventories.wager;
/*     */ import de.themoep.inventorygui.DynamicGuiElement;
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
/*     */ import water.of.cup.boardgames.game.GamePlayer;
/*     */ import water.of.cup.boardgames.game.inventories.GameInventory;
/*     */ import water.of.cup.boardgames.game.inventories.InventoryUtils;
/*     */ import water.of.cup.boardgames.game.wagers.RequestWager;
/*     */ import water.of.cup.boardgames.game.wagers.WagerManager;
/*     */ 
/*     */ public class GameWagerInventory extends InventoryScreen {
/*     */   private final Game game;
/*     */   private final GameInventory gameInventory;
/*     */   private final WagerManager wagerManager;
/*     */   
/*     */   public GameWagerInventory(GameInventory paramGameInventory) {
/*  31 */     super(paramGameInventory);
/*  32 */     this.gameInventory = paramGameInventory;
/*  33 */     this.game = paramGameInventory.getGame();
/*  34 */     this.wagerManager = paramGameInventory.getWagerManager();
/*     */   }
/*     */   
/*     */   public void build(Player paramPlayer, GameWagerCallback paramGameWagerCallback) {
/*  38 */     WagerOption wagerOption = this.gameInventory.getWagerOption(paramPlayer);
/*     */     
/*  40 */     if (!wagerOption.isOpened()) {
/*  41 */       renderJoinWager(paramPlayer, paramGameWagerCallback);
/*     */       
/*     */       return;
/*     */     } 
/*  45 */     if (wagerOption.getSelectedWager() == null) {
/*  46 */       renderCreateWager(paramPlayer, paramGameWagerCallback);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*  51 */     if (!this.wagerManager.getRequestWagers().contains(wagerOption.getSelectedWager())) {
/*  52 */       wagerOption.setSelectedWager(null);
/*  53 */       renderCreateWager(paramPlayer, paramGameWagerCallback);
/*     */       
/*     */       return;
/*     */     } 
/*  57 */     renderAcceptWager(paramPlayer, paramGameWagerCallback);
/*     */   }
/*     */ 
/*     */   
/*     */   private void renderCreateWager(Player paramPlayer, GameWagerCallback paramGameWagerCallback) {
/*  62 */     String[] arrayOfString = getCreateWagerViewSetup();
/*     */     
/*  64 */     InventoryGui inventoryGui = new InventoryGui((JavaPlugin)BoardGames.getInstance(), (InventoryHolder)paramPlayer, ConfigUtil.GUI_GAME_WAGER_TITLE.buildString(this.game.getAltName()), arrayOfString, new GuiElement[0]);
/*     */     
/*  66 */     inventoryGui.setFiller(new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
/*     */     
/*  68 */     inventoryGui.addElement((GuiElement)new StaticGuiElement('w', new ItemStack(Material.WHITE_STAINED_GLASS_PANE), new String[] { " " }));
/*     */ 
/*     */     
/*  71 */     renderRequestWagers(inventoryGui, paramPlayer, paramGameWagerCallback);
/*     */ 
/*     */     
/*  74 */     inventoryGui.addElement((GuiElement)new StaticGuiElement('a', InventoryUtils.getPlayerHead(paramPlayer), new String[] { paramPlayer.getDisplayName() }));
/*     */     
/*  76 */     boolean bool = this.wagerManager.hasRequestWager(paramPlayer);
/*  77 */     WagerOption wagerOption = this.gameInventory.getWagerOption(paramPlayer);
/*  78 */     ItemStack itemStack1 = InventoryUtils.getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjJmM2EyZGZjZTBjM2RhYjdlZTEwZGIzODVlNTIyOWYxYTM5NTM0YThiYTI2NDYxNzhlMzdjNGZhOTNiIn19fQ==");
/*  79 */     ItemStack itemStack2 = InventoryUtils.getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmIwZjZlOGFmNDZhYzZmYWY4ODkxNDE5MWFiNjZmMjYxZDY3MjZhNzk5OWM2MzdjZjJlNDE1OWZlMWZjNDc3In19fQ==");
/*     */     
/*  81 */     inventoryGui.addElement((GuiElement)new DynamicGuiElement('b', paramHumanEntity -> new StaticGuiElement('b', InventoryUtils.getPlayerHead(paramWagerOption.getGamePlayer().getPlayer()), (), new String[] { ChatColor.GREEN + paramWagerOption.getGamePlayer().getPlayer().getDisplayName() })));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  86 */     renderBetOptionButton(paramPlayer, inventoryGui, 'd', itemStack1, ConfigUtil.GUI_WAGER_NEXT.toString());
/*  87 */     renderBetOptionButton(paramPlayer, inventoryGui, 'c', itemStack2, ConfigUtil.GUI_WAGER_BACK.toString());
/*     */     
/*  89 */     inventoryGui.addElement((GuiElement)new DynamicGuiElement('e', paramHumanEntity -> new StaticGuiElement('e', new ItemStack(Material.GOLD_INGOT), (), new String[] { ChatColor.GREEN + "" + paramWagerOption.getWagerAmount() })));
/*     */ 
/*     */ 
/*     */     
/*  93 */     renderWagerButton(paramPlayer, inventoryGui, 'h', itemStack1, true);
/*  94 */     renderWagerButton(paramPlayer, inventoryGui, 'f', itemStack2, false);
/*     */     
/*  96 */     if (bool) {
/*  97 */       inventoryGui.addElement((GuiElement)new StaticGuiElement('i', new ItemStack(Material.RED_STAINED_GLASS_PANE), paramClick -> { RequestWager requestWager = this.wagerManager.getRequestWager(paramPlayer); paramGameWagerCallback.onCancel(requestWager); return true; }new String[] { ConfigUtil.GUI_WAGER_CANCEL
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 102 */               .toString() }));
/*     */     }
/*     */     else {
/*     */       
/* 106 */       inventoryGui.addElement((GuiElement)new StaticGuiElement('i', new ItemStack(Material.LIME_STAINED_GLASS_PANE), paramClick -> { RequestWager requestWager = new RequestWager(paramPlayer, paramWagerOption.getGamePlayer(), paramWagerOption.getWagerAmount()); if (requestWager.canCreate()) { paramGameWagerCallback.onCreate(requestWager); } else { paramPlayer.sendMessage(ConfigUtil.GUI_WAGER_NO_MONEY_CREATE.toString()); }  return true; }new String[] { ConfigUtil.GUI_WAGER_CREATE
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 116 */               .toString() }));
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 121 */     inventoryGui.setCloseAction(paramClose -> {
/*     */           paramGameWagerCallback.onLeave(paramPlayer);
/*     */           
/*     */           return false;
/*     */         });
/* 126 */     inventoryGui.show((HumanEntity)paramPlayer);
/*     */   }
/*     */   
/*     */   private void renderAcceptWager(Player paramPlayer, GameWagerCallback paramGameWagerCallback) {
/* 130 */     WagerOption wagerOption = this.gameInventory.getWagerOption(paramPlayer);
/* 131 */     RequestWager requestWager = wagerOption.getSelectedWager();
/*     */     
/* 133 */     String[] arrayOfString = getAcceptWagerViewSetup();
/*     */     
/* 135 */     InventoryGui inventoryGui = new InventoryGui((JavaPlugin)BoardGames.getInstance(), (InventoryHolder)paramPlayer, ConfigUtil.GUI_GAME_WAGER_TITLE.buildString(this.game.getAltName()), arrayOfString, new GuiElement[0]);
/*     */     
/* 137 */     inventoryGui.setFiller(new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
/*     */     
/* 139 */     inventoryGui.addElement((GuiElement)new StaticGuiElement('w', new ItemStack(Material.WHITE_STAINED_GLASS_PANE), new String[] { " " }));
/*     */ 
/*     */     
/* 142 */     renderRequestWagers(inventoryGui, paramPlayer, paramGameWagerCallback);
/*     */ 
/*     */     
/* 145 */     inventoryGui.addElement((GuiElement)new StaticGuiElement('a', InventoryUtils.getPlayerHead(requestWager.getOwner()), new String[] { requestWager.getOwner().getDisplayName() }));
/* 146 */     inventoryGui.addElement((GuiElement)new StaticGuiElement('c', InventoryUtils.getPlayerHead(paramPlayer), new String[] { paramPlayer.getDisplayName() }));
/*     */ 
/*     */     
/* 149 */     inventoryGui.addElement((GuiElement)new StaticGuiElement('b', new ItemStack(Material.GOLD_INGOT), new String[] { ChatColor.GREEN + "" + requestWager.getAmount() }));
/*     */ 
/*     */     
/* 152 */     Player player1 = requestWager.getOwnerBet().getPlayer();
/*     */ 
/*     */     
/* 155 */     Player player2 = (((GamePlayer)this.game.getGamePlayers().get(0)).getPlayer() == player1) ? ((GamePlayer)this.game.getGamePlayers().get(1)).getPlayer() : ((GamePlayer)this.game.getGamePlayers().get(0)).getPlayer();
/*     */     
/* 157 */     inventoryGui.addElement((GuiElement)new StaticGuiElement('d', InventoryUtils.getPlayerHead(player1), new String[] { player1.getDisplayName() }));
/* 158 */     inventoryGui.addElement((GuiElement)new StaticGuiElement('e', InventoryUtils.getPlayerHead(player2), new String[] { player2.getDisplayName() }));
/*     */ 
/*     */     
/* 161 */     inventoryGui.addElement((GuiElement)new StaticGuiElement('f', new ItemStack(Material.LIME_STAINED_GLASS_PANE), paramClick -> { if (paramRequestWager.canAccept(paramPlayer)) { paramWagerOption.setSelectedWager(null); paramGameWagerCallback.onAccept(paramPlayer, paramRequestWager); } else { paramPlayer.sendMessage(ConfigUtil.GUI_WAGER_NO_MONEY_ACCEPT.toString()); }  return true; }new String[] { ConfigUtil.GUI_WAGER_ACCEPT
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
/* 172 */             .toString() }));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 177 */     inventoryGui.addElement((GuiElement)new StaticGuiElement('h', new ItemStack(Material.RED_STAINED_GLASS_PANE), paramClick -> { paramWagerOption.setSelectedWager(null); build(paramPlayer, paramGameWagerCallback); return true; }new String[] { ConfigUtil.GUI_WAGER_DECLINE
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 183 */             .toString() }));
/*     */ 
/*     */ 
/*     */     
/* 187 */     inventoryGui.setCloseAction(paramClose -> {
/*     */           paramGameWagerCallback.onLeave(paramPlayer);
/*     */           
/*     */           return false;
/*     */         });
/* 192 */     inventoryGui.show((HumanEntity)paramPlayer);
/*     */   }
/*     */   
/*     */   private void renderBetOptionButton(Player paramPlayer, InventoryGui paramInventoryGui, char paramChar, ItemStack paramItemStack, String paramString) {
/* 196 */     WagerOption wagerOption = this.gameInventory.getWagerOption(paramPlayer);
/* 197 */     boolean bool = this.wagerManager.hasRequestWager(paramPlayer);
/* 198 */     paramInventoryGui.addElement((GuiElement)new StaticGuiElement(paramChar, paramItemStack, paramClick -> { if (paramBoolean) return true;  GamePlayer gamePlayer = (this.game.getGamePlayers().get(0) == paramWagerOption.getGamePlayer()) ? this.game.getGamePlayers().get(1) : this.game.getGamePlayers().get(0); paramWagerOption.setGamePlayer(gamePlayer); paramClick.getGui().draw(); return true; }new String[] { ChatColor.GREEN + paramString }));
/*     */   }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void renderWagerButton(Player paramPlayer, InventoryGui paramInventoryGui, char paramChar, ItemStack paramItemStack, boolean paramBoolean) {
/* 217 */     WagerOption wagerOption = this.gameInventory.getWagerOption(paramPlayer);
/* 218 */     boolean bool = this.wagerManager.hasRequestWager(paramPlayer);
/* 219 */     String str = paramBoolean ? ConfigUtil.GUI_WAGER_INCREASE.toString() : ConfigUtil.GUI_WAGER_DECREASE.toString();
/* 220 */     paramInventoryGui.addElement((GuiElement)new StaticGuiElement(paramChar, paramItemStack, paramClick -> { if (paramBoolean1) return true;  double d = paramWagerOption.getWagerAmount(); if (paramBoolean2) { d++; } else { d--; if (d < 1.0D) d = 1.0D;  }  paramWagerOption.setWagerAmount(d); paramClick.getGui().draw(); return true; }new String[] { str }));
/*     */   }
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
/*     */   
/*     */   private void renderRequestWagers(InventoryGui paramInventoryGui, Player paramPlayer, GameWagerCallback paramGameWagerCallback) {
/* 244 */     WagerOption wagerOption = this.gameInventory.getWagerOption(paramPlayer);
/* 245 */     RequestWager requestWager = wagerOption.getSelectedWager();
/*     */     
/* 247 */     ArrayList arrayList = this.wagerManager.getRequestWagers();
/* 248 */     GuiElementGroup guiElementGroup = new GuiElementGroup('g', new GuiElement[0]);
/*     */     
/* 250 */     for (RequestWager requestWager1 : arrayList) {
/* 251 */       ItemStack itemStack = InventoryUtils.getPlayerHead(requestWager1.getOwner());
/* 252 */       guiElementGroup.addElement((GuiElement)new StaticGuiElement('g', itemStack, paramClick -> { if (this.wagerManager.hasRequestWager(paramPlayer)) return true;  if (paramRequestWager1 != null && paramRequestWager1 == paramRequestWager2) { paramWagerOption.setSelectedWager(null); } else { paramWagerOption.setSelectedWager(paramRequestWager2); }  build(paramPlayer, paramGameWagerCallback); return true; }new String[] { ChatColor.GREEN + requestWager1
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 269 */               .getOwner().getDisplayName(), ConfigUtil.GUI_WAGER_BETTINGON
/* 270 */               .buildString(requestWager1.getOwnerBet().getPlayer().getDisplayName()) }));
/*     */     } 
/*     */ 
/*     */     
/* 274 */     paramInventoryGui.addElement((GuiElement)guiElementGroup);
/*     */   }
/*     */   
/*     */   private void renderJoinWager(Player paramPlayer, GameWagerCallback paramGameWagerCallback) {
/* 278 */     String[] arrayOfString = getJoinGuiSetup();
/*     */     
/* 280 */     InventoryGui inventoryGui = new InventoryGui((JavaPlugin)BoardGames.getInstance(), (InventoryHolder)paramPlayer, ConfigUtil.GUI_GAME_WAGER_TITLE.buildString(this.game.getAltName()), arrayOfString, new GuiElement[0]);
/*     */     
/* 282 */     inventoryGui.setFiller(new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
/*     */     
/* 284 */     inventoryGui.addElement((GuiElement)new StaticGuiElement('w', new ItemStack(Material.WHITE_STAINED_GLASS_PANE), new String[] { " " }));
/*     */     
/* 286 */     renderGameOptions(inventoryGui, 's', 'g');
/*     */     
/* 288 */     inventoryGui.addElement((GuiElement)new StaticGuiElement('a', new ItemStack(Material.BOOK), paramClick -> { this.gameInventory.getWagerOption(paramPlayer).setOpened(true); build(paramPlayer, paramGameWagerCallback); return true; }new String[] { ConfigUtil.GUI_WAGER_TEXT
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 293 */             .toString() }));
/*     */ 
/*     */ 
/*     */     
/* 297 */     inventoryGui.setCloseAction(paramClose -> {
/*     */           paramGameWagerCallback.onLeave(paramPlayer);
/*     */           
/*     */           return false;
/*     */         });
/* 302 */     inventoryGui.show((HumanEntity)paramPlayer);
/*     */   }
/*     */   
/*     */   private String[] getJoinGuiSetup() {
/* 306 */     char[][] arrayOfChar = new char[6][9];
/*     */ 
/*     */     
/* 309 */     for (char[] arrayOfChar1 : arrayOfChar) {
/* 310 */       Arrays.fill(arrayOfChar1, ' ');
/*     */     }
/*     */     
/*     */     byte b;
/* 314 */     for (b = 2; b <= 4; b++) {
/* 315 */       for (byte b1 = 1; b1 <= 3; b1++) {
/* 316 */         arrayOfChar[b][b1] = 'g';
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 321 */     for (b = 1; b <= 4; b++) {
/* 322 */       for (byte b1 = 5; b1 <= 7; b1++) {
/* 323 */         arrayOfChar[b][b1] = 'w';
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 328 */     arrayOfChar[3][6] = 'a';
/*     */ 
/*     */     
/* 331 */     arrayOfChar[1][2] = 's';
/*     */     
/* 333 */     return formatGuiSetup(arrayOfChar);
/*     */   }
/*     */   
/*     */   private String[] getCreateWagerViewSetup() {
/* 337 */     char[][] arrayOfChar = new char[6][9];
/*     */ 
/*     */     
/* 340 */     for (char[] arrayOfChar1 : arrayOfChar) {
/* 341 */       Arrays.fill(arrayOfChar1, ' ');
/*     */     }
/*     */ 
/*     */     
/* 345 */     addWagerArea(arrayOfChar);
/*     */ 
/*     */     
/* 348 */     for (byte b = 1; b <= 4; b++) {
/* 349 */       for (byte b1 = 5; b1 <= 7; b1++) {
/* 350 */         arrayOfChar[b][b1] = 'w';
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 355 */     arrayOfChar[1][6] = 'a';
/*     */ 
/*     */     
/* 358 */     arrayOfChar[2][6] = 'b';
/* 359 */     arrayOfChar[2][5] = 'c';
/* 360 */     arrayOfChar[2][7] = 'd';
/*     */ 
/*     */     
/* 363 */     arrayOfChar[3][6] = 'e';
/* 364 */     arrayOfChar[3][5] = 'f';
/* 365 */     arrayOfChar[3][7] = 'h';
/*     */     
/* 367 */     arrayOfChar[4][6] = 'i';
/*     */     
/* 369 */     return formatGuiSetup(arrayOfChar);
/*     */   }
/*     */   
/*     */   private String[] getAcceptWagerViewSetup() {
/* 373 */     char[][] arrayOfChar = new char[6][9];
/*     */ 
/*     */     
/* 376 */     for (char[] arrayOfChar1 : arrayOfChar) {
/* 377 */       Arrays.fill(arrayOfChar1, ' ');
/*     */     }
/*     */ 
/*     */     
/* 381 */     addWagerArea(arrayOfChar);
/*     */ 
/*     */     
/* 384 */     for (byte b = 1; b <= 4; b++) {
/* 385 */       for (byte b1 = 5; b1 <= 7; b1++) {
/* 386 */         arrayOfChar[b][b1] = 'w';
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 391 */     arrayOfChar[1][5] = 'a';
/* 392 */     arrayOfChar[1][6] = 'b';
/* 393 */     arrayOfChar[1][7] = 'c';
/*     */     
/* 395 */     arrayOfChar[2][5] = 'd';
/* 396 */     arrayOfChar[2][7] = 'e';
/*     */     
/* 398 */     arrayOfChar[3][6] = 'f';
/* 399 */     arrayOfChar[4][6] = 'h';
/*     */     
/* 401 */     return formatGuiSetup(arrayOfChar);
/*     */   }
/*     */ 
/*     */   
/*     */   private void addWagerArea(char[][] paramArrayOfchar) {
/* 406 */     for (byte b = 1; b <= 4; b++) {
/* 407 */       for (byte b1 = 1; b1 <= 3; b1++)
/* 408 */         paramArrayOfchar[b][b1] = 'g'; 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\inventories\wager\GameWagerInventory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */