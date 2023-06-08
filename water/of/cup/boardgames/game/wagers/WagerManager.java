/*     */ package water.of.cup.boardgames.game.wagers;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import org.bukkit.OfflinePlayer;
/*     */ import org.bukkit.entity.Player;
/*     */ import water.of.cup.boardgames.BoardGames;
/*     */ import water.of.cup.boardgames.game.GamePlayer;
/*     */ 
/*     */ 
/*     */ public class WagerManager
/*     */ {
/*  12 */   private final BoardGames instance = BoardGames.getInstance();
/*     */   private final ArrayList<EconomyWager> economyWagers;
/*     */   private final ArrayList<RequestWager> requestWagers;
/*     */   private final ArrayList<Wager> wagers;
/*     */   
/*     */   public WagerManager() {
/*  18 */     this.economyWagers = new ArrayList<>();
/*  19 */     this.requestWagers = new ArrayList<>();
/*  20 */     this.wagers = new ArrayList<>();
/*     */   }
/*     */   
/*     */   public void addRequestWager(RequestWager paramRequestWager) {
/*  24 */     paramRequestWager.withdrawInitial();
/*  25 */     this.requestWagers.add(paramRequestWager);
/*     */   }
/*     */   
/*     */   public void initGameWager(GamePlayer paramGamePlayer, double paramDouble) {
/*  29 */     EconomyWager economyWager = new EconomyWager(paramGamePlayer.getPlayer(), null, paramGamePlayer, paramDouble);
/*  30 */     this.instance.getEconomy().withdrawPlayer((OfflinePlayer)paramGamePlayer.getPlayer(), paramDouble);
/*  31 */     this.economyWagers.add(economyWager);
/*  32 */     this.wagers.add(economyWager);
/*     */   }
/*     */   
/*     */   public void addGameWagerPlayer(Player paramPlayer1, Player paramPlayer2) {
/*  36 */     EconomyWager economyWager = getWager(paramPlayer2);
/*  37 */     this.instance.getEconomy().withdrawPlayer((OfflinePlayer)paramPlayer1, economyWager.getAmount());
/*  38 */     economyWager.setPlayer2(paramPlayer1);
/*     */   }
/*     */   
/*     */   public void addWager(Wager paramWager) {
/*  42 */     this.wagers.add(paramWager);
/*     */   }
/*     */   
/*     */   public void endAllWagers() {
/*  46 */     for (Wager wager : this.wagers) {
/*  47 */       wager.cancel();
/*     */     }
/*     */     
/*  50 */     this.wagers.clear();
/*  51 */     this.economyWagers.clear();
/*     */   }
/*     */   
/*     */   public void endAllRequestWagers() {
/*  55 */     for (RequestWager requestWager : this.requestWagers) {
/*  56 */       requestWager.cancel();
/*     */     }
/*  58 */     this.requestWagers.clear();
/*     */   }
/*     */   
/*     */   public void endAll() {
/*  62 */     endAllWagers();
/*  63 */     endAllRequestWagers();
/*     */   }
/*     */   
/*     */   public void completeWagers(GamePlayer paramGamePlayer) {
/*  67 */     if (paramGamePlayer == null) {
/*  68 */       endAll();
/*     */       
/*     */       return;
/*     */     } 
/*  72 */     endAllRequestWagers();
/*     */     
/*  74 */     for (Wager wager : this.wagers) {
/*  75 */       wager.complete(paramGamePlayer);
/*     */     }
/*     */     
/*  78 */     this.wagers.clear();
/*  79 */     this.economyWagers.clear();
/*     */   }
/*     */   
/*     */   public void acceptRequestWager(Player paramPlayer, RequestWager paramRequestWager) {
/*  83 */     EconomyWager economyWager = paramRequestWager.createWager(paramPlayer);
/*     */     
/*  85 */     this.wagers.add(economyWager);
/*  86 */     this.economyWagers.add(economyWager);
/*  87 */     this.requestWagers.remove(paramRequestWager);
/*     */   }
/*     */   
/*     */   public EconomyWager getWager(Player paramPlayer) {
/*  91 */     for (EconomyWager economyWager : this.economyWagers) {
/*  92 */       if (economyWager.getPlayer1() != null && economyWager.getPlayer1().equals(paramPlayer))
/*  93 */         return economyWager; 
/*  94 */       if (economyWager.getPlayer2() != null && economyWager.getPlayer2().equals(paramPlayer)) {
/*  95 */         return economyWager;
/*     */       }
/*     */     } 
/*  98 */     return null;
/*     */   }
/*     */   
/*     */   public ArrayList<RequestWager> getRequestWagers() {
/* 102 */     return this.requestWagers;
/*     */   }
/*     */   
/*     */   public boolean hasRequestWager(Player paramPlayer) {
/* 106 */     for (RequestWager requestWager : this.requestWagers) {
/* 107 */       if (requestWager.getOwner().equals(paramPlayer)) return true;
/*     */     
/*     */     } 
/* 110 */     return false;
/*     */   }
/*     */   
/*     */   public RequestWager getRequestWager(Player paramPlayer) {
/* 114 */     for (RequestWager requestWager : this.requestWagers) {
/* 115 */       if (requestWager.getOwner().equals(paramPlayer)) return requestWager;
/*     */     
/*     */     } 
/* 118 */     return null;
/*     */   }
/*     */   
/*     */   public void cancelRequestWager(RequestWager paramRequestWager) {
/* 122 */     paramRequestWager.cancel();
/* 123 */     this.requestWagers.remove(paramRequestWager);
/*     */   }
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\wagers\WagerManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */