/*     */ package water.of.cup.boardgames.game.inventories.trade;
/*     */ 
/*     */ import de.themoep.inventorygui.InventoryGui;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.InventoryHolder;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import water.of.cup.boardgames.BoardGames;
/*     */ import water.of.cup.boardgames.game.Game;
/*     */ 
/*     */ 
/*     */ public class GameTrade
/*     */ {
/*     */   private final GameTradePlayer player1;
/*     */   private final GameTradePlayer player2;
/*     */   private final ArrayList<GameTradePlayer> players;
/*     */   private final GameTradeCallback gameTradeCallback;
/*     */   private GameTradeTimer gameTradeTimer;
/*     */   private final Game game;
/*     */   
/*     */   public GameTrade(Player paramPlayer1, Player paramPlayer2, Game paramGame, GameTradeCallback paramGameTradeCallback) {
/*  24 */     this.player1 = new GameTradePlayer(paramPlayer1);
/*  25 */     this.player2 = new GameTradePlayer(paramPlayer2);
/*  26 */     this.players = new ArrayList<>();
/*  27 */     this.players.add(this.player1);
/*  28 */     this.players.add(this.player2);
/*  29 */     this.gameTradeCallback = paramGameTradeCallback;
/*  30 */     this.game = paramGame;
/*     */   }
/*     */   
/*     */   public void sendBackItems() {
/*  34 */     for (GameTradePlayer gameTradePlayer : this.players) {
/*  35 */       sendItems(gameTradePlayer.getPlayer(), gameTradePlayer.getItems());
/*     */     }
/*     */   }
/*     */   
/*     */   public void sendWinnerItems(Player paramPlayer) {
/*  40 */     ArrayList<ItemStack> arrayList = this.player1.getItems();
/*  41 */     arrayList.addAll(this.player2.getItems());
/*  42 */     sendItems(paramPlayer, arrayList);
/*     */   }
/*     */   
/*     */   private void sendItems(Player paramPlayer, ArrayList<ItemStack> paramArrayList) {
/*  46 */     for (ItemStack itemStack : paramArrayList) {
/*  47 */       HashMap hashMap = paramPlayer.getInventory().addItem(new ItemStack[] { itemStack });
/*     */       
/*  49 */       for (ItemStack itemStack1 : hashMap.values()) {
/*  50 */         paramPlayer.getWorld().dropItem(paramPlayer.getLocation(), itemStack1);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void updateInventory(Player paramPlayer, ArrayList<ItemStack> paramArrayList) {
/*  56 */     GameTradePlayer gameTradePlayer = getGameTradePlayer(paramPlayer);
/*  57 */     gameTradePlayer.updateItems(paramArrayList);
/*     */     
/*  59 */     InventoryGui inventoryGui = InventoryGui.get((InventoryHolder)getOtherPlayer(gameTradePlayer).getPlayer());
/*  60 */     if (inventoryGui != null) {
/*  61 */       inventoryGui.draw();
/*     */     }
/*     */   }
/*     */   
/*     */   public void updateInventory(int paramInt) {
/*  66 */     for (GameTradePlayer gameTradePlayer : this.players) {
/*  67 */       (new GameTradeInventory(this, this.gameTradeCallback)).build(gameTradePlayer.getPlayer(), paramInt);
/*     */     }
/*     */   }
/*     */   
/*     */   private int getAmountItems(ArrayList<ItemStack> paramArrayList) {
/*  72 */     int i = 0;
/*  73 */     for (ItemStack itemStack : paramArrayList) {
/*  74 */       if (itemStack != null) i += itemStack.getAmount();
/*     */     
/*     */     } 
/*  77 */     return i;
/*     */   }
/*     */   
/*     */   public GameTradePlayer getGameTradePlayer(Player paramPlayer) {
/*  81 */     return paramPlayer.equals(this.player1.getPlayer()) ? this.player1 : this.player2;
/*     */   }
/*     */   
/*     */   public GameTradePlayer getOtherPlayer(GameTradePlayer paramGameTradePlayer) {
/*  85 */     return paramGameTradePlayer.equals(this.player1) ? this.player2 : this.player1;
/*     */   }
/*     */   
/*     */   public void setReady(Player paramPlayer, boolean paramBoolean) {
/*  89 */     GameTradePlayer gameTradePlayer1 = getGameTradePlayer(paramPlayer);
/*  90 */     gameTradePlayer1.setReady(paramBoolean);
/*     */     
/*  92 */     GameTradePlayer gameTradePlayer2 = getOtherPlayer(gameTradePlayer1);
/*  93 */     InventoryGui inventoryGui = InventoryGui.get((InventoryHolder)gameTradePlayer2.getPlayer());
/*  94 */     if (inventoryGui != null) {
/*  95 */       inventoryGui.draw();
/*     */     }
/*     */     
/*  98 */     if (allReady()) {
/*  99 */       if (this.gameTradeTimer == null) {
/* 100 */         this.gameTradeTimer = new GameTradeTimer(this);
/* 101 */         this.gameTradeTimer.runTaskTimer((Plugin)BoardGames.getInstance(), 5L, 5L);
/*     */       }
/*     */     
/* 104 */     } else if (this.gameTradeTimer != null) {
/* 105 */       this.gameTradeTimer.cancel();
/* 106 */       this.gameTradeTimer = null;
/*     */       
/* 108 */       updateInventory(-1);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onAccept() {
/* 114 */     if (allReady())
/* 115 */       this.gameTradeCallback.onAccept(this); 
/*     */   }
/*     */   
/*     */   public void cancelTimer() {
/* 119 */     if (this.gameTradeTimer != null) {
/* 120 */       this.gameTradeTimer.cancel();
/* 121 */       this.gameTradeTimer = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean allReady() {
/* 126 */     for (GameTradePlayer gameTradePlayer : this.players) {
/* 127 */       if (!gameTradePlayer.isReady()) return false; 
/*     */     } 
/* 129 */     return true;
/*     */   }
/*     */   
/*     */   public Game getGame() {
/* 133 */     return this.game;
/*     */   }
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\inventories\trade\GameTrade.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */