/*     */ package water.of.cup.boardgames.game;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import net.md_5.bungee.api.ChatMessageType;
/*     */ import net.md_5.bungee.api.chat.TextComponent;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.scheduler.BukkitRunnable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Clock
/*     */   extends BukkitRunnable
/*     */ {
/*     */   private Game game;
/*     */   private HashMap<GamePlayer, Double> playerTimes;
/*     */   private double lastTimeChange;
/*     */   private int increment;
/*     */   private boolean timer;
/*     */   private GamePlayer turn;
/*     */   private boolean sendAllTimes;
/*     */   
/*     */   public Clock(int paramInt, Game paramGame, boolean paramBoolean) {
/*  25 */     this.sendAllTimes = paramBoolean;
/*  26 */     this.playerTimes = new HashMap<>();
/*  27 */     for (GamePlayer gamePlayer : paramGame.getGamePlayers()) {
/*  28 */       this.playerTimes.put(gamePlayer, Double.valueOf(paramInt));
/*     */     }
/*  30 */     this.game = paramGame;
/*  31 */     this.turn = paramGame.getTurn();
/*  32 */     this.lastTimeChange = (System.currentTimeMillis() / 1000L);
/*     */     
/*  34 */     this.increment = 0;
/*  35 */     this.timer = false;
/*     */   }
/*     */   
/*     */   public void setIncrement(int paramInt) {
/*  39 */     this.increment = paramInt;
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
/*     */   public void run() {
/*  53 */     double d = (System.currentTimeMillis() / 1000L) - this.lastTimeChange;
/*  54 */     this.lastTimeChange = (System.currentTimeMillis() / 1000L);
/*     */     
/*  56 */     if (this.timer) {
/*  57 */       d *= -1.0D;
/*     */     }
/*  59 */     if (this.game.getTurn() != this.turn)
/*     */     {
/*  61 */       this.playerTimes.put(this.turn, Double.valueOf(((Double)this.playerTimes.get(this.turn)).doubleValue() + this.increment));
/*     */     }
/*     */ 
/*     */     
/*  65 */     this.turn = this.game.getTurn();
/*  66 */     this.playerTimes.put(this.turn, Double.valueOf(((Double)this.playerTimes.get(this.turn)).doubleValue() - d));
/*     */ 
/*     */     
/*  69 */     if (((Double)this.playerTimes.get(this.turn)).doubleValue() < 0.0D) {
/*     */       
/*  71 */       this.game.clockOutOfTime();
/*  72 */       this.game.gamePlayerOutOfTime(this.turn);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*  77 */     sendPlayersClockTimes();
/*     */   }
/*     */   
/*     */   public void sendPlayersClockTimes() {
/*  81 */     if (!this.sendAllTimes) {
/*  82 */       GamePlayer gamePlayer = this.game.getTurn();
/*  83 */       String str1 = (int)(((Double)this.playerTimes.get(gamePlayer)).doubleValue() / 60.0D) + ":" + (int)(((Double)this.playerTimes.get(gamePlayer)).doubleValue() % 60.0D);
/*  84 */       gamePlayer.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, 
/*  85 */           TextComponent.fromLegacyText(ChatColor.YELLOW + str1));
/*     */       
/*     */       return;
/*     */     } 
/*  89 */     String str = "";
/*  90 */     for (GamePlayer gamePlayer : this.game.getGamePlayers())
/*     */     {
/*  92 */       str = str + gamePlayer.getPlayer().getName() + ": " + (int)(((Double)this.playerTimes.get(gamePlayer)).doubleValue() / 60.0D) + ":" + (int)(((Double)this.playerTimes.get(gamePlayer)).doubleValue() % 60.0D) + " | ";
/*     */     }
/*     */     
/*  95 */     str = str.substring(0, str.length() - 2);
/*     */     
/*  97 */     for (GamePlayer gamePlayer : this.game.getGamePlayers()) {
/*  98 */       gamePlayer.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, 
/*  99 */           TextComponent.fromLegacyText(ChatColor.YELLOW + str));
/*     */     }
/*     */   }
/*     */   
/*     */   public void incementTime(GamePlayer paramGamePlayer, double paramDouble) {
/* 104 */     this.playerTimes.put(paramGamePlayer, Double.valueOf(((Double)this.playerTimes.get(paramGamePlayer)).doubleValue() + paramDouble));
/*     */   }
/*     */   
/*     */   public HashMap<GamePlayer, Double> getPlayerTimes() {
/* 108 */     return this.playerTimes;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTimer(boolean paramBoolean) {
/* 113 */     this.timer = paramBoolean;
/*     */   }
/*     */   
/*     */   public boolean getTimer() {
/* 117 */     return this.timer;
/*     */   }
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\Clock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */