/*     */ package water.of.cup.boardgames.game.inventories.trade;
/*     */ 
/*     */ import de.themoep.inventorygui.DynamicGuiElement;
/*     */ import de.themoep.inventorygui.GuiElement;
/*     */ import de.themoep.inventorygui.GuiElementGroup;
/*     */ import de.themoep.inventorygui.GuiStateElement;
/*     */ import de.themoep.inventorygui.InventoryGui;
/*     */ import de.themoep.inventorygui.StaticGuiElement;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.entity.HumanEntity;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.inventory.InventoryType;
/*     */ import org.bukkit.inventory.Inventory;
/*     */ import org.bukkit.inventory.InventoryHolder;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.plugin.java.JavaPlugin;
/*     */ import water.of.cup.boardgames.BoardGames;
/*     */ import water.of.cup.boardgames.config.ConfigUtil;
/*     */ import water.of.cup.boardgames.game.inventories.InventoryUtils;
/*     */ 
/*     */ 
/*     */ public class GameTradeInventory
/*     */ {
/*     */   private final GameTrade gameTrade;
/*     */   private final GameTradeCallback gameTradeCallback;
/*     */   
/*     */   public GameTradeInventory(GameTrade paramGameTrade, GameTradeCallback paramGameTradeCallback) {
/*  31 */     this.gameTrade = paramGameTrade;
/*  32 */     this.gameTradeCallback = paramGameTradeCallback;
/*     */   }
/*     */   
/*     */   public void build(Player paramPlayer) {
/*  36 */     build(paramPlayer, -1);
/*     */   }
/*     */   
/*     */   public void build(Player paramPlayer, int paramInt) {
/*  40 */     String[] arrayOfString = getGuiSetup();
/*     */     
/*  42 */     String str = ConfigUtil.GUI_GAME_TRADE_TITLE.buildString(this.gameTrade.getGame().getAltName());
/*  43 */     if (paramInt != -1) {
/*  44 */       str = str + " (" + paramInt + ")";
/*     */     }
/*  46 */     InventoryGui inventoryGui = new InventoryGui((JavaPlugin)BoardGames.getInstance(), (InventoryHolder)paramPlayer, str, arrayOfString, new GuiElement[0]);
/*     */     
/*  48 */     inventoryGui.setFiller(new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
/*     */     
/*  50 */     GameTradePlayer gameTradePlayer1 = this.gameTrade.getGameTradePlayer(paramPlayer);
/*  51 */     GameTradePlayer gameTradePlayer2 = this.gameTrade.getOtherPlayer(gameTradePlayer1);
/*  52 */     GuiElementGroup guiElementGroup = new GuiElementGroup('j', new GuiElement[0]);
/*     */     
/*  54 */     for (byte b = 0; b < 12; b++) {
/*  55 */       byte b1 = b;
/*  56 */       guiElementGroup.addElement((GuiElement)new DynamicGuiElement('j', paramHumanEntity -> new StaticGuiElement('j', (paramInt >= paramGameTradePlayer.getItems().size()) ? new ItemStack(Material.AIR) : paramGameTradePlayer.getItems().get(paramInt), (paramInt >= paramGameTradePlayer.getItems().size()) ? 0 : ((ItemStack)paramGameTradePlayer.getItems().get(paramInt)).getAmount(), (), new String[0])));
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  66 */     inventoryGui.addElement((GuiElement)guiElementGroup);
/*     */ 
/*     */     
/*  69 */     inventoryGui.addElement((GuiElement)new StaticGuiElement('h', InventoryUtils.getPlayerHead(paramPlayer), new String[] { paramPlayer.getDisplayName() }));
/*  70 */     inventoryGui.addElement((GuiElement)new StaticGuiElement('g', InventoryUtils.getPlayerHead(gameTradePlayer2.getPlayer()), new String[] { gameTradePlayer2.getPlayer().getDisplayName() }));
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
/*  84 */     GuiStateElement guiStateElement = new GuiStateElement('r', new GuiStateElement.State[] { new GuiStateElement.State(paramClick -> this.gameTrade.setReady(paramPlayer, true), "ready", new ItemStack(Material.LIME_STAINED_GLASS_PANE), new String[] { ConfigUtil.GUI_READY_TEXT.toString() }), new GuiStateElement.State(paramClick -> this.gameTrade.setReady(paramPlayer, false), "unready", new ItemStack(Material.RED_STAINED_GLASS_PANE), new String[] { ConfigUtil.GUI_UNREADY_TEXT.toString() }) });
/*     */ 
/*     */ 
/*     */     
/*  88 */     guiStateElement.setState(gameTradePlayer1.isReady() ? "ready" : "unready");
/*  89 */     inventoryGui.addElement((GuiElement)guiStateElement);
/*     */     
/*  91 */     inventoryGui.addElement((GuiElement)new DynamicGuiElement('t', paramHumanEntity -> new StaticGuiElement('t', paramGameTradePlayer.isReady() ? new ItemStack(Material.LIME_STAINED_GLASS_PANE) : new ItemStack(Material.RED_STAINED_GLASS_PANE), (), new String[] { paramGameTradePlayer.isReady() ? ConfigUtil.GUI_READY_TEXT.toString() : ConfigUtil.GUI_NOT_READY_TEXT.toString() })));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  98 */     Inventory inventory = Bukkit.createInventory(null, InventoryType.CHEST);
/*     */     
/* 100 */     if (arrayToList(inventory.getStorageContents()).size() == 0 && gameTradePlayer1.getItems().size() > 0) {
/* 101 */       for (ItemStack itemStack : gameTradePlayer1.getItems()) {
/* 102 */         inventory.addItem(new ItemStack[] { itemStack });
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 107 */     if (paramInt == -1) {
/* 108 */       inventoryGui.addElement(new GuiStorageActionElement('i', inventory, gameTradePlayer1, paramClick -> {
/*     */               List<ItemStack> list = arrayToList(paramInventory.getStorageContents());
/*     */               
/*     */               this.gameTrade.updateInventory(paramPlayer, new ArrayList<>(list));
/*     */               return false;
/*     */             }));
/*     */     } else {
/* 115 */       GuiElementGroup guiElementGroup1 = new GuiElementGroup('i', new GuiElement[0]);
/* 116 */       for (ItemStack itemStack : gameTradePlayer1.getItems()) {
/* 117 */         guiElementGroup1.addElement((GuiElement)new StaticGuiElement('i', itemStack, itemStack.getAmount(), paramClick -> true, new String[0]));
/*     */       }
/*     */       
/* 120 */       inventoryGui.addElement((GuiElement)guiElementGroup1);
/*     */     } 
/*     */ 
/*     */     
/* 124 */     inventoryGui.setCloseAction(paramClose -> {
/*     */           this.gameTradeCallback.onLeave(this.gameTrade);
/*     */           
/*     */           return false;
/*     */         });
/* 129 */     inventoryGui.show((HumanEntity)paramPlayer);
/*     */   }
/*     */   
/*     */   private List<ItemStack> arrayToList(ItemStack[] paramArrayOfItemStack) {
/* 133 */     ArrayList<ItemStack> arrayList = new ArrayList();
/* 134 */     for (ItemStack itemStack : paramArrayOfItemStack) {
/* 135 */       if (itemStack != null && itemStack.getType() != Material.AIR) {
/* 136 */         arrayList.add(itemStack);
/*     */       }
/*     */     } 
/* 139 */     return arrayList;
/*     */   }
/*     */   
/*     */   private String[] getGuiSetup() {
/* 143 */     return new String[] { "  h   g  ", " iii jjj ", " iii jjj ", " iii jjj ", " iii jjj ", "  r   t  " };
/*     */   }
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\inventories\trade\GameTradeInventory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */